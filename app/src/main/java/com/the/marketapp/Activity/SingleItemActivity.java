package com.the.marketapp.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.the.marketapp.Adapter.RelatedProductsAdapter;
import com.the.marketapp.Adapter.SingleItem_Banner_ViewPagerAdapter;
import com.the.marketapp.Adapter.Units_Adapter;
import com.the.marketapp.Fragment.SubMenu_Fragment;
import com.the.marketapp.Model.Related_Products_Model;
import com.the.marketapp.Model.SingleItem_Banner_Img_Model;
import com.the.marketapp.Model.Unit_List_Model;
import com.the.marketapp.Other.NetworkChangeReceiver;
import com.the.marketapp.Other.Utilities;
import com.the.marketapp.R;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cc.cloudist.acplibrary.ACProgressFlower;

public class SingleItemActivity extends AppCompatActivity {
    private static String URL_SINGLE_ITEM, URL_RELATED_PRODUCTS, URL_ADD_TO_CART;

    SharedPreferences preferences;
    String login_token, setApplicationlanguage;

    String availQty;
    String JSON_URL_LIST_CART;
    String item_name;
    String price = null;
    //vars
    String productAttribute_ID;
    private ArrayList<String> units_List = new ArrayList<>();
    SubMenu_Fragment subMenu_fragment;
    private RecyclerView recyclerView_RelatedProducts;
    private RelatedProductsAdapter relatedproductsadapter;

    ArrayList<Related_Products_Model> relatedProductsList = new ArrayList<Related_Products_Model>();
    ArrayList<Unit_List_Model> data_Unit_Items = new ArrayList<Unit_List_Model>();
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ArrayList<SingleItem_Banner_Img_Model> bannerImgArrayList;

    SingleItem_Banner_Img_Model bannerSingleItem_Image_model = new SingleItem_Banner_Img_Model();
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    Utilities utilities;
    String productId = "";

    TextView txt_item_name, txt_money, txt_qty, txt_desc, txt_head_relatedProducts;
    TextView txtCartCount;
    public static TextView txtCart;
    ImageView img_shoppinglist, img_wishlist, img_share;
    EditText edt_Count;
    ImageView img_plus, img_minus;
    public static LinearLayout linear_cart;
    MainActivity mainActivity;
    String wishListStatus;
    TextView txt_actionbar_Title;
    static LinearLayout NoInternet;
    private NetworkChangeReceiver receiver;
    ProgressBar loading;
    ImageView img_refesh;
    int int_count = 1;
    String str_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singl_item);
        Hawk.init(getApplicationContext())
                .setEncryptionMethod(HawkBuilder.EncryptionMethod.MEDIUM)
                .setStorage(HawkBuilder.newSqliteStorage(getApplicationContext()))
                .setLogLevel(LogLevel.FULL)
                .build();
        NoInternet = findViewById(R.id.lean_internet);
        Hawk.put("pages", "single_item");
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);

        try {
            if (utilities.isOnline(this)) {
                // Inflate the layout for this fragment





                txt_item_name = (TextView) findViewById(R.id.txt_item_name);
                txt_money = (TextView) findViewById(R.id.txt_money);
                txt_qty = (TextView) findViewById(R.id.txt_qty);
                txt_desc = (TextView) findViewById(R.id.txt_desc);
                txt_head_relatedProducts = (TextView) findViewById(R.id.txt_head_relatedProducts);
                edt_Count = (EditText) findViewById(R.id.edt_Count);
                img_plus = (ImageView) findViewById(R.id.img_plus);
                img_minus = (ImageView) findViewById(R.id.img_minus);
                linear_cart = (LinearLayout) findViewById(R.id.linear_cart);
                img_shoppinglist = (ImageView) findViewById(R.id.img_shoppinglist);
                img_wishlist = (ImageView) findViewById(R.id.img_wishlist);
                img_share = (ImageView) findViewById(R.id.img_share);
                txtCart = (TextView) findViewById(R.id.txtCart);
                loading = findViewById(R.id.loading);
                img_refesh = findViewById(R.id.img_refesh);

                img_refesh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        loading.setVisibility(View.VISIBLE);

                        final Timer timer = new Timer();
                        timer.scheduleAtFixedRate(
                                new java.util.TimerTask() {
                                    @Override
                                    public void run() {
                                        //
                                        runOnUiThread(new Runnable() {

                                            @Override
                                            public void run() {

                                                // Stuff that updates the UI
                                                loading.setVisibility(View.GONE);

                                            }
                                        });
                                        //                                        loading.setVisibility(View.GONE);
                                        timer.cancel();
                                    }
                                },
                                2000, 5000);
                    }
                });

                getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                getSupportActionBar().setCustomView(R.layout.actionbar_layout_without_menu);
                getSupportActionBar().setCustomView(getLayoutInflater().inflate(R.layout.actionbar_layout_without_menu, null),
                        new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));
                LinearLayout linearCartLalyoutNomenu = (LinearLayout) findViewById(R.id.linearCartLalyoutNomenu);
                linearCartLalyoutNomenu.setVisibility(View.VISIBLE);
                txt_actionbar_Title = (TextView) findViewById(R.id.txt_actionbar_TitleNomenu);
                txtCartCount = (TextView) findViewById(R.id.txtCartCountNomenu);

                LinearLayout linear_backArrow = (LinearLayout) findViewById(R.id.linear_backArrow);
                linear_backArrow.setVisibility(View.VISIBLE);

                linear_backArrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
                NoInternet.setVisibility(View.GONE);

            } else {
                NoInternet.setVisibility(View.VISIBLE);
            }
        } catch (
                Exception ex) {
            String s = ex.getMessage().toString();
            String h = s;
        }


    }

    //    protected void onNewIntent(Intent intent)
//    {
//        super.onNewIntent(intent);
//        setIntetnt();
//        shareIntent();
//    }
    public void shareIntent() {
        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
        if (appLinkData != null) {
            productId = appLinkData.getLastPathSegment();

        }

    }

    private void setBannerImage() {
        try {

            mPager = (ViewPager) findViewById(R.id.pager_singleItem);
            mPager.setAdapter(new SingleItem_Banner_ViewPagerAdapter(this, bannerImgArrayList));

            CirclePageIndicator indicator = (CirclePageIndicator)
                    findViewById(R.id.indicator_singleItem);
            NUM_PAGES = bannerImgArrayList.size();
            if (NUM_PAGES > 1) {
                indicator.setViewPager(mPager);

            }


            final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
            indicator.setRadius(4 * density);


            // Auto start of viewpager
            final Handler handler = new Handler();
            final Runnable Update = new Runnable() {
                public void run() {
                    if (currentPage == NUM_PAGES) {
                        currentPage = 0;
                    }
                    mPager.setCurrentItem(currentPage++, true);
                }
            };

            Timer swipeTimer = new Timer();
            swipeTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(Update);
                }
            }, 3000, 3000);

            // Pager listener over indicator
            indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageSelected(int position) {
                    currentPage = position;

                }

                @Override
                public void onPageScrolled(int pos, float arg1, int arg2) {

                }

                @Override
                public void onPageScrollStateChanged(int pos) {

                }
            });
        } catch (Exception ex) {
            String s = ex.getMessage().toString();
            String h = s;
        }

    }
//
//    /**
//     * Changes the icon of the drawer to back
//     */
//    public void showBackButton() {
//        if (getActivity() instanceof AppCompatActivity) {
////            getActivity().onBackPressed();
//            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        }
//    }

    private void set_Related_Products() {

        relatedproductsadapter = new RelatedProductsAdapter(this, relatedProductsList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView_RelatedProducts.setLayoutManager(mLayoutManager);
        recyclerView_RelatedProducts.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView_RelatedProducts.setItemAnimator(new DefaultItemAnimator());
        recyclerView_RelatedProducts.setAdapter(relatedproductsadapter);
    }


    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }

    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    private void setUnits() {
        productAttribute_ID = preferences.getString("productAttribute_ID", "");
        if (productAttribute_ID != null && !productAttribute_ID.isEmpty() && !productAttribute_ID.equals("null")) {
            if (data_Unit_Items.size() > 0) {
                for (int i = 0; i < data_Unit_Items.size(); i++) {
                    if (productAttribute_ID.equals(data_Unit_Items.get(i).getId())) {
                        availQty = data_Unit_Items.get(i).getAvail_qty();
                        SharedPreferences sharedpreferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                        final SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("availQty", availQty);
                        editor.commit();
                        if (Integer.parseInt(availQty) > 0) {
                            txtCart.setText(getString(R.string.Add_to_Cart));
                            linear_cart.setClickable(true);
                            img_plus.setClickable(true);
                            img_minus.setClickable(true);
                        } else {
                            txtCart.setText(getString(R.string.OUT_OF_STOCK));
                            linear_cart.setClickable(false);
                            img_plus.setClickable(false);
                            img_minus.setClickable(false);
                        }

                    }
                }
            } else {
                Log.v("Noqty", "Noqty");
                SharedPreferences sharedpreferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                final SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("availQty", availQty);
                editor.commit();
                if (Integer.parseInt(availQty) > 0) {
                    txtCart.setText(R.string.Add_to_Cart);
                    linear_cart.setClickable(true);
                    img_plus.setClickable(true);
                    img_minus.setClickable(true);

                } else {
                    txtCart.setText(R.string.OUT_OF_STOCK);
                    linear_cart.setClickable(false);
                    img_plus.setClickable(false);
                    img_minus.setClickable(false);
                }

            }


        } else {
            Log.v("Noqty", "Noqty");
            SharedPreferences sharedpreferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("availQty", availQty);
            editor.commit();
            if (Integer.parseInt(availQty) > 0) {
                txtCart.setText(R.string.Add_to_Cart);
                linear_cart.setClickable(true);
                img_plus.setClickable(true);
                img_minus.setClickable(true);

            } else {
                txtCart.setText(R.string.OUT_OF_STOCK);
                linear_cart.setClickable(false);
                img_plus.setClickable(false);
                img_minus.setClickable(false);
            }
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView_Units = findViewById(R.id.recyclerView_Units);
        recyclerView_Units.setLayoutManager(layoutManager);
        Units_Adapter adapter = new Units_Adapter(this, data_Unit_Items);
        recyclerView_Units.setAdapter(adapter);
    }

    private void loadSingleItem_api(String productId) {
        //creating a string request to send request to the url
//
        final ACProgressFlower dialog = new ACProgressFlower.Builder(this)
                .build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        URL_SINGLE_ITEM = utilities.GetUrl() + "product/" + productId;

        Log.v("URL_SINGLE_ITEM", URL_SINGLE_ITEM);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_SINGLE_ITEM,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("response_single", response);


                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            String status = obj.getString("status");
                            if (status.equals("200")) {

                                dialog.dismiss();

                                JSONObject obj_products = obj.getJSONObject("product");
                                String productID = obj_products.getString("id");
                                item_name = obj_products.getString("name");
                                txt_actionbar_Title.setText(item_name);
                                String initialAvailQty = obj_products.getString("quantity");
                                String description = obj_products.getString("description");
                                //String price = obj_products.getString("price");
                                String cover = obj_products.getString("cover");

                                JSONArray attributesJsonObj = obj_products.getJSONArray("attributes");
                                if (attributesJsonObj.length() > 0) {
                                    for (int k = 0; k < attributesJsonObj.length(); k++) {
                                        price = attributesJsonObj.getJSONObject(k).getString("price");
                                    }
                                } else {
                                    price = obj_products.getString("price");
                                }
                                bannerSingleItem_Image_model.setSinleItem_banner_Img(cover);
                                bannerImgArrayList.add(bannerSingleItem_Image_model);

                                availQty = initialAvailQty;
                                txt_item_name.setText(item_name);
                                txt_money.setText(getString(R.string._QAR) + " " + price);
                                if (description != null && !description.isEmpty() && !description.equals("null")) {
                                    txt_desc.setText(description);
                                }

                                JSONArray obj_images = obj.getJSONArray("images");
                                int u = obj_images.length();
                                for (int i = 0; i < obj_images.length(); i++) {
                                    String img_src = obj_images.getJSONObject(i).getString("src");
                                    String img_id = obj_images.getJSONObject(i).getString("id");

                                    bannerSingleItem_Image_model.setSinleItem_banner_Img(img_src);
                                    if (!bannerImgArrayList.contains(img_src)) {
                                        bannerImgArrayList.add(bannerSingleItem_Image_model);
                                    }

//
                                }
                                int s = bannerImgArrayList.size();
                                int y = s;
                                setBannerImage();

                                //For seleciong attributes
                                JSONArray obj_attribute = obj.getJSONArray("productAttributes");
                                if (obj_attribute.length() > 0) {
                                    int du = obj_attribute.length();
                                    for (int i = 0; i < obj_attribute.length(); i++) {
                                        String attribute_id = obj_attribute.getJSONObject(i).getString("id");
                                        String attribute_avail_qty = obj_attribute.getJSONObject(i).getString("quantity");
                                        String attribute_price = obj_attribute.getJSONObject(i).getString("price");
                                        JSONArray attributes_values_obj = obj_attribute.getJSONObject(i).getJSONArray("attributes_values");

                                        String value = attributes_values_obj.getJSONObject(0).getString("value");
                                        Log.v("attribute_avail_qty_api", attribute_avail_qty);
                                        data_Unit_Items.add(new Unit_List_Model(attribute_id, attribute_avail_qty, attribute_price, value));
                                        SharedPreferences sharedpreferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                                        final SharedPreferences.Editor editor = sharedpreferences.edit();
                                        String initialID = obj_attribute.getJSONObject(0).getString("id");
                                        editor.putString("productAttribute_ID", initialID);
                                        editor.commit();


                                    }

                                } else {
                                    Log.v("initialAvailQty", initialAvailQty);
                                    availQty = initialAvailQty;
                                    SharedPreferences sharedpreferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                                    final SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putString("availQty", availQty);
                                    editor.putString("productAttribute_ID", "");

                                    editor.commit();
                                }
                                try {
                                    String qty = data_Unit_Items.get(0).getValue();
                                    if (qty != null && !qty.isEmpty() && !qty.equals("null")) {
                                        txt_qty.setText(qty);
                                    }

                                } catch (Exception ex) {
                                    String h = ex.getMessage().toString();
                                    Log.v("hjkl", h);
                                }
                                setUnits();
                                //   set_Main_Items();
                                //  item_main_adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(getApplicationContext(), getString(R.string.Server_Error), Toast.LENGTH_LONG).show();
                            }


                        } catch (JSONException e) {
                            if (e != null && !e.equals("null")) {
                                dialog.dismiss();
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
                headers.put("lang", setApplicationlanguage);
                Log.v("lang", setApplicationlanguage);

                return headers;
            }

        };


        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);


    }

    private void loadRelated_Products_api() {
        //creating a string request to send request to the url
//


        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_RELATED_PRODUCTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            String status = obj.getString("status");
                            if (status.equals("200")) {


                                JSONArray obj_rltd_products = obj.getJSONArray("related_products");
                                relatedProductsList.clear();
                                int related_products_cunt = obj_rltd_products.length();
                                if (related_products_cunt == 0) {
                                    txt_head_relatedProducts.setVisibility(View.GONE);
                                } else {
                                    for (int i = 0; i < obj_rltd_products.length(); i++) {
                                        JSONObject jsondata = obj_rltd_products.getJSONObject(i);
                                        txt_head_relatedProducts.setVisibility(View.VISIBLE);
                                        relatedProductsList.add(new Related_Products_Model(jsondata.getString("id"), jsondata.getString("name"), jsondata.getString("cover")));


                                    }

                                    set_Related_Products();
                                }


                                setBannerImage();

                                //   set_Main_Items();
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
                Log.v("lang", setApplicationlanguage);

                return headers;
            }

        };


        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);


    }

    private void Add_to_Cart_api() {


        productAttribute_ID = preferences.getString("productAttribute_ID", "");

        URL_ADD_TO_CART = utilities.GetUrl() + "cart/store?product=" + productId + "&quantity=" + int_count + "&productAttribute=" + productAttribute_ID;

        Log.v("token_notification", login_token);
        Log.v("URL_ADD_TO_CART", URL_ADD_TO_CART);

        final ACProgressFlower dialog = new ACProgressFlower.Builder(this)
                .build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADD_TO_CART,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("response", response);
                        SharedPreferences sharedpreferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                        final SharedPreferences.Editor editor = sharedpreferences.edit();
                        dialog.dismiss();

                        try {

                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            Log.v("response", response);

                            String status = obj.getString("status");
                            String msg=obj.getString("msg");
                            if (status.equals("200")) {
                                String success = obj.getString("success");

                                if (success.equals("1")) {
                                    editor.putString("productAttribute_ID", "");
                                    loadCartItems_api_Count();
                                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                } else {

                                    dialog.dismiss();
                                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                Toast.makeText(getApplicationContext(), getString(R.string.Server_Error), Toast.LENGTH_SHORT).show();


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
                Log.v("lang", setApplicationlanguage);

                return headers;
            }

        };


        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);


    }

    public void loadCartItems_api_Count() {

        //creating a string request to send request to the url
        JSON_URL_LIST_CART = utilities.GetUrl() + "cart";

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
                                        SharedPreferences sharedpreferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
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
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        //adding the string request to request queue
        requestQueue.add(stringRequest);


    }


    public boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            Intent in = new Intent(SingleItemActivity.this, MainActivity.class);
            in.putExtra("fragmentFromSingle", "" + fragment);
            startActivity(in);
            return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        Log.e("DEBUG", "onResume of HomeFragment");
        String titleMain = getIntent().getExtras().getString("titleMain");
        wishListStatus = getIntent().getExtras().getString("wishListStatus");
        productId = getIntent().getExtras().getString("productId");
        shareIntent();
        if (wishListStatus != null && !wishListStatus.isEmpty() && !wishListStatus.equals("null")) {
            if (wishListStatus.equals("favourite")) {
                img_wishlist.setImageDrawable(null);
                img_wishlist.setBackgroundResource(R.mipmap.ic_favorite_selected);
            } else {
                img_wishlist.setImageDrawable(null);
                img_wishlist.setBackgroundResource(R.mipmap.wish_selected);
            }
        }


        img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                 String shareBody = item_name + "\n" + price+" QAR" + "\n" +"View product here : "+ " https://www.themarket.qa/api/productview/" + productId;
              //  String shareBody = "https://www.themarket.qa";
                Log.v("shareBody", shareBody);
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Welcome to The Market.");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        //product_id = preferences.getString("productId", "");
        preferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        login_token = preferences.getString("login_token", "");
        Log.v("token_cart", login_token);
        setApplicationlanguage = preferences.getString("setApplicationlanguage", "");
        final int logedIn = preferences.getInt("logedIn", 0);
        Log.v("logedIn", "" + logedIn);
        txtCartCount.setVisibility(View.GONE);
        img_wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < subMenu_fragment.data_Sub_Items.size(); i++) {
                    if (productId.equals(subMenu_fragment.data_Sub_Items.get(i).getId())) {
                        wishListStatus = subMenu_fragment.data_Sub_Items.get(i).getStatusWishList();
                        String k = wishListStatus;
                        String kl = k;

                    }
                }
                int logedIn = preferences.getInt("logedIn", 0);
                Log.v("logedIn", "" + logedIn);
                if (logedIn == 1) {
                    if (wishListStatus.equals("favourite")) {
                        Log.v("delete", "delete");

                        img_wishlist.setImageDrawable(null);
                        img_wishlist.setBackgroundResource(R.mipmap.wish_selected);
                        if (productId != null && !productId.isEmpty() && !productId.equals("null")) {
                            Add_TO_WishList_api("delete");
                        }

                    } else {
                        //  Toast.makeText(mContext,"Add Wish List",Toast.LENGTH_SHORT).show();
//                                SharedPreferences.Editor editor = mContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
//                                editor.putString("productId", itemSubModel.getId());
//                                editor.apply();

//                                subMenu_fragment.data_Sub_Items.set
                        img_wishlist.setImageDrawable(null);
                        img_wishlist.setBackgroundResource(R.mipmap.ic_favorite_selected);

                        if (productId != null && !productId.isEmpty() && !productId.equals("null")) {
                            Add_TO_WishList_api("add");
                        }
                        Log.v("add", "add");

                    }

                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(SingleItemActivity.this);

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
                                    Intent in = new Intent(SingleItemActivity.this, LogInActivity.class);
                                    startActivity(in);
                                }
                            });


                    builder.show();
                }
            }
        });

        ImageView img_cart = (ImageView) findViewById(R.id.img_cart_actionbarNomenu);
        img_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(SingleItemActivity.this, CartActivity.class);
                startActivity(in);
            }
        });


        txt_actionbar_Title.setText(titleMain);


        int length = txt_actionbar_Title.length();
//                if (length > 25) {
//                    imgActionBarIcon.setVisibility(View.GONE);
//                } else {
//                    imgActionBarIcon.setVisibility(View.INVISIBLE);
//
//                }

        //  showBackButton();


        if (logedIn == 1) {
            loadCartItems_api_Count();
            txtCartCount.setVisibility(View.VISIBLE);
        }
        linear_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (logedIn == 1) {
                    availQty = preferences.getString("availQty", "0");
                    Log.v("availQty_cart", availQty);


                    if (Integer.parseInt(availQty) >= int_count) {
                        productAttribute_ID = preferences.getString("productAttribute_ID", "null");
                        Log.v("Attribute_ID_Single", productAttribute_ID);
                        Add_to_Cart_api();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please select a lower quantity", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(
                            SingleItemActivity.this);

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
                                    Intent in = new Intent(SingleItemActivity.this, LogInActivity.class);
                                    Hawk.put("from", "SingleItemActivity");
                                    startActivity(in);
                                }
                            });


                    builder.show();
                }
            }

        });


        // productId = prefs.getString("productId", "0"); //0 is the default value.
        // Log.v("productIdSingle", productId);


        URL_RELATED_PRODUCTS = utilities.GetUrl() + "related-products/";
        URL_RELATED_PRODUCTS = URL_RELATED_PRODUCTS + productId;

        bannerImgArrayList = new ArrayList<>();
        edt_Count.setText(String.format("%02d", int_count));
        txt_head_relatedProducts.setVisibility(View.VISIBLE);
        recyclerView_RelatedProducts = (RecyclerView) findViewById(R.id.recyclerView_RelatedProducts);

        relatedProductsList = new ArrayList<>();

        img_shoppinglist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // subMenu_fragment.recyclerView_subitems.requestDisallowInterceptTouchEvent(false);
//
                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("productId", productId);
                editor.putString("fromSingleItem", "1");
                editor.apply();


//                        Fragment fragment = new ShoppingListPopUpFragment();
//                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//
//                        transaction.add(R.id.fragment_containerSingleItem, new ShoppingListPopUpFragment());
//
//                        transaction.commit();

                Intent in = new Intent(SingleItemActivity.this, MainActivity.class);
                in.putExtra("fromSingleItemActivity", "1");
                startActivity(in);


            }
        });
        //prepareAlbums();


        img_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int_count = Integer.parseInt(edt_Count.getText().toString());
                int_count = int_count + 1;
                edt_Count.setText("" + String.format("%02d", int_count));
            }
        });

        img_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int_count = Integer.parseInt(edt_Count.getText().toString());
                if (int_count == 1) {
                    edt_Count.setText(String.format("%02d", int_count));
                } else {
                    int_count = int_count - 1;
                    edt_Count.setText(String.format("%02d", int_count));
                }

            }
        });
        if (productId != null && !productId.isEmpty() && !productId.equals("null")) {
            loadSingleItem_api(productId);
            loadRelated_Products_api();
        }


        super.onResume();
    }

    public void Add_TO_WishList_api(final String action) {

        String URL_ADD_WISH_LIST = utilities.GetUrl() + "accounts/wishlist/add";
        Log.v("URL_ADD_WISH_LIST", URL_ADD_WISH_LIST);

        RequestQueue queue = Volley.newRequestQueue(this);
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
                                    if (action == "delete") {
                                        for (int i = 0; i < subMenu_fragment.data_Sub_Items.size(); i++) {
                                            if (productId.equals(subMenu_fragment.data_Sub_Items.get(i).getId())) {
                                                subMenu_fragment.data_Sub_Items.get(i).setStatusWishList("notfavourite");
                                                subMenu_fragment.sub_item_adapter.notifyDataSetChanged();
                                            }
                                        }
                                    } else {
                                        for (int i = 0; i < subMenu_fragment.data_Sub_Items.size(); i++) {
                                            if (productId.equals(subMenu_fragment.data_Sub_Items.get(i).getId())) {
                                                subMenu_fragment.data_Sub_Items.get(i).setStatusWishList("favourite");
                                                subMenu_fragment.sub_item_adapter.notifyDataSetChanged();
                                            }
                                        }
                                    }
                                }

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
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", action);
                params.put("product_id", productId);
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

        String GET_WISH_LIST = utilities.GetUrl() + "accounts/wishlist";
        final String product_id = preferences.getString("productId", "");
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.GET, GET_WISH_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

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
                                        String product_idWishList = jsondata.getString("product_id");

                                        if (product_id.equals(product_idWishList)) {
                                            img_wishlist.setImageDrawable(null);
                                            img_wishlist.setBackgroundResource(R.mipmap.ic_favorite_selected);
                                        } else {
                                            img_wishlist.setImageDrawable(null);
                                            img_wishlist.setBackgroundResource(R.mipmap.wish_selected);
                                        }


                                    }


                                } else {

                                }


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
                Map<String, String> params = new HashMap<String, String>();
                params.put("userToken", login_token);
                params.put("lang", setApplicationlanguage);
                Log.v("lang", setApplicationlanguage);
                return params;
            }
        };
        queue.add(sr);

    }

    public void onDestroy() {

        super.onDestroy();

        Hawk.put("pages", "home");

    }
    @Override
    public void onBackPressed() {
        finish();
    }
}

