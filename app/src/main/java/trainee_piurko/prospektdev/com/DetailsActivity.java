package trainee_piurko.prospektdev.com;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DetailsActivity extends AppCompatActivity {

    private static final String TAG = "DetailsActivity";

    private AppItem mAppItem = new AppItem();
    private ImageView mImageView;
    private TextView mTextView;
    private Button shareButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent intent = getIntent();

        mAppItem.setUrl(intent.getStringExtra("put url"));
        mAppItem.setCaption(intent.getStringExtra("put title"));
        mAppItem.setId(intent.getStringExtra("put id"));
        mAppItem.setDirUrl(intent.getStringExtra("put dir url"));


        mImageView = (ImageView) findViewById(R.id.details_image_view);
        if(MainActivity.isOnline(DetailsActivity.this)) {
            Glide.with(DetailsActivity.this).load(mAppItem.getUrl()).into(mImageView);
        }else
            Glide.with(DetailsActivity.this).load(mAppItem.getUrl()).into(mImageView);

        mTextView = (TextView) findViewById(R.id.details_text_view);
        mTextView.setText("Id: "+mAppItem.getId()+"\n"+"Name: "+mAppItem.getCaption());

        shareButton = (Button) findViewById(R.id.share_button);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri address = Uri.parse(mAppItem.getUrl());
                Intent openlinkIntent = new Intent(Intent.ACTION_SEND);
                openlinkIntent.setType("text/plain");
                openlinkIntent.putExtra(Intent.EXTRA_TEXT, mAppItem.getUrl());
                openlinkIntent = Intent.createChooser(openlinkIntent,getString(R.string.send_url));
                startActivity(openlinkIntent);
            }
        });

    }
}
