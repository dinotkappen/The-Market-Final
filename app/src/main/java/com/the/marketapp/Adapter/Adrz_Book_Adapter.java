package com.the.marketapp.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.the.marketapp.Activity.AddressBookActivity;
import com.the.marketapp.Activity.Edit_Adrz_Activity;
import com.bumptech.glide.Glide;
import com.the.marketapp.Model.Adrz_Book_Model;
import com.the.marketapp.Other.Utilities;
import com.the.marketapp.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.the.marketapp.Adapter.Item_Main_Adapter.MY_PREFS_NAME;

public class Adrz_Book_Adapter extends RecyclerView.Adapter<Adrz_Book_Adapter.MyViewHolder> {

    private Context mContext;
    public List<Adrz_Book_Model> adrzModelList;
    private List<String> provinence_ID_List;
    private List<String> provinence_Values_List;
    Utilities utilities;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_name, txt_address_1, txt_address_2, txt_Provinence, txt_Phone;
        public ImageView img_alias, img_default, img_edit, img_delete;
       public AddressBookActivity addressBookActivity;
        Utilities utilities;


        public MyViewHolder(View view) {
            super(view);
            txt_name = (TextView) view.findViewById(R.id.txt_name);
            txt_address_1 = (TextView) view.findViewById(R.id.txt_address_1);
            //   txt_address_2 = (TextView) view.findViewById(R.id.txt_address_2);
            txt_Provinence = (TextView) view.findViewById(R.id.txt_Provinence);
//            img_love = (ImageView) view.findViewById(R.id.img_love);
            txt_Phone = (TextView) view.findViewById(R.id.txt_Phone);

            img_alias = (ImageView) view.findViewById(R.id.img_alias);
            img_default = (ImageView) view.findViewById(R.id.img_default);
            img_edit = (ImageView) view.findViewById(R.id.img_edit);
          //  img_delete = (ImageView) view.findViewById(R.id.img_delete);


        }
    }

    public Adrz_Book_Adapter(Context mContext, List<Adrz_Book_Model> adrzModelList, List<String> provinence_ID_List, List<String> provinence_Values_List) {
        this.mContext = mContext;
        this.adrzModelList = adrzModelList;
        this.provinence_ID_List = provinence_ID_List;
        this.provinence_Values_List = provinence_Values_List;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adrz_book_card, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        try{
        final Adrz_Book_Model itemSubModel = adrzModelList.get(position);


        String name=itemSubModel.getName();
        if (name != null && !name.isEmpty() && !name.equals("null"))
        {
            holder.txt_name.setText(name);
        }
        String Address_1=itemSubModel.getAddress_1();
        if (Address_1 != null && !Address_1.isEmpty() && !Address_1.equals("null"))
        {
            holder.txt_address_1.setText(Address_1);
        }
        String phone=itemSubModel.getPhone();

        if (phone != null && !phone.isEmpty() && !phone.equals("null"))
        {
            holder.txt_Phone.setText(mContext.getString(R.string.Phone)+": " + phone);
        }

        String provinenceName=itemSubModel.getProvinenceName();
        if (provinenceName != null && !provinenceName.isEmpty() && !provinenceName.equals("null"))
        {
            holder.txt_Provinence.setText(provinenceName);
        }
        // holder.txt_address_2.setText(itemSubModel.getAddress_2());



//        for (int j = 0; j < provinence_ID_List.size(); j++) {
//            if (provinence_ID_List.equals(itemSubModel.getProvince_id())) {
//                String provinence = provinence_Values_List.get(j);
//                holder.txt_Provinence.setText(provinence);
//
//
//            }
//
//
//        }
        String aliyas = itemSubModel.getAlias();
        if (aliyas.equals("home")) {
            Glide.with(mContext).load(R.drawable.home).into(holder.img_alias);
        } else {
            Glide.with(mContext).load(R.drawable.work).into(holder.img_alias);
        }
        String is_default = itemSubModel.getIs_default();
        if (is_default.equals("1")) {
            holder.img_default.setImageResource(R.mipmap.ic_round_tick);

        } else {
            holder.img_default.setImageResource(R.mipmap.ic_radio_not_selected);
        }

        // loading itemMainModel cover using Glide library

        // loading itemMainModel cover using Glide library
        // Glide.with(mContext).load(itemSubModel.getLove()).into(holder.img_love);

        holder.img_default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                try {
                    SharedPreferences.Editor editor = mContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putString("addressId", itemSubModel.getAdrz_ID());
                    editor.apply();
                    final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                    builder.setTitle(mContext.getString(R.string.ADDRESS));
                    builder.setMessage(mContext.getString(R.string.Make_this_My_default_address));
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
                                    try {
                                        String id = itemSubModel.getAdrz_ID();
                                        if(mContext instanceof AddressBookActivity){
                                            ((AddressBookActivity)mContext).setDefaultAdrzAPI(itemSubModel.getAdrz_ID());
                                        }
                                        //((AddressBookActivity)mContext).setDefaultAdrzAPI(itemSubModel.getAdrz_ID());
                                       // holder.addressBookActivity.setDefaultAdrzAPI(itemSubModel.getAdrz_ID());
                                    }
                                    catch (Exception ex)
                                    {
                                        Log.v("defalAdrz",ex.getMessage().toString());
                                    }


                                }
                            });


                    builder.show();
                } catch (Exception ex) {
                    String msg = ex.getMessage().toString();
                    Log.v("img_default", msg);
                }
            }
        });
        holder.img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                SharedPreferences.Editor editor = mContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("addressId", itemSubModel.getAdrz_ID());
                editor.apply();
//                Fragment fragment = new Edit_Adrz_Fragment();
//                FragmentManager fragmentManager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.fragmentContainerAddress, fragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
                    Intent in =new Intent(mContext, Edit_Adrz_Activity.class);
                    mContext.startActivity(in);
                } catch (Exception ex) {
                    String msg = ex.getMessage().toString();
                    Log.v("img_edit", msg);
                }
            }

        });

//        holder.img_delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View v) {
//
//                SharedPreferences.Editor editor = mContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
//                editor.putString("addressId", itemSubModel.getAdrz_ID());
//                editor.apply();
//                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//
//                builder.setTitle("Delete Address");
//                builder.setMessage("Are you sure you want to delete address?");
//                builder.setCancelable(false);
//                builder.setNegativeButton("NO",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog,
//                                                int which) {
//                                dialog.dismiss();
//                            }
//                        });
//                builder.setPositiveButton("YES",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog,
//                                                int which) {
//                                address_book_Fragment.Delete_AdrzApi(itemSubModel.getAdrz_ID());
//                                Fragment fragment = new Address_Book_Fragment();
//                                FragmentManager fragmentManager = ((AppCompatActivity)v.getContext()).getSupportFragmentManager();
//                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                                fragmentTransaction.replace(R.id.fragment_container, fragment);
//                                fragmentTransaction.addToBackStack(null);
//                                fragmentTransaction.commit();
//
//                            }
//                        });
//
//
//                builder.show();
//
//
//            }
//        });
        } catch (Exception ex) {
            String msg = ex.getMessage().toString();
            Log.v("onBindViewHolder", msg);
        }
    }


    @Override
    public int getItemCount() {
        return adrzModelList.size();
    }


}
