package com.fashio.apps.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.fashio.apps.R;


public class Help_Activity extends AppCompatActivity {



    private WebView webView;
    private ProgressBar p_bar;
    private int id =0;
    private TextView theader;
    private String url;
    private String title;


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_);

        Typeface tf2 = Typeface.createFromAsset(getAssets(), "fonts/arial.ttf");
        theader = (TextView) findViewById(R.id.header);
        theader.setTypeface(tf2);


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras!=null){
            url = extras.getString("url");
            title = extras.getString("title");

            theader.setText(title);
        }


        p_bar = (ProgressBar) findViewById(R.id.p_bar);

        webView = (WebView) findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                webView.setVisibility(View.VISIBLE);
                p_bar.setVisibility(View.GONE);
            }
        });

    }
}
