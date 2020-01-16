package com.the.marketapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.the.marketapp.Activity.SingleOrderHistoryActivity;
import com.the.marketapp.Model.OrderHistoryModel;
import com.the.marketapp.R;

import java.util.List;


public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.MyViewHolder> {

    private Context mContext;
    private List<OrderHistoryModel> orderHistoryModel;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView  txtOrderReference,txtDate,txtPrice,txtOrderStatus;
        LinearLayout linear_orderhistory_layout;




        public MyViewHolder(View view) {
            super(view);

            txtOrderReference = (TextView) view.findViewById(R.id.txtOrderReference);
            txtDate= (TextView) view.findViewById(R.id.txtDate);
            txtPrice= (TextView) view.findViewById(R.id.txtPrice);
            txtOrderStatus= (TextView) view.findViewById(R.id.txtOrderStatus);
            linear_orderhistory_layout=(LinearLayout)view.findViewById(R.id.linear_orderhistory_layout);

        }
    }


    public OrderHistoryAdapter(Context mContext, List<OrderHistoryModel> orderHistoryModel) {
        this.mContext = mContext;
        this.orderHistoryModel = orderHistoryModel;

    }
    @Override
    public OrderHistoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.orderhistory_adapter_layout, parent, false);


        return new OrderHistoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final OrderHistoryAdapter.MyViewHolder holder, int position) {
        final OrderHistoryModel itemSubModel = orderHistoryModel.get(position);
        final OrderHistoryAdapter.MyViewHolder viewHolder = (OrderHistoryAdapter.MyViewHolder)holder;
        //holder.total_dbl_local=Double.parseDouble(itemSubModel.getTotal());

        viewHolder.txtOrderReference.setText(itemSubModel.getRefrenceOrderHistory());
        viewHolder.txtDate.setText(itemSubModel.getDateOrderHistory());
        viewHolder.txtPrice.setText(itemSubModel.getPriceOrderHistory()+" "+mContext.getString(R.string._QAR));
        viewHolder.txtOrderStatus.setText(itemSubModel.getStatusOrderHistory());

        viewHolder.linear_orderhistory_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(mContext, SingleOrderHistoryActivity.class);
                in.putExtra("orderHistoryID",itemSubModel.getIdOrderHistory());
               mContext.startActivity(in);

            }
        });

    }


    @Override
    public int getItemCount() {
        return orderHistoryModel.size();
    }

    public void removeItem(int position) {
        orderHistoryModel.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, orderHistoryModel.size());
    }
}
