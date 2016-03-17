package com.example.helper;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by mburns on 8/27/2015.
 */
public class ConnectivityHelper {

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean is4g = (cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null) ?
                cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting() : false;
        boolean isWifi = (cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null) ?
                cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting() : false;
        return (is4g || isWifi);
    }

    public static boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return (cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null) ?
                cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting() : false;
    }
}
