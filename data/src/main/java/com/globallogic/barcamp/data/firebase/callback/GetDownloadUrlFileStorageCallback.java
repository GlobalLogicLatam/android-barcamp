package com.globallogic.barcamp.data.firebase.callback;

import android.net.Uri;

/**
 * Created by Gonzalo.Martin on 11/6/2016
 */
public interface GetDownloadUrlFileStorageCallback {
    void onGetDownloadUrlFileSuccess(Uri uri);

    void onGetDownloadUrlFileFailure(Throwable error);
}