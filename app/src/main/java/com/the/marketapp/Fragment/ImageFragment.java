package com.the.marketapp.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.the.marketapp.R;


public class ImageFragment extends Fragment {
    View rootView;
    ImageView imgSingle;

    public ImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_image, container, false);
        imgSingle=(ImageView)rootView.findViewById(R.id.imgSingle);
        return rootView;
    }


}
