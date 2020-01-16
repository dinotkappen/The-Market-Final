package com.the.marketapp.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.the.marketapp.Fragment.SubMenu_Fragment;
import com.the.marketapp.Model.Item_Main_Model;
import com.the.marketapp.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class Item_Main_Adapter extends RecyclerView.Adapter<Item_Main_Adapter.MyViewHolder> {

private Context mContext;
private List<Item_Main_Model> itemMainModelList;
public static final String MY_PREFS_NAME = "MyPrefsFile";
public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView title;
    public ImageView thumbnail;
    public LinearLayout layout_card;
    CardView card_view;
    SubMenu_Fragment subMenu_fragment;

    public MyViewHolder(View view) {
        super(view);
        title = (TextView) view.findViewById(R.id.title);
        thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        layout_card = (LinearLayout) view.findViewById(R.id.layout_card);
        card_view = (CardView) view.findViewById(R.id.card_view);


    }
}

    public Item_Main_Adapter(Context mContext, List<Item_Main_Model> itemMainModelList) {
        this.mContext = mContext;
        this.itemMainModelList = itemMainModelList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_main_card, parent, false);


        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Item_Main_Model itemMainModel = itemMainModelList.get(position);
        // holder.title.setText(itemMainModelList.get(position).getName());
//        for (int i = 0; i < itemMainModelList.size(); i++) {
//            String h = itemMainModelList.get(i).getName().toString();
//            String p = h;
//            holder.title.setText(itemMainModelList.get(i).getName());
//        }
        holder.title.setText(itemMainModel.getName());

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SharedPreferences.Editor editor =mContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putString("categoryId", itemMainModel.getId());
                    Log.v("categoryId",itemMainModel.getId());
                    editor.apply();

                    Fragment fragment = new SubMenu_Fragment();
                    FragmentManager fragmentManager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, fragment);
                    fragmentTransaction.addToBackStack(null);

                    Bundle bundle = new Bundle();
                    bundle.putString("titleMain", itemMainModel.getName());
                    fragment.setArguments(bundle);
                    fragmentTransaction.commit();







                } catch (Exception ex) {
                    String h = ex.getMessage().toString();
                    String k = h;
                }

            }
        });
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SharedPreferences.Editor editor =mContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putString("categoryId", itemMainModel.getId());
                    Log.v("categoryId",itemMainModel.getId());
                    editor.apply();

                    Fragment fragment = new SubMenu_Fragment();
                    FragmentManager fragmentManager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, fragment);
                    fragmentTransaction.addToBackStack(null);

                    Bundle bundle = new Bundle();
                    bundle.putString("titleMain", itemMainModel.getName());
                    fragment.setArguments(bundle);
                    fragmentTransaction.commit();


                } catch (Exception ex) {
                    String h = ex.getMessage().toString();
                    String k = h;
                }
            }
        });

        String imgUrl=itemMainModel.getThumbnail();

        if (imgUrl != null && !imgUrl.isEmpty() && !imgUrl.equals("null"))
        {
            Glide.with(mContext).load(imgUrl).into(holder.thumbnail);
        }
        else
        {
            Glide.with(mContext).load("http://market.whyte.company/storage/products/no-image.jpg").into(holder.thumbnail);
        }

            // loading itemMainModel cover using Glide library




    }


    @Override
    public int getItemCount() {
        return itemMainModelList.size();
    }
}
