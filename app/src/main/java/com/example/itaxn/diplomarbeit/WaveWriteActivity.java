package com.example.itaxn.diplomarbeit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.itaxn.diplomarbeit.stego.lsbmachine.LSBHiderEncrypter;
import com.example.itaxn.diplomarbeit.stego.lsbmachine.MessageTooLongException;
import com.example.itaxn.diplomarbeit.stego.lsbmachine.Wav;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.FileNotFoundException;
import java.io.IOException;

public class WaveWriteActivity extends AppCompatActivity implements View.OnClickListener {
    private String pw;
    private String msg;
    private String path;
    private Wav wav;
    private MySnackBar snackBar;

    public static final int FILE_PICKER_REQUEST_CODE = 1;

    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wave_write);
        final Button btnMSG = (Button) findViewById(R.id.buttonHideMSG);
        btnMSG.setOnClickListener(this);
        Button btn = (Button) findViewById(R.id.buttonFileSearch);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFilePicker();
            }

        });
        this.snackBar = new MySnackBar(findViewById(R.id.main_layout_id1), "");
        Button btn1 = findViewById(R.id.button11);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(WaveWriteActivity.this, HomeActivity.class);
                //startActivity(i , ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                startActivity(i);
            }

        });
    }

    private boolean allSet() {
        boolean erf = false;
        EditText editText = this.findViewById(R.id.editText_Password);
        EditText editText2 = this.findViewById(R.id.editText2);
        pw = editText.getText().toString();
        msg = editText2.getText().toString();

        boolean isNotNull = path != null && pw != null && msg != null;
        boolean isFilled = !pw.trim().equals("") && !msg.trim().equals("");

        if (isNotNull && isFilled) {
            erf = true;
        }

        return erf;
    }

    private void openFilePicker() {
        new MaterialFilePicker()
                .withActivity(this)
                .withRequestCode(FILE_PICKER_REQUEST_CODE)
                .withHiddenFiles(false)
                .withTitle("Search WAV-File")
                .start();
    }

    private void hideMsg() {
        try {
            LSBHiderEncrypter hider = new LSBHiderEncrypter(this.wav);
            hider.hideEncrypted(msg, pw);
            this.snackBar.setText("Successs!");
            this.snackBar.show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MessageTooLongException e) {
            this.snackBar.setText("The Message is too long for this file!");
            this.snackBar.show();
        } catch (Exception e) {
            this.snackBar.setText("Some Error occured");
            this.snackBar.show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            String path = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);

            if (path != null) {
                this.path = path;
                try {
                    this.wav = new Wav(this.path);
                } catch (FileNotFoundException e) {
                    this.path = "";
                    this.snackBar.setText("That is not a wav file");
                    this.snackBar.show();
                } catch (IOException e) {
                }

                TextView tV = findViewById(R.id.textView_File);
                tV.append(" " + path);
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view == findViewById(R.id.buttonHideMSG)) {
            //Hide your MSG
            if (allSet()) {
                new Thread(new Runnable() {
                    public void run() {
                        hideMsg();
                    }
                }).start();
            } else {
                Toast.makeText(WaveWriteActivity.this,
                        "Some input is empty! :", Toast.LENGTH_LONG).show();
            }
        }
    }
}
