package trainee_piurko.prospektdev.com;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String PUT_URL = "put url";
    private static final String PUT_TITLE = "put title";
    private static final String PUT_ID = "put id";


    private RecyclerView itemRecyclerView;
    private List<AppItem> mItems = new ArrayList<>();
    private boolean isOnline = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemRecyclerView = (RecyclerView) findViewById(R.id.treinee_app_recycler_view);
        itemRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        new FetchItemTask().execute();

        setudAdapter();
        if ( !isOnline(this) ){
            Toast.makeText(getApplicationContext(),
                    "Нет соединения с интернетом!",Toast.LENGTH_LONG).show();
            isOnline = false;
        }
        else {
            isOnline = true;
        }
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
                Glide.with(MainActivity.this).load(appItem.getUrl()).into(holder.itemImageViev);
            }
            
            holder.itemImageViev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainActivity.this,DetailsActivity.class);
                    i.putExtra(PUT_URL, appItem.getUrlNormal());
                    i.putExtra(PUT_TITLE, appItem.getCaption());
                    i.putExtra(PUT_ID, appItem.getId());
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
                return list;
            }

        }

        @Override
        protected void onPostExecute(List<AppItem> appItems) {
            mItems = appItems;
            setudAdapter();
        }
    }
}
