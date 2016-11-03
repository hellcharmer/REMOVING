package com.example.charmer.moving.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.example.charmer.moving.R;

import io.rong.imageloader.core.DisplayImageOptions;
import io.rong.imageloader.core.ImageLoader;
import io.rong.imageloader.core.listener.ImageLoadingListener;


/**
 * Created by HMY
 */
public class ImageLoaderUtil {

    public static ImageLoader getImageLoader(Context context) {
        return ImageLoader.getInstance();
    }

    public static DisplayImageOptions getPhotoImageOption() {
        Integer extra = 1;
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                .showImageForEmptyUri(R.drawable.banner_default).showImageOnFail(R.drawable.banner_default)
                .showImageOnLoading(R.drawable.banner_default)
                .extraForDownloader(extra)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        return options;
    }

    public static void displayImage(Context context, ImageView imageView, String url, DisplayImageOptions options, com.nostra13.universalimageloader.core.listener.ImageLoadingListener imageLoadingListener) {
        getImageLoader(context).displayImage(url, imageView, options);
    }

    public static void displayImage(Context context, ImageView imageView, String url, DisplayImageOptions options, ImageLoadingListener listener) {
        getImageLoader(context).displayImage(url, imageView, options, listener);
    }
}
