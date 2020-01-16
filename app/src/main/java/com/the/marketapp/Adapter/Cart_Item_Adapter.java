package com.the.marketapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.the.marketapp.Activity.CartActivity;
import com.bumptech.glide.Glide;
import com.the.marketapp.Model.Cart_Item_Model;
import com.the.marketapp.R;

import java.util.List;

public class Cart_Item_Adapter extends RecyclerView.Adapter<Cart_Item_Adapter.MyViewHolder> {

    private Context mContext;
    public List<Cart_Item_Model> cart_item_ModelList;
    CartActivity cartFragment;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_title, txt_qty, txt_price, txtAttributeCart;
        public ImageView img_thumbnail, img_love, img_plus, img_minus;
        public LinearLayout linear_layout_card;
        public EditText edt_Count;
        public int int_count = 1;
        public double int_count_normal_format;
        public double total_price_dbl, sub_total_int, total_dbl_local;
        String cartID, quantity;

        CartActivity cartActivity;

        public MyViewHolder(View view) {
            super(view);
            try {
                txt_title = (TextView) view.findViewById(R.id.txt_title);
                txt_qty = (TextView) view.findViewById(R.id.txt_qty);
                txt_price = (TextView) view.findViewById(R.id.txt_price);
                // txtAttributeCart = (TextView) view.findViewById(R.id.txtAttributeCart);
                img_thumbnail = (ImageView) view.findViewById(R.id.img_thumbnail);
                img_love = (ImageView) view.findViewById(R.id.img_love);
                linear_layout_card = (LinearLayout) view.findViewById(R.id.linear_layout_card);
                edt_Count = (EditText) view.findViewById(R.id.edt_Count);
                img_plus = (ImageView) view.findViewById(R.id.img_plus);
                img_minus = (ImageView) view.findViewById(R.id.img_minus);
                edt_Count.setText(String.format("%02d", int_count));
            } catch (Exception ex) {
                String msg = ex.getMessage().toString();
                Log.v("CartAdapterInit", msg);
            }
        }
    }


    public Cart_Item_Adapter(Context mContext, List<Cart_Item_Model> cart_item_ModelList) {
        this.mContext = mContext;
        this.cart_item_ModelList = cart_item_ModelList;
        this.cartFragment = cartFragment;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item_card, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        try {
            final Cart_Item_Model itemSubModel = cart_item_ModelList.get(position);
            final MyViewHolder viewHolder = (MyViewHolder) holder;
            //holder.total_dbl_local=Double.parseDouble(itemSubModel.getTotal());
            final String cartID = itemSubModel.getProduct_ID();
            viewHolder.txt_title.setText(itemSubModel.getName());
            viewHolder.txt_qty.setText(itemSubModel.getParams());
            // viewHolder.txtAttributeCart.setText(itemSubModel.getParams()+mContext.getString(R.string._QAR)+"  "+itemSubModel.getPrice());
            viewHolder.txt_price.setText(" " + mContext.getString(R.string._QAR) + " " + itemSubModel.getPrice());
            holder.edt_Count.setText(String.format("%02d", Integer.parseInt(itemSubModel.getQty())));

            holder.img_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        holder.int_count = Integer.parseInt(holder.edt_Count.getText().toString());
                        holder.int_count = holder.int_count + 1;
                        holder.int_count_normal_format = Double.parseDouble("" + holder.int_count);
                        holder.edt_Count.setText(String.format("%02d", holder.int_count));
                        Log.v("price", itemSubModel.getPrice());

                        holder.cartID = itemSubModel.getProduct_ID();
                        holder.quantity = holder.edt_Count.getText().toString();
                        if (holder.cartID != null && !holder.cartID.isEmpty() && !holder.cartID.equals("null")) {

                            if (holder.quantity != null && !holder.quantity.isEmpty() && !holder.quantity.equals("null")) {

                                ((CartActivity) mContext).Update_Cart_Count_api(holder.cartID, holder.quantity);
                                //  cartFragment.Update_Cart_Count_api(holder.cartID, holder.quantity);
                            }
                        }


                        holder.total_dbl_local = Double.parseDouble(cartFragment.netTotal.getText().toString());
                        holder.total_price_dbl = holder.total_dbl_local + Double.parseDouble(itemSubModel.getPrice());
                        cartFragment.txt_net_totalCart.setText(mContext.getString(R.string._QAR) + " " + holder.total_price_dbl);
                        cartFragment.txt_SubTotal.setText(mContext.getString(R.string._QAR) + " " + holder.total_price_dbl);
                        cartFragment.netTotal.setText("" + holder.total_price_dbl);
                    } catch (Exception ex) {
                        String msg = ex.getMessage().toString();
                        Log.v("Plus", msg);
                    }

                }
            });

            holder.img_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        holder.int_count = Integer.parseInt(holder.edt_Count.getText().toString());
                        if (holder.int_count > 1) {

                            holder.int_count = holder.int_count - 1;
                            holder.edt_Count.setText(String.format("%02d", holder.int_count));
                            holder.cartID = itemSubModel.getProduct_ID();
                            holder.quantity = holder.edt_Count.getText().toString();
                            if (holder.cartID != null && !holder.cartID.isEmpty() && !holder.cartID.equals("null")) {

                                if (holder.quantity != null && !holder.quantity.isEmpty() && !holder.quantity.equals("null")) {
                                    ((CartActivity) mContext).Update_Cart_Count_api(holder.cartID, holder.quantity);
                                    // cartFragment.Update_Cart_Count_api(holder.cartID, holder.quantity);
                                }
                            }
                            // cartFragment.Update_Cart_Count_api(itemSubModel.getProduct_ID(), holder.edt_Count.getText().toString());
                            holder.total_dbl_local = Double.parseDouble(cartFragment.netTotal.getText().toString());
                            holder.total_price_dbl = holder.total_dbl_local - Double.parseDouble(itemSubModel.getPrice());

                            cartFragment.txt_SubTotal.setText(mContext.getString(R.string._QAR) + " " + holder.total_price_dbl);
                            cartFragment.txt_net_totalCart.setText(mContext.getString(R.string._QAR) + " " + holder.total_price_dbl);
                            cartFragment.netTotal.setText("" + holder.total_price_dbl);

                        } else  {
                            holder.int_count = holder.int_count - 1;
                            if(holder.int_count==0)
                            {
                                ((CartActivity) mContext).DeleteCart(cartID);
                            }


                        }
                    } catch (Exception ex) {
                        String msg = ex.getMessage().toString();
                        Log.v("Minus", msg);
                    }


                }
            });

//        holder.img_thumbnail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //  Toast.makeText(v.getContext(), "Clicked ", Toast.LENGTH_SHORT).show();
//
//            }
//        });

            // loading itemMainModel cover using Glide library
            Glide.with(mContext).load(itemSubModel.getThumbnail()).into(holder.img_thumbnail);
            // loading itemMainModel cover using Glide library

        } catch (Exception ex) {
            String msg = ex.getMessage().toString();
            Log.v("Bind", msg);
        }
    }


    @Override
    public int getItemCount() {
        return cart_item_ModelList.size();
    }

    public void removeItem(int position) {
        cart_item_ModelList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, cart_item_ModelList.size());
    }


}