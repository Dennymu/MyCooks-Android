package com.mycooksapp.mycooks;

import android.app.Activity;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

class Utility {
    private Activity activity;
    private ConstraintLayout progressLayout;
    private ProgressBar progressBar;

    Utility() {}

    Utility(Activity activity) {
        this.activity = activity;
    }

    void showActionBar(Toolbar toolbar) {
        ((AppCompatActivity) activity).setSupportActionBar(toolbar);

        try {
            ((AppCompatActivity) activity).getSupportActionBar().setDisplayShowHomeEnabled(true);
            ((AppCompatActivity) activity).getSupportActionBar().setDisplayShowTitleEnabled(false);
        } catch(NullPointerException e) {
            Toast.makeText(activity, "Action bar failed :(", Toast.LENGTH_SHORT).show();
        }
    }

    void showActionBarWithTitle(Toolbar toolbar) {
        ((AppCompatActivity) activity).setSupportActionBar(toolbar);

        try {
            ((AppCompatActivity) activity).getSupportActionBar().setDisplayShowHomeEnabled(true);
            ((AppCompatActivity) activity).getSupportActionBar().setDisplayShowTitleEnabled(false);
        } catch(NullPointerException e) {
            Toast.makeText(activity, "Action bar failed :(", Toast.LENGTH_SHORT).show();
        }
    }

    void showProgress() {
        if (progressLayout == null || progressBar == null) { return; }
        progressLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    void hideProgress() {
        if (progressLayout == null || progressBar == null) { return; }
        progressLayout.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    void setProgress(ConstraintLayout layout, ProgressBar bar) {
        progressLayout = layout;
        progressBar = bar;
    }
}
