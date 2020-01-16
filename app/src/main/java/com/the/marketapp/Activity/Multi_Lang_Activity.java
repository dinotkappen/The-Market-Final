package com.the.marketapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.the.marketapp.Other.Utilities;
import com.the.marketapp.R;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;

import java.util.Locale;


public class Multi_Lang_Activity extends AppCompatActivity {

    private RadioGroup radioGroup;
    RadioButton rbtn_en, rbtn_ar;
    Button btn_Slct_lang;
    String setApplicationlanguage;
    SharedPreferences preferences;
    Utilities utilities;
    String from;



    public static final String MY_PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Hawk.init(this)
                .setEncryptionMethod(HawkBuilder.EncryptionMethod.MEDIUM)
                .setStorage(HawkBuilder.newSqliteStorage(this))
                .setLogLevel(LogLevel.FULL)
                .build();
//        try {
//            if (utilities.isOnline(this)) {
                setContentView(R.layout.activity_multi__lang_);
                btn_Slct_lang = (Button) findViewById(R.id.btn_Slct_lang);
                radioGroup = (RadioGroup) findViewById(R.id.radioGroup_multi_lang);
                rbtn_en = (RadioButton) findViewById(R.id.rbtn_en);
                rbtn_ar = (RadioButton) findViewById(R.id.rbtn_ar);
                rbtn_ar.setChecked(false);
                rbtn_en.setChecked(true);
                from= getIntent().getExtras().getString("from");
                preferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                String langSelected = preferences.getString("setApplicationlanguage", "");
                Hawk.put("selectLang",langSelected);
                if (langSelected != null && !langSelected.isEmpty() && !langSelected.equals("null") && !langSelected.equals("0")) {
                    if (langSelected.equals("en")) {
                        rbtn_en.setChecked(true);
                        rbtn_ar.setChecked(false);
                    } else {
                        rbtn_ar.setChecked(true);
                        rbtn_en.setChecked(false);
                    }
                    setApplicationlanguage = langSelected;
                    if(!from.equals("MainActivity")) {
                        languageSelection();
                    }
                }



                btn_Slct_lang.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        onSubmit(v);
                    }
                });
//            } else {
//                setContentView(R.layout.msg_no_internet);
//            }
//
//
//        } catch (Exception ex) {
//            Log.v("Cart_main", ex.getMessage().toString());
//
//        }

    }

    public void onSubmit(View v) {
        SharedPreferences sharedpreferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        RadioButton rb = (RadioButton) radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
        if (rb.getText().equals(getString(R.string.English))) {
            editor.putString("setApplicationlanguage", "en");
            Hawk.put("selectLang","en");
            rbtn_en.setChecked(true);
            setApplicationlanguage = "en";
            editor.commit();
        } else {
            editor.putString("setApplicationlanguage", "ar");
            setApplicationlanguage = "ar";
            Hawk.put("selectLang","ar");
            rbtn_ar.setChecked(true);
            editor.commit();
        }

        languageSelection();

//        Toast.makeText(Multi_Lang_Activity.this, rb.getText(), Toast.LENGTH_SHORT).show();
    }

    public void languageSelection() {
        try {
            Locale locale = new Locale(setApplicationlanguage);
            Resources resources = getResources();
            Configuration config = resources.getConfiguration();
            config.locale = locale;
            if (Build.VERSION.SDK_INT >= 17) {
                config.setLayoutDirection(locale);
            }

            resources.updateConfiguration(config, resources.getDisplayMetrics());

            Intent intent = new Intent(Multi_Lang_Activity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } catch (Exception ex) {
            Log.v("languageSelection", ex.getMessage().toString());
        }
    }
}
