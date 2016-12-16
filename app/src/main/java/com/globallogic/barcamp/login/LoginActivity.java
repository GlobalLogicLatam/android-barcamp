package com.globallogic.barcamp.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.widget.AppCompatEditText;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookException;
import com.facebook.login.widget.LoginButton;
import com.globallogic.barcamp.BaseActivity;
import com.globallogic.barcamp.R;
import com.globallogic.barcamp.home.MainActivity;

import java.util.Arrays;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginView {

    // UI references.
    private AppCompatEditText mEmailView;
    private AppCompatEditText mPasswordView;
    private View mEmailSignInButton;
    private LoginButton fbLogin;
    private CallbackManager callbackManager;
    private ProgressDialog pDialog;
    private View loginSeparator;

    @Override
    protected LoginPresenter buildPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_login;
    }

    @Override
    protected int getRootLayoutId() {
        return R.id.root_login;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void showWaitDialog() {
        if (pDialog == null || !pDialog.isShowing()) {
            pDialog = new ProgressDialog(this);
            pDialog.setMessage(getString(R.string.wait));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
    }

    @Override
    public void dismissWaitDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    @Override
    public void setEmailViewError(String message) {
        mEmailView.setError(message);
        mEmailView.requestFocus();
    }

    @Override
    protected void init() {
        mEmailView = (AppCompatEditText) findViewById(R.id.email);
        mPasswordView = (AppCompatEditText) findViewById(R.id.password);
        mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        fbLogin = (LoginButton) findViewById(R.id.facebook_login_button);
        callbackManager = CallbackManager.Factory.create();
        loginSeparator = findViewById(R.id.login_separator);
    }

    @Override
    protected void setControls() {
        // set editor action listener for email view
        mEmailView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int id, KeyEvent event) {
                if (id == EditorInfo.IME_ACTION_DONE) {
                    attemptLogin(false);
                    return true;
                }
                return false;
            }
        });

        // set editor action listener for password view
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE) {
                    attemptLogin(true);
                    return true;
                }
                return false;
            }
        });

        // set click listener for sign in button
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean usingPassword = mPasswordView.getVisibility() == View.VISIBLE;
                attemptLogin(usingPassword);
            }
        });

        fbLogin.setReadPermissions(Arrays.asList("public_profile, email"));
        // remove facebook logo
        fbLogin.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        fbLogin.registerCallback(callbackManager, presenter);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     *
     * @param usingPassword
     */
    private void attemptLogin(boolean usingPassword) {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        if (usingPassword) {
            presenter.validatePassword(email, password);
        } else {
            presenter.validate(email);
        }

    }

    public void hidePasswordField() {
        mPasswordView.setVisibility(View.GONE);
        mEmailView.requestFocus();
    }

    @Override
    public void setPasswordViewError(String message) {
        mPasswordView.setError(message);
        mPasswordView.requestFocus();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideFacebookButton() {
        loginSeparator.setVisibility(View.GONE);
        fbLogin.setVisibility(View.GONE);
    }

    @Override
    public void showFacebookButton() {
        loginSeparator.setVisibility(View.VISIBLE);
        fbLogin.setVisibility(View.VISIBLE);
    }

    @Override
    public void showFacebookError(FacebookException error) {
        showToast(error.getMessage());
    }

    public void showPasswordField() {
        mPasswordView.setVisibility(View.VISIBLE);
        mPasswordView.requestFocus();
    }

    @Override
    public Activity getCurrentActivity() {
        return this;
    }
}

