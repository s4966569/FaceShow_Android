package com.yanxiu.gphone.faceshow.util.nineGridView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lzy.ninegrid.NineGridView;
import com.yanxiu.gphone.faceshow.R;

/**
 * @author frc on 2018/1/15.
 */

public class GlideNineImageLoader implements NineGridView.ImageLoader {
    @Override
    public void onDisplayImage(Context context, ImageView imageView, String url) {
        Glide.with(context).load(url).fitCenter().placeholder(R.drawable.ic_default_color).error(R.drawable.net_error_picture).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);

    }

    @Override
    public Bitmap getCacheImage(String url) {
        return null;
    }
}
