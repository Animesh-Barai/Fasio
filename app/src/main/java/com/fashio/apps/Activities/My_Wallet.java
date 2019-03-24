package com.fashio.apps.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fashio.apps.Adapters.Wallet_Adapter;
import com.fashio.apps.Applications.AppController;
import com.fashio.apps.Model.Wallet;
import com.fashio.apps.R;
import com.fashio.apps.Session.Customer_Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class My_Wallet extends AppCompatActivity{

    private static final String WALLET_URL = "https://apps.itrifid.com/fashio/rest_server/selectWallet/API-KEY/123456";
    private static final String KEY_RES_ID = "res_id";
    private Customer_Session customer_session;
    private TextView mwb;
    private ProgressBar p_bar;
    private Toolbar toolbar;
    private LinearLayout lin;
    private List<Wallet> transactions=new ArrayList<>();
    private RecyclerView recyclerView;
    private Wallet_Adapter mAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        customer_session=new Customer_Session(getApplicationContext());
        recyclerView= (RecyclerView) findViewById(R.id.recycler_v);

        recyclerView.setNestedScrollingEnabled(false);

        mwb = (TextView) findViewById(R.id.mwb);
        lin = (LinearLayout) findViewById(R.id.wall_lin);
        p_bar = (ProgressBar) findViewById(R.id.p_bar);

        getWallet();
    }


    void getWallet(){

        String tag_json_obj = "json_obj_req";

        Map<String, String> postParam= new HashMap<>();
        postParam.put(KEY_RES_ID,customer_session.getCustomerID());


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                WALLET_URL, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            String status = response.getString("status");

                            if (status.equals("200")){

                                JSONArray jsonArray = response.getJSONArray("wallet_data");

                                if (jsonArray.length()>1){
                                    recyclerView.setVisibility(View.VISIBLE);
                                }

                                for (int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    String trans_id = jsonObject1.getString("transaction_id");
                                    String trans_type = jsonObject1.getString("transaction_details");
                                    String trans_amount= jsonObject1.getString("transaction_amount");
                                    String trans_status = jsonObject1.getString("transaction_type");

                                    Wallet wallet = new Wallet(trans_id,trans_type,trans_amount,trans_status);
                                    transactions.add(wallet);
                                }

                                mAdapter = new Wallet_Adapter(getApplicationContext(),transactions);

                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(mAdapter);

                                p_bar.setVisibility(View.GONE);
                                mwb.setText("₹"+response.getString("total_wallet_amount"));
                                lin.setVisibility(View.VISIBLE);

                            }else if (status.equals("400")){
                                p_bar.setVisibility(View.GONE);
                                mwb.setText("₹0");
                                lin.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }else {
                                Toast.makeText(My_Wallet.this, "Connection Error", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(My_Wallet.this, e.toString(), Toast.LENGTH_SHORT).show();
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


}
