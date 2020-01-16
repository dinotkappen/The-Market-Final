package com.the.marketapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.the.marketapp.Activity.SingleItemActivity;
import com.bumptech.glide.Glide;
import com.the.marketapp.Model.Related_Products_Model;
import com.the.marketapp.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.the.marketapp.Adapter.Item_Main_Adapter.MY_PREFS_NAME;

public class RelatedProductsAdapter extends RecyclerView.Adapter<RelatedProductsAdapter.MyViewHolder> {

    private Context mContext;
    private List<Related_Products_Model> relatedPrdctsModelList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView thumbnail;
        public LinearLayout layout_card;
        CardView card_view;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.txt_title);
            thumbnail = (ImageView) view.findViewById(R.id.img_thumbnail);
            layout_card = (LinearLayout) view.findViewById(R.id.linear_layout_card);
            card_view = (CardView) view.findViewById(R.id.card_view);
        }
    }


    public RelatedProductsAdapter(Context mContext, List<Related_Products_Model> relatedPrdctsModelList) {
        this.mContext = mContext;
        this.relatedPrdctsModelList = relatedPrdctsModelList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.related_products_card, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Related_Products_Model itemRelatedModel = relatedPrdctsModelList.get(position);
        holder.title.setText(itemRelatedModel.getName());

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SharedPreferences.Editor editor =mContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putString("productId", itemRelatedModel.getId());
                    editor.apply();

                    Intent in =new Intent(mContext, SingleItemActivity.class);
                    in.putExtra("titleMain",itemRelatedModel.getName());
                    mContext.startActivity(in);
                } catch (Exception ex) {
                    String h = ex.getMessage().toString();
                    String k = h;
                }
            }
        });

        // loading itemMainModel cover using Glide library
        Glide.with(mContext).load(itemRelatedModel.getThumbnail()).into(holder.thumbnail);

    }


    @Override
    public int getItemCount() {
        return relatedPrdctsModelList.size();
    }
}
