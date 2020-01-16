package com.the.marketapp.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.the.marketapp.R;

public class SplashActivity extends AppCompatActivity {

    /**
     * Duration of wait
     **/
    private final int SPLASH_DISPLAY_LENGTH = 500;
    int logedIn = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {

            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_splash);

            /* New Handler to start the Menu-Activity
             * and close this Splash-Screen after some seconds.*/
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

//
//                        // Start your app main activity
                    Intent i = new Intent(SplashActivity.this, Multi_Lang_Activity.class);
                    i.putExtra("from","SplashActivity");
                    startActivity(i);
                    finish();
//
                }
            }, SPLASH_DISPLAY_LENGTH);
        } catch (Exception ex) {
            String ms = ex.getMessage().toString();
            String y = ms;
        }
    }
}
