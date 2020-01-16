package com.the.marketapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.the.marketapp.Activity.LogInActivity;
import com.the.marketapp.Activity.MainActivity;
import com.the.marketapp.Other.Utilities;
import com.the.marketapp.Other.VolleySingleton;
import com.the.marketapp.R;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cc.cloudist.acplibrary.ACProgressFlower;


public class SignUpFragment extends Fragment {
    View rootView;
    TextView txtTC;
    Button btn_SignUp;
    String URL_SIGNUP;
    int countRbTC = 0;
    String selectLang;
    Utilities utilities;
    RadioButton radioTC;
    ScrollView scrollSignUp;
    LogInActivity logInActivity;
    LinearLayout len_teams_click;
    SharedPreferences preferences;
    String setApplicationlanguage;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    EditText edt_Name, edt_Email, edt_Phone, edt_Password;

    public SignUpFragment() {
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
            // Inflate the layout for this fragment
            rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);
            URL_SIGNUP = utilities.GetUrl() + "sign-up?";

            btn_SignUp = (Button) rootView.findViewById(R.id.btn_SignUp);
            edt_Name = (EditText) rootView.findViewById(R.id.edt_Name);
            edt_Email = (EditText) rootView.findViewById(R.id.edt_Email);
            edt_Phone = (EditText) rootView.findViewById(R.id.edt_Phone);
            edt_Password = (EditText) rootView.findViewById(R.id.edt_Password);
//            txtTC = (TextView) rootView.findViewById(R.id.txtTC);
            scrollSignUp = (ScrollView) rootView.findViewById(R.id.scrollSignUp);
            len_teams_click =  rootView.findViewById(R.id.len_team_click);

            final RadioButton rbTC = (RadioButton) rootView.findViewById(R.id.rbTC);

            rbTC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (countRbTC == 0) {
                        rbTC.setChecked(true);
                        countRbTC = 1;
                    } else {
                        rbTC.setChecked(false);
                        countRbTC = 0;
                    }

                }
            });
            len_teams_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(getActivity(), MainActivity.class);
                    in.putExtra("fromSignUpFragment","fromSignUpFragment");
                    startActivity(in);
                }
            });

            preferences = getActivity().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
            setApplicationlanguage = preferences.getString("setApplicationlanguage", "");
                selectLang=Hawk.get("selectLang","");
                if(selectLang.equals("en"))
                {
                    edt_Name.setGravity(Gravity.LEFT);
                    edt_Email.setGravity(Gravity.LEFT);
                    edt_Phone.setGravity(Gravity.LEFT);
                    edt_Password.setGravity(Gravity.LEFT);
                }
                else
                {
                    edt_Name.setGravity(Gravity.RIGHT);
                    edt_Email.setGravity(Gravity.RIGHT);
                    edt_Phone.setGravity(Gravity.RIGHT);
                    edt_Password.setGravity(Gravity.RIGHT);

                }


                btn_SignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (rbTC.isChecked()) {
                        submitForm();
                        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    } else {
                        rbTC.setError(getString(R.string.AgreeTC));
                        scrollSignUp.smoothScrollTo(0, rbTC.getTop());
                    }
                    Log.e("login_check", "Login_chech");

                }
            });
//        } else {
//            rootView = inflater.inflate(R.layout.msg_no_internet, container, false);
//        }
//    } catch (Exception ex) {
//        String msg = ex.getMessage().toString();
//        Log.v("msg_Main", msg);
//    }
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
        if (!validatePassword()) {
            return;
        }

        UserRegistration_api();
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
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
    private boolean validateName() {
        if (edt_Name.getText().toString().trim().isEmpty()) {
            edt_Name.setError(getString(R.string.Enter_a_valid_name));
            requestFocus(edt_Name);
            return false;
        }

        return true;
    }

    private boolean validateEmail() {
        String email = edt_Email.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            edt_Email.setError(getString(R.string.Enter_Your_Email_Id_Here));
            requestFocus(edt_Email);
            return false;
        }

        return true;
    }

    private boolean validatePhone() {
        if (edt_Phone.getText().toString().trim().isEmpty()) {
            edt_Phone.setError(getString(R.string.Please_enter_the_phone_number));
            requestFocus(edt_Phone);
            return false;
        }

        return true;
    }

    private boolean validatePassword() {
        if (edt_Password.getText().toString().trim().isEmpty()) {
            edt_Password.setError(getString(R.string.Password_Canot_Empty));
            requestFocus(edt_Password);
            return false;
        }

        if (TextUtils.isEmpty(edt_Password.getText().toString()) || edt_Password.getText().toString().length() < 8) {
            edt_Password.setError(getString(R.string.Passwords_must_be_at_least_6_characters_long));
            requestFocus(edt_Password);
            return false;
        }
        return true;
    }

    private void UserRegistration_api() {
        //creating a string request to send request to the url

        final ACProgressFlower dialog = new ACProgressFlower.Builder(getActivity())
                .build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SIGNUP,
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
                                    JSONObject user_obj = obj.getJSONObject("user");
                                    String user_id = user_obj.getString("id");
                                    String customer_name = user_obj.getString("name");
                                    String user_email = user_obj.getString("email");
                                    String login_token = user_obj.getString("login_token");
                                    String phone = user_obj.getString("phone");
                                    SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedpreferences.edit();


                                    editor.putString("user_id", user_id);
                                    editor.putString("customer_name", customer_name);
                                    editor.putString("login_token", login_token);
                                    editor.putString("user_email", user_email);
                                    editor.putString("phone", phone);
                                    editor.putInt("logedIn", 1);

                                    editor.commit();

                                    Toast.makeText(getContext(), "User registered successfully...", Toast.LENGTH_SHORT).show();
                                    logInActivity.DeviceChkApi(getActivity());
//                                    Intent in = new Intent(getActivity(), MainActivity.class);
//                                    startActivity(in);
                                } else {


                                    Toast.makeText(getContext(), "Email already exists. please choose a different one", Toast.LENGTH_SHORT).show();

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
                params.put("name", edt_Name.getText().toString().trim());
                params.put("phone", edt_Phone.getText().toString().trim());
                params.put("email", edt_Email.getText().toString().trim());
                params.put("password", edt_Password.getText().toString().trim());

                return params;
            }
        };


        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }

}
