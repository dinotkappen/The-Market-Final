package com.the.marketapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.the.marketapp.Model.SingleOrderHistoryModel;
import com.the.marketapp.R;

import java.util.List;


public class SingleOrderHistoryAdapter extends RecyclerView.Adapter<SingleOrderHistoryAdapter.MyViewHolder> {

    private Context mContext;
    private List<SingleOrderHistoryModel> singleOrderHistoryModel;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView  txtItemNameSingle,txtQtySingle,txtPriceSingle;
       ImageView imgSingleItem;




        public MyViewHolder(View view) {
            super(view);

            txtItemNameSingle = (TextView) view.findViewById(R.id.txtItemNameSingle);
            txtQtySingle= (TextView) view.findViewById(R.id.txtQtySingle);
            txtPriceSingle= (TextView) view.findViewById(R.id.txtPriceSingle);
            imgSingleItem= (ImageView) view.findViewById(R.id.imgSingleItem);
            

        }
    }
    
    public SingleOrderHistoryAdapter(Context mContext, List<SingleOrderHistoryModel> singleOrderHistoryModel) {
        this.mContext = mContext;
        this.singleOrderHistoryModel = singleOrderHistoryModel;

    }
    @Override
    public SingleOrderHistoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_orderhistory_adapter_layout, parent, false);


        return new SingleOrderHistoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SingleOrderHistoryAdapter.MyViewHolder holder, int position) {
        final SingleOrderHistoryModel itemSubModel = singleOrderHistoryModel.get(position);
        final SingleOrderHistoryAdapter.MyViewHolder viewHolder = (SingleOrderHistoryAdapter.MyViewHolder)holder;
        //holder.total_dbl_local=Double.parseDouble(itemSubModel.getTotal());

        viewHolder.txtItemNameSingle.setText(itemSubModel.getItemNameSingleOrderHistory());
        viewHolder.txtQtySingle.setText(mContext.getString(R.string.Quantity)+" "+itemSubModel.getQtySingleOrderHistory());
        viewHolder.txtPriceSingle.setText(mContext.getString(R.string._QAR)+" "+itemSubModel.getPriceSingleOrderHistory());

        Glide.with(mContext).load(itemSubModel.getImgSingleOrderHistory()).into(holder.imgSingleItem);
        

        

    }





    @Override
    public int getItemCount() {
        return singleOrderHistoryModel.size();
    }

    public void removeItem(int position) {
        singleOrderHistoryModel.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, singleOrderHistoryModel.size());
    }
}
