package com.globallogic.barcamp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;

/**
 * Created by Gonzalo.Martin on 10/28/2016
 */

public class DeviceUtils {

    private DeviceUtils() {
        // Private constructor
    }

    /**
     * Checks if there is a network connection
     * @param context
     * @return true if there is network connection
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] networks = cm.getAllNetworks();
            NetworkInfo networkInfo;
            for (Network mNetwork : networks) {
                networkInfo = cm.getNetworkInfo(mNetwork);
                if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                    return true;
                }
            }
        } else {
            boolean haveConnectedWifi = false;
            boolean haveConnectedMobile = false;
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null) {
                haveConnectedWifi = "WIFI".equalsIgnoreCase(networkInfo.getTypeName()) && networkInfo.isConnected();
                haveConnectedMobile = "MOBILE".equalsIgnoreCase(networkInfo.getTypeName()) && networkInfo.isConnected();
            }
            return haveConnectedWifi || haveConnectedMobile;
        }
        return false;
    }

}
