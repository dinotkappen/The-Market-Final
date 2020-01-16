package com.the.marketapp.Adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.the.marketapp.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ImageViewAdapter extends PagerAdapter {


    private String[] urls;
    public static ArrayList<String> singleImagList=new ArrayList<>();

    ArrayList<String> imageslist = new ArrayList<>();
    ArrayList<String> imageslistCompare = new ArrayList<>();
    private LayoutInflater inflater;
    private Context context;




    public ImageViewAdapter(Context context, ArrayList<String> imageslist) {
        this.context = context;
//            this.imageModelArrayList = imageModelArrayList;
        this.imageslist = imageslist;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return imageslist.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.imageview_adapter_layout, view, false);

        assert imageLayout != null;
        final ImageView imageViewSingleImgView = (ImageView) imageLayout
                .findViewById(R.id.imageViewSingleImgView);


        int k = imageslist.size();



        Log.v("k",""+k);
        for (int i = 0; i < k; i++) {
            String img=imageslist.get(i);

                  Glide.with(context)
                          .load(imageslist.get(i))
                          .into(imageViewSingleImgView);
                  imageslistCompare.add(img);


        }


        view.addView(imageLayout, 0);



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
