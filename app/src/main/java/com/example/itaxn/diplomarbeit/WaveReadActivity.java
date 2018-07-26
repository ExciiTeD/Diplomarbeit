package com.example.itaxn.diplomarbeit;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.itaxn.diplomarbeit.stego.lsbmachine.LSBReader;
import com.example.itaxn.diplomarbeit.stego.lsbmachine.LSBReaderDecrypter;
import com.example.itaxn.diplomarbeit.stego.lsbmachine.Wav;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.IOException;
import java.nio.file.Path;

public class WaveReadActivity extends AppCompatActivity implements View.OnClickListener {
    private String pw;
    private String path;
    private MySnackBar snackBar;

    public static final int FILE_PICKER_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wave_read);

        //Search for password from SearchFileActivity
        Button btnFind = (Button) findViewById(R.id.buttonFindMSG);
        btnFind.setOnClickListener(this);

        Button btn = (Button) findViewById(R.id.buttonFileSearch2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFilePicker();
            }

        });

        Button btn1 = findViewById(R.id.button9);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(WaveReadActivity.this, HomeActivity.class);
                //startActivity(i , ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                startActivity(i);
            }

        });
       /* Button btn2 = findViewById(R.id.button10);
        btn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent i = new Intent(WaveReadActivity.this , HomeActivity.class);
                //startActivity(i , ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                startActivity(i);
            }

        });*/
        this.snackBar = new MySnackBar(findViewById(R.id.main_layout_id), "");
        //TODO

        //FEHLER
    }

    /**
     * proofs if data was entered into the fields
     *
     * @return boolean weather data was entered or not.
     */
    private boolean allSet() {
        boolean erf = false;
        EditText editText = this.findViewById(R.id.editText_Password2);
        pw = editText.getText().toString();

        boolean isNotNull = path != null && pw != null;
        boolean isFilled = !pw.trim().equals("");

        if (isNotNull && isFilled) {
            erf = true;
        }
        return erf;
    }

    private void showError() {
        Toast.makeText(this, "Allow external storage reading", Toast.LENGTH_SHORT).show();
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

            if (path != null) {
                Log.d("Path: ", path);
                this.path = path;
                TextView tV = findViewById(R.id.textView_File2);
                tV.append(" " + path);
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view == (Button) findViewById(R.id.buttonFindMSG)) {
            if (allSet()) {
                //Toast.makeText(WaveReadActivity.this, "Find IT!", Toast.LENGTH_LONG).show();
                Wav wav = null;
                try {
                    wav = new Wav(path);
                    LSBReaderDecrypter reader = new LSBReaderDecrypter(wav);
                    String s = new String(reader.readMessage(pw));
                    Intent intent = new Intent(this, TextActivity.class);
                    intent.putExtra("message", s);
                    //NEU
                    int index = path.lastIndexOf("/");
                    String fileName = path.substring(index + 1);
                    intent.putExtra("fileName", fileName);
                    //
                    startActivity(intent);
                } catch (Exception e) {
                    this.snackBar.setText("No data could be found!");
                    this.snackBar.show();
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(WaveReadActivity.this, "Not enough information!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
