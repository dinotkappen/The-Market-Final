package com.the.marketapp.Fragment;

import android.app.Dialog;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.the.marketapp.Activity.CartActivity;
import com.the.marketapp.Activity.LogInActivity;
import com.the.marketapp.Activity.MainActivity;
import com.the.marketapp.Adapter.Sub_Item_Adapter;
import com.the.marketapp.Adapter.Submenu_category_Adapter;
import com.the.marketapp.Model.Category_Model;
import com.the.marketapp.Model.Item_Sub_Model;
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

import static android.content.Context.MODE_PRIVATE;
import static android.view.Gravity.LEFT;
import static com.the.marketapp.Adapter.Item_Main_Adapter.MY_PREFS_NAME;


public class SubMenu_Fragment extends Fragment {
    View rootView;
    private Context mContext;
    //the URL having the json data

    String JSON_URL_CATEGORIES;
    Utilities utilities;
    private RecyclerView subCat_recyclerview;
    //  private FilterAdapter filter_adapter;
    String categoryId_str;
    String main_cat_id;
    SharedPreferences preferences;
    public LinearLayout scroll;
    int logedIn;
    int lasPosition = 0;
    String lastPageUrl;
    String next_page_url;
    Boolean exist;
    ImageView imgFilter;
    int filter = 0;

    Dialog filterDialog;
    ListView listViewFilter;
    List<String> filterValues;
    String filterValue;
    TextView txtCartCount;
    MainActivity mainActivity;
    String setApplicationlanguage, login_token;
    public static Sub_Item_Adapter sub_item_adapter;
    public Submenu_category_Adapter submenu_category_Adapter;
    ArrayList<String> wish_list_product_ID = new ArrayList<>();
    public RecyclerView recyclerView_subitems, recyclerViewFilter;
    private static String URL_CHILD_CAT, URL_ADD_WISH_LIST, GET_WISH_LIST;
    public static ArrayList<Item_Sub_Model> data_Sub_Items = new ArrayList<Item_Sub_Model>();
    //vars
    //   private ArrayList<String> str_Sub_Cat = new ArrayList<>();

    ArrayList<Category_Model> itemCatList = new ArrayList<Category_Model>();

    public SubMenu_Fragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        try {
            if (utilities.isOnline(getActivity())) {
                Hawk.init(getActivity())
                        .setEncryptionMethod(HawkBuilder.EncryptionMethod.MEDIUM)
                        .setStorage(HawkBuilder.newSqliteStorage(getActivity()))
                        .setLogLevel(LogLevel.FULL)
                        .build();
                Hawk.put("pages", "sub_menu");
        rootView = inflater.inflate(R.layout.fragment_sub_menu_, container, false);
        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        data_Sub_Items.clear();
                preferences = getActivity().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(R.layout.actionbar_submenu);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(getLayoutInflater().inflate(R.layout.actionbar_submenu, null),
                new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));
        TextView txt_actionbar_Title = (TextView) ((AppCompatActivity) getActivity()).findViewById(R.id.txt_actionbar_Title);

        txtCartCount = (TextView) ((AppCompatActivity) getActivity()).findViewById(R.id.txtCartCount);
        LinearLayout linearMenuBlack = (LinearLayout) ((AppCompatActivity) getActivity()).findViewById(R.id.linearMenuBlack);
        LinearLayout linearBack = (LinearLayout) ((AppCompatActivity) getActivity()).findViewById(R.id.linearBack);


        //ImageView imgBack = (ImageView) ((AppCompatActivity) getActivity()).findViewById(R.id.imgBack);
        LinearLayout linearCartLalyout = (LinearLayout) ((AppCompatActivity) getActivity()).findViewById(R.id.linearCartLalyout);
        linearCartLalyout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (logedIn==1) {
                    Intent in = new Intent(getActivity(), CartActivity.class);
                    startActivity(in);
                }
                else {
                    Intent in = new Intent(getActivity(), LogInActivity.class);
                    startActivity(in);
                }
            }
        });
        linearBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), MainActivity.class);
                startActivity(in);
            }
        });
        setHasOptionsMenu(true);


        linearMenuBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // If the navigation drawer is not open then open it, if its already open then close it.
                if (!mainActivity.drawer.isDrawerOpen(GravityCompat.START))
                    mainActivity.drawer.openDrawer(Gravity.START);
                else mainActivity.drawer.closeDrawer(Gravity.END);
            }
        });
        mainActivity.navigation.setVisibility(View.GONE);
//        mainActivity.toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                mainActivity.toggle.setHomeAsUpIndicator(R.mipmap.menu_white);
////                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//
//              Intent in=new Intent(getActivity(),MainActivity.class);
//              startActivity(in);
//            }
//        });

                logedIn = preferences.getInt("logedIn", 0);
        imgFilter = (ImageView) rootView.findViewById(R.id.imgFilter);
        sub_item_adapter = new Sub_Item_Adapter(getActivity(), data_Sub_Items, SubMenu_Fragment.this);
        Log.v("data_Sub_Items", "" + data_Sub_Items.size());

        subCat_recyclerview = (RecyclerView) rootView.findViewById(R.id.recyclerView_subCat);
        recyclerView_subitems = (RecyclerView) rootView.findViewById(R.id.recyclerView_subitems);


        filterValues = new ArrayList<>();
        filterValues.add(getString(R.string.Low_to_high));
        filterValues.add(getString(R.string.High_to_low));


        filterDialog = new Dialog(getActivity());
        filterDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        filterDialog.setContentView(R.layout.filter_adapter_layout);
        filterDialog.setCanceledOnTouchOutside(true);
        Window window = filterDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.TOP | LEFT;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        listViewFilter = (ListView) filterDialog.findViewById(R.id.listViewFilter);
        // Create an ArrayAdapter from List
        ArrayAdapter<String> adapter_BodyColor = new ArrayAdapter<String>
                (getActivity(), android.R.layout.simple_list_item_1, filterValues) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                // Initialize a TextView for ListView each Item
                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                // Set the text color of TextView (ListView Item)
                tv.setTextColor(getResources().getColor(R.color.colorBlack));

                // Generate ListView Item using TextView
                return view;
            }
        };
        listViewFilter.setAdapter(adapter_BodyColor);


//        // Capture ListView item click
        listViewFilter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                filterValue = (String) listViewFilter.getItemAtPosition(position);
                String FILTER_URL = null;
                Log.v("str_CustomerType", filterValue);
                data_Sub_Items.clear();
                if (filterValue.equals(getString(R.string.Low_to_high))) {


                    categoryId_str = preferences.getString("categoryId", "0");
                    FILTER_URL = utilities.GetUrl() + "category/" + categoryId_str + "?order_by=asc";
                    loadSubItems_api(FILTER_URL);
                } else {
                    categoryId_str = preferences.getString("categoryId", "0");
                    FILTER_URL = utilities.GetUrl() + "category/" + categoryId_str + "?order_by=desc";
                    loadSubItems_api(FILTER_URL);
                }


                filterDialog.dismiss();


            }
        });


        imgFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterDialog.show();
            }
        });

//        filterModel.add(new FilterModel("Low to high"));
//        filterModel.add(new FilterModel("High to low"));
//
//        //filter_adapter = new FilterAdapter(getActivity(), filterModel);
//        recyclerViewFilter = (RecyclerView) rootView.findViewById(R.id.recyclerViewFilter);
//        recyclerViewFilter.setVisibility(View.GONE);
//        recyclerViewFilter.requestDisallowInterceptTouchEvent(true);
//        RecyclerView.LayoutManager mLayoutManagerFilter = new GridLayoutManager(getActivity(), 1);
//        recyclerViewFilter.setLayoutManager(mLayoutManagerFilter);
//        imgFilter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(filter==0)
//                {
//                    recyclerViewFilter.setVisibility(View.VISIBLE);
//                    filter=1;
//                }
//                else
//                {
//                    recyclerViewFilter.setVisibility(View.GONE);
//                    filter=0;
//                }
//
//            }
//        });

        //  recyclerView_subitems.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
//        recyclerViewFilter.setItemAnimator(new DefaultItemAnimator());
//        recyclerViewFilter.setAdapter(filter_adapter);


        scroll = (LinearLayout) rootView.findViewById(R.id.scroll);

        recyclerView_subitems.requestDisallowInterceptTouchEvent(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView_subitems.setLayoutManager(mLayoutManager);
        //  recyclerView_subitems.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView_subitems.setItemAnimator(new DefaultItemAnimator());
        recyclerView_subitems.setAdapter(sub_item_adapter);


        recyclerView_subitems.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // Toast.makeText(getContext(), "Lst", Toast.LENGTH_SHORT).show();
                    if (next_page_url != null && !next_page_url.isEmpty() && !next_page_url.equals("null")) {
                        loadSubItems_api(next_page_url);
                    }

                }
            }
        });


        if (lasPosition == 0) {
            recyclerView_subitems.scrollToPosition(0);
        } else {
            recyclerView_subitems.scrollToPosition(lasPosition - 2);
        }


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String titleMain = bundle.getString("titleMain", "THE MARKET");
            txt_actionbar_Title.setText(titleMain);
        }

        try {
            // Set title bar



            setApplicationlanguage = preferences.getString("setApplicationlanguage", "");
            login_token = preferences.getString("login_token", "");

            Hawk.put("login_token",login_token);

            Log.v("login_token", login_token);

            categoryId_str = preferences.getString("categoryId", "0");
            loadCategories_api();
            if (categoryId_str != null && !categoryId_str.isEmpty() && !categoryId_str.equals("null")) {
                JSON_URL_CATEGORIES = utilities.GetUrl() + "category/" + categoryId_str;
            }

            if (logedIn == 0) {
                txtCartCount.setVisibility(View.INVISIBLE);
                loadSubItems_api(JSON_URL_CATEGORIES);


            } else {
                loadCartItems_api_Count();


                GET_WishList_api();

            }


            //0 is the default value.


        } catch (Exception ex) {
            String error = ex.getMessage().toString();
            String h = error;
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


    private void set_Category() {


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView_subCat);
        recyclerView.setLayoutManager(layoutManager);
        submenu_category_Adapter = new Submenu_category_Adapter(getActivity(), itemCatList, SubMenu_Fragment.this);
        recyclerView.setAdapter(submenu_category_Adapter);
    }


    private void loadCategories_api() {
        //creating a string request to send request to the url

        final String categoryId = preferences.getString("categoryId", "0");
        URL_CHILD_CAT = utilities.GetUrl() + "categories";
        Log.v("URL_CHILD_CAT", URL_CHILD_CAT);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CHILD_CAT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.v("response", response);
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            String status = obj.getString("status");
                            if (status.equals("200")) {

                                JSONArray obj_categories = obj.getJSONArray("categories");

                                for (int i = 0; i < obj_categories.length(); i++) {

                                    JSONObject categoryObject = obj_categories.getJSONObject(i);
                                    String child_id = categoryObject.getString("id");
                                    String h = child_id;
                                    String cat = categoryId;
                                    String cats = cat;
                                    if (child_id.equals(categoryId)) {
                                        itemCatList.add(new Category_Model(categoryId, getString(R.string.All), true));
                                        JSONArray chilCatObj = categoryObject.getJSONArray("children");

                                        for (int j = 0; j < chilCatObj.length(); j++) {
                                            JSONObject sub_cat_obj = chilCatObj.getJSONObject(j);

                                            itemCatList.add(new Category_Model(sub_cat_obj.getString("id"), sub_cat_obj.getString("name"), false));

                                        }
                                    }


                                    // do smth
                                }

                                set_Category();


                                //   set_Main_Items();
                                //  item_main_adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(getContext(), getString(R.string.Server_Error), Toast.LENGTH_LONG).show();
                            }


                        } catch (JSONException e) {
                            if (e != null && !e.equals("null")) {
                                String mjk = e.getMessage().toString();
                                Log.v("errorgetMessage()", mjk);
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
                Log.v("lang", setApplicationlanguage);

                return headers;
            }

        };


        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //adding the string request to request queue
        requestQueue.add(stringRequest);

    }

    public boolean checkAlreadyExist(String id) {
        exist = false;
        for (int i = 0; i < data_Sub_Items.size(); i++) {
            if (data_Sub_Items.get(i).getId().equals(id)) {
                exist = true;
            }


        }
        return exist;
    }

    public void loadSubItems_api(final String JSON_URL_CATEGORIES) {
        //creating a string request to send request to the url

        final ACProgressFlower dialog = new ACProgressFlower.Builder(getActivity())
                .build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Log.v("JSON_URL_CATEGORIES", JSON_URL_CATEGORIES);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL_CATEGORIES,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Log.v("response_subItems", response);

                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);


                            String status = obj.getString("status");
                            Log.v("status", status);

                            if (status.equals("200")) {

                                dialog.dismiss();
                                JSONObject obj_products = obj.getJSONObject("products");
                                String current_page = obj_products.getString("current_page");
                                JSONArray jsonArray_data = obj_products.getJSONArray("data");

                                for (int i = 0; i < jsonArray_data.length(); i++) {
                                    JSONObject jsondata = jsonArray_data.getJSONObject(i);
                                    String id = jsondata.getString("id");
                                    String name = jsondata.getString("name");
                                    String cover = jsondata.getString("cover");
                                    String quantity = jsondata.getString("weight");
                                    String price = jsondata.getString("price");

                                    if (checkAlreadyExist(id) == false) {
                                        data_Sub_Items.add(new Item_Sub_Model
                                                (jsondata.getString("id"), jsondata.getString("name"), jsondata.getString("cover"),
                                                        quantity, jsondata.getString("price"), "notfavourite"));
                                    }

//                                    if (data_Sub_Items.size() > 0) {
//
//
//
//                                            if (data_Sub_Items.get(i).getId().equals(id)) {
//
//                                            } else {
//                                                data_Sub_Items.add(new Item_Sub_Model
//                                                        (jsondata.getString("id"), jsondata.getString("name"), jsondata.getString("cover"),
//                                                                quantity, jsondata.getString("price"), "notfavourite"));
//
//                                            }
//
//
//                                    } else {
//
//                                    }


                                }


                                if (wish_list_product_ID.size() > 0) {
                                    if (data_Sub_Items.size() > 0) {

                                        for (int i = 0; i < data_Sub_Items.size(); i++) {
                                            for (int j = 0; j < wish_list_product_ID.size(); j++) {
                                                String wishesd_item_id = wish_list_product_ID.get(j);
                                                if (wishesd_item_id.equals(data_Sub_Items.get(i).getId())) {

                                                    data_Sub_Items.get(i).setStatusWishList("favourite");
                                                }
                                            }


                                        }
                                    }


                                }
                                next_page_url = obj_products.getString("next_page_url");

                                lastPageUrl = obj_products.getString("last_page_url");
                                Log.v("next_page_url", next_page_url);
                                // do smth


                                sub_item_adapter.notifyDataSetChanged();
                                // set_Sub_Items(next_page_url);

                                //submenu_category_Adapter.notifyDataSetChanged();


                                //   set_Main_Items();
                                //  item_main_adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(getContext(), getString(R.string.Server_Error), Toast.LENGTH_LONG).show();
                            }


                        } catch (JSONException e) {
                            if (e != null && !e.equals("null")) {
                                String mjk = e.getMessage().toString();
                                Log.v("errorgetMessage()", mjk);
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
                headers.put("lang", setApplicationlanguage);
                Log.v("lang", setApplicationlanguage);

                return headers;
            }

        };


        //creating a request queue

        //adding the string request to request queue
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                0,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }

    public void Add_TO_WishList_api(final String action) {

        URL_ADD_WISH_LIST = utilities.GetUrl() + "accounts/wishlist/add";
        Log.v("URL_ADD_WISH_LIST", URL_ADD_WISH_LIST);
        final String product_id = preferences.getString("productId", "");
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest sr = new StringRequest(Request.Method.POST, URL_ADD_WISH_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            String status = obj.getString("status");
                            Log.v("status", status);

                            String success = obj.getString("success");

                            if (status.equals("200")) {

                                if (success.equals("1")) {
                                    String msg = obj.getString("msg");
                                    //Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                                }

                            } else {
                                Toast.makeText(getActivity(), getString(R.string.Server_Error), Toast.LENGTH_LONG).show();
                            }


                        } catch (JSONException e) {
                            if (e != null && !e.equals("null")) {
                                String mjk = e.getMessage().toString();
                                Log.v("errorgetMessage()", mjk);
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
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", action);
                params.put("product_id", product_id);
                return params;
            }

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

    }

    public void DELETE_FROM_WishList_api() {

        URL_ADD_WISH_LIST = utilities.GetUrl() + "accounts/wishlist/add";
        Log.v("URL_ADD_WISH_LIST", URL_ADD_WISH_LIST);
        final String product_id = preferences.getString("productId", "");
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest sr = new StringRequest(Request.Method.POST, URL_ADD_WISH_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            String status = obj.getString("status");
                            Log.v("status", status);

                            String success = obj.getString("success");

                            if (status.equals("200")) {

                                if (success.equals("1")) {
                                    String msg = obj.getString("msg");
                                    Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                                }

                            } else {
                                Toast.makeText(getActivity(), getString(R.string.Server_Error), Toast.LENGTH_LONG).show();
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
                params.put("action", "add");
                params.put("product_id", product_id);
                return params;
            }

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

    }

    public void GET_WishList_api() {

        GET_WISH_LIST = utilities.GetUrl() + "accounts/wishlist";
        final String product_id = preferences.getString("productId", "");
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest sr = new StringRequest(Request.Method.GET, GET_WISH_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        wish_list_product_ID.clear();
                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            String status = obj.getString("status");
                            Log.v("status", status);


                            if (status.equals("200")) {
                                JSONArray jsonArray_wishlist = obj.getJSONArray("wishlist");
                                if (jsonArray_wishlist.length() > 0) {
                                    for (int i = 0; i < jsonArray_wishlist.length(); i++) {
                                        JSONObject jsondata = jsonArray_wishlist.getJSONObject(i);
                                        String id = jsondata.getString("id");
                                        String product_id = jsondata.getString("product_id");
                                        wish_list_product_ID.add(product_id);
                                    }

                                    //Listing sub items in the page based on previous page click
                                    loadSubItems_api(JSON_URL_CATEGORIES);
                                } else {
                                    loadSubItems_api(JSON_URL_CATEGORIES);
                                }


                            } else {
                                Toast.makeText(getActivity(), getString(R.string.Server_Error), Toast.LENGTH_LONG).show();
                            }


                        } catch (JSONException e) {
                            if (e != null && !e.equals("null")) {
                                String mjk = e.getMessage().toString();
                                Log.v("errorgetMessage()", mjk);
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

    }

    public void loadCartItems_api_Count() {

        //creating a string request to send request to the url
        String JSON_URL_LIST_CART = utilities.GetUrl() + "cart";

        Log.v("JSON_URL_CATEGORIES", JSON_URL_LIST_CART);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL_LIST_CART,
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
                                        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                                        final SharedPreferences.Editor editor = sharedpreferences.edit();
                                        editor.putString("cartCount", cartCount);
                                        editor.commit();
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
                            if (e != null && !e.equals("null")) {
                                String mjk = e.getMessage().toString();
                                Log.v("errorgetMessage()", mjk);
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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        //adding the string request to request queue
        requestQueue.add(stringRequest);


    }

    @Override
    public void onResume() {
        Log.e("DEBUG", "onResume of HomeFragment");
        //loadItems_api();
        // edt_Search.setText("");
        loadCartItems_api_Count();
        super.onResume();
    }
    public void onDestroy(){
        Hawk.put("pages", "home");
        super.onDestroy();

    }


}
