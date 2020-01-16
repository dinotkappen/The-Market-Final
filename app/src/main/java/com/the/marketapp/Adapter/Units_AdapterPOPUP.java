package com.the.marketapp.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.the.marketapp.Model.Unit_List_Model;
import com.the.marketapp.R;

import java.util.ArrayList;

public class Units_AdapterPOPUP extends RecyclerView.Adapter<Units_AdapterPOPUP.ViewHolder> {
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private static final String TAG = "Units_Adapter";

    int last_pos = 0;

    //vars
    ArrayList<Unit_List_Model> data_Unit_Items_POPUP;

    private Context mContext;

    public Units_AdapterPOPUP(Context context, ArrayList<Unit_List_Model> data_Unit_Items_POPUP) {
        this.data_Unit_Items_POPUP = data_Unit_Items_POPUP;

        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_units_adapter_popup, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        SharedPreferences sharedpreferences = mContext.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();

        final SparseBooleanArray selectedItems = new SparseBooleanArray();
        holder.edt_UnitsPOPUP.setText(data_Unit_Items_POPUP.get(position).getValue());
        if (data_Unit_Items_POPUP.size() > 0) {
            if (position==last_pos) {
                holder.edt_UnitsPOPUP.setBackgroundResource(R.drawable.btn_selected);
                editor.putString("productAttribute_ID", data_Unit_Items_POPUP.get(position).getId());
                Log.v("productAttribute_ID", data_Unit_Items_POPUP.get(position).getId());
                editor.commit();


            } else if(last_pos==position){
                holder.edt_UnitsPOPUP.setBackgroundResource(R.drawable.btn_selected);
            }
            else
            {
                holder.edt_UnitsPOPUP.setBackgroundResource(R.drawable.btn_not_selected);
            }

        }


        holder.edt_UnitsPOPUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                last_pos=position;
                notifyDataSetChanged();
                String g = data_Unit_Items_POPUP.get(position).getAvail_qty();
                holder.edt_UnitsPOPUP.setBackgroundResource(R.drawable.btn_selected);
                editor.putString("productAttribute_ID", data_Unit_Items_POPUP.get(position).getId());
                Log.v("productAttribute_ID", data_Unit_Items_POPUP.get(position).getId());
                editor.commit();


            }
        });


    }

    @Override
    public int getItemCount() {
        return data_Unit_Items_POPUP.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        EditText edt_UnitsPOPUP;

        public ViewHolder(View itemView) {
            super(itemView);
            edt_UnitsPOPUP = itemView.findViewById(R.id.edt_UnitsPOPUP);
        }
    }
}
