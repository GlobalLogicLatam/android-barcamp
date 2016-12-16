package com.globallogic.barcamp.login;

import android.text.TextUtils;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.globallogic.barcamp.BarcampApplication;
import com.globallogic.barcamp.BasePresenter;
import com.globallogic.barcamp.R;
import com.globallogic.barcamp.asyntask.UserDataFromFacebookTask;
import com.globallogic.barcamp.data.firebase.FirebaseWorker;
import com.globallogic.barcamp.data.firebase.callback.FirebaseAuthCallback;
import com.globallogic.barcamp.data.firebase.callback.UserDataCallback;
import com.globallogic.barcamp.utils.StringUtils;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

/**
 * Created by Gonzalo.Martin on 9/27/2016
 */
public class LoginPresenter extends BasePresenter<LoginView> implements UserDataFromFacebookTask.OnFacebookLoginListener, FacebookCallback<LoginResult> {

    private FirebaseWorker firebaseWorker = new FirebaseWorker();
    private String userEmail;

    private FirebaseAuthCallback firebaseLoginAuthCallback = new FirebaseAuthCallback() {
        @Override
        public void onAuthSuccess() {
            // it should not be success at first time
            onDismissWaitDialog();
        }

        @Override
        public void onAuthFailure(Throwable error) {
            if (error instanceof FirebaseAuthInvalidCredentialsException) {
                // it's admin user
                view.showPasswordField();
                view.hideFacebookButton();
                onDismissWaitDialog();
            } else if (error instanceof FirebaseAuthInvalidUserException) {
                // it isn't admin user
                view.showFacebookButton();
                view.hidePasswordField();
                loginAsAnonymous();
            }
        }
    };

    public LoginPresenter(LoginView view) {
        super(view);
    }

    @Override
    public void onCreate() {
        // check for pending login
        checkPendingLogin();
    }

    public void getUserDataFromFacebook(AccessToken accessToken) {
        new UserDataFromFacebookTask(this).execute(accessToken);
    }

    @Override
    public void onShowWaitDialog() {
        view.showWaitDialog();
    }

    @Override
    public void onDismissWaitDialog() {
        view.dismissWaitDialog();
    }

    @Override
    public void onFacebookLogin(String email, String fbId) {
        userEmail = email;
        onShowWaitDialog();

        // facebook logout
        LoginManager.getInstance().logOut();
        loginAsAnonymous();
    }

    // region Facebook
    @Override
    public void onSuccess(LoginResult loginResult) {
        getUserDataFromFacebook(loginResult.getAccessToken());
    }

    @Override
    public void onCancel() {
        // Do nothing for now..
    }

    @Override
    public void onError(FacebookException error) {
        view.showFacebookError(error);
    }
    // endRegion

    /**
     * Validates the provided email
     *
     * @param email
     */
    public void validate(String email) {
        onShowWaitDialog();

        boolean cancel = false;

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            view.setEmailViewError(BarcampApplication.get().getString(R.string.error_field_required));
            cancel = true;
        } else if (!isEmailValid(email)) {
            view.setEmailViewError(BarcampApplication.get().getString(R.string.error_invalid_email));
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            onDismissWaitDialog();
            view.showToast(BarcampApplication.get().getString(R.string.invalid_login));
        } else {
            //All validations passed
            userEmail = email;
            firebaseWorker.loginFirebase(view.getCurrentActivity(),
                    firebaseLoginAuthCallback, email, view.getCurrentActivity().getString(R.string.dummy_pass));
        }
    }

    /**
     * Validates the provided email and password
     *
     * @param email
     * @param password
     */
    public void validatePassword(final String email, String password) {
        if (password.isEmpty()) {
            view.setPasswordViewError(BarcampApplication.get().getString(R.string.error_password_required));
        } else {
            // check admin user
            onShowWaitDialog();
            firebaseWorker.loginFirebase(view.getCurrentActivity(), new FirebaseAuthCallback() {
                @Override
                public void onAuthSuccess() {
                    loginSuccess();
                }

                @Override
                public void onAuthFailure(Throwable error) {
                    onDismissWaitDialog();
                    if (error instanceof FirebaseAuthInvalidCredentialsException) {
                        view.setPasswordViewError(BarcampApplication.get().getString(R.string.error_invalid_password));
                    }
                }
            }, email, password);
        }
    }

    private void checkPendingLogin() {
        // Sometimes, the firebase session expires, so we need to log in again
        String savedEmail = BarcampApplication.get().getSavedEmail();
        if (savedEmail != null && !savedEmail.isEmpty()) {
            validate(savedEmail);
        }
    }

    private void loginSuccess() {
        view.startMainActivity();
    }

    private boolean isEmailValid(String email) {
        return StringUtils.isEmailValid(email);
    }

    private void loginAsAnonymous() {
        firebaseWorker.loginFirebase(view.getCurrentActivity(), new FirebaseAuthCallback() {
            @Override
            public void onAuthSuccess() {
                // save email in firebase
                saveEmail();
            }

            @Override
            public void onAuthFailure(Throwable error) {
                onDismissWaitDialog();
                Log.e(TAG, "Error login as anonymous", error);
            }
        });
    }

    /**
     * Stores the entered email in firebase
     */
    private void saveEmail() {
        firebaseWorker.addGuestEmail(userEmail, new UserDataCallback() {
            @Override
            public void onUserDataSaveCompleted() {
                BarcampApplication.get().setUserEmail(userEmail);
                firebaseWorker.removeEmailEventListener();
                onDismissWaitDialog();
                loginSuccess();
            }
        });
    }


}
