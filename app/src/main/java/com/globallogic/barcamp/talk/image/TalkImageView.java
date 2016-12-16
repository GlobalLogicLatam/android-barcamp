package com.globallogic.barcamp.talk.image;

import android.graphics.Bitmap;

import com.globallogic.barcamp.BaseView;

/**
 * Created by Gonzalo.Martin on 11/7/2016
 */
public interface TalkImageView extends BaseView {
    void loadImage(String finalUrl);

    void setTitle();

    void showLoading();

    void dismissLoading();

    void setLoadedImage(Bitmap bitmap);
}
