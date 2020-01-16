package com.the.marketapp.Activity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.the.marketapp.Adapter.ImageViewAdapter;
import com.the.marketapp.Other.Utilities;
import com.the.marketapp.R;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

public class ImageViewerActivity extends AppCompatActivity {
  Utilities utilities;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    ImageView imgClose;
    LinearLayout linearClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            if (utilities.isOnline(this)) {
                setContentView(R.layout.activity_image_viewer);
                imgClose=(ImageView)findViewById(R.id.imgClose);
                linearClose=(LinearLayout)findViewById(R.id.linearClose);


                ArrayList<String> imageslist = (ArrayList<String>) getIntent().getSerializableExtra("key");
                ArrayList<String>imageslistCompare=new ArrayList<>();
                int h=imageslist.size();
                Log.v("hokkl",""+h);
                
                try{

                    for (int i=0;i<imageslist.size();i++)
                    {
                        String img=imageslist.get(i);
                        if(!imageslistCompare.contains(img))
                        {
                            imageslistCompare.add(img);
                        }
                        else
                        {
                            String k="l";
                        }

                    }

                mPager = (ViewPager) findViewById(R.id.pagerImageViewer);
                mPager.setAdapter(new ImageViewAdapter(this, imageslistCompare));

                CirclePageIndicator indicator = (CirclePageIndicator)
                        findViewById(R.id.indicatorImageViewer);
                NUM_PAGES = imageslistCompare.size();
                Log.v("NUM_PAGES",""+NUM_PAGES);

                    indicator.setViewPager(mPager);




                final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
                indicator.setRadius(4 * density);


                    NUM_PAGES = imageslist.size();



                    // Pager listener over indicator
                    indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                        @Override
                        public void onPageSelected(int position) {
                            currentPage = position;

                        }

                        @Override
                        public void onPageScrolled(int pos, float arg1, int arg2) {

                        }

                        @Override
                        public void onPageScrollStateChanged(int pos) {

                        }
                    });

                    imgClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
                } catch (Exception ex) {
                    String s = ex.getMessage().toString();

                }




        } else {
                setContentView(R.layout.msg_no_internet);
            }
        } catch (Exception ex) {
            String s = ex.getMessage().toString();
            String h = s;
        }
    }


}
