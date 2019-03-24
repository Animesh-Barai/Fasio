package com.fashio.apps.Activities;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fashio.apps.Adapters.Order_Adapter_1;
import com.fashio.apps.Applications.AppController;
import com.fashio.apps.Model.Main_Order;
import com.fashio.apps.Model.Order;
import com.fashio.apps.R;
import com.fashio.apps.Session.Customer_Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class My_Orders extends AppCompatActivity {


    private RecyclerView recyclerView;
    private static final String CANCE_URL = "https://apps.itrifid.com/fashio/rest_server/requestCancellationReturn/API-KEY/123456";
    private static final String KEY_RES = "res_id";
    private static final String KEY_OFFSET= "offset";
    private static final String ORDER_URL = "https://apps.itrifid.com/fashio/rest_server/resUserOrder/API-KEY/123456";
    private ProgressBar p_bar;
    private List<Order> orders= new ArrayList<>();
    private Order_Adapter_1 mAdapter;
    private int offset=0;
    private Customer_Session customer_session;
    private ProgressDialog mProgressDialog;
    private LinearLayout lin;
    private Dialog reason_dialog;
    private EditText reason_edit;
    private Button cancel_btn;
    private int cancel_pos=0;
    private List<Main_Order> main_orders=new ArrayList<>();
    private int cancel_main_pos =0;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        customer_session=new Customer_Session(getApplicationContext());
        recyclerView = (RecyclerView) findViewById(R.id.r_view);
        p_bar = (ProgressBar) findViewById(R.id.p_bar);

        recyclerView.setNestedScrollingEnabled(false);

        lin = (LinearLayout) findViewById(R.id.empty_layout);

        reason_dialog  = new Dialog(My_Orders.this,R.style.BottomDialog);
        reason_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        reason_dialog.getWindow().getAttributes().windowAnimations = R.style.CustomDialogAnimation;
        layoutParams.copyFrom(reason_dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.BOTTOM;
        reason_dialog.getWindow().setAttributes(layoutParams);
        reason_dialog.setCancelable(true);
        reason_dialog.setContentView(R.layout.reason_dialog);

        reason_edit = reason_dialog.findViewById(R.id.input_cancel_reason);
        cancel_btn = reason_dialog.findViewById(R.id.review_btn);

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (reason_edit.getText().toString().trim().equals("")){
                    Toast.makeText(My_Orders.this, "Please give the reason", Toast.LENGTH_SHORT).show();
                }else {
                    showProgressDialog();
                    cancelProduct(main_orders.get(cancel_main_pos).getOrders().get(cancel_pos).getProduct_id(),main_orders.get(cancel_main_pos).getOrders().get(cancel_pos).getOrder_id());
                }
            }
        });

        getUserOrders();
    }


    private void cancelProduct(String product_id,String order_id) {

        String tag_json_obj = "json_obj_req";

        Map<String, String> postParam= new HashMap<>();
        postParam.put("order_id",order_id);
        postParam.put("product_id", product_id);
        postParam.put("reason",reason_edit.getText().toString().trim());
        postParam.put(KEY_RES,customer_session.getCustomerID());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                CANCE_URL, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        hideProgressDialog();
                        try {
                            String status =response.getString("status");
                            if (status.equals("200")){
                                Toast.makeText(My_Orders.this, "Cancellation request accepted", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(My_Orders.this, "Already Cancelled", Toast.LENGTH_SHORT).show();
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

    void getUserOrders(){

        String tag_json_obj = "json_obj_req";

        Map<String, String> postParam= new HashMap<>();
        postParam.put(KEY_RES,customer_session.getCustomerID());
        postParam.put(KEY_OFFSET, String.valueOf(offset));


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                ORDER_URL, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            String status = response.getString("status");
                            if (status.equals("200")){
                                JSONArray jsonArray = response.getJSONArray("order_data");
                                for (int i=0;i<jsonArray.length();i++){

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String is_old = jsonObject.getString("is_old");

                                    String order_id = jsonObject.getString("order_id");
                                    String tracking_id = jsonObject.getString("order_status");
                                    String reseller_margin= jsonObject.getString("reseller_margin");
                                    List<Order> orders = new ArrayList<>();

                                    offset = jsonObject.getInt("new_offset");

                                    if (is_old.equals("0")){

                                        JSONArray jsonArray1 = jsonObject.getJSONArray("item_list");
                                        for (int j=0;j<jsonArray1.length();j++){
                                            JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                                            String sub_total= jsonObject1.getString("subtotal");
                                            JSONObject jsonObject2 = jsonObject1.getJSONObject("options");
                                            String img_url = jsonObject2.getString("product_image");
                                            String id = jsonObject1.getString("id");
                                            Order order = new Order(order_id,sub_total,reseller_margin,id,img_url);
                                            orders.add(order);
                                        }

                                        main_orders.add(new Main_Order(order_id,reseller_margin,orders,tracking_id));

                                    }else {

                                        String product_id = jsonObject.getString("product_id");
                                        String img_url = jsonObject.getString("img_url");
                                        String sub_total = jsonObject.getString("item_total");

                                        Order order = new Order(order_id,sub_total,reseller_margin,product_id,img_url);
                                        orders.add(order);

                                        main_orders.add(new Main_Order(order_id,reseller_margin,orders,tracking_id));
                                    }

                                }

                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());

                                mAdapter = new Order_Adapter_1(recyclerView, getApplicationContext(), main_orders, new Order_Adapter_1.ClickListener() {
                                    @Override
                                    public void onClick(View view, int position) {

                                    }

                                    @Override
                                    public void onLongClick(View view, int position) {

                                    }

                                    @Override
                                    public void onCancelClick(View view, int main_pos, int local_pos) {
                                        cancel_pos=local_pos;
                                        cancel_main_pos =main_pos;
                                        reason_dialog.show();
                                    }

                                });

                                recyclerView.setAdapter(mAdapter);

                                mAdapter.setOnLoadMoreListener(new Order_Adapter_1.OnLoadMoreListener() {
                                    @Override
                                    public void onLoadMore() {
                                        main_orders.add(null);
                                        mAdapter.notifyItemRemoved(main_orders.size());
                                        loadUserOrders();
                                    }
                                });

                                p_bar.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                lin.setVisibility(View.GONE);

                            }else {

                                p_bar.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.GONE);
                                lin.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            Toast.makeText(My_Orders.this, e.toString(), Toast.LENGTH_SHORT).show();
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


    void loadUserOrders(){

        String tag_json_obj = "json_obj_req";

        Map<String, String> postParam= new HashMap<>();
        postParam.put(KEY_RES,customer_session.getCustomerID());
        postParam.put(KEY_OFFSET, String.valueOf(offset));

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                ORDER_URL, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            main_orders.remove(main_orders.size() - 1);
                            mAdapter.notifyItemRemoved(main_orders.size());

                            String status = response.getString("status");
                            if (status.equals("200")){
                                JSONArray jsonArray = response.getJSONArray("order_data");
                                for (int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String is_old = jsonObject.getString("is_old");

                                    String order_id = jsonObject.getString("order_id");
                                    String tracking_id = jsonObject.getString("order_status");
                                    String reseller_margin= jsonObject.getString("reseller_margin");
                                    List<Order> orders = new ArrayList<>();

                                    offset = jsonObject.getInt("new_offset");

                                    if (is_old.equals("0")){

                                        JSONArray jsonArray1 = jsonObject.getJSONArray("item_list");
                                        for (int j=0;j<jsonArray1.length();j++){
                                            JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                                            String sub_total= jsonObject1.getString("subtotal");
                                            JSONObject jsonObject2 = jsonObject1.getJSONObject("options");
                                            String img_url = jsonObject2.getString("product_image");
                                            String id = jsonObject1.getString("id");
                                            Order order = new Order(order_id,sub_total,reseller_margin,id,img_url);
                                            orders.add(order);
                                        }

                                        main_orders.add(new Main_Order(order_id,reseller_margin,orders,tracking_id));

                                    }else {

                                        String product_id = jsonObject.getString("product_id");
                                        String img_url = jsonObject.getString("img_url");
                                        String sub_total = jsonObject.getString("item_total");

                                        Order order = new Order(order_id,sub_total,reseller_margin,product_id,img_url);
                                        orders.add(order);

                                        main_orders.add(new Main_Order(order_id,reseller_margin,orders,tracking_id));
                                    }

                                }

                                mAdapter.setLoaded();
                                mAdapter.notifyDataSetChanged();

                            }else {
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

}
