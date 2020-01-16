package com.the.marketapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.the.marketapp.Fragment.SubMenu_Fragment;
import com.the.marketapp.Model.Category_Model;
import com.the.marketapp.Other.Utilities;
import com.the.marketapp.R;

import java.util.ArrayList;

public class Submenu_category_Adapter extends RecyclerView.Adapter<Submenu_category_Adapter.ViewHolder> {

    private static final String TAG = "Submenu_category_Adapter";

    //vars
    // private ArrayList<String> str_Sub_Cat = new ArrayList<>();
    private ArrayList<Category_Model> itemCatList = new ArrayList<>();
    private Context mContext;
    SubMenu_Fragment fragment;
    Utilities utilities;
    Submenu_category_Adapter submenu_category_Adapter;
    int last_pos = 0;
    int row_index=0;

    public Submenu_category_Adapter(Context context, ArrayList<Category_Model> itemCatList, SubMenu_Fragment fragment) {
        this.itemCatList = itemCatList;

        this.mContext = context;
        this.fragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.edt_sub_Cat.setText(itemCatList.get(position).getCategory_name());




        holder.edt_sub_Cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                holder.edt_sub_Cat.setBackgroundResource(R.drawable.edt_round_selected);
//                holder.edt_sub_Cat.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
                itemCatList.get(position).setStatus(true);
                fragment.data_Sub_Items.clear();
                String categoryId_str = itemCatList.get(position).getId();
                String JSON_URL_CATEGORIES = utilities.GetUrl()+"category/"+categoryId_str;
                        fragment.loadSubItems_api(JSON_URL_CATEGORIES);
                row_index=position;
                notifyDataSetChanged();


            }
        });

        if(row_index==position){

            holder.edt_sub_Cat.setBackgroundResource(R.drawable.edt_round_selected);
            holder.edt_sub_Cat.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
        }
        else
        {
            holder.edt_sub_Cat.setBackgroundResource(R.drawable.edt_black_border);
            holder.edt_sub_Cat.setTextColor(mContext.getResources().getColor(R.color.colorBlack));
        }


    }

    @Override
    public int getItemCount() {
        return itemCatList.size();
    }
    @Override
    public int getItemViewType(int position)
    {
        return position;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {


        EditText edt_sub_Cat;

        public ViewHolder(View itemView) {
            super(itemView);
            edt_sub_Cat = itemView.findViewById(R.id.edt_Sub_Cat);

        }
    }
}
