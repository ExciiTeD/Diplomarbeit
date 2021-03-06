package com.example.itaxn.diplomarbeit;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.itaxn.diplomarbeit.audio.Converter;
import com.example.itaxn.diplomarbeit.audio.Wav;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.IOException;

public class WavPlayerActivity extends AppCompatActivity {
    private MediaPlayer mp;
    public static final int FILE_PICKER_REQUEST_CODE = 1;
    private boolean wavReady;
    private boolean isStopped;
    private ToggleButton toggleButton;
    private String path;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wav_player);
        Button button = (Button) findViewById(R.id.button6);
        textView = findViewById(R.id.textView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFilePicker();
            }
        });
        toggleButton = findViewById(R.id.toggleButton2);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(toggleButton.isChecked()){
                    if(wavReady && isStopped){
                        prepareMediaPlayer();
                        mp.start();
                    }
                    else if(wavReady){
                        mp.start();
                    }
                    else{
                        Toast.makeText(WavPlayerActivity.this, "Please select a Wav - File", Toast.LENGTH_LONG).show();
                    }

                }
                else {
                    if (wavReady) {
                        mp.pause();
                        isStopped = true;
                    } else {
                        Toast.makeText(WavPlayerActivity.this, "No Wav-File to stop", Toast.LENGTH_LONG).show();
                    }
                }
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
            this.path = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            prepareMediaPlayer();
        }
    }

    private void prepareMediaPlayer(){
        int index = path.lastIndexOf(".");
        String mimeType = path.substring(index + 1);
        if(mimeType.equals("wav")){
            try {
                Wav wav = new Wav(path);
                if (wav.getNumChannels() == 2) {
                    Converter.toMono(wav);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.mp = MediaPlayer.create(this, Uri.parse(this.path));
            wavReady = true;
            isStopped = false;
            int i = path.lastIndexOf("/");
            String fileName = path.substring(i + 1);
            textView.setText(fileName);

        }
        else{
            Toast.makeText(WavPlayerActivity.this, "Please select a Wav - File", Toast.LENGTH_LONG).show();
        }
    }

}
