package com.the.marketapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.the.marketapp.Activity.SingleItemActivity;
import com.the.marketapp.Model.Unit_List_Model;
import com.the.marketapp.R;

import java.util.ArrayList;

public class Units_Adapter extends RecyclerView.Adapter<Units_Adapter.ViewHolder> {
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private static final String TAG = "Units_Adapter";
    SingleItemActivity singleitemfragment = new SingleItemActivity();
    int last_pos = 0;
    String attribute_id, attribute_avail_qty, attribute_price, value;
    Boolean selectedEdittext;
    //vars
    ArrayList<Unit_List_Model> data_Unit_Items;

    private Context mContext;

    public Units_Adapter(Context context, ArrayList<Unit_List_Model> data_Unit_Items) {
        this.data_Unit_Items = data_Unit_Items;

        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_units, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        SharedPreferences sharedpreferences = mContext.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();

        final SparseBooleanArray selectedItems = new SparseBooleanArray();
        holder.edt_Units.setText(data_Unit_Items.get(position).getValue());
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.edt_Units.getLayoutParams();
        if (data_Unit_Items.size() > 0) {

            if (data_Unit_Items.size() == 1) {


            }
            if (position == last_pos) {
                holder.edt_Units.setBackgroundResource(R.drawable.btn_selected);
                attribute_id = data_Unit_Items.get(position).getId();
                attribute_avail_qty = data_Unit_Items.get(position).getAvail_qty();
                attribute_price = data_Unit_Items.get(position).getPrice();
                value = data_Unit_Items.get(position).getValue();


            } else if (last_pos == position) {
                holder.edt_Units.setBackgroundResource(R.drawable.btn_selected);
            } else {
                holder.edt_Units.setBackgroundResource(R.drawable.btn_not_selected);

            }

        }


        holder.edt_Units.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                last_pos = position;
                notifyDataSetChanged();
                String g = data_Unit_Items.get(position).getAvail_qty();
                holder.edt_Units.setBackgroundResource(R.drawable.btn_selected);
                TextView txtView = (TextView) ((Activity) mContext).findViewById(R.id.txt_qty);
                TextView txt_money = (TextView) ((Activity) mContext).findViewById(R.id.txt_money);
                EditText edt_Count = (EditText) ((Activity) mContext).findViewById(R.id.edt_Count);

                int avail_qty = Integer.parseInt(data_Unit_Items.get(position).getAvail_qty());
                Log.v("avail_qty_adapter", "" + avail_qty);
                int count = Integer.parseInt(edt_Count.getText().toString());

                txtView.setText(data_Unit_Items.get(position).getValue());
                txt_money.setText(mContext.getString(R.string._QAR)+" "+data_Unit_Items.get(position).getPrice());


                if (avail_qty > 0) {
                    SharedPreferences sharedpreferences = mContext.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                    final SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("availQty", "" + avail_qty);

                    singleitemfragment.txtCart.setText(mContext.getString(R.string.ADD_TO_CART));
                    editor.putString("productAttribute_ID", data_Unit_Items.get(position).getId());
                    singleitemfragment.linear_cart.setClickable(true);
                    editor.commit();

                } else {
                    SharedPreferences sharedpreferences = mContext.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                    final SharedPreferences.Editor editor = sharedpreferences.edit();
                    singleitemfragment.txtCart.setText(mContext.getString(R.string.OUT_OF_STOCK));
                    singleitemfragment.linear_cart.setClickable(false);
                    editor.putString("productAttribute_ID", "0");
                    editor.commit();
                }


                Log.v("productAttribute_ID", data_Unit_Items.get(position).getId());


            }
        });


    }

    @Override
    public int getItemCount() {
        return data_Unit_Items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        EditText edt_Units;

        public ViewHolder(View itemView) {
            super(itemView);
            edt_Units = itemView.findViewById(R.id.edt_Units);
        }
    }
}
