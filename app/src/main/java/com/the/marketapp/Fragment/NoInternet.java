package com.the.marketapp.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.the.marketapp.R;

import java.util.Timer;

public class NoInternet extends Fragment {
    View rootView;
    Button ReTry;
    ProgressBar loading;
    ImageView img_refesh;
    LinearLayout lean_internet;
    public NoInternet() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

                // Inflate the layout for this fragment
                rootView = inflater.inflate(R.layout.msg_no_internet, container, false);

                ReTry = rootView.findViewById(R.id.bt_retry);
                loading = rootView.findViewById(R.id.loading);
                img_refesh = rootView.findViewById(R.id.img_refesh);
                lean_internet = rootView.findViewById(R.id.lean_internet);
                lean_internet.setVisibility(View.VISIBLE);
            img_refesh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        loading.setVisibility(View.VISIBLE);

                        final Timer timer = new Timer();
                        timer.scheduleAtFixedRate(
                                new java.util.TimerTask() {
                                    @Override
                                    public void run() {
//
                                        getActivity().runOnUiThread(new Runnable() {

                                            @Override
                                            public void run() {

                                                // Stuff that updates the UI
                                                loading.setVisibility(View.GONE);

                                            }
                                        });
//                                        loading.setVisibility(View.GONE);
                                        timer.cancel();
                                    }
                                },
                                2000, 5000);
                    }
                });

        return rootView;
    }


}
