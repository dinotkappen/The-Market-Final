package com.the.marketapp.Activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.the.marketapp.Fragment.LogInFragment;
import com.the.marketapp.Fragment.SignUpFragment;
import com.the.marketapp.Other.Config;
import com.the.marketapp.Other.NotificationUtils;
import com.the.marketapp.Other.PrefUtil;
import com.the.marketapp.Other.Utilities;
import com.the.marketapp.Other.VolleySingleton;
import com.the.marketapp.R;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.messaging.FirebaseMessaging;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.cloudist.acplibrary.ACProgressFlower;

public class LogInActivity extends AppCompatActivity {

    private Toolbar toolbar_login;
    private TabLayout tabLayout_login;
    private ViewPager viewPager_login;
    TextView txt_login_title1, txt_login_title2, txt_login_title3, txt_login_title4;
    LinearLayout  linearTabSwitchLayout;
  public static   SharedPreferences preferences;
    Context context;

    private CallbackManager callbackManager;

    LoginButton loginButton;
    public static String accessToken_str;
    String fbID;
    public PrefUtil prefUtil;
    String TAGS;
    String name_fb, email_fb,URL_FB;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public static   Utilities utilities;
    public static String login_token;
    Button btn;
    ACProgressFlower dialogMain;
    private static final String EMAIL = "email";
    public static String firebasseRegID;
    public static String setApplicationlanguage;

    private static final String TAG = LogInActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    static LogInActivity loginActivty ;//= LogInActivity.this;viewPager_login

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        loginActivty = LogInActivity.this;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        Hawk.init(LogInActivity.this)
                .setEncryptionMethod(HawkBuilder.EncryptionMethod.MEDIUM)
                .setStorage(HawkBuilder.newSqliteStorage(LogInActivity.this))
                .setLogLevel(LogLevel.FULL)
                .build();
        try {
            if (utilities.isOnline(this)) {
        setContentView(R.layout.activity_log_in);
        printKeyHash(this);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        linearTabSwitchLayout=(LinearLayout)findViewById(R.id.linearTabSwitchLayout);
        dialogMain= new ACProgressFlower.Builder(this)
                .build();

        preferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        setApplicationlanguage = preferences.getString("setApplicationlanguage", "");
        Log.v("setApplicationlanguage", setApplicationlanguage);

//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                loginButton.performClick();
//            }
//        });
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email"));
        callbackManager = CallbackManager.Factory.create();

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    // txtMessage.setText(message);
                }
            }
        };

        displayFirebaseRegId();

        //firebasseRegID = preferences.getString("regFireID", "0");
        //String j = firebasseRegID;

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        try {
                            dialogMain.show();
                            accessToken_str = loginResult.getAccessToken().getToken();
                            fbID = loginResult.getAccessToken().getUserId();
                            Log.v("fbID", fbID);


//                            startActivityForResult(signInIntent, 101);
//                             save accessToken to SharedPreference
//                            prefUtil.saveAccessToken(accessToken_str);

                            GraphRequest request = GraphRequest.newMeRequest(
                                    loginResult.getAccessToken(),
                                    new GraphRequest.GraphJSONObjectCallback() {
                                        @Override
                                        public void onCompleted(JSONObject jsonObject,
                                                                GraphResponse response) {

                                            // Getting FB User Data
                                            Bundle facebookData = getFacebookData(jsonObject);


                                        }
                                    });

                            Bundle parameters = new Bundle();
                            parameters.putString("fields", "id,first_name,last_name,email,gender");
                            request.setParameters(parameters);
                            request.executeAsync();
                        } catch (Exception ex) {
                            String msg = ex.getMessage().toString();
                            String r = msg;
                        }
                    }


                    @Override
                    public void onCancel() {
                        Log.d(TAG, "Login attempt cancelled.");
                    }

                    @Override
                    public void onError(FacebookException e) {
                        e.printStackTrace();
                        Log.d(TAG, "Login attempt failed.");
                        deleteAccessToken();
                    }
                }
        );




        txt_login_title1 = (TextView) findViewById(R.id.txt_login_title1);
        txt_login_title3 = (TextView) findViewById(R.id.txt_login_title3);
        txt_login_title4 = (TextView) findViewById(R.id.txt_login_title4);
        //btn=(Button)findViewById(R.id.btn);


        txt_login_title1.setText(getString(R.string.Welcome_Sign_in_to_your_Account));
        txt_login_title3.setText(getString(R.string.Create_an_Account));
        txt_login_title4.setText(getString(R.string.Sign_upOne));

        toolbar_login = (Toolbar) findViewById(R.id.toolbar_login);
        setSupportActionBar(toolbar_login);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        viewPager_login = (ViewPager) findViewById(R.id.viewPager_login);
        setupViewPager(viewPager_login);

        tabLayout_login = (TabLayout) findViewById(R.id.tabLayout_login);
        tabLayout_login.setupWithViewPager(viewPager_login);

        for (int i = 0; i < tabLayout_login.getTabCount(); i++) {
            View tab = ((ViewGroup) tabLayout_login.getChildAt(0)).getChildAt(i);
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
            p.setMargins(0, 0, 50, 0);
            tab.requestLayout();
        }
        txt_login_title1.setText(R.string.Welcome_Sign_in_to_your_Account);
        txt_login_title3.setText(R.string.Create_an_Account);
        txt_login_title4.setText(R.string.Sign_up);
        linearTabSwitchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabLayout_login.getTabAt(1).select();
            }
        });
        txt_login_title4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabLayout_login.getTabAt(1).select();

            }
        });
        tabLayout_login.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position_tab = tab.getPosition();
                if (position_tab == 0) {
                    txt_login_title1.setText(R.string.Welcome_Sign_in_to_your_Account);
                    txt_login_title3.setText(R.string.Create_an_Account);
                    txt_login_title4.setText(R.string.Sign_up);
                    txt_login_title4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tabLayout_login.getTabAt(1).select();

                        }
                    });

                } else {
                    txt_login_title1.setText(R.string.Create_A_New_Account);
                    txt_login_title3.setText(R.string.Your_already_have_an_account);
                    txt_login_title4.setText(R.string.sign_in);
                    txt_login_title4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tabLayout_login.getTabAt(0).select();
                        }
                    });
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

            } else {
                setContentView(R.layout.msg_no_internet);
            }


        } catch (Exception ex) {
            Log.v("Cart_main", ex.getMessage().toString());

        }

    }
    //******For getting Hash key to give in fb developer console
    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
                Log.v("key", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
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
    private Bundle getFacebookData(JSONObject object) {
        Bundle bundle = new Bundle();

        try {
            String id = object.getString("id");


            bundle.putString("idFacebook", id);
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));

            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));

//            if (object.has("gender"))
//                bundle.putString("gender", object.getString("gender"));

            if (object.has("email")) {
                bundle.putString("email", object.getString("email"));
            }
            else
            {
                Log.v("firebasseRegID",firebasseRegID);
                name_fb = object.getString("first_name");
                Intent in=new Intent(LogInActivity.this,FbEmailActivity.class);
                in.putExtra("valueMissing","email");
                in.putExtra("name",name_fb);
                in.putExtra("fb_id",fbID);
                in.putExtra("token",accessToken_str);
                in.putExtra("firebasseRegID",firebasseRegID);
                startActivity(in);
            }
            try {
                name_fb = object.getString("first_name");
                email_fb = object.getString("email");
                if (email_fb != null && !email_fb.isEmpty() && !email_fb.equals("null"))
                {

                    FB_API();
                }
                else
                {
                    Intent in=new Intent(LogInActivity.this,FbEmailActivity.class);
                    in.putExtra("name",name_fb);
                    in.putExtra("fb_id",fbID);
                    in.putExtra("token",accessToken_str);
                    in.putExtra("firebasseRegID",firebasseRegID);
                    startActivity(in);


                }



            } catch (JSONException e) {
                e.printStackTrace();
            }
//            prefUtil.saveFacebookUserInfo(object.getString("first_name"),
//                    object.getString("last_name"), object.getString("email"),
//                    "gender", profile_pic.toString());

        } catch (Exception e) {
            Log.d(TAG, "BUNDLE Exception : " + e.toString());
        }

        return bundle;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 100) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.

        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter_login = new ViewPagerAdapter(getSupportFragmentManager());

        adapter_login.addFragment(new LogInFragment(), "");
        adapter_login.addFragment(new SignUpFragment(), "");


        viewPager.setAdapter(adapter_login);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> loginFragmentList = new ArrayList<>();
        private final List<String> loginFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return loginFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return loginFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            loginFragmentList.add(fragment);
            loginFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return loginFragmentTitleList.get(position);
        }
    }

    private void FB_API() {
        //creating a string request to send request to the url
//        final ACProgressFlower dialog = new ACProgressFlower.Builder(this)
//                .build();
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.show();
        URL_FB = utilities.GetUrl() + "sign-up-fb";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_FB,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialogMain.dismiss();
                        SharedPreferences sharedpreferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
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
                                    //   String login_token = jsonObj.getString("login_token");
                                    String jh = login_token;
                                    Log.v("login_token_fb", login_token);
                                    String email = jsonObj.getString("email");
                                    String name = jsonObj.getString("name");

                                    editor.putString("customer_name", name);
                                    editor.putString("login_token", login_token);
                                    editor.putString("user_email", email);
                                    editor.putInt("logedIn", 1);
                                    editor.commit();

//                                    Intent in = new Intent(LogInActivity.this, MainActivity.class);
//                                    startActivity(in);
                                    LoginManager.getInstance().logOut();
//                                    DeviceChkApi();
//                                    Intent in = new Intent(LogInActivity.this, MainActivity.class);
//                                    startActivity(in);

                                    DeviceChkApi(LogInActivity.this);


                                } else if (success.equals("0")) {

                                    String msg = obj.getString("msg");
                                    Log.v("msgh", msg);
                                    if (msg.equals("-1")) {
                                        JSONObject user = obj.getJSONObject("user");
                                        String email = user.getString("email");
                                        if (email == null && email.isEmpty() && email.equals("null")) {
                                            editor.putInt("fb_email", 0);
                                        }
//                                        Intent in = new Intent(LogInActivity.this, SignUpDetailsSocialLogIn.class);
//                                        startActivity(in);
                                        editor.putString("logInMetod", "fb");
                                        editor.commit();
                                        Log.v("Ok", "ok");

                                    }
                                    else if(msg.equals("-3"))
                                    {
                                        Intent in=new Intent(LogInActivity.this,FbEmailActivity.class);
                                        in.putExtra("valueMissing","phone");
                                        in.putExtra("name",name_fb);
                                        in.putExtra("email",email_fb);
                                        in.putExtra("fb_id",fbID);
                                        in.putExtra("token",accessToken_str);
                                        in.putExtra("firebasseRegID",firebasseRegID);
                                        startActivity(in);
                                    }

                                } else {


                                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Server error", Toast.LENGTH_SHORT).show();

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
            protected Map<String, String> getParams() throws AuthFailureError {
                SharedPreferences sharedpreferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                Map<String, String> params = new HashMap<>();
                params.put("name", name_fb);
                params.put("email", email_fb);
                params.put("fb_id", fbID);
                params.put("phone", "");
                params.put("token", accessToken_str);
                params.put("lang", setApplicationlanguage);

                editor.putString("name_fb", name_fb);
                editor.putString("email_fb", email_fb);
                editor.putString("fbID", fbID);
                editor.putString("accessToken_str", accessToken_str);
                editor.commit();


                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }
    public static void DeviceChkApi(final Context mContext) {
        //creating a string request to send request to the url
        login_token = preferences.getString("login_token", null);
        String   URL_DEVICE_REG = utilities.GetUrl() + "accounts/registerdevice";

        //login_token = preferences.getString("login_token", "");


        // Log.v("login_token_LogIn", login_token);
//        Log.v("refreshedToken_LogIn", refreshedToken);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DEVICE_REG,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {


                            //converting response to json object
                            JSONObject obj = new JSONObject(response);


                            String status = obj.getString("status");
                            if (status.equals("200")) {
                                String success = obj.getString("success");

                                if (success.equals("1")) {

                                    String msg = obj.getString("msg");
                                    Log.v("msgDevice", msg);
                                 String from=  Hawk.get("from","");
                                 if(!from.equals("SingleItemActivity"))
                                 {
                                     
                                     Intent in=new Intent(mContext,MainActivity.class);
                                     mContext.startActivity(in);
                                 }
                                    loginActivty.finish();

                                } else {


                                    Toast.makeText(mContext, "Failed", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                Toast.makeText(mContext, "Server error", Toast.LENGTH_SHORT).show();

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
            /** Passing some request headers* */
            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("userToken", login_token);

                return headers;
            }


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("device_id", firebasseRegID);
                params.put("device_type", "Android");
                params.put("lang", setApplicationlanguage);
                params.put("device_os", "Android");

                return params;
            }
        };

        VolleySingleton.getInstance(mContext).addToRequestQueue(stringRequest);

    }
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", "");
        firebasseRegID=regId;

        Log.e(TAG, "Firebase reg id: " + regId);

//        if (!TextUtils.isEmpty(regId))
////            txtRegId.setText("Firebase Reg Id: " + regId);
//        else
        //txtRegId.setText("Firebase Reg Id is not received yet!");
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
}


