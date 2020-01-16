package com.the.marketapp.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.the.marketapp.Activity.SingleItemActivity;
import com.bumptech.glide.Glide;
import com.the.marketapp.Activity.LogInActivity;
import com.the.marketapp.Fragment.ShoppingListPopUpFragment;
import com.the.marketapp.Fragment.SubMenu_Fragment;
import com.the.marketapp.Model.Item_Sub_Model;
import com.the.marketapp.Other.Utilities;
import com.the.marketapp.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.the.marketapp.Adapter.Item_Main_Adapter.MY_PREFS_NAME;

public class Sub_Item_Adapter extends RecyclerView.Adapter<Sub_Item_Adapter.MyViewHolder> {

    private Context mContext;
    private List<Item_Sub_Model> itemSubModelList;
    Utilities utilities;
    SubMenu_Fragment subMenu_fragment;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_title_subitem, txt_qty_subitem, txt_price_subitem, txtSubMenuAttribute;
        public ImageView img_thumbnail_subitem, img_love_subitem, imgShoppingListSubItem;
        public LinearLayout linear_layout_card_subitem, linear_Main;
        private CardView card_view_subitem;
        SharedPreferences preferences;
        public static final String MY_PREFS_NAME = "MyPrefsFile";
        Dialog logout_Dialog;

        public MyViewHolder(View view) {
            super(view);
            // txtSubMenuAttribute = (TextView) view.findViewById(R.id.txtSubMenuAttribute);
            txt_title_subitem = (TextView) view.findViewById(R.id.txt_title_subitem);
            txt_qty_subitem = (TextView) view.findViewById(R.id.txt_qty_subitem);
            txt_price_subitem = (TextView) view.findViewById(R.id.txt_price_subitem);
            img_thumbnail_subitem = (ImageView) view.findViewById(R.id.img_thumbnail_subitem);
            img_love_subitem = (ImageView) view.findViewById(R.id.img_love_subitem);
            imgShoppingListSubItem = (ImageView) view.findViewById(R.id.imgShoppingListSubItem);
            linear_layout_card_subitem = (LinearLayout) view.findViewById(R.id.linear_layout_card_subitem);
            preferences = mContext.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
            linear_Main = (LinearLayout) view.findViewById(R.id.linear_Main);
            card_view_subitem = (CardView) view.findViewById(R.id.card_view_subitem);


        }
    }


    public Sub_Item_Adapter(Context mContext, List<Item_Sub_Model> itemSubModelList, SubMenu_Fragment subMenu_fragment) {
        this.mContext = mContext;
        this.itemSubModelList = itemSubModelList;
        this.subMenu_fragment = subMenu_fragment;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sub_item_card, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Item_Sub_Model itemSubModel = itemSubModelList.get(position);

        String title = itemSubModel.getName();
        String qty = itemSubModel.getQty();
        String price = itemSubModel.getPrice();

        if (title != null && !title.isEmpty() && !title.equals("null")) {
            holder.txt_title_subitem.setText(title);
        }

        if (qty != null && !qty.isEmpty() && !qty.equals("null")) {
            holder.txt_qty_subitem.setText(qty);
        }
        if (price != null && !price.isEmpty() && !price.equals("null")) {
            holder.txt_price_subitem.setText(price+" " + mContext.getResources().getString(R.string._QAR));
        }


        // holder.txtSubMenuAttribute.setText(itemSubModel.getQty()+" - "+itemSubModel.getPrice()+" "+mContext.getResources().getString(R.string._QAR));

        if (itemSubModel.getStatusWishList().equals("favourite")) {
            holder.img_love_subitem.setBackgroundResource(R.mipmap.favorite);
        } else {
            holder.img_love_subitem.setBackgroundResource(R.drawable.ic_favorite);
        }


        SharedPreferences.Editor editor = mContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("productId", itemSubModel.getId());
        editor.apply();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SharedPreferences.Editor editor = mContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
//                editor.putString("productId", itemSubModel.getId());
//                Log.v("productId",itemSubModel.getId());
//                editor.apply();

                Intent in = new Intent(mContext, SingleItemActivity.class);
                in.putExtra("titleMain", itemSubModel.getName());
                in.putExtra("wishListStatus", itemSubModel.getStatusWishList());
                in.putExtra("productId", itemSubModel.getId());

                mContext.startActivity(in);
//
            }
        });
        holder.img_thumbnail_subitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                SharedPreferences.Editor editor = mContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
//                editor.putString("productId", itemSubModel.getId());
//                Log.v("productId",itemSubModel.getId());
//                editor.apply();


                Intent in = new Intent(mContext, SingleItemActivity.class);
                in.putExtra("titleMain", itemSubModel.getName());
                in.putExtra("wishListStatus", itemSubModel.getStatusWishList());
                in.putExtra("productId", itemSubModel.getId());
                mContext.startActivity(in);
//

            }
        });

        holder.img_love_subitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int logedIn = holder.preferences.getInt("logedIn", 0);
                Log.v("logedIn", "" + logedIn);
                if (logedIn == 1) {
                    if (itemSubModel.getStatusWishList().equals("favourite")) {
                        //Toast.makeText(mContext,"Delete Wish List",Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor = mContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putString("productId", itemSubModel.getId());
                        editor.apply();
                        holder.img_love_subitem.setImageDrawable(null);
                        holder.img_love_subitem.setBackgroundResource(R.drawable.ic_favorite);
                        subMenu_fragment.Add_TO_WishList_api("delete");
                    } else {
                        //  Toast.makeText(mContext,"Add Wish List",Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor = mContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putString("productId", itemSubModel.getId());
                        editor.apply();
                        holder.img_love_subitem.setImageDrawable(null);
                        holder.img_love_subitem.setBackgroundResource(R.mipmap.favorite);
                        subMenu_fragment.Add_TO_WishList_api("add");
                    }

                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(
                            mContext);

                    builder.setTitle(mContext.getString(R.string.LogIn));
                    builder.setMessage(mContext.getString(R.string.Please_login_to_continue));
                    builder.setCancelable(false);
                    builder.setNegativeButton(mContext.getString(R.string.NO),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.setPositiveButton(mContext.getString(R.string.YES),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    Intent in = new Intent(mContext, LogInActivity.class);
                                    mContext.startActivity(in);
                                }
                            });


                    builder.show();
                }

            }

        });
        holder.imgShoppingListSubItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int logedIn = holder.preferences.getInt("logedIn", 0);
                Log.v("logedIn", "" + logedIn);
                if (logedIn == 1) {


                    subMenu_fragment.recyclerView_subitems.requestDisallowInterceptTouchEvent(false);
//                    subMenu_fragment.scroll.setBackgroundResource(R.color.transparent);
//                    subMenu_fragment.recyclerView_subitems.setBackgroundResource(R.color.transparent);
//                    holder.linear_layout_card_subitem.setBackgroundResource(R.color.transparent);
//                    holder.linear_Main.setBackgroundResource(R.color.transparent);
//                    holder.card_view_subitem.setBackgroundResource(R.color.transparent);
                    SharedPreferences.Editor editor = mContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putString("productId", itemSubModel.getId());
                    editor.apply();

//                    Fragment fragment = new ShoppingListPopUpFragment();
//                    FragmentManager fragmentManager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    fragmentTransaction.attach(fragment);
//                     fragmentTransaction.addToBackStack(null);
//                    fragmentTransaction.commit();

                    Fragment fragment = new ShoppingListPopUpFragment();
                    FragmentTransaction transaction = ((AppCompatActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
                    transaction.addToBackStack(null);
                    /*
                     * When this container fragment is created, we fill it with our first
                     * "real" fragment
                     */
                    transaction.add(R.id.fragment_container, new ShoppingListPopUpFragment());

                    transaction.commit();

                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(
                            mContext);

                    builder.setTitle(mContext.getString(R.string.LogIn));
                    builder.setMessage(mContext.getString(R.string.Please_login_to_continue));
                    builder.setCancelable(false);
                    builder.setNegativeButton(mContext.getString(R.string.NO),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.setPositiveButton(mContext.getString(R.string.YES),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    Intent in = new Intent(mContext, LogInActivity.class);
                                    mContext.startActivity(in);
                                }
                            });


                    builder.show();
                }

            }

        });

//        holder.imgShoppingListSubItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                int logedIn = holder.preferences.getInt("logedIn", 0);
//                Log.v("logedIn", "" + logedIn);
//                if (logedIn == 1) {
//                    Toast.makeText(mContext,"Clicked Shopping List",Toast.LENGTH_SHORT).show();
//                    Fragment fragment = new Shop_List_Fragment();
//                    FragmentManager fragmentManager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    fragmentTransaction.replace(R.id.fragment_container, fragment);
//                    fragmentTransaction.addToBackStack(null);
//                    fragmentTransaction.commit();
//                } else {
//                    final AlertDialog.Builder builder = new AlertDialog.Builder(
//                            mContext);
//
//                    builder.setTitle(mContext.getString(R.string.LogIn));
//                    builder.setMessage(mContext.getString(R.string.Please_login_to_continue));
//                    builder.setCancelable(false);
//                    builder.setNegativeButton(mContext.getString(R.string.NO),
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog,
//                                                    int which) {
//                                    dialog.dismiss();
//                                }
//                            });
//                    builder.setPositiveButton(mContext.getString(R.string.YES),
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog,
//                                                    int which) {
//                                    Intent in = new Intent(mContext, LogInActivity.class);
//                                    mContext.startActivity(in);
//                                }
//                            });
//
//
//                    builder.show();
//                }
//
//            }
//
//        });

        // loading itemMainModel cover using Glide library
        Glide.with(mContext).load(itemSubModel.getThumbnail()).into(holder.img_thumbnail_subitem);
        // loading itemMainModel cover using Glide library
        // Glide.with(mContext).load(itemSubModel.getLove()).into(holder.img_love);


    }


    @Override
    public int getItemCount() {
        return itemSubModelList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        subMenu_fragment.recyclerView_subitems = recyclerView;
    }
}
