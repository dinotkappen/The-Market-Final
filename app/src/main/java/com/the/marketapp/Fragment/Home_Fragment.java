package com.the.marketapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.the.marketapp.Adapter.Banner_Img_Adapter;
import com.the.marketapp.Adapter.Item_Main_Adapter;
import com.the.marketapp.Model.BannerImage_Model;
import com.the.marketapp.Model.Cart_Item_Model;
import com.the.marketapp.Model.Item_Main_Model;
import com.the.marketapp.Other.NetworkChangeReceiver;
import com.the.marketapp.Other.Utilities;
import com.the.marketapp.R;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class Home_Fragment extends Fragment {

    int logedIn;
    View rootView;
    String cartCount="";
    String fromSearch;
    Utilities utilities;
    TextView txtCartCount;
    RequestQueue cartQueue;
    MainActivity mainActivity;
    AppBarLayout appBarLayout;
    public EditText edt_Search;
    StringRequest cartRequest;
    SwipeRefreshLayout Refresh;
    String setApplicationlanguage;
    SharedPreferences preferences;
    private RecyclerView recyclerView;
    RecyclerView recycler_view_Search;
    private boolean isConnected = false;
    private NetworkChangeReceiver receiver;
    private static String JSON_URL_BANNERS;
    private static ViewPager mPager_Banner;
    private static int NUM_PAGES_BANNER = 0;
    RelativeLayout relative_viewpager_banner;
    private static String JSON_URL_CATEGORIES;
    private static int currentPage_Banner = 0;
    private Item_Main_Adapter obj_item_main_adapter_;
    private Banner_Img_Adapter data_Banner_Images_adapter;
    String JSON_URL_LIST_CART, login_token, JSON_URL_SEARCH;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    ArrayList<Item_Main_Model> data_Main_Items = new ArrayList<Item_Main_Model>();
    ArrayList<Cart_Item_Model> cart_item_ModelList = new ArrayList<Cart_Item_Model>();
    ArrayList<BannerImage_Model> data_Banner_Model = new ArrayList<BannerImage_Model>();

    public Home_Fragment() {
        // Required empty public constructor
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        getContext().registerReceiver(receiver, filter);

//        try {
//            if (utilities.isOnline(getActivity())) {

                // here
                if (savedInstanceState == null) {

                    rootView = inflater.inflate(R.layout.fragment_home_, container, false);
                    Utilities.removePhoneKeypad(rootView);
                    mainActivity.navigation.setVisibility(View.VISIBLE);
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(R.layout.actionbar_home);
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(getLayoutInflater().inflate(R.layout.actionbar_home, null),
                            new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));
                    TextView txt_actionbar_Title = (getActivity()).findViewById(R.id.txt_actionbar_Title);
                    ImageView imgMenu =  ( getActivity()).findViewById(R.id.imgMenu);
                    imgMenu.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!mainActivity.drawer.isDrawerOpen(GravityCompat.START))
                                mainActivity.drawer.openDrawer(Gravity.START);
                            else mainActivity.drawer.closeDrawer(Gravity.END);
                        }
                    });
                    txtCartCount = (TextView) ((AppCompatActivity) getActivity()).findViewById(R.id.txtCartCount);
                    // txt_actionbar_Title.setText("THE MARKET");
                    txt_actionbar_Title.setText(getString(R.string.THE_MARKET));
                    LinearLayout linearMenu = (LinearLayout) ((AppCompatActivity) getActivity()).findViewById(R.id.linearMenu);
                    //  mainActivity.toggle.setHomeAsUpIndicator(R.mipmap.menu_white);


                    linearMenu.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!mainActivity.drawer.isDrawerOpen(GravityCompat.START))
                                mainActivity.drawer.openDrawer(Gravity.START);
                            else mainActivity.drawer.closeDrawer(Gravity.END);
                        }
                    });
//                    mainActivity.toggle.setHomeAsUpIndicator(R.mipmap.menu_white);
//                    mainActivity.toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            mainActivity.drawer = (DrawerLayout)rootView.findViewById(R.id.drawer_layout);
//                            mainActivity.toggle = new ActionBarDrawerToggle(
//                                    getActivity(), mainActivity.drawer, mainActivity.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//                            mainActivity.toggle.setDrawerIndicatorEnabled(false);
//                            mainActivity.toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    mainActivity.drawer.openDrawer(GravityCompat.START);
//                                }
//                            });
//                            mainActivity.toggle.setHomeAsUpIndicator(R.mipmap.menu_white);
//                            mainActivity.drawer.addDrawerListener(mainActivity.toggle);
//                            mainActivity.toggle.syncState();
//                        }
//                    });
                    ImageView imgActionBarIcon = (ImageView) ((AppCompatActivity) getActivity()).findViewById(R.id.img_bar_icon);
                    imgActionBarIcon.setVisibility(View.VISIBLE);
                    preferences = getActivity().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                     logedIn = preferences.getInt("logedIn", 0);
                    JSON_URL_CATEGORIES = utilities.GetUrl() + "categories";
                    JSON_URL_BANNERS = utilities.GetUrl() + "banners";
                    setApplicationlanguage = preferences.getString("setApplicationlanguage", "");
                    fromSearch = preferences.getString("fromSearch", "0");
                    recycler_view_Search =  rootView.findViewById(R.id.recycler_view_SearchHome);



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

                    appBarLayout = (AppBarLayout) rootView.findViewById(R.id.appbar_home);
                    appBarLayout.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#330000ff")));
                    //appBarLayout.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#550000ff")));

                    Refresh =  rootView.findViewById(R.id.swipe_refresh);
                    recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
                    relative_viewpager_banner = (RelativeLayout) rootView.findViewById(R.id.relative_viewpager_banner);
                    edt_Search = (EditText) rootView.findViewById(R.id.edt_Search_Home);

                    obj_item_main_adapter_ = new Item_Main_Adapter(getActivity(), data_Main_Items);
                    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(obj_item_main_adapter_);


                    Refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            loadBannerList_api();
                            loadItems_api();
                        }
                    });

                    edt_Search.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Fragment fragment = new SearchFragment();
                            FragmentManager fragmentManager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_container, fragment);
                            fragmentTransaction.addToBackStack(null);
                            Bundle bundle = new Bundle();
                            bundle.putString("from", "HomeFragment");
                            fragment.setArguments(bundle);
                            fragmentTransaction.commit();
                        }
                    });


                    //  prepareAlbums();
                    loadBannerList_api();


                    loadItems_api();


//                customerType_Dialog = new Dialog(getActivity());
//                customerType_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                customerType_Dialog.setContentView(R.layout.dialog_customertype);
//                customerType_Dialog.setCanceledOnTouchOutside(false);
//                listView_CustomerType = (ListView) rootView.findViewById(R.id.listView_CustomerType);
//                customerType_Values = new ArrayList<>();
//
//                ArrayAdapter<String> adapter_BodyColor = new ArrayAdapter<String>
//                        (getActivity(), android.R.layout.simple_list_item_1, customerType_Values) {
//                    @Override
//                    public View getView(int position, View convertView, ViewGroup parent) {
//                        // Get the Item from ListView
//                        View view = super.getView(position, convertView, parent);
//
//                        // Initialize a TextView for ListView each Item
//                        TextView tv = (TextView) view.findViewById(android.R.id.text1);
//
//                        // Set the text color of TextView (ListView Item)
//                        tv.setTextColor(getResources().getColor(R.color.colorBlack));
//
//                        // Generate ListView Item using TextView
//                        return view;
//                    }
//                };
//
//                listView_CustomerType.setAdapter(adapter_BodyColor);


//        // Capture ListView item click
//                listView_CustomerType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    public void onItemClick(AdapterView<?> parent, View view,
//                                            int position, long id) {
//
//                        String str_CustomerType = (String) listView_CustomerType.getItemAtPosition(position);
//                        Log.v("str_CustomerType",str_CustomerType);
//
//                        customerType_Dialog.dismiss();
//
//
//                    }
//                });

                    Log.v("logedIn", "" + logedIn);
                    txtCartCount.setVisibility(View.INVISIBLE);

                    if (logedIn == 1) {
                        login_token = preferences.getString("login_token", "");
                        txtCartCount.setVisibility(View.VISIBLE);

//                    cartFragment.loadCartItems_api();
//                    cartCount = preferences.getString("cartCount", "0");
//                    Log.v("cartCount", cartCount);
                        //txtCartCount.setText(cartCount);

                        loadCartItems_api_Count();

                    }
                }


//            } else {
//
//                rootView = inflater.inflate(R.layout.msg_no_internet, container, false);
//            }
//        } catch (Exception ex) {
//            String msg = ex.getMessage().toString();
//            String c = msg;
//        }


        return rootView;
    }


    private void setBannerImage() {


        try {
            data_Banner_Images_adapter = new Banner_Img_Adapter(getActivity(), data_Banner_Model);
            mPager_Banner = (ViewPager) rootView.findViewById(R.id.pager_banner);
            if (getActivity() != null) {
                mPager_Banner.setAdapter(data_Banner_Images_adapter);
            }


            CirclePageIndicator indicator = rootView.findViewById(R.id.indicator);

            indicator.setViewPager(mPager_Banner);


            final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
            indicator.setRadius(4 * density);

            NUM_PAGES_BANNER = data_Banner_Model.size();

            // Auto start of viewpager
            final Handler handler = new Handler();
            final Runnable Update = new Runnable() {
                public void run() {
                    if (currentPage_Banner == NUM_PAGES_BANNER) {
                        currentPage_Banner = 0;
                    }
                    mPager_Banner.setCurrentItem(currentPage_Banner++, true);
                }
            };

            Timer swipeTimer = new Timer();
            swipeTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(Update);
                }
            }, 6000, 6000);

            // Pager listener over indicator
            indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageSelected(int position) {
                    currentPage_Banner = position;

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

    private void set_Main_Items() {
        try {
            obj_item_main_adapter_ = new Item_Main_Adapter(getActivity(), data_Main_Items);

            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(obj_item_main_adapter_);
        } catch (Exception ex) {
            String s = ex.getMessage().toString();
            Log.v("set_Main_Items", ex.getMessage().toString());
        }
    }

    public void clearEditText() {
        edt_Search.setText("");
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

    private void loadBannerList_api() {
//

        //creating a string request to send request to the url
        StringRequest bannerRequest = new StringRequest(Request.Method.GET, JSON_URL_BANNERS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            String status = obj.getString("status");
                            if (status.equals("200")) {
                                //so here we are getting that json array
                                JSONArray bannersArray = obj.getJSONArray("banners");

                                data_Banner_Model.clear();
                                //now looping through all the elements of the json array
                                for (int i = 0; i < bannersArray.length(); i++) {
                                    //getting the json object of the particular index inside the array
                                    JSONObject jsondata = bannersArray.getJSONObject(i);
                                    String url = jsondata.getString("banners_image");
                                    String h = url;

                                    data_Banner_Model.add(new BannerImage_Model(jsondata.getString("banners_image")));


                                }


                                setBannerImage();
                            } else {
                                Toast.makeText(getContext(), getString(R.string.Server_Error), Toast.LENGTH_LONG).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs

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
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //adding the string request to request queue
        requestQueue.add(bannerRequest);
    }


    private void loadItems_api() {
        //creating a string request to send request to the url
        Refresh.setRefreshing(true);
//        final ACProgressFlower dialog = new ACProgressFlower.Builder(getActivity())
//                .build();
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.show();

        StringRequest itemsRequest = new StringRequest(Request.Method.GET, JSON_URL_CATEGORIES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            String status = obj.getString("status");
                            if (status.equals("200")) {

//                                dialog.dismiss();
                                Refresh.setRefreshing(false);
                                //so here we are getting that json array
                                JSONArray jsonarray = obj.getJSONArray("categories");


                                data_Main_Items.clear();
                                //now looping through all the elements of the json array
                                for (int i = 0; i < jsonarray.length(); i++) {
                                    //getting the json object of the particular index inside the array
                                    JSONObject jsondata = jsonarray.getJSONObject(i);
                                    String name = jsondata.getString("name");
                                    String cover = jsondata.getString("cover");
                                    data_Main_Items.add(new Item_Main_Model(jsondata.getString("id"), jsondata.getString("name"), jsondata.getString("cover")));

                                }

//                                set_Main_Items();
//                                recyclerView.setAdapter(obj_item_main_adapter_);
                                obj_item_main_adapter_.notifyDataSetChanged();
                                //  item_main_adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(getContext(), getString(R.string.Server_Error), Toast.LENGTH_LONG).show();
                            }


                        } catch (JSONException e) {
                            Log.v("errorgetMessage()", e.getMessage().toString());
//                            dialog.dismiss();
                            Refresh.setRefreshing(false);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


//                        dialog.dismiss();
                        Refresh.setRefreshing(false);
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

                        // Toast.makeText(getContext(), error.getMessage().toString(), Toast.LENGTH_SHORT).show();


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
        requestQueue.add(itemsRequest);


    }


    public void loadCartItems_api_Count() {

        //creating a string request to send request to the url
        JSON_URL_LIST_CART = utilities.GetUrl() + "cart";

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

                                    cartCount = obj.getString("cartCount");
                                    if (cartCount != null && !cartCount.isEmpty() && !cartCount.equals("null")) {
                                        Log.v("cartCount_Home", cartCount);
                                        txtCartCount.setText(cartCount);
                                        txtCartCount.setVisibility(View.VISIBLE);
                                        try {
                                            SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                                            final SharedPreferences.Editor editor = sharedpreferences.edit();
                                            editor.putString("cartCount", cartCount);
                                            editor.apply();
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
                            Log.v("errorgetMessage()", e.getMessage().toString());
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

                return headers;
            }

        };


        //creating a request queue
         cartQueue = Volley.newRequestQueue(getContext());

        //adding the string request to request queue
        cartQueue.add(cartRequest);


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
        cartQueue.cancelAll(cartRequest);
        super.onDestroy();

    }
}
