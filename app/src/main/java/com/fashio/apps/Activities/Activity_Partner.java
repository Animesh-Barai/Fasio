package com.fashio.apps.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fashio.apps.Applications.AppController;
import com.fashio.apps.R;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;


public class Activity_Partner extends AppCompatActivity {

    private Button btn;
    private EditText seller_name;
    private EditText seller_phone;
    private EditText business_name;
    private EditText seller_gst;
    private EditText seller_location;
    private EditText email;
    private static final String INSERT_DETAILS_URL = "https://apps.itrifid.com/fashio/rest_server/partnetRequest/API-KEY/123456";
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner);

        seller_name = findViewById(R.id.input_name);
        seller_phone = findViewById(R.id.input_phone);
        business_name = findViewById(R.id.input_business_name);
        seller_gst = findViewById(R.id.input_gst);
        seller_location = findViewById(R.id.input_location);
        email = findViewById(R.id.email_id);

        btn =(Button ) findViewById(R.id.submit_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!seller_name.getText().toString().trim().equals("") && !seller_phone.getText().toString().trim().equals("") && !business_name.getText().toString().trim().equals("") && !seller_gst.getText().toString().trim().equals("") && !seller_location.getText().toString().trim().equals("") && !email.getText().toString().trim().equals("")){
                    showProgressDialog();
                    addAddress();
                }else {
                    Toast.makeText(Activity_Partner.this, "Please Fill it out..", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

//    void getDetails(){
//
//        // Tag used to cancel the request
//        String tag_json_obj = "json_obj_req";
//
//        Map<String, String> postParam= new HashMap<>();
//        postParam.put("reseller_id",customer_session.getCustomerID());
//
//
//        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
//                GET_DETAILS_URL, new JSONObject(postParam),
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//
//                        hideProgressDialog();
//                        try {
//                            String status = response.getString("status");
//
//                            if(status.equals("200")){
//
//                            }else {
//                                //do nothing
//                                btn.setVisibility(View.VISIBLE);
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(Activity_Partner.this,"Connection error",Toast.LENGTH_LONG).show();
//            }
//        }) {
//
//            /**
//             * Passing some request headers
//             * */
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<>();
//                headers.put("Content-Type", "application/json; charset=utf-8");
//                return headers;
//            }
//
//
//
//        };
//
//        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
//    }

    void addAddress(){

        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        Map<String, String> postParam= new HashMap<>();
        postParam.put("name",seller_name.getText().toString().trim());
        postParam.put("business_name",business_name.getText().toString().trim());
        postParam.put("gst_no",seller_gst.getText().toString().trim());
        postParam.put("location", seller_location.getText().toString().trim());
        postParam.put("mobile",seller_phone.getText().toString().trim());
        postParam.put("email",email.getText().toString().trim());


        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                INSERT_DETAILS_URL, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        hideProgressDialog();
                        try {
                            String status = response.getString("status");

                            if(status.equals("202")){
                                Toast.makeText(Activity_Partner.this, "Your Request has been accepted , we will back to you very soon", Toast.LENGTH_LONG).show();
                            }else {
                                hideProgressDialog();
                                Toast.makeText(Activity_Partner.this, "Connection Error", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Activity_Partner.this,"Connection error",Toast.LENGTH_LONG).show();
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
