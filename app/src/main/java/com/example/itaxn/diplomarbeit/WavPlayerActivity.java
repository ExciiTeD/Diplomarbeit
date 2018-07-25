package com.example.itaxn.diplomarbeit;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

public class WavPlayerActivity extends AppCompatActivity {
    private MediaPlayer mp;
    public static final int FILE_PICKER_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wav_player);
        Button button = (Button) findViewById(R.id.button6);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFilePicker();
            }
        });

        Button btn1 = findViewById(R.id.button13);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(WavPlayerActivity.this, HomeActivity.class);
                //startActivity(i , ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                startActivity(i);
            }

        });
        //this.mp = MediaPlayer.create(this, new Ur)
    }

    private void openFilePicker() {
        new MaterialFilePicker()
                .withActivity(this)
                .withRequestCode(FILE_PICKER_REQUEST_CODE)
                .withHiddenFiles(false)
                .withTitle("Search WAV-File")
                .start();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            String path = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            Log.i("tag", "onActivityResult: Dies ist der pfad: " + path);
            this.mp = MediaPlayer.create(this, Uri.parse(path));
            this.mp.start();
        }
    }
}
