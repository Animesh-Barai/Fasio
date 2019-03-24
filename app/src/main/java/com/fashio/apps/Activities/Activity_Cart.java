package com.fashio.apps.Activities;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fashio.apps.Adapters.Cart_Adapter;
import com.fashio.apps.Applications.AppController;
import com.fashio.apps.Model.Cart;
import com.fashio.apps.R;
import com.fashio.apps.Session.Customer_Session;
import com.fashio.apps.Session.Name_Session;
import com.fashio.apps.Session.Phone_Session;
import com.fashio.apps.Session.Session_Manager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Activity_Cart extends AppCompatActivity {


    private Dialog seller_dialog;
    private EditText edit_name;
    private EditText edit_phone;
    private EditText edit_margin;
    private EditText edit_notes;
    private EditText edit_seller_name;
    private static final String CART_URL = "https://apps.itrifid.com/fashio/rest_server/selectUserCart/API-KEY/123456" ;
    private static final String DELETE_CART_URL = "https://apps.itrifid.com/fashio/rest_server/deleteCartProduct/API-KEY/123456" ;
    private static final String KEY_ID = "res_id";
    private static final String UPDATE_CART_URL = "https://apps.itrifid.com/fashio/rest_server/updateCartProductQty/API-KEY/123456";
    private static final String PRODUCT_URL = "https://apps.itrifid.com/fashio/rest_server/carttest/API-KEY/123456";
    private static final String KEY_CUSTOMER_ID = "c_id";
    private static final String KEY_PRODUCT_ID = "product_id";
    private static final String KEY_BOOK_NAME = "product_name";
    private static final String KEY_PRICE = "product_price";
    private static final String KEY_IMG = "img_url";
    private Customer_Session customer_session;
    private RecyclerView recycler;
    private Cart_Adapter mAdapter;
    private Toolbar toolbar;
    private ProgressBar progressBar;
    private TextView textView;
    private Button checkout_btn;
    private List<Cart> products;
    private RelativeLayout empty_cart;
    private RelativeLayout cart;
    private Session_Manager session_manager;
    private double total_price;
    private ProgressDialog mProgressDialog;
    static Activity_Cart activityA;
    private String product_total;
    private String product_weight;
    private String cod_cost;
    private String shipment_cost;
    private Name_Session name_session;
    private Phone_Session phone_session;
    private FrameLayout btn_ok;
    private String is_cod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        activityA = this;

        recycler = (RecyclerView) findViewById(R.id.recycler_view);

        recycler.setNestedScrollingEnabled(false);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        phone_session= new Phone_Session(getApplicationContext());
        name_session = new Name_Session(getApplicationContext());
        customer_session = new Customer_Session(getApplicationContext());

        checkout_btn = (Button) findViewById(R.id.check);

        textView = (TextView) findViewById(R.id.my_cart);

        progressBar = (ProgressBar) findViewById(R.id.progress_Bar_cart);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/shiv5.ttf");

        session_manager = new Session_Manager(getApplicationContext());

        products = new ArrayList<>();


        empty_cart = (RelativeLayout) findViewById(R.id.empty_cart_layout);

        cart = (RelativeLayout) findViewById(R.id.cart_layout);

        seller_dialog  = new Dialog(Activity_Cart.this,R.style.BottomDialog);
        seller_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        seller_dialog.getWindow().getAttributes().windowAnimations = R.style.CustomDialogAnimation;
        layoutParams.copyFrom(seller_dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.BOTTOM;
        seller_dialog.getWindow().setAttributes(layoutParams);
        seller_dialog.setCancelable(true);
        seller_dialog.setContentView(R.layout.seller_request_dialog);

        edit_name = (EditText) seller_dialog.findViewById(R.id.input_name);
        edit_name.setText(name_session.getName());
        edit_phone = (EditText) seller_dialog.findViewById(R.id.input_phone);
        edit_phone.setText(phone_session.getPhoneNO());
        edit_notes = (EditText) seller_dialog.findViewById(R.id.input_nnote);

        edit_margin = (EditText) seller_dialog.findViewById(R.id.input_margin);
        edit_seller_name = (EditText) seller_dialog.findViewById(R.id.input_seller_name);

        btn_ok = (FrameLayout) seller_dialog.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edit_phone.getText().toString().trim().equals("") && !edit_notes.getText().toString().trim().equals("") && !edit_margin.getText().toString().trim().equals("") && !edit_seller_name.getText().toString().trim().equals("")){
                    Intent intent = new Intent(getApplicationContext(),Payment_Activity_1.class);
                    Bundle data = new Bundle();
                    data.putSerializable("images", (Serializable) products);
                    intent.putExtra("ca",data);
                    intent.putExtra("product_total",product_total);
                    intent.putExtra("product_weight",product_weight);
                    intent.putExtra("ship_cost",shipment_cost);
                    intent.putExtra("cod_cost",cod_cost);
                    intent.putExtra("name",name_session.getName());
                    intent.putExtra("phone",edit_phone.getText().toString().trim());
                    intent.putExtra("notes",edit_notes.getText().toString().trim());
                    intent.putExtra("margin",edit_margin.getText().toString().trim());
                    intent.putExtra("seller_name",edit_seller_name.getText().toString().trim());
                    intent.putExtra("is_cod",is_cod);
                    startActivity(intent);
                }else {
                    Toast.makeText(Activity_Cart.this, "Please fill it out..", Toast.LENGTH_SHORT).show();
                }
            }
        });

        getCartProducts();
        checkout_btn.setVisibility(View.GONE);
        cart.setVisibility(View.GONE);
        empty_cart.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        checkout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seller_dialog.show();
            }
        });

    }

    void refreshAdapter_Login(){



    }

    @Override
    protected void onResume() {
        super.onResume();

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {


       //do nothing
        }else{
        }


    }


    private void getCartProducts(){

        products.clear();

        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        Map<String, String> postParam= new HashMap<>();
        postParam.put(KEY_ID,customer_session.getCustomerID());


        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                CART_URL, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            String status = response.getString("status");

                            if(status.equals("200")){

                                is_cod=response.getString("is_cod");
                                JSONArray jsonArray = response.getJSONArray("cart_item");
                                product_total = response.getString("cart_total");
                                shipment_cost = String.valueOf(response.getInt("shipemnt_cost"));
                                if (response.getString("is_cod").equals("0")){
                                    cod_cost = "0";
                                }else {
                                    cod_cost = response.getString("cod_cost");
                                }
                                product_weight = response.getString("cart_weight");

                                for(int i=0;i<jsonArray.length();i++){

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    JSONObject properties = jsonObject.getJSONObject("properties");

                                    String book_id = properties.getString("product_id");
                                    String product_name = properties.getString("product_name");
                                    String price = properties.getString("selling_price");
                                    String qty = properties.getString("qty");
                                    String img_url = properties.getString("product_image");
                                    String product_size = properties.getString("product_size");
                                    String product_weight = properties.getString("product_weight");

                                    Cart cart = new Cart(book_id,product_name,price,qty,img_url,product_size,product_weight);
                                    products.add(cart);

                                }


                                if (!products.isEmpty()) {

                                    progressBar.setVisibility(View.INVISIBLE);
                                    empty_cart.setVisibility(View.GONE);
                                    cart.setVisibility(View.VISIBLE);
                                    checkout_btn.setVisibility(View.VISIBLE);


                                    mAdapter = new Cart_Adapter(getApplicationContext(), products, new Cart_Adapter.ClickListener() {
                                        @Override
                                        public void onClick(View view, int position) {

                                        }

                                        @Override
                                        public void onLongClick(View view, int position) {

                                        }

                                        @Override
                                        public void onDeleteClick(View view, int position) {
                                            view.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation));
                                            showProgressDialog();
                                            mProgressDialog.setMessage("Deleting..");

                                            Cart product = products.get(position);
                                            delete_cart_url(product.getBook_id(),position,product.getBookPrice(),product.getQuantity());

                                        }


                                        @Override
                                        public void onPlusClick(View view, int position) {

                                            int quant = Integer.parseInt(products.get(position).getQuantity()) + 1;

                                            if (quant<=10){
                                                showProgressDialog();
                                                mProgressDialog.setMessage("Updating..");
                                                update_cart_url(products.get(position).getBook_id(), String.valueOf(quant));
                                            }else
                                                Toast.makeText(Activity_Cart.this, "Quantity should not be more than 10", Toast.LENGTH_SHORT).show();

                                        }

                                        @Override
                                        public void onMinusClick(View view, int position) {

                                            int quant = Integer.parseInt(products.get(position).getQuantity()) -1;

                                            if (quant>=1){
                                                showProgressDialog();
                                                mProgressDialog.setMessage("Updating..");
                                                update_cart_url(products.get(position).getBook_id(), String.valueOf(quant));
                                            }

                                        }

                                        @Override
                                        public void onItemClick(View view, int position) {

                                            Intent intent1=new Intent(getApplicationContext(),Activity_Cart.class);
                                            intent1.putExtra("id",products.get(position).getBook_id());
                                            intent1.putExtra("cat_id","");
                                            intent1.putExtra("class_id","");
                                            intent1.putExtra("sub_id","");
                                            startActivity(intent1);
                                        }

                                    });


                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                    recycler.setLayoutManager(mLayoutManager);
                                    recycler.setItemAnimator(new DefaultItemAnimator());
                                    recycler.setAdapter(mAdapter);

                                }
                            }else if (status.equals("205")){

                                checkout_btn.setVisibility(View.GONE);
                                empty_cart.setVisibility(View.VISIBLE);
                                cart.setVisibility(View.GONE);
                                progressBar.setVisibility(View.INVISIBLE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Activity_Cart.this, "Connection Error", Toast.LENGTH_SHORT).show();
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


    private void delete_cart_url(final String book_id, final int pos, final String bookPrice, final String quantity) {

        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        Map<String, String> postParam= new HashMap<>();
        postParam.put("res_id",customer_session.getCustomerID());
        postParam.put("product_id",book_id);

        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                DELETE_CART_URL, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        hideProgressDialog();
                        try {
                            String status = response.getString("status");

                            products.remove(pos);
                            mAdapter.notifyDataSetChanged();


                            if(status.equals("202")){

                                if (products.isEmpty()) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    checkout_btn.setVisibility(View.GONE);
                                    empty_cart.setVisibility(View.VISIBLE);
                                    cart.setVisibility(View.GONE);
                                }else {
                                    restartFirstActivity();
                                }

                            }

                            hideProgressDialog();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                Toast.makeText(Activity_Cart.this, "Connection Error", Toast.LENGTH_SHORT).show();
            }
        }) {

            /**
             * Passing some request headers
             * */
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


    private void update_cart_url(String book_id,String quantity) {

        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        Map<String, String> postParam= new HashMap<>();
        postParam.put("res_id",customer_session.getCustomerID());
        postParam.put("product_id",book_id);
        postParam.put("qty",quantity);

        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                UPDATE_CART_URL, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        hideProgressDialog();
                        try {
                            String status = response.getString("status");

                            if(status.equals("202")){
                                restartFirstActivity();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                Toast.makeText(Activity_Cart.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {

            /**
             * Passing some request headers
             * */
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



    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Deleting");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void addToCart(final int position) {

        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";
        Map<String, String> postParam= new HashMap<>();
        postParam.put(KEY_CUSTOMER_ID,customer_session.getCustomerID());
        postParam.put(KEY_PRODUCT_ID,products.get(position).getBook_id());
        postParam.put(KEY_BOOK_NAME,products.get(position).getBookName());
        postParam.put(KEY_PRICE ,products.get(position).getQuantity());
        postParam.put(KEY_IMG ,products.get(position).getImgUrl());

        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                PRODUCT_URL, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");

                            Cart cart = products.get(position);

                            if(status.equals("202")){

                            }

                            if (position==products.size()-1){
                                getCartProducts();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                Toast.makeText(Activity_Cart.this, "Connection error", Toast.LENGTH_SHORT).show();
            }
        }) {

            /**
             * Passing some request headers
             * */
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

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void restartFirstActivity()
    {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
