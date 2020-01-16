package com.the.marketapp.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.the.marketapp.Adapter.SingleOrderHistoryAdapter;
import com.the.marketapp.Model.SingleOrderHistoryModel;
import com.the.marketapp.Other.Utilities;
import com.the.marketapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.the.marketapp.Adapter.Item_Main_Adapter.MY_PREFS_NAME;

public class SingleOrderHistoryActivity extends AppCompatActivity {
    ArrayList<SingleOrderHistoryModel> singleOrderHistoryModel = new ArrayList<SingleOrderHistoryModel>();
    SingleOrderHistoryAdapter singleOrderHistoryAdapter;
    String SINGLEORDERHISTORY_URL;
    Utilities utilities;
    SharedPreferences preferences;
    String setApplicationlanguage, login_token;
    int logedIn;
    String orderHistoryID;
    LinearLayout linearSingleOrderHistory, linearSingleOrderHistoryLayoutNotVisible;
    TextView txtSubTotalOrderSingle, txtShippingChargeOrderSingle, txtGrandTotalOrderSingle, txtNameOrderSingle, txtAdrz1OrderSingle, txtProvinenceOrderSingle, txtPhoneOrderSingle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            if (utilities.isOnline(this)) {
                setContentView(R.layout.activity_single_order_history);

                linearSingleOrderHistory = (LinearLayout) findViewById(R.id.linearSingleOrderHistory);
                linearSingleOrderHistoryLayoutNotVisible = (LinearLayout) findViewById(R.id.linearSingleOrderHistoryLayoutNotVisible);
                linearSingleOrderHistory.setVisibility(View.VISIBLE);
                linearSingleOrderHistoryLayoutNotVisible.setVisibility(View.GONE);

                getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                getSupportActionBar().setCustomView(R.layout.actionbar_layout_without_menu);
                getSupportActionBar().setCustomView(getLayoutInflater().inflate(R.layout.actionbar_layout_without_menu, null),
                        new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));
                TextView txt_actionbar_Title = (TextView) findViewById(R.id.txt_actionbar_TitleNomenu);
                txt_actionbar_Title.setText(getString(R.string.OREDER_DETAILS));

                LinearLayout linearCartLalyoutNomenu = (LinearLayout) findViewById(R.id.linearCartLalyoutNomenu);
                linearCartLalyoutNomenu.setVisibility(View.INVISIBLE);
                LinearLayout linear_backArrow = (LinearLayout) findViewById(R.id.linear_backArrow);
                linear_backArrow.setVisibility(View.VISIBLE);

                linear_backArrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });


                preferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                setApplicationlanguage = preferences.getString("setApplicationlanguage", "");
                login_token = preferences.getString("login_token", "");
                logedIn = preferences.getInt("logedIn", 0);
                Log.v("language_NF", setApplicationlanguage);
                Log.v("login_token_NF", login_token);
                Log.v("logedIn_NF", "" + logedIn);


                txtSubTotalOrderSingle = (TextView) findViewById(R.id.txtSubTotalOrderSingle);
                txtShippingChargeOrderSingle = (TextView) findViewById(R.id.txtShippingChargeOrderSingle);
                txtGrandTotalOrderSingle = (TextView) findViewById(R.id.txtGrandTotalOrderSingle);
                txtNameOrderSingle = (TextView) findViewById(R.id.txtNameOrderSingle);
                txtAdrz1OrderSingle = (TextView) findViewById(R.id.txtAdrz1OrderSingle);
                txtProvinenceOrderSingle = (TextView) findViewById(R.id.txtProvinenceOrderSingle);
                txtPhoneOrderSingle = (TextView) findViewById(R.id.txtPhoneOrderSingle);

                Intent intent = getIntent();
                orderHistoryID = intent.getStringExtra("orderHistoryID");

                if (logedIn == 1) {
                    if (orderHistoryID != null && !orderHistoryID.isEmpty() && !orderHistoryID.equals("null")) {
                        GET_SINGLE_ORDERHISTORY_API();
                    }
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(
                            SingleOrderHistoryActivity.this);

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
                                    Intent in = new Intent(SingleOrderHistoryActivity.this, LogInActivity.class);
                                    startActivity(in);
                                }
                            });


                    builder.show();
                }
            } else {
                setContentView(R.layout.msg_no_internet);
            }
        } catch (
                Exception ex) {
            String s = ex.getMessage().toString();
            String h = s;
        }
    }

    private void setSingleOrderHistory() {


        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewSingleOrderHistory);
        recyclerView.setLayoutManager(layoutManager);
        singleOrderHistoryAdapter = new SingleOrderHistoryAdapter(this, singleOrderHistoryModel);
        recyclerView.setAdapter(singleOrderHistoryAdapter);
    }


    public void GET_SINGLE_ORDERHISTORY_API() {
        if (orderHistoryID != null && !orderHistoryID.isEmpty() && !orderHistoryID.equals("null")) {
            SINGLEORDERHISTORY_URL = utilities.GetUrl() + "accounts/order/" + orderHistoryID;
        }


        if (SINGLEORDERHISTORY_URL != null && !SINGLEORDERHISTORY_URL.isEmpty() && !SINGLEORDERHISTORY_URL.equals("null")) {
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest sr = new StringRequest(Request.Method.GET, SINGLEORDERHISTORY_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            singleOrderHistoryModel.clear();
                            try {
                                if (response != null && !response.isEmpty() && !response.equals("null")) {
                                    //getting the whole json object from the response
                                    JSONObject obj = new JSONObject(response);
                                    if (obj.length() > 0) {
                                        JSONObject status_OBJ = obj.getJSONObject("status");
                                        String status = status_OBJ.getString("name");

                                        JSONObject order_OBJ = obj.getJSONObject("order");
                                        String reference = order_OBJ.getString("reference");
                                        String subTotal = order_OBJ.getString("total_products");
                                        String shipping_charge = order_OBJ.getString("shipping_charge");
                                        String total = order_OBJ.getString("total");
                                        String payment = order_OBJ.getString("payment");

                                        txtSubTotalOrderSingle.setText(getString(R.string._QAR) + " " + subTotal);
                                        txtShippingChargeOrderSingle.setText(getString(R.string._QAR) + " " + shipping_charge);
                                        txtGrandTotalOrderSingle.setText(getString(R.string._QAR) + " " + total);

                                        JSONObject address_OBJ = obj.getJSONObject("shipping_address");
                                        String name = address_OBJ.getString("name");
                                        String address_1 = address_OBJ.getString("address_1");
                                        String phone = address_OBJ.getString("phone");
                                        String city = address_OBJ.getString("city");
                                        String country = address_OBJ.getString("country");

                                        if (name != null && !name.isEmpty() && !name.equals("null")) {
                                            txtNameOrderSingle.setText(name);
                                        }

                                        if (address_1 != null && !address_1.isEmpty() && !address_1.equals("null")) {
                                            txtAdrz1OrderSingle.setText(address_1);
                                        }
                                        if (phone != null && !phone.isEmpty() && !phone.equals("null")) {
                                            txtPhoneOrderSingle.setText(phone);
                                        }
                                        if (country != null && !country.isEmpty() && !country.equals("null")) {
                                            txtProvinenceOrderSingle.setText(city + "," + country);
                                        }






//                                        JSONObject province_OBJ = address_OBJ.getJSONObject("province");
//                                        String zone_name=province_OBJ.getString("zone_name");

                                        JSONArray products_Array = obj.getJSONArray("products");
                                        for (int i = 0; i < products_Array.length(); i++) {
                                            JSONObject jsondata = products_Array.getJSONObject(i);
                                            String id = jsondata.getString("id");
                                            String product_name = jsondata.getString("name");
                                            String cover = jsondata.getString("cover");

                                            JSONObject pivot_obj = jsondata.getJSONObject("pivot");
                                            String quantity = pivot_obj.getString("quantity");
                                            String product_price = pivot_obj.getString("product_price");

                                            singleOrderHistoryModel.add(new SingleOrderHistoryModel(cover, quantity, product_price, product_name));

                                        }

                                        setSingleOrderHistory();

                                    } else {
                                        Log.e("Response", "Response: ");
                                    }
                                } else {
                                    Log.e("ResponseString", "ResponseString:Empty");
                                }


                            } catch (JSONException e) {

                                if (e != null && !e.equals("null")) {
                                    Log.v("errorgetMessage()", "errorCatch");
                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                Log.v("TimeoutError", "TimeoutError");
                                //This indicates that the reuest has either time out or there is no connection
                            } else if (error instanceof AuthFailureError) {
                                Log.v("AuthFailureError", "AuthFailureError");
                                //Error indicating that there was an Authentication Failure while performing the request
                            } else if (error instanceof ServerError) {
                                Log.v("ServerError", "ServerError");
                                //Indicates that the server responded with a error response
                            } else if (error instanceof NetworkError) {
                                Log.v("NetworkErrorParseError", "NetworkError");
                                //Indicates that there was network error while performing the request
                            } else if (error instanceof ParseError) {
                                Log.v("ParseError", "ParseError");
                                // Indicates that the server response could not be parsed
                            }
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
