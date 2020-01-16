package com.the.marketapp.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
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
import com.the.marketapp.Model.Provinence_Model;
import com.the.marketapp.Other.Utilities;
import com.the.marketapp.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    EditText edt_city, edt_adrz, edt_Phone, edt_Name;
    EditText edt_Name_Map, edt_Adrz_Map, edt_City_Map, edt_Phone_Map;
    String city = null;

    String provinence;
    String JSON_URL_GET_ZONE;
    String country = null;
    String knownName = null;
    String address = null;
    Double lat_dbl, long_dbl;
    Button btn_Confirm, btn_Change;
    SharedPreferences preferences;
    String login_token;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    String default_adrz, shipping_method_id;
    ImageView img_chk_map;
    int map_adrz_flag = 0;
    Utilities utilities;
    String JSON_URL_ALL_ADRZ;
    String province_name;
    String selectedTimeSlot;
    LinearLayout linear_adrz_api, linear_adrz_map;
    ArrayList<String> provinceList = new ArrayList<>();

    ArrayList<Provinence_Model> provinence_models = new ArrayList<Provinence_Model>();
    String setApplicationlanguage;
    LinearLayout linearCashOnDelivery, linearOnlinePayment;
    ImageView imgCash, imgOnline;
    String paymeny_method;
    ScrollView scrollMap;
    Dialog provinence_dilg;
    ListView provinence_listView;
    List<String> provinence_Values;
    List<String> provinence_ID;
    LinearLayout linearDeliveryTime;
    TextView txtNoteDeliveryTime;

    Dialog timeSlotDilg;
    ListView timeSlotListView;
    ArrayList<String> timeSlotValues;
    EditText txtTimeSlot;
    String noteDeliveryTime;
    EditText edtTimeSlot;
    private static final String TAG = "PlaceSelectionListener";
    String latSearch,lonSearch,selectLang;
    LinearLayout linearBackMap,len_team_click;
    TextView txtTeams,txtCondition;
    int countRbTC = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            if (utilities.isOnline(this)) {

                Hawk.init(getApplicationContext())
                        .setEncryptionMethod(HawkBuilder.EncryptionMethod.MEDIUM)
                        .setStorage(HawkBuilder.newSqliteStorage(getApplicationContext()))
                        .setLogLevel(LogLevel.FULL)
                        .build();
                Hawk.put("pages", "mapActivity");

        setContentView(R.layout.activity_maps);
        selectLang = Hawk.get("selectLang", "");


        txtTeams=  findViewById(R.id.txt_tearms);
        txtCondition=  findViewById(R.id.txt_lhear);
        linearBackMap= (LinearLayout) findViewById(R.id.linearBackMap);
        linearCashOnDelivery = (LinearLayout) findViewById(R.id.linearCashOnDelivery);
        linearOnlinePayment = (LinearLayout) findViewById(R.id.linearOnlinePayment);
        imgCash = (ImageView) findViewById(R.id.imgCash);
        imgOnline = (ImageView) findViewById(R.id.imgOnline);
        scrollMap = (ScrollView) findViewById(R.id.scrollMap);
        linearDeliveryTime = (LinearLayout) findViewById(R.id.linearDeliveryTime);
        edtTimeSlot = (EditText) findViewById(R.id.edtTimeSlot);
        len_team_click =  findViewById(R.id.len_team_click);
        txtNoteDeliveryTime = (TextView) findViewById(R.id.txtNoteDeliveryTime);
        txtNoteDeliveryTime.setVisibility(View.GONE);

//                if (selectLang.equals("en")) {
//                    txtCondition.setText(R.string.herebyTC);
//                     txtTeams.setText(R.string.AgreeTC);
////            edt_Cmnt.setForegroundGravity(Gravity.CENTER_VERTICAL);
//
//                } else {
//                    txtTeams.setText(R.string.herebyTC);
//                    txtCondition.setText(R.string.AgreeTC);
////                    edt_Phone.setGravity(Gravity.RIGHT);
////                    edt_Name.setGravity(Gravity.RIGHT);
////                    edt_Cmnt.setGravity(Gravity.RIGHT);
////                    edt_Email.setGravity(Gravity.RIGHT);
////            edt_Cmnt.setForegroundGravity(Gravity.CENTER_VERTICAL);
//
//                }

        imgOnline.setVisibility(View.INVISIBLE);
        imgCash.setVisibility(View.VISIBLE);
        paymeny_method = "cash_on_delivery";

        linearBackMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        img_chk_map = (ImageView) findViewById(R.id.img_chk_map);
        preferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        setApplicationlanguage = preferences.getString("setApplicationlanguage", "");
        login_token = preferences.getString("login_token", "");
        int logedIn = preferences.getInt("logedIn", 0);
        default_adrz = preferences.getString("default_adrz", "");
        shipping_method_id = preferences.getString("shipping_method_id", "");

        linearCashOnDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgOnline.setVisibility(View.INVISIBLE);
                imgCash.setVisibility(View.VISIBLE);
                paymeny_method = "cash_on_delivery";
            }
        });
        linearOnlinePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgOnline.setVisibility(View.VISIBLE);
                imgCash.setVisibility(View.INVISIBLE);
                paymeny_method = "card_payment";
            }
        });

                final CheckBox rbTC =  findViewById(R.id.rbTCMaps);

                rbTC.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (countRbTC == 0) {
                            rbTC.setChecked(true);
                            rbTC.setError(null);

                            countRbTC = 1;
                        } else {
                            rbTC.setChecked(false);
                            countRbTC = 0;
                        }

                    }
                });
                len_team_click.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in = new Intent(MapsActivity.this, MainActivity.class);
                        in.putExtra("fromMapsActivity", "fromMapsActivity");
                        startActivity(in);
                    }
                });


                Places.initialize(getApplicationContext(), "AIzaSyC570AtbhxlMWwHUzLPotyFqDbJzoF6Tc8");
        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.place_fragment);

// Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(com.google.android.libraries.places.api.model.Place.Field.ID, com.google.android.libraries.places.api.model.Place.Field.NAME, com.google.android.libraries.places.api.model.Place.Field.LAT_LNG));

// Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
// TODO: Get info about the selected place.

                String getLatLng = "" + place.getLatLng();
                latSearch = "" + place.getLatLng().latitude;
                lonSearch= "" + place.getLatLng().longitude;
                if (latSearch != null && !latSearch.isEmpty() && !latSearch.equals("null"))
                {
                    if (lonSearch != null && !lonSearch.isEmpty() && !lonSearch.equals("null"))
                    {


                        // Add a marker in Sydney and move the camera
                        LatLng doha = new LatLng(Double.parseDouble(latSearch), Double.parseDouble(lonSearch));
                        mMap.addMarker(new MarkerOptions().position(doha).title(""));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(latSearch), Double.parseDouble(lonSearch)), 16));

                        lat_dbl = doha.latitude;
                        long_dbl = doha.longitude;
                        get_Adrz();
                    }
                }


                // this will contain " they taste good"
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getLatLng());
            }

            @Override
            public void onError(Status status) {
// TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
        Log.v("logedIn", "" + logedIn);
        if (logedIn == 1) {


            Log.v("default_adrz", "" + default_adrz);

            edt_city = (EditText) findViewById(R.id.edt_city);
            edt_adrz = (EditText) findViewById(R.id.edt_adrz);
            edt_Phone = (EditText) findViewById(R.id.edt_Phone);
            edt_Name = (EditText) findViewById(R.id.edt_Name);

            edt_Name_Map = (EditText) findViewById(R.id.edt_Name_Map);
            edt_Adrz_Map = (EditText) findViewById(R.id.edt_Adrz_Map);
            edt_City_Map = (EditText) findViewById(R.id.edt_City_Map);
            edt_Phone_Map = (EditText) findViewById(R.id.edt_Phone_Map);

            if(setApplicationlanguage.equals("en"))
            {
                edt_Name_Map.setGravity(Gravity.LEFT);
                edt_Adrz_Map.setGravity(Gravity.LEFT);
                edt_City_Map.setGravity(Gravity.LEFT);
                edt_Phone_Map.setGravity(Gravity.LEFT);

            }
            else
            {
                edt_Name_Map.setGravity(Gravity.RIGHT);
                edt_Adrz_Map.setGravity(Gravity.RIGHT);
                edt_City_Map.setGravity(Gravity.RIGHT);
                edt_Phone_Map.setGravity(Gravity.RIGHT);
            }


            linear_adrz_api = (LinearLayout) findViewById(R.id.linear_adrz_api);
            linear_adrz_api.setVisibility(View.VISIBLE);
            linear_adrz_map = (LinearLayout) findViewById(R.id.linear_adrz_map);
            linear_adrz_map.setVisibility(View.GONE);

            btn_Confirm = (Button) findViewById(R.id.btn_Confirm);
            btn_Change = (Button) findViewById(R.id.btn_Change);

            edt_city.setVisibility(View.VISIBLE);
            edt_adrz.setVisibility(View.VISIBLE);
            edt_Name.setVisibility(View.VISIBLE);
            edt_Phone.setVisibility(View.VISIBLE);

            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            timeSlotValues = new ArrayList<>();
            timeSlotDilg = new Dialog(this);
            timeSlotDilg.requestWindowFeature(Window.FEATURE_NO_TITLE);
            timeSlotDilg.setContentView(R.layout.timeslotdialog);
            timeSlotDilg.setCanceledOnTouchOutside(false);
            timeSlotListView = (ListView) timeSlotDilg.findViewById(R.id.timeSlotListView);

            linearDeliveryTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GetDeliveryTimeAPI();
                }
            });

            edtTimeSlot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GetDeliveryTimeAPI();
                }
            });


            // Capture ListView item click
            timeSlotListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    try {
                        // Capture the click position and set it into a string
                        String phones = (String) timeSlotListView.getItemAtPosition(position);
                        edtTimeSlot.setText((String) timeSlotListView.getItemAtPosition(position));
                        edtTimeSlot.setError(null);
//                input_State.setText(phones);
                        selectedTimeSlot = timeSlotValues.get(position);
                        Log.v("selectedTimeSlot", selectedTimeSlot);
                        timeSlotDilg.dismiss();

                    } catch (Exception ex) {
                        String msg = ex.getMessage().toString();
                        Log.v("provinence_listView", msg);
                    }
                }
            });

            provinence_Values = new ArrayList<>();
            provinence_ID = new ArrayList<>();
            provinence_dilg = new Dialog(this);
            provinence_dilg.requestWindowFeature(Window.FEATURE_NO_TITLE);
            provinence_dilg.setContentView(R.layout.dialog_provinence);
            provinence_dilg.setCanceledOnTouchOutside(false);
            provinence_listView = (ListView) provinence_dilg.findViewById(R.id.provinence_listView);


            edt_City_Map.setOnClickListener(new View.OnClickListener() {
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
                        edt_City_Map.setText((String) provinence_listView.getItemAtPosition(position));
//                input_State.setText(phones);
                        String selectedProvinenceID = provinence_ID.get(position);
                        provinence_dilg.dismiss();

                    } catch (Exception ex) {
                        String msg = ex.getMessage().toString();
                        Log.v("provinence_listView", msg);
                    }
                }
            });

            btn_Confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (selectedTimeSlot != null && !selectedTimeSlot.isEmpty() && !selectedTimeSlot.equals("null")) {
                        if (rbTC.isChecked()) {
                            if (map_adrz_flag == 0) {
                                //creating a string request to send request to the url


                                Intent in = new Intent(MapsActivity.this, Order_Summary_Activity.class);
                                in.putExtra("paymeny_method", paymeny_method);
                                in.putExtra("delivery_time", selectedTimeSlot);
                                String adrz_api = edt_adrz.getText().toString();

                                in.putExtra("name_api", edt_Name.getText().toString());
                                if (adrz_api != null && !adrz_api.isEmpty() && !adrz_api.equals("null")) {

                                    String city_api = edt_city.getText().toString();

                                    in.putExtra("adrz_api", adrz_api);
                                    in.putExtra("city_api", city_api);
                                    in.putExtra("phone_api", edt_Phone.getText().toString());
                                    in.putExtra("map_adrz_flag", "" + map_adrz_flag);
                                    startActivity(in);


                                } else {
                                    edt_adrz.setError("Address is not valid");
                                    scrollMap.smoothScrollTo(0, edt_adrz.getTop());
                                }

                            } else {

                                submitAdrzForm__Api();
                            }
                        } else {
                            rbTC.setError(getString(R.string.AgreeTC));
                            scrollMap.smoothScrollTo(0, rbTC.getTop());
                        }

                    } else {
                        edtTimeSlot.setError("Please select a delivery time");
                    }
                }

            });
            img_chk_map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (map_adrz_flag == 0) {
                        img_chk_map.setImageResource(R.mipmap.ic_round_tick);
                        map_adrz_flag = 1;
                        linear_adrz_api.setVisibility(View.GONE);
                        linear_adrz_map.setVisibility(View.VISIBLE);
                        get_Adrz();

                    } else {
                        img_chk_map.setImageResource(R.mipmap.ic_radio_not_selected);
                        linear_adrz_api.setVisibility(View.VISIBLE);
                        linear_adrz_map.setVisibility(View.GONE);
                        map_adrz_flag = 0;
                    }

                }
            });
            btn_Change.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent in = new Intent(MapsActivity.this, AddressBookActivity.class);
                    in.putExtra("from", "MapsActivity");
                    startActivity(in);
                    finish();


                }
            });
            JSON_URL_ALL_ADRZ = utilities.GetUrl();

            GetZoneAPI();


//            PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
//                    getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
//
//            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//                @Override
//                public void onPlaceSelected(Place place) {
//                    mMap.clear();
//                    mMap.addMarker(new MarkerOptions().position(place.getLatLng()).title(place.getName().toString()));
//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
//                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 12.0f));
//                }
//
//                @Override
//                public void onError(Status status) {
//
//                }
//            });


        } else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(
                    MapsActivity.this);

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
                            Intent in = new Intent(MapsActivity.this, LogInActivity.class);
                            startActivity(in);
                        }
                    });


            builder.show();
        }
            } else {
                setContentView(R.layout.msg_no_internet);
            }


        } catch (Exception ex) {
            Log.v("Cart_main", ex.getMessage().toString());

        }
    }

    /**
     * Validating form
     */
    private void submitAdrzForm__Api() {
        if (!validateName()) {
            return;
        }

        if (!validateAdrz()) {
            return;
        }


        if (!validateCity()) {
            return;
        }


        if (!validatePhone()) {
            return;
        }
        if (!validatePhoneFormat()) {
            return;
        }

        String city_Map = edt_City_Map.getText().toString();
        if (city_Map != null && !city_Map.isEmpty() && !city_Map.equals("null")) {
            for (int i = 0; i < provinceList.size(); i++) {
                String cityAvailable = provinceList.get(i);
                if (city_Map.equals(cityAvailable)) {

                    provinence = cityAvailable;
                    Intent in = new Intent(MapsActivity.this, Order_Summary_Activity.class);
                    in.putExtra("name_map", edt_Name_Map.getText().toString());
                    in.putExtra("adrz_Map", edt_Adrz_Map.getText().toString());
                    in.putExtra("city_Map", provinence);
                    in.putExtra("phone_Map", edt_Phone_Map.getText().toString());
                    in.putExtra("map_adrz_flag", "" + map_adrz_flag);
                    in.putExtra("lat", "" + lat_dbl);
                    in.putExtra("lon", "" + long_dbl);
                    in.putExtra("paymeny_method", paymeny_method);
                    in.putExtra("delivery_time", selectedTimeSlot);
                    startActivity(in);
                }


            }
            if (provinence != null && !provinence.isEmpty() && !provinence.equals("null")) {


            } else {

                edt_City_Map.setFocusable(true);
                scrollMap.smoothScrollTo(0, edt_City_Map.getTop());
                Toast.makeText(getApplicationContext(), "Service is not available in this location", Toast.LENGTH_SHORT).show();


//                AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
//                builder.setMessage("Service not available in this location")
//                        .setCancelable(false)
//                        .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                //do things
//
//                                edt_City_Map.setFocusable(true);
//                                scrollMap.smoothScrollTo(0,edt_Adrz_Map.getTop());
//                                dialog.dismiss();
//
//
//                            }
//                        });
//                AlertDialog alert = builder.create();
//                alert.show();
            }
        } else {
            edt_city.setError("City is not available");
        }

    }
    private boolean validatePhoneFormat() {
        if(edt_Phone_Map.getText().toString().trim().length() < 6 || edt_Phone_Map.getText().toString().trim().length() > 13) {
            // if(phone.length() != 10) {
            edt_Phone_Map.setError(getString(R.string.Invalid_Phone_Number));
            requestFocus(edt_Phone_Map);
            return false;
        }

        return true;
    }
    private boolean validatePhone() {
        if (edt_Phone_Map.getText().toString().trim().isEmpty()) {
            edt_Phone_Map.setError(getString(R.string.Please_enter_the_phone_number));
            requestFocus(edt_Phone_Map);
            return false;
        }

        return true;
    }

    private boolean validateCity() {
        if (edt_City_Map.getText().toString().trim().isEmpty()) {
            edt_City_Map.setError("Please enter city");
            scrollMap.smoothScrollTo(0, edt_City_Map.getTop());
            requestFocus(edt_City_Map);
            return false;
        }

        return true;
    }

    private boolean validateAdrz() {
        if (edt_Adrz_Map.getText().toString().trim().isEmpty()) {
            edt_Adrz_Map.setError("Please enter address");
            requestFocus(edt_Adrz_Map);
            return false;
        }

        return true;
    }

    private boolean validateName() {
        if (edt_Name_Map.getText().toString().trim().isEmpty()) {
            edt_Name_Map.setError(getString(R.string.Please_enter_a_valid_name));
            requestFocus(edt_Name_Map);
            return false;
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng doha = new LatLng(25.2854, 51.5310);
        mMap.addMarker(new MarkerOptions().position(doha).title("Doha"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(25.2854, 51.5310), 10.0f));

        lat_dbl = doha.latitude;
        long_dbl = doha.longitude;

        // Setting a click event handler for the map
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(latLng);

                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                lat_dbl = latLng.latitude;
                long_dbl = latLng.longitude;
                // Clears the previously touched position
                mMap.clear();

                // Animating to the touched position
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                // Placing a marker on the touched position
                mMap.addMarker(markerOptions);


                get_Adrz();


            }
        });
    }

    public void get_Adrz() {
        //Getting Adrz
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat_dbl, long_dbl, 1);
            if (addresses.size() > 0) {
                address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                city = addresses.get(0).getLocality();
                country = addresses.get(0).getCountryName();
                knownName = addresses.get(0).getFeatureName();
                if (TextUtils.isEmpty(address)) {
                    edt_Adrz_Map.setText("");
                    //  edt_adrz.setVisibility(View.GONE);
                    return; // or break, continue, throw
                } else {
                    edt_Adrz_Map.setVisibility(View.VISIBLE);
                    edt_Adrz_Map.setText(address);
                }


                if (TextUtils.isEmpty(city)) {
                    edt_City_Map.setText("");
                    // edt_city.setVisibility(View.GONE);
                    return; // or break, continue, throw
                } else {
                    edt_City_Map.setVisibility(View.VISIBLE);
                    edt_City_Map.setText(city);
                } // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void All_AdrzApi() {

        JSON_URL_ALL_ADRZ = JSON_URL_ALL_ADRZ + "accounts/address";
        Log.v("JSON_URL_ALL_ADRZ", JSON_URL_ALL_ADRZ);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL_ALL_ADRZ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("response", response);
                        SharedPreferences sharedpreferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            String status = obj.getString("status");

                            if (status.equals("200")) {


                                JSONArray jsonArray_address = obj.getJSONArray("address");
                                if (jsonArray_address.length() == 0) {
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(
                                            MapsActivity.this);

                                    builder.setTitle(getString(R.string.Address));
                                    builder.setMessage(getString(R.string.Select_an_address));
                                    builder.setCancelable(false);
                                    builder.setNegativeButton(getString(R.string.NO),
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog,
                                                                    int which) {
                                                    img_chk_map.setImageResource(R.mipmap.ic_round_tick);
                                                    map_adrz_flag = 1;
                                                    linear_adrz_api.setVisibility(View.GONE);
                                                    linear_adrz_map.setVisibility(View.VISIBLE);
                                                    get_Adrz();
                                                    dialog.dismiss();
                                                }
                                            });
                                    builder.setPositiveButton(getString(R.string.YES),
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog,
                                                                    int which) {
                                                    Intent in = new Intent(MapsActivity.this, AddAddressActivity.class);
                                                    in.putExtra("from", "MapsActivity");
                                                    in.putExtra("add_adrz", 1);
                                                    in.putExtra("AdrzCount","1");
                                                    startActivity(in);
                                                    finish();
                                                }
                                            });


                                    builder.show();

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

                                        for (int k = 0; k < provinence_models.size(); k++) {
                                            if (provinence_models.get(k).getProvinence_ID().equals(province_id)) {
                                                province_name = provinence_models.get(k).getProvinence_Name();
                                                String u = province_name;
                                                Log.v("province_name", province_name);
                                            }
                                        }


                                        if (is_default.equals("1")) {
                                            if (address_1 != null && !address_1.isEmpty() && !address_1.equals("null")) {
                                                edt_adrz.setText(address_1);
                                                edt_adrz.setError(null);
                                                if (province_name != null && !province_name.isEmpty() && !province_name.equals("null")) {
                                                    edt_city.setText(province_name);
                                                    if (phone != null && !phone.isEmpty() && !phone.equals("null")) {
                                                        edt_Phone.setText(phone);
                                                        if (name != null && !name.isEmpty() && !name.equals("null")) {
                                                            edt_Name.setText(name);
                                                        }

                                                    }
                                                }

                                            }


                                            editor.putString("default_adrz", id);
                                            editor.apply();
                                        }

                                    }

                                }


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

    private void GetZoneAPI() {
        JSON_URL_GET_ZONE = utilities.GetUrl() + "zone/";
        JSON_URL_GET_ZONE = JSON_URL_GET_ZONE + "174";
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


                                    provinceList.add(zone_name);

                                    provinence_Values.add(zone_name);
                                    provinence_ID.add(zone_id);
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

    private void GetDeliveryTimeAPI() {
        String GET_DELIVERTY_TIME = utilities.GetUrl() + "get/delivery-time";

        Log.v("GET_DELIVERTY_TIME", GET_DELIVERTY_TIME);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, GET_DELIVERTY_TIME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("response", response);

                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            String status = obj.getString("status");

                            if (status.equals("200")) {
                                timeSlotValues.clear();
                                JSONArray jsonArrayObj = obj.getJSONArray("data");
                                if (jsonArrayObj.length() > 0) {
                                    for (int i = 0; i < jsonArrayObj.length(); i++) {
                                        String timeSlot = jsonArrayObj.getString(i);
                                        if (timeSlot != null && !timeSlot.isEmpty() && !timeSlot.equals("null")) {
                                            timeSlotValues.add(timeSlot);
                                            String hj = timeSlot;
                                        }

                                    }

                                    String note = obj.getString("note");
                                    if (note != null && !note.isEmpty() && !note.equals("null")) {
                                        txtNoteDeliveryTime.setVisibility(View.VISIBLE);
                                        txtNoteDeliveryTime.setText(note);
                                    }


                                    // Create an ArrayAdapter from List
                                    ArrayAdapter<String> adapterTimeSlot = new ArrayAdapter<String>
                                            (getApplicationContext(), android.R.layout.simple_list_item_1, timeSlotValues) {
                                        @Override
                                        public View getView(int position, View convertView, ViewGroup parent) {
                                            // Get the Item from ListView

                                            View view = super.getView(position, convertView, parent);

                                            // Initialize a TextView for ListView each Item
                                            TextView txtTimeSlot = (TextView) view.findViewById(android.R.id.text1);

                                            // Set the text color of TextView (ListView Item)
                                            txtTimeSlot.setTextColor(getResources().getColor(R.color.colorBlack));

                                            // Generate ListView Item using TextView
                                            return view;
                                        }
                                    };

                                    timeSlotListView.setAdapter(adapterTimeSlot);
                                    timeSlotDilg.show();
                                } else {
                                    Log.v("jsonArrayObj", "jsonArrayObj");
                                }

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
    public void onDestroy(){
        super.onDestroy();
        Hawk.put("pages", "home");
    }


}
