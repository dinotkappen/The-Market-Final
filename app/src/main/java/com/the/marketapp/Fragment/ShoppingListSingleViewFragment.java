package com.the.marketapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.the.marketapp.Adapter.SingleShopListAdapter;
import com.the.marketapp.Model.SingleShoppingListModel;
import com.the.marketapp.Other.Utilities;
import com.the.marketapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cc.cloudist.acplibrary.ACProgressFlower;

public class ShoppingListSingleViewFragment extends Fragment {

    View rootView;
    String listID;
    String URL_Get_Single_Shopping_List;
    String login_token, setApplicationlanguage, product_id, productAttribute_ID;
    ;
    Utilities utilities;
    SharedPreferences preferences;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    RecyclerView recyclerViewSingleShopList;
    private RecyclerView.LayoutManager mLayoutManager;

    private SingleShopListAdapter singleShopListAdapter;
    ArrayList<SingleShoppingListModel> singleShoppingListModels = new ArrayList<SingleShoppingListModel>();
    LinearLayout linearItemsNotAvail, linearLayoutMain;
    Button btnAddCartSigleShopList;
    String URL_ADD_TO_CART;

    public ShoppingListSingleViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            if (utilities.isOnline(getActivity())) {
                // Inflate the layout for this fragment
                rootView = inflater.inflate(R.layout.fragment_shopping_list_single_view, container, false);

                preferences = getContext().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                recyclerViewSingleShopList = (RecyclerView) rootView.findViewById(R.id.recyclerViewSingleShopList);
                linearItemsNotAvail = (LinearLayout) rootView.findViewById(R.id.linearItemsNotAvail);
                linearLayoutMain = (LinearLayout) rootView.findViewById(R.id.linearLayoutMain);
                linearItemsNotAvail.setVisibility(View.GONE);
                linearLayoutMain.setVisibility(View.VISIBLE);
                btnAddCartSigleShopList = (Button) rootView.findViewById(R.id.btnAddCartSigleShopList);

                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(R.layout.actionbar_layout_without_menu);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(getLayoutInflater().inflate(R.layout.actionbar_layout_without_menu, null),
                        new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));

                TextView txt_actionbar_Title = (TextView) ((AppCompatActivity) getActivity()).findViewById(R.id.txt_actionbar_Title);
                LinearLayout linear_backArrow=(LinearLayout) ((AppCompatActivity) getActivity()).findViewById(R.id.linear_backArrow);
                linear_backArrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getFragmentManager().getBackStackEntryCount() > 0) {
                            getFragmentManager().popBackStack();

                        }
                    }
                });
                LinearLayout linearCartLalyoutNomenu=(LinearLayout) ((AppCompatActivity) getActivity()).findViewById(R.id.linearCartLalyoutNomenu);
                linearCartLalyoutNomenu.setVisibility(View.INVISIBLE);



                listID = getArguments().getString("listID");
                login_token = preferences.getString("login_token", "");
                setApplicationlanguage = preferences.getString("setApplicationlanguage", "");
                Log.v("login_token_onCreate", login_token);
                Log.v("listID", listID);
                if (listID != null && !listID.isEmpty() && !listID.equals("null")) {
                    getSingleShoppingListApi();
                    Log.v("listID_Call", listID);
                }
                btnAddCartSigleShopList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listID != null && !listID.isEmpty() && !listID.equals("null")) {

                            Add_to_Cart_api();
                        }

                    }
                });
            } else {
                rootView = inflater.inflate(R.layout.msg_no_internet, container, false);
            }
        } catch (Exception ex) {
            String msg = ex.getMessage().toString();
            Log.v("msg_Main", msg);
        }

        return rootView;
    }

    public void setSingleShoppingList() {
        try {
            if (singleShoppingListModels.size() > 0) {
                singleShopListAdapter = new SingleShopListAdapter(getActivity(), singleShoppingListModels);
                Log.v("shoppingListModel", "" + singleShoppingListModels.size());
                recyclerViewSingleShopList.setHasFixedSize(true);
                recyclerViewSingleShopList.setNestedScrollingEnabled(false);
                mLayoutManager = new LinearLayoutManager(getActivity());
                recyclerViewSingleShopList.setLayoutManager(mLayoutManager);
                recyclerViewSingleShopList.setAdapter(singleShopListAdapter);
            }

        } catch (Exception ex) {
            Log.v("setShoppingList", ex.getMessage().toString());
        }
    }

    private void getSingleShoppingListApi() {

        final ACProgressFlower dialog = new ACProgressFlower.Builder(getActivity())
                .build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        URL_Get_Single_Shopping_List = utilities.GetUrl() + "accounts/shopping/list-items/" + listID;
        Log.v("Get_SingleShopping_List", URL_Get_Single_Shopping_List);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_Get_Single_Shopping_List,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("response_Get_Single", response);
                        dialog.dismiss();

                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);
                            Log.v("responseJSON_Get_Single", response);
                            String status = obj.getString("status");

                            if (status.equals("200")) {

                                singleShoppingListModels.clear();
                                JSONArray jsonArrayList = obj.getJSONArray("list-tems");

                                {
                                    if (jsonArrayList.length() > 0) {
                                        linearLayoutMain.setVisibility(View.VISIBLE);
                                        linearItemsNotAvail.setVisibility(View.GONE);
                                        for (int i = 0; i < jsonArrayList.length(); i++) {
                                            JSONObject jsondata = jsonArrayList.getJSONObject(i);
                                            String id = jsondata.getString("id");
                                            String product_id = jsondata.getString("product_id");
                                            JSONObject productJsonObj = jsondata.getJSONObject("product");
                                            String name = productJsonObj.getString("name");
                                            String cover = productJsonObj.getString("cover");
//                                            String cover = "http://market.whyte.company/storage/products/" + cover11;
                                            String quantity = productJsonObj.getString("quantity");
                                            String price = productJsonObj.getString("price");


                                            singleShoppingListModels.add(new SingleShoppingListModel(id, product_id, cover, name, quantity, price));

                                            setSingleShoppingList();
                                        }
                                    } else {
                                        linearLayoutMain.setVisibility(View.GONE);
                                        linearItemsNotAvail.setVisibility(View.VISIBLE);
                                    }

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


    private void Add_to_Cart_api() {
        login_token = preferences.getString("login_token", "");

        URL_ADD_TO_CART = utilities.GetUrl() + "cart/list/store/" + listID;

        Log.v("token_notification", login_token);
        Log.v("URL_ADD_TO_CART", URL_ADD_TO_CART);

        final ACProgressFlower dialog = new ACProgressFlower.Builder(getActivity())
                .build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_ADD_TO_CART,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("response", response);
                        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                        final SharedPreferences.Editor editor = sharedpreferences.edit();
                        dialog.dismiss();

                        try {

                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            Log.v("response", response);

                            String status = obj.getString("status");
                            if (status.equals("200")) {
                                String success = obj.getString("success");

                                if (success.equals("1")) {
                                    editor.putString("productAttribute_ID", "");
                                    Toast.makeText(getContext(), "Item successfully added to the cart...", Toast.LENGTH_SHORT).show();
                                } else {

                                    dialog.dismiss();
                                    Toast.makeText(getContext(), "Notifications are not available", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                Toast.makeText(getContext(), getString(R.string.Server_Error), Toast.LENGTH_SHORT).show();


                            }

                        } catch (JSONException e) {
                            dialog.dismiss();
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
                Log.v("userToken_AddCart", login_token);


                return headers;
            }

        };


        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //adding the string request to request queue
        requestQueue.add(stringRequest);


    }


}