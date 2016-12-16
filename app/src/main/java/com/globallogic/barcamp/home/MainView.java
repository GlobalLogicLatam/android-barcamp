package com.globallogic.barcamp.home;

import android.content.DialogInterface;

import com.globallogic.barcamp.BaseView;

/**
 * Created by Gonzalo.Martin on 9/27/2016
 */
public interface MainView extends BaseView {
    void setHomeTabsFragment();

    void displayLogoutConfirmation(DialogInterface.OnClickListener positiveButton);

    void restartApp();

    void notifyLogout();

    boolean fragmentCanBack();
}
