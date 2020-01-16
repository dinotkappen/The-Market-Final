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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.the.marketapp.Adapter.Order_Summary_Adapter;
import com.the.marketapp.Model.Order_Summary_Model;
import com.the.marketapp.Other.Utilities;
import com.the.marketapp.R;
import com.qpay.qpayandroidsdk.base.QpaySdk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.cloudist.acplibrary.ACProgressFlower;

public class Order_Summary_Activity extends AppCompatActivity {
    String JSON_URL_PLACE_ORDER_API, JSON_URL_LIST_CART, JSON_URL_CHECKOUT_API, URL_PROMOCODE_API;
    Utilities utilities;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences preferences;
    String login_token, default_adrz, shipping_method_id, shippingCoast;
    Double grandTotal_dbl;
    String grandTotal_str;
    Button btn_placeorder;
    String minimum_cart_amount;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Order_Summary_Model> order_Summary_ModelList = new ArrayList<Order_Summary_Model>();
    private Order_Summary_Adapter order_Summary_adapter;
    RecyclerView recycler_view_orderSummary;
    TextView txt_PriceAll, txt_SubTotal, txt_DeliveryCharge, txt_GrandTotal, txtPromo;
    String map_adrz_flag;
    String name, adrz, city, phone, lat, lon, provinenceID, strEmail;
    TextView txt_Name, txt_Adrz1, txt_Provinence, txt_Phone;
    EditText edtPromoCode;
    LinearLayout linearPromoCodeAddBtnLayout;
    String promocodeStr, discount_amt;
    ImageView imgBtnPlus, imgBtnMinus;
    List<String> provinence_Values;
    List<String> provinence_ID;
    String setApplicationlanguage;
    String paymeny_method,delivery_time;
    String status_payment;
    String transactionId_payment;
    String orderId_payment;
    String amount_payment;
    String datetime_payment;
    String reason_payment;
    String transactionStatus_payment;
    int countVolley = 0;
    String finalAmount;
    final QpaySdk reqParams = new QpaySdk(this);
    // To recognize adrz from map or default and used different api for pasing address
    String apiMethod;
    //Used for Qpay integration
    String zone_id,zone_code,zone_name,zone_name_arabic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            if (utilities.isOnline(this)) {
                setContentView(R.layout.activity_order__summary_);
                btn_placeorder = (Button) findViewById(R.id.btn_placeorder);


                getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                getSupportActionBar().setCustomView(R.layout.actionbar_layout_without_menu);
                getSupportActionBar().setCustomView(getLayoutInflater().inflate(R.layout.actionbar_layout_without_menu, null),
                        new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));

                ImageView img_cart_actionbar = (ImageView) findViewById(R.id.img_cart_actionbarNomenu);
                TextView txt_actionbar_Title = (TextView) findViewById(R.id.txt_actionbar_TitleNomenu);
                txt_actionbar_Title.setText(R.string.ORDER_SUMMARY);
                img_cart_actionbar.setVisibility(View.INVISIBLE);


                LinearLayout linear_backArrow = (LinearLayout) findViewById(R.id.linear_backArrow);
                LinearLayout linearCartLalyoutNomenu = (LinearLayout) findViewById(R.id.linearCartLalyoutNomenu);
                linearCartLalyoutNomenu.setVisibility(View.INVISIBLE);
                linear_backArrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

                provinence_Values = new ArrayList<>();
                provinence_ID = new ArrayList<>();

                imgBtnPlus = (ImageView) findViewById(R.id.imgBtnPlus);
                imgBtnMinus = (ImageView) findViewById(R.id.imgBtnMinus);
                imgBtnMinus.setVisibility(View.GONE);
                imgBtnPlus.setVisibility(View.VISIBLE);
                txt_PriceAll = (TextView) findViewById(R.id.txt_PriceAll);
                txt_SubTotal = (TextView) findViewById(R.id.txt_SubTotal);
                txt_DeliveryCharge = (TextView) findViewById(R.id.txt_DeliveryCharge);
                txt_GrandTotal = (TextView) findViewById(R.id.txt_GrandTotal);
                txtPromo = (TextView) findViewById(R.id.txtPromo);

                txt_Name = (TextView) findViewById(R.id.txt_Name);
                txt_Adrz1 = (TextView) findViewById(R.id.txt_Adrz1);
                txt_Provinence = (TextView) findViewById(R.id.txt_Provinence);
                txt_Phone = (TextView) findViewById(R.id.txt_Phone);
                edtPromoCode = (EditText) findViewById(R.id.edtPromoCode);
                linearPromoCodeAddBtnLayout = (LinearLayout) findViewById(R.id.linearPromoCodeAddBtnLayout);


                preferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                setApplicationlanguage = preferences.getString("setApplicationlanguage", "");

                strEmail = preferences.getString("user_email", "");
                recycler_view_orderSummary = (RecyclerView) findViewById(R.id.recycler_view_orderSummary);
                login_token = preferences.getString("login_token", null);
                int logedIn = preferences.getInt("logedIn", 0);
                default_adrz = preferences.getString("default_adrz", "");


//        shipping_method_id = preferences.getString("shipping_method_id", null);
//        shippingCoast = preferences.getString("shippingCoast", null);

                Intent intent = getIntent();
                paymeny_method = intent.getStringExtra("paymeny_method");
                delivery_time= intent.getStringExtra("delivery_time");
                Log.v("paymeny_method", paymeny_method);
                Log.v("timeOrderSummary", delivery_time);
                map_adrz_flag = intent.getExtras().getString("map_adrz_flag");
                if (map_adrz_flag.equals("0")) {
                    //JSON_URL_CHECKOUT_API = utilities.GetUrl() + "checkout/step/saveShippingAddress?address_id=" + default_adrz;
                    name = intent.getExtras().getString("name_api");
                    adrz = intent.getExtras().getString("adrz_api");
                    city = intent.getExtras().getString("city_api");
                    phone = intent.getExtras().getString("phone_api");
                    apiMethod = "1";

                } else {
                    name = intent.getExtras().getString("name_map");
                    adrz = intent.getExtras().getString("adrz_Map");
                    city = intent.getExtras().getString("city_Map");
                    phone = intent.getExtras().getString("phone_Map");
                    lat = intent.getExtras().getString("lat");
                    lon = intent.getExtras().getString("lon");
                    apiMethod = "0";
                    //   JSON_URL_CHECKOUT_API = utilities.GetUrl() + "checkout/step/saveShippingAddress?address_1=" + name + "&address_2="
                    //      + adrz + "&country_id=174&province_id=" + city + "&phone=" + phone + "&latitude=" + lat + "&longitude=" + lon;


                }
                if (name != null && !name.isEmpty() && !name.equals("null")) {
                    txt_Name.setText(name);
                }
                if (adrz != null && !adrz.isEmpty() && !adrz.equals("null")) {
                    txt_Adrz1.setText(adrz);
                }
                if (city != null && !city.isEmpty() && !city.equals("null")) {
                    txt_Provinence.setText(city);
                }
                if (phone != null && !phone.isEmpty() && !phone.equals("null")) {
                    txt_Phone.setText(phone);
                }
                GetZoneAPI();
                imgBtnPlus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        promocodeStr = edtPromoCode.getText().toString();
                        if (promocodeStr != null && !promocodeStr.isEmpty() && !promocodeStr.equals("null")) {
                            PROMOCODE_API();
                            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        } else {
                            edtPromoCode.setError("Please eneter valid promocode");
                        }

                    }
                });
                imgBtnMinus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        edtPromoCode.setFocusable(true);
                        edtPromoCode.setText("");
                        txtPromo.setText("0");
                        txt_GrandTotal.setText(grandTotal_str);
                        imgBtnMinus.setVisibility(View.GONE);
                        imgBtnPlus.setVisibility(View.VISIBLE);
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(edtPromoCode, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
                btn_placeorder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (minimum_cart_amount != null && !minimum_cart_amount.isEmpty() && !minimum_cart_amount.equals("null")) {
                            Log.v("minimum_cart_amount",minimum_cart_amount);
                            Log.v("finalAmount",finalAmount);

                            if(Double.parseDouble(finalAmount)>=Double.parseDouble(minimum_cart_amount))
                            {
                                if (map_adrz_flag.equals("0")) {

                                } else {

                                }
                                if (paymeny_method != null && !paymeny_method.isEmpty() && !paymeny_method.equals("null")) {

                                    placeOrderApi();
                                }
                            }
                            else
                            {
                                final AlertDialog.Builder builder = new AlertDialog.Builder(
                                        Order_Summary_Activity.this);

                                builder.setTitle("Sorry");
                                builder.setMessage("Minimum amount should be "+minimum_cart_amount);
                                builder.setCancelable(false);

                                builder.setPositiveButton(getString(R.string.OK),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int which) {
                                                dialog.dismiss();
                                            }
                                        });


                                builder.show();
                            }



                        }


                    }
                });
            } else {
                setContentView(R.layout.msg_no_internet);
            }
        } catch (Exception ex) {
            Log.v("OrderSummary", ex.getMessage().toString());
        }


    }

    public void setOrderSummary() {
        try {
            order_Summary_adapter = new Order_Summary_Adapter(this, order_Summary_ModelList);

            recycler_view_orderSummary.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(this);
            recycler_view_orderSummary.setLayoutManager(mLayoutManager);
            recycler_view_orderSummary.setAdapter(order_Summary_adapter);
        } catch (Exception ex) {
            Log.v("setOrderSummary", ex.getMessage().toString());
        }
    }

    public void loadCartItems_api() {
        //creating a string request to send request to the url
        JSON_URL_LIST_CART = utilities.GetUrl() + "cart";

        final ACProgressFlower dialog = new ACProgressFlower.Builder(this)
                .build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Log.v("JSON_URL_CATEGORIES", JSON_URL_LIST_CART);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL_LIST_CART,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        Log.v("response", response);
                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            String status = obj.getString("status");
                            Log.v("status", status);

                            order_Summary_ModelList.clear();
                            if (status.equals("200")) {

                                JSONArray jsonArray_data = obj.getJSONArray("cartItems");
                                String cartCount = obj.getString("cartCount");
                                 minimum_cart_amount = obj.getString("minimum_cart_amount");
                                Log.v("minimum_cart_amount",minimum_cart_amount);
                                String subtotal = obj.getString("subtotal");
                                String total = obj.getString("total");
                                 finalAmount = total;

                                txt_PriceAll.setText(total + " " + getString(R.string._QAR));
                                txt_SubTotal.setText(subtotal + " " + getString(R.string._QAR));
                                txt_DeliveryCharge.setText(shippingCoast + " " + getString(R.string._QAR));
                                grandTotal_dbl = Double.parseDouble(subtotal) + Double.parseDouble(shippingCoast);
                                grandTotal_str = String.format("%.2f", grandTotal_dbl);
                                txt_GrandTotal.setText(grandTotal_str + " " + getString(R.string._QAR));

                                for (int i = 0; i < jsonArray_data.length(); i++) {
                                    JSONObject jsondata = jsonArray_data.getJSONObject(i);
                                    String id = jsondata.getString("id");
                                    String qty = jsondata.getString("qty");
                                    // JSONObject option=jsondata.getJSONObject("option");

                                    //*****************************
//                                    JSONObject option = new JSONObject(jsondata.getString("option"));
//                                    if(option.length()>0) {
//                                        Iterator keys = option.keys();
//                                        String params = String.valueOf(keys.next());
//                                        String params_value = option.getString(params);
//                                    }

                                    //**********************************************
//                                    while (keys.hasNext()) {
//                                       JSONObject obj_details = option.getJSONObject((String) keys.next());
//                                        String weight=obj_details
//                                        String para=weight;
//
//
//
//
//                                        // do smth
//                                    }

                                    // String Weight=option.getString("Weight");
                                    String item_price = jsondata.getString("price");
                                    String item_sub_total = jsondata.getString("sub_total");

                                    JSONObject jsonObject_Product = jsondata.getJSONObject("product");
                                    String product_Name = jsonObject_Product.getString("name");


                                    order_Summary_ModelList.add(new Order_Summary_Model
                                            (product_Name, item_price, qty, item_sub_total, subtotal, total));

                                }



                                setOrderSummary();


                            } else {
                                Toast.makeText(getApplicationContext(), "Server error, please contact the customer care service...", Toast.LENGTH_LONG).show();
                            }


                        } catch (JSONException e) {
                            dialog.dismiss();
                            if (e != null && !e.equals("null")) {
                                Log.v("errorgetMessage()", "errorCatch");
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        dialog.dismiss();
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


            /** Passing some request headers* */
            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("userToken", login_token);
                headers.put("lang", setApplicationlanguage);
                Log.v("login_token2", login_token);
                return headers;
            }

        };


        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        //adding the string request to request queue
        requestQueue.add(stringRequest);


    }

    public void placeOrderApi() {
        //creating a string request to send request to the url
        JSON_URL_PLACE_ORDER_API = utilities.GetUrl() + "place-order?";

        final ACProgressFlower dialog = new ACProgressFlower.Builder(this)
                .build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Log.v("PLACE_ORDER_API", JSON_URL_PLACE_ORDER_API);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL_PLACE_ORDER_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        Log.v("response_placeOrder", response);

                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            String status = obj.getString("status");
                            Log.v("status", status);

                            String success = obj.getString("success");

                            if (status.equals("200")) {

                                if (success.equals("1")) {

                                    if (paymeny_method.equals("cash_on_delivery")) {
                                        String order = obj.getString("referenceId");
                                        Intent in = new Intent(Order_Summary_Activity.this, ThankYouActivity.class);
                                        in.putExtra("orderId", order);
                                        startActivity(in);
                                        SharedPreferences sharedpreferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                                        final SharedPreferences.Editor editor = sharedpreferences.edit();
                                        editor.putString("cartCount", "0");
                                        editor.commit();
                                    } else {
                                        JSONObject orderjSONObject = obj.getJSONObject("order");
                                        String id_payment = orderjSONObject.getString("id");
                                        String order_payment = orderjSONObject.getString("reference");
                                        String total_payment = orderjSONObject.getString("total");
                                        JSONObject objbilling_address = obj.getJSONObject("billing_address");
                                        String city_payment = objbilling_address.getString("city");
                                        String name_payment = objbilling_address.getString("name");
                                        String phone_payment = objbilling_address.getString("phone");

                                        String address_1_payment = objbilling_address.getString("address_1");

                                        String gatewayid_payment = obj.getString("gatewayid");
                                        String secretkey_payment = obj.getString("secretkey");
                                        String mode_payment = obj.getString("mode");

                                        JSONObject objuser = obj.getJSONObject("user");
                                        String email = objuser.getString("email");

                                        reqParams.setCurrency("QAR");
                                        reqParams.setCountry("QA"); // 2 digit of country code
                                        reqParams.setCity(city_payment);
                                        reqParams.setState(city_payment);
                                        reqParams.setEmail(email);
                                        reqParams.setAddress(address_1_payment);
                                        reqParams.setName(name_payment);
                                        reqParams.setPhone(phone_payment);
                                        BigDecimal sPrice = new BigDecimal(total_payment);
                                        reqParams.setAmount(sPrice);
                                        reqParams.setDescription(order_payment);
                                        reqParams.setReferenceId(id_payment);
                                        reqParams.setGatewayId(gatewayid_payment);
                                        reqParams.setSecretKey(secretkey_payment);
                                        reqParams.setMode(mode_payment);

                                        // call payment method
                                        reqParams.doPayment();

                                    }

                                    //Set all required parameters


//


                                } else {
                                    String msg_error = obj.getString("msg");
                                    Log.v("msg_error", msg_error);
//

                                    AlertDialog.Builder builder = new AlertDialog.Builder(Order_Summary_Activity.this);
                                    builder.setMessage(msg_error)
                                            .setCancelable(false)
                                            .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    //do things
                                                    dialog.dismiss();
                                                    Intent in = new Intent(Order_Summary_Activity.this, MainActivity.class);
                                                    startActivity(in);
                                                }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }

                            } else {
                                Toast.makeText(getApplicationContext(), "Server error, please contact the customer care service...", Toast.LENGTH_LONG).show();
                            }


                        } catch (JSONException e) {
                            dialog.dismiss();
                            if (e != null && !e.equals("null")) {
                                Log.v("errorgetMessage()", "errorCatch");
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        dialog.dismiss();
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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("shipping_method_id", shipping_method_id);
                params.put("shipping_address", default_adrz);
                params.put("coupon", "");
                params.put("discount", "");
                params.put("billing_address", default_adrz);
                params.put("paymeny_method", paymeny_method);
                params.put("delivery_time",delivery_time);


                return params;
            }

            /** Passing some request headers* */
            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("userToken", login_token);
                headers.put("lang", setApplicationlanguage);
                Log.v("login_token2", login_token);
                return headers;
            }

        };


        //creating a request queue
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
        RequestQueue requestQueue = Volley.newRequestQueue(Order_Summary_Activity.this, new HurlStack());
        requestQueue.add(stringRequest).setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 5000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 0; //retry turn off
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        //adding the string request to request queue
        //  requestQueue.add(stringRequest);


    }

    public void checkout_Cart_Map_api() {
        JSON_URL_CHECKOUT_API = utilities.GetUrl() + "checkout/step/saveShippingAddress?";

        final ACProgressFlower dialog = new ACProgressFlower.Builder(this)
                .build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Log.v("JSON_URL_CHECKOUT_API", JSON_URL_CHECKOUT_API);
        RequestQueue queue = Volley.newRequestQueue(this);
//for POST requests, only the following line should be changed to

        StringRequest sr = new StringRequest(Request.Method.POST, JSON_URL_CHECKOUT_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            String status = obj.getString("status");
                            Log.v("status", status);

                            String success = obj.getString("success");

                            if (status.equals("200")) {

                                if (success.equals("1")) {

                                    JSONObject jsonAdrz = obj.getJSONObject("address");
                                    default_adrz=jsonAdrz.getString("id");


                                    JSONArray jsonArray_data = obj.getJSONArray("shipping_methods");


                                    for (int i = 0; i < jsonArray_data.length(); i++) {
                                        JSONObject jsondata = jsonArray_data.getJSONObject(i);
                                        shipping_method_id = jsondata.getString("id");
                                        shippingCoast = jsondata.getString("cost");
//
                                        loadCartItems_api();

                                    }

                                }

                            } else {
                                Toast.makeText(getApplicationContext(), "Server error, please contact the customer care service...", Toast.LENGTH_LONG).show();
                            }


                        } catch (JSONException e) {
                            dialog.dismiss();
                            if (e != null && !e.equals("null")) {
                                Log.v("errorgetMessage()", "errorCatch");
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
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
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("address_1", name);
                params.put("address_2", adrz);
                params.put("country_id", "174");
                params.put("province_id", provinenceID);
                params.put("phone", phone);
                params.put("latitude", lat);
                params.put("longitude", lon);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userToken", login_token);
                params.put("lang", setApplicationlanguage);
                return params;
            }
        };
        queue.add(sr);

    }

    public void checkout_Cart_api() {
        JSON_URL_CHECKOUT_API = utilities.GetUrl() + "checkout/step/saveShippingAddress?";

        final ACProgressFlower dialog = new ACProgressFlower.Builder(this)
                .build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Log.v("JSON_URL_CHECKOUT_API", JSON_URL_CHECKOUT_API);
        RequestQueue queue = Volley.newRequestQueue(this);
//for POST requests, only the following line should be changed to

        StringRequest sr = new StringRequest(Request.Method.POST, JSON_URL_CHECKOUT_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            String status = obj.getString("status");
                            Log.v("status", status);

                            String success = obj.getString("success");

                            if (status.equals("200")) {

                                if (success.equals("1")) {

                                    JSONArray jsonArray_data = obj.getJSONArray("shipping_methods");


                                    for (int i = 0; i < jsonArray_data.length(); i++) {
                                        JSONObject jsondata = jsonArray_data.getJSONObject(i);
                                        shipping_method_id = jsondata.getString("id");
                                        shippingCoast = jsondata.getString("cost");
//
                                        loadCartItems_api();

                                    }

                                }

                            } else {
                                Toast.makeText(getApplicationContext(), "Server error, please contact the customer care service...", Toast.LENGTH_LONG).show();
                            }


                        } catch (JSONException e) {
                            dialog.dismiss();
                            if (e != null && !e.equals("null")) {
                                Log.v("errorgetMessage()", "errorCatch");
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
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
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("address_id", default_adrz);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userToken", login_token);
                params.put("lang", setApplicationlanguage);
                return params;
            }
        };
        queue.add(sr);

    }

    public void PROMOCODE_API() {
        //creating a string request to send request to the url
        String URLPROMOCODE = utilities.GetUrl() + "apply-coupon";

        Log.v("URLPROMOCODE", URLPROMOCODE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLPROMOCODE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.v("response", response);
                        try {
                            if (response != null && !response.isEmpty() && !response.equals("null")) {
                                //getting the whole json object from the response
                                JSONObject obj = new JSONObject(response);

                                String status = obj.getString("status");
                                Log.v("status", status);

                                if (status.equals("200")) {
                                    String success = obj.getString("success");
                                    if (success.equals("1")) {
                                        String msg = obj.getString("msg");
                                        JSONObject couponObj = obj.getJSONObject("coupon");
                                        String discAmount = couponObj.getString("amount");

                                        Double discAppliedAmnt = Double.parseDouble(grandTotal_str) - Double.parseDouble(discAmount);
                                        txtPromo.setText(discAmount);
                                        txt_GrandTotal.setText("" + discAppliedAmnt + " QAR");
                                        edtPromoCode.setFocusable(false);
                                        imgBtnMinus.setVisibility(View.VISIBLE);
                                        imgBtnPlus.setVisibility(View.GONE);


                                        Log.v("msg", msg);
                                    } else {
                                        String msg = obj.getString("msg");
                                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                        edtPromoCode.setError(msg);
                                        imgBtnMinus.setVisibility(View.GONE);
                                        imgBtnPlus.setVisibility(View.VISIBLE);
                                        edtPromoCode.setFocusable(true);
                                        edtPromoCode.setText("");

                                    }

                                    //  set_Cart();
                                    //   set_Main_Items();
                                    // cart_item_adapter.notify();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Can't update count", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Log.v("responseStrUpdate", response);
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
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("coupon", edtPromoCode.getText().toString());
                return params;
            }


            /** Passing some request headers* */
            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("userToken", login_token);
                headers.put("lang", setApplicationlanguage);
                Log.v("login_token2", login_token);
                return headers;
            }

        };


        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);


    }

    private void GetZoneAPI() {

        String JSON_URL_GET_ZONE = utilities.GetUrl() + "zone/" + 174;
        Log.v("JSON_URL_GET_ZONE", JSON_URL_GET_ZONE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL_GET_ZONE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("response", response);

                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            String status = obj.getString("status");

                            if (status.equals("200")) {
                                JSONArray jsonArray_zone = obj.getJSONArray("zone");

                                for (int i = 0; i < jsonArray_zone.length(); i++) {
                                    JSONObject jsondata = jsonArray_zone.getJSONObject(i);
                                     zone_id = jsondata.getString("zone_id");
                                     zone_code = jsondata.getString("zone_code");
                                     zone_name = jsondata.getString("zone_name");
                                     zone_name_arabic = jsondata.getString("zone_name_arabic");


                                    provinence_ID.add(zone_id);
                                    provinence_Values.add(zone_name);
                                    String hj=zone_name;
                                    String kj=hj;


                                }
                                for (int j = 0; j < provinence_Values.size(); j++) {
                                    String lk=city;
                                    String prov=provinence_Values.get(j);

                                    if (city.equals(provinence_Values.get(j))) {
                                        provinenceID = provinence_ID.get(j);
                                        String h = provinenceID;
                                    }
                                }
//                                for(int j=0;j<provinence_Values.size();j++)
//                                {
//                                  if(city.equals(provinence_Values.get(j)))
//                                    {
//                                        provinenceID=provinence_ID.get(j);
//                                        String h=provinenceID;
//                                    }
//                                }
                                if (apiMethod.equals("1")) {
                                    checkout_Cart_api();
                                } else if (provinenceID != null && !provinenceID.isEmpty() && !provinenceID.equals("null")) {
                                    checkout_Cart_Map_api();
                                }

                                //  item_main_adapter.notifyDataSetChanged();
                            } else {

                                Toast.makeText(getApplicationContext(), getString(R.string.Server_Error), Toast.LENGTH_LONG).show();
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


            /** Passing some request headers* */
            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();

                Log.v("login_token2", login_token);
                headers.put("lang", setApplicationlanguage);
                return headers;
            }

        };


        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);


    }

    public void UpdateOrder() {
        //creating a string request to send request to the url
        String URLUpdateOrder = utilities.GetUrl() + "update-order";

        Log.v("URLUpdateOrder", URLUpdateOrder);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLUpdateOrder,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.v("response", response);
                        try {
                            if (response != null && !response.isEmpty() && !response.equals("null")) {
                                //getting the whole json object from the response
                                JSONObject obj = new JSONObject(response);

                                String status = obj.getString("status");
                                Log.v("status", status);

                                if (status.equals("200")) {
                                    String success = obj.getString("success");
                                    if (success.equals("1")) {
                                        String msg = obj.getString("msg");

                                        String referenceId = obj.getString("referenceId");

                                        Intent in = new Intent(Order_Summary_Activity.this, ThankYouActivity.class);
                                        in.putExtra("orderId", referenceId);
                                        startActivity(in);
                                        SharedPreferences sharedpreferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                                        final SharedPreferences.Editor editor = sharedpreferences.edit();
                                        editor.putString("cartCount", "0");
                                        editor.commit();

                                        Log.v("PaySuccess", "PaySuccess");
                                    } else {
                                        Log.v("PayFailed", "PayFailed");
                                    }

                                    //  set_Cart();
                                    //   set_Main_Items();
                                    // cart_item_adapter.notify();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Log.v("responseStrUpdate", response);
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
            protected Map<String, String> getParams() throws AuthFailureError {
                countVolley++;
                Log.v("countVolley", "" + countVolley);
                Map<String, String> params = new HashMap<>();

                params.put("status", status_payment);
                params.put("transactionId", transactionId_payment);
                params.put("orderId", orderId_payment);
                params.put("amount", amount_payment);
                params.put("datetime", datetime_payment);
                params.put("reason", reason_payment);
                params.put("transactionStatus", transactionStatus_payment);
                return params;
            }


            /** Passing some request headers* */
            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("userToken", login_token);
                headers.put("lang", setApplicationlanguage);
                Log.v("login_token2", login_token);
                return headers;
            }

        };


//        //creating a request queue
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//
//        //adding the string request to request queue
//        requestQueue.add(stringRequest);

        // VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

        queue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && data != null) {
            String dataString = data.getStringExtra("data");

            try {
                JSONObject paymentObj = new JSONObject(dataString);
                status_payment = paymentObj.getString("status");
                transactionId_payment = paymentObj.getString("transactionId");
                orderId_payment = paymentObj.getString("orderId");
                amount_payment = paymentObj.getString("amount");
                datetime_payment = paymentObj.getString("datetime");
                reason_payment = paymentObj.getString("reason");
                transactionStatus_payment = paymentObj.getString("transactionStatus");
            } catch (JSONException e) {
                Log.v("APIFailed", "APIFailed");
                e.printStackTrace();
            }

            //Toast.makeText(paymentRequest.this, dataString, Toast.LENGTH_SHORT).show();
            // SdkUtils.showAlertPopUp(this, dataString);
            Log.v("APICalled", "APICalled");
            UpdateOrder();
        }
    }
}
