package com.globallogic.barcamp;

import android.content.Intent;

/**
 * Created by Gonzalo.Martin on 9/27/2016
 */
public abstract class BasePresenter<T extends BaseView> {

    protected static final String TAG = BasePresenter.class.getSimpleName();

    protected T view;

    public BasePresenter(T view) {
        this.view = view;
    }

    public void onCreate() {
        // To be implemented in sub classes
    }

    public void onCreateView() {
        // To be implemented in sub classes
    }

    public void onStart() {
        // To be implemented in sub classes
    }

    public void onViewCreated() {
        // To be implemented in sub classes
    }

    public void onStop() {
        // To be implemented in sub classes
    }

    public void onDestroy() {
        // To be implemented in sub classes
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // To be implemented in sub classes
    }

    public void onLogout() {
        BarcampApplication.get().setUserEmail("");
        // To be implemented in sub classes
    }

    public boolean canBack() {
        return true;
    }

    public void onDestroyView() {
        // To be implemented in sub classes
    }


}
