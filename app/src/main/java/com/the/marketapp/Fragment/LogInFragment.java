package com.the.marketapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.the.marketapp.Activity.LogInActivity;
import com.the.marketapp.Activity.SignUpPhoneValidationActivity;
import com.bumptech.glide.Glide;
import com.the.marketapp.Activity.Forgot_pwd_Activity;
import com.the.marketapp.Activity.MainActivity;
import com.the.marketapp.Other.PrefUtil;
import com.the.marketapp.Other.Utilities;
import com.the.marketapp.Other.VolleySingleton;
import com.the.marketapp.R;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import cc.cloudist.acplibrary.ACProgressFlower;


public class LogInFragment extends Fragment {
    View rootView;
    Button btn_LogIn;
    String URL_LOGIN;
    Utilities utilities;
    EditText edt_Username, edt_Pwd;
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    TextView txt_forgot_pwd;
    LinearLayout linear_fb, linear_gmail;
    ImageView imgFb, imgGmail;
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 100;
    private CallbackManager callbackManager;
    String googleName, googleEmail, googleToken, googleID, URL_GOOGLE;
    String login_token;
    LoginButton loginButton;
    public PrefUtil prefUtil;
    public static String accessToken_str;
    String fbID;
    String TAG;
    String name_fb, email_fb, URL_FB;
    ACProgressFlower dialogMain;
    String setApplicationlanguage;
    SharedPreferences preferences;
    LogInActivity logInActivity;
    MainActivity mainActivity;

    public LogInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {

            if (utilities.isOnline(getActivity())) {

                // Inflate the layout for this fragment
                FacebookSdk.sdkInitialize(getActivity());
                callbackManager = CallbackManager.Factory.create();
                rootView = inflater.inflate(R.layout.fragment_log_in, container, false);
                linear_fb = (LinearLayout) rootView.findViewById(R.id.linear_fb);
                linear_gmail = (LinearLayout) rootView.findViewById(R.id.linear_gmail);
                loginButton = (LoginButton) rootView.findViewById(R.id.login_button);
                imgFb = (ImageView) rootView.findViewById(R.id.imgFb);
                imgGmail = (ImageView) rootView.findViewById(R.id.imgGmail);
                dialogMain = new ACProgressFlower.Builder(getActivity())
                        .build();
                Hawk.init(getActivity())
                        .setEncryptionMethod(HawkBuilder.EncryptionMethod.MEDIUM)
                        .setStorage(HawkBuilder.newSqliteStorage(getActivity()))
                        .setLogLevel(LogLevel.FULL)
                        .build();
                Glide.with(this)
                        .load(R.drawable.ic_fb)
                        .into(imgFb);
                Glide.with(this)
                        .load(R.drawable.ic_gmail)
                        .into(imgGmail);

                URL_LOGIN = utilities.GetUrl() + "login?";
                preferences = getActivity().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                setApplicationlanguage = preferences.getString("setApplicationlanguage", "");

                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                txt_forgot_pwd = (TextView) rootView.findViewById(R.id.txt_forgot_pwd);

                txt_forgot_pwd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in = new Intent(getActivity(), Forgot_pwd_Activity.class);
                        startActivity(in);

                    }
                });
                btn_LogIn = (Button) rootView.findViewById(R.id.btn_LogIn);
                edt_Username = (EditText) rootView.findViewById(R.id.edt_Username);
                edt_Pwd = (EditText) rootView.findViewById(R.id.edt_Pwd);
                if (setApplicationlanguage.equals("en")) {
                    edt_Username.setGravity(Gravity.LEFT);
                    edt_Pwd.setGravity(Gravity.LEFT);
                } else {
                    edt_Username.setGravity(Gravity.RIGHT);
                    edt_Pwd.setGravity(Gravity.RIGHT);

                }

                dialogMain.setCanceledOnTouchOutside(false);

                btn_LogIn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        submitForm();
                        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    }
                });

                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();
                mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
                linear_gmail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogMain.show();
                        googleSignIn();
                    }
                });
                // printKeyHash(getActivity());
                linear_fb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        loginButton.performClick();
                    }
                });
                loginButton.setReadPermissions(Arrays.asList(
                        "public_profile", "email"));
                callbackManager = CallbackManager.Factory.create();


            } else {
                rootView = inflater.inflate(R.layout.msg_no_internet, container, false);
            }


        } catch (Exception ex) {
            Log.v("Cart_main", ex.getMessage().toString());

        }
        return rootView;
    }

    private void deleteAccessToken() {
        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {

                if (currentAccessToken == null) {
                    //User logged out
                    prefUtil.clearToken();
                    LoginManager.getInstance().logOut();
                }
            }
        };
    }

    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
            if (acct != null) {
                googleName = acct.getDisplayName();
                String personGivenName = acct.getGivenName();
                String personFamilyName = acct.getFamilyName();
                googleEmail = acct.getEmail();
                googleID = acct.getId();
                googleToken = acct.getIdToken();
                Uri personPhoto = acct.getPhotoUrl();
                Log.v("googleID", googleID);
                Log.v("googleToken", googleToken);

                googleLogIn();
            }

            // Signed in successfully, show authenticated UI.
            // updateUI(account);
        } catch (ApiException e) {


            String xyz = e.getMessage().toString();
            String j = xyz;
            Log.v("xyz", xyz);

        }
    }


    /**
     * Validating form
     */
    private void submitForm() {
        if (!validateEmail()) {
            return;
        }


        if (!validatePassword()) {
            return;
        }

        LogInCheck_api();
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validateEmail() {
        try {
            String email = edt_Username.getText().toString().trim();

            if (email.isEmpty() || !isValidEmail(email)) {
                edt_Username.setError(getString(R.string.Please_enter_a_valid_email));
                requestFocus(edt_Username);
                return false;
            }
        } catch (Exception ex) {
            Log.v("validateEmail", ex.getMessage().toString());

        }
        return true;
    }

    private boolean validatePassword() {
        if (edt_Pwd.getText().toString().trim().isEmpty()) {
            edt_Pwd.setError(getString(R.string.Password_cannot_be_empty));
            requestFocus(edt_Pwd);
            return false;
        }

        return true;
    }


    private void googleLogIn() {
        //creating a string request to send request to the url

//        URL_GOOGLE = utilities.GetUrl() + "sign-up-google";
        URL_GOOGLE="https://qgarage.cherrydemoserver10.com/api/customer/google";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GOOGLE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialogMain.dismiss();
                        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        try {


                            //converting response to json object
                            JSONObject obj = new JSONObject(response);


                            String status = obj.getString("status");
                            if (status.equals("200")) {
                                String success = obj.getString("success");

                                if (success.equals("1")) {

                                    JSONObject jsonObj = obj.getJSONObject("user");

                                    login_token = jsonObj.getString("login_token");
                                    Hawk.put("login_token", login_token);

                                    Log.v("login_token_gmail", login_token);
                                    String email = jsonObj.getString("email");
                                    String name = jsonObj.getString("name");

                                    editor.putString("login_token", login_token);
                                    editor.putString("user_email", email);
                                    editor.putString("customer_name", name);


                                    editor.putInt("logedIn", 1);
                                    editor.commit();


//                                    Intent in = new Intent(getActivity(), MainActivity.class);
//                                    startActivity(in);
                                    logInActivity.DeviceChkApi(getActivity());


                                } else if (success.equals("0")) {

                                    String msg = obj.getString("msg");
                                    Log.v("msgh", msg);
                                    if (msg.equals("-1")) {

                                        Intent in = new Intent(getActivity(), SignUpPhoneValidationActivity.class);
                                        in.putExtra("logInMetod", "gmail");
                                        in.putExtra("googleName", googleName);
                                        in.putExtra("googleEmail", googleEmail);
                                        in.putExtra("googleID", googleID);
                                        in.putExtra("googleToken", googleToken);
                                        startActivity(in);

                                        Log.v("Ok", "ok");

                                    }


                                } else {


                                    Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                Toast.makeText(getActivity(), "Server error", Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                Map<String, String> params = new HashMap<>();
                params.put("name", googleName);
                params.put("email", googleEmail);
                params.put("phone", "");
                params.put("gmail_id", googleID);
                params.put("token", googleToken);
                editor.putString("googleName", googleName);
                editor.putString("googleEmail", googleEmail);
                editor.putString("googleID", googleID);
                editor.putString("googleToken", googleToken);
                editor.commit();


                return params;
            }
        };

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }

    private void LogInCheck_api() {
        //creating a string request to send request to the url

        final ACProgressFlower dialog = new ACProgressFlower.Builder(getActivity())
                .build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();

                        try {

                            SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();

                            //converting response to json object
                            JSONObject obj = new JSONObject(response);


                            String status = obj.getString("status");

                            if (status.equals("200")) {
                                String success = obj.getString("success");

                                if (success.equals("1")) {
                                    JSONObject user_obj = obj.getJSONObject("user");
                                    String user_id = user_obj.getString("id");
                                    String customer_name = user_obj.getString("name");
                                    String user_email = user_obj.getString("email");
                                    String login_token = user_obj.getString("login_token");
                                    String lname = user_obj.getString("lname");
                                    String profile_pic = user_obj.getString("profile_pic");


                                    Hawk.put("login_token", login_token);
                                    editor.putString("user_id", user_id);
                                    editor.putString("customer_name", customer_name);
                                    editor.putString("login_token", login_token);
                                    Log.v("login_token", login_token);
                                    editor.putString("user_email", user_email);
                                    editor.putString("lname", lname);
                                    editor.putString("profile_pic", profile_pic);
                                    editor.putInt("logedIn", 1);
                                    editor.commit();


                                    logInActivity.DeviceChkApi(getActivity());


                                    //getActivity().finish();
                                } else {
                                    String msg = obj.getString("msg");
                                    edt_Username.setText("");
                                    requestFocus(edt_Username);
                                    edt_Pwd.setText("");
                                    editor.putInt("logedIn", 0);
                                    editor.commit();
                                    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                Toast.makeText(getContext(), getString(R.string.Server_Error), Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            Log.v("Cart_main", e.getMessage().toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("lang", setApplicationlanguage);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", edt_Username.getText().toString().trim());
                params.put("password", edt_Pwd.getText().toString().trim());

                return params;
            }
        };

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }
}
