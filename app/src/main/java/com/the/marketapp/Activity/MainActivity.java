package com.the.marketapp.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.the.marketapp.Fragment.DeliveryTimeFragment;
import com.the.marketapp.Fragment.NoInternet;
import com.the.marketapp.Fragment.PrivacyPolicyFragment;
import com.the.marketapp.Fragment.ShoppingListPopUpFragment;
import com.the.marketapp.Other.NetworkChangeReceiver;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.crashlytics.android.Crashlytics;
import com.the.marketapp.Fragment.Contact_Fragment;
import com.the.marketapp.Fragment.FAQ_Fragment;
import com.the.marketapp.Fragment.FavoriteFragment;
import com.the.marketapp.Fragment.Home_Fragment;
import com.the.marketapp.Fragment.NotificationFragment;
import com.the.marketapp.Fragment.Order_History_Fragment;
import com.the.marketapp.Fragment.SearchFragment;
import com.the.marketapp.Fragment.SuggestItemFragment;
import com.the.marketapp.Fragment.Your_Info_Fragment;
import com.the.marketapp.Model.Cart_Item_Model;
import com.the.marketapp.Other.Utilities;
import com.the.marketapp.R;
import com.the.marketapp.Fragment.Settings_Fragment;
import com.the.marketapp.Fragment.Shop_List_Fragment;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;

import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;

import static com.the.marketapp.Adapter.Item_Main_Adapter.MY_PREFS_NAME;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {

    View view;
    int logedIn;
    Context context;
    Utilities utilities;
    TextView txtCartCount;
    static FragmentManager fm;
    SharedPreferences preferences;
    LinearLayout nav_head_profile;
    public static Toolbar toolbar;
    TextView prof_name, txt_logout;
    ImageView profile_image_header;
    static MainActivity mainActivity;
    public static DrawerLayout drawer;
    String str_user_name, str_user_pic;
    private boolean isConnected = false;
    static NavigationView navigationView;
    String JSON_URL_LIST_CART, login_token;
    private NetworkChangeReceiver receiver;
    public static ActionBarDrawerToggle toggle;
    public static BottomNavigationView navigation;
    ArrayList<Cart_Item_Model> cart_item_ModelList = new ArrayList<Cart_Item_Model>();
    private static final String TAG = MainActivity.class.getSimpleName();
    String fragmentFromSingle, fromSingleItemActivity, fromSignUpFragment,fromMapsActivity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivity = MainActivity.this;
        Hawk.init(getApplicationContext())
                .setEncryptionMethod(HawkBuilder.EncryptionMethod.MEDIUM)
                .setStorage(HawkBuilder.newSqliteStorage(getApplicationContext()))
                .setLogLevel(LogLevel.FULL)
                .build();
        Hawk.put("pages", "home");
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);

//        Hawk.put("pages", "cart");
//        try {
//            if (utilities.isOnline(this)) {
                Fabric.with(this, new Crashlytics());
                setContentView(R.layout.activity_main);

                view = getWindow().getDecorView();

                toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                //toolbar.bringtoFront();
                toolbar.bringToFront();
                //  img_bar_icon = (ImageView) findViewById(R.id.img_bar_icon);
                //  img_cart = (ImageView) findViewById(R.id.img_cart);
                // txt_actionbar_Title = (TextView) findViewById(R.id.txt_actionbar_Title);
//            txt_actionbar_Title.setVisibility(View.GONE);
//            img_cart.setVisibility(View.GONE);
//            img_bar_icon.setVisibility(View.GONE);
                getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                getSupportActionBar().setCustomView(R.layout.actionbar_layout);
                getSupportActionBar().setCustomView(getLayoutInflater().inflate(R.layout.actionbar_layout, null),
                        new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));
                TextView txt_actionbar_Title = (TextView) findViewById(R.id.txt_actionbar_Title);
                ImageView img_cart = (ImageView) findViewById(R.id.img_cart_actionbar);
                img_cart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent in = new Intent(MainActivity.this, CartActivity.class);
                        startActivity(in);
                    }
                });

                txtCartCount = (TextView) findViewById(R.id.txtCartCount);
//            txt_actionbar_Title.setText("MARKET");

                final Fragment fragment = new Fragment();
                img_cart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Intent in = new Intent(MainActivity.this, CartActivity.class);
                        startActivity(in);
                    }
                });
                preferences = getSharedPreferences("mypreferences", Context.MODE_PRIVATE);


//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        //**Hiding title from action bar**
//        getSupportActionBar().setDisplayShowTitleEnabled(true);
//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(R.layout.abs_layout);


                drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                toggle = new ActionBarDrawerToggle(
                        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                toggle.setDrawerIndicatorEnabled(false);
                toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        drawer.openDrawer(GravityCompat.START);
                    }
                });
                // toggle.setHomeAsUpIndicator(R.mipmap.menu_white);
                drawer.addDrawerListener(toggle);
                toggle.syncState();


                 navigationView = (NavigationView) findViewById(R.id.nav_view);
                navigationView.setNavigationItemSelectedListener(this);
                View headerview = navigationView.getHeaderView(0);

                //getting bottom navigation view and attaching the listener
                navigation = findViewById(R.id.navigation);
                navigation.setOnNavigationItemSelectedListener(this);

                preferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                 logedIn = preferences.getInt("logedIn", 0);
                str_user_name = preferences.getString("customer_name", "");
                str_user_pic = preferences.getString("profile_pic", "");
                // str_user_pic = "http://market.whyte.company/storage/app/" + str_user_pic;
//                Log.v("prof_pic_main", str_user_pic);


                nav_head_profile = (LinearLayout) headerview.findViewById(R.id.nav_head_profile);
                prof_name = (TextView) headerview.findViewById(R.id.prof_name);
                txt_logout = (TextView) headerview.findViewById(R.id.txt_logout);
                profile_image_header = (ImageView) headerview.findViewById(R.id.profile_image_header);
                prof_name.setText(str_user_name);


//                Glide
//                        .with(this)
//                        .load(str_user_pic)
//                        .centerCrop()
//                        .placeholder(R.drawable.avatar)
//                        .into(profile_image_header);
                updateNavHeaderView();


//            Glide.with(this)
//                    .load(str_user_pic)
//                    .into(profile_image_header);

                Log.v("prof_pic_main22", str_user_pic);

                nav_head_profile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        if (drawer.isDrawerOpen(GravityCompat.START)) {
                            drawer.closeDrawer(GravityCompat.START);
                        }
                        if(logedIn==0)
                        {
                            Intent intent=new Intent(MainActivity.this,LogInActivity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Fragment fragment = new Your_Info_Fragment();
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_container, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }

                    }
                });


                txt_logout = (TextView) headerview.findViewById(R.id.txt_logout);
                SharedPreferences sharedpreferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                final SharedPreferences.Editor editor = sharedpreferences.edit();
                if (logedIn == 0) {
                    txt_logout.setText(getString(R.string.LogIn));
                } else {
                    txt_logout.setText(getString(R.string.Logout));
                }
                txt_logout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (txt_logout.getText().toString().equals(getString(R.string.LogIn))) {
                            Intent in = new Intent(MainActivity.this, LogInActivity.class);
                            startActivity(in);
                        } else {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(
                                    MainActivity.this);

                            builder.setTitle(getString(R.string.Logout));
                            builder.setMessage(getString(R.string.Are_you_sure_you_want_to_logout));
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
                                            prof_name.setText("");
                                            editor.putInt("logedIn", 0);
                                            editor.putString("login_token", "");
                                            editor.putString("customer_name", "");
                                            editor.putString("profile_pic", "");
                                            editor.putString("setApplicationlanguage", "");
                                            editor.commit();
                                            Log.v("ok", "ok");
                                            Intent in = new Intent(MainActivity.this, MainActivity.class);
                                            startActivity(in);
                                        }
                                    });


                            builder.show();


                        }


                    }
                });


                //to go to Address fragment
                Intent mIntent = getIntent();
                int intValue = mIntent.getIntExtra("add_adrz", 0);
                String notificationId = preferences.getString("notificationId", "0");
                notificationId = mIntent.getStringExtra("notificationId");
                String myReceiptFragment = preferences.getString("myReceiptFragment", "0");
                String myReceiptFragmentID = preferences.getString("orderHistoryID", "0");
//                if (intValue == 1) {
//
//                    loadFragment(new Address_Book_Fragment());
//                } else if (notificationId.equals("1")) {
//
//                    editor.putString("notificationId", "0");
//                    editor.commit();
//                    loadFragment(new SingleOrderHistoryFragment());
//                } else {
//
//                    loadFragment(new Home_Fragment());
//                }

                fragmentFromSingle = getIntent().getStringExtra("fragmentFromSingle");
                fromSingleItemActivity = getIntent().getStringExtra("fromSingleItemActivity");
                fromSignUpFragment = getIntent().getStringExtra("fromSignUpFragment");

        fromMapsActivity = getIntent().getStringExtra("fromMapsActivity");
        if (fromMapsActivity != null && !fromMapsActivity.isEmpty() && !fromMapsActivity.equals("null")) {
            if (fromMapsActivity.equals("fromMapsActivity")) {
                loadFragment(new PrivacyPolicyFragment());
            }

        }
        else if (fromSignUpFragment != null && !fromSignUpFragment.isEmpty() && !fromSignUpFragment.equals("null")) {
            if (fromSignUpFragment.equals("fromSignUpFragment")) {
                loadFragment(new PrivacyPolicyFragment());
            }

        } else if (fragmentFromSingle != null && !fragmentFromSingle.isEmpty() && !fragmentFromSingle.equals("null")) {
            if (fragmentFromSingle.contains("SearchFragment")) {
                loadFragment(new SearchFragment());
            } else if (fragmentFromSingle.contains("FavoriteFragment")) {
                loadFragment(new FavoriteFragment());
            } else if (fragmentFromSingle.contains("NotificationFragment")) {
                loadFragment(new NotificationFragment());
            } else if (fragmentFromSingle.contains("Home_Fragment")) {
                loadFragment(new Home_Fragment());
            }

        } else if (fromSingleItemActivity != null && !fromSingleItemActivity.isEmpty() && !fromSingleItemActivity.equals("null")) {
            loadFragment(new ShoppingListPopUpFragment());
        } else {
            loadFragment(new Home_Fragment());
        }



//            } else {
//                setContentView(R.layout.msg_no_internet);
//            }
//
//
//        } catch (Exception ex) {
//            String msg = ex.getMessage().toString();
//            String m = msg;
//        }
    }

    //    @Override
//    public void onBackPressed() {
//        if (mNavigationDrawerFragment.isDrawerOpen())
//            mNavigationDrawerFragment.closeDrawer();
//        else if (getFragmentManager().getBackStackEntryCount()>0)
//            getFragmentManager().popBackStack();
//        else
//            super.onBackPressed();
//    }


    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        //   displaySelectedScreen(item.getItemId());
        try {
            switch (item.getItemId()) {

                // Bottom  Menu
                case R.id.navigation_home:
                    fragment = new Home_Fragment();
                    // item.setIcon(R.drawable.child_selector);
//                    navigationView.setCheckedItem(R.id.navigation_home);
                    navigation.getMenu().getItem(0).setCheckable(true);

                    navigationView.setCheckedItem(R.id.nav_shop);
                    item.setChecked(false);
                    navigationView.getMenu().getItem(0).setChecked(true);

                    break;

                case R.id.navigation_search:
                    fragment = new SearchFragment();
                    navigation.getMenu().getItem(1).setCheckable(true);
                    navigationView.getMenu().getItem(0).setChecked(false);
                    navigationView.getMenu().getItem(1).setChecked(false);
                    navigationView.getMenu().getItem(2).setChecked(false);
                    navigationView.getMenu().getItem(3).setChecked(false);
                    navigationView.getMenu().getItem(4).setChecked(false);
                    navigationView.getMenu().getItem(5).setChecked(false);
                    navigationView.getMenu().getItem(6).setChecked(false);
                    navigationView.getMenu().getItem(7).setChecked(false);
                    navigationView.getMenu().getItem(8).setChecked(false);
                    navigationView.getMenu().getItem(9).setChecked(false);
                    navigationView.getMenu().getItem(10).setChecked(false);
                    break;

                case R.id.navigation_favorite:
                    if (logedIn == 1) {
                        fragment = new FavoriteFragment();
//                        item.setIcon(R.drawable.child_selector);
                    } else {
                        LogIn();
                    }
                    navigation.getMenu().getItem(2).setCheckable(true);

                    navigationView.getMenu().getItem(0).setChecked(false);
                    navigationView.getMenu().getItem(1).setChecked(false);
                    navigationView.getMenu().getItem(2).setChecked(false);
                    navigationView.getMenu().getItem(3).setChecked(false);
                    navigationView.getMenu().getItem(4).setChecked(false);
                    navigationView.getMenu().getItem(5).setChecked(false);
                    navigationView.getMenu().getItem(6).setChecked(false);
                    navigationView.getMenu().getItem(7).setChecked(false);
                    navigationView.getMenu().getItem(8).setChecked(false);
                    navigationView.getMenu().getItem(9).setChecked(false);
                    navigationView.getMenu().getItem(10).setChecked(false);
//                    navigationView.setCheckedItem(R.id.navigation_favorite);
                    //     fragment = new FavoriteFragment();

                    break;

                case R.id.navigation_notifications:
                    if (logedIn == 1) {
                        fragment = new NotificationFragment();
                    } else {
                        LogIn();
                    }
                    navigation.getMenu().getItem(3).setCheckable(true);

                    navigationView.getMenu().getItem(0).setChecked(false);
                    navigationView.getMenu().getItem(1).setChecked(false);
                    navigationView.getMenu().getItem(2).setChecked(false);
                    navigationView.getMenu().getItem(3).setChecked(false);
                    navigationView.getMenu().getItem(4).setChecked(false);
                    navigationView.getMenu().getItem(5).setChecked(false);
                    navigationView.getMenu().getItem(6).setChecked(false);
                    navigationView.getMenu().getItem(7).setChecked(false);
                    navigationView.getMenu().getItem(8).setChecked(false);
                    navigationView.getMenu().getItem(9).setChecked(false);
                    navigationView.getMenu().getItem(10).setChecked(false);

                    break;

// Side Menu

                case R.id.nav_shop:
                    fragment = new Home_Fragment();

                    navigationView.getMenu().getItem(0).setCheckable(true);

                    navigation.getMenu().getItem(0).setCheckable(true);
                    navigation.getMenu().getItem(1).setCheckable(false);
                    navigation.getMenu().getItem(2).setCheckable(false);
                    navigation.getMenu().getItem(3).setCheckable(false);


                    break;

                case R.id.nav_orderhistory:
                    navigationView.getMenu().getItem(1).setCheckable(true);
                    if (logedIn == 1) {
                        fragment = new Order_History_Fragment();
                    } else {
                        LogIn();
                    }
                    navigation.getMenu().getItem(0).setCheckable(false);
                    navigation.getMenu().getItem(1).setCheckable(false);
                    navigation.getMenu().getItem(2).setCheckable(false);
                    navigation.getMenu().getItem(3).setCheckable(false);
//                    navigation.getMenu().getItem(0).setChecked(false);
//                    R.id.nav_orderhistory.setChecked(true);

                    navigationView.setCheckedItem(R.id.nav_orderhistory);
                    //   fragment = new Order_History_Fragment();

                    break;

                case R.id.nav_shoppinglist:
                    navigationView.getMenu().getItem(2).setCheckable(true);
                    if (logedIn == 1) {
                        fragment = new Shop_List_Fragment();
                    } else {
                        LogIn();
                    }
                    //   fragment = new Shop_List_Fragment();
                    navigationView.setCheckedItem(R.id.nav_shoppinglist);
                    navigation.getMenu().getItem(0).setCheckable(false);
                    navigation.getMenu().getItem(1).setCheckable(false);
                    navigation.getMenu().getItem(2).setCheckable(false);
                    navigation.getMenu().getItem(3).setCheckable(false);
                    break;


                case R.id.nav_settings:
                    navigationView.getMenu().getItem(3).setCheckable(true);
                    if (logedIn == 1) {
                        fragment = new Settings_Fragment();
                    } else {
                        LogIn();
                    }
                    navigationView.setCheckedItem(R.id.nav_settings);
                    navigation.getMenu().getItem(0).setCheckable(false);
                    navigation.getMenu().getItem(1).setCheckable(false);
                    navigation.getMenu().getItem(2).setCheckable(false);
                    navigation.getMenu().getItem(3).setCheckable(false);

                    break;
                case R.id.nav_suggestitem:
                    navigationView.getMenu().getItem(4).setCheckable(true);
                    fragment = new SuggestItemFragment();
                    navigation.getMenu().getItem(0).setCheckable(false);
                    navigation.getMenu().getItem(1).setCheckable(false);
                    navigation.getMenu().getItem(2).setCheckable(false);
                    navigation.getMenu().getItem(3).setCheckable(false);

                    break;
                case R.id.nav_changelang:
                    navigationView.getMenu().getItem(5).setCheckable(true);
                    Intent intent = new Intent(MainActivity.this, Multi_Lang_Activity.class);
                    intent.putExtra("from","MainActivity");
                    startActivity(intent);
                    navigation.getMenu().getItem(0).setCheckable(false);
                    navigation.getMenu().getItem(1).setCheckable(false);
                    navigation.getMenu().getItem(2).setCheckable(false);
                    navigation.getMenu().getItem(3).setCheckable(false);

                    break;
                case R.id.nav_contact:
                    navigationView.getMenu().getItem(6).setCheckable(true);
                    fragment = new Contact_Fragment();
                    navigation.getMenu().getItem(0).setCheckable(false);
                    navigation.getMenu().getItem(1).setCheckable(false);
                    navigation.getMenu().getItem(2).setCheckable(false);
                    navigation.getMenu().getItem(3).setCheckable(false);

                    break;
                case R.id.nav_FAQ:
                    navigationView.getMenu().getItem(7).setCheckable(true);
                    fragment = new FAQ_Fragment();

                    navigation.getMenu().getItem(0).setCheckable(false);
                    navigation.getMenu().getItem(1).setCheckable(false);
                    navigation.getMenu().getItem(2).setCheckable(false);
                    navigation.getMenu().getItem(3).setCheckable(false);
                    break;


                case R.id.nav_share:
                    navigationView.getMenu().getItem(8).setCheckable(true);
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBody = "https://www.themarket.qa/api/appview";
                    Log.v("shareBody", shareBody);
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Welcome to The Market.");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));
                    navigation.getMenu().getItem(0).setCheckable(false);
                    navigation.getMenu().getItem(1).setCheckable(false);
                    navigation.getMenu().getItem(2).setCheckable(false);
                    navigation.getMenu().getItem(3).setCheckable(false);

                    break;
                case R.id.nav_TC:
                    navigationView.getMenu().getItem(9).setCheckable(true);
                    fragment = new PrivacyPolicyFragment();
                    navigation.getMenu().getItem(0).setCheckable(false);
                    navigation.getMenu().getItem(1).setCheckable(false);
                    navigation.getMenu().getItem(2).setCheckable(false);
                    navigation.getMenu().getItem(3).setCheckable(false);
                    break;

                case R.id.nav_DeliveryTime:
                    navigationView.getMenu().getItem(10).setCheckable(true);
                    fragment = new DeliveryTimeFragment();
                    navigation.getMenu().getItem(0).setCheckable(false);
                    navigation.getMenu().getItem(1).setCheckable(false);
                    navigation.getMenu().getItem(2).setCheckable(false);
                    navigation.getMenu().getItem(3).setCheckable(false);
                    break;


//                case R.id.nav_s:
//                    fragment = new SubMenu_Fragment();
//                    break;


            }
        } catch (Exception ex) {
            String msg = ex.getMessage().toString();
            String h = msg;
        }

        return loadFragment(fragment);
    }

    public void LogIn() {
        Intent intent = new Intent(MainActivity.this, LogInActivity.class);
        startActivity(intent);
    }
    public static boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
//            mainActivity.getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.fragment_container, fragment)
//                    .commit();

//            Fragment fragment = new Home();
             fm = mainActivity.getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.replace(R.id.fragment_container, fragment).commitAllowingStateLoss();

            DrawerLayout drawer =  mainActivity.findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
        return false;
    }

    public void updateNavHeaderView() {
        try {
            str_user_name = preferences.getString("customer_name", "");
            str_user_pic = preferences.getString("profile_pic", "");
            if (str_user_pic != null && !str_user_pic.isEmpty() && !str_user_pic.equals("")) {

//                Glide.with(profile_image_header.getContext()).load(str_user_pic).placeholder(R.drawable.avatar)
//                        .dontAnimate().into(profile_image_header);

                Glide.with(MainActivity.this)
                        .load(str_user_pic)
                        .apply(new RequestOptions().placeholder(R.drawable.avatar).error(R.drawable.avatar))
                        .into(profile_image_header);

                prof_name.setText(str_user_name);
                if (logedIn == 0) {
                    txt_logout.setText(getString(R.string.LogIn));
                } else {
                    txt_logout.setText(getString(R.string.Logout));
                }



            }
        } catch (Exception ex) {
            Log.v("updateNavHeaderView", ex.getMessage().toString());
        }
    }

    @Override
    public void onBackPressed() {
        Fragment fragment_back = mainActivity.getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        if (drawer.isDrawerOpen(Gravity.START)) {
            drawer.closeDrawer(Gravity.START);
        }
        else if (drawer.isDrawerOpen(Gravity.START)) {
            drawer.closeDrawer(Gravity.START);
        }
        else if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        }
        else if (fragment_back instanceof Home_Fragment) {
            moveTaskToBack(true);
        }
        else if (fragment_back instanceof PrivacyPolicyFragment) {
            super.onBackPressed();
        }
        else if (Hawk.get("pages" ).equals("mapActivity")) {
            Intent in = new Intent(MainActivity.this, CartActivity.class);
            startActivity(in);
        }
        else if (Hawk.get("pages").equals("ShopListPopUp")){
//                Hawk.put("pages", "sub_menu");
//                fm.popBackStack();
            super.onBackPressed();
            }
        else if (fragment_back==null){
                Hawk.put("pages", "home");
//                fm.popBackStack();
            Intent main = new Intent(MainActivity.this,MainActivity.class);
            startActivity(main);
            }


        else {
//            super.onBackPressed();
//            loadFragment(new Home_Fragment());
            fm.popBackStack();
        }
    }


    public static void no_internet(boolean status){
        Fragment fragment_back = mainActivity.getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        if (status  ){
            if (fragment_back instanceof NoInternet) {
                navigation.getMenu().getItem(0).setCheckable(true);

                navigationView.setCheckedItem(R.id.nav_shop);
                loadFragment(new Home_Fragment());
            }
//
              else  if (Hawk.get("pages").equals("cart")){
//
                    CartActivity.NoInternet.setVisibility(View.GONE);
//                    CartActivity.linearCartNotVisible.setVisibility(View.GONE);
//                    CartActivity.NoItem.setText("");
                }
              else if (Hawk.get("pages").equals("address")){

                  AddAddressActivity.NoInternet.setVisibility(View.GONE);

            }
              else if (Hawk.get("pages").equals("single_item")){

                  SingleItemActivity.NoInternet.setVisibility(View.GONE);

            }
//              else if (Hawk.get("pages").equals("ShopListPopUp")){
//                Hawk.put("pages", "sub_menu");
//                fm.popBackStack();
//
//            }




        }
        else{
//

            if (Hawk.get("pages").equals("cart")){
//
                CartActivity.NoInternet.setVisibility(View.VISIBLE);
            }
            else if (Hawk.get("pages").equals("address")){

                AddAddressActivity.NoInternet.setVisibility(View.VISIBLE);

            }
            else if (Hawk.get("pages").equals("single_item")){

                SingleItemActivity.NoInternet.setVisibility(View.VISIBLE);

            }
            else {
                loadFragment(new NoInternet());
            }
        }
//
    }

//    prajith commit : tested Ok






}