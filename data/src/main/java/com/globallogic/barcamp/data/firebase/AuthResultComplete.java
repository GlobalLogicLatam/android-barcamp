package com.globallogic.barcamp.data.firebase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.globallogic.barcamp.data.firebase.callback.FirebaseAuthCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

/**
 * Created by Gonzalo.Martin on 10/14/2016
 */

public class AuthResultComplete implements OnCompleteListener<AuthResult> {

    private static final String TAG = AuthResultComplete.class.getSimpleName();

    private FirebaseAuthCallback authCallback;

    public AuthResultComplete(FirebaseAuthCallback authCallback) {
        this.authCallback = authCallback;
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        Log.d(TAG, "signInAnonymously:onComplete:" + task.isSuccessful());
        if (task.isSuccessful()) {
            authCallback.onAuthSuccess();
        } else {
            authCallback.onAuthFailure(task.getException());
        }
    }
}
