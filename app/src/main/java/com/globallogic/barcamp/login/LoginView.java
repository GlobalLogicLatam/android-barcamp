package com.globallogic.barcamp.login;

import android.app.Activity;

import com.facebook.FacebookException;
import com.globallogic.barcamp.BaseView;

/**
 * Created by Gonzalo.Martin on 9/27/2016
 */
public interface LoginView extends BaseView {

    void startMainActivity();

    void showWaitDialog();

    void dismissWaitDialog();

    void setEmailViewError(String message);

    void showPasswordField();

    void hidePasswordField();

    void setPasswordViewError(String message);

    void showToast(String message);

    void hideFacebookButton();

    void showFacebookButton();

    void showFacebookError(FacebookException error);

    Activity getCurrentActivity();
}
