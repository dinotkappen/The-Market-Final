package com.the.marketapp.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
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
import com.the.marketapp.Adapter.Cart_Item_Adapter;
import com.the.marketapp.Other.NetworkChangeReceiver;
import com.the.marketapp.Model.Cart_Item_Model;
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
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;

import cc.cloudist.acplibrary.ACProgressFlower;

public class CartActivity extends AppCompatActivity {

    View rootView;
    String default_adrz;
    private Paint p = new Paint();
    Button btn_checkout;
    private RecyclerView recyclerView_cart;

    public ArrayList<Cart_Item_Model> cart_item_ModelList = new ArrayList<Cart_Item_Model>();

    public Cart_Item_Adapter cart_item_adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    String JSON_URL_LIST_CART, JSON_URL_UPDATE_CART_COUNT, JSON_URL_CHECKOUT_API;
    Utilities utilities;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences preferences;
    String login_token;
    public TextView txt_SubTotal, txt_net_totalCart, netTotal;
    String params_value;
    String availQty;
    public TextView txtCartCount;
    public String cartCount, cartCountAll;
    static LinearLayout linearCartNotVisible, linearCartMain;
    String cartId;
    SwipeController swipeController = null;
    String setApplicationlanguage;
    Double validateMinimumAmountDbl, minimum_cart_amount_dbl;
    static TextView NoItem;
    private NetworkChangeReceiver receiver;
    static LinearLayout NoInternet;
    ProgressBar loading;
    ImageView img_refesh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        Hawk.init(getApplicationContext())
                .setEncryptionMethod(HawkBuilder.EncryptionMethod.MEDIUM)
                .setStorage(HawkBuilder.newSqliteStorage(getApplicationContext()))
                .setLogLevel(LogLevel.FULL)
                .build();
        Hawk.put("pages", "cart");
//        try {
//            if (utilities.isOnline(this)) {
                // Inflate the layout for this fragment
                setContentView(R.layout.activity_cart);
                NoItem = (TextView) findViewById(R.id.txt_noitem);
                txt_SubTotal = (TextView) findViewById(R.id.txt_SubTotal);
                txt_net_totalCart = (TextView) findViewById(R.id.txt_net_totalCart);
                netTotal = (TextView) findViewById(R.id.netTotal);
                loading = findViewById(R.id.loading);
                img_refesh = findViewById(R.id.img_refesh);
                getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                getSupportActionBar().setCustomView(R.layout.actionbar_layout_without_menu);
                getSupportActionBar().setCustomView(getLayoutInflater().inflate(R.layout.actionbar_layout_without_menu, null),
                        new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));
                TextView txt_actionbar_Title = (TextView) findViewById(R.id.txt_actionbar_TitleNomenu);
                txt_actionbar_Title.setText(getString(R.string.MY_BAG));


                txtCartCount = (TextView) findViewById(R.id.txtCartCountNomenu);
                ImageView imgBackArrow = (ImageView) findViewById(R.id.imgBackArrowNomenu);
                imgBackArrow.setVisibility(View.VISIBLE);

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

                ImageView img_cart = (ImageView) findViewById(R.id.img_cart_actionbarNomenu);
                img_cart.setVisibility(View.INVISIBLE);

                preferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);

                imgBackArrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

                int logedIn = preferences.getInt("logedIn", 0);
                Log.v("logedIn", "" + logedIn);
                if (logedIn == 1) {
                    login_token = preferences.getString("login_token", "");
                    default_adrz = preferences.getString("default_adrz", "");
                    setApplicationlanguage = preferences.getString("setApplicationlanguage", "");
                    Log.v("setApplicationlanguage", setApplicationlanguage);

                    Log.v("login_token", login_token);

                    recyclerView_cart = (RecyclerView) findViewById(R.id.recyclerView_cart);
                    btn_checkout = (Button) findViewById(R.id.btn_checkout);
                    linearCartMain = (LinearLayout) findViewById(R.id.linearCartMain);
                    linearCartNotVisible = (LinearLayout) findViewById(R.id.linearCartNotVisible);
                    NoInternet = findViewById(R.id.lean_internet);
                    linearCartMain.setVisibility(View.VISIBLE);
                    linearCartNotVisible.setVisibility(View.GONE);
                    IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
                    receiver = new NetworkChangeReceiver();
                    registerReceiver(receiver, filter);
                    btn_checkout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                if (validateMinimumAmountDbl >=minimum_cart_amount_dbl) {
                                    login_token = preferences.getString("login_token", null);
                                    Intent in = new Intent(CartActivity.this, MapsActivity.class);
                                    startActivity(in);
                                } else {
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(
                                            CartActivity.this);


                                    builder.setMessage(getString(R.string.Minimum_cart_amount_of)+" "+minimum_cart_amount_dbl);
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
                            } catch (
                                    Exception ex) {
                                Log.v("btn_checkout", ex.getMessage().toString());

                            }

                        }
                    });


                    cart_item_ModelList = new ArrayList<>();
                    //cart_item_adapter = new Cart_Item_Adapter(getActivity(), cart_item_ModelList);

                    enableSwipe_Cart();


                    loadCartItems_api();


                } else {
//                    final AlertDialog.Builder builder = new AlertDialog.Builder(
//                            this);
//
//                    builder.setTitle("Log In");
//                    builder.setMessage("To continue please log in");
//                    builder.setCancelable(false);
//                    builder.setNegativeButton("NO",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog,
//                                                    int which) {
//                                    dialog.dismiss();
//                                }
//                            });
//                    builder.setPositiveButton("YES",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog,
//                                                    int which) {
//                                    Intent in = new Intent(CartActivity.this, LogInActivity.class);
//                                    startActivity(in);
//                                }
//                            });
//
//
//                    builder.show();
                    Intent in = new Intent(CartActivity.this, LogInActivity.class);
                    startActivity(in);
                }

// Swipe Button fore  Delete
//                swipeController = new SwipeController(new SwipeControllerActions() {
//                    @Override
//                    public void onRightClicked(int position) {
//                        try {
//
//                            cartId = cart_item_ModelList.get(position).getProduct_ID();
//                            DeleteCart(cartId);
//                            cart_item_adapter.cart_item_ModelList.remove(position);
//                            cart_item_adapter.notifyItemRemoved(position);
//                            cart_item_adapter.notifyItemRangeChanged(position, cart_item_adapter.getItemCount());
//
//                        } catch (Exception ex) {
//                            cartId = cart_item_ModelList.get(0).getProduct_ID();
//                            String j = ex.getMessage().toString();
//                            Log.v("j", j);
//                            DeleteCart(cartId);
//                        }
//                    }
//                },
//
//                        this);
//
//                ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
//                itemTouchhelper.attachToRecyclerView(recyclerView_cart);
//
//                recyclerView_cart.addItemDecoration(new RecyclerView.ItemDecoration() {
//                    @Override
//                    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
//                        swipeController.onDraw(c);
//                    }
//                });
//            } else {
//                setContentView(R.layout.msg_no_internet);
//            }
//
//
//        } catch (Exception ex) {
//            Log.v("Cart_main", ex.getMessage().toString());
//
//        }


    }

    private void enableSwipe_Cart() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
//
//                if (direction == ItemTouchHelper.LEFT) {
//                    final Cart_Item_Model deletedModel = cart_item_ModelList.get(position);
//                    final int deletedPosition = position;
//                    cart_item_adapter.removeItem(position);
//
//                } else {
                final Cart_Item_Model deletedModel = cart_item_ModelList.get(position);
                final int deletedPosition = position;
                // cart_item_adapter.removeItem(position);

                cartId = cart_item_ModelList.get(position).getProduct_ID();
//                            DeleteCart(cartId);
//                            cart_item_adapter.cart_item_ModelList.remove(position);
//                            cart_item_adapter.notifyItemRemoved(position);
//                            cart_item_adapter.notifyItemRangeChanged(position, cart_item_adapter.getItemCount());

                cart_item_adapter.removeItem(position);
                if (cartId != null && !cartId.isEmpty() && !cartId.equals("null")) {
                    DeleteCart(cartId);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX > 0) {
//                        p.setColor(Color.parseColor("#388E3C"));
//                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
//                        c.drawRect(background, p);
//                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.cart);
//                        RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
//                        c.drawBitmap(icon, null, icon_dest, p);
                    } else {
                        p.setColor(Color.parseColor("#fa315b"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    }

                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView_cart);
    }

    public void set_Cart() {
        try {
            cart_item_adapter = new Cart_Item_Adapter(this, cart_item_ModelList);
            recyclerView_cart.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(this);
            recyclerView_cart.setLayoutManager(mLayoutManager);
            recyclerView_cart.setAdapter(cart_item_adapter);


        } catch (Exception ex) {
            String msg = ex.getMessage().toString();
            Log.v("msg_swipe", msg);
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
                            if (response != null && !response.isEmpty() && !response.equals("null")) {
                                //getting the whole json object from the response
                                JSONObject obj = new JSONObject(response);

                                String status = obj.getString("status");
                                Log.v("status", status);
                                cart_item_ModelList.clear();

                                if (status.equals("200")) {

                                    JSONArray jsonArray_data = obj.getJSONArray("cartItems");

                                    if (jsonArray_data.length() > 0) {
                                        linearCartMain.setVisibility(View.VISIBLE);
                                        linearCartNotVisible.setVisibility(View.GONE);


                                        cartCount = obj.getString("cartCount");
                                        minimum_cart_amount_dbl = Double.parseDouble(obj.getString("minimum_cart_amount"));
                                        cartCountAll = cartCount;

                                        if (cartCount != null && !cartCount.isEmpty() && !cartCount.equals("null")) {
                                            Log.v("cartCount_SubMenuFrag", cartCount);
                                            // txtCartCount.setText(cartCount);
                                            txtCartCount.setVisibility(View.INVISIBLE);
                                            SharedPreferences sharedpreferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                                            final SharedPreferences.Editor editor = sharedpreferences.edit();
                                            editor.putString("cartCount", cartCount);
                                            editor.commit();
                                        } else {

                                            txtCartCount.setVisibility(View.INVISIBLE);
                                        }
                                        String subtotal = obj.getString("subtotal");
                                        String total = obj.getString("total");
                                        validateMinimumAmountDbl = Double.parseDouble(total);

                                        for (int i = 0; i < jsonArray_data.length(); i++) {
                                            JSONObject jsondata = jsonArray_data.getJSONObject(i);

                                            String id = jsondata.getString("id");
                                            String qty = jsondata.getString("qty");
                                            // JSONObject option=jsondata.getJSONObject("option");
                                            String price = jsondata.getString("price");
                                            String optionStr = jsondata.getString("option");
                                            if (optionStr != null && !optionStr.isEmpty() && !optionStr.equals("null")) {
                                                JSONObject option = new JSONObject(jsondata.getString("option"));
                                                if (option.length() > 0) {
                                                    Iterator keys = option.keys();
                                                    String params = String.valueOf(keys.next());
                                                    params_value = option.getString(params);
                                                } else {
                                                    params_value = "";
                                                }
                                            }


//
                                            // String price = jsondata.getString("price");
                                            // String sub_total = jsondata.getString("sub_total");

                                            JSONObject jsonObject_Product = jsondata.getJSONObject("product");
                                            String product_Name = jsonObject_Product.getString("name");

                                            String cover = jsonObject_Product.getString("cover");
                                           // String price = jsonObject_Product.getString("price");
                                            String h = cover;

                                            cart_item_ModelList.add(new Cart_Item_Model
                                                    (id, product_Name, cover,
                                                            qty, price, subtotal, params_value, total, cartCount));
                                        }

                                        txt_net_totalCart.setText(getString(R.string._QAR) + "  " + total);
                                        txt_SubTotal.setText(getString(R.string._QAR) + "  " + subtotal);
                                        netTotal.setText(total);
                                        // do smth


                                        set_Cart();
                                    } else {

                                        linearCartMain.setVisibility(View.GONE);
                                        linearCartNotVisible.setVisibility(View.VISIBLE);
                                        NoItem.setText(R.string.Nothing_to_Show);

                                        SharedPreferences sharedpreferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                                        final SharedPreferences.Editor editor = sharedpreferences.edit();
                                        editor.putString("cartCount", cartCount);
                                        editor.commit();
                                        txtCartCount.setVisibility(View.INVISIBLE);
                                        Log.v("cartCount_Cart", cartCount);


                                    }


                                    //   set_Main_Items();
                                    //  item_main_adapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Server error, please contact the customer care service...", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Log.v("responseStrLoad", response);
                            }

                        } catch (Exception ex) {

                            dialog.dismiss();
                            if (ex != null && !ex.equals("null")) {
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

    private void cart_item_adapter() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT) {
                    final Cart_Item_Model deletedModel = cart_item_ModelList.get(position);
                    final int deletedPosition = position;
                    cart_item_adapter.removeItem(position);

                    cartId = cart_item_ModelList.get(position).getProduct_ID();
                    DeleteCart(cartId);


                } else {
                    final Cart_Item_Model deletedModel = cart_item_ModelList.get(position);
                    final int deletedPosition = position;
                    cart_item_adapter.removeItem(position);
                    if (cartId != null && !cartId.isEmpty() && !cartId.equals("null")) {
                        DeleteCart(cartId);
                    }

                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX > 0) {
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.cart);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    } else {
                        p.setColor(Color.parseColor("#fa315b"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.cart);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    }

                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView_cart);
    }

    public void DeleteCart(String cartId) {
        //creating a string request to send request to the url

        String URL_LIST_CART_Delete = utilities.GetUrl() + "cart/delete/" + cartId;


        Log.v("URL_LIST_CART_Delete", URL_LIST_CART_Delete);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_LIST_CART_Delete,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null && !response.isEmpty() && !response.equals("null")) {
                            Log.v("response", response);
                            try {
                                //getting the whole json object from the response
                                JSONObject obj = new JSONObject(response);

                                String status = obj.getString("status");
                                Log.v("status", status);


                                if (status.equals("200")) {
                                    String msg = obj.getString("msg");
//                                Log.v("cartCount",""+cart_item_ModelList.size());
//                                txtCartCount.setVisibility(View.VISIBLE);
                                    txtCartCount.setText("" + cart_item_ModelList.size());
//                                cart_item_adapter.notifyDataSetChanged();
                                    loadCartItems_api();

                                } else {
                                    Toast.makeText(getApplicationContext(), "Server error, please contact the customer care service...", Toast.LENGTH_LONG).show();
                                }


                            } catch (Exception ex) {


                                if (ex != null && !ex.equals("null")) {
                                    Log.v("errorgetMessage()", "errorCatch");
                                }
                            }
                        } else {
                            Log.v("responseStrDelete", response);
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


    public void Update_Cart_Count_api(String cartID, String quantity) {
        //creating a string request to send request to the url
        JSON_URL_UPDATE_CART_COUNT = utilities.GetUrl() + "cart/update/" + cartID + "?quantity=" + quantity;

        Log.v("UPDATE_CART_COUNT", JSON_URL_UPDATE_CART_COUNT);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL_UPDATE_CART_COUNT,
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
                                    String msg = obj.getString("msg");


                                    Log.v("msg", msg);
                                    loadCartItems_api();
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
    public void onDestroy() {

        super.onDestroy();

        Hawk.put("pages", "home");

    }


}
