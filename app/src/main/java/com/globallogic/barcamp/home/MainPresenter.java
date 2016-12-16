package com.globallogic.barcamp.home;

import android.content.DialogInterface;

import com.globallogic.barcamp.BasePresenter;
import com.globallogic.barcamp.data.firebase.FirebaseWorker;

/**
 * Created by Gonzalo.Martin on 9/27/2016
 */
public class MainPresenter extends BasePresenter<MainView> {

    private FirebaseWorker firebaseWorker = new FirebaseWorker();

    public MainPresenter(MainView view) {
        super(view);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // set home tabs fragment
        view.setHomeTabsFragment();
    }

    public void logoutItemPressed() {
        view.displayLogoutConfirmation(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // execute logout
                firebaseWorker.logout();
                dialogInterface.dismiss();
                view.notifyLogout();
                view.restartApp();
            }
        });
    }

    @Override
    public boolean canBack() {
        return view.fragmentCanBack();
    }
}
