package com.the.marketapp.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.the.marketapp.Activity.MainActivity;
import com.the.marketapp.Adapter.FAQ_Adapter;
import com.the.marketapp.Model.FAQ_Model;
import com.the.marketapp.Other.Utilities;
import com.the.marketapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cc.cloudist.acplibrary.ACProgressFlower;


public class FAQ_Fragment extends Fragment {
    View rootView;
    String JSON_URL_FAQ;
    Utilities utilities;
    ArrayList<FAQ_Model> faq_Model = new ArrayList<FAQ_Model>();
    private FAQ_Adapter faq_adapter_;
    RecyclerView recycler_view_faq;
    String setApplicationlanguage;
MainActivity mainActivity;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences preferences;
    public FAQ_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        try {
            if (utilities.isOnline(getActivity())) {
                rootView = inflater.inflate(R.layout.fragment_faq_, container, false);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(R.layout.actionbar_common);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(getLayoutInflater().inflate(R.layout.actionbar_common, null),
                        new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));

                TextView txt_actionbar_Title = (TextView) ((AppCompatActivity) getActivity()).findViewById(R.id.txt_actionbar_Title);
                txt_actionbar_Title.setText(R.string.FAQ);
                LinearLayout linearMenuBlack = (LinearLayout) ((AppCompatActivity) getActivity()).findViewById(R.id.linearMenuBlack);
                linearMenuBlack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // If the navigation drawer is not open then open it, if its already open then close it.
                        if (!mainActivity.drawer.isDrawerOpen(GravityCompat.START))
                            mainActivity.drawer.openDrawer(Gravity.START);
                        else mainActivity.drawer.closeDrawer(Gravity.END);
                    }
                });
                recycler_view_faq = (RecyclerView) rootView.findViewById(R.id.recycler_view_faq);
                preferences = getContext().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                setApplicationlanguage = preferences.getString("setApplicationlanguage", "");
                FAQ_Api();
            } else {
                rootView = inflater.inflate(R.layout.msg_no_internet, container, false);
            }
        } catch (Exception ex) {
            String msg = ex.getMessage().toString();
            Log.v("msg_Main", msg);
        }
        return rootView;
    }

    public void setFAQ() {
        try {
            faq_adapter_ = new FAQ_Adapter(getActivity(), faq_Model);
            recycler_view_faq.setAdapter(faq_adapter_);

            faq_adapter_ = new FAQ_Adapter(getActivity(), faq_Model);

            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
            recycler_view_faq.setLayoutManager(mLayoutManager);
            recycler_view_faq.setItemAnimator(new DefaultItemAnimator());
            recycler_view_faq.setAdapter(faq_adapter_);
        } catch (Exception ex) {
            String msg = ex.getMessage().toString();
            Log.v("setFAQ", msg);
        }

    }

    private void FAQ_Api() {
        final ACProgressFlower dialog = new ACProgressFlower.Builder(getActivity())
                .build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        JSON_URL_FAQ = utilities.GetUrl() + "faqs";
        Log.v("JSON_URL_FAQ", JSON_URL_FAQ);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL_FAQ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("response", response);
                        dialog.dismiss();
                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            String status = obj.getString("status");

                            if (status.equals("200")) {
                                faq_Model.clear();
                                JSONArray jsonArray_faqa = obj.getJSONArray("faqa");
                                for (int i = 0; i < jsonArray_faqa.length(); i++) {
                                    JSONObject jsondata = jsonArray_faqa.getJSONObject(i);
                                    String question = jsondata.getString("question");
                                    String answer = jsondata.getString("answer");
                                    faq_Model.add(new FAQ_Model(jsondata.getString("question"), jsondata.getString("answer"), false));
                                }

                                setFAQ();

                            } else {
                                dialog.dismiss();
                                Toast.makeText(getContext(), "Server error, please contact the customer care service...", Toast.LENGTH_LONG).show();
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
                headers.put("lang", setApplicationlanguage);
                return headers;
            }

        };



        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //adding the string request to request queue
        requestQueue.add(stringRequest);

    }






}
