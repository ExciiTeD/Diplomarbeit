package com.example.itaxn.diplomarbeit;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

public class MySnackBar {
    private Snackbar snackbar;
    private View snackbarView;

    public MySnackBar(View view, String msg) {
        this.snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG);
        this.snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(Color.DKGRAY);
        TextView textView = (TextView) snackbarView.findViewById
                (android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
    }

    public void setText(String msg) {
        this.snackbar.setText(msg);
    }

    public void show() {
        this.snackbar.show();
    }
}
