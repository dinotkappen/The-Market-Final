package com.the.marketapp.Other;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.the.marketapp.Activity.MainActivity;

public class NetworkChangeReceiver extends BroadcastReceiver {


    private boolean isConnected = false;
    @Override
    public void onReceive(final Context context, final Intent intent) {

//        Log.v(LOG_TAG, "Receieved notification about network status");
        isNetworkAvailable(context);

    }


    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        if (!isConnected) {
                            Log.e("connection_status", "Now you are connected to Internet!");
                           MainActivity. no_internet(true);
//                            networkStatus.setText("Now you are connected to Internet!");
                            isConnected = true;
                            //do your processing here ---
                            //if you need to post any data to the server or get status
                            //update from the server

                        }
                        return true;
                    }
                }
            }
        }
        Log.e("connection_status", "You are not connected to Internet!");
//        networkStatus.setText("You are not connected to Internet!");
        MainActivity. no_internet(false);
        isConnected = false;
        return false;
    }
}
