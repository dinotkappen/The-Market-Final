package com.the.marketapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.the.marketapp.Activity.ImageViewerActivity;
import com.bumptech.glide.Glide;
import com.the.marketapp.Model.SingleItem_Banner_Img_Model;
import com.the.marketapp.R;

import java.util.ArrayList;

public class SingleItem_Banner_ViewPagerAdapter extends PagerAdapter {


    private String[] urls;
    public static ArrayList<String> singleImagList = new ArrayList<>();

    private ArrayList<SingleItem_Banner_Img_Model> bannerImgArrayList;
    private LayoutInflater inflater;
    private Context context;


    public SingleItem_Banner_ViewPagerAdapter(Context context, ArrayList<SingleItem_Banner_Img_Model> bannerImgArrayList) {
        this.context = context;
//            this.imageModelArrayList = imageModelArrayList;
        this.bannerImgArrayList = bannerImgArrayList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return bannerImgArrayList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.layout_singleitem_banner, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.imageView);


        int k = bannerImgArrayList.size();
        for (int i = 0; i < k; i++) {

            Glide.with(context)
                    .load(bannerImgArrayList.get(i).getSinleItem_banner_Img())
                    .into(imageView);
            if (!singleImagList.contains(bannerImgArrayList.get(i).getSinleItem_banner_Img())) {
                singleImagList.add(bannerImgArrayList.get(i).getSinleItem_banner_Img());
            }


        }


        view.addView(imageLayout, 0);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(context, ImageViewerActivity.class);
                //To pass:
                in.putExtra("key", singleImagList);
                context.startActivity(in);
            }
        });

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}