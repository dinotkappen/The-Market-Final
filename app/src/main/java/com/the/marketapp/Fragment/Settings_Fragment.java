package com.the.marketapp.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.the.marketapp.Activity.AddressBookActivity;
import com.the.marketapp.Activity.LogInActivity;
import com.the.marketapp.Activity.MainActivity;
import com.the.marketapp.Other.Utilities;
import com.the.marketapp.R;


public class Settings_Fragment extends Fragment {
    View rootView;
    EditText edt_your_info, edt_change_pwd, edt_logout, edt_adrz_book;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    MainActivity mainActivity;
    Utilities utilities;
    int logedIn;
    SharedPreferences preferences;


    public Settings_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            if (utilities.isOnline(getActivity())) {
                // Inflate the layout for this fragment
                rootView = inflater.inflate(R.layout.fragment_settings, container, false);

                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(R.layout.actionbar_common);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(getLayoutInflater().inflate(R.layout.actionbar_common, null),
                        new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));


                TextView txt_actionbar_Title = (TextView) ((AppCompatActivity) getActivity()).findViewById(R.id.txt_actionbar_Title);
                txt_actionbar_Title.setText(R.string.SETTINGS);

                LinearLayout linearMenuBlack = (LinearLayout) ((AppCompatActivity) getActivity()).findViewById(R.id.linearMenuBlack);
                linearMenuBlack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // If the navigation drawer is not open then open it, if its already open then close it.
                        if (!mainActivity.drawer.isDrawerOpen(GravityCompat.START))
                            mainActivity.drawer.openDrawer(Gravity.START);
                        else mainActivity.drawer.closeDrawer(Gravity.END);
                    }
                });
                preferences = getContext().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                logedIn = preferences.getInt("logedIn", 0);
                Log.v("logedInSettings", "" + logedIn);
                edt_your_info = (EditText) rootView.findViewById(R.id.edt_your_info);
                edt_change_pwd = (EditText) rootView.findViewById(R.id.edt_change_pwd);
                edt_logout = (EditText) rootView.findViewById(R.id.edt_logout);
                edt_adrz_book = (EditText) rootView.findViewById(R.id.edt_adrz_book);
//                edtChangeLang = (EditText) rootView.findViewById(R.id.edtChangeLang);
                if(logedIn==0)
                {
                    edt_change_pwd.setVisibility(View.GONE);
                    edt_logout.setVisibility(View.GONE);
                }
                else
                {
                    edt_change_pwd.setVisibility(View.VISIBLE);
                    edt_logout.setVisibility(View.VISIBLE);
                }


                edt_adrz_book.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (logedIn == 0) {
                            Intent intent = new Intent(getActivity(), LogInActivity.class);
                            getActivity().startActivity(intent);
                        } else {
                            Intent in = new Intent(getActivity(), AddressBookActivity.class);
                            in.putExtra("from", "Settings_Fragment");
                            startActivity(in);
                        }
                    }
                });

                edt_your_info.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (logedIn == 0) {
                            Intent intent = new Intent(getActivity(), LogInActivity.class);
                            getActivity().startActivity(intent);
                        } else {
                            Fragment fragment = new Your_Info_Fragment();
                            FragmentManager fragmentManager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_container, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }

                    }
                });

                edt_change_pwd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (logedIn == 0) {
                            edt_change_pwd.setVisibility(View.GONE);
                            edt_logout.setVisibility(View.GONE);
                        } else {
                            edt_change_pwd.setVisibility(View.VISIBLE);
                            edt_logout.setVisibility(View.VISIBLE);
                            Fragment fragment = new Forgot_Pwd_Fragment();
                            FragmentManager fragmentManager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_container, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                    }
                });

//                edtChangeLang.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
//                        final SharedPreferences.Editor editor = sharedpreferences.edit();
//                        editor.putString("setApplicationlanguage", "0");
//                        editor.commit();
//                        Intent in = new Intent(getActivity(), Multi_Lang_Activity.class);
//                        startActivity(in);
//                    }
//                });

                edt_logout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (logedIn == 0) {
                            edt_change_pwd.setVisibility(View.GONE);
                            edt_logout.setVisibility(View.GONE);
                        } else {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(
                                    getContext());

                            builder.setTitle(getString(R.string.Logout));
                            builder.setMessage(getString(R.string.Are_you_sure_you_want_to_logout));
                            builder.setCancelable(false);
                            builder.setNegativeButton(R.string.NO,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            builder.setPositiveButton(R.string.YES,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedpreferences.edit();
                                            editor.putString("user_id", "");
                                            editor.putString("customer_name", "");
                                            editor.putString("login_token", "");
                                            Log.v("login_token", "");
                                            editor.putString("user_email", "");
                                            editor.putString("lname", "");
                                            editor.putString("profile_pic", "");
                                            editor.putInt("logedIn", 0);
                                            editor.commit();
                                            Intent in = new Intent(getContext(), MainActivity.class);
                                            startActivity(in);
                                        }
                                    });


                            builder.show();
                        }


                    }
                });
            } else {
                rootView = inflater.inflate(R.layout.msg_no_internet, container, false);
            }


        } catch (Exception ex) {
            Log.v("ShopList_main", ex.getMessage().toString());

        }
        return rootView;
    }


}