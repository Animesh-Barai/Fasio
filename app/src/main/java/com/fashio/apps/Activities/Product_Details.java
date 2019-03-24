package com.fashio.apps.Activities;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fashio.apps.Adapters.Cat_New_Adapter;
import com.fashio.apps.Adapters.Review_Adapter;
import com.fashio.apps.Applications.AppController;
import com.fashio.apps.Drawable.BadgeDrawable;
import com.fashio.apps.Model.Review;
import com.fashio.apps.Model.Selection;
import com.fashio.apps.R;
import com.fashio.apps.Session.Customer_Session;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import customfonts.Zoom.TouchImageView;


public class Product_Details extends AppCompatActivity {

    private static final String KEY_C_ID = "c_id";
    private static final String PIN_URL = "https://www.bukarte.com/rest_server/validatePostalCode/API-KEY/123456";
    private static final String KEY_PIN = "location";
    private static final String REVIEW_URL = "https://apps.itrifid.com/fashio/rest_server/productReview/API-KEY/123456";
    private static final String REVIEW_POST_URL = "https://apps.itrifid.com/fashio/rest_server/putUserFeedback/API-KEY/123456";
    private String cart_count="0";
    private static final String CART_GET_URL ="https://apps.itrifid.com/fashio/rest_server/selectUserCart/API-KEY/123456";
    private static final String CART_URL ="https://apps.itrifid.com/fashio/rest_server/userCart/API-KEY/123456";
    private static final String KEY_PRODUCT_ID = "product_id";
    private static final String KEY_PRODUCT_NAME = "product_name";
    private static final String KEY_ACTUAL_PRICE = "actual_price";
    private static final String KEY_SELLING_PRICE = "selling_price";
    private static final String KEY_IMG_URL = "product_image";
    private static final String KEY_PRODUCT_WEIGHT = "product_weight";
    private static final String KEY_CATLOG_ID = "catalog_id";
    private static final String KEY_RES_ID = "res_id";
    private static final String KEY_COD_AVAILABLE = "is_cod_available";
    private static final String KEY_QTY = "qty";
    private static final String KEY_PRODUCT_SIZE = "product_size";
    private ViewPager viewPager;
    private Button btn_cart;
    private TextView cat_name;
    private static final String FAV_URL = "https://apps.itrifid.com/fashio/rest_server/userlikelist/API-KEY/123456";
    private static final String KEY_ID = "c_id";
    private static final String PRODUCT_ID = "product_id";
    private List<Selection> images;
    private RelativeLayout btn_share;
    private Button btn_download;
    private int selectedPosition = 0;
    private MyViewPagerAdapter myViewPagerAdapter;
    private TouchImageView full_image;
    private ImageView heart;
    private TextView original_price;
    private TextView selling_price;
    private TextView cat_desc;
    private Customer_Session customer_session;
    private TextView count_all;
    private String cat_name_s;
    private int id =0;
    private String img_name;
    private RelativeLayout loader_layout;
    private TextView loading_text;
    private String p_no;
    private TextView product_id_text;
    private TextView sizes_available;
    private TextView cod_available;
    private String catlog_id;
    private Dialog bottom_dialog;
    private Cat_New_Adapter size_adapter;
    private Cat_New_Adapter qty_adapter;
    private RecyclerView size_rec;
    private RecyclerView qty_list;
    private List<String> qty= new ArrayList<>();
    private Button btn;
    private ProgressDialog mProgressDialog;
    String size_avail="";
    private Toolbar toolbar;
    private TextView title_toolbar;
    private RelativeLayout review_layout;
    private RecyclerView recyclerView;
    private LinearLayout review_lin;
    private Button rate_btn;
    private List<Review> reviews = new ArrayList<>();
    private Dialog rate_dialog;
    private RatingBar ratingBar;
    private EditText edit_review;
    private EditText pin_edit;
    private TextView deliv_text;
    private TextView check_btn;
    private String pin_code_entered;
    private int pin_pos=0;
    private List<String> delivery=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        Intent intent=getIntent();
        Bundle extras = intent.getExtras();

        customer_session=new Customer_Session(getApplicationContext());

        if (extras!=null){
            Bundle data = extras.getBundle("ca");
            images= (List<Selection>) data.getSerializable("images");
            selectedPosition = extras.getInt("pos");
            id = extras.getInt("id");
            cat_name_s = extras.getString("cat_name");
            p_no=extras.getString("pNo");
            catlog_id = extras.getString("catlog_id");

            if (images.get(0).getSize_avail().get(0).equals("false")){
                //do nothing..
            }else {

            }

            for(int i=0;i<images.size();i++){
                delivery.add("0");
            }

        }


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title_toolbar = findViewById(R.id.toolbar_title);

        bottom_dialog  = new Dialog(Product_Details.this,R.style.BottomDialog);
        bottom_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        bottom_dialog.getWindow().getAttributes().windowAnimations = R.style.CustomDialogAnimation;
        layoutParams.copyFrom(bottom_dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.BOTTOM;
        bottom_dialog.getWindow().setAttributes(layoutParams);
        bottom_dialog.setCancelable(true);
        bottom_dialog.setContentView(R.layout.quantity_dialog);

        rate_dialog  = new Dialog(Product_Details.this,R.style.BottomDialog);
        rate_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams1 = new WindowManager.LayoutParams();
        rate_dialog.getWindow().getAttributes().windowAnimations = R.style.CustomDialogAnimation;
        layoutParams1.copyFrom(rate_dialog.getWindow().getAttributes());
        layoutParams1.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams1.height = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams1.gravity = Gravity.BOTTOM;
        rate_dialog.getWindow().setAttributes(layoutParams1);
        rate_dialog.setCancelable(true);
        rate_dialog.setContentView(R.layout.rate_dialog);


        ratingBar = rate_dialog.findViewById(R.id.rate_bar);
        edit_review = rate_dialog.findViewById(R.id.input_review);

        rate_btn = rate_dialog.findViewById(R.id.review_btn);
        rate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ratingBar.getRating()!=0 && !edit_review.getText().toString().trim().equals("")){
                    showProgressDialog();
                    rate_dialog.dismiss();
                    rateandReview();
                }else {
                    Toast.makeText(Product_Details.this, "Please rate and review", Toast.LENGTH_SHORT).show();
                }
            }
        });

        qty.add("1");
        qty.add("2");
        qty.add("3");
        qty.add("4");
        qty.add("5");
        qty.add("6");
        qty.add("7");
        qty.add("8");
        qty.add("9");
        qty.add("10");

        size_rec= bottom_dialog.findViewById(R.id.r_view);
        qty_list = bottom_dialog.findViewById(R.id.r_view_1);

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        qty_list.setLayoutManager(horizontalLayoutManager);
        qty_list.setItemAnimator(new DefaultItemAnimator());

        qty_adapter=new Cat_New_Adapter(getApplicationContext(),qty);
        qty_list.setAdapter(qty_adapter);

        btn= bottom_dialog.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(qty_adapter.getSelectedPosition()!=-1 && size_adapter.getSelectedPosition()!=-1){
                    bottom_dialog.dismiss();
                    showProgressDialog();
                    addToCart();
                }else {
                    Toast.makeText(Product_Details.this, "Please select the size and quantity", Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewPager = findViewById(R.id.viewpager);

        myViewPagerAdapter = new MyViewPagerAdapter();

        viewPager.setAdapter(myViewPagerAdapter);

        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        setCurrentItem(selectedPosition);

        getCartProducts();
    }

    private void rateandReview() {

        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        Map<String, String> postParam= new HashMap<>();
        postParam.put("res_id",customer_session.getCustomerID());
        postParam.put("product_id",images.get(selectedPosition).getImg_id());
        postParam.put("catalogue_id",catlog_id);
        postParam.put("rating", String.valueOf(ratingBar.getRating()));
        postParam.put("review",edit_review.getText().toString().trim());

        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                REVIEW_POST_URL, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        hideProgressDialog();

                        try{
                            String status = response.getString("status");
                            if (status.equals("200")){
                                Toast.makeText(Product_Details.this, "Thanks for Rating :)", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(Product_Details.this, "You have rated this product", Toast.LENGTH_SHORT).show();
                            }
                            invalidateOptionsMenu();

                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                Toast.makeText(Product_Details.this, "Connection Error , Try Again", Toast.LENGTH_SHORT).show();
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

    //  adapter
    public class MyViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;


        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.product_details_layout, container, false);

            deliv_text = view.findViewById(R.id.pin_text);
            deliv_text.setTag(position);

            if (delivery.get(position).equals("0")){
                deliv_text.setText("Usually Deliver in 7-10 days..");
            }else if (delivery.get(position).equals(String.valueOf(position+12))){
                deliv_text.setText("This product is available at your pin code");
                deliv_text.setTextColor(getResources().getColor(R.color.deliv));
            }else if (delivery.get(position).equals(String.valueOf(position+11))){
                deliv_text.setText("Incorrect Pin !!");
                deliv_text.setTextColor(getResources().getColor(R.color.colorPrimary));

            }

            pin_edit = (EditText) view.findViewById(R.id.pin_check);
            pin_edit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    pin_code_entered= editable.toString();
                }
            });



            check_btn = view.findViewById(R.id.btn_check);
            check_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (pin_code_entered.equals("")){
                        Toast.makeText(Product_Details.this, "Enter your pin code please..", Toast.LENGTH_SHORT).show();
                    }else {
                        showProgressDialog();
                        validatePin(selectedPosition);
                    }
                }
            });

            cat_name = view.findViewById(R.id.cat_name_text);
            cat_name.setText(images.get(selectedPosition).getImage_name());

            count_all = (TextView) view.findViewById(R.id.count_all);

            full_image = (TouchImageView) view.findViewById(R.id.image_preview);
            full_image.setMaxZoom(4f);

            full_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(),Full_Image.class);
                    Bundle data = new Bundle();
                    data.putSerializable("images", (Serializable) images);
                    intent.putExtra("ca",data);
                    intent.putExtra("pos",position);
                    intent.putExtra("cat_name",images.get(position).getCat_name());
                    intent.putExtra("id",id);
                    intent.putExtra("pNo",p_no);
                    intent.putExtra("catlog_id",catlog_id);
                    startActivity(intent);
                }
            });

            heart = (ImageView) view.findViewById(R.id.fav_add);

            cat_desc = view.findViewById(R.id.desc_text);
            cat_desc.setText(images.get(position).getCat_desc());

            original_price = (TextView) view.findViewById(R.id.price_original);
            selling_price = (TextView) view.findViewById(R.id.price);

            sizes_available = view.findViewById(R.id.size_available);
            if (images.get(selectedPosition).getSize_avail().get(0).equals("false")){
                sizes_available.setText("No Size Available");
            }else {
                size_avail="";
                for (int i=0;i<images.get(selectedPosition).getSize_avail().size();i++){

                    size_avail+=images.get(selectedPosition).getSize_avail().get(i)+",";
                }
                sizes_available.setText(size_avail);
            }

            cod_available = view.findViewById(R.id.cod_available_text);
            if (images.get(selectedPosition).getCod_avail().equals("0")){
                cod_available.setText("Cash On Delivery ✗");
            }else {
                cod_available.setText("Cash On Delivery ✓");
            }

            try {

                Picasso.with(getApplicationContext()).load(images.get(position).getImg_url()).noFade().into(full_image, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {

                    }
                });

            } catch (NullPointerException e) {
                Toast.makeText(getApplicationContext(), "Wait till the image get loaded", Toast.LENGTH_LONG).show();
            }

            if (images.get(position).getLiked()){
                heart.setImageResource(R.drawable.heart_re);
            }else{
                heart.setImageResource(R.drawable.heart_outline);
            }

            count_all.setText(images.get(position).getCount_all());
//            if (id==1){
//                cat_name.setText(images.get(position).getImage_name());
//            }else if (id==2){
//                cat_name.setText(images.get(position).getCat_name());
//            }else if (id==3){
//                cat_name.setText(images.get(position).getCat_name());
//                heart.setVisibility(View.GONE);
//                count_all.setVisibility(View.GONE);
//            }else {
//                cat_name.setText(cat_name_s);
//            }

            recyclerView = view.findViewById(R.id.review_r_view);
            recyclerView.setNestedScrollingEnabled(false);

            review_layout = view.findViewById(R.id.review_layout);

            product_id_text = (TextView) view.findViewById(R.id.product_id_text);

            product_id_text.setText("Product ID - "+images.get(position).getImg_id());

            title_toolbar.setText("Product ID - "+images.get(position).getImg_id());

            original_price.setText("₹"+images.get(position).getActual_price());

            original_price.setPaintFlags(original_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            if(images.get(position).getActual_price().equals("0.00")){
                original_price.setVisibility(View.GONE);
            }

            rate_btn = view.findViewById(R.id.btn_rate);
            rate_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rate_dialog.show();
                }
            });

            selling_price.setText("₹"+images.get(position).getPrice());

            heart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addToFav((ImageView) v,images.get(position).getImg_id(),position);
                }
            });

            review_lin = view.findViewById(R.id.review_lin);

            full_image.setTag(position);

            full_image.setId(position);

            review_lin.setVisibility(View.GONE);

            getReviews();

            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == (obj);
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    private void setCurrentItem(final int pos) {
        selectedPosition=pos;
        viewPager.setCurrentItem(pos, false);
        displayMetaInfo(selectedPosition);
    }

    //  page change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            displayMetaInfo(position);
            selectedPosition=position;
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    @SuppressLint("SetTextI18n")
    private void displayMetaInfo(final int position) {


        btn_cart = findViewById(R.id.cart_btn);
        btn_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (images.get(selectedPosition).getSize_avail().get(0).equals("false")){
                    Toast.makeText(Product_Details.this, "Size Not Available", Toast.LENGTH_SHORT).show();
                }else {
                    showSIzeAndQtyDiaog();
                }
            }
        });
    }


    private void addToFav(final ImageView v,String img_id,int pos) {


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
                                count_all.setText(response.getString("all_like_counter"));
                            }else if (status.equals("208")){
                                v.setImageResource(R.drawable.heart_outline);
                                count_all.setText(response.getString("all_like_counter"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_SHORT).show();
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    void shareImage() {


        //Uri bmpUri = getLocalBitmapUri(imageView);
        ImageView Imgv = (ImageView)viewPager.findViewWithTag(viewPager.getCurrentItem());

        Drawable drawable = Imgv.getDrawable();
        Bitmap bmp = null;

        try {
            if (drawable instanceof BitmapDrawable)
                bmp = ((BitmapDrawable) drawable).getBitmap();
            else {

                Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                drawable.draw(canvas);
                bmp = bitmap;


            }
        }catch (NullPointerException e){
            Toast.makeText(getApplicationContext(),"Wait till the image get loaded",Toast.LENGTH_SHORT).show();
        }


        Uri bmpUri = null;
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);

            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (NullPointerException e) {
            Toast.makeText(getApplicationContext(), "Wait till the image get loaded", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (bmpUri != null) {
            // Construct a ShareIntent with link to image
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            shareIntent.setType("image/*");
            shareIntent.setPackage("com.whatsapp");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            shareIntent.putExtra(Intent.EXTRA_TEXT,"I want to buy this");
            try {
                shareIntent.putExtra("jid", p_no+ "@s.whatsapp.net"); //phone number without "+" prefix
                startActivity(shareIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getApplicationContext(), "Please make sure whatsapp is installed on your device", Toast.LENGTH_LONG).show();
            }
        } else {
            // ...sharing failed, handle error
        }
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



    public void downloadImage(String imgloc, final String Dir) {

        Picasso.with(getApplicationContext())
                .load(imgloc)
                .into(new Target() {
                          @Override
                          public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
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
                                  MediaScannerConnection.scanFile(getApplicationContext(),
                                          new String[]{myDir.getAbsolutePath()}, null, null);


                                  Log.i("TAG", "Agter scanning" + myDir.getAbsolutePath());

                              } catch (Exception e) {
                                  // some action
                              }


                              String root = Environment.getExternalStorageDirectory().toString();

                              File filePath = new File(root + "/~AK " + Dir);

                              sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(String.valueOf(filePath)))));

                              Toast.makeText(Product_Details.this, "Image Downloaded successfully", Toast.LENGTH_SHORT).show();
                          }

                          @Override
                          public void onBitmapFailed(Drawable errorDrawable) {
                          }

                          @Override
                          public void onPrepareLoad(Drawable placeHolderDrawable) {
                          }
                      }
                );

    }


    void showSIzeAndQtyDiaog(){

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        size_rec.setLayoutManager(horizontalLayoutManager);
        size_rec.setItemAnimator(new DefaultItemAnimator());

        size_adapter = new Cat_New_Adapter(getApplicationContext(), images.get(selectedPosition).getSize_avail());
        size_rec.setAdapter(size_adapter);

        bottom_dialog.show();
    }


    private void addToCart(){

        String tag_json_obj = "json_obj_req";

        Map<String, String> postParam= new HashMap<>();
        postParam.put(KEY_PRODUCT_ID,images.get(selectedPosition).getImg_id());
        postParam.put(KEY_PRODUCT_NAME,images.get(selectedPosition).getImage_name());
        postParam.put(KEY_QTY, qty.get(qty_adapter.getSelectedPosition()));
        postParam.put(KEY_SELLING_PRICE,images.get(selectedPosition).getPrice());
        postParam.put(KEY_ACTUAL_PRICE,images.get(selectedPosition).getActual_price());
        postParam.put(KEY_IMG_URL,images.get(selectedPosition).getImg_url());
        postParam.put(KEY_PRODUCT_WEIGHT,images.get(selectedPosition).getWeight());
        postParam.put(KEY_CATLOG_ID,catlog_id);
        postParam.put(KEY_RES_ID,customer_session.getCustomerID());
        postParam.put(KEY_COD_AVAILABLE,images.get(selectedPosition).getCod_avail());
        postParam.put(KEY_PRODUCT_SIZE,images.get(selectedPosition).getSize_avail().get(size_adapter.getSelectedPosition()));

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                CART_URL, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        hideProgressDialog();
                        try {
                            String status = response.getString("status");

                            if (status.equals("202")){
                                Toast.makeText(Product_Details.this, "Item Added to Cart", Toast.LENGTH_SHORT).show();
                                getCartProducts();
                            }else if (status.equals("208")){
                                 Toast.makeText(Product_Details.this, "Connection Error", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_SHORT).show();
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }


    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
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

            case R.id.rate:
                rate_dialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_p, menu);

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


    @Override
    protected void onResume() {
        super.onResume();
        getCartProducts();
    }

    private void getCartProducts(){

        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        Map<String, String> postParam= new HashMap<>();
        postParam.put("res_id",customer_session.getCustomerID());


        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                CART_GET_URL, new JSONObject(postParam),
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



    void getReviews(){

        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        Map<String, String> postParam= new HashMap<>();
        postParam.put("product_id",images.get(selectedPosition).getImg_id());

        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                REVIEW_URL, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try{
                            String status = response.getString("status");
                            if (status.equals("202")){

                                reviews.clear();

                                JSONArray jsonArray = response.getJSONArray("all_review");
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String review = jsonObject.getString("review");
                                    String name = jsonObject.getString("full_name");
                                    String rating = jsonObject.getString("rating");

                                    reviews.add(new Review(review,rating,name));
                                }

                                Review_Adapter review_adapter = new Review_Adapter(getApplicationContext(),reviews );

                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());

                                recyclerView.setAdapter(review_adapter);
                                review_lin.setVisibility(View.VISIBLE);
                            }else {
                                review_lin.setVisibility(View.GONE);
                            }
                            invalidateOptionsMenu();

                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                review_lin.setVisibility(View.GONE);
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

    void validatePin(final int position){

        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        Map<String, String> postParam= new HashMap<>();
        postParam.put(KEY_PIN,pin_code_entered);

        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                PIN_URL, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String status = response.getString("status");
                            if (status.equals("200")){

                                JSONObject jsonObject = response.getJSONObject("response");

                                delivery.set(position, String.valueOf(position+12));
                                myViewPagerAdapter.notifyDataSetChanged();

                                Toast.makeText(Product_Details.this, "Product Available at your location", Toast.LENGTH_SHORT).show();
                            }else {
                                delivery.set(position, String.valueOf(position+11));
                                myViewPagerAdapter.notifyDataSetChanged();
                                Toast.makeText(Product_Details.this, "Currently we are not delivering to your location", Toast.LENGTH_SHORT).show();
                            }

                            hideProgressDialog();

                        } catch (JSONException e) {
                            Toast.makeText(Product_Details.this, e.toString(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                Toast.makeText(getApplicationContext(),"Connection Error , Try Again", Toast.LENGTH_LONG).show();
            }
        }) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }



        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }


}
