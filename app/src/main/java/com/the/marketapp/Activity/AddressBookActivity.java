package com.the.marketapp.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.the.marketapp.Adapter.Adrz_Book_Adapter;

import com.the.marketapp.Model.Adrz_Book_Model;
import com.the.marketapp.Model.Provinence_Model;
import com.the.marketapp.Other.SwipeControllerActions;
import com.the.marketapp.Other.Utilities;
import com.the.marketapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.cloudist.acplibrary.ACProgressFlower;

public class AddressBookActivity extends AppCompatActivity {

    String from;
    String adrz_ID;
    String login_token;
    Utilities utilities;
    String province_name;
    ImageView add_adrz_img;
     ACProgressFlower dialog ;
    List<String> Country_ID_List;
    SharedPreferences preferences;
    String setApplicationlanguage;
    List<String> provinence_ID_List;
    List<String> Country_Values_List;
    List<String> provinence_Values_List;
    RecyclerView recyclerView_adrz_book;
    SwipeController swipeControllerAdrz = null;
    private Adrz_Book_Adapter adrz_book_adapter;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    HashMap<String, String> Provinence_HashMap = new HashMap<String, String>();
    ArrayList<Adrz_Book_Model> data_adrz_Model = new ArrayList<Adrz_Book_Model>();
    ArrayList<Provinence_Model> provinence_models = new ArrayList<Provinence_Model>();
    ArrayList<HashMap<String, String>> Provinence_ArrayList = new ArrayList<HashMap<String, String>>();
    String JSON_URL_ALL_ADRZ, JSON_URL_GET_ZONE, JSON_URL_GET_COUNTRY, JSON_URL_SET_DFLT_ADRZ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            if (utilities.isOnline(this)) {
                setContentView(R.layout.activity_address_book);
                getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                getSupportActionBar().setCustomView(R.layout.actionbar_layout_without_menu);
                getSupportActionBar().setCustomView(getLayoutInflater().inflate(R.layout.actionbar_layout_without_menu, null),
                        new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));

                ImageView img_cart_actionbar = (ImageView) findViewById(R.id.img_cart_actionbarNomenu);
                TextView txt_actionbar_Title = (TextView) findViewById(R.id.txt_actionbar_TitleNomenu);
                LinearLayout linearCartLalyoutNomenu = (LinearLayout) findViewById(R.id.linearCartLalyoutNomenu);
                linearCartLalyoutNomenu.setVisibility(View.INVISIBLE);
                LinearLayout linear_backArrow = (LinearLayout) findViewById(R.id.linear_backArrow);

                Intent intent = getIntent();
                from = intent.getStringExtra("from");

                recyclerView_adrz_book = (RecyclerView) findViewById(R.id.recyclerView_adrz_book);
                txt_actionbar_Title.setText(getString(R.string.ADDRESS));
                ImageView imgBackArrow = (ImageView) findViewById(R.id.imgBackArrowNomenu);
                imgBackArrow.setVisibility(View.VISIBLE);

                linear_backArrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
                img_cart_actionbar.setVisibility(View.INVISIBLE);

                TextView txtCartCount = (TextView) findViewById(R.id.txtCartCountNomenu);
                txtCartCount.setVisibility(View.INVISIBLE);
                add_adrz_img = (ImageView) findViewById(R.id.add_adrz_img);


                JSON_URL_ALL_ADRZ = utilities.GetUrl();
                JSON_URL_GET_ZONE = utilities.GetUrl() + "zone/";
                JSON_URL_GET_COUNTRY = utilities.GetUrl() + "countries";

                preferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                login_token = preferences.getString("login_token", "");
                setApplicationlanguage = preferences.getString("setApplicationlanguage", "");
                Log.v("setApplicationlanguage", setApplicationlanguage);
                int logedIn = preferences.getInt("logedIn", 0);
                Log.v("logedIn", "" + logedIn);
                if (logedIn == 1) {
                    try {
                        provinence_Values_List = new ArrayList<>();
                        provinence_ID_List = new ArrayList<>();
                        Country_Values_List = new ArrayList<>();
                        Country_ID_List = new ArrayList<>();

                        add_adrz_img.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Intent in = new Intent(AddressBookActivity.this, AddAddressActivity.class);
                                    in.putExtra("from", "AddressBookActivity");
                                    startActivity(in);
                                    finish();
                                } catch (Exception ex) {
                                    String msg = ex.getMessage().toString();
                                    Log.v("add_adrz_img", msg);
                                }
                            }
                        });
                        GetCountry_API();

                        swipeControllerAdrz = new SwipeController(new SwipeControllerActions() {
                            @Override
                            public void onRightClicked(int position) {
                                try {

                                    adrz_ID = data_adrz_Model.get(position).getAdrz_ID();
                                    Delete_AdrzApi(adrz_ID);
                                    adrz_book_adapter.adrzModelList.remove(position);
                                    adrz_book_adapter.notifyItemRemoved(position);
                                    adrz_book_adapter.notifyItemRangeChanged(position, adrz_book_adapter.getItemCount());

                                } catch (Exception ex) {
                                    adrz_ID = data_adrz_Model.get(0).getAdrz_ID();
                                    String j = ex.getMessage().toString();
                                    Log.v("j", j);
                                    Delete_AdrzApi(adrz_ID);
                                }
                            }
                        }, this);

                        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeControllerAdrz);
                        itemTouchhelper.attachToRecyclerView(recyclerView_adrz_book);

                        recyclerView_adrz_book.addItemDecoration(new RecyclerView.ItemDecoration() {
                            @Override
                            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

                                swipeControllerAdrz.onDraw(c);
                            }
                        });
                    } catch (Exception ex) {
                        String msg = ex.getMessage().toString();
                        Log.v("msg_MainAdrzBook", msg);
                    }

                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(
                            this);

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
                                    Intent in = new Intent(AddressBookActivity.this, LogInActivity.class);
                                    startActivity(in);
                                }
                            });


                    builder.show();
                }
            } else {
                setContentView(R.layout.msg_no_internet);
            }
        } catch (Exception ex) {
            String msg = ex.getMessage().toString();
            Log.v("msg_Main", msg);
        }
    }

    private void set_Adrz_Book() {
        try {
            adrz_book_adapter = new Adrz_Book_Adapter(this, data_adrz_Model, provinence_ID_List, provinence_Values_List);
            Log.v("data_adrz_Model", "" + data_adrz_Model.size());

            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
            recyclerView_adrz_book.setLayoutManager(mLayoutManager);
            recyclerView_adrz_book.setItemAnimator(new DefaultItemAnimator());
            recyclerView_adrz_book.setAdapter(adrz_book_adapter);
        } catch (Exception ex) {
            String msg = ex.getMessage().toString();
            Log.v("set_Adrz_Book", msg);
        }
    }

    private void GetZoneAPI() {

        JSON_URL_GET_ZONE = JSON_URL_GET_ZONE + Country_ID_List.get(0);
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
                                    String zone_id = jsondata.getString("zone_id");
                                    String zone_code = jsondata.getString("zone_code");
                                    String zone_name = jsondata.getString("zone_name");
                                    String zone_name_arabic = jsondata.getString("zone_name_arabic");

                                    Provinence_HashMap.put("zone_id", zone_id);
                                    Provinence_HashMap.put("zone_name", zone_name);
                                    Provinence_ArrayList.add(Provinence_HashMap);
                                    provinence_models.add(new Provinence_Model(zone_id, zone_name));


                                }

                                All_AdrzApi();

                                //  item_main_adapter.notifyDataSetChanged();
                            } else {

                                Toast.makeText(getApplicationContext(), "Server error, please contact the customer care service...", Toast.LENGTH_LONG).show();
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
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("lang", setApplicationlanguage);
                return headers;
            }

        };


        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);


    }

    private void GetCountry_API() {

        JSON_URL_GET_COUNTRY = JSON_URL_GET_COUNTRY;
        Log.v("JSON_URL_GET_ZONE", JSON_URL_GET_ZONE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL_GET_COUNTRY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("response", response);

                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            String status = obj.getString("status");

                            if (status.equals("200")) {
                                JSONArray jsonArray_countries = obj.getJSONArray("countries");

                                for (int i = 0; i < jsonArray_countries.length(); i++) {
                                    JSONObject jsondata = jsonArray_countries.getJSONObject(i);
                                    String id = jsondata.getString("id");
                                    String name = jsondata.getString("name");

                                    Country_ID_List.add(id);
                                    Country_Values_List.add(name);


                                }
                                GetZoneAPI();

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

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("lang", setApplicationlanguage);
                return headers;
            }

        };


        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);


    }

    private void All_AdrzApi() {


        try {
            dialog = new ACProgressFlower.Builder(this)
                    .build();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSON_URL_ALL_ADRZ = JSON_URL_ALL_ADRZ + "accounts/address";
        Log.v("JSON_URL_ALL_ADRZ", JSON_URL_ALL_ADRZ);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL_ALL_ADRZ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("response", response);
                        dialog.dismiss();
                        SharedPreferences sharedpreferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            String status = obj.getString("status");

                            if (status.equals("200")) {

                                data_adrz_Model.clear();
                                JSONArray jsonArray_address = obj.getJSONArray("address");
                                if (jsonArray_address.length() == 0) {
                                    Intent in = new Intent(AddressBookActivity.this, AddAddressActivity.class);
                                    in.putExtra("AdrzCount", "0");
                                    in.putExtra("from", "AddressBookActivity");
                                    startActivity(in);
                                    finish();


                                } else {
                                    for (int i = 0; i < jsonArray_address.length(); i++) {
                                        JSONObject jsondata = jsonArray_address.getJSONObject(i);
                                        String id = jsondata.getString("id");
                                        String alias = jsondata.getString("alias");
                                        String name = jsondata.getString("name");
                                        String address_1 = jsondata.getString("address_1");
                                        String address_2 = jsondata.getString("address_2");
                                        String province_id = jsondata.getString("province_id");
                                        String country_id = jsondata.getString("country_id");
                                        String customer_id = jsondata.getString("customer_id");
                                        String phone = jsondata.getString("phone");
                                        String is_default = jsondata.getString("is_default");
                                        String latitude = jsondata.getString("latitude");
                                        String longitude = jsondata.getString("longitude");

                                        editor.putString("default_adrz", id);
                                        editor.apply();

                                        for (int k = 0; k < provinence_models.size(); k++) {
                                            if (provinence_models.get(k).getProvinence_ID().equals(province_id)) {
                                                province_name = provinence_models.get(k).getProvinence_Name();
                                                String u = province_name;
                                                Log.v("province_name", province_name);
                                            }
                                        }


                                        data_adrz_Model.add(new Adrz_Book_Model(jsondata.getString("id"), jsondata.getString("alias"),
                                                jsondata.getString("name"), jsondata.getString("address_1"), jsondata.getString("address_2"),
                                                province_name, jsondata.getString("country_id"), jsondata.getString("customer_id"),
                                                jsondata.getString("phone"), jsondata.getString("is_default"), jsondata.getString("latitude"),
                                                jsondata.getString("longitude")));


                                    }
                                    set_Adrz_Book();
                                }


                                //  item_main_adapter.notifyDataSetChanged();
                            } else {
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(), getString(R.string.Server_Error), Toast.LENGTH_LONG).show();
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
                Log.v("login_token_Adrz", login_token);
                return headers;
            }

        };


        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);


    }

    public void Delete_AdrzApi(String adrz_ID) {

        final ACProgressFlower dialog = new ACProgressFlower.Builder(this)
                .build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        String JSON_URL_DELETE = utilities.GetUrl() + "accounts/address/delete/" + adrz_ID;


        Log.v("JSON_URL_DELETE", JSON_URL_DELETE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL_DELETE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("response", response);
                        dialog.dismiss();
                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            String status = obj.getString("status");
                            String success = obj.getString("success");
                            String msg = obj.getString("msg");
                            if (status.equals("200")) {

                                if (success.equals("1")) {
//                                    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
//                                    Fragment fragment = new Address_Book_Fragment();
//                                    FragmentManager fragmentManager = ((AppCompatActivity) getContext()).getSupportFragmentManager();
//                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                                    fragmentTransaction.replace(R.id.fragment_container, fragment);
//                                    fragmentTransaction.addToBackStack(null);
//                                    fragmentTransaction.commit();
                                    // All_AdrzApi();

                                    adrz_book_adapter.notifyDataSetChanged();
                                    if (data_adrz_Model.size() > 0) {

                                    } else {
                                        Intent in = new Intent(AddressBookActivity.this, AddAddressActivity.class);
                                        in.putExtra("from", "AddressBookActivity");
                                        in.putExtra("AdrzCount", "0");
                                        startActivity(in);

                                    }
                                    Log.v("msg", msg);
                                }


                                //  item_main_adapter.notifyDataSetChanged();
                            } else {
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(), getString(R.string.Server_Error), Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);


    }

    public void setDefaultAdrzAPI(String adrz_ID) {

        JSON_URL_SET_DFLT_ADRZ = utilities.GetUrl() + "address/set-default/" + adrz_ID;
        Log.v("JSON_URL_SET_DFLT_ADRZ", JSON_URL_SET_DFLT_ADRZ);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL_SET_DFLT_ADRZ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("response", response);

                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            String status = obj.getString("status");

                            if (status.equals("200")) {


                                String msg = obj.getString("msg");
                                Log.v("msg", msg);
                                //adrz_book_adapter.notifyDataSetChanged();
                                if (from != null && !from.isEmpty() && !from.equals("null")) {
                                    if (from.equals("MapsActivity")) {
                                        Intent in = new Intent(AddressBookActivity.this, MapsActivity.class);
                                        startActivity(in);
                                        finish();
                                    } else {
                                        Intent in = new Intent(AddressBookActivity.this, AddressBookActivity.class);
                                        startActivity(in);
                                        finish();
                                    }

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
}

