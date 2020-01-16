package com.the.marketapp.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.the.marketapp.Activity.MainActivity;
import com.the.marketapp.Other.Utilities;
import com.the.marketapp.R;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;


public class PrivacyPolicyFragment extends Fragment {
    View rootView;
    WebView myWebViewPrivacy;
    Utilities utilities;
    MainActivity mainActivity;

    public PrivacyPolicyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        try {
//            if (utilities.isOnline(getActivity())) {
                // Inflate the layout for this fragment
        Hawk.init(getActivity())
                .setEncryptionMethod(HawkBuilder.EncryptionMethod.MEDIUM)
                .setStorage(HawkBuilder.newSqliteStorage(getActivity()))
                .setLogLevel(LogLevel.FULL)
                .build();
        Hawk.put("pages", "Privacy");

        rootView = inflater.inflate(R.layout.fragment_about, container, false);

                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(R.layout.actionbar_common);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(getLayoutInflater().inflate(R.layout.actionbar_common, null),
                        new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));
                TextView txt_actionbar_Title = (TextView) ((AppCompatActivity) getActivity()).findViewById(R.id.txt_actionbar_Title);
                txt_actionbar_Title.setText("Terms & Conditions");

                LinearLayout linearMenuBlack = (LinearLayout) ((AppCompatActivity) getActivity()).findViewById(R.id.linearMenuBlack);
                linearMenuBlack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // If the navigation drawer is not open then open it, if its already open then close it.
                        if (!mainActivity.drawer.isDrawerOpen(GravityCompat.START))
                            mainActivity.drawer.openDrawer(Gravity.START);
                        else mainActivity.drawer.closeDrawer(Gravity.END);
                    }
                });



                myWebViewPrivacy = (WebView) rootView.findViewById(R.id.myWebViewAbout);

                myWebViewPrivacy.getSettings().setJavaScriptEnabled(true);
                myWebViewPrivacy.loadUrl("https://www.themarket.qa/page/privacy-policy");

//            } else {
//                rootView = inflater.inflate(R.layout.msg_no_internet, container, false);
//            }
//        } catch (Exception ex) {
//            String msg = ex.getMessage().toString();
//            Log.v("msg_Main", msg);
//        }
        return rootView;
    }

    public void onDestroy(){
        super.onDestroy();
        Hawk.put("pages", "home");
    }


}
