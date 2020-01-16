package com.the.marketapp.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
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
import com.the.marketapp.Activity.MainActivity;
import com.the.marketapp.Other.Utilities;
import com.the.marketapp.R;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;

import org.json.JSONException;
import org.json.JSONObject;

import cc.cloudist.acplibrary.ACProgressFlower;


public class Contact_Fragment extends Fragment {
    View rootView;
    EditText edt_Name, edt_Email, edt_Phone,edt_Cmnt;
    private static String JSON_URL_CONTACT;
    Utilities utilities;
    String login_token;
    String name, email, telephone, comment;
    SharedPreferences preferences;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    Button btn_Send;
    MainActivity mainActivity;
    String selectLang;
    public Contact_Fragment() {
        // Required empty public constructor
    }





    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        try {
//            if (utilities.isOnline(getActivity())) {
        Hawk.init(getContext())
                .setEncryptionMethod(HawkBuilder.EncryptionMethod.MEDIUM)
                .setStorage(HawkBuilder.newSqliteStorage(getContext()))
                .setLogLevel(LogLevel.FULL)
                .build();
        selectLang = Hawk.get("selectLang", "");

        rootView= inflater.inflate(R.layout.fragment_contact, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(R.layout.actionbar_common);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(getLayoutInflater().inflate(R.layout.actionbar_common, null),
                new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));

        TextView txt_actionbar_Title = (TextView) ((AppCompatActivity) getActivity()).findViewById(R.id.txt_actionbar_Title);
        txt_actionbar_Title.setText(R.string.CONTACT_US);
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
        JSON_URL_CONTACT = utilities.GetUrl() + "contact-us?";
        preferences = getContext().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        login_token = preferences.getString("login_token", null);
        edt_Name = (EditText) rootView.findViewById(R.id.edt_Name);
        edt_Email = (EditText) rootView.findViewById(R.id.edt_Email);
        edt_Phone = (EditText) rootView.findViewById(R.id.edt_Phone);
        edt_Cmnt = (EditText) rootView.findViewById(R.id.edt_Message);
        btn_Send=(Button)rootView.findViewById(R.id.btn_Send);

        if (selectLang.equals("en")) {
            edt_Phone.setGravity(Gravity.LEFT);
            edt_Name.setGravity(Gravity.LEFT);
            edt_Cmnt.setGravity(Gravity.LEFT);
            edt_Email.setGravity(Gravity.LEFT);
//            edt_Cmnt.setForegroundGravity(Gravity.CENTER_VERTICAL);

        } else {
            edt_Phone.setGravity(Gravity.RIGHT);
            edt_Name.setGravity(Gravity.RIGHT);
            edt_Cmnt.setGravity(Gravity.RIGHT);
            edt_Email.setGravity(Gravity.RIGHT);
//            edt_Cmnt.setForegroundGravity(Gravity.CENTER_VERTICAL);

        }

        btn_Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                submitForm();
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                } catch (Exception ex) {
                    String msg = ex.getMessage().toString();
                    Log.v("btn_Send", msg);
                }
            }
        });
//            } else {
//                rootView = inflater.inflate(R.layout.msg_no_internet, container, false);
//            }
//        } catch (Exception ex) {
//            String msg = ex.getMessage().toString();
//            Log.v("msg_Main", msg);
//        }
//        appBarLayout = (AppBarLayout) rootView.findViewById(R.id.app_layout);
//        appBarLayout.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#330000ff")));
        return rootView;
    }
    /**
     * Validating form
     */
    private void submitForm() {
        if (!validateName()) {
            return;
        }
        if (!validateEmail()) {
            return;
        }
        if (!validatePhone()) {
            return;
        }
        if (!validatePhoneFormat()) {
            return;
        }
        if (!validateMessage()) {
            return;
        }

        Contact_Api();
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private boolean validateMessage() {
        if (edt_Cmnt.getText().toString().trim().isEmpty()) {
            edt_Cmnt.setError(getString(R.string.Please_enter_description));
            requestFocus(edt_Cmnt);
            return false;
        }

        return true;
    }
    private boolean validateName() {
        if (edt_Name.getText().toString().trim().isEmpty()) {
            edt_Name.setError(getString(R.string.Enter_a_valid_name));
            requestFocus(edt_Name);
            return false;
        }

        return true;
    }
    private boolean validatePhoneFormat() {
        if(edt_Phone.getText().toString().trim().length() < 6 || edt_Phone.getText().toString().trim().length() > 13) {
            // if(phone.length() != 10) {
            edt_Phone.setError(getString(R.string.Invalid_Phone_Number));
            requestFocus(edt_Phone);
            return false;
        }

        return true;
    }
    private boolean validateEmail() {
        String email = edt_Email.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            edt_Email.setError(getString(R.string.Invalid_Email_Address));
            requestFocus(edt_Email);
            return false;
        }

        return true;
    }


    private boolean validatePhone() {
        if (edt_Phone.getText().toString().trim().isEmpty()) {
            edt_Phone.setError(getString(R.string.Invalid_Phone_Number));
            requestFocus(edt_Phone);
            return false;
        }

        return true;
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void Contact_Api() {
        final ACProgressFlower dialog = new ACProgressFlower.Builder(getActivity())
                .build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        name = edt_Name.getText().toString();
        email = edt_Email.getText().toString();
        telephone = edt_Phone.getText().toString();
        comment=edt_Cmnt.getText().toString();
        JSON_URL_CONTACT = JSON_URL_CONTACT + "name=" + name + "&email=" + email + "&telephone=" + telephone + "&comment=" + comment;
        Log.v("JSON_URL_CONTACT",JSON_URL_CONTACT);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL_CONTACT,
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
                                    edt_Name.setText("");
                                    edt_Phone.setText("");
                                    edt_Email.setText("");
                                    edt_Cmnt.setText("");
                                    Log.v("msg",msg);
                                }



                                //  item_main_adapter.notifyDataSetChanged();
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
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //adding the string request to request queue
        requestQueue.add(stringRequest);

    }
}