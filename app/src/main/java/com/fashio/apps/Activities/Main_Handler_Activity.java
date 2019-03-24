package com.fashio.apps.Activities;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fashio.apps.Applications.AppController;
import com.fashio.apps.Drawable.BadgeDrawable;
import com.fashio.apps.Fragments.Fragment_Home;
import com.fashio.apps.Fragments.Fragment_Loved;
import com.fashio.apps.Fragments.Fragment_Query;
import com.fashio.apps.Fragments.Fragment_Refresh;
import com.fashio.apps.Notifications.Config;
import com.fashio.apps.Notifications.NotificationUtils;
import com.fashio.apps.R;
import com.fashio.apps.Session.Contact_Session;
import com.fashio.apps.Session.Customer_Session;
import com.fashio.apps.Session.Name_Session;
import com.fashio.apps.Session.Phone_Session;
import com.fashio.apps.ViewPager.CustomViewPager;
import com.google.android.gms.analytics.Tracker;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.messaging.FirebaseMessaging;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class Main_Handler_Activity extends AppCompatActivity implements Fragment_Home.onDataLoadListener ,Fragment_Refresh.onDataLoadListener {

    private static final String CART_URL= "https://apps.itrifid.com/fashio/rest_server/selectUserCart/API-KEY/123456";
    private static final String KEY_C_ID = "res_id";
    private Phone_Session phone_session;
    private Name_Session name_session;
    private BottomNavigationView bottom_navigation;
    public static CustomViewPager viewPager;
    MenuItem prevMenuItem;
    private Toolbar toolbar;
    public NestedScrollView nestedScrollView2;
    public RelativeLayout catalogue_layout;
    private TextView toolbar_title;
    private FloatingActionButton floatingActionButton;
//    private ListView listView;
//    private DrawerLayout drawerLayout;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private static final String TAG = Main_Handler_Activity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    int id =0;
    //Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    int id_=0;
    private ProgressBar fragment_bar;
    private LinearLayout lin_cat;
    private LinearLayout lin_frag;
    private NestedScrollView nestedScrollView;
    private NestedScrollView nestedScrollView1;
    public String wp_no="";
    public String wp_text="";
    public String wp_b_no="";
    public String wp_b_text="";
    private TextView cs_id;
    private Customer_Session customer_session;
    private Tracker tracker;
    private Contact_Session contact_session;
    private TextView user_name;
    private TextView phone;
    private LinearLayout cart_layout;
    private LinearLayout wallet_layout;
    private LinearLayout help_layout;
    private LinearLayout payment_details_layout;
    private LinearLayout my_orders_layout;
    private DrawerLayout drawerLayout;
    private String cart_count="";
    private LinearLayout ranking_layout;
    private LinearLayout terms_layout;
    private LinearLayout about_layout;
    private LinearLayout partner_layout;
    private LinearLayout website_layout;
    private ImageView fb;
    private ImageView twitter;
    private ImageView insta;
    private ImageView utube;


//    private PublisherInterstitialAd mPublisherInterstitialAd;
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verifyStoragePermissions(this);

        customer_session = new Customer_Session(getApplicationContext());
        contact_session=new Contact_Session(getApplicationContext());

//        mPublisherInterstitialAd = new PublisherInterstitialAd(this);
//        mPublisherInterstitialAd.setAdUnitId("ca-app-pub-8846572999899891/5897251835");
//        mPublisherInterstitialAd.loadAd(new PublisherAdRequest.Builder().build());

//        mPublisherInterstitialAd.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//            public void onAdClosed() {
//                // Load the next interstitial.
//                mPublisherInterstitialAd.loadAd(new PublisherAdRequest.Builder().build());
//            }
//
//        });

        fb = findViewById(R.id.fb_);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.facebook.com/fasio.apps"));
                i.setPackage("com.android.chrome");
                startActivity(i);
            }
        });

        twitter = findViewById(R.id.twitter);
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://twitter.com/Fasio_app"));
                i.setPackage("com.android.chrome");
                startActivity(i);
            }
        });


        insta = findViewById(R.id.insta);
        insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.instagram.com/fasio.app/"));
                i.setPackage("com.android.chrome");
                startActivity(i);
            }
        });

        utube = findViewById(R.id.utube);
        utube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.youtube.com/channel/UCpKVIir8EWji-w47Qe9I6cw"));
                i.setPackage("com.android.chrome");
                startActivity(i);
            }
        });

        about_layout = findViewById(R.id.about_layout);
        about_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Help_Activity.class);
                intent.putExtra("url","https://apps.itrifid.com/fashio/about.php");
                intent.putExtra("title","About Us");
                startActivity(intent);
            }
        });

        website_layout = findViewById(R.id.website_layout);
        website_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.fasio.app"));
                i.setPackage("com.android.chrome");
                startActivity(i);
            }
        });

        terms_layout = findViewById(R.id.terms_layout);
        terms_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Help_Activity.class);
                intent.putExtra("url","https://apps.itrifid.com/fashio/contact.php");
                intent.putExtra("title","Contact Us");
                startActivity(intent);
            }
        });

        partner_layout = findViewById(R.id.partner_layout);
        partner_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Activity_Partner.class));
            }
        });

        Typeface tf1 = Typeface.createFromAsset(getAssets(), "fonts/arial.ttf");

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        FirebaseMessaging.getInstance().subscribeToTopic("global");

        displayFirebaseRegId();

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);


                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }
            }
        };

        phone_session=new Phone_Session(getApplicationContext());
        name_session = new Name_Session(getApplicationContext());

        user_name = findViewById(R.id.user_name);
        user_name.setText(name_session.getName());

        phone = findViewById(R.id.email);
        phone.setText("+91 "+phone_session.getPhoneNO());

        cs_id = findViewById(R.id.cs_id);
        cs_id.setText("Customer ID - "+customer_session.getCustomerID());

        wallet_layout= findViewById(R.id.wallet_layout);
        wallet_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),My_Wallet.class));
            }
        });

        my_orders_layout= findViewById(R.id.my_orders_layout);
        my_orders_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),My_Orders.class));
            }
        });

        drawerLayout = findViewById(R.id.drawer_layout);

        cart_layout = findViewById(R.id.cart_layout);
        cart_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Activity_Cart.class));
            }
        });

        help_layout = findViewById(R.id.help_layout);
        help_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Help_Activity.class);
                intent.putExtra("url","https://apps.itrifid.com/fashio/help.php");
                intent.putExtra("title","Help");
                startActivity(intent);
            }
        });

        payment_details_layout = findViewById(R.id.payment_details_layout);
        payment_details_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Bank_Details.class));
            }
        });

        ranking_layout = findViewById(R.id.ranking_layout);
        ranking_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Seller_Ranckings.class));
            }
        });

        setSupportActionBar(toolbar);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.write_);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Write_Activity.class));
            }
        });

        viewPager = (CustomViewPager) findViewById(R.id.viewpager);
        viewPager.disableScroll(true);

        getSupportActionBar().setElevation(0);

        bottom_navigation = (BottomNavigationView)findViewById(R.id.navigation);
        removeShiftMode(bottom_navigation);

        bottom_navigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.navigation_home:
                                if (id_==0){
                                    //do nothing for now
                                }else {
                                    lin_cat.setVisibility(GONE);
                                    lin_frag.setVisibility(VISIBLE);
                                    fragment_bar.setVisibility(GONE);
                                    nestedScrollView.setVisibility(GONE);
                                    nestedScrollView1.setVisibility(VISIBLE);
                                    id_ =0;
                                }
                                viewPager.setCurrentItem(0);
                                floatingActionButton.setVisibility(GONE);
                                break;

                            case R.id.navigation_trending:
                                viewPager.setCurrentItem(1);
                                floatingActionButton.setVisibility(GONE);
                                break;

                            case R.id.navigation_loved:
                                viewPager.setCurrentItem(2);
                                floatingActionButton.setVisibility(GONE);
                                break;


                            case R.id.navigation_query:
                                floatingActionButton.setVisibility(GONE);
                                viewPager.setCurrentItem(3);

                                if (whatsappInstalledOrNot("com.whatsapp.w4b") && whatsappInstalledOrNot("com.whatsapp")){
                                    String title = "Choose :";
                                    CharSequence[] itemlist ={"Whatsapp",
                                            "Whatsapp Business",
                                    };

                                    AlertDialog.Builder builder = new AlertDialog.Builder(Main_Handler_Activity.this);
                                    //builder.setIcon(R.drawable.icon_app);
                                    builder.setTitle(title);
                                    builder.setItems(itemlist, new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case 0:// Take Photo
                                                    openWhatsApp();
                                                    break;
                                                case 1:// Choose Existing Photo
                                                    openWhatsAppBusiness();
                                                    break;

                                                default:
                                                    break;
                                            }
                                        }
                                    });
                                    AlertDialog alert = builder.create();
                                    alert.setCancelable(true);
                                    alert.show();

                                }else {
                                    openWhatsApp();
                                }
                                break;
                        }
                        return false;
                    }
                });

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottom_navigation.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationViewBehaviour());

        //        CoordinatorLayout.LayoutParams layoutParams1 = (CoordinatorLayout.LayoutParams) toolbar.getLayoutParams();
//        layoutParams1.setBehavior(new ToolbarViewBehaviour());

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else
                {
                    bottom_navigation.getMenu().getItem(0).setChecked(false);
                }

                bottom_navigation.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottom_navigation.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setTypeface(tf1);

        //Add fragments
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Fragment_Refresh());
        adapter.addFragment(new Fragment_Home());
        adapter.addFragment(new Fragment_Loved());
        adapter.addFragment(new Fragment_Query());

        viewPager.setOffscreenPageLimit(4);

        //Setting adapter
        viewPager.setAdapter(adapter);

        setUpNavDrawer();

        FirebaseMessaging.getInstance().subscribeToTopic(customer_session.getCustomerID());

    }

    void setUpNavDrawer(){
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black));

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }
//
//    void startGame(){
//        mRewardedVideoAd.loadAd(getString(R.string.ad_unit_id), new AdRequest.Builder().build());
//    }


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

        getCartProducts();
//        mRewardedVideoAd.resume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        mRewardedVideoAd.pause(this);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }

    @Override
    public void onSliderLoaded(String w_no, String wtext, String wb_no, String wb_text) {
        wp_no = w_no;
        wp_text = wtext;
        wp_b_no = wb_no;
        wp_b_text = wb_text;
    }

    @Override
    public void requiredDtata(int id, LinearLayout lin1, LinearLayout lin2, ProgressBar p_bar, NestedScrollView sv1, NestedScrollView sv2, NestedScrollView sv3, RelativeLayout catloguye_layout) {
        id_ = id;
        lin_cat = lin1;
        lin_frag = lin2;
        fragment_bar = p_bar;
        nestedScrollView = sv1;
        nestedScrollView1 = sv2;
        nestedScrollView2 = sv3;
        catalogue_layout= catloguye_layout;
    }


    @Override
    public void requiredDtata(int id, LinearLayout lin1, LinearLayout lin2, ProgressBar p_bar, NestedScrollView sv1, NestedScrollView sv2) {
        id_ = id;
        lin_cat = lin1;
        lin_frag = lin2;
        fragment_bar = p_bar;
        nestedScrollView = sv1;
        nestedScrollView1 = sv2;
    }

    class PagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragments = new ArrayList<>();

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment) {
            mFragments.add(fragment);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }


//    private void addContact(String name, String phone) {
//        ContentValues values = new ContentValues();
//        values.put(android.provider.Contacts.People.NUMBER, phone);
//        values.put(android.provider.Contacts.People.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM);
//        values.put(android.provider.Contacts.People.LABEL, name);
//        values.put(android.provider.Contacts.People.NAME, name);
//        Uri dataUri = getContentResolver().insert(android.provider.Contacts.People.CONTENT_URI, values);
//        Uri updateUri = Uri.withAppendedPath(dataUri, android.provider.Contacts.People.Phones.CONTENT_DIRECTORY);
//        values.clear();
//        values.put(android.provider.Contacts.People.Phones.TYPE, android.provider.Contacts.People.TYPE_MOBILE);
//        values.put(android.provider.Contacts.People.NUMBER, phone);
//        updateUri = getContentResolver().insert(updateUri, values);
//    }


    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId))
            //Toast.makeText(getApplicationContext(), "Push Notification Subscribed " +regId, Toast.LENGTH_LONG).show();
            //txtRegId.setText("Firebase Reg Id: " + regId);
            Log.e(TAG, "Push Notification Subscribed: " + regId);
        else
            Toast.makeText(getApplicationContext(), "No Id ", Toast.LENGTH_LONG).show();
    }

    private boolean whatsappInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }


    private void openWhatsApp() {


        String smsNumber = "918877910042";


        if (wp_no.equals("")){
            //do nothing
        }else {
            smsNumber = wp_no;
        }

        String message = "Hi,\n" +":";
        if (wp_text.equals("")){
            //do nithung
        }else {
            message = wp_text;
        }



        boolean isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp");
        if (isWhatsappInstalled) {

            try{

                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, message);
                whatsappIntent.putExtra("jid", smsNumber + "@s.whatsapp.net"); //phone number without "+" prefix
                startActivity(whatsappIntent);
            }catch (ActivityNotFoundException e){
                e.printStackTrace();
            }

        } else {
            try {
                Uri uri = Uri.parse("market://details?id=com.whatsapp");
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                Toast.makeText(getApplicationContext(), "WhatsApp not Installed",
                        Toast.LENGTH_SHORT).show();
                startActivity(goToMarket);
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }
    }

    private void openWhatsAppBusiness() {

        String smsNumber = "918877910042";


        if (wp_b_no.equals("")){
            //do nothing
        }else {
            smsNumber = wp_b_no;
        }

        String message = "Hi,\n" +":";
        if (wp_b_text.equals("")){
            //do nithung
        }else {
            message = wp_b_text;
        }

        try{
            Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
            whatsappIntent.setType("text/plain");
            whatsappIntent.setPackage("com.whatsapp.w4b");
            whatsappIntent.putExtra(Intent.EXTRA_TEXT, message);
            whatsappIntent.putExtra("jid", smsNumber + "@s.whatsapp.net"); //phone number without "+" prefix
            startActivity(whatsappIntent);
        }catch (ActivityNotFoundException e){
            e.printStackTrace();
        }

    }


    @SuppressLint("RestrictedApi")
    public static void removeShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }

        } catch (NoSuchFieldException e) {
            Log.e("ERROR NO SUCH FIELD", "Unable to get shift mode field");
        } catch (IllegalAccessException e) {
            Log.e("ERROR ILLEGAL ALG", "Unable to change value of shift mode");
        }
    }

        @Override
    public void onBackPressed() {

//            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
//                drawerLayout.closeDrawers();
//                return;
//            }

            if (viewPager.getCurrentItem()!=0){
                if (id_==0){
                    viewPager.setCurrentItem(0);
                }else if(id_==1){
                    lin_cat.setVisibility(GONE);
                    lin_frag.setVisibility(GONE);
                    catalogue_layout.setVisibility(VISIBLE);
                    fragment_bar.setVisibility(GONE);
                    nestedScrollView.setVisibility(GONE);
                    nestedScrollView1.setVisibility(GONE);
                    nestedScrollView2.setVisibility(VISIBLE);
                    id_ =2;
                }else if (id_==2) {
                    lin_cat.setVisibility(GONE);
                    lin_frag.setVisibility(VISIBLE);
                    catalogue_layout.setVisibility(GONE);
                    fragment_bar.setVisibility(GONE);
                    nestedScrollView.setVisibility(GONE);
                    nestedScrollView1.setVisibility(VISIBLE);
                    nestedScrollView2.setVisibility(GONE);
                    id_ = 0;
                }
            }else {
                if (id_==0){
                    finish();
                }else {
                    lin_cat.setVisibility(GONE);
                    lin_frag.setVisibility(VISIBLE);
                    fragment_bar.setVisibility(GONE);
                    nestedScrollView.setVisibility(GONE);
                    nestedScrollView1.setVisibility(VISIBLE);
                    id_ =0;
                }
            }


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
              finish();
              return true;

            case R.id.cart:
                startActivity(new Intent(getApplicationContext(),Activity_Cart.class));
                return true;

            case R.id.search:
                Intent intent = new Intent(getApplicationContext(),Search_Activity.class);
                intent.putExtra("pNo","");
                startActivity(intent);
                return true;

             default:
                 return super.onOptionsItemSelected(item);
        }

    }


//    void setUpNavDrawer(){
//        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
//
//            @Override
//            public void onDrawerClosed(View drawerView) {
//                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
//                super.onDrawerClosed(drawerView);
//            }
//
//            @Override
//            public void onDrawerOpened(View drawerView) {
//                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
//                super.onDrawerOpened(drawerView);
//            }
//        };
//
//        //Setting the actionbarToggle to drawer layout
//        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
//        actionBarDrawerToggle.syncState();
//    }


    @Override
    public void onDestroy() {
//        mRewardedVideoAd.destroy(this);
        super.onDestroy();
    }



    private void addContact(String name, String phone) {
        ContentValues values = new ContentValues();
        values.put(android.provider.Contacts.People.NUMBER, phone);
        values.put(android.provider.Contacts.People.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM);
        values.put(android.provider.Contacts.People.LABEL, name);
        values.put(android.provider.Contacts.People.NAME, name);
        Uri dataUri = getContentResolver().insert(android.provider.Contacts.People.CONTENT_URI, values);
        Uri updateUri = Uri.withAppendedPath(dataUri, android.provider.Contacts.People.Phones.CONTENT_DIRECTORY);
        values.clear();
        values.put(android.provider.Contacts.People.Phones.TYPE, android.provider.Contacts.People.TYPE_MOBILE);
        values.put(android.provider.Contacts.People.NUMBER, phone);
        updateUri = getContentResolver().insert(updateUri, values);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem menuItem1 = menu.findItem(R.id.cart);

        try {

            LayerDrawable icon = (LayerDrawable) menuItem1.getIcon();
            setBadgeCount(this, icon, cart_count);

        } catch (NullPointerException e) {
            //do nothing for now...
        }

        return true;
    }


    public static void setBadgeCount(Context context, LayerDrawable icon, String count) {

        BadgeDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof BadgeDrawable) {
            badge = (BadgeDrawable) reuse;
        } else {
            badge = new BadgeDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
    }

    public static  CustomViewPager getV(){
        return viewPager;
    }


    private void getCartProducts(){

        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        Map<String, String> postParam= new HashMap<>();
        postParam.put(KEY_C_ID,customer_session.getCustomerID());


        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                CART_URL, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try{
                            String status = response.getString("status");
                            if (status.equals("200")){
                                cart_count = String.valueOf(response.getInt("cart_count"));
                            }else {
                                cart_count="0";
                            }
                            invalidateOptionsMenu();

                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

        };


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }



}