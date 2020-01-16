package com.the.marketapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.the.marketapp.Model.SingleShoppingListModel;
import com.the.marketapp.R;

import java.util.List;

public class SingleShopListAdapter extends RecyclerView.Adapter<SingleShopListAdapter.MyViewHolder> {

    private Context mContext;
    private List<SingleShoppingListModel> singleShoppingListModel;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtNameSingleShoppingList,txtQtySingleShoppingList,txtPrieSingleShoppingList;
        public ImageView imgSingleShoppingList;

        public MyViewHolder(View view) {
            super(view);

            txtNameSingleShoppingList = (TextView) view.findViewById(R.id.txtNameSingleShoppingList);
            txtQtySingleShoppingList= (TextView) view.findViewById(R.id.txtQtySingleShoppingList);
            txtPrieSingleShoppingList= (TextView) view.findViewById(R.id.txtPrieSingleShoppingList);
            imgSingleShoppingList = (ImageView) view.findViewById(R.id.imgSingleShoppingList);

        }
    }
    public SingleShopListAdapter(Context mContext, List<SingleShoppingListModel> singleShoppingListModel) {
        this.mContext = mContext;
        this.singleShoppingListModel = singleShoppingListModel;

    }
    @Override
    public SingleShopListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_shoplist_adapter_layout, parent, false);


        return new SingleShopListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SingleShopListAdapter.MyViewHolder holder, int position) {
        final SingleShoppingListModel itemSubModel = singleShoppingListModel.get(position);
        final SingleShopListAdapter.MyViewHolder viewHolder = (SingleShopListAdapter.MyViewHolder)holder;
        //holder.total_dbl_local=Double.parseDouble(itemSubModel.getTotal());

        viewHolder.txtNameSingleShoppingList.setText(itemSubModel.getNameSingleShopList());
      //  viewHolder.txtQtySingleShoppingList.setText(itemSubModel.getQtySingleShopList());
        viewHolder.txtPrieSingleShoppingList.setText(itemSubModel.getPriceSingleShopList()+" "+mContext.getString(R.string._QAR));


        Glide.with(mContext).load(itemSubModel.getImgSingleShopList()).into(holder.imgSingleShoppingList);

    }





    @Override
    public int getItemCount() {
        return singleShoppingListModel.size();
    }

    public void removeItem(int position) {
        singleShoppingListModel.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, singleShoppingListModel.size());
    }
}
