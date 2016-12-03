package com.play.zv.seamountain.view;

import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;


import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.play.zv.seamountain.R;
import com.play.zv.seamountain.adapter.GrilAdapter;
import com.squareup.picasso.Picasso;




public class PictureActivity extends AppCompatActivity {
    public static final String EXTRA_IMAGE_URL = "image_url";
    private String mImageUrl;
    public static final String TRANSIT_PIC = "picture";
    private PhotoView picture;


    private void parseIntent() {
        mImageUrl = getIntent().getStringExtra(EXTRA_IMAGE_URL);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        parseIntent();

        picture = (PhotoView) findViewById(R.id.img);

        //Picasso.with(this).load(mImageUrl).into(picture);
        Glide.with(this)
                .load(mImageUrl)
                .into(picture);

        picture.enable();
        //picture.setAdjustViewBounds(true);
        //mPhotoViewAttacher = new PhotoViewAttacher(picture);
    }


}