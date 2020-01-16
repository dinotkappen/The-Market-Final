package com.the.marketapp.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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
import com.the.marketapp.Other.Utilities;
import com.the.marketapp.R;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cc.cloudist.acplibrary.ACProgressFlower;

public class Forgot_Pwd_Fragment extends Fragment {

    View rootView;
    EditText edt_Current_Pwd, edt_New_Pwd, edt_Cnfrm_Pwd;
    Button btn_Change_Pwd;
    String str_current_pwd, str_new_pwd;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences preferences;
    String login_token;
    String JSON_URL_UPDATE_PWD;
    Utilities utilities;
    String setApplicationlanguage;
    String selectLang;

    public Forgot_Pwd_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            if (utilities.isOnline(getActivity())) {
                Hawk.init(getActivity())
                        .setEncryptionMethod(HawkBuilder.EncryptionMethod.MEDIUM)
                        .setStorage(HawkBuilder.newSqliteStorage(getActivity()))
                        .setLogLevel(LogLevel.FULL)
                        .build();
                // Inflate the layout for this fragment
                rootView = inflater.inflate(R.layout.fragment_forgot__pwd_, container, false);
                edt_Current_Pwd = (EditText) rootView.findViewById(R.id.edt_Current_Pwd);
                edt_New_Pwd = (EditText) rootView.findViewById(R.id.edt_New_Pwd);
                edt_Cnfrm_Pwd = (EditText) rootView.findViewById(R.id.edt_Cnfrm_Pwd);
                btn_Change_Pwd = (Button) rootView.findViewById(R.id.btn_Change_Pwd);
                preferences = getContext().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                setApplicationlanguage = preferences.getString("setApplicationlanguage", "");
                int logedIn = preferences.getInt("logedIn", 0);
                Log.v("logedIn", "" + logedIn);
                selectLang = Hawk.get("selectLang", "");
                if(selectLang.equals("en"))
                {
                    edt_Current_Pwd.setGravity(Gravity.LEFT);
                    edt_New_Pwd.setGravity(Gravity.LEFT);
                    edt_Cnfrm_Pwd.setGravity(Gravity.LEFT);
                }
                else
                {
                    edt_Current_Pwd.setGravity(Gravity.RIGHT);
                    edt_New_Pwd.setGravity(Gravity.RIGHT);
                    edt_Cnfrm_Pwd.setGravity(Gravity.RIGHT);
                }


                if (logedIn == 1) {
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(R.layout.actionbar_layout_without_menu);
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(getLayoutInflater().inflate(R.layout.actionbar_layout_without_menu, null),
                            new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));
                    LinearLayout linear_backArrow = (LinearLayout) ((AppCompatActivity) getActivity()).findViewById(R.id.linear_backArrow);
                    linear_backArrow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (getFragmentManager().getBackStackEntryCount() > 0) {
                                getFragmentManager().popBackStack();

                            }
                        }
                    });
                    LinearLayout linearCartLalyoutNomenu = (LinearLayout) ((AppCompatActivity) getActivity()).findViewById(R.id.linearCartLalyoutNomenu);
                    linearCartLalyoutNomenu.setVisibility(View.INVISIBLE);
                    TextView txt_actionbar_TitleNomenu = (TextView) ((AppCompatActivity) getActivity()).findViewById(R.id.txt_actionbar_TitleNomenu);
                    txt_actionbar_TitleNomenu.setText(R.string.CHANGE_PASSWORD);


                    login_token = preferences.getString("login_token", null);
                    Log.v("login_token", login_token);

                    btn_Change_Pwd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            submitForm();
                            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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
                                    Intent in = new Intent(getContext(), LogInActivity.class);
                                    startActivity(in);
                                }
                            });


                    builder.show();
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

    private void submitForm() {

        if (!validateCurrentPassword()) {
            return;
        }

        if (!validateNewPassword()) {
            return;
        }
        if (!validateCnfrmPassword()) {
            return;
        }

        Update_Password_api();
    }

    private boolean validateCurrentPassword() {
        if (edt_Current_Pwd.getText().toString().trim().isEmpty()) {
            edt_Current_Pwd.setError("Please enter the current password");
            requestFocus(edt_Current_Pwd);
            return false;
        }

        return true;
    }

    private boolean validateNewPassword() {
        if (edt_New_Pwd.getText().toString().trim().isEmpty()) {
            edt_New_Pwd.setError("Please enter the new password");
            requestFocus(edt_New_Pwd);
            return false;
        }
        if (TextUtils.isEmpty(edt_New_Pwd.getText().toString()) || edt_New_Pwd.getText().toString().length() < 8) {
            edt_New_Pwd.setError(getString(R.string.Passwords_must_be_at_least_6_characters_long));
            requestFocus(edt_New_Pwd);
            return false;
        }

        return true;
    }

    private boolean validateCnfrmPassword() {
        if (edt_Cnfrm_Pwd.getText().toString().trim().isEmpty()) {
            edt_Cnfrm_Pwd.setError("Please enter the confirm password");
            requestFocus(edt_Cnfrm_Pwd);
            return false;
        } else {
            if (edt_New_Pwd.getText().toString().trim().toLowerCase().indexOf(edt_Cnfrm_Pwd.getText().toString().trim().toLowerCase()) != 0) {

                edt_Cnfrm_Pwd.setError(getString(R.string.Passwords_do_not_match));
                edt_Cnfrm_Pwd.requestFocus();
                return false;
            }
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void Update_Password_api() {
        //creating a string request to send request to the url
        str_current_pwd = edt_Current_Pwd.getText().toString();
        str_new_pwd = edt_New_Pwd.getText().toString();


        JSON_URL_UPDATE_PWD = utilities.GetUrl() + "accounts/update?type=password&current-password=" + str_current_pwd + "&password=" + str_new_pwd;

        Log.v("login_token1", login_token);
        Log.v("JSON_URL_UPDATE_PWD", JSON_URL_UPDATE_PWD);
        final ACProgressFlower dialog = new ACProgressFlower.Builder(getActivity())
                .build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL_UPDATE_PWD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("response", response);
                        dialog.dismiss();
                        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        try {

                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            Log.v("response", response);

                            String status = obj.getString("status");
                            if (status.equals("200")) {
                                String success = obj.getString("success");
                                String msg = obj.getString("msg");
                                if (success.equals("1")) {

                                    login_token = obj.getString("login_token");
                                    Log.v("new_token", login_token);
                                    editor.putString("login_token", login_token);
                                    editor.commit();
                                    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                                    Log.v("msg", msg);
                                    edt_Current_Pwd.setText("");
                                    edt_New_Pwd.setText("");
                                    edt_Cnfrm_Pwd.setText("");


                                } else {

                                    dialog.dismiss();
                                    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                                    edt_Current_Pwd.setText("");
                                    edt_New_Pwd.setText("");
                                    edt_Cnfrm_Pwd.setText("");

                                }
                            } else if (status.equals("401")) {
                                String msg = obj.getString("msg");
                                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                                edt_Current_Pwd.setText("");
                                edt_New_Pwd.setText("");
                                edt_Cnfrm_Pwd.setText("");


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
                Log.v("login_token2", login_token);
                return headers;
            }

        };


        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //adding the string request to request queue
        requestQueue.add(stringRequest);

    }
}