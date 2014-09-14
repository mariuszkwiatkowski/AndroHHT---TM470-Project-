package com.kwiatkowski.androhht.androhht.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * This application uses 3rd party libraries and code.
 * Refer to LICENCE.txt for licensing details
 */

public class ConnectivityCheck {

    public static boolean isConnected(Context ctx) {
        Context context = ctx;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()){
            return true;
        };
        return false;

    }



}
