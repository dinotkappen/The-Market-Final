package com.the.marketapp.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.the.marketapp.Model.FAQ_Model;
import com.the.marketapp.R;

import java.util.List;


public class FAQ_Adapter  extends RecyclerView.Adapter<FAQ_Adapter.MyViewHolder> {

    private Context mContext;
    private List<FAQ_Model> faq_ModelList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_desc;
        public EditText edt_title;
        public LinearLayout linear_content;
        public int count=0;


        public MyViewHolder(View view) {
            super(view);

            txt_desc = (TextView) view.findViewById(R.id.txt_desc);
            edt_title = (EditText) view.findViewById(R.id.edt_title);
            linear_content=(LinearLayout)view.findViewById(R.id.linear_content);


        }
    }


    public FAQ_Adapter(Context mContext, List<FAQ_Model> faq_ModelList) {
        this.mContext = mContext;
        this.faq_ModelList = faq_ModelList;
    }
    @Override
    public FAQ_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.faq_card, parent, false);


        return new FAQ_Adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FAQ_Adapter.MyViewHolder holder, final int position) {
        final FAQ_Model itemSubModel = faq_ModelList.get(position);
        final FAQ_Adapter.MyViewHolder viewHolder = (FAQ_Adapter.MyViewHolder)holder;

        viewHolder.edt_title.setText(itemSubModel.getTitle());
        Drawable img = mContext.getResources().getDrawable( R.mipmap.ic_down_arrow );
        viewHolder.edt_title.setCompoundDrawablesWithIntrinsicBounds( null, null, img, null);

        viewHolder.edt_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemSubModel.getStatus()==false)
                {
                    holder.linear_content.setVisibility(View.VISIBLE);
                    viewHolder.count=position;
                    itemSubModel.setStatus(true);
                    viewHolder.txt_desc.setText(Html.fromHtml(Html.fromHtml(itemSubModel.getContent()).toString()));
                    Drawable img = mContext.getResources().getDrawable( R.mipmap.ic_up_arrow );
                    viewHolder.edt_title.setCompoundDrawablesWithIntrinsicBounds( null, null, img, null);
                }
                else
                {
                    holder.linear_content.setVisibility(View.GONE);
                    viewHolder.count=position;
                    itemSubModel.setStatus(false);
                    Drawable img = mContext.getResources().getDrawable( R.mipmap.ic_down_arrow );
                    viewHolder.edt_title.setCompoundDrawablesWithIntrinsicBounds( null, null, img, null);
                    //viewHolder.txt_desc.setText(Html.fromHtml(Html.fromHtml(itemSubModel.getContent()).toString()));
                }
                ;
            }
        });


    }

    @Override
    public int getItemCount() {
        return faq_ModelList.size();
    }

    public void removeItem(int position) {
        faq_ModelList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, faq_ModelList.size());
    }
    @Override
    public int getItemViewType(int position)
    {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



}