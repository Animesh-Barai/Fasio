package com.fashio.apps.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fashio.apps.Adapters.Ranking_Adapter;
import com.fashio.apps.Applications.AppController;
import com.fashio.apps.Model.Ranking;
import com.fashio.apps.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Seller_Ranckings extends AppCompatActivity{

    private static final String KEY_OFFSET = "offset";
    private static final String RANKING_URL = "https://apps.itrifid.com/fashio/rest_server/resellerRanking/API-KEY/123456";
    private RecyclerView recyclerView;
    private ProgressBar p_bar;
    private List<Ranking> rankings=new ArrayList<>();
    private int offset=0;
    private int ranking=1;
    private Ranking_Adapter mAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_rankings);

        recyclerView = findViewById(R.id.r_view);
        recyclerView.setNestedScrollingEnabled(false);
        p_bar = findViewById(R.id.p_bar);

        getRankings();
    }

    void getRankings(){

        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        Map<String, String> postParam= new HashMap<>();
        postParam.put(KEY_OFFSET,String.valueOf(offset));

        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                RANKING_URL, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String status = response.getString("status");

                            if (status.equals("200")){
                                offset = response.getInt("new_offset");
                                JSONArray jsonArray =response.getJSONArray("response");
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String seller_name = jsonObject.getString("reseller_name");
                                    String seller_sold = jsonObject.getString("total_invoice_sell");
                                    String seller_earned = jsonObject.getString("total_margin");
                                    rankings.add(new Ranking(String.valueOf(ranking),seller_name,seller_sold,seller_earned));
                                    ranking++;
                                }

                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());

                                 mAdapter = new Ranking_Adapter(recyclerView,getApplicationContext(),rankings);
                                recyclerView.setAdapter(mAdapter);

                                mAdapter.setOnLoadMoreListener(new Ranking_Adapter.OnLoadMoreListener() {
                                    @Override
                                    public void onLoadMore() {
                                        rankings.add(null);
                                        mAdapter.notifyItemRemoved(rankings.size());
                                        loadRankings();
                                    }
                                });

                                p_bar.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);


                            }else {
                                Toast.makeText(Seller_Ranckings.this, "Connection Error", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
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

    private void loadRankings() {

// Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        Map<String, String> postParam= new HashMap<>();
        postParam.put(KEY_OFFSET,String.valueOf(offset));

        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                RANKING_URL, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            rankings.remove(rankings.size() - 1);
                            mAdapter.notifyItemRemoved(rankings.size());

                            String status = response.getString("status");

                            if (status.equals("200")){
                                offset = response.getInt("new_offset");
                                JSONArray jsonArray =response.getJSONArray("response");
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String seller_name = jsonObject.getString("reseller_name");
                                    String seller_sold = jsonObject.getString("total_invoice_sell");
                                    String seller_earned = jsonObject.getString("total_margin");
                                    rankings.add(new Ranking(String.valueOf(ranking),seller_name,seller_sold,seller_earned));
                                    ranking++;
                                }

                                mAdapter.setLoaded();
                                mAdapter.notifyDataSetChanged();

                            }else {
                                Toast.makeText(Seller_Ranckings.this, "Connection Error", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
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
