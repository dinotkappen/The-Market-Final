package com.the.marketapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

public class SignUpPhoneValidationActivity extends AppCompatActivity {
    String googleName, googleEmail, googleToken, googleID, URL_GOOGLE, logInMetod, phone;
    String login_token;
    Utilities utilities;
    EditText edtPhone;
    Button btnSubmit;
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        try {
            if (utilities.isOnline(this)) {
                setContentView(R.layout.activity_sign_up_phone_validation);
                edtPhone = (EditText) findViewById(R.id.edtPhone);
                btnSubmit = (Button) findViewById(R.id.btnSubmit);

                Intent intent = getIntent();


                googleName = intent.getStringExtra("googleName");
                googleEmail = intent.getStringExtra("googleEmail");
                googleToken = intent.getStringExtra("googleToken");
                googleID = intent.getStringExtra("googleID");
                URL_GOOGLE = intent.getStringExtra("URL_GOOGLE");
                logInMetod = intent.getStringExtra("logInMetod");


                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (logInMetod != null && !logInMetod.isEmpty() && !logInMetod.equals("null")) {
                            if (logInMetod.equals("gmail")) {


                                    submitForm();



                            }
                        }
                    }
                });

            } else {
                setContentView(R.layout.msg_no_internet);
            }
        } catch (Exception ex) {
            Log.v("Phone", ex.getMessage().toString());
        }
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
           getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    private boolean validatePhone() {
        if (edtPhone.getText().toString().trim().isEmpty()) {
            edtPhone.setError(getString(R.string.Invalid_Phone_Number));
            requestFocus(edtPhone);
            return false;
        }


        return true;
    }
    private boolean validateFormat() {
        if(edtPhone.getText().toString().trim().length() < 6 || edtPhone.getText().toString().trim().length() > 13) {
            // if(phone.length() != 10) {
            edtPhone.setError(getString(R.string.Invalid_Phone_Number));
            requestFocus(edtPhone);
            return false;
        }

        return true;
    }
    /**
     * Validating form
     */
    private void submitForm() {

        if (!validatePhone()) {
            return;
        }
        if (!validateFormat()) {
            return;
        }

        googleLogIn();
    }
    private void googleLogIn() {
        phone=edtPhone.getText().toString();
        //creating a string request to send request to the url
        final ACProgressFlower dialog = new ACProgressFlower.Builder(SignUpPhoneValidationActivity.this)
                .build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        URL_GOOGLE = utilities.GetUrl() + "sign-up-google";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GOOGLE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();

                        try {
                            SharedPreferences sharedpreferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);


                            String status = obj.getString("status");
                            if (status.equals("200")) {
                                String success = obj.getString("success");

                                if (success.equals("1")) {

                                    JSONObject jsonObj = obj.getJSONObject("user");

                                    login_token = jsonObj.getString("login_token");
                                    Log.v("login_token_gmail", login_token);
                                    String email_Success = jsonObj.getString("email");
                                    String name_Success = jsonObj.getString("name");
                                    String phone_Success = jsonObj.getString("phone");

                                    editor.putString("login_token", login_token);
                                    editor.putString("user_email", email_Success);
                                    editor.putString("phone", phone_Success);
                                    editor.putInt("logedIn", 1);
                                    editor.commit();


                                    Intent in = new Intent(SignUpPhoneValidationActivity.this, MainActivity.class);
                                    startActivity(in);


                                } else if (success.equals("0")) {

                                    String msg = obj.getString("msg");
                                    Log.v("msgh", msg);
                                    if (msg.equals("-1")) {

                                        Intent in = new Intent(SignUpPhoneValidationActivity.this, SignUpPhoneValidationActivity.class);
                                        in.putExtra("logInMetod", "gmail");
                                        in.putExtra("googleName", googleName);
                                        in.putExtra("googleEmail", googleEmail);
                                        in.putExtra("googleID", googleID);
                                        in.putExtra("googleToken", googleToken);
                                        startActivity(in);

                                        Log.v("Ok", "ok");

                                    }


                                } else {


                                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();

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
                params.put("name", googleName);
                params.put("email", googleEmail);
                params.put("phone", phone);
                params.put("gmail_id", googleID);
                params.put("token", googleToken);

                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }
}
