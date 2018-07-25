package com.example.itaxn.diplomarbeit;

import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.itaxn.diplomarbeit.fft.FastFourierTransformation;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class RecordingActivity extends AppCompatActivity implements View.OnClickListener {
    private Button buttonstart;
    private Button buttonstop;
    private AudioRecord recorder;
    private TextView tv;
    private FastFourierTransformation fft;
    private short[] audioData;
    private static final short AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    private static final short CHANNEL_CONF = AudioFormat.CHANNEL_IN_MONO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);
        this.buttonstart = (Button) findViewById(R.id.button5);
        this.buttonstop = (Button) findViewById(R.id.button);
        this.buttonstart.setOnClickListener(this);
        this.buttonstop.setOnClickListener(this);
        Button btn1 = findViewById(R.id.button12);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RecordingActivity.this, HomeActivity.class);
                //startActivity(i , ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                startActivity(i);
            }

        });
        this.init();
    }

    private void init() {
        int minBufferSize = AudioRecord.getMinBufferSize(44100, CHANNEL_CONF, AUDIO_FORMAT);
        if (minBufferSize <= 8192) {
            minBufferSize = 8192;
        }

        audioData = new short[minBufferSize / 2];
        this.recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, 44100,
                CHANNEL_CONF, AUDIO_FORMAT, minBufferSize);
    }

    @Override
    public void onClick(View view) {
        if (buttonstart == view) {
            this.recordAudio();
        } else {
            recorder.stop();
        }
    }

    private void recordAudio() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                recorder.startRecording();

                ArrayList<Boolean> al = new ArrayList<Boolean>();

                while (recorder.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
                    recorder.read(audioData, 0, audioData.length);
                    double[] d = new double[audioData.length];
                    for (int i = 0; i < audioData.length; i++) {
                        d[i] = (double) audioData[i];
                    }
                    fft = new FastFourierTransformation(d);
                    final double[] fftData = fft.doComplexFFT();


                    boolean val = false;
                    for (int i = 0; i < fftData.length; i++) {
                        if (((i + 1) * 10.76660156) > 19950 && ((i + 1) * 10.76660156) < 20200 && fftData[i] > 0.1) {
                            Log.i("", "run: amk");
                            val = true;
                        } else if (((i + 1) * 10.76660156) > 19990 && ((i + 1) * 10.76660156) < 20200 && fftData[i] < 1E-3) {
                            //al.add(false);
                        }
                    }
                    al.add(val);
                }

                int i = 0;
                int j = 0;
                for (Boolean aBoolean : al) {
                    j++;
                    if (aBoolean) i++;
                }
                Log.i("haha", "Counter: " + i);
                Log.i("haha", "Total amount: " + j);
            }
        }).start();
    }
}
