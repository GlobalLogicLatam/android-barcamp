package com.globallogic.barcamp.loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

/**
 * Created by Gonzalo.Martin on 10/18/2016
 */
public class ImageLoader {

    public interface ImageLoaderCallback {
        void onImageLoaded(Bitmap bitmap);
    }

    public static void load(Context context, String url, ImageView imageView) {
        if (url != null && !url.isEmpty()) {
            Glide.with(context).load(url).into(imageView);
        }
    }

    public static void load(Context context, String url, ImageView imageView, int placeholderResource) {
        if (url != null && !url.isEmpty()) {
            Glide.with(context).load(url).placeholder(placeholderResource).into(imageView);
        }
    }

    public static void loadInBackground(Context context, String url, int placeholderResource, final ImageLoaderCallback callback) {
        if (url != null && !url.isEmpty()) {
            Glide.with(context).load(url).asBitmap().placeholder(placeholderResource).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    callback.onImageLoaded(resource);
                }
            });
        }
    }
}
