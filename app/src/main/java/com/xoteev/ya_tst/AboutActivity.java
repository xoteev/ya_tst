package com.xoteev.ya_tst;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Intent intent = getIntent();

        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        assert tb != null;
        tb.setTitle(intent.getStringExtra("Title"));
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        final ImageView ivBig = (ImageView) findViewById(R.id.ivImageBig);

        Picasso.with(this)
                .load(intent.getStringExtra("ImageBig"))
                .placeholder(R.drawable.ic_download_cover)
                .error(R.drawable.ic_download_error)
                .into(ivBig);

        final TextView tvStyleAbout = (TextView) findViewById(R.id.tvStyle_about);
        assert tvStyleAbout != null;
        tvStyleAbout.setText(intent.getStringExtra("Style_about"));

        final TextView tvCountSongs_about = (TextView) findViewById(R.id.tvCountSongs_about);
        String strSong = String.format("%d %s   %s   %d %s",
                intent.getIntExtra("CountAlbums", 0),
                FormattedStr(intent.getIntExtra("CountAlbums", 0), "альбом", "альбома", "альбомов"),
                "\u2219",
                intent.getIntExtra("CountSongs", 0),
                FormattedStr(intent.getIntExtra("CountSongs", 0), "песня", "песни", "песен"));

        assert tvCountSongs_about != null;
        tvCountSongs_about.setText(strSong);

        final TextView tvAbout = (TextView) findViewById(R.id.tvAbout);
        assert tvAbout != null;
        tvAbout.setText(intent.getStringExtra("About"));
    }

    // "альбом", "альбома", "альбомов"
    public String FormattedStr(int nCount, String text1, String text2, String text3) {
        String strEnd = "";
        int b = nCount % 100;
        if (b > 10 && b < 20)
            strEnd = text3;
        else {
            b = b % 10;
            switch (b) {
                case 1: {
                    strEnd = text1;
                    break;
                }
                case 2:
                case 3:
                case 4: {
                    strEnd = text2;
                    break;
                }
                case 0:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:{
                    strEnd = text3;
                    break;
                }
            }
        }
        return strEnd;
    }
}