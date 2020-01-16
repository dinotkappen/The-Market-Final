package com.the.marketapp.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.the.marketapp.Fragment.ShoppingListSingleViewFragment;
import com.the.marketapp.Model.ShoppingListModel;
import com.the.marketapp.Other.Utilities;
import com.the.marketapp.R;

import java.util.List;



public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.MyViewHolder> {

    private Context mContext;
    public List<ShoppingListModel> shoppingListModel;
    Utilities utilities;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtShoppingListName;





        public MyViewHolder(View view) {
            super(view);
            txtShoppingListName = (TextView) view.findViewById(R.id.txtShoppingListName);


        }
    }

    public ShoppingListAdapter(Context mContext, List<ShoppingListModel> shoppingListModel) {
        this.mContext = mContext;
        this.shoppingListModel = shoppingListModel;
    }

    @Override
    public ShoppingListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shoppig_list_adapter_layout, parent, false);

        return new ShoppingListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ShoppingListAdapter.MyViewHolder holder, int position) {
        final ShoppingListModel itemSubModel = shoppingListModel.get(position);

        holder.txtShoppingListName.setText(itemSubModel.getNameShoppingList());

        holder.txtShoppingListName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ShoppingListSingleViewFragment();

                Bundle args = new Bundle();
                args.putString("listID", itemSubModel.getIdShoppinList());
                fragment.setArguments(args);
                FragmentManager fragmentManager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

    }


    @Override
    public int getItemCount() {
        return shoppingListModel.size();
    }
}
