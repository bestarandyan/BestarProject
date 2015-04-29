package com.manyi.mall.widget.GalleryWidget;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.view.View;
import android.widget.ImageView;

import com.manyi.mall.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;


/**
 * Created by Ted on 14-7-2.
 */
public final class ImageLoaderConfig {

    public static final DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.head)// 设置图片在下载期间显示的图片
            .showImageForEmptyUri(R.drawable.head)// 设置图片Uri为空或是错误的时候显示的图片
            .showImageOnFail(R.drawable.head)// 设置图片加载/解码过程中错误时候显示的图片
            .cacheInMemory(false)// 是否缓存都內存中
            .cacheOnDisc(false)// 是否缓存到sd卡上
            .considerExifParams(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .displayer(new SimpleBitmapDisplayer()).build();

    public static final DisplayImageOptions options_small = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.head)// 设置图片在下载期间显示的图片
            .showImageForEmptyUri(R.drawable.head)// 设置图片Uri为空或是错误的时候显示的图片
            .showImageOnFail(R.drawable.head)// 设置图片加载/解码过程中错误时候显示的图片
            .cacheInMemory(false)// 是否缓存都內存中
            .cacheOnDisc(true)// 是否缓存到sd卡上
            .considerExifParams(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .displayer(new SimpleBitmapDisplayer()).build();

    public static final DisplayImageOptions options_rotate = new DisplayImageOptions.Builder()
            .cacheInMemory(false)// 是否缓存都內存中
            .cacheOnDisk(false)// 是否缓存到sd卡上
            .considerExifParams(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .displayer(new SimpleBitmapDisplayer()).build();

    public static final DisplayImageOptions options_agent = new DisplayImageOptions.Builder()
            .cacheInMemory(true)// 是否缓存都內存中
            .cacheOnDisk(true)// 是否缓存到sd卡上
            .considerExifParams(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .displayer(new SimpleBitmapDisplayer()).build();


    public static final ImageLoadingListener img_load_listener = new SimpleImageLoadingListener() {
        @Override
        public void onLoadingStarted(String imageUri, View view) {
            if (view != null) {
                ((ImageView) view).setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            if (view != null) {
                ((ImageView) view).setScaleType(ImageView.ScaleType.CENTER_CROP);
                if (view instanceof LoadProgressImageView) {//防止进度条不消失
                    ((LoadProgressImageView) view).setCurrentProgress(100);
                }
            }
        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (view != null) {
                ((ImageView) view).setScaleType(ImageView.ScaleType.CENTER_CROP);
                TransitionDrawable oldTransitionDrawable = new TransitionDrawable(new Drawable[]{
                        new ColorDrawable(android.R.color.transparent), new BitmapDrawable(loadedImage)});
                oldTransitionDrawable.setCrossFadeEnabled(true);
                ((ImageView) view).setImageDrawable(oldTransitionDrawable);
                oldTransitionDrawable.startTransition(300);
                if (view instanceof LoadProgressImageView) {//防止进度条不消失
                    ((LoadProgressImageView) view).setCurrentProgress(100);
                }
            }
        }
    };

    public static final ImageLoadingProgressListener imgProgressListener = new ImageLoadingProgressListener() {
        @Override
        public void onProgressUpdate(String imageUri, View view, int current, int total) {
            if (view instanceof LoadProgressImageView) {
                ((LoadProgressImageView) view).setCurrentProgress(((float) current / total) * 100);
            }
        }
    };
}
