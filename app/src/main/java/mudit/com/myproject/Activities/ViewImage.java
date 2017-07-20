package mudit.com.myproject.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import mudit.com.myproject.R;

public class ViewImage extends AppCompatActivity {
    ImageView imgViewImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewimage);
        imgViewImage=(ImageView)findViewById(R.id.imgViewImage);
        Intent intent=getIntent();
        String uri=intent.getStringExtra("uri");
        Picasso.with(this).load(uri).fit().centerInside().into(imgViewImage);
    }
}
