package com.the.marketapp.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

public class Edit_Adrz_Activity extends AppCompatActivity {
    ImageView img_chk_box;
    private RadioGroup radioGroup_AdrzType;
    RadioButton radioButton_home, radioButton_office;
    Button btn_Update_Adrz;
    EditText edt_adrz_Name, edt_adrz_Address, edt_adrz_Region, edt_adrz_Phone;
    private static String JSON_URL_UPDATE_ADRZ, JSON_URL_GET_ZONE, JSON_URL_GET_COUNTRY, JSON_URL_GET_ADRZ;
    Utilities utilities;
    String login_token;
    String adrz_id;
    String title, desc;
    SharedPreferences preferences;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    Button btn_Submit;
    String name, adrz, region, phone;
    Dialog provinence_dilg;
    ListView provinence_listView;
    List<String> provinence_Values;
    List<String> provinence_ID;
    List<String> Country_Values;
    List<String> Country_ID;
    String selectedProvinenceID;
    int default_flag = 0;
    String alias = "home";
    String setApplicationlanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            if (utilities.isOnline(this)) {
                setContentView(R.layout.activity_edit__adrz_);

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
                linear_backArrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

                img_chk_box = (ImageView) findViewById(R.id.img_chk_box);
                btn_Update_Adrz = (Button) findViewById(R.id.btn_Update_Adrz);
                edt_adrz_Name = (EditText) findViewById(R.id.edt_adrz_Name);
                edt_adrz_Address = (EditText) findViewById(R.id.edt_adrz_Address);
                edt_adrz_Region = (EditText) findViewById(R.id.edt_adrz_Region);
                edt_adrz_Phone = (EditText) findViewById(R.id.edt_adrz_Phone);
                radioGroup_AdrzType = (RadioGroup) findViewById(R.id.radioGroup_AdrzType);
                radioButton_home = (RadioButton) findViewById(R.id.radioButton_home);
                radioButton_office = (RadioButton) findViewById(R.id.radioButton_office);

                JSON_URL_UPDATE_ADRZ = utilities.GetUrl() + "accounts/address/update/";
                preferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                adrz_id = preferences.getString("addressId", "");
                login_token = preferences.getString("login_token", "");
                setApplicationlanguage = preferences.getString("setApplicationlanguage", "");
                Log.v("setApplicationlanguage", setApplicationlanguage);

                JSON_URL_GET_ZONE = utilities.GetUrl() + "zone/";
                JSON_URL_GET_COUNTRY = utilities.GetUrl() + "countries";
                JSON_URL_GET_ADRZ = utilities.GetUrl() + "accounts/address/edit/";
                int logedIn = preferences.getInt("logedIn", 0);
                Log.v("logedIn", "" + logedIn);
                if (logedIn == 1) {
                    provinence_Values = new ArrayList<>();
                    provinence_ID = new ArrayList<>();
                    Country_Values = new ArrayList<>();
                    Country_ID = new ArrayList<>();

                    provinence_dilg = new Dialog(this);
                    provinence_dilg.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    provinence_dilg.setContentView(R.layout.dialog_provinence);
                    provinence_dilg.setCanceledOnTouchOutside(false);
                    provinence_listView = (ListView) provinence_dilg.findViewById(R.id.provinence_listView);

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
                                    //Toast.makeText(getActivity(), rb.getText(), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception ex) {
                                String msg = ex.getMessage().toString();
                                Log.v("radioGroup_AdrzType", msg);
                            }

                        }
                    });
                    btn_Update_Adrz.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                Intent in=new Intent(getActivity(), MapsActivity.class);
//                startActivity(in);
                            try {
                                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                                submitForm();
                            } catch (Exception ex) {
                                String msg = ex.getMessage().toString();
                                Log.v("btn_Update_Adrz", msg);
                            }

                        }
                    });
                    GetCountry_API();
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(
                            this);

                    builder.setTitle("Log In");
                    builder.setMessage("To continue please log in");
                    builder.setCancelable(false);
                    builder.setNegativeButton("NO",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.setPositiveButton("YES",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    Intent in = new Intent(Edit_Adrz_Activity.this, LogInActivity.class);
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

    /**
     * Validating form
     */
    private void submitForm() {

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

        Update_AdrzApi();
    }


    private boolean validatePhone() {
        if (edt_adrz_Phone.getText().toString().trim().isEmpty()) {
            edt_adrz_Phone.setError("Please enter phone");
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
            edt_adrz_Name.setError("Please enter name");
            requestFocus(edt_adrz_Name);
            return false;
        }

        return true;
    }

    private boolean validateAdrz() {
        if (edt_adrz_Address.getText().toString().trim().isEmpty()) {
            edt_adrz_Address.setError("Please enter address");
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
                                GetAdrzAPI();

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

                                    Country_ID.add(id);
                                    Country_Values.add(name);


                                }
                                GetZoneAPI();

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

    private void GetAdrzAPI() {

        JSON_URL_GET_ADRZ = JSON_URL_GET_ADRZ + adrz_id;
        Log.v("JSON_URL_GET_ADRZ", JSON_URL_GET_ADRZ);


        final ACProgressFlower dialog = new ACProgressFlower.Builder(this)
                .build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL_GET_ADRZ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("response", response);
                        dialog.dismiss();
                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            String status = obj.getString("status");
                            if (status.equals("200")) {
                                JSONObject jsonArray_address = obj.getJSONObject("address");
                                String address_id = jsonArray_address.getString("id");
                                String alias = jsonArray_address.getString("alias");
                                String name = jsonArray_address.getString("name");
                                String address_1 = jsonArray_address.getString("address_1");
                                String address_2 = jsonArray_address.getString("address_2");
                                String province_id = jsonArray_address.getString("province_id");
                                String customer_id = jsonArray_address.getString("customer_id");
                                String phone = jsonArray_address.getString("phone");
                                String is_default = jsonArray_address.getString("is_default");

                                if (name != null && !name.isEmpty() && !name.equals("null"))
                                {
                                    edt_adrz_Name.setText(name);
                                }
                                if (address_1 != null && !address_1.isEmpty() && !address_1.equals("null"))
                                {
                                    edt_adrz_Address.setText(address_1);
                                }
                                if (phone != null && !phone.isEmpty() && !phone.equals("null"))
                                {
                                    edt_adrz_Phone.setText(phone);
                                }



                                if (is_default.equals("1")) {
                                    img_chk_box.setImageResource(R.mipmap.ic_round_tick);
                                } else {
                                    img_chk_box.setImageResource(R.mipmap.ic_radio_not_selected);
                                }

                                if (alias.equals("home")) {
                                    radioButton_office.setChecked(false);
                                    radioButton_home.setChecked(true);
                                    alias = "home";
                                } else {
                                    radioButton_office.setChecked(true);
                                    radioButton_home.setChecked(false);
                                    alias = "Office";
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Server error...", Toast.LENGTH_SHORT).show();
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

    private void Update_AdrzApi() {
        name = edt_adrz_Name.getText().toString();
        adrz = edt_adrz_Address.getText().toString();
        region = edt_adrz_Region.getText().toString();
        phone = edt_adrz_Phone.getText().toString();
        final ACProgressFlower dialog = new ACProgressFlower.Builder(this)
                .build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        JSON_URL_UPDATE_ADRZ = JSON_URL_UPDATE_ADRZ + adrz_id + "?alias=" + alias + "&name=" + name + "&address_1=" + adrz + "&province_id=" + selectedProvinenceID + "&phone=" + phone +
                "&country_id=174&is_default=" + default_flag;
        Log.v("JSON_URL_SUGGESTON", JSON_URL_UPDATE_ADRZ);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL_UPDATE_ADRZ,
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
                                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                    Intent in = new Intent(Edit_Adrz_Activity.this, AddressBookActivity.class);
                                    startActivity(in);
                                    Log.v("msg", msg);
                                }


                                //  item_main_adapter.notifyDataSetChanged();
                            } else {
                                dialog.dismiss();
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);


    }


}


