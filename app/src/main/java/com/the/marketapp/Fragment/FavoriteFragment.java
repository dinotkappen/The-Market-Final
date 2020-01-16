package com.the.marketapp.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import com.the.marketapp.Activity.LogInActivity;
import com.the.marketapp.Activity.MainActivity;
import com.the.marketapp.Adapter.Favorite_Item_Adapter;
import com.the.marketapp.Model.Favourite_Item_Model;
import com.the.marketapp.Other.Utilities;
import com.the.marketapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.the.marketapp.Adapter.Item_Main_Adapter.MY_PREFS_NAME;


public class FavoriteFragment extends Fragment {
    View rootView;
    private Paint p = new Paint();
    private RecyclerView recyclerView_favorite;

    private RecyclerView.LayoutManager mLayoutManager;
    private Favorite_Item_Adapter favorite_item_adapter;
    ArrayList<Favourite_Item_Model> favorite_item_ModelList = new ArrayList<Favourite_Item_Model>();

    String GET_WISH_LIST;
    Utilities utilities;
    SharedPreferences preferences;
    String setApplicationlanguage, login_token;
    TextView txtCartCount;
    LinearLayout linearMainLayoutFavourite, linearOrderNotVisibleFavourite;
    String product_id;
    MainActivity mainActivity;
    public FavoriteFragment() {
        // Required empty public constructor
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        try {
            if (utilities.isOnline(getActivity())) {
                rootView = inflater.inflate(R.layout.fragment_favorite, container, false);

                linearMainLayoutFavourite = (LinearLayout) rootView.findViewById(R.id.linearMainLayoutFavourite);
                linearOrderNotVisibleFavourite = (LinearLayout) rootView.findViewById(R.id.linearOrderNotVisibleFavourite);
                linearMainLayoutFavourite.setVisibility(View.VISIBLE);
                linearOrderNotVisibleFavourite.setVisibility(View.GONE);
                recyclerView_favorite = (RecyclerView) rootView.findViewById(R.id.recyclerView_favorite);

                preferences = getActivity().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);

                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(R.layout.actionbar_common_bottom);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(getLayoutInflater().inflate(R.layout.actionbar_common_bottom, null),
                        new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));
                LinearLayout linearMenuWhite = (LinearLayout) ((AppCompatActivity) getActivity()).findViewById(R.id.linearMenuWhite);
                linearMenuWhite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // If the navigation drawer is not open then open it, if its already open then close it.
                        if (!mainActivity.drawer.isDrawerOpen(GravityCompat.START))
                            mainActivity.drawer.openDrawer(Gravity.START);
                        else mainActivity.drawer.closeDrawer(Gravity.END);
                    }
                });
                TextView txt_actionbar_Title = (TextView) ((AppCompatActivity) getActivity()).findViewById(R.id.txt_actionbar_Title);
                txt_actionbar_Title.setText(getString(R.string.FAVORITES));

                setApplicationlanguage = preferences.getString("setApplicationlanguage", "");
                login_token = preferences.getString("login_token", "");
                Log.v("login_token_Favourit", login_token);
                int logedIn = preferences.getInt("logedIn", 0);
                mainActivity.navigation.setVisibility(View.VISIBLE);
                if (logedIn == 0) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(
                            getContext());

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
                                    Intent in = new Intent(getActivity(), LogInActivity.class);
                                    getActivity().startActivity(in);
                                }
                            });


                    builder.show();
                } else {
                    GET_WishList_api();
                    //loadCartItems_api_Count();
                    enableSwipeWishList();
                    txtCartCount.setVisibility(View.VISIBLE);
                }


                // prepareAlbums();
                // enableSwipe_Favourite();
            } else {
                rootView = inflater.inflate(R.layout.msg_no_internet, container, false);
            }
        } catch (Exception ex) {
            String msg = ex.getMessage().toString();
            Log.v("msg_Main", msg);
        }
        return rootView;
    }

    private void enableSwipeWishList() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) {

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
                final Favourite_Item_Model deletedModel = favorite_item_ModelList.get(position);
                final int deletedPosition = position;
                // cart_item_adapter.removeItem(position);

                product_id = favorite_item_ModelList.get(position).getId();
//                            DeleteCart(cartId);
//                            cart_item_adapter.cart_item_ModelList.remove(position);
//                            cart_item_adapter.notifyItemRemoved(position);
//                            cart_item_adapter.notifyItemRangeChanged(position, cart_item_adapter.getItemCount());

                favorite_item_adapter.removeItem(position);
                if (product_id != null && !product_id.isEmpty() && !product_id.equals("null")) {
                    DeleteWishList_api(product_id);
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
        itemTouchHelper.attachToRecyclerView(recyclerView_favorite);
    }

//    private void enableSwipeWishList() {
//        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) {
//
//            @Override
//            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//                int position = viewHolder.getAdapterPosition();
////
////                if (direction == ItemTouchHelper.LEFT) {
////                    final Cart_Item_Model deletedModel = cart_item_ModelList.get(position);
////                    final int deletedPosition = position;
////                    cart_item_adapter.removeItem(position);
////
////                } else {
//
//                // cart_item_adapter.removeItem(position);
//
//                product_id = favorite_item_ModelList.get(position).getId();
////                            DeleteCart(cartId);
////                            cart_item_adapter.cart_item_ModelList.remove(position);
////                            cart_item_adapter.notifyItemRemoved(position);
////                            cart_item_adapter.notifyItemRangeChanged(position, cart_item_adapter.getItemCount());
//
//                favorite_item_adapter.removeItem(position);
//                if (product_id != null && !product_id.isEmpty() && !product_id.equals("null")) {
//                    DeleteWishList_api(product_id);
//                }
//            }

//            @Override
//            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//
//                Bitmap icon;
//                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
//
//                    View itemView = viewHolder.itemView;
//                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
//                    float width = height / 3;
//
//                    if (dX > 0) {
////                        p.setColor(Color.parseColor("#388E3C"));
////                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
////                        c.drawRect(background, p);
////                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.cart);
////                        RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
////                        c.drawBitmap(icon, null, icon_dest, p);
//                    } else {
//                        p.setColor(Color.parseColor("#fa315b"));
//                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
//                        c.drawRect(background, p);
//                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete);
//                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
//                        c.drawBitmap(icon, null, icon_dest, p);
//                    }
//
//                }
//                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//            }
//        };
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
//        itemTouchHelper.attachToRecyclerView(recyclerView_favorite);
//    }
    private void set_FavouriteeItems() {
        try {
            favorite_item_adapter = new Favorite_Item_Adapter(getActivity(), favorite_item_ModelList);

            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
            recyclerView_favorite.setLayoutManager(mLayoutManager);
            //  recyclerView_subitems.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
            recyclerView_favorite.setItemAnimator(new DefaultItemAnimator());
            recyclerView_favorite.setAdapter(favorite_item_adapter);
        } catch (Exception ex) {
            String msg = ex.getMessage().toString();
            Log.v("set_FavouriteeItems", msg);
        }
    }

    public void GET_WishList_api() {

        GET_WISH_LIST = utilities.GetUrl() + "accounts/wishlist";
        Log.v("GET_WISH_LIST", GET_WISH_LIST);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest sr = new StringRequest(Request.Method.GET, GET_WISH_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("response1", response);
                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);
                            Log.v("response2", response);

                            String status = obj.getString("status");
                            Log.v("status", status);

                            favorite_item_ModelList.clear();
                            if (status.equals("200")) {
                                JSONArray jsonArray_wishlist = obj.getJSONArray("wishlist");
                                if (jsonArray_wishlist.length() > 0) {
                                    linearMainLayoutFavourite.setVisibility(View.VISIBLE);
                                    linearOrderNotVisibleFavourite.setVisibility(View.GONE);
                                    for (int i = 0; i < jsonArray_wishlist.length(); i++) {
                                        JSONObject jsondata = jsonArray_wishlist.getJSONObject(i);
                                        String id = jsondata.getString("id");
                                        String product_id = jsondata.getString("product_id");
                                        String cover = jsondata.getString("cover");
                                        Log.v("cover", cover);
                                        String weight = jsondata.getString("weight");
                                        favorite_item_ModelList.add(new Favourite_Item_Model(jsondata.getString("id"), jsondata.getString("name"),
                                                jsondata.getString("weight"), jsondata.getString("price"), jsondata.getString("cover")));
                                    }
                                    set_FavouriteeItems();
                                } else {
                                    linearMainLayoutFavourite.setVisibility(View.GONE);
                                    linearOrderNotVisibleFavourite.setVisibility(View.VISIBLE);
                                }

                            } else {
                                Toast.makeText(getActivity(), getString(R.string.Server_Error), Toast.LENGTH_LONG).show();
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
                Log.v("login_token", login_token);
                return params;
            }
        };
        queue.add(sr);

    }

//    public void loadCartItems_api_Count() {
//
//        //creating a string request to send request to the url
//        String JSON_URL_LIST_CART = utilities.GetUrl() + "cart";
//
//        Log.v("JSON_URL_CATEGORIES", JSON_URL_LIST_CART);
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL_LIST_CART,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//                        Log.v("response", response);
//                        try {
//                            //getting the whole json object from the response
//                            JSONObject obj = new JSONObject(response);
//
//                            String status = obj.getString("status");
//                            Log.v("status", status);
//
//                            if (status.equals("200")) {
//
//                                JSONArray jsonArray_data = obj.getJSONArray("cartItems");
//
//                                if (jsonArray_data.length() > 0) {
//
//                                    String cartCount = obj.getString("cartCount");
//                                    if (cartCount != null && !cartCount.isEmpty() && !cartCount.equals("null")) {
//                                        Log.v("cartCount_SubMenuFrag", cartCount);
//                                        txtCartCount.setText(cartCount);
//                                        txtCartCount.setVisibility(View.VISIBLE);
//                                        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
//                                        final SharedPreferences.Editor editor = sharedpreferences.edit();
//                                        editor.putString("cartCount", cartCount);
//                                        editor.commit();
//                                    } else {
//
//                                        txtCartCount.setVisibility(View.INVISIBLE);
//                                    }
//                                } else {
//                                    txtCartCount.setVisibility(View.INVISIBLE);
//                                }
//
//                                //   set_Main_Items();
//                                //  item_main_adapter.notifyDataSetChanged();
//                            } else {
//                            }
//
//
//                        } catch (JSONException e) {
//                            Log.v("volley", e.getMessage().toString());
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.v("volley", error.getMessage().toString());
////                        Toast.makeText(get, error.getMessage(), Toast.LENGTH_SHORT).show();
//
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
//                Log.v("login_token2", login_token);
//                headers.put("lang", setApplicationlanguage);
//                return headers;
//            }
//
//        };
//
//
//        //creating a request queue
//        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//
//        //adding the string request to request queue
//        requestQueue.add(stringRequest);
//
//
//    }

    public void DeleteWishList_api(String productID) {

       String URL_ADD_WISH_LIST = utilities.GetUrl() + "accounts/wishlist/add";
        Log.v("URL_ADD_WISH_LIST", URL_ADD_WISH_LIST);
         product_id =productID;
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
                                   // Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                                    favorite_item_adapter.notifyDataSetChanged();
                                    if(favorite_item_ModelList.size()==0)
                                    {
                                        linearMainLayoutFavourite.setVisibility(View.GONE);
                                        linearOrderNotVisibleFavourite.setVisibility(View.VISIBLE);
                                    }
                                }

                            } else {
                                Toast.makeText(getActivity(), getString(R.string.Server_Error), Toast.LENGTH_LONG).show();
                            }


                        } catch (JSONException e) {

                            if (e != null && !e.equals("null")) {

                                if (e != null && !e.equals("null")) {
                                    Log.v("errorgetMessage()", "errorCatch");
                                }
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
                params.put("action", "delete");
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

}
