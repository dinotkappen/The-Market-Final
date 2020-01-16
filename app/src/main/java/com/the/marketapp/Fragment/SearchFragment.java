package com.the.marketapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.the.marketapp.Activity.CartActivity;
import com.the.marketapp.Activity.MainActivity;
import com.the.marketapp.Adapter.SearchAdapter;
import com.the.marketapp.Model.SearchModel;
import com.the.marketapp.Other.Utilities;
import com.the.marketapp.R;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.the.marketapp.Adapter.Item_Main_Adapter.MY_PREFS_NAME;

public class SearchFragment extends Fragment {

    String from;
    View rootView;
    String login_token;
    Utilities utilities;
    ImageView imgClose;
    String keyword = " ";
    EditText edt_Search;
    TextView txtCartCount;
    InputMethodManager imm;
    String JSON_URL_SEARCH;
    RequestQueue cartQueue;
    MainActivity mainActivity;
    String encodedQueryString;
    StringRequest cartRequest;
    SharedPreferences preferences;
    SearchAdapter objSearchAdapter;
    RecyclerView recycler_view_Search;
    String setApplicationlanguage,selectLang;
    ArrayList<SearchModel> objSeachModel = new ArrayList<SearchModel>();
    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Hawk.init(getContext())
                .setEncryptionMethod(HawkBuilder.EncryptionMethod.MEDIUM)
                .setStorage(HawkBuilder.newSqliteStorage(getContext()))
                .setLogLevel(LogLevel.FULL)
                .build();
        selectLang = Hawk.get("selectLang", "");

        preferences = getActivity().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        setApplicationlanguage = preferences.getString("setApplicationlanguage", "");

//        try {
//            if (utilities.isOnline(getActivity())) {
                // Inflate the layout for this fragment
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(R.layout.actionbar_home);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(getLayoutInflater().inflate(R.layout.actionbar_home, null),
                  new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));

                TextView txt_actionbar_Title    = ( getActivity()).findViewById(R.id.txt_actionbar_Title);
                ImageView imgMenu               = ( getActivity()).findViewById(R.id.imgMenu);
                ImageView img_bar_icon          = ( getActivity()).findViewById(R.id.img_bar_icon);
                txtCartCount                    = (getActivity()).findViewById(R.id.txtCartCount);
                LinearLayout linearCartLalyout  =  (getActivity()).findViewById(R.id.linearCartLalyout);

                img_bar_icon.setVisibility(View.INVISIBLE);
                imgMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!mainActivity.drawer.isDrawerOpen(GravityCompat.START))
                            mainActivity.drawer.openDrawer(Gravity.START);
                        else mainActivity.drawer.closeDrawer(Gravity.END);
                    }
                });

                // txt_actionbar_Title.setText("THE MARKET");
                txt_actionbar_Title.setText(getString(R.string.SEARCH));
                LinearLayout linearMenu =  (getActivity()).findViewById(R.id.linearMenu);
                //  mainActivity.toggle.setHomeAsUpIndicator(R.mipmap.menu_white);
                mainActivity.navigation.setVisibility(View.VISIBLE);

                linearMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!mainActivity.drawer.isDrawerOpen(GravityCompat.START))
                            mainActivity.drawer.openDrawer(Gravity.START);
                        else mainActivity.drawer.closeDrawer(Gravity.END);
                    }
                });



                linearCartLalyout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent in = new Intent(getActivity(), CartActivity.class);
                        startActivity(in);
                    }
                });

                Bundle bundle = this.getArguments();
                if (bundle != null) {
                    from= bundle.getString("from", "");
                }


                rootView = inflater.inflate(R.layout.fragment_search, container, false);
                edt_Search                  = rootView.findViewById(R.id.edt_Search);
                 recycler_view_Search       = rootView.findViewById(R.id.recycler_view_Search);
                imgClose                    = rootView.findViewById(R.id.imgClose);


        if (selectLang.equals("en")) {
            edt_Search.setGravity(Gravity.LEFT);

        } else {
            edt_Search.setGravity(Gravity.RIGHT);

        }
                imgClose.setVisibility(View.GONE);
                edt_Search.setFocusable(true);
                imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(edt_Search, InputMethodManager.SHOW_FORCED);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                edt_Search.requestFocus();

                edt_Search.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void afterTextChanged(Editable s) {
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        String temp = keyword = edt_Search.getText().toString();
                        keyword = temp;
                        try {
                            encodedQueryString = URLEncoder.encode(keyword,"UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        temp = "";
                        Log.v("encodedQueryString", encodedQueryString);
                        if (encodedQueryString != null && !encodedQueryString.isEmpty() && !encodedQueryString.equals("null")) {
                            SearchAPI(encodedQueryString);
                            imgClose.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            objSeachModel.clear();
                            objSearchAdapter.notifyDataSetChanged();
                            imgClose.setVisibility(View.GONE);
                        }

                    }
                });
                login_token = preferences.getString("login_token", "");
                if (login_token != null && !login_token.isEmpty() && !login_token.equals("null"))
                {
                    loadCartItems_api_Count();
                }

                imgClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (from != null && !from.isEmpty() && !from.equals("null"))
                        {
                            if(from.equals("HomeFragment"))
                            {
                                edt_Search.setText("");
                                objSeachModel.clear();
                                objSearchAdapter.notifyDataSetChanged();
                                getFragmentManager().popBackStack();

                            }
                            else
                            {
                                edt_Search.setText("");
                                objSeachModel.clear();
                                objSearchAdapter.notifyDataSetChanged();
                            }
                        }
                        else
                        {
                            edt_Search.setText("");
                            objSeachModel.clear();
                            objSearchAdapter.notifyDataSetChanged();
                        }



                    }
                });

//            } else {
//                rootView = inflater.inflate(R.layout.msg_no_internet, container, false);
//            }
//
//
//        } catch (Exception ex) {
//            Log.v("Cart_main", ex.getMessage().toString());
//
//        }
        return rootView;
    }

    private void setSearch() {
        try {


            objSearchAdapter = new SearchAdapter(getActivity(), objSeachModel);

            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
            recycler_view_Search.setLayoutManager(mLayoutManager);
            //recycler_view_Search.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(10), false));
            recycler_view_Search.getLayoutManager().setMeasurementCacheEnabled(false);
            recycler_view_Search.setItemAnimator(new DefaultItemAnimator());
            recycler_view_Search.setAdapter(objSearchAdapter);
        } catch (Exception ex) {
            String msg = ex.getMessage().toString();
            Log.v("msg_sub", msg);
        }

    }

    private void SearchAPI(String keyword) {


        //creating a string request to send request to the url
        JSON_URL_SEARCH = utilities.GetUrl() + "products?q=" + keyword;
        Log.v("JSON_URL_SEARCH", JSON_URL_SEARCH);

        keyword = "";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL_SEARCH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            String status = obj.getString("status");
                            objSeachModel.clear();
                            if (status.equals("200")) {


                                //so here we are getting that json array
                                JSONObject obj_product = obj.getJSONObject("products");
                                JSONObject obj_Data = obj_product.getJSONObject("data");


                                Iterator keys = obj_Data.keys();
                                while (keys.hasNext()) {
                                    JSONObject obj_details = obj_Data.getJSONObject((String) keys.next());
                                    String id = obj_details.getString("id");
                                    String name = obj_details.getString("name");
                                    objSeachModel.add(new SearchModel(id, name));

                                }


                                setSearch();


                                //  item_main_adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(getContext(), "Server error, please contact the customer care service...", Toast.LENGTH_LONG).show();
                            }


                        } catch (JSONException e) {
                            Log.v("Cart_main", e.getMessage().toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //displaying the error in toast if occurrs
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //adding the string request to request queue
        requestQueue.add(stringRequest);

    }

    public void loadCartItems_api_Count() {

        //creating a string request to send request to the url
        String JSON_URL_LIST_CART = utilities.GetUrl() + "cart";

        Log.v("JSON_URL_CATEGORIES", JSON_URL_LIST_CART);
         cartRequest = new StringRequest(Request.Method.GET, JSON_URL_LIST_CART,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.v("response", response);
                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            String status = obj.getString("status");
                            Log.v("status", status);

                            if (status.equals("200")) {

                                JSONArray jsonArray_data = obj.getJSONArray("cartItems");

                                if (jsonArray_data.length() > 0) {

                                    String cartCount = obj.getString("cartCount");
                                    if (cartCount != null && !cartCount.isEmpty() && !cartCount.equals("null")) {
                                        Log.v("cartCount_SubMenuFrag", cartCount);
                                        txtCartCount.setText(cartCount);
                                        txtCartCount.setVisibility(View.VISIBLE);
                                        try {
                                            SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                                            final SharedPreferences.Editor editor = sharedpreferences.edit();
                                            editor.putString("cartCount", cartCount);
                                            editor.commit();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else {

                                        txtCartCount.setVisibility(View.INVISIBLE);
                                    }
                                } else {
                                    txtCartCount.setVisibility(View.INVISIBLE);
                                }

                                //   set_Main_Items();
                                //  item_main_adapter.notifyDataSetChanged();
                            } else {
                            }


                        } catch (JSONException e) {
//                            Log.v("volley", e.getMessage().toString());
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Log.v("volley", error.getMessage().toString());
//                        Toast.makeText(get, error.getMessage(), Toast.LENGTH_SHORT).show();

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
         cartQueue = Volley.newRequestQueue(getContext());

        //adding the string request to request queue
        cartQueue.add(cartRequest);


    }

    public void onDestroy(){
        if (cartQueue!=null)
        cartQueue.cancelAll(cartRequest);
//        removePhoneKeypad();
//        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        super.onDestroy();

    }




}