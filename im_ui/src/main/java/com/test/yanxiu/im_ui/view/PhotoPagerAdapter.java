package com.test.yanxiu.im_ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.test.yanxiu.common_base.utils.SharedSingleton;
import com.test.yanxiu.im_ui.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/6/23 14:26.
 * Function :
 */
public class PhotoPagerAdapter extends PagerAdapter {

    private Context mContext;

    private List<String> mPaths = new ArrayList<>();
    private List<ZoomImageView> mImageViews = new ArrayList<>();

    public PhotoPagerAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<String> paths, List<ZoomImageView> images) {
        if (paths == null || images == null || paths.size() != images.size()) {
            return;
        }
        this.mImageViews.clear();
        this.mPaths.clear();
        this.mImageViews.addAll(images);
        this.mPaths.addAll(paths);
        this.notifyDataSetChanged();
    }

    public List<ZoomImageView> getImageViews() {
        return this.mImageViews;
    }

    public void deleteItem(int position) {
        if (position > -1 && position < mPaths.size()) {
            this.mPaths.remove(position);
            this.mImageViews.remove(position);
            this.notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mImageViews != null ? mImageViews.size() : 0;
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final ZoomImageView imageView = mImageViews.get(position);
        String path=mPaths.get(position);
        Bitmap cache=SharedSingleton.getInstance().getCachedBitmap(path);
        if (cache != null) {
            imageView.setImageBitmap(cache);
        }else{
//            imageView.setImageResource(R.drawable.bg_im_pic_holder_view);
        }
        Glide.with(mContext)
                .load(path)
                .asBitmap()
                .error(R.drawable.bg_im_pic_holder_view)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        imageView.setImageBitmap(resource);
                    }
                });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickPhotoListener != null) {
                    mOnClickPhotoListener.onClick(v, position);
                }
            }
        });
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    onClickPhotoListener mOnClickPhotoListener;

    public void setOnClickPhotoListener(onClickPhotoListener iOnClickPhotoListener) {
        mOnClickPhotoListener = iOnClickPhotoListener;
    }

    public interface onClickPhotoListener {
        void onClick(View view, int position);
    }
}
