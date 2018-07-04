package trainee_piurko.prospektdev.com;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String PUT_URL = "put url";
    private static final String PUT_TITLE = "put title";
    private static final String PUT_ID = "put id";
    private static final String PUT_DIR_URL = "put dir url";


    private RecyclerView itemRecyclerView;
    private List<AppItem> mItems = new ArrayList<>();
    private boolean isOnline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemRecyclerView = (RecyclerView) findViewById(R.id.treinee_app_recycler_view);
        itemRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        if ( !isOnline(this) ){
            Toast.makeText(getApplicationContext(),
                    "Нет соединения с интернетом!",Toast.LENGTH_LONG).show();
            isOnline = false;
        }
        else { isOnline = true; }

        new FetchItemTask().execute();

        setudAdapter();
    }

    private void setudAdapter(){
        itemRecyclerView.setAdapter(new ItemAdapter(mItems));
    }

    public static boolean isOnline(Context context)
    {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {
            return true;
        }
        return false;
    }

    private class ItemHolder extends RecyclerView.ViewHolder{

        private ImageView itemImageViev;

        public ItemHolder(View itemView) {
            super(itemView);
            itemImageViev = (ImageView) itemView.findViewById(R.id.treinee_app_image_view);

        }
    }

    private class ItemAdapter extends RecyclerView.Adapter<ItemHolder>{
        private List<AppItem> mAppItems;

        public ItemAdapter(List<AppItem> items){
            mAppItems = items;
        }

        @NonNull
        @Override
        public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
            View v = inflater.inflate(R.layout.app_item, parent,false);
            return new ItemHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
            final AppItem appItem = mAppItems.get(position);
            if(isOnline) {
                Glide.with(MainActivity.this)
                        .load(appItem.getUrl())
                        .into(holder.itemImageViev);
                Glide.with(MainActivity.this)
                        .asBitmap()
                        .load(appItem.getUrl())
                        .into(new SimpleTarget<Bitmap>(100, 100) {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                appItem.setDirUrl(saveImage(resource, appItem.getId()));
                            }
                        });
            }else {
                Glide.with(MainActivity.this)
                        .load(appItem.getDirUrl())
                        .into(holder.itemImageViev);
                Log.i(TAG,appItem.getDirUrl());
            }

            holder.itemImageViev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainActivity.this,DetailsActivity.class);
                    i.putExtra(PUT_URL, appItem.getUrlNormal());
                    i.putExtra(PUT_TITLE, appItem.getCaption());
                    i.putExtra(PUT_ID, appItem.getId());
                    i.putExtra(PUT_DIR_URL, appItem.getDirUrl());
                    startActivity(i);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mAppItems.size();
        }
    }


    private class FetchItemTask extends AsyncTask<Void,Void, List<AppItem>>{

        @Override
        protected List<AppItem> doInBackground(Void... voids) {
            if(isOnline) {
                List<AppItem> list = new PinterestExplorer().feachItems();
                DBAssistant.get(MainActivity.this).addAppItem(list);
                return list;
            }else {
                List<AppItem> list = DBAssistant.get(MainActivity.this).getAppItems();
                Log.i(TAG,"Загрузка данных из бд");
                return list;
            }

        }

        @Override
        protected void onPostExecute(List<AppItem> appItems) {
            mItems = appItems;
            setudAdapter();
        }
    }

    private String saveImage(Bitmap image, String id) {
        String savedImagePath = null;

        String imageFileName = "JPEG_"+ id + ".jpg";
        File storageDir = new File(            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + "/TreineePiurko");
        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }
        if (success) {
            File imageFile = new File(storageDir, imageFileName);
            savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Add the image to the system gallery
            galleryAddPic(savedImagePath);
        }
        return savedImagePath;
    }

    private void galleryAddPic(String imagePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }
}
