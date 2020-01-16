package com.the.marketapp.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.the.marketapp.Activity.LogInActivity;
import com.the.marketapp.Activity.MainActivity;
import com.the.marketapp.Other.Utilities;
import com.the.marketapp.R;
import com.bumptech.glide.request.RequestOptions;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import cc.cloudist.acplibrary.ACProgressFlower;

import static android.content.Context.MODE_PRIVATE;

public class Your_Info_Fragment extends Fragment {
    EditText edt_Name, edt_Email, edt_Phone;
    String name, email, phone, str_user_pic;
    String JSON_URL_PROFILE, JSON_URL_UPDATE_PROFILE;
    String login_token;
    Utilities utilities;
    Button btn_Update;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences preferences;
    ImageView profile_image, imgProfileEdit;
    TextView txt_customer_name, txt_logout;
    View rootView;
    String setApplicationlanguage;

    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    String[] permissionsRequired = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private Button btnCheckPermissions;
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;


    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int MY_GALLERY_REQUEST_CODE = 101;
    int changeProfileImg = 0;
    int PERMISSION_ALL = 1;
    //    String[] PERMISSIONS = {
//
//            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            android.Manifest.permission.SYSTEM_ALERT_WINDOW,
//            android.Manifest.permission.CAMERA
//    };
    ArrayList<Uri> img_List = new ArrayList<>();
    Bitmap photo = null;
    Uri selectedMediaUri;
    String encodedImage;
    String selectLang;
    // EditText edtCodeEng,edtCodeAr;
    ACProgressFlower dialogUpdateProfile;

    public Your_Info_Fragment() {
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
                Hawk.put("pages", "profile");
                // Inflate the layout for this fragment
                rootView = inflater.inflate(R.layout.fragment_your__info_, container, false);
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
                txt_actionbar_TitleNomenu.setText(R.string.YOUR_INFO);

                JSON_URL_PROFILE = utilities.GetUrl() + "accounts/my-account";
                preferences = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                setApplicationlanguage = preferences.getString("setApplicationlanguage", "");
                selectLang = Hawk.get("selectLang", "");

                profile_image = (ImageView) rootView.findViewById(R.id.profile_image);
                imgProfileEdit = (ImageView) rootView.findViewById(R.id.imgProfileEdit);
                edt_Name = (EditText) rootView.findViewById(R.id.edt_Name);
                edt_Email = (EditText) rootView.findViewById(R.id.edt_Email);
                edt_Phone = (EditText) rootView.findViewById(R.id.edt_Phone);

                if (selectLang.equals("en")) {
                    edt_Name.setGravity(Gravity.LEFT);
                    edt_Email.setGravity(Gravity.LEFT);
                    edt_Phone.setGravity(Gravity.LEFT);
                } else {
                    edt_Name.setGravity(Gravity.RIGHT);
                    edt_Email.setGravity(Gravity.RIGHT);
                    edt_Phone.setGravity(Gravity.RIGHT);
                }



                btn_Update = (Button) rootView.findViewById(R.id.btn_Update);
                int logedIn = preferences.getInt("logedIn", 0);
                permissionStatus = getActivity().getSharedPreferences("permissionStatus", MODE_PRIVATE);
                profile_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectImage();

                    }
                });
                imgProfileEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            selectImage();

                        } catch (Exception ex) {
                            String msg = ex.getMessage().toString();
                            Log.v("imgProfileEdit", msg);
                        }
                    }
                });
                if (logedIn == 1) {
                    login_token = preferences.getString("login_token", null);
                    Log.v("login_token", login_token);
                    btn_Update.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                submitForm();
                                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                            } catch (Exception ex) {
                                String msg = ex.getMessage().toString();
                                Log.v("btn_Update", msg);
                            }
                        }
                    });
                    loadProfile_api();


//                    btn_Update.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                            submitForm();
//
//                        }
//                    });
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(
                            getContext());

                    builder.setTitle(getString(R.string.Login));
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

    String currentPhotoPath;

    private void galleryAddPic() {
        try {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File f = new File(currentPhotoPath);
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            img_List.add(contentUri);
            String url = "" + contentUri;

//            Glide.with(getContext())
//                    .load(url)
//                    .placeholder(R.drawable.avatar)
//                    .into(profile_image);

            Glide.with(getContext())
                    .load(url)
                    .apply(new RequestOptions().placeholder(R.drawable.avatar).error(R.drawable.avatar))
                    .into(profile_image);
            Log.v("contentUri", "" + contentUri);
            getActivity().sendBroadcast(mediaScanIntent);
        } catch (Exception ex) {
            String msg = ex.getMessage().toString();
            Log.v("galleryAddPic", msg);
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {

            case 100:
                if (requestCode == 100) {
                    img_List.clear();
                    try {
                        if (requestCode == MY_CAMERA_REQUEST_CODE) {
                            photo = (Bitmap) imageReturnedIntent.getExtras().get("data");
                            Log.v("photoMM", "" + photo);
                            // profile_image.setImageBitmap(photo);
                            UploadImg();

                            Glide.with(getContext())
                                    .load(photo)
                                    .apply(new RequestOptions().placeholder(R.drawable.avatar).error(R.drawable.avatar))
                                    .into(profile_image);

                        }

                        Log.v("selectedMediaUri", "" + selectedMediaUri);
                    } catch (Exception ex) {
                        //Intent in = new Intent(getContext(), ThankyouActivity.class);
                        Log.v("cameraError", ex.getMessage().toString());

                        galleryAddPic();
                        Log.v("galleryAddPicMain", "galleryAddPic");


                    }
                }

                break;
            case 101:
                if (requestCode == 101) {
                    try {

                        selectedMediaUri = imageReturnedIntent.getData();
                        img_List.add(selectedMediaUri);

                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                            // Log.d(TAG, String.valueOf(bitmap));


                            Glide.with(getContext())
                                    .load(bitmap)
                                    .apply(new RequestOptions().placeholder(R.drawable.avatar).error(R.drawable.avatar))
                                    .into(profile_image);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    } catch (Exception ex) {
                        String msg = ex.getMessage().toString();
                        Log.v("msg_error", msg);
                    }

                }

                break;
        }

    }

    public void UploadImg() {
        try {
            ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, baos1);
            byte[] imageBytes = baos1.toByteArray();
            String imagePathStr = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), photo, "title", null);
            //  String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            selectedMediaUri = Uri.parse(imagePathStr);
            Log.v("photo", "" + photo);
            profile_image.setImageBitmap(photo);
            Log.v("b", "" + selectedMediaUri);
            Log.v("imagePathStr", "" + imagePathStr);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                // Log.d(TAG, String.valueOf(bitmap));


//                profile_image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            img_List.add(selectedMediaUri);
            String imageString = Arrays.toString(imageBytes);
            if (imageString.equals(null)) {
                encodedImage = "";
            } else {
                encodedImage = imageString;
            }
            //  encodedImage = imageString.replaceAll("\n", "");


//        JSONArray arr = new JSONArray();
//        arr.put("data:image/jpeg;base64," + encodedImage);
        } catch (Exception ex) {
            String msg = ex.getMessage().toString();
            Log.v("UploadImg", msg);
        }

    }

    private void selectImage() {
        try {
            if (ActivityCompat.checkSelfPermission(getActivity(), permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(getActivity(), permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(getActivity(), permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissionsRequired[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissionsRequired[1])
                        || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissionsRequired[2])) {
                    //Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Need Multiple Permissions");
                    builder.setMessage("This app needs Camera and Gallery permissions.");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            ActivityCompat.requestPermissions(getActivity(), permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                        }
                    });
                    builder.setNegativeButton(getString(R.string.Cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else if (permissionStatus.getBoolean(permissionsRequired[0], false)) {
                    //Previously Permission Request was cancelled with 'Dont Ask Again',
                    // Redirect to Settings after showing Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Need Multiple Permissions");
                    builder.setMessage("This app needs Camera and Gallery permissions.");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            sentToSettings = true;
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                            intent.setData(uri);
                            startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                            Toast.makeText(getContext(), "Go to Permissions to Grant  Camera and Gallery", Toast.LENGTH_LONG).show();
                        }
                    });
                    builder.setNegativeButton(getString(R.string.Cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    //just request the permission
                    ActivityCompat.requestPermissions(getActivity(), permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                }


                SharedPreferences.Editor editor = permissionStatus.edit();
                editor.putBoolean(permissionsRequired[0], true);
                editor.commit();
            } else {
                //You already have the permission, just go ahead.
                proceedAfterPermission();
            }
        } catch (Exception ex) {
            Log.v("cv", ex.getMessage().toString());
        }
    }

    private void Update_Profile_api() {

        if (img_List.size() > 0) {
            dialogUpdateProfile = new ACProgressFlower.Builder(getActivity())
                    .text("Uploading image...")
                    .textSize(24)
                    .build();
            dialogUpdateProfile.setCanceledOnTouchOutside(false);
            dialogUpdateProfile.show();
        } else {
            dialogUpdateProfile = new ACProgressFlower.Builder(getActivity())
                    .build();
            dialogUpdateProfile.setCanceledOnTouchOutside(false);
            dialogUpdateProfile.show();
        }


        //getting the tag from the edittext
        name = edt_Name.getText().toString();
        email = edt_Email.getText().toString();
        phone = edt_Phone.getText().toString();


        JSON_URL_UPDATE_PROFILE = utilities.GetUrl() + "accounts/update?";
        Log.v("login_token1", login_token);
        Log.v("JSON_URL_UPDATE_PROFILE", JSON_URL_UPDATE_PROFILE);
        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, JSON_URL_UPDATE_PROFILE,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        dialogUpdateProfile.dismiss();
                        try {
                            SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            JSONObject obj = new JSONObject(new String(response.data));
                            String msg = obj.getString("msg");

                            String success = obj.getString("success");
                            if (success.equals("1")) {

                                Log.v("msg_attachment", msg);
                                String profile_img_str = obj.getString("profile-img");
                                editor.putString("profile_pic", profile_img_str);
                                editor.putString("customer_name", name);

                                editor.commit();
                                img_List.clear();

//                                Glide.with(profile_image.getContext()).load(profile_img_str).placeholder(R.drawable.avatar)
//                                        .dontAnimate().into(profile_image);

                               /* profile_image.setImageResource(0);
                                Glide.with(profile_image.getContext())
                                        .load(profile_img_str)
                                        .apply(new RequestOptions().placeholder(R.drawable.avatar).error(R.drawable.avatar))
                                        .into(profile_image);*/
                                ((MainActivity) getActivity()).updateNavHeaderView();


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                        dialogUpdateProfile.dismiss();
                    }
                }) {

            /*
             * If you want to add more parameters with the image
             * you can do it here
             * here we have only one parameter with the image
             * which is tags
             * */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("type", "details");
                params.put("name", name);
                params.put("phone", phone);
                params.put("email", email);
                return params;
            }


            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                for (int i = 0; i < img_List.size(); i++) {
                    InputStream iStream = null;
                    try {
                        Log.v("msg_att", "");
                        iStream = getActivity().getContentResolver().openInputStream(img_List.get(i));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }


                    params.put("profile", new DataPart("file_avatar.jpg",
                            getFileDataFromDrawable(getActivity(), img_List.get(i))));
                    // params.put("images[" + i + "]", new DataPart("image" + i + ".jpg", inputData, "image/jpeg"));


                }


                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("userToken", login_token);
                headers.put("lang", setApplicationlanguage);

                return headers;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(getActivity()).add(volleyMultipartRequest);
    }


    public byte[] getFileDataFromDrawable(Context context, Uri uri) {
        String sd = uri.toString();
        Log.v("sd", sd);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Log.v("image", "" + uri.toString().contains("image"));
        if (uri.toString().contains("image")) {

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();


        }


        return byteArrayOutputStream.toByteArray();
    }

    private void loadProfile_api() {
        //creating a string request to send request to the url
        login_token = preferences.getString("login_token", "");
        final ACProgressFlower dialog = new ACProgressFlower.Builder(getActivity())
                .build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL_PROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();

                        try {

                            //converting response to json object
                            JSONObject obj = new JSONObject(response);


                            String status = obj.getString("status");
                            if (status.equals("200")) {


                                JSONObject objDetails = obj.getJSONObject("customer");
                                String id = objDetails.getString("id");

                                name = objDetails.getString("name");
                                email = objDetails.getString("email");
                                phone = objDetails.getString("phone");
                                str_user_pic = objDetails.getString("profile_pic");
                                String k = str_user_pic;
                                if (email != null && !email.isEmpty() && !email.equals("null")) {
                                    edt_Email.setText(email);
                                }
                                if (name != null && !name.isEmpty() && !name.equals("null")) {
                                    edt_Name.setText(name);
                                }
                                if (phone != null && !phone.isEmpty() && !phone.equals("null")) {
                                    phone = phone.replace("+", "");
                                    String hj = phone;
                                    Log.v("setlanguage", selectLang);
                                    if (selectLang.equals("en")) {

                                        edt_Phone.setText("+" + phone);

                                    } else {
                                        if (Build.VERSION.SDK_INT > 26) {
                                            // Call some material design APIs here
                                            edt_Phone.setText("+"+phone);

                                        } else {
                                            edt_Phone.setText(phone+"+");
                                        }


                                    }


                                }

                                SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString("profile_pic", str_user_pic);
                                editor.commit();
//                                    Glide.with(getContext())
//                                            .load(str_user_pic)
//                                            .into(profile_image);


                                Glide.with(getContext())
                                        .load(str_user_pic)
                                        .apply(new RequestOptions().placeholder(R.drawable.avatar).error(R.drawable.avatar))
                                        .into(profile_image);

                                ((MainActivity) getActivity()).updateNavHeaderView();

                                Log.v("prof_pic_main22", str_user_pic);


                            } else {
                                Toast.makeText(getContext(), "Server error", Toast.LENGTH_SHORT).show();


                            }

                        } catch (JSONException e) {
                            dialog.dismiss();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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

    /**
     * Validating form
     */
    private void submitForm() {

        if (!validateName()) {
            return;
        }

        if (!validatePhone()) {
            return;
        }

        if (!validateEmail()) {
            return;
        }

        Update_Profile_api();
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
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

    private boolean validateName() {
        if (edt_Name.getText().toString().trim().isEmpty()) {
            edt_Name.setError(getString(R.string.Enter_a_valid_name));
            requestFocus(edt_Name);
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

        if (edt_Phone.getText().toString().trim().length() < 6 || edt_Phone.getText().toString().trim().length() > 13) {
            // if(phone.length() != 10) {
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


    private void proceedAfterPermission() {

        // Toast.makeText(getContext(), "We got All Permissions", Toast.LENGTH_LONG).show();
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                //  boolean result=Utility.checkPermission(getActivity());
                if (items[item].equals("Take Photo")) {

                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, MY_CAMERA_REQUEST_CODE);

                } else if (items[item].equals("Choose from Library")) {

                    final Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    galleryIntent.setType("image/*");
                    galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), MY_GALLERY_REQUEST_CODE);
                    // startActivityForResult(galleryIntent, MY_GALLERY_REQUEST_CODE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }


            if (allgranted) {
                proceedAfterPermission();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Need Storage Permission");
                builder.setMessage("This app needs phone permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                Toast.makeText(getActivity(), "Unable to get Permission", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();


        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }
}