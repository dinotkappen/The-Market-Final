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
import com.the.marketapp.Model.NotificationModel;
import com.the.marketapp.R;


import java.util.List;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private Context mContext;
    private List<NotificationModel> notificationModel;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtNotificationTitle,txtNotificationContent;
        public LinearLayout linear_notification_layout;




        public MyViewHolder(View view) {
            super(view);

            txtNotificationTitle = (TextView) view.findViewById(R.id.txtNotificationTitle);
            txtNotificationContent= (TextView) view.findViewById(R.id.txtNotificationContent);
            linear_notification_layout= (LinearLayout) view.findViewById(R.id.linear_notification_layout);


        }
    }


    public NotificationAdapter(Context mContext, List<NotificationModel> notificationModel) {
        this.mContext = mContext;
        this.notificationModel = notificationModel;

    }
    @Override
    public NotificationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_adapter_layout, parent, false);


        return new NotificationAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final NotificationAdapter.MyViewHolder holder, int position) {
        final NotificationModel itemSubModel = notificationModel.get(position);
        final NotificationAdapter.MyViewHolder viewHolder = (NotificationAdapter.MyViewHolder)holder;
        //holder.total_dbl_local=Double.parseDouble(itemSubModel.getTotal());

        viewHolder.txtNotificationTitle.setText(itemSubModel.getTitleNotification());
        viewHolder.txtNotificationContent.setText(itemSubModel.getMessageNotification());

        viewHolder.linear_notification_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(mContext, SingleOrderHistoryActivity.class);
                in.putExtra("orderHistoryID",itemSubModel.getIdNotification());
                mContext.startActivity(in);

            }
        });

    }





    @Override
    public int getItemCount() {
        return notificationModel.size();
    }

    public void removeItem(int position) {
        notificationModel.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, notificationModel.size());
    }
}
