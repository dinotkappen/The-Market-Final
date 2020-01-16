package com.the.marketapp.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.the.marketapp.Activity.LogInActivity;
import com.the.marketapp.Activity.MainActivity;
import com.the.marketapp.Adapter.OrderHistoryAdapter;
import com.the.marketapp.Model.OrderHistoryModel;
import com.the.marketapp.Other.Utilities;
import com.the.marketapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.the.marketapp.Adapter.Item_Main_Adapter.MY_PREFS_NAME;


public class Order_History_Fragment extends Fragment {
    View rootView;

    ArrayList<OrderHistoryModel> orderHistoryModel = new ArrayList<OrderHistoryModel>();
    OrderHistoryAdapter orderHistoryAdapter;
    String ORDERHISTORY_URL;
    Utilities utilities;
    SharedPreferences preferences;
    String setApplicationlanguage, login_token;
    int logedIn;

    LinearLayout linearOrderHistoryLayout, linearOrderHistoryLayoutNotVisible;
    MainActivity mainActivity;

    public Order_History_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            if (utilities.isOnline(getActivity())) {
                // Inflate the layout for this fragment
                rootView = inflater.inflate(R.layout.fragment_order__history, container, false);
                linearOrderHistoryLayout = (LinearLayout) rootView.findViewById(R.id.linearOrderHistoryLayout);
                linearOrderHistoryLayoutNotVisible = (LinearLayout) rootView.findViewById(R.id.linearOrderHistoryLayoutNotVisible);
                linearOrderHistoryLayout.setVisibility(View.VISIBLE);
                linearOrderHistoryLayoutNotVisible.setVisibility(View.GONE);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(R.layout.actionbar_common);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(getLayoutInflater().inflate(R.layout.actionbar_common, null),
                        new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));

                LinearLayout linearMenuBlack = (LinearLayout) ((AppCompatActivity) getActivity()).findViewById(R.id.linearMenuBlack);
                TextView txt_actionbar_Title = (TextView) ((AppCompatActivity) getActivity()).findViewById(R.id.txt_actionbar_Title);
                txt_actionbar_Title.setText(getString(R.string.MY_ORDERS));
                linearMenuBlack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // If the navigation drawer is not open then open it, if its already open then close it.
                        if (!mainActivity.drawer.isDrawerOpen(GravityCompat.START))
                            mainActivity.drawer.openDrawer(Gravity.START);
                        else mainActivity.drawer.closeDrawer(Gravity.END);
                    }
                });

                preferences = getActivity().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                setApplicationlanguage = preferences.getString("setApplicationlanguage", "");
                login_token = preferences.getString("login_token", "");
                logedIn = preferences.getInt("logedIn", 0);
                Log.v("language_NF", setApplicationlanguage);
                Log.v("login_token_NF", login_token);
                Log.v("logedIn_NF", "" + logedIn);
                if (logedIn == 1) {
                    GET_ORDERHISTORY_API();
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(
                            getContext());

                    builder.setTitle(getString(R.string.LogIn));
                    builder.setMessage(getString(R.string.Please_login_to_continue));
                    builder.setCancelable(false);
                    builder.setNegativeButton(getString(R.string.NO),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.setPositiveButton(getString(R.string.YES),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    Intent in = new Intent(getActivity(), LogInActivity.class);
                                    getActivity().startActivity(in);
                                }
                            });


                    builder.show();
                }


            } else {
                rootView = inflater.inflate(R.layout.msg_no_internet, container, false);
            }
        } catch (Exception ex) {
            String msg = ex.getMessage().toString();
            Log.v("msg_Main", msg);
        }
        return rootView;
    }

    private void setOrderHistory() {


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerViewMyOrders);
        recyclerView.setLayoutManager(layoutManager);
        orderHistoryAdapter = new OrderHistoryAdapter(getActivity(), orderHistoryModel);
        recyclerView.setAdapter(orderHistoryAdapter);
    }

    public void GET_ORDERHISTORY_API() {

        ORDERHISTORY_URL = utilities.GetUrl() + "accounts/orders";
        if (ORDERHISTORY_URL != null && !ORDERHISTORY_URL.isEmpty() && !ORDERHISTORY_URL.equals("null")) {
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            StringRequest sr = new StringRequest(Request.Method.GET, ORDERHISTORY_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            orderHistoryModel.clear();
                            try {
                                if (response != null && !response.isEmpty() && !response.equals("null")) {
                                    //getting the whole json object from the response
                                    JSONObject obj = new JSONObject(response);
                                    if (obj.length() > 0) {
                                        String status = obj.getString("status");

                                        if (status.equals("200")) {
                                            JSONObject jsonObjectorders = obj.getJSONObject("orders");
                                            if (jsonObjectorders.length() > 0) {
                                                JSONObject jsondata = jsonObjectorders.getJSONObject("data");
                                                if (jsondata.length() > 0) {
                                                    Iterator keys = jsondata.keys();
                                                    while (keys.hasNext()) {
                                                        JSONObject obj_details = jsondata.getJSONObject((String) keys.next());
                                                        String id = obj_details.getString("id");
                                                        String reference = obj_details.getString("reference");
                                                        String total = obj_details.getString("total");
                                                        String created_at = obj_details.getString("created_at");

                                                        JSONObject status_obj = obj_details.getJSONObject("status");
                                                        String statusOrderhistory = status_obj.getString("name");

                                                        try {
                                                            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd H:mm");
                                                            final Date dateObj = sdf.parse(created_at);
                                                            //  System.out.println(dateObj);
                                                            created_at = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a").format(dateObj);
                                                        } catch (final ParseException e) {
                                                            e.printStackTrace();
                                                        }

                                                        orderHistoryModel.add(new OrderHistoryModel(id, created_at, reference, total, statusOrderhistory));


                                                        // do smth
                                                    }
                                                    setOrderHistory();
                                                } else {
                                                    linearOrderHistoryLayout.setVisibility(View.GONE);
                                                    linearOrderHistoryLayoutNotVisible.setVisibility(View.VISIBLE);
                                                }


                                            } else {
                                                Log.v("jsonObjectorders", "jsonObjectorders");
                                                linearOrderHistoryLayout.setVisibility(View.GONE);
                                                linearOrderHistoryLayoutNotVisible.setVisibility(View.VISIBLE);
                                            }


                                        } else {
                                            Toast.makeText(getActivity(), getString(R.string.Server_Error), Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Log.e("Response", "Response: ");
                                    }
                                } else {
                                    Log.e("ResponseString", "ResponseString:Empty");
                                }


                            } catch (JSONException e) {
                                System.out.println("Error " + e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("HttpClient", "error: " + error.toString());
                        }
                    }) {


                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("userToken", login_token);
                    params.put("lang", setApplicationlanguage);
                    Log.v("lang", setApplicationlanguage);
                    return params;
                }
            };
            queue.add(sr);
        } else {
            Log.v("Url", "No url");
        }

    }


}