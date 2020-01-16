package com.the.marketapp.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.the.marketapp.Activity.LogInActivity;
import com.the.marketapp.Activity.MainActivity;
import com.the.marketapp.Adapter.ShoppingListAdapter;
import com.the.marketapp.Model.ShoppingListModel;
import com.the.marketapp.Other.SwipeControllerActions;
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
import java.util.Map;

import cc.cloudist.acplibrary.ACProgressFlower;


public class Shop_List_Fragment extends Fragment {

    int logedIn;
    View rootView;
    String listId;
    ImageView imgAdd;
    Utilities utilities;
    String login_token;
    String shoppingListName;
    EditText edtAddShopList;
    LinearLayout linearImgAdd;
    SharedPreferences preferences;
    String setApplicationlanguage;
    RecyclerView recyclerviewShoppingList;
    SwipeController swipeController = null;
    private ShoppingListAdapter shoppingListAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    ArrayList<ShoppingListModel> shoppingListModel = new ArrayList<ShoppingListModel>();
    String URL_Get_Shopping_List, URL_ADD_Shopping_List, URL_ADD_TO_CART;

MainActivity mainActivity;

    public Shop_List_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Hawk.init(getActivity())
                .setEncryptionMethod(HawkBuilder.EncryptionMethod.MEDIUM)
                .setStorage(HawkBuilder.newSqliteStorage(getActivity()))
                .setLogLevel(LogLevel.FULL)
                .build();
        Hawk.put("pages", "shoplist");

//        try {
//            if (utilities.isOnline(getActivity())) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(R.layout.actionbar_common);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(getLayoutInflater().inflate(R.layout.actionbar_common, null),
                        new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));

                LinearLayout linearMenuBlack = (LinearLayout) ((AppCompatActivity) getActivity()).findViewById(R.id.linearMenuBlack);
                TextView txt_actionbar_Title = (TextView) ((AppCompatActivity) getActivity()).findViewById(R.id.txt_actionbar_Title);
                txt_actionbar_Title.setText(R.string.SHOPPING_LIST);
                linearMenuBlack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // If the navigation drawer is not open then open it, if its already open then close it.
                        if (!mainActivity.drawer.isDrawerOpen(GravityCompat.START))
                            mainActivity.drawer.openDrawer(Gravity.START);
                        else mainActivity.drawer.closeDrawer(Gravity.END);
                    }
                });



                // Inflate the layout for this fragment

                rootView = inflater.inflate(R.layout.fragment_shop_, container, false);
                recyclerviewShoppingList = (RecyclerView) rootView.findViewById(R.id.recyclerviewShoppingList);
                preferences = getContext().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                setApplicationlanguage = preferences.getString("setApplicationlanguage", "");
                imgAdd          = rootView.findViewById(R.id.imgAdd);
                linearImgAdd    = rootView.findViewById(R.id.linearImgAdd);
                edtAddShopList  = rootView.findViewById(R.id.edtAddShopList);
                login_token     = preferences.getString("login_token", "");
//                Log.v("login_token_onCreate", login_token);

                logedIn = preferences.getInt("logedIn", 0);
//                Log.v("logedIn", "" + logedIn);
                if (logedIn == 1) {
                    try {
                        linearImgAdd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                shoppingListName = edtAddShopList.getText().toString();
                                if (shoppingListName != null && !shoppingListName.isEmpty() && !shoppingListName.equals("null")) {
                                    addShoppingListApi(shoppingListName);
                                    Log.v("Ok", "OK");
                                } else {
                                    edtAddShopList.setError(getString(R.string.Please_enter_a_valid_name));
                                }


                            }
                        });
                    } catch (Exception ex) {
//                        Log.v("logedIn", ex.getMessage().toString());
                    }
                    getShoppingListApi();

                    swipeController = new SwipeController(new SwipeControllerActions() {
                        @Override
                        public void onRightClicked(int position) {
                            try {

                                listId = shoppingListModel.get(position).getIdShoppinList();
                                deleteShoppingListApi(listId);
                                shoppingListAdapter.shoppingListModel.remove(position);
                                shoppingListAdapter.notifyItemRemoved(position);
                                shoppingListAdapter.notifyItemRangeChanged(position, shoppingListAdapter.getItemCount());

                            } catch (Exception ex) {
                                listId = shoppingListModel.get(0).getIdShoppinList();
                                String j = ex.getMessage().toString();
                                Log.v("j", j);
                                deleteShoppingListApi(listId);
                            }
                        }
                    },

                            getActivity());

                    ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
                    itemTouchhelper.attachToRecyclerView(recyclerviewShoppingList);

                    recyclerviewShoppingList.addItemDecoration(new RecyclerView.ItemDecoration() {
                        @Override
                        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                            swipeController.onDraw(c);
                        }
                    });
                } else {
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
                }
//            } else {
//                rootView = inflater.inflate(R.layout.msg_no_internet, container, false);
//            }


//        } catch (Exception ex) {
//            Log.v("ShopList_main", ex.getMessage().toString());
//
//        }
        return rootView;
    }

    public void setShoppingList() {
        try {
            if (shoppingListModel.size() > 0) {
                shoppingListAdapter = new ShoppingListAdapter(getActivity(), shoppingListModel);
                Log.v("shoppingListModel", "" + shoppingListModel.size());
                recyclerviewShoppingList.setHasFixedSize(true);
                mLayoutManager = new LinearLayoutManager(getActivity());
                recyclerviewShoppingList.setLayoutManager(mLayoutManager);
                recyclerviewShoppingList.setAdapter(shoppingListAdapter);
                recyclerviewShoppingList.setNestedScrollingEnabled(false);
            }

        } catch (Exception ex) {
            Log.v("setShoppingList", ex.getMessage().toString());
        }
    }

    private void getShoppingListApi() {

        final ACProgressFlower dialog = new ACProgressFlower.Builder(getActivity())
                .build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        URL_Get_Shopping_List = utilities.GetUrl() + "accounts/shopping/list/get";
        Log.v("URL_Get_Shopping_List", URL_Get_Shopping_List);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_Get_Shopping_List,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("response", response);
                        dialog.dismiss();

                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);
                            Log.v("responseJSON", response);
                            String status = obj.getString("status");

                            if (status.equals("200")) {

                                shoppingListModel.clear();
                                JSONArray jsonArrayList = obj.getJSONArray("list");

                                {
                                    for (int i = 0; i < jsonArrayList.length(); i++) {
                                        JSONObject jsondata = jsonArrayList.getJSONObject(i);
                                        String id = jsondata.getString("id");
                                        String customer_id = jsondata.getString("customer_id");
                                        String name = jsondata.getString("name");


                                        shoppingListModel.add(new ShoppingListModel(id, customer_id, name));


                                    }
                                    setShoppingList();
                                }


                                //  item_main_adapter.notifyDataSetChanged();
                            } else {
                                dialog.dismiss();
                                Toast.makeText(getContext(), getString(R.string.Server_Error), Toast.LENGTH_LONG).show();
                            }


                        } catch (JSONException e) {
                            System.out.println("Error " + e.getMessage());
                            e.printStackTrace();
                            dialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        dialog.dismiss();
                    }
                }) {


            /** Passing some request headers* */
            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                if (login_token != null && !login_token.isEmpty() && !login_token.equals("null")) {
                    headers.put("userToken", login_token);
                    headers.put("lang", setApplicationlanguage);
                    Log.v("login_token_Adrz", login_token);
                } else {
                    Intent in = new Intent(getActivity(), LogInActivity.class);
                    getActivity().startActivity(in);
                }

                return headers;
            }

        };


        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //adding the string request to request queue
        requestQueue.add(stringRequest);


    }

    private void addShoppingListApi(final String shoppingListName) {

        //creating a string request to send request to the url
        final ACProgressFlower dialog = new ACProgressFlower.Builder(getActivity())
                .build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();


        Log.v("login_token", login_token);
        URL_ADD_Shopping_List = utilities.GetUrl() + "accounts/shopping/list/add";
        Log.v("URL_ADD_Shopping_List", URL_ADD_Shopping_List);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADD_Shopping_List,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("response_add", response);
                        dialog.dismiss();
                        try {

                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            Log.v("response_ShoppingList", response);


                            String status = obj.getString("status");
                            if (status.equals("200")) {
                                String success = obj.getString("success");

                                if (success.equals("1")) {

                                    String msg = obj.getString("msg");
                                    String list = obj.getString("list");


                                    getShoppingListApi();

                                    Log.v("msgs", msg);

                                } else {


                                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                Toast.makeText(getContext(), "Server error", Toast.LENGTH_SHORT).show();


                            }

                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        dialog.dismiss();
                    }
                }) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", shoppingListName);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                if (login_token != null && !login_token.isEmpty() && !login_token.equals("null")) {
                    params.put("userToken", login_token);
                    params.put("lang", setApplicationlanguage);
                    Log.v("login_token_Adrz", login_token);
                } else {
                    Intent in = new Intent(getActivity(), LogInActivity.class);
                    getActivity().startActivity(in);
                }
                return params;
            }
        };
        queue.add(stringRequest);

    }


    private void deleteShoppingListApi(final String shoppingListName) {

        //creating a string request to send request to the url
        final ACProgressFlower dialog = new ACProgressFlower.Builder(getActivity())
                .build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Log.v("login_token", login_token);
     String   URL_DELETE_Shopping_List = utilities.GetUrl() + "accounts/shopping/list/delete";
        Log.v("DELETE_Shopping_List", URL_DELETE_Shopping_List);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DELETE_Shopping_List,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("response_add", response);
                        dialog.dismiss();
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            Log.v("response_ShoppingList", response);

                            String status = obj.getString("status");
                            if (status.equals("200")) {
                                String success = obj.getString("success");

                                if (success.equals("1")) {

                                    String msg = obj.getString("msg");
                                    String list = obj.getString("list");


                                    //getShoppingListApi();
                                    shoppingListAdapter.notifyDataSetChanged();

                                    Log.v("msgs", msg);

                                } else {


                                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                Toast.makeText(getContext(), "Server error", Toast.LENGTH_SHORT).show();


                            }

                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        dialog.dismiss();
                    }
                }) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("list_id", shoppingListName);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                if (login_token != null && !login_token.isEmpty() && !login_token.equals("null")) {
                    params.put("userToken", login_token);
                    params.put("lang", setApplicationlanguage);
                    Log.v("login_token_Adrz", login_token);
                } else {
                    Intent in = new Intent(getActivity(), LogInActivity.class);
                    getActivity().startActivity(in);
                }
                return params;
            }
        };
        queue.add(stringRequest);

    }
    public void onDestroy(){
        Hawk.put("pages", "home");
        super.onDestroy();

    }



}
