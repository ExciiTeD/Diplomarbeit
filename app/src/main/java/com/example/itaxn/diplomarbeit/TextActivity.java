package com.example.itaxn.diplomarbeit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        Intent intent = this.getIntent();
        TextView tv = (TextView) findViewById(R.id.message);
        tv.setText(intent.getStringExtra("message"));
        tv.setMovementMethod(new ScrollingMovementMethod());
        TextView tv_heading = findViewById(R.id.textView);

        tv_heading.setText(intent.getStringExtra("fileName"));

        Button btn = findViewById(R.id.button8);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TextActivity.this, WaveReadActivity.class);
                //startActivity(i , ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                startActivity(i);
            }

        });
    }
}
