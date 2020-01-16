package com.the.marketapp.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
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
import android.view.inputmethod.InputMethodManager;
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
import com.the.marketapp.Adapter.NotificationAdapter;
import com.the.marketapp.Model.NotificationModel;
import com.the.marketapp.Other.Utilities;
import com.the.marketapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.the.marketapp.Adapter.Item_Main_Adapter.MY_PREFS_NAME;

public class NotificationFragment extends Fragment {
    View rootView;


    ArrayList<NotificationModel> notificationModel = new ArrayList<NotificationModel>();
    NotificationAdapter notificationAdapter;
    String NOTIFCATION_URL;
    Utilities utilities;
    SharedPreferences preferences;
    String setApplicationlanguage, login_token;
    int logedIn;

    LinearLayout linearMainLayout, linearOrderNotVisible;
    MainActivity mainActivity;


    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            if (utilities.isOnline(getActivity())) {
                // Inflate the layout for this fragment
                rootView = inflater.inflate(R.layout.fragment_notification, container, false);
                linearMainLayout = (LinearLayout) rootView.findViewById(R.id.linearMainLayout);
                linearOrderNotVisible = (LinearLayout) rootView.findViewById(R.id.linearOrderNotVisible);
                linearMainLayout.setVisibility(View.VISIBLE);
                linearOrderNotVisible.setVisibility(View.GONE);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(R.layout.actionbar_common_bottom);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(getLayoutInflater().inflate(R.layout.actionbar_common_bottom, null),
                        new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));
                LinearLayout linearMenuWhite = (LinearLayout) ((AppCompatActivity) getActivity()).findViewById(R.id.linearMenuWhite);
                linearMenuWhite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // If the navigation drawer is not open then open it, if its already open then close it.
                        if (!mainActivity.drawer.isDrawerOpen(GravityCompat.START))
                            mainActivity.drawer.openDrawer(Gravity.START);
                        else mainActivity.drawer.closeDrawer(Gravity.END);
                    }
                });
                mainActivity.navigation.setVisibility(View.VISIBLE);
                TextView txt_actionbar_Title = (TextView) ((AppCompatActivity) getActivity()).findViewById(R.id.txt_actionbar_Title);
                txt_actionbar_Title.setText(getString(R.string.NOTIFICATIONS));

                preferences = getActivity().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                setApplicationlanguage = preferences.getString("setApplicationlanguage", "");
                login_token = preferences.getString("login_token", "");
                logedIn = preferences.getInt("logedIn", 0);
                Log.v("language_NF", setApplicationlanguage);
                Log.v("login_token_NF", login_token);
                Log.v("logedIn_NF", "" + logedIn);
                if (logedIn == 1) {
                    GET_NOTIFICATION_API();
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


            } else {
                rootView = inflater.inflate(R.layout.msg_no_internet, container, false);
            }
        } catch (Exception ex) {
            String msg = ex.getMessage().toString();
            Log.v("msg_Main", msg);
        }
        return rootView;
    }

    private void setNotification() {

        try {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            RecyclerView recyclerView = rootView.findViewById(R.id.recyclerViewNotification);
            recyclerView.setLayoutManager(layoutManager);
            notificationAdapter = new NotificationAdapter(getActivity(), notificationModel);
            recyclerView.setAdapter(notificationAdapter);
        } catch (Exception ex) {
            String msg = ex.getMessage().toString();
            Log.v("setNotification", msg);
        }
    }

    public void GET_NOTIFICATION_API() {

        NOTIFCATION_URL = utilities.GetUrl() + "accounts/notification";
        if (NOTIFCATION_URL != null && !NOTIFCATION_URL.isEmpty() && !NOTIFCATION_URL.equals("null")) {
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            StringRequest sr = new StringRequest(Request.Method.GET, NOTIFCATION_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            notificationModel.clear();
                            try {
                                if (response != null && !response.isEmpty() && !response.equals("null")) {
                                    //getting the whole json object from the response
                                    JSONObject obj = new JSONObject(response);
                                    if (obj.length() > 0) {
                                        String status = obj.getString("status");
                                        Log.v("status", status);
                                        String success = obj.getString("success");


                                        if (status.equals("200")) {
                                            if (success.equals("1")) {
                                                JSONObject jsonObjectnotification = obj.getJSONObject("notification");
                                                if (jsonObjectnotification.length() > 0) {

                                                    JSONArray jsonArray_data = jsonObjectnotification.getJSONArray("data");
                                                    if (jsonArray_data.length() > 0) {
                                                        linearMainLayout.setVisibility(View.VISIBLE);
                                                        linearOrderNotVisible.setVisibility(View.GONE);
                                                        for (int i = 0; i < jsonArray_data.length(); i++) {
                                                            JSONObject jsondata = jsonArray_data.getJSONObject(i);
                                                            String id = jsondata.getString("id");
                                                            String order_id = jsondata.getString("order_id");
                                                            String title = jsondata.getString("title");
                                                            String message = jsondata.getString("message");

                                                            notificationModel.add(new NotificationModel(id, order_id, title, message));
                                                        }
                                                        setNotification();

                                                    } else {
                                                        linearMainLayout.setVisibility(View.GONE);
                                                        linearOrderNotVisible.setVisibility(View.VISIBLE);
                                                    }
                                                }


                                            } else {
                                                Log.e("Success", "Failed: ");
                                            }


                                        } else {
                                            Toast.makeText(getActivity(), getString(R.string.Server_Error), Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Log.e("Response", "Response: ");
                                    }
                                } else {
                                    Log.e("ResponseString", "ResponseString:Empty");
                                }


                            } catch (JSONException e) {
                                Log.v("setNotification", e.getMessage().toString());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("HttpClient", "error: " + error.toString());
                        }
                    }) {


                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("userToken", login_token);
                    params.put("lang", setApplicationlanguage);
                    Log.v("lang", setApplicationlanguage);
                    return params;
                }
            };
            queue.add(sr);
        } else {
            Log.v("Url", "No url");
        }

    }


}
