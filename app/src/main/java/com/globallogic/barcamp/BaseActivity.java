package com.globallogic.barcamp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import com.globallogic.barcamp.offline.OfflineActivity;
import com.globallogic.barcamp.utils.DeviceUtils;

public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity {

    private BroadcastReceiver connectionChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (shouldCheckInternetConnection()) {
                checkInternetConnection();
            }
        }
    };

    protected T presenter;

    protected abstract T buildPresenter();

    protected abstract int getLayoutResource();

    protected abstract int getRootLayoutId();

    protected abstract void setControls();

    protected abstract void init();

    private boolean isKeyboardOpened = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = buildPresenter();
        presenter.onCreate();

        setContentView(getLayoutResource());

        addKeyboardListener();

        init();

        setControls();
    }

    private void addKeyboardListener() {
        final View rootView = findViewById(getRootLayoutId());
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                rootView.getWindowVisibleDisplayFrame(r);
                int screenHeight = rootView.getRootView().getHeight();

                // r.bottom is the position above soft keypad or device button.
                // if keypad is shown, the r.bottom is smaller than that before.
                int keypadHeight = screenHeight - r.bottom;

                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                    // keyboard is opened
                    isKeyboardOpened = true;
                } else {
                    // keyboard is closed
                    isKeyboardOpened = false;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(connectionChangedReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(connectionChangedReceiver);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (presenter.canBack()) {
            super.onBackPressed();
        }
    }

    public void setKeyboardVisibility(boolean visible) {
        View view = getCurrentFocus();
        if (visible) {
            if (view != null && !isKeyboardOpened) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInputFromInputMethod(view.getWindowToken(), 0);
            }
        } else {
            if (view != null && isKeyboardOpened) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    protected boolean shouldCheckInternetConnection() {
        return true;
    }

    public void restart() {
        BarcampApplication.get().clearTakenRooms();
        Intent restartIntent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
        restartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(restartIntent);
    }

    private void checkInternetConnection() {
        if (DeviceUtils.isNetworkAvailable(this)) {
            if (this instanceof OfflineActivity) {
                finish();
            }
        } else {
            displayOfflineActivity();
        }
    }

    private void displayOfflineActivity() {
        if (!(this instanceof OfflineActivity)) {
            startActivity(new Intent(this, OfflineActivity.class));
        }
    }
}
