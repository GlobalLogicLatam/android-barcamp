package com.globallogic.barcamp.data.firebase.callback;

/**
 * Created by Gonzalo.Martin on 10/13/2016
 */

public interface FirebaseAuthCallback {
    void onAuthSuccess();

    void onAuthFailure(Throwable error);
}
