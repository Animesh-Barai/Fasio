package com.fashio.apps.Fragments;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fashio.apps.Activities.Main_Handler_Activity;
import com.fashio.apps.Activities.Product_Details;
import com.fashio.apps.Adapters.Cat_Image_Adapter_1;
import com.fashio.apps.Adapters.Home_Adapter_1;
import com.fashio.apps.Adapters.Home_Adapter_2;
import com.fashio.apps.Adapters.MyAdapter;
import com.fashio.apps.Applications.AppController;
import com.fashio.apps.Model.Home_1;
import com.fashio.apps.Model.Selection;
import com.fashio.apps.Model.Slider;
import com.fashio.apps.R;
import com.fashio.apps.Session.Customer_Session;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import me.relex.circleindicator.CircleIndicator;
import static android.content.Context.CLIPBOARD_SERVICE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class Fragment_Home extends Fragment {

    private static final String KEY_OFFSET = "offset";
    private int offset=0;
    public Fragment_Home() {
        // Required empty public constructor
    }
    private ImageView fsatisy;
    private TextView rating;
    private static final String FAV_URL = "https://apps.itrifid.com/fashio/rest_server/userlikelist/API-KEY/123456";
    private static final String KEY_ID = "c_id";
    private static final String PRODUCT_ID = "product_id";
    private static final String TAG = Main_Handler_Activity.class.getSimpleName();
    private static final String DATA_CAT_URL = "https://apps.itrifid.com/fashio/rest_server/catalogueContent/API-KEY/123456";
    private static final String KEY_CAT_ID = "catalogue_id";
    private static final String KEY_CATEGORY_ID = "category_id";
    private static final String KEY_C_ID = "c_id";
    private ArrayList<Selection> images=new ArrayList<>();
    private SwipeRefreshLayout refresh;
    private RecyclerView recyclerView;
    private View rootview;
    private RelativeLayout rl;
    private MyAdapter myAdapter;
    private static ViewPager mPager;
    private List<Home_1> categories=new ArrayList<>();
    private Home_Adapter_1 mAdapter;
    private String cat_name;
    private String cat_id;
    private String tag_name;
    private String tag_name_2;
    private String tag_color_2;
    private String tag_color;
    private String cat_count;
    private String img_url;
    private ProgressBar p_bar;
    private static final String DATA_URL = "https://apps.itrifid.com/fashio/rest_server/homecontent/API-KEY/123456";
    private static final String IMG_URL = "https://apps.itrifid.com/fashio/rest_server/getHomeSlider/API-KEY/123456";
    private LinearLayout lin_cat_details;
    private LinearLayout lin_frag_home;
    private RelativeLayout cat_layout;
    private ProgressBar p_bar_1;
    private RecyclerView r_view_1;
    private TextView cat_text;
    private TextView catno;
    private TextView catdesc;
    private TextView tag_name_text;
    // Progress Dialog
    private ProgressDialog mProgressDialog;
    private Button btn_download;
    private Button btn_copy;
    private Button btn_share_desc;
    private Button btn_share_fb;
    private RelativeLayout btn_share;
    private CheckBox share_all_check;
    private int total_selected=0;
    private ImageView img;
    private ArrayList<Uri> files;
    private List<Slider> sliders = new ArrayList<>();
    private int id =0;
    private int id_=0;
    private int pos;
    private Customer_Session customer_session;
    private Cat_Image_Adapter_1 mAdapter_1;
    //Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public interface onDataLoadListener{
        void onSliderLoaded(String w_no, String wtext, String wb_no, String wb_text);
        void requiredDtata(int id, LinearLayout lin1, LinearLayout lin2, ProgressBar p_bar, NestedScrollView sv1, NestedScrollView sv2,NestedScrollView sv3,RelativeLayout catloguye_layout);
    }
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    onDataLoadListener onDataloadListener;
    private NestedScrollView scroll_view;
    private NestedScrollView scroll_view_1;
    private NestedScrollView scroll_view_2;
    private LinearLayout frag_catlogue_details;
    private RecyclerView catlogue_rec_view;
    private RelativeLayout catlogue_layout;
    private TextView catlogue_content;
    private TextView catlogue_desc;
    private TextView catlogue_tag_name;
    private TextView catlogue_tag_name_2;
    private TextView catlogue_no;
    private ProgressBar p_bar_2;
    int splash_id;
    String splash_cat;
    public String wp_no="";
    public String wp_no_b="";
    public String wp_text="";
    public String wp_b_text="";
    private int load_id = 0;
    private String cat_desc="";
    private String slider_url="";
    private Home_Adapter_2 mAdapter_2;
    private static final String DATA_CATLOGUE_URL = "https://apps.itrifid.com/fashio/rest_server/catCatalogueContent/API-KEY/123456";
    private List<Home_1> catlogues=new ArrayList<>();
    private List<String> sizes_avail=new ArrayList<>();


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onDataloadListener = (onDataLoadListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onSomeEventListener");
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        rootview =inflater.inflate(R.layout.fragment_fragment__home, container, false);

        Intent intent = getActivity().getIntent();
        Bundle extras = intent.getExtras();

        if (extras!=null){
            splash_id = extras.getInt("id");
            splash_cat = extras.getString("cat");
        }

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCanceledOnTouchOutside(false);

        Typeface tf1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/arial.ttf");

        customer_session = new Customer_Session(getActivity());

        lin_cat_details = (LinearLayout) rootview.findViewById(R.id.cat_details);
        lin_frag_home = (LinearLayout) rootview.findViewById(R.id.fragment_home);
        cat_layout = (RelativeLayout) rootview.findViewById(R.id.cat_layout);
        catlogue_layout = (RelativeLayout) rootview.findViewById(R.id.catlogue_layout);
        frag_catlogue_details = (LinearLayout) rootview.findViewById(R.id.catlogue_details);
        img = (ImageView) rootview.findViewById(R.id.img_slider);

        scroll_view = (NestedScrollView) rootview.findViewById(R.id.scroll_view);
        scroll_view_1 = (NestedScrollView) rootview.findViewById(R.id.scroll_view_1);
        scroll_view_2 = (NestedScrollView) rootview.findViewById(R.id.catalogue_scroll_view);
        catlogue_rec_view = (RecyclerView) rootview.findViewById(R.id.catlogue_rec_view);

        catlogue_content = (TextView) rootview.findViewById(R.id.catlogue_name);
        catlogue_desc = (TextView) rootview.findViewById(R.id.catlogue_desc);
        catlogue_no = (TextView) rootview.findViewById(R.id.catlogue_no);
        catlogue_tag_name = (TextView) rootview.findViewById(R.id.newly_added_2);
        catlogue_tag_name_2 = (TextView) rootview.findViewById(R.id.newly_added_3);
        p_bar_2 = (ProgressBar) rootview.findViewById(R.id.p_bar_2);

        cat_text = (TextView) rootview.findViewById(R.id.cat_name);

        catno = (TextView) rootview.findViewById(R.id.cat_no);

        catdesc = (TextView) rootview.findViewById(R.id.cat_desc);

        tag_name_text = (TextView) rootview.findViewById(R.id.newly_added);

        btn_copy = (Button) rootview.findViewById(R.id.copy_desc_btn);
        btn_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", categories.get(pos).getCat_desc());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getActivity(), "Description Copied", Toast.LENGTH_SHORT).show();
            }

        });


        share_all_check = (CheckBox) rootview.findViewById(R.id.share);
        share_all_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mAdapter_1.selectAll();
                }else {
                    mAdapter_1.deselectAll();
                }
            }
        });

        btn_share_desc = (Button) rootview.findViewById(R.id.share_desc_btn);
        btn_share_desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (whatsappInstalledOrNot("com.whatsapp") && whatsappInstalledOrNot("com.whatsapp.w4b")) {
                    {
                        String title = "Send to :";
                        CharSequence[] itemlist = {"Whatsapp",
                                "Whatsapp Business",
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        //builder.setIcon(R.drawable.icon_app);
                        builder.setTitle(title);
                        builder.setItems(itemlist, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:// Take Photo
                                        try {
                                            Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                                            whatsappIntent.setType("text/plain");
                                            whatsappIntent.setPackage("com.whatsapp");
                                            whatsappIntent.putExtra(Intent.EXTRA_TEXT, categories.get(pos).getCat_desc());
                                            startActivity(whatsappIntent);
                                        } catch (NullPointerException e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                    case 1:// Choose Existing Photo
                                        try {
                                            Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                                            whatsappIntent.setType("text/plain");
                                            whatsappIntent.setPackage("com.whatsapp.w4b");
                                            whatsappIntent.putExtra(Intent.EXTRA_TEXT, categories.get(pos).getCat_desc());
                                            startActivity(whatsappIntent);
                                        } catch (NullPointerException e) {
                                            e.printStackTrace();
                                        }
                                        break;

                                    default:
                                        break;
                                }
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.setCancelable(true);
                        alert.show();


                    }
                }


            }
        });

        rating = rootview.findViewById(R.id.rating);

        fsatisy = rootview.findViewById(R.id.fsatisfy);

        btn_share_fb = (Button) rootview.findViewById(R.id.share_facebook_btn);
        btn_share_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mProgressDialog.show();

                id_=2;
                total_selected=0;
                for (int i=0;i<images.size();i++){
                    if (images.get(i).getSelect().equals(true)){
                        total_selected+=1;
                    }
                }

                files=new ArrayList<>();
                for(int j=0;j<images.size();j++){

                    if(images.get(j).getSelect().equals(true)){
                        String img_url = images.get(j).getImg_url();
                        new ShareTask().execute(img_url);
                    }

                    if(j==images.size()-1){
                        if(total_selected==0){
                            mProgressDialog.dismiss();
                            Toast.makeText(getActivity(), "Select at least one image", Toast.LENGTH_SHORT).show();
                        }
                    }

                }

            }
        });

        btn_download = (Button) rootview.findViewById(R.id.download_btn);
        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<images.size();i++){

                    if(images.get(i).getSelect().equals(true)){

                        mProgressDialog.show();

                        Thread shivThread1 = new Thread() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(500);

                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            downloadImages();
                                        }
                                    });

                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                            }
                        };

                        shivThread1.start();

                        break;
                    }else {
                        if(i==images.size()-1){
                            Toast.makeText(getActivity(), "Select at least one image", Toast.LENGTH_SHORT).show();
                        }
                    }


                }

            }
        });

        catlogue_rec_view.setNestedScrollingEnabled(false);

        btn_share = (RelativeLayout) rootview.findViewById(R.id.sahre_btn);
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Check if we have write permission
                int permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (permission != PackageManager.PERMISSION_GRANTED) {
                    // We don't have permission so prompt the user
                    ActivityCompat.requestPermissions(
                            getActivity(),
                            PERMISSIONS_STORAGE,
                            REQUEST_EXTERNAL_STORAGE
                    );
                }else {


                    for(int k=0;k<images.size();k++) {

                        if (images.get(k).getSelect().equals(true)) {

                            if (whatsappInstalledOrNot("com.whatsapp") && whatsappInstalledOrNot("com.whatsapp.w4b")){
                                String title = "Send to :";
                                CharSequence[] itemlist ={"Whatsapp",
                                        "Whatsapp Business",
                                };

                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                //builder.setIcon(R.drawable.icon_app);
                                builder.setTitle(title);
                                builder.setItems(itemlist, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            case 0:// Take Photo
                                                try
                                                {
                                                    id_=0;
                                                    total_selected=0;
                                                    for (int i=0;i<images.size();i++){
                                                        if (images.get(i).getSelect().equals(true)){
                                                            total_selected+=1;
                                                        }
                                                    }

                                                    files=new ArrayList<>();
                                                    for(int j=0;j<images.size();j++){

                                                        if(images.get(j).getSelect().equals(true)){
                                                            String img_url = images.get(j).getImg_url();
                                                            new ShareTask().execute(img_url);
                                                        }

                                                    }

                                                }catch (NullPointerException e){
                                                    e.printStackTrace();
                                                }
                                                break;
                                            case 1:// Choose Existing Photo
                                                try {
                                                    id_ =1;
                                                    total_selected=0;
                                                    for (int i=0;i<images.size();i++){
                                                        if (images.get(i).getSelect().equals(true)){
                                                            total_selected+=1;
                                                        }
                                                    }

                                                    files=new ArrayList<>();
                                                    for(int j=0;j<images.size();j++){

                                                        if(images.get(j).getSelect().equals(true)){
                                                            String img_url = images.get(j).getImg_url();
                                                            new ShareTask().execute(img_url);
                                                        }

                                                    }

                                                }catch (NullPointerException e){
                                                    e.printStackTrace();
                                                }
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

                                try {
                                    total_selected=0;
                                    for (int i=0;i<images.size();i++){
                                        if (images.get(i).getSelect().equals(true)){
                                            total_selected+=1;
                                        }
                                    }

                                    files=new ArrayList<>();
                                    for(int j=0;j<images.size();j++){

                                        if(images.get(j).getSelect().equals(true)){
                                            String img_url = images.get(j).getImg_url();
                                            new ShareTask().execute(img_url);
                                        }

                                    }

                                }catch (NullPointerException e){
                                    e.printStackTrace();
                                }
                            }


                            break;

                        }else {
                            if(k==images.size()-1){
                                Toast.makeText(getActivity(), "Select at least one image", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }

                 }

            }
        });

        r_view_1 = (RecyclerView) rootview.findViewById(R.id.recycler_view);
        rl= (RelativeLayout) rootview.findViewById(R.id.home_slider);

        p_bar = (ProgressBar) rootview.findViewById(R.id.progress_bar);
        p_bar_1 = (ProgressBar) rootview.findViewById(R.id.p_bar_1);

        recyclerView = (RecyclerView) rootview.findViewById(R.id.r_view);

        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setVisibility(View.VISIBLE);

        r_view_1.setNestedScrollingEnabled(false);

        refresh = (SwipeRefreshLayout) rootview.findViewById(R.id.simpleSwipeRefreshLayout);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if(scroll_view_1.getVisibility()== VISIBLE){
                    p_bar.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(GONE);
                    rl.setVisibility(GONE);
                    getData();
                    refresh.setRefreshing(false);
                }else if(scroll_view.getVisibility()==VISIBLE){
                    lin_cat_details.setVisibility(VISIBLE);
                    lin_frag_home.setVisibility(GONE);
                    p_bar_1.setVisibility(View.VISIBLE);
                    scroll_view.setVisibility(VISIBLE);
                    scroll_view_2.setVisibility(GONE);
                    scroll_view_1.setVisibility(View.GONE);
                    cat_text.setText(catlogues.get(pos).getCat_name());
                    catdesc.setText(catlogues.get(pos).getCat_desc());
                    tag_name_text.setText("#"+catlogues.get(pos).getTag_name());
//                    tag_name_2_text.setText("#"+catlogues.get(pos).getTag_name_2());
                    if (catlogues.get(pos).getTag_name().equals("Default") || categories.get(pos).getTag_name().equals("") || categories.get(pos).getTag_name().equals("default")){
                        tag_name_text.setVisibility(GONE);
                    }

                    if (catlogues.get(pos).getTag_name_2().equals("Default") || categories.get(pos).getTag_name_2().equals("")|| categories.get(pos).getTag_name().equals("default")){
//                        tag_name_2_text.setVisibility(GONE);
                    }

                    try{
                        tag_name_text.setBackgroundColor(Color.parseColor(catlogues.get(pos).getTag_color()));
//                        tag_name_2_text.setBackgroundColor(Color.parseColor(catlogues.get(pos).getTag_color_2()));
                    }catch (IllegalStateException e){
                        e.printStackTrace();
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }catch (StringIndexOutOfBoundsException e){
                        e.printStackTrace();
                    }
                    getDetails();
                    refresh.setRefreshing(false);
                }else {
                    catlogue_layout.setVisibility(GONE);
                    p_bar_2.setVisibility(VISIBLE);
                    lin_cat_details.setVisibility(GONE);
                    lin_frag_home.setVisibility(GONE);
                    scroll_view.setVisibility(GONE);
                    scroll_view_1.setVisibility(GONE);
                    scroll_view_2.setVisibility(View.VISIBLE);
                    catlogue_content.setText(categories.get(pos).getCat_name());
                    catlogue_desc.setText(categories.get(pos).getCat_desc());
                    catlogue_tag_name.setText(categories.get(pos).getTag_name());
                    catlogue_tag_name_2.setText(categories.get(pos).getTag_name_2());
                    if (categories.get(pos).getTag_name().equals("Default") || categories.get(pos).getTag_name().equals("") || categories.get(pos).getTag_name().equals("default")){
                        catlogue_tag_name.setVisibility(GONE);
                    }

                    if (categories.get(pos).getTag_name_2().equals("Default") || categories.get(pos).getTag_name_2().equals("")|| categories.get(pos).getTag_name().equals("default")){
                        catlogue_tag_name_2.setVisibility(GONE);
                    }

                    try{
                        catlogue_tag_name.setBackgroundColor(Color.parseColor(categories.get(pos).getTag_color()));
                        catlogue_tag_name_2.setBackgroundColor(Color.parseColor(categories.get(pos).getTag_color_2()));
                    }catch (IllegalStateException e){
                        e.printStackTrace();
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }catch (StringIndexOutOfBoundsException e){
                        e.printStackTrace();
                    }
                    getCatlogues();
                    refresh.setRefreshing(false);
                }

            }
        });


        return rootview;
    }

    private void init(View v) {

        mPager = (ViewPager) v.findViewById(R.id.pager);
        mPager.setAdapter(myAdapter);

        CircleIndicator indicator = (CircleIndicator) v.findViewById(R.id.indicator);
        indicator.setViewPager(mPager);


        mPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                refresh.setEnabled(false);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        refresh.setEnabled(true);
                        break;
                }
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


        NUM_PAGES = sliders.size();

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mPager.getParent().requestDisallowInterceptTouchEvent(true);
            }
        });

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getData();
    }

    void getData(){

        String tag_json_obj = "json_obj_req";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                DATA_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            categories.clear();

                            String status = response.getString("status");

                            if(status.equals("200")){

                                JSONArray jsonArray = response.getJSONArray("userData");


                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject= jsonArray.getJSONObject(i);

                                    cat_name = jsonObject.getString("categoryName");
                                    cat_id =jsonObject.getString("categoryId");
                                    int count = jsonObject.getInt("items_count");
                                    cat_count = String.valueOf(count);
                                    tag_name = jsonObject.getString("tag_name");
                                    tag_color = jsonObject.getString("tag_color");
                                    tag_name_2 = jsonObject.getString("tag2_name");
                                    tag_color_2 = jsonObject.getString("tag2_color");
                                    img_url=jsonObject.getString("category_image");
                                    cat_desc=jsonObject.getString("description");

                                    JSONObject jsonObject1 = jsonObject.getJSONObject("wa_data");
                                    wp_no = jsonObject1.getString("gen_whatsapp_number");
                                    wp_no_b = jsonObject1.getString("biz_whatsapp_number");
                                    wp_text = jsonObject1.getString("gen_whatsapp_text");
                                    wp_b_text = jsonObject1.getString("biz_whatsapp_text");
                                    slider_url=jsonObject1.getString("img_dest");

                                    onDataloadListener.onSliderLoaded(wp_no,wp_text,wp_no_b,wp_b_text);

                                    categories.add(new Home_1(cat_name,cat_id,cat_count,tag_name,tag_name_2,tag_color,tag_color_2,cat_desc,img_url,""));
                                }

//                                onDataloadListener.onDataLoaded(arrays);

                                mAdapter = new Home_Adapter_1(getActivity(), categories, new Home_Adapter_1.ClickListener() {
                                    @Override
                                    public void onClick(View view, int position) {
                                        pos = position;
                                        startThread();
                                        lin_cat_details.setVisibility(GONE);
                                        lin_frag_home.setVisibility(GONE);
                                        scroll_view.setVisibility(GONE);
                                        scroll_view_1.setVisibility(GONE);
                                        scroll_view_2.setVisibility(View.VISIBLE);
                                        catlogue_content.setText(categories.get(pos).getCat_name());
                                        catlogue_desc.setText(categories.get(position).getCat_desc());
                                        catlogue_tag_name.setText(categories.get(pos).getTag_name());
                                        catlogue_tag_name_2.setText(categories.get(pos).getTag_name_2());
                                        if (categories.get(pos).getTag_name().equals("Default") || categories.get(pos).getTag_name().equals("") || categories.get(pos).getTag_name().equals("default")){
                                            catlogue_tag_name.setVisibility(GONE);
                                        }
                                        load_id=2;
                                        if (categories.get(pos).getTag_name_2().equals("Default") || categories.get(pos).getTag_name_2().equals("")|| categories.get(pos).getTag_name().equals("default")){
                                            catlogue_tag_name_2.setVisibility(GONE);
                                        }

                                        try{
                                            catlogue_tag_name.setBackgroundColor(Color.parseColor(categories.get(pos).getTag_color()));
                                            catlogue_tag_name_2.setBackgroundColor(Color.parseColor(categories.get(pos).getTag_color_2()));
                                        }catch (IllegalStateException e){
                                            e.printStackTrace();
                                        }catch (NullPointerException e){
                                            e.printStackTrace();
                                        }catch (StringIndexOutOfBoundsException e){
                                            e.printStackTrace();
                                        }
                                        getCatlogues();
                                    }

                                    @Override
                                    public void onLongClick(View view, int position) {

                                    }

                                    @Override
                                    public void onImgCLick(View view, int position) {
                                        pos = position;
                                        startThread();
                                        lin_cat_details.setVisibility(GONE);
                                        lin_frag_home.setVisibility(GONE);
                                        scroll_view.setVisibility(GONE);
                                        scroll_view_1.setVisibility(GONE);
                                        scroll_view_2.setVisibility(View.VISIBLE);
                                        catlogue_content.setText(categories.get(pos).getCat_name());
                                        catlogue_desc.setText(categories.get(position).getCat_desc());
                                        catlogue_tag_name.setText(categories.get(pos).getTag_name());
                                        catlogue_tag_name_2.setText(categories.get(pos).getTag_name_2());
                                        if (categories.get(pos).getTag_name().equals("Default") || categories.get(pos).getTag_name().equals("") || categories.get(pos).getTag_name().equals("default")){
                                            catlogue_tag_name.setVisibility(GONE);
                                        }

                                        if (categories.get(pos).getTag_name_2().equals("Default") || categories.get(pos).getTag_name_2().equals("")|| categories.get(pos).getTag_name().equals("default")){
                                            catlogue_tag_name.setVisibility(GONE);
                                        }

                                        try{
                                            tag_name_text.setBackgroundColor(Color.parseColor(categories.get(pos).getTag_color()));
//                                            tag_name_2_text.setBackgroundColor(Color.parseColor(categories.get(pos).getTag_color_2()));
                                        }catch (IllegalStateException e){
                                            e.printStackTrace();
                                        }catch (NullPointerException e){
                                            e.printStackTrace();
                                        }catch (StringIndexOutOfBoundsException e){
                                            e.printStackTrace();
                                        }
                                        load_id=2;
                                        getCatlogues();
                                    }
                                });

                                Picasso.with(getActivity()).load(slider_url).placeholder(R.drawable.phimg).into(img);

                                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
                                recyclerView.setLayoutManager(staggeredGridLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(mAdapter);

                                p_bar.setVisibility(GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                rl.setVisibility(GONE);
                                img.setVisibility(View.VISIBLE);

//                                recyclerView.setBackgroundColor(getResources().getColor(R.color.black));
//                                getImages();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }



//    void getImages(){
//
//        String tag_json_obj = "json_obj_req";
//
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
//                IMG_URL, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//
//                        sliders.clear();
//                        try {
//                            String status = response.getString("status");
//
//                            if (status.equals("200")){
//                                JSONArray jsonArray = response.getJSONArray("userData");
//                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
//
//                            }else {
//                                Toast.makeText(getActivity(), "Connection Error", Toast.LENGTH_SHORT).show();
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//
//                    }
//                }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//// Adding request to request queue
//        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
//
//    }

    public void downloadImages() {

        for (int i = 0; i < images.size(); i++) {

            if(images.get(i).getSelect().equals(true)){
                String img_url = images.get(i).getImg_url();
                new DownloadTask().execute(img_url);
            }

            if(i==images.size()-1){
                mProgressDialog.hide();
                Toast.makeText(getActivity(), "Images Downloaded Successfully", Toast.LENGTH_SHORT).show();
            }

        }
    }


    private void scanFile(String path) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATA,""+path);
        values.put(MediaStore.Images.Media.MIME_TYPE,"image/jpeg");
        getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        Log.i("TAG", "Agter scanning" +path);
    }


    private class DownloadTask extends AsyncTask<String,Integer,Bitmap> {

        final String Dir = categories.get(pos).getCat_name();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Bitmap doInBackground(String... param) {
            return getBitmapFromURL(param[0]);

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            try {
                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/~AK " + Dir);

                if (!myDir.exists()) {
                    myDir.mkdirs();
                }

                String name = new Date().getTime() + ".jpg";
                myDir = new File(myDir, name);
                FileOutputStream out = new FileOutputStream(myDir);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

                out.flush();
                out.close();
                Log.i("TAG", "scanning File " + myDir.getAbsolutePath());
                MediaScannerConnection.scanFile(getActivity(),
                        new String[]{myDir.getAbsolutePath()}, null, null);


                // sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(myDir)));


                Log.i("TAG", "Agter scanning" + myDir.getAbsolutePath());

            } catch (Exception e) {
                // some action
            }


            String root = Environment.getExternalStorageDirectory().toString();

            File filePath = new File(root + "/~AK " + Dir);

            getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(String.valueOf(filePath)))));

            mProgressDialog.hide();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
        }
    }

    void shareImages(){

        total_selected=0;
        for (int i=0;i<images.size();i++){
            if (images.get(i).getSelect().equals(true)){
                total_selected+=1;
            }
        }

        files=new ArrayList<>();
        for(int j=0;j<images.size();j++){

            if(images.get(j).getSelect().equals(true)){
                String img_url = images.get(j).getImg_url();
                new ShareTask().execute(img_url);
            }

        }

    }

    private  class ShareTask extends AsyncTask<String,Integer,Bitmap>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... param) {
            return getBitmapFromURL(param[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            Uri uri = getImageUri(getActivity(), bitmap);
            files.add(uri);


            if (total_selected==files.size()){

                final Thread thread= new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            Thread.sleep(1000);

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    final Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                                    intent.putExtra(Intent.EXTRA_SUBJECT, "Here are some files.");
                                    intent.setType("image/jpeg"); /* This example is sharing jpeg images. */
                                    if(id_==0){
                                        intent.setPackage("com.whatsapp");
                                    }else if (id==1){
                                        intent.setPackage("com.whatsapp.w4b");
                                    }else if (id_==2){
                                        intent.setPackage("com.facebook.katana");
                                    }
                                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
                                    startActivity(Intent.createChooser(intent, "Send these images"));
                                    mProgressDialog.hide();
                                }
                            });


                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                });

                thread.start();


            }
        }

    }


    public static Bitmap getBitmapFromURL(String src) {

        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void getDetails(){

        share_all_check.setChecked(false);

        String tag_json_obj = "json_obj_req";

        Map<String,String> post_param = new HashMap<>();
        post_param.put(KEY_CAT_ID,catlogues.get(pos).getCat_no());
        post_param.put(KEY_C_ID,customer_session.getCustomerID());


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                DATA_CAT_URL,new JSONObject(post_param),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        images.clear();
                        try {
                            String status = response.getString("status");
                            if (status.equals("200")){
                                JSONArray jsonArray = response.getJSONArray("userData");
                                String cat_desc = response.getString("cat_desc");
                                String is_size_avail = response.getString("is_size_ava");
                                if (is_size_avail.equals("1")){
                                    sizes_avail.clear();
                                    JSONArray size_array= response.getJSONArray("available_sizes");
                                    for (int i=0;i<size_array.length();i++){
                                        JSONObject sizes_object = size_array.getJSONObject(i);
                                        String size = sizes_object.getString("size");
                                        sizes_avail.add(size);
                                    }
                                }else {
                                    sizes_avail.add("false");
                                }

                                String ava_cat_ra = response.getString("ava_cata_rate");
                                rating.setText(ava_cat_ra);

                                if (catlogues.get(pos).getFsatisfy().equals("1")){
                                    fsatisy.setVisibility(View.VISIBLE);
                                }else {
                                    fsatisy.setVisibility(View.GONE);
                                }

                                String cod_available = response.getString("is_cod_ava");

                                for (int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    JSONObject properties= jsonObject.getJSONObject("product_data");

                                    String is_satisfy = jsonObject.getString("is_satisfy");
                                    String rate_count = jsonObject.getString("rate_count");
                                    Boolean isLiked = jsonObject.getBoolean("isLiked");
                                    String img_url = properties.getString("original_img_dest");
                                    String img_id = properties.getString("img_id");
                                    String price = properties.getString("selling_price");
                                    String actual_price = properties.getString("actual_price");
                                    String cat_name = properties.getString("catalogue_name");
                                    String count_all = properties.getString("like_count");
                                    String image_name = properties.getString("image_name");
                                    String product_weight = properties.getString("product_weight");

                                    Selection selection = new Selection(img_url,false,img_id,price,actual_price,cat_name,count_all,isLiked,image_name,sizes_avail,cod_available,cat_desc,product_weight,rate_count,is_satisfy);
                                    images.add(selection);
                                }

                                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);

                                mAdapter_1 = new Cat_Image_Adapter_1(recyclerView,getActivity(), images,  new Cat_Image_Adapter_1.ClickListener() {

                                    @Override
                                    public void onFavClick(View v, int pos, View v2) {
                                        addToFav((ImageView) v,images.get(pos).getImg_id(),(TextView) v2);
                                    }

                                    @Override
                                    public void onClick(View view, int position) {

                                    }

                                    @Override
                                    public void onImgClick(View view, int position) {
                                        Intent intent = new Intent(getActivity(),Product_Details.class);
                                        Bundle data = new Bundle();
                                        data.putSerializable("images", images);
                                        intent.putExtra("ca",data);
                                        intent.putExtra("pos",position);
                                        intent.putExtra("cat_name",images.get(position).getCat_name());
                                        intent.putExtra("id",1);
                                        intent.putExtra("pNo",wp_no);
                                        intent.putExtra("catlog_id",catlogues.get(pos).getCat_no());
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onLongClick(View view, int position) {

                                    }
                                });


                                r_view_1.setLayoutManager(staggeredGridLayoutManager);
                                r_view_1.setItemAnimator(new DefaultItemAnimator());
                                r_view_1.setAdapter(mAdapter_1);

                                mAdapter_1.setOnLoadMoreListener(new Cat_Image_Adapter_1.OnLoadMoreListener() {
                                    @Override
                                    public void onLoadMore() {
                                        images.add(null);
                                        mAdapter.notifyItemRemoved(categories.size());
                                        loadDetails();
                                    }
                                });

                                catno.setText(images.size()+" Designs");

                                id =1;
                                load_id=1;
                                onDataloadListener.requiredDtata(id,lin_cat_details,lin_frag_home,p_bar,scroll_view,scroll_view_1,scroll_view_2,catlogue_layout);

                                p_bar_1.setVisibility(GONE);
                                cat_layout.setVisibility(View.VISIBLE);
                            }else {
                                Toast.makeText(getActivity(), "Connection Error", Toast.LENGTH_SHORT).show();
                                p_bar.setVisibility(View.VISIBLE);
                                cat_layout.setVisibility(GONE);
                                lin_frag_home.setVisibility(View.VISIBLE);
                                lin_cat_details.setVisibility(GONE);
                                scroll_view.setVisibility(GONE);
                                scroll_view_1.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            Toast.makeText(getActivity(), "Connection Error", Toast.LENGTH_SHORT).show();
                            p_bar.setVisibility(View.VISIBLE);
                            cat_layout.setVisibility(GONE);
                            lin_frag_home.setVisibility(View.VISIBLE);
                            lin_cat_details.setVisibility(GONE);
                            scroll_view.setVisibility(GONE);
                            scroll_view_1.setVisibility(View.VISIBLE);
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Connection Error", Toast.LENGTH_SHORT).show();
                p_bar.setVisibility(View.VISIBLE);
                cat_layout.setVisibility(GONE);
                lin_frag_home.setVisibility(View.VISIBLE);
                lin_cat_details.setVisibility(GONE);
                scroll_view.setVisibility(GONE);
                scroll_view_1.setVisibility(View.VISIBLE);
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    private void loadDetails() {

        share_all_check.setChecked(false);

        String tag_json_obj = "json_obj_req";

        Map<String,String> post_param = new HashMap<>();
        post_param.put(KEY_OFFSET, String.valueOf(offset));
        if (splash_id ==1){
            post_param.put(KEY_CAT_ID,splash_cat);
        }else {
            post_param.put(KEY_CAT_ID,categories.get(pos).getCat_no());
        }
        post_param.put(KEY_C_ID,customer_session.getCustomerID());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                DATA_CAT_URL,new JSONObject(post_param),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        images.clear();
                        try {
                            String status = response.getString("status");
                            offset = response.getInt("offset");

                            if (status.equals("200")){
                                offset = response.getInt("offset");
                                JSONArray jsonArray = response.getJSONArray("userData");
                                String cat_desc = response.getString("cat_desc");
                                String is_size_avail = response.getString("is_size_ava");
                                if (is_size_avail.equals("1")){
                                    sizes_avail.clear();
                                    JSONArray size_array= response.getJSONArray("available_sizes");
                                    for (int i=0;i<size_array.length();i++){
                                        JSONObject sizes_object = size_array.getJSONObject(i);
                                        String size = sizes_object.getString("size");
                                        sizes_avail.add(size);
                                    }
                                }else {
                                    sizes_avail.add("false");
                                }
                                String cod_available = response.getString("is_cod_ava");

                                for (int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    JSONObject properties= jsonObject.getJSONObject("product_data");

                                    String is_satisfy = jsonObject.getString("is_satisfy");
                                    String rate_count = jsonObject.getString("rate_count");
                                    Boolean isLiked = jsonObject.getBoolean("isLiked");
                                    String img_url = properties.getString("original_img_dest");
                                    String img_id = properties.getString("img_id");
                                    String price = properties.getString("selling_price");
                                    String actual_price = properties.getString("actual_price");
                                    String cat_name = properties.getString("catalogue_name");
                                    String count_all = properties.getString("like_count");
                                    String image_name = properties.getString("image_name");
                                    String product_weight = properties.getString("product_weight");

                                    Selection selection = new Selection(img_url,false,img_id,price,actual_price,cat_name,count_all,isLiked,image_name,sizes_avail,cod_available,cat_desc,product_weight,rate_count,is_satisfy);
                                    images.add(selection);
                                }

                                mAdapter_1.setLoaded();
                                mAdapter_1.notifyDataSetChanged();

                            }else {
                            }

                        } catch (JSONException e) {
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);


    }

//    public void getDetails(String cat_no){
//
//        String tag_json_obj = "json_obj_req";
//
//        Map<String,String> post_param = new HashMap<>();
//        post_param.put(KEY_CAT_ID,cat_no);
//        post_param.put(KEY_C_ID,customer_session.getCustomerID());
//
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
//                DATA_CAT_URL,new JSONObject(post_param),
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//
//                        images.clear();
//                        try {
//                            String status = response.getString("status");
//
//                            if (status.equals("200")){
//                                JSONArray jsonArray = response.getJSONArray("userData");
//
//                                for (int i=0;i<jsonArray.length();i++){
//
//                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                    JSONObject properties= jsonObject.getJSONObject("product_data");
//
//                                    String cadesc = jsonObject.getString("description");
//                                    Boolean isLiked = jsonObject.getBoolean("isLiked");
//                                    String img_url = properties.getString("original_img_dest");
//                                    String img_id = properties.getString("img_id");
//                                    String price = properties.getString("selling_price");
//                                    String actual_price = properties.getString("actual_price");
//                                    String cat_name = properties.getString("category_name");
//                                    String count_all = properties.getString("like_count");
//                                    String image_name = properties.getString("image_name");
//
//                                    cat_text.setText(cat_name);
//                                    catdesc.setText(cadesc);
//
//
//                                    Selection selection = new Selection(img_url,false,img_id,price,actual_price,cat_name,count_all,isLiked,image_name);
//                                    images.add(selection);
//                                }
//
//
//                                mAdapter_1 = new Cat_Image_Adapter(getActivity(), images,  new Cat_Image_Adapter.ClickListener() {
//
//                                    @Override
//                                    public void onFavClick(View v, int pos, View v2) {
//                                        addToFav((ImageView) v,images.get(pos).getImg_id(),(TextView) v2);
//                                    }
//
//                                    @Override
//                                    public void onClick(View view, int position) {
//
//                                    }
//
//                                    @Override
//                                    public void onImgClick(View view, int position) {
//                                        Intent intent = new Intent(getActivity(),Full_Image.class);
//                                        Bundle data = new Bundle();
//                                        data.putSerializable("images", images);
//                                        intent.putExtra("ca",data);
//                                        intent.putExtra("pos",position);
//                                        intent.putExtra("cat_name",images.get(position).getCat_name());
//                                        intent.putExtra("id",1);
//                                        startActivity(intent);
//                                    }
//
//                                    @Override
//                                    public void onLongClick(View view, int position) {
//
//                                    }
//                                });
//
//                                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
//                                r_view_1.setLayoutManager(staggeredGridLayoutManager);
//                                r_view_1.setItemAnimator(new DefaultItemAnimator());
//                                r_view_1.setAdapter(mAdapter_1);
//
//                                catno.setText(images.size()+" Designs");
//
//                                id =1;
//                                load_id=1;
//                                onDataloadListener.requiredDtata(id,lin_cat_details,lin_frag_home,p_bar,scroll_view,scroll_view_1);
//                                p_bar_1.setVisibility(View.GONE);
//                                cat_layout.setVisibility(View.VISIBLE);
//                                scroll_view.setVisibility(View.VISIBLE);
//                                scroll_view_1.setVisibility(View.GONE);
//                            }else {
//                                Toast.makeText(getActivity(), "Connection Error", Toast.LENGTH_SHORT).show();
//                                p_bar.setVisibility(View.VISIBLE);
//                                cat_layout.setVisibility(View.GONE);
//                                lin_frag_home.setVisibility(View.VISIBLE);
//                                lin_cat_details.setVisibility(View.GONE);
//                                scroll_view.setVisibility(View.GONE);
//                                scroll_view_1.setVisibility(View.VISIBLE);
//                            }
//
//                        } catch (JSONException e) {
//                            Toast.makeText(getActivity(), "Connection Error", Toast.LENGTH_SHORT).show();
//                            p_bar.setVisibility(View.VISIBLE);
//                            cat_layout.setVisibility(View.GONE);
//                            lin_frag_home.setVisibility(View.VISIBLE);
//                            lin_cat_details.setVisibility(View.GONE);
//                            scroll_view.setVisibility(View.GONE);
//                            scroll_view_1.setVisibility(View.VISIBLE);
//                        }
//
//
//                    }
//                }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getActivity(), "Connection Error", Toast.LENGTH_SHORT).show();
//                p_bar.setVisibility(View.VISIBLE);
//                cat_layout.setVisibility(View.GONE);
//                lin_frag_home.setVisibility(View.VISIBLE);
//                lin_cat_details.setVisibility(View.GONE);
//                scroll_view.setVisibility(View.GONE);
//                scroll_view_1.setVisibility(View.VISIBLE);
//            }
//        });
//
//// Adding request to request queue
//        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
//
//    }



    private void addToFav(final ImageView v, String img_id, final TextView t) {


        String tag_json_obj = "json_obj_req";

        Map<String, String> postParam= new HashMap<>();
        postParam.put(KEY_ID,customer_session.getCustomerID());
        postParam.put(PRODUCT_ID,img_id);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                FAV_URL, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String status = response.getString("status");

                            if (status.equals("200")){
                                v.setImageResource(R.drawable.heart_re);
                                t.setText(response.getString("all_like_counter"));
                            }else if (status.equals("208")){
                                v.setImageResource(R.drawable.heart_outline);
                                t.setText(response.getString("all_like_counter"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Connection Error", Toast.LENGTH_SHORT).show();
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    private boolean whatsappInstalledOrNot(String uri) {
        PackageManager pm = getActivity().getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }


    public void getCatdetails(int position){
        pos = position;
        lin_cat_details.setVisibility(View.VISIBLE);
        lin_frag_home.setVisibility(GONE);
        p_bar_1.setVisibility(View.VISIBLE);
        scroll_view.setVisibility(GONE);
        scroll_view_1.setVisibility(View.VISIBLE);
        cat_text.setText(categories.get(pos).getCat_name());
        catdesc.setText("");
        tag_name_text.setText(categories.get(pos).getTag_name());
//        tag_name_2_text.setText(categories.get(pos).getTag_name_2());

        if (categories.get(pos).getTag_name().equals("Default") || categories.get(pos).getTag_name().equals("") || categories.get(pos).getTag_name().equals("default")){
            tag_name_text.setVisibility(GONE);
        }

        if (categories.get(pos).getTag_name_2().equals("Default") || categories.get(pos).getTag_name_2().equals("")|| categories.get(pos).getTag_name().equals("default")){
//            tag_name_2_text.setVisibility(GONE);
        }

        try{
            tag_name_text.setBackgroundColor(Color.parseColor(categories.get(pos).getTag_color()));
//            tag_name_2_text.setBackgroundColor(Color.parseColor(categories.get(pos).getTag_color_2()));
        }catch (IllegalStateException e){
            e.printStackTrace();
        }catch (NullPointerException e){
            e.printStackTrace();
        }catch (StringIndexOutOfBoundsException e){
            e.printStackTrace();
        }
        getDetails();
    }

    private void openWhatsApp() {

        String smsNumber = "919836005273";
        if (wp_no.equals("")){
            //do nothing
        }else {
            smsNumber = wp_no;
        }

        String message = "Hi,\n" +
                "I got your Number from AK Kurtis APP\n" +
                "\n" +
                "My Requirement is :";
        if (!wp_text.equals("")){
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
                Toast.makeText(getActivity(), "WhatsApp not Installed",
                        Toast.LENGTH_SHORT).show();
                startActivity(goToMarket);
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }

    }

    private void openWhatsAppBusiness() {

        String smsNumber = "919836005273";
        if (wp_no.equals("")){
            //do nothing
        }else {
            smsNumber = wp_no;
        }

        String message = "Hi,\n" +
                "I got your Number from AK Kurtis APP\n" +
                "\n" +
                "My Requirement is :";
        if (!wp_b_text.equals("")){
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

    public String getWno(){
        return wp_no;
    }

    public String getwText(){
        return wp_text;
    }

    public String getWNOB(){
        return wp_no_b;
    }

    public String getWp_b_text(){
        return wp_b_text;
    }

    void startThread(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (load_id==0){
                            Toast.makeText(getActivity(), "Image Loading.. , Your internet connection is slow", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        thread.start();

    }


    void getCatlogues(){

        String tag_json_obj = "json_obj_req";
        offset =0;

        Map<String, String> postParam= new HashMap<>();
        postParam.put(KEY_CATEGORY_ID,categories.get(pos).getCat_no());
        postParam.put(KEY_C_ID,customer_session.getCustomerID());
        postParam.put(KEY_OFFSET, String.valueOf(offset));


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                DATA_CATLOGUE_URL, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            catlogues.clear();

                            String status = response.getString("status");

                            if(status.equals("200")){

                                offset = response.getInt("new_offset");
                                JSONArray jsonArray = response.getJSONArray("userData");

                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject= jsonArray.getJSONObject(i);

                                    cat_name = jsonObject.getString("catalogue_name");
                                    cat_id =jsonObject.getString("catalogue_id");
                                    tag_name = jsonObject.getString("catalogue_tag");
                                    tag_color = jsonObject.getString("catalogue_tag_color");
                                    img_url=jsonObject.getString("catalogue_image");
                                    cat_desc=jsonObject.getString("catalogue_description");
                                    String is_satisfy = jsonObject.getString("is_satisfy");

                                    catlogues.add(new Home_1(cat_name,cat_id,cat_count,tag_name,"default",tag_color,"",cat_desc,img_url,is_satisfy));
                                }

//                                onDataloadListener.onDataLoaded(arrays);

                                id =2;
                                load_id=2;
                                onDataloadListener.requiredDtata(id,lin_cat_details,lin_frag_home,p_bar,scroll_view,scroll_view_1,scroll_view_2,catlogue_layout);


                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                                catlogue_rec_view.setLayoutManager(mLayoutManager);
                                catlogue_rec_view.setItemAnimator(new DefaultItemAnimator());

                                mAdapter_2 = new Home_Adapter_2(scroll_view_1,getActivity(), catlogues, new Home_Adapter_2.ClickListener() {
                                    @Override
                                    public void onClick(View view, int position) {
                                        pos = position;
                                        startThread();
                                        cat_layout.setVisibility(GONE);
                                        lin_cat_details.setVisibility(VISIBLE);
                                        lin_frag_home.setVisibility(GONE);
                                        p_bar_1.setVisibility(View.VISIBLE);
                                        scroll_view.setVisibility(VISIBLE);
                                        scroll_view_2.setVisibility(GONE);
                                        scroll_view_1.setVisibility(View.GONE);
                                        cat_text.setText(catlogues.get(pos).getCat_name());
                                        catdesc.setText(catlogues.get(pos).getCat_desc());
                                        tag_name_text.setText("#"+catlogues.get(pos).getTag_name());
//                                        tag_name_2_text.setText("#"+catlogues.get(pos).getTag_name_2());
                                        if (catlogues.get(pos).getTag_name().equals("Default") || categories.get(pos).getTag_name().equals("") || categories.get(pos).getTag_name().equals("default")){
                                            tag_name_text.setVisibility(GONE);
                                        }

                                        if (catlogues.get(pos).getTag_name_2().equals("Default") || categories.get(pos).getTag_name_2().equals("")|| categories.get(pos).getTag_name().equals("default")){
//                                            tag_name_2_text.setVisibility(GONE);
                                        }

                                        try{
                                            tag_name_text.setBackgroundColor(Color.parseColor(catlogues.get(pos).getTag_color()));
//                                            tag_name_2_text.setBackgroundColor(Color.parseColor(catlogues.get(pos).getTag_color_2()));
                                        }catch (IllegalStateException e){
                                            e.printStackTrace();
                                        }catch (NullPointerException e){
                                            e.printStackTrace();
                                        }catch (StringIndexOutOfBoundsException e){
                                            e.printStackTrace();
                                        }catch (IllegalArgumentException e){
                                            e.printStackTrace();
                                        }
                                        getDetails();
                                    }

                                    @Override
                                    public void onLongClick(View view, int position) {

                                    }

                                    @Override
                                    public void onImgCLick(View view, int position) {
                                        pos = position;
                                        startThread();
                                        cat_layout.setVisibility(GONE);
                                        lin_cat_details.setVisibility(VISIBLE);
                                        lin_frag_home.setVisibility(GONE);
                                        p_bar_1.setVisibility(View.VISIBLE);
                                        scroll_view.setVisibility(VISIBLE);
                                        scroll_view_2.setVisibility(GONE);
                                        scroll_view_1.setVisibility(View.GONE);
                                        cat_text.setText(catlogues.get(pos).getCat_name());
                                        catdesc.setText(catlogues.get(pos).getCat_desc());
                                        tag_name_text.setText("#"+catlogues.get(pos).getTag_name());
//                                        tag_name_2_text.setText("#"+catlogues.get(pos).getTag_name_2());
                                        if (catlogues.get(pos).getTag_name().equals("Default") || categories.get(pos).getTag_name().equals("") || categories.get(pos).getTag_name().equals("default")){
                                            tag_name_text.setVisibility(GONE);
                                        }

                                        if (catlogues.get(pos).getTag_name_2().equals("Default") || categories.get(pos).getTag_name_2().equals("")|| categories.get(pos).getTag_name().equals("default")){
//                                            tag_name_2_text.setVisibility(GONE);
                                        }

                                        try{
                                            tag_name_text.setBackgroundColor(Color.parseColor(catlogues.get(pos).getTag_color()));
//                                            tag_name_2_text.setBackgroundColor(Color.parseColor(catlogues.get(pos).getTag_color_2()));
                                        }catch (IllegalStateException e){
                                            e.printStackTrace();
                                        }catch (NullPointerException e){
                                            e.printStackTrace();
                                        }catch (StringIndexOutOfBoundsException e){
                                            e.printStackTrace();
                                        }catch (IllegalArgumentException e){
                                            e.printStackTrace();
                                        }
                                        getDetails();
                                    }
                                });

                                catlogue_rec_view.setAdapter(mAdapter_2);

                                mAdapter_2.setLoadId0();
                                mAdapter_2.setOnLoadMoreListener(new Home_Adapter_2.OnLoadMoreListener() {
                                    @Override
                                    public void onLoadMore() {
                                        catlogues.add(null);
                                        mAdapter_2.notifyItemRemoved(catlogues.size());
                                        loadCatlogues();
                                    }
                                });

                                catlogue_layout.setVisibility(View.VISIBLE);
                                p_bar_2.setVisibility(GONE);
                            }

                        } catch (JSONException e) {
                            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    private void loadCatlogues() {

        String tag_json_obj = "json_obj_req";

        Map<String, String> postParam= new HashMap<>();
        postParam.put(KEY_CATEGORY_ID,categories.get(pos).getCat_no());
        postParam.put(KEY_C_ID,customer_session.getCustomerID());
        postParam.put(KEY_OFFSET, String.valueOf(offset));

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                DATA_CATLOGUE_URL, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            catlogues.remove(catlogues.size() - 1);
                            mAdapter_2.notifyItemRemoved(catlogues.size());

                            String status = response.getString("status");
                            offset = response.getInt("new_offset");

                            if(status.equals("200")){
                                JSONArray jsonArray = response.getJSONArray("userData");
                                if (jsonArray==null)
                                    mAdapter_2.setLoadID1();

                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject= jsonArray.getJSONObject(i);

                                    cat_name = jsonObject.getString("catalogue_name");
                                    cat_id =jsonObject.getString("catalogue_id");
                                    tag_name = jsonObject.getString("catalogue_tag");
                                    tag_color = jsonObject.getString("catalogue_tag_color");
                                    img_url=jsonObject.getString("catalogue_image");
                                    cat_desc=jsonObject.getString("catalogue_description");
                                    String is_satisfy = jsonObject.getString("is_satisfy");

                                    catlogues.add(new Home_1(cat_name,cat_id,cat_count,tag_name,"default",tag_color,"",cat_desc,img_url,is_satisfy));
                                }

                                catlogue_no.setText(categories.size()+" designs");

                                mAdapter_2.setLoaded();
                                mAdapter_2.notifyDataSetChanged();

                            }

                        } catch (JSONException e) {
                            mAdapter_2.setLoadID1();
                            mAdapter_2.setLoaded();
                            mAdapter_2.notifyDataSetChanged();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                mAdapter_2.setLoadID1();
                mAdapter_2.setLoaded();
                mAdapter_2.notifyDataSetChanged();
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }



}
