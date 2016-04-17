package com.xoteev.ya_tst;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class Adapter extends BaseAdapter {

    private final LayoutInflater mInflater;
    private LineElement[] mlineElement;
    private final Context mContext;


    public Adapter(Context context, LineElement[] lineElements ) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mlineElement = lineElements;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mlineElement == null ? 0 : mlineElement.length;
    }

    @Override
    public Object getItem(int position) {
        return mlineElement[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view = convertView != null ? convertView
                : mInflater.inflate(R.layout.line_item, parent, false);

        final LineElement LineElement = mlineElement[position];
        if (LineElement == null)
            return view;

        final TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvTitle.setText(LineElement.Title);

        final TextView tvStyle = (TextView) view.findViewById(R.id.tvStyle);
        tvStyle.setText(LineElement.Style);

        String strSong = String.format("%d %s, %d %s",
                LineElement.CountAlbums,
                FormattedStr(LineElement.CountAlbums, "альбом", "альбома", "альбомов"),
                LineElement.CountSongs,
                FormattedStr(LineElement.CountSongs, "песня", "песни", "песен"));

        final TextView tvCountSongs = (TextView) view.findViewById(R.id.tvCountSong);
        tvCountSongs.setText(strSong);

        final ImageView ivSmall = (ImageView) view.findViewById(R.id.ivImageSmall);
        Picasso.with(mContext)
                .load(LineElement.ImageUrlSmall)
                .error(R.drawable.ic_sentiment_very_dissatisfied_black_48dp)
                .into(ivSmall);

        return view;
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
