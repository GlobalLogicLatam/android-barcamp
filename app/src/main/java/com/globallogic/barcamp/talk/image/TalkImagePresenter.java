package com.globallogic.barcamp.talk.image;

import android.graphics.Bitmap;

import com.globallogic.barcamp.BarcampApplication;
import com.globallogic.barcamp.BasePresenter;
import com.globallogic.barcamp.loader.ImageLoader;

/**
 * Created by Gonzalo.Martin on 11/7/2016
 */
public class TalkImagePresenter extends BasePresenter<TalkImageView> implements ImageLoader.ImageLoaderCallback {
    private String imageUrl;

    public TalkImagePresenter(TalkImageView view) {
        super(view);
    }

    @Override
    public void onViewCreated() {
        view.setTitle();
        if (BarcampApplication.get().containsUrl(imageUrl)) {
            view.showLoading();
            view.loadImage(BarcampApplication.get().getFinalUrl(imageUrl));
        }
    }

    public void setImageAttributes(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public void onImageLoaded(Bitmap bitmap) {
        view.dismissLoading();
        view.setLoadedImage(bitmap);
    }
}
