package com.fashio.apps.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fashio.apps.Applications.AppController;
import com.fashio.apps.Model.Cart;
import com.fashio.apps.R;
import com.fashio.apps.Session.Customer_Session;
import com.fashio.apps.Session.Name_Session;
import com.fashio.apps.Session.Phone_Session;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Payment_Activity_1 extends Activity {

    private static final String WALLET_URL = "https://apps.itrifid.com/aashopoholics/rest_server/selectWallet/API-KEY/123456";
    private static final String KEY_RES_ID = "res_id";
    private Customer_Session customer_session;
    static Payment_Activity_1 activity;
    private CheckBox cod;
    private CheckBox op;
    private CheckBox use;
    private TextView base_a;
    private TextView total_text;
    private TextView cod_amount;
    private TextView shiiping_text;
    private TextView apply;
    private String product_id;
    private FrameLayout btn_okk;
    private Phone_Session phone_session;
    private Name_Session name_session;
    private int id=0;
    private Button cnt;
    private String name;
    private String phone;
    private String margin;
    private String notes;
    private String seller_name;
    private String product_total;
    private String product_weight;
    private String ship_cost;
    private String cod_cost;
    private String wallet_amount;
    private TextView sub_total;
    private ProgressBar p_bar;
    private RelativeLayout payment_layout;
    private Double wallet_used=0.0;
    private Double inv_total;
    private Double margin_total;
    private Double rec_amount;
    private RelativeLayout cod_charge_layout;
    private Double price_s_text=0.0;
    private List<Cart> products;
    private String is_cod="0";
    private CardView cardView;
    private TextView cart_weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_mode);

        base_a= (TextView)  findViewById(R.id.total_price);
        cart_weight = findViewById(R.id.cart_weight);
        shiiping_text = (TextView) findViewById(R.id.shipping_charge);
        cod_amount = (TextView) findViewById(R.id.cod_charge);
        total_text = (TextView) findViewById(R.id.total_amount);
        cod_charge_layout = findViewById(R.id.cod_charge_layout);
        sub_total = findViewById(R.id.sub_total);
        p_bar = findViewById(R.id.p_bar);
        payment_layout = findViewById(R.id.payment_layout);
        cardView = findViewById(R.id.card_3);

        final Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras!=null){
            name = extras.getString("name");
            phone = extras.getString("phone");
            margin = extras.getString("margin");
            notes = extras.getString("notes");
            seller_name = extras.getString("seller_name");
            product_total = extras.getString("product_total");
            product_weight = extras.getString("product_weight");
            ship_cost = extras.getString("ship_cost");
            cod_cost = extras.getString("cod_cost");
            is_cod = extras.getString("is_cod");

            Bundle data = extras.getBundle("ca");
            products= (List<Cart>) data.getSerializable("images");

            if (is_cod.equals("0")){
                cardView.setVisibility(View.GONE);
            }else {
                cardView.setVisibility(View.VISIBLE);
            }

            base_a.setText("Rs"+product_total);
            margin_total = Double.valueOf(product_total) + Double.valueOf(margin) +Double.valueOf(ship_cost);

            shiiping_text.setText("Rs"+ship_cost);
            cod_amount.setText("Rs"+cod_cost);
            total_text.setText("Rs"+margin_total);

            cart_weight.setTag(product_weight+"kg");
        }

        activity = this;


        customer_session = new Customer_Session(getApplicationContext());
        name_session = new Name_Session(getApplicationContext());
        phone_session = new Phone_Session(getApplicationContext());

        calcCOD();


        apply = (TextView) findViewById(R.id.apply);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                dialog.show();
            }
        });

        op =(CheckBox) findViewById(R.id.op);
        op.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (op.isChecked()) {
                    if (inv_total ==0){
                        cod.setChecked(false);
                        use.setChecked(false);
                    }else if (cod.isChecked() && use.isChecked()){
                        cod.setChecked(false);
                        inv_total = inv_total + wallet_used;

                        if (Double.valueOf(wallet_amount)>inv_total){
                            wallet_used = inv_total;
                        }else {
                            wallet_used = Double.valueOf(wallet_amount);
                        }

                        inv_total = inv_total - wallet_used;
                        total_text.setText("₹"+inv_total);

                        if (inv_total==0){
                            use.setChecked(false);
                        }

                    }else if (use.isChecked()){
                        //do nothing
                    }else {
                        cod.setChecked(false);
                        calc();
                    }
                }
            }
        });

//        bt = (CheckBox) findViewById(R.id.bt);
//        bt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (bt.isChecked()){
//                    cod.setChecked(false);
//                    op.setChecked(false);
//                }
//            }
//        });

        cod = (CheckBox) findViewById(R.id.cod);
        cod.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (cod.isChecked()){
                    op.setChecked(false);

                    if (inv_total==0){
                        use.setChecked(false);
                        calcCOD();
                    }else {
                        inv_total = inv_total + Double.parseDouble(cod_cost);
                        total_text.setText("₹"+inv_total);
                    }

                }else {
                    cod_charge_layout.setVisibility(View.GONE);


                    if (inv_total == 0 && use.isChecked()) {
                        wallet_used -= Double.valueOf(cod_cost);
                    } else if (use.isChecked()) {
                        inv_total = inv_total-Double.valueOf(cod_cost);

                        wallet_used = Double.valueOf(wallet_amount);
                        total_text.setText("₹"+inv_total);
                    }
                }
            }
        });

        use = (CheckBox) findViewById(R.id.wallet);
        use.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (use.isChecked()){
                    double a = Double.valueOf(wallet_amount);

                    if (a <= 0) {
                        use.setChecked(false);
                        Toast.makeText(Payment_Activity_1.this, "No Amount to Redeem", Toast.LENGTH_SHORT).show();
                    } else {
                        if (a>inv_total){
                            wallet_used = inv_total;
                        }else {
                            wallet_used = Double.valueOf(a);
                        }

                        inv_total= inv_total - wallet_used;
                        total_text.setText("₹"+inv_total);

                        if (price_s_text==0){
                            if (cod.isChecked()){
                                cod.setChecked(false);
                            }
                            op.setChecked(false);
                        }

                    }
                }else {
                    int p= Integer.parseInt(wallet_amount);
                    if (p<=0){
                        use.setChecked(false);
                        Toast.makeText(Payment_Activity_1.this, "No Amount to Redeem", Toast.LENGTH_SHORT).show();
                    }else {
                        inv_total = inv_total + wallet_used;
                        total_text.setText("₹"+inv_total);
                    }

                    wallet_used=0.0;
                }
            }
        });

        cnt = (Button) findViewById(R.id.cnt);
        cnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(use.isChecked() && inv_total==0){
                    Intent intent1 = new Intent(getApplicationContext(),Address_Activity.class);
                    Bundle data = new Bundle();
                    data.putSerializable("images", (Serializable) products );
                    intent1.putExtra("ca",data);
                    intent1.putExtra("product_total",product_total);
                    intent1.putExtra("margin",margin);
                    intent1.putExtra("invoice_total",String.valueOf(wallet_used));
                    intent1.putExtra("product_weight",product_weight);
                    intent1.putExtra("ship_cost",ship_cost);
                    intent1.putExtra("cod_cost",cod_cost);
                    intent1.putExtra("wallet_used",wallet_used);
                    intent1.putExtra("name",name);
                    intent1.putExtra("seller_name",seller_name);
                    intent1.putExtra("notes",notes);
                    intent1.putExtra("phone",phone);
                    intent1.putExtra("pay_mode","wallet");
                    startActivity(intent1);
                }else {
                    if (cod.isChecked()){
                        Intent intent1 = new Intent(getApplicationContext(),Address_Activity.class);
                        Bundle data = new Bundle();
                        data.putSerializable("images", (Serializable) products );
                        intent1.putExtra("ca",data);
                        intent1.putExtra("product_total",product_total);
                        intent1.putExtra("margin",margin);
                        intent1.putExtra("invoice_total",inv_total);
                        intent1.putExtra("product_weight",product_weight);
                        intent1.putExtra("ship_cost",ship_cost);
                        intent1.putExtra("cod_cost",cod_cost);
                        intent1.putExtra("wallet_used",wallet_used);
                        intent1.putExtra("name",name);
                        intent1.putExtra("seller_name",seller_name);
                        intent1.putExtra("notes",notes);
                        intent1.putExtra("phone",phone);
                        intent1.putExtra("pay_mode","cod");
                        startActivity(intent1);
                    }else if (op.isChecked()) {
                        inv_total-=Double.valueOf(margin);
                        Intent intent1 = new Intent(getApplicationContext(),Address_Activity.class);
                        Bundle data = new Bundle();
                        data.putSerializable("images", (Serializable) products );
                        intent1.putExtra("ca",data);
                        intent1.putExtra("product_total",product_total);
                        intent1.putExtra("margin",margin);
                        intent1.putExtra("invoice_total",inv_total);
                        intent1.putExtra("product_weight",product_weight);
                        intent1.putExtra("ship_cost",ship_cost);
                        intent1.putExtra("cod_cost",cod_cost);
                        intent1.putExtra("wallet_used",wallet_used);
                        intent1.putExtra("name",name);
                        intent1.putExtra("seller_name",seller_name);
                        intent1.putExtra("notes",notes);
                        intent1.putExtra("phone",phone);
                        intent1.putExtra("pay_mode","op");
                        startActivity(intent1);
                    }else {
                        Toast.makeText(Payment_Activity_1.this, "₹"+ inv_total +" has to be made , Please select your mode of payment in order to proceed..", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

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

                            payment_layout.setVisibility(View.VISIBLE);
                            p_bar.setVisibility(View.GONE);
                            if (status.equals("200")){
                                wallet_amount = response.getString("total_wallet_amount");
                                sub_total.setText("Rs"+wallet_amount);
                            }else {
                                wallet_amount="0";
                                sub_total.setText("Rs"+wallet_amount);
                            }

                        } catch (JSONException e) {
                            Toast.makeText(Payment_Activity_1.this, "Connetion Error", Toast.LENGTH_SHORT).show();
                            sub_total.setText("Rs"+wallet_amount);
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                sub_total.setText("Rs"+wallet_amount);
                Toast.makeText(Payment_Activity_1.this, "Connetion Error", Toast.LENGTH_SHORT).show();
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    double calc(){
        cod_charge_layout.setVisibility(View.GONE);
        inv_total = margin_total;
        total_text.setText("Rs"+inv_total);
        return inv_total;
    }

    double calcCOD(){
        cod_charge_layout.setVisibility(View.VISIBLE);
        inv_total = margin_total+ Double.valueOf(cod_cost);
        total_text.setText("Rs"+inv_total);
        return inv_total;
    }


}
