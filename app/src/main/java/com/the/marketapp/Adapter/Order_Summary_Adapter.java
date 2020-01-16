package com.the.marketapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.the.marketapp.Model.Order_Summary_Model;
import com.the.marketapp.R;
import java.util.List;

public class Order_Summary_Adapter extends RecyclerView.Adapter<Order_Summary_Adapter.MyViewHolder> {

    private Context mContext;
    private List<Order_Summary_Model> order_Summary_ModelList;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_name_recycle,txt_unit_recycle,txt_qty_recycle,txt_price_recycle;



        public MyViewHolder(View view) {
            super(view);

            txt_name_recycle = (TextView) view.findViewById(R.id.txt_name_recycle);
            txt_unit_recycle= (TextView) view.findViewById(R.id.txt_unit_recycle);
            txt_qty_recycle= (TextView) view.findViewById(R.id.txt_qty_recycle);
            txt_price_recycle = (TextView) view.findViewById(R.id.txt_price_recycle);

        }
    }


    public Order_Summary_Adapter(Context mContext, List<Order_Summary_Model> order_Summary_ModelList) {
        this.mContext = mContext;
        this.order_Summary_ModelList = order_Summary_ModelList;

    }
    @Override
    public Order_Summary_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_recycler_order_summary, parent, false);


        return new Order_Summary_Adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final Order_Summary_Adapter.MyViewHolder holder, int position) {
        final Order_Summary_Model itemSubModel = order_Summary_ModelList.get(position);
        final Order_Summary_Adapter.MyViewHolder viewHolder = (Order_Summary_Adapter.MyViewHolder)holder;
        //holder.total_dbl_local=Double.parseDouble(itemSubModel.getTotal());

        viewHolder.txt_name_recycle.setText(itemSubModel.getItem_Name());
        viewHolder.txt_unit_recycle.setText(itemSubModel.getItem_units()+" "+mContext.getString(R.string._QAR));
        viewHolder.txt_qty_recycle.setText(itemSubModel.getItem_qty());
        viewHolder.txt_price_recycle.setText(itemSubModel.getItem_Price()+" "+mContext.getString(R.string._QAR));



    }


    @Override
    public int getItemCount() {
        return order_Summary_ModelList.size();
    }

    public void removeItem(int position) {
        order_Summary_ModelList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, order_Summary_ModelList.size());
    }
}