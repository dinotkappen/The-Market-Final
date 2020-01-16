package com.the.marketapp.Other;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Utilities {

    public static boolean isOnline(final Context mcontext)
    {
        boolean response = true;
        try {
            ConnectivityManager cm = (ConnectivityManager) mcontext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = cm.getActiveNetworkInfo();
            if (ni == null) {
                response = false;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            response = false;
        }
        Log.v("response","response : "+response);
        return response;
    }

    public static String GetUrl()
    {
        return "https://www.themarket.qa/api/";
    }

    public static void removePhoneKeypad(View rootView) {
        InputMethodManager inputManager = (InputMethodManager) rootView
                .getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        IBinder binder = rootView.getWindowToken();
        inputManager.hideSoftInputFromWindow(binder,
                InputMethodManager.HIDE_NOT_ALWAYS);
    }



}
