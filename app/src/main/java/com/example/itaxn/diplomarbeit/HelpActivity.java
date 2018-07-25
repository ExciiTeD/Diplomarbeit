package com.example.itaxn.diplomarbeit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        WebView webView = findViewById(R.id.webView);
        webView.loadUrl("file:///android_asset/helpText.html");
        webView.requestFocus();
        Button btn = findViewById(R.id.button7);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HelpActivity.this, HomeActivity.class);
                //startActivity(i , ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                startActivity(i);
            }

        });

    }
}
