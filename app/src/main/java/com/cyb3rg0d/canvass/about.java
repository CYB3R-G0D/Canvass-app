package com.cyb3rg0d.canvass;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class about extends AppCompatActivity {

    TextView versionTxt,moreApps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about2);
        // Default actionBar color
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#3700B3")));

        versionTxt = findViewById(R.id.versionName);
        moreApps = findViewById(R.id.moreApps);

        String versionName = BuildConfig.VERSION_NAME;
        versionTxt.setText(versionName);

        moreApps.setMovementMethod(LinkMovementMethod.getInstance());
    }
}