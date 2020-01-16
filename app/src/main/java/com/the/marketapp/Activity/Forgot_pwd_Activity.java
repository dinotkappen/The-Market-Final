package com.the.marketapp.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.the.marketapp.Other.Utilities;
import com.the.marketapp.Other.VolleySingleton;
import com.the.marketapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cc.cloudist.acplibrary.ACProgressFlower;

public class Forgot_pwd_Activity extends AppCompatActivity {

    //TextView txt_Register;
    EditText edt_Email_ForgotPwd;
    String URL_RESET_PWD;
    Utilities utilities;
    Button btn_reset_pwd;
    String setApplicationlanguage;
    SharedPreferences preferences;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    LinearLayout linearBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        try {
            if (utilities.isOnline(this)) {
                setContentView(R.layout.activity_forgot_pwd_);
                URL_RESET_PWD = utilities.GetUrl() + "forgetpassword";

                preferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                setApplicationlanguage = preferences.getString("setApplicationlanguage", "");
                Log.v("setApplicationlanguage", setApplicationlanguage);
              //  txt_Register = (TextView) findViewById(R.id.txt_Register);
                edt_Email_ForgotPwd = (EditText) findViewById(R.id.edt_Email_ForgotPwd);
                btn_reset_pwd = (Button) findViewById(R.id.btn_Update_Pwd);
                linearBack=(LinearLayout)findViewById(R.id.linearBack);
                linearBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
//                txt_Register.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        Intent in = new Intent(Forgot_pwd_Activity.this, LogInActivity.class);
//                        startActivity(in);
//                    }
//                });

                btn_reset_pwd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        submitForm();
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    }
                });
            } else {
                setContentView(R.layout.msg_no_internet);
            }


        } catch (Exception ex) {
            Log.v("Cart_main", ex.getMessage().toString());

        }
    }

    private void ResetPwd_api() {
        //creating a string request to send request to the url

        final ACProgressFlower dialog = new ACProgressFlower.Builder(this)
                .build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_RESET_PWD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();

                        try {

                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            String status = obj.getString("status");
                            if (status.equals("200")) {
                                String success = obj.getString("success");

                                if (success.equals("1")) {
                                    String msg = obj.getString("msg");
                                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                    Log.v("msg", msg);

                                    finish();

                                } else {

                                    edt_Email_ForgotPwd.setText("");
                                    requestFocus(edt_Email_ForgotPwd);
                                    String error = obj.getString("error");
                                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                                    Log.v("error", error);

                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Server error", Toast.LENGTH_SHORT).show();

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
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", edt_Email_ForgotPwd.getText().toString().trim());
               // params.put("lang", setApplicationlanguage);
                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean validateEmail() {
        String email = edt_Email_ForgotPwd.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            edt_Email_ForgotPwd.setError(getString(R.string.Please_enter_a_valid_email));
            requestFocus(edt_Email_ForgotPwd);
            return false;
        }

        return true;
    }

    private void submitForm() {
        if (!validateEmail()) {
            return;
        }
        ResetPwd_api();
    }
}
