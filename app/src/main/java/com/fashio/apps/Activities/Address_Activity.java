package com.fashio.apps.Activities;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fashio.apps.Adapters.Select_Adapter;
import com.fashio.apps.Applications.AppController;
import com.fashio.apps.Model.Address;
import com.fashio.apps.Model.Cart;
import com.fashio.apps.R;
import com.fashio.apps.Session.Customer_Session;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Address_Activity extends AppCompatActivity implements PaymentResultListener {


    private static final String DELETE_CART_URL = "https://apps.itrifid.com/fashio/rest_server/deleteCartProduct/API-KEY/123456" ;
    private static final String RESPONSE_URL = "https://apps.itrifid.com/fashio/rest_server/paymentResponse/API-KEY/123456";
    private static final String ADD_ID =  "address_id";
    private static final String KEY_MOBILE = "cus_mobile";
    private static final String KEY_C_ID  = "cus_id";
    private static final String KEY_PRODUCT_ID = "product_id";
    private static final String KEY_PRODUCT_PRICE = "product_price";
    private static final String KEY_MARGIN = "reseller_margin";
    private static final String KEY_TOTAL = "invoice_total";
    private static final String KEY_NOTES = "notes";
    private static final String ORDER_URL ="https://apps.itrifid.com/fashio/rest_server/getUserOrder/API-KEY/123456";
    private static final String ADD_NEW_URL = "https://apps.itrifid.com/fashio/rest_server/saveUserAddress/API-KEY/123456";
    private static final String ADDRESS_URL = "https://apps.itrifid.com/fashio/rest_server/retrieveUserAddress/API-KEY/123456";
    private static final String KEY_ID = "reseller_id";
    private static final String KEY_NAME = "cus_name";
    private static final String KEY_PHONE = "mobile_number";
    private static final String KEY_ADDRESS_1 = "add_l1";
    private static final String KEY_ADDRESS_2 = "add_l2";
    private static final String KEY_LANDMARK = "landmark";
    private static final String KEY_PIN_CODE = "pin";
    private static final String KEY_CITY = "city";
    private static final String KEY_STATE = "state";
    private static final String KEY_COUNTRY = "country";
    private static final String KEY_PAY = "pay_mode";
    private static final String TAG = Main_Handler_Activity.class.getSimpleName();
    private Customer_Session customer_session;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private List<Address> addresses=new ArrayList<>();
    private Select_Adapter mAdapter;
    static Address_Activity activity;
    private ScrollView scrollView;
    private ProgressDialog mProgressDialog;
    private EditText name;
    private EditText et_phone;
    private EditText et_address;
    private EditText et_address_2;
    private EditText et_landmark;
    private EditText et_pin;
    private EditText et_city;
    private EditText et_state;
    private Button cont;
    private int add = 0;
    private Toolbar toolbar;
    private String pay_mode;
    private String base_amount;
    private String tot_amount;
    private String cod_charge;
    private String margin;
    private String product_id;
    private int add_pos;
    private Double amount=0.0;
    private String item_total;
    private String c_name;
    private String c_phone;
    private String notes;
    private int id=0;
    private String order_id;
    private static final String KEY_RES_ID = "res_id";
    private static final String KEY_ORDER_ID = "order_id";
    private static final String KEY_PAYMENT_STATUS = "payment_status";
    private String ship_charge="";
    private String product_weight="";
    private List<Cart> products;
    private String wallet_used="";
    private String seller_name="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_);


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras!=null){
            Bundle data = extras.getBundle("ca");
            products= (List<Cart>) data.getSerializable("images");
            pay_mode = extras.getString("pay_mode");
            base_amount = extras.getString("product_total");
            tot_amount = String.valueOf(extras.getDouble("invoice_total"));
            cod_charge = extras.getString("cod_cost");
            ship_charge = extras.getString("ship_cost");
            margin = extras.getString("margin");
            c_name = extras.getString("name");
            c_phone = extras.getString("phone");
            notes = extras.getString("notes");
            product_weight = extras.getString("product_weight");
            wallet_used = String.valueOf(extras.getDouble("wallet_used"));
            seller_name = extras.getString("seller_name");

            if (pay_mode.equals("op")){
                amount = Double.valueOf(tot_amount) * 100;
            }
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        activity = this;

        customer_session = new Customer_Session(getApplicationContext());
        progressBar = (ProgressBar) findViewById(R.id.p_bar);
        recyclerView = (RecyclerView) findViewById(R.id.r_view);
        scrollView = (ScrollView) findViewById(R.id.new_add);

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/arial.ttf");
        name = (EditText) findViewById(R.id.input_first_name);
        name.setTypeface(tf);
        ((TextInputLayout) findViewById(R.id.name_layout)).setTypeface(tf);
        et_phone = (EditText) findViewById(R.id.input_phone);
        et_phone.setTypeface(tf);
        ((TextInputLayout) findViewById(R.id.phone_layout)).setTypeface(tf);
        et_address = (EditText) findViewById(R.id.input_address);
        et_address.setTypeface(tf);
        ((TextInputLayout) findViewById(R.id.address)).setTypeface(tf);
        et_address_2 = (EditText) findViewById(R.id.input_address_2);
        et_address_2.setTypeface(tf);
        ((TextInputLayout) findViewById(R.id.address_2)).setTypeface(tf);
        et_landmark = (EditText) findViewById(R.id.input_landmark);
        et_landmark.setTypeface(tf);
        ((TextInputLayout) findViewById(R.id.landmark)).setTypeface(tf);
        et_pin = (EditText) findViewById(R.id.input_pin);
        et_pin.setTypeface(tf);
        ((TextInputLayout) findViewById(R.id.pin)).setTypeface(tf);
        et_city = (EditText) findViewById(R.id.input_city);
        et_city.setTypeface(tf);
        ((TextInputLayout) findViewById(R.id.city)).setTypeface(tf);
        et_state = (EditText) findViewById(R.id.input_state);
        et_state.setTypeface(tf);
        ((TextInputLayout) findViewById(R.id.state)).setTypeface(tf);

        cont = (Button) findViewById(R.id.continue_btn);
        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!name.getText().toString().isEmpty() && !et_phone.getText().toString().isEmpty() && !et_address.getText().toString().isEmpty() && !et_address_2.getText().toString().isEmpty() && !et_landmark.getText().toString().isEmpty() && !et_pin.getText().toString().isEmpty() && !et_city.getText().toString().isEmpty() && !et_state.getText().toString().isEmpty()){
                    addAddress();
                    showProgressDialog();
                }else {
                    Toast.makeText(Address_Activity.this, "Please fill it out", Toast.LENGTH_SHORT).show();
                }
            }
        });

        getAddress();

    }


    private void getAddress(){

        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        String c_id = customer_session.getCustomerID();

        Map<String, String> postParam= new HashMap<>();
        postParam.put(KEY_ID,c_id);


        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                ADDRESS_URL, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            addresses.clear();

                            String status = response.getString("status");
                            if(status.equals("200")) {
                                JSONArray jsonArray = response.getJSONArray("address_data");

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject properties = jsonArray.getJSONObject(i);

                                    String position_id = properties.getString("address_id");
                                    String name = properties.getString("cus_name");
                                    String phone = properties.getString("mobile_number");
                                    String add_l1 = properties.getString("add_l1");
                                    String add_l2 = properties.getString("add_l2");
                                    String landmark = properties.getString("landmark");
                                    String pin_code = properties.getString("pin");
                                    String city = properties.getString("city");
                                    String state = properties.getString("state");

                                    Address address = new Address(name, phone, add_l1, add_l2, landmark, pin_code, city, state, position_id);
                                    addresses.add(address);
                                }

                                add = jsonArray.length();
                                invalidateOptionsMenu();

                                mAdapter = new Select_Adapter(addresses, new Select_Adapter.ClickListener() {
                                    @Override
                                    public void onClick(View view, int position) {

                                    }

                                    @Override
                                    public void onLongClick(View view, int position) {

                                    }

                                    @Override
                                    public void onSelect(View view, int position) {
                                        showProgressDialog();
                                        add_pos =position;
                                        placeOrder();
                                    }

                                });
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(mAdapter);

                                scrollView.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);

                            }else {
                                scrollView.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                                progressBar.setVisibility(View.GONE);
                                invalidateOptionsMenu();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(Address_Activity.this,"Connection error",Toast.LENGTH_LONG).show();
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


    void addAddress(){

        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        Map<String, String> postParam= new HashMap<>();
        postParam.put(KEY_ID,customer_session.getCustomerID());
        postParam.put(KEY_NAME,name.getText().toString().trim());
        postParam.put(KEY_PHONE,et_phone.getText().toString().trim());
        postParam.put(KEY_ADDRESS_1,et_address.getText().toString().trim());
        postParam.put(KEY_ADDRESS_2,et_address_2.getText().toString().trim());
        postParam.put(KEY_LANDMARK,et_landmark.getText().toString().trim());
        postParam.put(KEY_PIN_CODE,et_pin.getText().toString().trim());
        postParam.put(KEY_CITY,et_city.getText().toString().trim());
        postParam.put(KEY_STATE,et_state.getText().toString().trim());


        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                ADD_NEW_URL, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String status = response.getString("status");
                            hideProgressDialog();

                            if(status.equals("200")){
                                Toast.makeText(Address_Activity.this, "Data Added", Toast.LENGTH_SHORT).show();
                                scrollView.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.GONE);
                                progressBar.setVisibility(View.VISIBLE);
                                getAddress();
                            }else {
                                Toast.makeText(Address_Activity.this, "Connection Error", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                Toast.makeText(Address_Activity.this,"Connection error",Toast.LENGTH_LONG).show();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_checkout, menu);
        MenuItem item = menu.findItem(R.id.add_new);
        if (add==0){
            item.setVisible(false);
        }else if (add==1){
            item.setVisible(true);
            item.setVisible(true);
            item.setIcon(getResources().getDrawable(R.drawable.plus_w));
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.trans_right_out,R.anim.trans_right);
                return true;
            case R.id.add_new:
                scrollView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                add=1;
                item.setVisible(false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void placeOrder(){

        String tag_json_obj = "json_obj_req";

        Map<String, String> postParam= new HashMap<>();
        postParam.put("cus_name",c_name);
        postParam.put("cus_mobile",c_phone);
        postParam.put(KEY_C_ID,customer_session.getCustomerID());
        postParam.put(KEY_PRODUCT_ID,"0");
        postParam.put(ADD_ID,addresses.get(add_pos).getmPos());
        postParam.put(KEY_PRODUCT_PRICE,base_amount);
        postParam.put(KEY_MARGIN,margin);
        postParam.put(KEY_TOTAL, tot_amount);
        postParam.put(KEY_NOTES,notes);
        postParam.put("items_data", String.valueOf(itemListToJsonConvert(products)));
        postParam.put("wallet_amount",wallet_used);
        postParam.put("received_amount",tot_amount);
        postParam.put("shipment_price",ship_charge);
        postParam.put("cod_price",cod_charge);
        postParam.put("seller_name",seller_name);
        postParam.put("cart_weight",product_weight);
        postParam.put("payment_mode",pay_mode);


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                ORDER_URL, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String status = response.getString("status");
                            if (status.equals("200")){
                                order_id = response.getString("order_id");
                                if (pay_mode.equals("op")){
                                    hideProgressDialog();
                                    startPayment(response.getString("order_id"));
                                    id =1;
                                }else {
                                    for (int i=0;i<products.size();i++){
                                        delete_cart_url(products.get(i).getBook_id(),i,products.get(i).getBookPrice(),products.get(i).getQuantity(),i);
                                    }
                                }
                            }else {
                                Toast.makeText(Address_Activity.this, "Connection Error , try Again", Toast.LENGTH_SHORT).show();
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


    public void startPayment(String oid) {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();

        co.setImage(R.mipmap.ic_launcher);

        try {
            JSONObject options = new JSONObject();
            options.put("name", c_name);
            options.put("description", "Order ID - "+oid);
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", amount);
            JSONObject preFill = new JSONObject();
            preFill.put("email", "apps.itrifid@gmail.com");
            preFill.put("contact", c_phone);
            options.put("prefill", preFill);
            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }

    }


    @Override
    public void onPaymentSuccess(String s) {
        try {
            for (int i=0;i<products.size();i++){
               delete_cart_url(products.get(i).getBook_id(),i,products.get(i).getBookPrice(),products.get(i).getQuantity(),i);
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    @Override
    public void onPaymentError(int i, String s) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }



    void sendPaymentResponse(String o_id, final String response_){

        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        Map<String, String> postParam= new HashMap<>();
        postParam.put(KEY_RES_ID,customer_session.getCustomerID());
        postParam.put(KEY_ORDER_ID,o_id);
        postParam.put(KEY_PAYMENT_STATUS,response_);


        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                RESPONSE_URL, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String status = response.getString("status");
                            hideProgressDialog();

                            if(status.equals("200")){
                                if (response_.equals("Successful")){
                                    Activity_Cart.activityA.finish();
                                    Intent intent = new Intent(getApplicationContext(),Thanks_Activity.class);
                                    intent.putExtra("order_status",1);
                                    intent.putExtra("order_id",order_id);
                                    startActivity(intent);
                                    Payment_Activity_1.activity.finish();
                                    finish();
                                }else {
                                    Activity_Cart.activityA.finish();
                                    Intent intent = new Intent(getApplicationContext(),Thanks_Activity.class);
                                    intent.putExtra("order_status",2);
                                    intent.putExtra("order_id",order_id);
                                    startActivity(intent);
                                    Payment_Activity_1.activity.finish();
                                    finish();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                Toast.makeText(Address_Activity.this,error.toString(),Toast.LENGTH_LONG).show();
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


    public JSONArray itemListToJsonConvert(List<Cart> list) {

        JSONArray jArray = new JSONArray();// /ItemDetail jsonArray

        for (int i = 0; i < list.size(); i++) {
            JSONObject jGroup = new JSONObject();// /sub Object

            try {
                jGroup.put("id", list.get(i).getBook_id());
                jGroup.put("name", list.get(i).getBookName());
                jGroup.put("price",list.get(i).getBookPrice());
                jGroup.put("qty", list.get(i).getQuantity());
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("product_image",list.get(i).getImgUrl());
                jsonObject.put("product_unit_price","0");
                jGroup.put("options",jsonObject);
                jGroup.put("rowid",String.valueOf(i));
                jGroup.put("subtotal",list.get(i).getBookPrice());
                jGroup.put("size",list.get(i).getAvail_size());
                jArray.put(jGroup);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return jArray;
    }


    private void delete_cart_url(final String book_id, final int pos, final String bookPrice, final String quantity, final int i) {

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

                            if(status.equals("202")){
                                if(pay_mode.equals("op")){
                                    if (i==products.size()-1){
                                        showProgressDialog();
                                        sendPaymentResponse(order_id,"Successful");
                                    }
                                }else {
                                    Activity_Cart.activityA.finish();
                                    Intent intent = new Intent(getApplicationContext(),Thanks_Activity.class);
                                    intent.putExtra("order_status",1);
                                    intent.putExtra("order_id",order_id);
                                    startActivity(intent);
                                    Payment_Activity_1.activity.finish();
                                    finish();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                Toast.makeText(Address_Activity.this, "Connection Error", Toast.LENGTH_SHORT).show();
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

}
