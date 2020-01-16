package com.the.marketapp.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
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
import com.the.marketapp.Activity.SingleItemActivity;
import com.the.marketapp.Adapter.ShoppingListPopUpAdapter;
import com.the.marketapp.Adapter.Units_AdapterPOPUP;
import com.the.marketapp.Model.ShoppingListPOPUPModel;
import com.the.marketapp.Model.Unit_List_Model;
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

import static android.content.Context.MODE_PRIVATE;


public class ShoppingListPopUpFragment extends Fragment {

    FrameLayout clickOutSide;
    int logedIn;
    View rootView;
    String login_token;
    Utilities utilities;
    ImageView imgAddPOPUP;
    String shoppingListNamePOPUP;
    EditText edtAddShopListPOPUP;
    Button btnAddToShopListPOPUP;
    SharedPreferences preferences;
    LinearLayout linearImgAddPOPUP;
    RecyclerView recyclerviewShoppingListPOPUP;
    private RecyclerView.LayoutManager mLayoutManager;
    ShoppingListPopUpFragment shoppingListPopUpFragment;
    ArrayList<String> addToList_from = new ArrayList<>();
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private ShoppingListPopUpAdapter shoppingListAdapterPOPUP;
    String productId, setApplicationlanguage, productAttribute_ID;
    ArrayList<Unit_List_Model> data_Unit_Items = new ArrayList<Unit_List_Model>();
    ArrayList<ShoppingListPOPUPModel> shoppingListPOPUPModel = new ArrayList<ShoppingListPOPUPModel>();
    String URL_Get_Shopping_ListPOPUP, URL_ADD_Shopping_List, URL_ADD_GROUP_Shopping_List, URL_SINGLE_ITEM;
    SingleItemActivity singleItemActivity;
    public ShoppingListPopUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        try {
//            if (utilities.isOnline(getActivity())) {

                Hawk.init(getActivity())
                        .setEncryptionMethod(HawkBuilder.EncryptionMethod.MEDIUM)
                        .setStorage(HawkBuilder.newSqliteStorage(getActivity()))
                        .setLogLevel(LogLevel.FULL)
                        .build();
                Hawk.put("pages", "ShopListPopUp");

                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(R.layout.actionbar_layout);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(getLayoutInflater().inflate(R.layout.actionbar_layout, null),
                        new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));

                ImageView img_bar_icon          =  ( getActivity()).findViewById(R.id.img_bar_icon);
                ImageView img_cart_actionbar    =  ( getActivity()).findViewById(R.id.img_cart_actionbar);
                TextView txt_actionbar_Title    =  ( getActivity()).findViewById(R.id.txt_actionbar_Title);
                TextView txt_actionbar_cart     =  ( getActivity()).findViewById(R.id.txtCartCount);

                txt_actionbar_cart.setVisibility(View.GONE);
                txt_actionbar_Title.setText(getString(R.string.SHOPPING_LIST));
                img_bar_icon.setVisibility(View.INVISIBLE);
                img_cart_actionbar.setVisibility(View.INVISIBLE);
                // Inflate the layout for this fragment
                // Inflate the layout for this fragment
                rootView = inflater.inflate(R.layout.fragment_shopping_list_pop_up, container, false);
                recyclerviewShoppingListPOPUP = (RecyclerView) rootView.findViewById(R.id.recyclerviewShoppingListPOPUP);
                preferences = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                imgAddPOPUP =  rootView.findViewById(R.id.imgAddPOPUP);

                linearImgAddPOPUP       =  rootView.findViewById(R.id.linearImgAddPOPUP);
                edtAddShopListPOPUP     =  rootView.findViewById(R.id.edtAddShopListPOPUP);
                btnAddToShopListPOPUP   =  rootView.findViewById(R.id.btnAddToShopListPOPUP);
                clickOutSide            =  rootView.findViewById(R.id.len_frame_click);

                clickOutSide.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                login_token = preferences.getString("login_token", "");
                Log.v("login_token_onCreate", login_token);

                logedIn = preferences.getInt("logedIn", 0);
                setApplicationlanguage = preferences.getString("setApplicationlanguage", "");
                Log.v("logedIn", "" + logedIn);
                Log.v("setApplicationlanguage", "" + setApplicationlanguage);
                if (logedIn == 1) {
                   try {
                       productId = preferences.getString("productId", "0");
                       Log.v("productId_popup", productId);//0 is the default value.
                       loadSingleItem_api();
                       linearImgAddPOPUP.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               try {
                                   shoppingListNamePOPUP = edtAddShopListPOPUP.getText().toString();
                                   if (shoppingListNamePOPUP != null && !shoppingListNamePOPUP.isEmpty() && !shoppingListNamePOPUP.equals("null")) {
                                       addGroupShoppingListApi(shoppingListNamePOPUP);
                                       Log.v("Ok", "OK");
                                   } else {
                                       edtAddShopListPOPUP.setError(getString(R.string.Please_enter_a_valid_name));
                                   }

                               } catch (Exception ex) {
                                   Log.v("linearImgAddPOPUP", ex.getMessage().toString());

                               }


                           }
                       });
                       btnAddToShopListPOPUP.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               try {
                                   addToList_from = shoppingListAdapterPOPUP.listIDLIst;
                                   Log.v("addToList_from", "" + addToList_from.size());
                                   productId = preferences.getString("productId", "0");
                                   productAttribute_ID = preferences.getString("productAttribute_ID", "0");
                                   if (addToList_from.size() > 0) {
                                       if (productAttribute_ID != null && !productAttribute_ID.isEmpty() && !productAttribute_ID.equals("null")) {
                                           {
                                               if (productId != null && !productId.isEmpty() && !productId.equals("null")) {
                                                   {

                                                       addToShoppingListApi();

                                                       Log.v("productId", productId);
                                                       Log.v("productAttribute_ID_F", productAttribute_ID);
                                                   }
                                               }

                                           }
                                       }


                                   } else {
                                       Toast.makeText(getContext(), "Please select an item", Toast.LENGTH_SHORT).show();
                                   }
                               } catch (Exception ex) {
                                   Log.v("btnAddToShopListPOPUP", ex.getMessage().toString());

                               }
                           }
                       });
                       getShoppingListApi();
                   }
                   catch (Exception ex)
                    {
                        Log.v("LoggedIn",ex.getMessage().toString());
                    }
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
//
//
//        } catch (Exception ex) {
//            Log.v("ShopList_main", ex.getMessage().toString());
//
//        }
        return rootView;
    }

    public void setShoppingList() {
        try {
            if (shoppingListPOPUPModel.size() > 0) {
                shoppingListAdapterPOPUP = new ShoppingListPopUpAdapter(getActivity(), shoppingListPOPUPModel, shoppingListPopUpFragment);
                Log.v("shoppingListPOPUPModel", "" + shoppingListPOPUPModel.size());
                recyclerviewShoppingListPOPUP.setNestedScrollingEnabled(false);
                mLayoutManager = new LinearLayoutManager(getActivity());
                recyclerviewShoppingListPOPUP.setLayoutManager(mLayoutManager);
                recyclerviewShoppingListPOPUP.setAdapter(shoppingListAdapterPOPUP);
            }

        } catch (Exception ex) {
            Log.v("setShoppingList", ex.getMessage().toString());
        }
    }

    private void setUnits() {

        try {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            RecyclerView recyclerView_Units = rootView.findViewById(R.id.recyclerView_Units_POPUP);
            recyclerView_Units.setLayoutManager(layoutManager);
            Units_AdapterPOPUP adapter = new Units_AdapterPOPUP(getActivity(), data_Unit_Items);
            recyclerView_Units.setAdapter(adapter);
        } catch (Exception ex) {
            Log.v("setUnits", ex.getMessage().toString());

        }
    }

    private void getShoppingListApi() {

        final ACProgressFlower dialog = new ACProgressFlower.Builder(getActivity())
                .build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        URL_Get_Shopping_ListPOPUP = utilities.GetUrl() + "accounts/shopping/list/get";
        Log.v("URL_Get_Shopping_List", URL_Get_Shopping_ListPOPUP);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_Get_Shopping_ListPOPUP,
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

                                shoppingListPOPUPModel.clear();
                                JSONArray jsonArrayList = obj.getJSONArray("list");

                                {
                                    for (int i = 0; i < jsonArrayList.length(); i++) {
                                        JSONObject jsondata = jsonArrayList.getJSONObject(i);
                                        String id = jsondata.getString("id");
                                        String customer_id = jsondata.getString("customer_id");
                                        String name = jsondata.getString("name");


                                        shoppingListPOPUPModel.add(new ShoppingListPOPUPModel(id, customer_id, name, false));


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

    private void addGroupShoppingListApi(final String shoppingListName) {

        //creating a string request to send request to the url
        final ACProgressFlower dialog = new ACProgressFlower.Builder(getActivity())
                .build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();


        Log.v("login_token", login_token);
        URL_ADD_GROUP_Shopping_List = utilities.GetUrl() + "accounts/shopping/list/add";
        Log.v("ADD_GROUP_Shopping_List", URL_ADD_GROUP_Shopping_List);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADD_GROUP_Shopping_List,
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

    public void addToShoppingListApi() {

        //creating a string request to send request to the url
        final ACProgressFlower dialog = new ACProgressFlower.Builder(getActivity())
                .build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();


        Log.v("login_token", login_token);
        URL_ADD_Shopping_List = utilities.GetUrl() + "accounts/shopping/list-item/add";
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

                                    SharedPreferences.Editor editor = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();


                                  String finishMethod=  preferences.getString("fromSingleItem", "");
                                  if(finishMethod.equals("1"))
                                  {
                                      editor.putString("fromSingleItem", "0");
                                      editor.commit();
//                                      getActivity().finish();

                                      MainActivity.no_internet(true);
                                  }
                                  else
                                  {
//                                      Intent in=new Intent(getActivity(), MainActivity.class);
//                                      in.putExtra("fromShoppingListPopUpFragment","1");
//                                      startActivity(in);

//                                      Fragment fragment = new SubMenu_Fragment();
//                                      FragmentManager fragmentManager = ((AppCompatActivity) getContext()).getSupportFragmentManager();
//                                      FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                                      fragmentTransaction.replace(R.id.fragment_container, fragment);
//                                      fragmentTransaction.commit();
//                                      Log.v("msgss", msg);
//                                      getActivity().getFragmentManager().popBackStack();
//                                      Hawk.put("pages", "sub_menu");
                                      MainActivity.no_internet(true);

                                  }



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

//                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//                        error.printStackTrace();
//                        Fragment fragment = new SubMenu_Fragment();
//                        FragmentManager fragmentManager = ((AppCompatActivity) getContext()).getSupportFragmentManager();
//                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                        fragmentTransaction.replace(R.id.fragment_container, fragment);
//                        fragmentTransaction.commit();
//                        getActivity().getFragmentManager().popBackStack();
//                        Hawk.put("pages", "sub_menu");
                        MainActivity.no_internet(true);


                    }
                }) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("product_id", productId);
                Log.v("productId_POP", productId);
                Log.v("productAttribute_ID_POP", productAttribute_ID);
                params.put("product_attribute", productAttribute_ID);
                for (int i = 0; i < addToList_from.size(); i++) {
                    params.put("list_id[" + i + "]", addToList_from.get(i));
                }


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

    private void loadSingleItem_api() {
//creating a string request to send request to the url
//
        final ACProgressFlower dialog = new ACProgressFlower.Builder(getActivity())
                .build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        URL_SINGLE_ITEM = utilities.GetUrl() + "product/";
        URL_SINGLE_ITEM = URL_SINGLE_ITEM + productId;
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

//

                                //For seleciong attributes
                                JSONArray obj_attribute = obj.getJSONArray("productAttributes");
                                int du = obj_attribute.length();
                                for (int i = 0; i < obj_attribute.length(); i++) {
                                    String attribute_id = obj_attribute.getJSONObject(i).getString("id");
                                    String attribute_avail_qty = obj_attribute.getJSONObject(i).getString("quantity");
                                    String attribute_price = obj_attribute.getJSONObject(i).getString("price");
                                    JSONArray attributes_values_obj = obj_attribute.getJSONObject(i).getJSONArray("attributes_values");

                                    String value = attributes_values_obj.getJSONObject(0).getString("value");
                                    if (Integer.parseInt(attribute_avail_qty) > 0) {
                                        data_Unit_Items.add(new Unit_List_Model(attribute_id, attribute_avail_qty, attribute_price, value));
                                    }

                                }
                                setUnits();
                                //   set_Main_Items();
                                //  item_main_adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(getContext(), getString(R.string.Server_Error), Toast.LENGTH_LONG).show();
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

                        dialog.dismiss();
                    }
                }) {


/**
 * Passing some request headers*
 * Passing some request headers
 * <p>
 * Passing some request headers
 */
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

    @Override
    public void onResume() {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        super.onResume();
    }
}
