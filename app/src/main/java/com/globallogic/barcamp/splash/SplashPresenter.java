package com.globallogic.barcamp.splash;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.globallogic.barcamp.BasePresenter;
import com.globallogic.barcamp.data.firebase.FirebaseWorker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Gonzalo.Martin on 10/28/2016
 */
public class SplashPresenter extends BasePresenter<SplashView> {

    private static final long SPLASH_TIME = 3000;

    private FirebaseWorker firebaseWorker = new FirebaseWorker();

    private boolean loggedIn = false;
    private boolean authStateChanged = false;

    private FirebaseAuth.AuthStateListener mAuthStateListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            if (!authStateChanged) {
                authStateChanged = true;
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    loggedIn = true;
                    firebaseWorker.removeAuthListener(mAuthStateListener);
                }
            }
        }
    };

    public SplashPresenter(SplashView view) {
        super(view);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        firebaseWorker.addAuthListener(mAuthStateListener);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (authStateChanged) {
                    onAuthStateChanged();
                } else {
                    checkAuthState();
                }
            }
        }, SPLASH_TIME);
    }

    @Override
    public void onDestroy() {
        firebaseWorker.removeAuthListener(mAuthStateListener);
        super.onDestroy();
    }

    private void checkAuthState() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (authStateChanged) {
                    onAuthStateChanged();
                } else {
                    checkAuthState();
                }
            }
        }, 500);
    }

    private void onAuthStateChanged() {
        if (loggedIn) {
            view.openMainActivity();
        } else {
            view.openLoginActivity();
        }
    }
}
