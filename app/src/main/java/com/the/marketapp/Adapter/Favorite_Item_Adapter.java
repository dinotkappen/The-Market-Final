package com.the.marketapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.the.marketapp.Activity.SingleItemActivity;
import com.bumptech.glide.Glide;
import com.the.marketapp.Model.Favourite_Item_Model;
import com.the.marketapp.R;

import java.util.List;

public class Favorite_Item_Adapter extends RecyclerView.Adapter<Favorite_Item_Adapter.MyViewHolder> {

    private Context mContext;
    private List<Favourite_Item_Model> favorite_item_ModelList;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_title, txt_qty, txt_price,txtDetails;
        public ImageView img_thumbnail_Favourite, img_love;


        public MyViewHolder(View view) {
            super(view);

            txt_title = (TextView) view.findViewById(R.id.txt_title);
            txtDetails= (TextView) view.findViewById(R.id.txtDetails);
//            txt_qty = (TextView) view.findViewById(R.id.txt_qty);
//            txt_price = (TextView) view.findViewById(R.id.txt_price);
            img_thumbnail_Favourite = (ImageView) view.findViewById(R.id.img_thumbnail_Favourite);
            img_love = (ImageView) view.findViewById(R.id.img_love);

        }
    }


    public Favorite_Item_Adapter(Context mContext, List<Favourite_Item_Model> favorite_item_ModelList) {
        this.mContext = mContext;
        this.favorite_item_ModelList = favorite_item_ModelList;

    }

    @Override
    public Favorite_Item_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favorite_item_card, parent, false);


        return new Favorite_Item_Adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final Favorite_Item_Adapter.MyViewHolder holder, int position) {
        final Favourite_Item_Model itemSubModel = favorite_item_ModelList.get(position);
        final Favorite_Item_Adapter.MyViewHolder viewHolder = (Favorite_Item_Adapter.MyViewHolder) holder;
        //holder.total_dbl_local=Double.parseDouble(itemSubModel.getTotal());

        viewHolder.txt_title.setText(itemSubModel.getName());
        //viewHolder.txt_qty.setText(itemSubModel.getQty());
        String price = itemSubModel.getPrice();
        String qtry = itemSubModel.getQty();
        viewHolder.txtDetails.setText(qtry+" - "+price+"  "+mContext.getString(R.string._QAR));
//        viewHolder.txt_price.setText(itemSubModel.getPrice() + " QAR");
//        viewHolder.txt_qty.setText(itemSubModel.getQty());

        viewHolder.img_thumbnail_Favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SharedPreferences.Editor editor = mContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
//                editor.putString("productId", itemSubModel.getId());
//                Log.v("productId", itemSubModel.getId());
//                editor.apply();

                Intent in =new Intent(mContext, SingleItemActivity.class);
                in.putExtra("titleMain",itemSubModel.getName());
                in.putExtra("productId",itemSubModel.getId());
                mContext.startActivity(in);
            }
        });

        viewHolder.txt_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SharedPreferences.Editor editor = mContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
//                editor.putString("productId", itemSubModel.getId());
//                Log.v("productId", itemSubModel.getId());
//                editor.apply();

                Intent in =new Intent(mContext, SingleItemActivity.class);
                in.putExtra("titleMain",itemSubModel.getName());
                in.putExtra("productId",itemSubModel.getId());
                mContext.startActivity(in);
            }
        });


        String img = itemSubModel.getThumbnail();
        Log.v("img",img);
//
        // loading itemMainModel cover using Glide library
      //  Glide.with(mContext).load(img).into(holder.img_thumbnail_Favourite);

//        Glide
//                .with(mContext)
//                .load(img)
//                .centerCrop()
//
//                .into(holder.img_thumbnail_Favourite);

        Glide.with(mContext)
                .load(img)
                .into(holder.img_thumbnail_Favourite);
        // loading itemMainModel cover using Glide library


    }


    @Override
    public int getItemCount() {
        return favorite_item_ModelList.size();
    }

    public void removeItem(int position) {
        favorite_item_ModelList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, favorite_item_ModelList.size());
    }
}
