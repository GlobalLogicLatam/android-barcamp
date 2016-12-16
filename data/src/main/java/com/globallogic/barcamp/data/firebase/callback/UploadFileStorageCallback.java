package com.globallogic.barcamp.data.firebase.callback;

/**
 * Created by Gonzalo.Martin on 10/18/2016
 */
public interface UploadFileStorageCallback {
    void onFailureUploadFile(Throwable error);

    void onFinishUploadFile(String urlFile);
}
