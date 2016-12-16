package com.globallogic.barcamp.splash;

import android.content.Intent;

import com.globallogic.barcamp.BaseActivity;
import com.globallogic.barcamp.R;
import com.globallogic.barcamp.home.MainActivity;
import com.globallogic.barcamp.login.LoginActivity;

/**
 * Created by Gonzalo.Martin on 10/28/2016
 */

public class SplashActivity extends BaseActivity<SplashPresenter> implements SplashView {

    @Override
    protected SplashPresenter buildPresenter() {
        return new SplashPresenter(this);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_splash;
    }

    @Override
    protected int getRootLayoutId() {
        return R.id.root_splash;
    }

    @Override
    protected void setControls() {
        // Do nothing..
    }

    @Override
    protected void init() {
        // Do nothing..
    }

    @Override
    protected boolean shouldCheckInternetConnection() {
        return false;
    }

    @Override
    public void openMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void openLoginActivity() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
