package com.the.marketapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.the.marketapp.Fragment.ShoppingListPopUpFragment;
import com.the.marketapp.Model.ShoppingListPOPUPModel;
import com.the.marketapp.Other.Utilities;
import com.the.marketapp.R;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListPopUpAdapter extends RecyclerView.Adapter<ShoppingListPopUpAdapter.MyViewHolder> {

    private Context mContext;
    private List<ShoppingListPOPUPModel> shoppingListPOPUPModel;
    Utilities utilities;
    ShoppingListPopUpFragment shoppingListPopUpFragment;
    public ArrayList<String> listIDLIst = new ArrayList<>();


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtnameShoppingListPopUp;
        public LinearLayout linearRadioCheck;
        public TextView txtRadio;


        public String listID;


        public MyViewHolder(View view) {
            super(view);
            txtnameShoppingListPopUp = (TextView) view.findViewById(R.id.txtnameShoppingListPopUp);
            linearRadioCheck = (LinearLayout) view.findViewById(R.id.linearRadioCheck);
            txtRadio = (TextView) view.findViewById(R.id.txtRadio);


        }
    }

    public ShoppingListPopUpAdapter(Context mContext, List<ShoppingListPOPUPModel> shoppingListPOPUPModel, ShoppingListPopUpFragment shoppingListPopUpFragment) {
        this.mContext = mContext;
        this.shoppingListPOPUPModel = shoppingListPOPUPModel;
        this.shoppingListPopUpFragment = shoppingListPopUpFragment;
    }

    @Override
    public ShoppingListPopUpAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shopping_list_popup_adapter, parent, false);

        return new ShoppingListPopUpAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ShoppingListPopUpAdapter.MyViewHolder holder, final int position) {
        final ShoppingListPOPUPModel itemSubModel = shoppingListPOPUPModel.get(position);

        holder.txtnameShoppingListPopUp.setText(itemSubModel.getNameShoppingListPopUp());

        holder.linearRadioCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // holder.txtRadio.setBackgroundResource(R.mipmap.ic_round_tick);
                holder.listID = itemSubModel.getIdShoppinListPopUp();


                if (itemSubModel.getStatus() == true) {
                    shoppingListPOPUPModel.get(position).setStatus(false);

                    holder.txtRadio.setBackgroundResource(R.drawable.circular_radio_not_selected);
                } else {

                    shoppingListPOPUPModel.get(position).setStatus(true);
                    holder.txtRadio.setBackgroundResource(R.mipmap.tick_ok);

                }
                addToList(holder.listID);


            }
        });


    }

    public void addToList(String listID) {

        if (this.listIDLIst.size() == 0) {
            this.listIDLIst.add(listID);

        } else {

            if (this.listIDLIst.contains(listID)) {
                this.listIDLIst.remove(listID);
                Log.v("listID", listID);
                Log.v("A", "A");
            } else {
                this.listIDLIst.add(listID);
                Log.v("listID", listID);
                Log.v("B", "B");

            }

            Log.v("lkADD", "" + this.listIDLIst.size());
        }

    }


    @Override
    public int getItemCount() {
        return shoppingListPOPUPModel.size();
    }
}

