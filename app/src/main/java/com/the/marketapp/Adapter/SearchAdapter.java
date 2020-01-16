package com.the.marketapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;


import com.the.marketapp.Activity.SingleItemActivity;
import com.the.marketapp.Fragment.Home_Fragment;
import com.the.marketapp.Model.SearchModel;
import com.the.marketapp.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {

    private Context mContext;
    private List<SearchModel> objSearchModel;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public Home_Fragment home_fragment;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_title;

        public MyViewHolder(View view) {
            super(view);
            txt_title = (TextView) view.findViewById(R.id.txtProductname);


        }
    }

    public SearchAdapter(Context mContext, List<SearchModel> objSearchModel) {
        this.mContext = mContext;
        this.objSearchModel = objSearchModel;
    }
    @Override
    public SearchAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_adapter_layout, parent, false);


        return new SearchAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SearchAdapter.MyViewHolder holder, final int position) {
        final SearchModel itemSubModel = objSearchModel.get(position);
        SharedPreferences preferences;
        final SearchAdapter.MyViewHolder viewHolder = (SearchAdapter.MyViewHolder)holder;

        viewHolder.txt_title.setText(itemSubModel.getProductNameSearch());

        viewHolder.txt_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    SharedPreferences.Editor editor = mContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putString("productId", "" + itemSubModel.getProductIDSearch());
                   String productId=itemSubModel.getProductIDSearch();
                   Log.v("productIdSearch",productId);
                   editor.putString("fromSearch","1");
                   editor.apply();

                    Intent in =new Intent(mContext, SingleItemActivity.class);
                    in.putExtra("titleMain",itemSubModel.getProductNameSearch());
                    in.putExtra("productId",itemSubModel.getProductIDSearch());
                    mContext.startActivity(in);

//                    home_fragment.edt_Search.setText("");
//                    home_fragment.clearEditText();


                } catch (Exception ex) {
                    String h = ex.getMessage().toString();
                    String k = h;
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return objSearchModel.size();
    }

    public void removeItem(int position) {
        objSearchModel.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, objSearchModel.size());
    }

    @Override
    public int getItemViewType(int position)
    {
        return position;
    }


}
