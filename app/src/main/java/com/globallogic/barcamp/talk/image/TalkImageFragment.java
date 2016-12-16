package com.globallogic.barcamp.talk.image;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.globallogic.barcamp.BaseFragment;
import com.globallogic.barcamp.R;
import com.globallogic.barcamp.loader.ImageLoader;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Gonzalo.Martin on 11/7/2016
 */

public class TalkImageFragment extends BaseFragment<TalkImagePresenter> implements TalkImageView {

    private ImageView talkImage;
    private String imageUrl;
    private String name;
    private ProgressDialog progressDialog;

    public static TalkImageFragment newInstance(String name, String imageUrl) {
        TalkImageFragment talkImageFragment = new TalkImageFragment();
        talkImageFragment.setName(name);
        talkImageFragment.setImageUrl(imageUrl);
        return talkImageFragment;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_talk_image;
    }

    @Override
    protected TalkImagePresenter buildPresenter() {
        TalkImagePresenter talkImagePresenter = new TalkImagePresenter(this);
        talkImagePresenter.setImageAttributes(imageUrl);
        return talkImagePresenter;
    }

    @Override
    protected void initView(View view) {
        talkImage = (ImageView) view.findViewById(R.id.talk_image);
    }

    private void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public void loadImage(String finalUrl) {
        ImageLoader.loadInBackground(getContext(), finalUrl, R.drawable.ic_talk_image_placeholder, presenter);
    }

    @Override
    public void setTitle() {
        setToolbarTitle(name);
    }

    @Override
    public void showLoading() {
        progressDialog = ProgressDialog.show(getContext(), "", "Cargando", true);
    }

    @Override
    public void dismissLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void setLoadedImage(final Bitmap bitmap) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                talkImage.setImageBitmap(bitmap);
                new PhotoViewAttacher(talkImage);
            }
        });
    }

    private void setName(String name) {
        this.name = name;
    }
}
