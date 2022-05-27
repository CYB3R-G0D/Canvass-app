package com.cyb3rg0d.canvass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class about extends AppCompatActivity {

    TextView versionTxt,moreApps;
    String items[] = new String[] {"License","Privacy Policy","Report","More apps"};
    String[] urls = {"https://github.com/CYB3R-G0D/Canvass-app/blob/main/LICENSE",
            "https://github.com/CYB3R-G0D/PRIVACY.md#privacy-policy",
            "https://github.com/CYB3R-G0D/Canvass-app/issues","https://github.com/CYB3R-G0D"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about2);
        // Default actionBar color
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#3700B3")));

        versionTxt = findViewById(R.id.versionName);

        String versionName = BuildConfig.VERSION_NAME;
        versionTxt.setText(versionName);

        ListView listView = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,items);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String clickedItem=(String) listView.getItemAtPosition(i);
                Intent openLinkIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urls[i]));
                startActivity(openLinkIntent);
            }
        });
    }
}