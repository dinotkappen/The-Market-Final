package com.the.marketapp.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import com.the.marketapp.Other.NetworkChangeReceiver;
import com.the.marketapp.Other.Utilities;
import com.the.marketapp.R;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.cloudist.acplibrary.ACProgressFlower;

public class AddAddressActivity extends AppCompatActivity {

    ImageView img_chk_box;
    Button btn_Save_Adrz;
    private RadioGroup radioGroup_AdrzType;
    Utilities utilities;
    String login_token;
    String title, desc;
    SharedPreferences preferences;
    Button btn_Submit;
    String name, adrz, region, phone;
    String setApplicationlanguage;
    Dialog provinence_dilg;
    ListView provinence_listView;
    List<String> provinence_Values;
    List<String> provinence_ID;
    List<String> Country_Values;
    List<String> Country_ID;
    int default_flag = 0;
    String alias = "home";
    String AdrzCount;
    String from;
    String selectLang;
    String selectedProvinenceID;
    static LinearLayout NoInternet;
    private NetworkChangeReceiver receiver;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    EditText edt_adrz_Name, edt_adrz_Address, edt_adrz_Region, edt_adrz_Phone;
    private static String JSON_URL_ADD_ADRZ, JSON_URL_GET_ZONE, JSON_URL_GET_COUNTRY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        Hawk.init(getApplicationContext())
                .setEncryptionMethod(HawkBuilder.EncryptionMethod.MEDIUM)
                .setStorage(HawkBuilder.newSqliteStorage(getApplicationContext()))
                .setLogLevel(LogLevel.FULL)
                .build();
        NoInternet = findViewById(R.id.linearCartNotVisible);
        Hawk.put("pages", "address");
        selectLang = Hawk.get("selectLang", "");
//        try {
//            if (utilities.isOnline(this)) {



                IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
                receiver = new NetworkChangeReceiver();
                registerReceiver(receiver, filter);

                getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                getSupportActionBar().setCustomView(R.layout.actionbar_layout_without_menu);
                getSupportActionBar().setCustomView(getLayoutInflater().inflate(R.layout.actionbar_layout_without_menu, null),
                        new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));

                ImageView img_cart_actionbar = (ImageView) findViewById(R.id.img_cart_actionbarNomenu);
                TextView txt_actionbar_Title = (TextView) findViewById(R.id.txt_actionbar_TitleNomenu);
                LinearLayout linearCartLalyoutNomenu = (LinearLayout) findViewById(R.id.linearCartLalyoutNomenu);
                linearCartLalyoutNomenu.setVisibility(View.INVISIBLE);
                LinearLayout linear_backArrow = (LinearLayout) findViewById(R.id.linear_backArrow);


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

                img_chk_box = (ImageView) findViewById(R.id.img_chk_box);
                btn_Save_Adrz = (Button) findViewById(R.id.btn_Save_Adrz);
                edt_adrz_Name = (EditText) findViewById(R.id.edt_adrz_Name);
                edt_adrz_Address = (EditText) findViewById(R.id.edt_adrz_Address);
                edt_adrz_Region = (EditText) findViewById(R.id.edt_adrz_Region);
                edt_adrz_Phone = (EditText) findViewById(R.id.edt_adrz_Phone);
                radioGroup_AdrzType = (RadioGroup) findViewById(R.id.radioGroup_AdrzType);

                if (selectLang.equals("en")) {
                    edt_adrz_Name.setGravity(Gravity.LEFT);
                    edt_adrz_Address.setGravity(Gravity.LEFT);
                    edt_adrz_Region.setGravity(Gravity.LEFT);
                    edt_adrz_Phone.setGravity(Gravity.LEFT);
                } else {
                    edt_adrz_Name.setGravity(Gravity.RIGHT);
                    edt_adrz_Address.setGravity(Gravity.RIGHT);
                    edt_adrz_Region.setGravity(Gravity.RIGHT);
                    edt_adrz_Phone.setGravity(Gravity.RIGHT);
                }

                JSON_URL_ADD_ADRZ = utilities.GetUrl() + "accounts/address/create?";
                preferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                login_token = preferences.getString("login_token", null);
                JSON_URL_GET_ZONE = utilities.GetUrl() + "zone/";
                JSON_URL_GET_COUNTRY = utilities.GetUrl() + "countries";
                setApplicationlanguage = preferences.getString("setApplicationlanguage", "");
                int logedIn = preferences.getInt("logedIn", 0);
                Log.v("logedIn", "" + logedIn);
                if (logedIn == 1) {
                    try {
                        Intent intent = getIntent();
                        from = intent.getStringExtra("from");
                        provinence_Values = new ArrayList<>();
                        provinence_ID = new ArrayList<>();
                        Country_Values = new ArrayList<>();
                        Country_ID = new ArrayList<>();

                        provinence_dilg = new Dialog(this);
                        provinence_dilg.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        provinence_dilg.setContentView(R.layout.dialog_provinence);
                        provinence_dilg.setCanceledOnTouchOutside(false);
                        provinence_listView = (ListView) provinence_dilg.findViewById(R.id.provinence_listView);


                        AdrzCount = intent.getStringExtra("AdrzCount");


                        edt_adrz_Region.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                provinence_dilg.show();
                            }
                        });

                        // Create an ArrayAdapter from List
                        ArrayAdapter<String> adapter_State = new ArrayAdapter<String>
                                (this, android.R.layout.simple_list_item_1, provinence_Values) {
                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                // Get the Item from ListView
                                View view = super.getView(position, convertView, parent);

                                // Initialize a TextView for ListView each Item
                                TextView txt_Provinence = (TextView) view.findViewById(android.R.id.text1);

                                // Set the text color of TextView (ListView Item)
                                txt_Provinence.setTextColor(getResources().getColor(R.color.colorBlack));

                                // Generate ListView Item using TextView
                                return view;
                            }
                        };


                        provinence_listView.setAdapter(adapter_State);

                        // Capture ListView item click
                        provinence_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            public void onItemClick(AdapterView<?> parent, View view,
                                                    int position, long id) {
                                try {
                                    // Capture the click position and set it into a string
                                    String phones = (String) provinence_listView.getItemAtPosition(position);
                                    edt_adrz_Region.setText((String) provinence_listView.getItemAtPosition(position));
//                input_State.setText(phones);
                                    selectedProvinenceID = provinence_ID.get(position);
                                    provinence_dilg.dismiss();

                                } catch (Exception ex) {
                                    String msg = ex.getMessage().toString();
                                    Log.v("provinence_listView", msg);
                                }
                            }
                        });
                        //radioGroup_AdrzType.clearCheck();

                        img_chk_box.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    if (default_flag == 0) {
                                        img_chk_box.setImageResource(R.mipmap.ic_round_tick);
                                        default_flag = 1;
                                    } else {
                                        img_chk_box.setImageResource(R.mipmap.ic_radio_not_selected);
                                        default_flag = 0;
                                    }
                                } catch (Exception ex) {
                                    String msg = ex.getMessage().toString();
                                    Log.v("img_chk_box", msg);
                                }

                            }
                        });
                        radioGroup_AdrzType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                try {
                                    RadioButton rb = (RadioButton) group.findViewById(checkedId);
                                    if (null != rb && checkedId > -1) {
                                        alias = rb.getText().toString();
                                        Log.v("alias", alias);
                                        String gh = alias;
                                        //Toast.makeText(getActivity(), rb.getText(), Toast.LENGTH_SHORT).show();
                                    }

                                } catch (Exception ex) {
                                    String msg = ex.getMessage().toString();
                                    Log.v("radioGroup_AdrzType", msg);
                                }
                            }


                        });
                        btn_Save_Adrz.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                Intent in=new Intent(getActivity(), MapsActivity.class);
//                startActivity(in);
                                try {
                                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                                    submitForm();
                                } catch (Exception ex) {
                                    String msg = ex.getMessage().toString();
                                    Log.v("btn_Save_Adrz", msg);
                                }

                            }
                        });
                        GetCountry_API();
                    } catch (Exception ex) {
                        String msg = ex.getMessage().toString();
                        Log.v("msg_MainLogIn", msg);
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
                                    Intent in = new Intent(AddAddressActivity.this, LogInActivity.class);
                                    startActivity(in);
                                }
                            });


                    builder.show();
                }
                NoInternet.setVisibility(View.GONE);
//            } else {
//                NoInternet.setVisibility(View.VISIBLE);
////                setContentView(R.layout.msg_no_internet);
//            }
//        } catch (Exception ex) {
//            String msg = ex.getMessage().toString();
//            Log.v("msg_MainAdrz", msg);
//        }
    }

    /**
     * Validating form
     */
    private void submitForm() {
        try {
            if (!validateName()) {
                return;
            }

            if (!validateAdrz()) {
                return;
            }


            if (!validateRegion()) {
                return;
            }


            if (!validatePhone()) {
                return;
            }

            Add_AdrzApi();
        } catch (Exception ex) {
            String msg = ex.getMessage().toString();
            Log.v("submitForm", msg);
        }
    }


    private boolean validatePhone() {
        if (edt_adrz_Phone.getText().toString().trim().isEmpty()) {
            edt_adrz_Phone.setError(getString(R.string.Phone_NumberEmail_Address_cannot_be_empty));
            requestFocus(edt_adrz_Phone);
            return false;
        }

        return true;
    }

    private boolean validateRegion() {
        if (edt_adrz_Region.getText().toString().trim().isEmpty()) {
            edt_adrz_Region.setError("Please enter region");
            requestFocus(edt_adrz_Region);
            return false;
        }

        return true;
    }

    private boolean validateName() {
        if (edt_adrz_Name.getText().toString().trim().isEmpty()) {
            edt_adrz_Name.setError(getString(R.string.Enter_a_valid_name));
            requestFocus(edt_adrz_Name);
            return false;
        }

        return true;
    }

    private boolean validateAdrz() {
        if (edt_adrz_Address.getText().toString().trim().isEmpty()) {
            edt_adrz_Address.setError(getString(R.string.Please_enter_a_valid_address));
            requestFocus(edt_adrz_Address);
            return false;
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

//    private void Add_AdrzApi() {
//        name = edt_adrz_Name.getText().toString();
//        adrz = edt_adrz_Address.getText().toString();
//        region = edt_adrz_Region.getText().toString();
//        phone = edt_adrz_Phone.getText().toString();
//        final ACProgressFlower dialog = new ACProgressFlower.Builder(this)
//                .build();
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.show();
//        if (AdrzCount != null && !AdrzCount.isEmpty() && !AdrzCount.equals("null")) {
//            default_flag = 1;
//            JSON_URL_ADD_ADRZ = JSON_URL_ADD_ADRZ + "alias=" + alias + "&name=" + name + "&address_1=" + adrz + "&province_id=" + selectedProvinenceID + "&phone=" + phone +
//                    "&country_id=174&is_default=" + default_flag;
//        } else {
//            JSON_URL_ADD_ADRZ = JSON_URL_ADD_ADRZ + "alias=" + alias + "&name=" + name + "&address_1=" + adrz + "&province_id=" + selectedProvinenceID + "&phone=" + phone +
//                    "&country_id=174&is_default=" + default_flag;
//        }
//
//        Log.v("JSON_URL_SUGGESTON", JSON_URL_ADD_ADRZ);
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL_ADD_ADRZ,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.v("response", response);
//                        dialog.dismiss();
//                        SharedPreferences sharedpreferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
//                        SharedPreferences.Editor editor = sharedpreferences.edit();
//                        try {
//                            //getting the whole json object from the response
//                            JSONObject obj = new JSONObject(response);
//
//                            String status = obj.getString("status");
//                            String success = obj.getString("success");
//                            String msg = obj.getString("msg");
//                            if (status.equals("200")) {
//                                JSONObject json_Adrz = obj.getJSONObject("address");
//
//                                if (success.equals("1")) {
//                                    String is_default = json_Adrz.getString("is_default");
//                                    if (is_default.equals("1")) {
//                                        String id = json_Adrz.getString("id");
//                                        editor.putString("default_adrz", id);
//                                        editor.commit();
//
//                                    }
//
//                                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
//                                    if (from.equals("MapsActivity")) {
//                                        Intent in = new Intent(AddAddressActivity.this, MapsActivity.class);
//                                        startActivity(in);
//                                        finish();
//                                    } else {
//                                        Intent in = new Intent(AddAddressActivity.this, AddressBookActivity.class);
//                                        startActivity(in);
//                                        finish();
//                                    }
//
//                                }
//
//
//                                //  item_main_adapter.notifyDataSetChanged();
//                            } else {
//                                dialog.dismiss();
//                                Toast.makeText(getApplicationContext(), getString(R.string.Server_Error), Toast.LENGTH_LONG).show();
//                            }
//
//
//                        } catch (JSONException e) {
//                            Log.v("main", e.getMessage().toString());
//                            dialog.dismiss();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        dialog.dismiss();
//                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                            Log.v("TimeoutError", "TimeoutError");
//                            //This indicates that the reuest has either time out or there is no connection
//                        } else if (error instanceof AuthFailureError) {
//                            Log.v("AuthFailureError", "AuthFailureError");
//                            //Error indicating that there was an Authentication Failure while performing the request
//                        } else if (error instanceof ServerError) {
//                            Log.v("ServerError", "ServerError");
//                            //Indicates that the server responded with a error response
//                        } else if (error instanceof NetworkError) {
//                            Log.v("NetworkErrorParseError", "NetworkError");
//                            //Indicates that there was network error while performing the request
//                        } else if (error instanceof ParseError) {
//                            Log.v("ParseError", "ParseError");
//                            // Indicates that the server response could not be parsed
//                        }
//                    }
//                }) {
//
//
//            /** Passing some request headers* */
//            /**
//             * Passing some request headers
//             */
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("userToken", login_token);
//                headers.put("lang", setApplicationlanguage);
//                Log.v("login_token2", login_token);
//                return headers;
//            }
//
//        };
//
//
//        //creating a request queue
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//
//        //adding the string request to request queue
//        requestQueue.add(stringRequest);
//
//
//    }

    public void Add_AdrzApi() {
        name = edt_adrz_Name.getText().toString();
        adrz = edt_adrz_Address.getText().toString();
        region = edt_adrz_Region.getText().toString();
        phone = edt_adrz_Phone.getText().toString();
        final ACProgressFlower dialog = new ACProgressFlower.Builder(this)
                .build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        //creating a string request to send request to the url
        JSON_URL_ADD_ADRZ = utilities.GetUrl() + "accounts/address/create?";


        Log.v("JSON_URL_ADD_ADRZ", JSON_URL_ADD_ADRZ);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL_ADD_ADRZ,
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
                            String success = obj.getString("success");
                            String msg = obj.getString("msg");
                            if (status.equals("200")) {
                                JSONObject json_Adrz = obj.getJSONObject("address");

                                if (success.equals("1")) {
                                    String is_default = json_Adrz.getString("is_default");
                                    if (is_default.equals("1")) {
                                        String id = json_Adrz.getString("id");
                                        editor.putString("default_adrz", id);
                                        editor.commit();

                                    }

                                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                    if (from.equals("MapsActivity")) {
                                        Intent in = new Intent(AddAddressActivity.this, MapsActivity.class);
                                        startActivity(in);
                                        finish();
                                    } else {
                                        Intent in = new Intent(AddAddressActivity.this, AddressBookActivity.class);
                                        startActivity(in);
                                        finish();
                                    }

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

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("alias", alias);
                params.put("name", name);
                params.put("address_1", adrz);
                params.put("province_id", selectedProvinenceID);
                params.put("phone", phone);
                params.put("country_id", "174");
                if (AdrzCount != null && !AdrzCount.isEmpty() && !AdrzCount.equals("null")) {
                    params.put("is_default", "1");
                } else {
                    params.put("is_default", ""+default_flag);
                }


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
        RequestQueue requestQueue = Volley.newRequestQueue(AddAddressActivity.this, new HurlStack());
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

    private void GetZoneAPI() {

        JSON_URL_GET_ZONE = JSON_URL_GET_ZONE + Country_ID.get(0);
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


                                    provinence_ID.add(zone_id);
                                    provinence_Values.add(zone_name);


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

                                    Country_ID.add(id);
                                    Country_Values.add(name);


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


            /** Passing some request headers* */
            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
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
    public void onDestroy() {

        super.onDestroy();

        Hawk.put("pages", "home");

    }
}
