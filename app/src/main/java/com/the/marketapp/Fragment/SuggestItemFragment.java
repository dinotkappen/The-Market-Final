package com.the.marketapp.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.the.marketapp.Activity.MainActivity;
import com.the.marketapp.Other.Utilities;
import com.the.marketapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cc.cloudist.acplibrary.ACProgressFlower;


public class SuggestItemFragment extends Fragment {
    View rootView;

    EditText edt_Title, edt_Desc;
    private static String JSON_URL_SUGGESTON;
    Utilities utilities;
    String login_token;
    String title, desc;
    SharedPreferences preferences;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    Button btn_Submit;
    String setApplicationlanguage;
    MainActivity mainActivity;
    public SuggestItemFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            if (utilities.isOnline(getActivity())) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_suggest_item, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(R.layout.actionbar_common);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(getLayoutInflater().inflate(R.layout.actionbar_common, null),
                new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));

        TextView txt_actionbar_Title = (TextView) ((AppCompatActivity) getActivity()).findViewById(R.id.txt_actionbar_Title);
        txt_actionbar_Title.setText(R.string.SUGGEST_AN_ITEM);
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


        JSON_URL_SUGGESTON = utilities.GetUrl() + "suggestion?";
        preferences = getContext().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        setApplicationlanguage = preferences.getString("setApplicationlanguage", "");
        login_token = preferences.getString("login_token", null);
        edt_Title = (EditText) rootView.findViewById(R.id.edt_Title);
        edt_Desc = (EditText) rootView.findViewById(R.id.edt_Desc);

        btn_Submit=(Button)rootView.findViewById(R.id.btn_Submit);
        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                submitForm();

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
    private void submitForm() {
        if (!validateTitle()) {
            return;
        }
        if (!validateMessage()) {
            return;
        }


        Suggestion_Api();
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    private boolean validateMessage() {
        if (edt_Desc.getText().toString().trim().isEmpty()) {
            edt_Desc.setError(getString(R.string.Please_enter_description));
            requestFocus(edt_Desc);
            return false;
        }

        return true;
    }
    private boolean validateTitle() {
        if (edt_Title.getText().toString().trim().isEmpty()) {
            edt_Title.setError(getString(R.string.Please_enter_a_title));
            requestFocus(edt_Title);
            return false;
        }

        return true;
    }
    private void Suggestion_Api() {
        title = edt_Title.getText().toString();
        desc = edt_Desc.getText().toString();
        final ACProgressFlower dialog = new ACProgressFlower.Builder(getActivity())
                .build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        JSON_URL_SUGGESTON = JSON_URL_SUGGESTON + "title="+title+"&comment="+desc;
        Log.v("JSON_URL_SUGGESTON",JSON_URL_SUGGESTON);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL_SUGGESTON,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("response",response);
                        dialog.dismiss();
                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            String status = obj.getString("status");
                            String success = obj.getString("success");
                            String msg = obj.getString("msg");
                            if (status.equals("200")) {

                                if (success.equals("1")) {
                                    Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
                                    Log.v("msg",msg);
                                }


                                //  item_main_adapter.notifyDataSetChanged();
                            } else {
                                dialog.dismiss();
                                Toast.makeText(getContext(), "Server error, please contact the customer care service...", Toast.LENGTH_LONG).show();
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

                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                        String msg = error.getMessage().toString();
                        String n = msg;
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
                Log.v("login_token2", login_token);
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
