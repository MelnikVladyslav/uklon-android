package com.example.uklon_android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uklon_android.R;

public class GpsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_accept);

        Button btnEnableGPS = findViewById(R.id.next);
        btnEnableGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Відкрити налаштування для увімкнення GPS
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        Button btnskip = findViewById(R.id.skip);
        btnskip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Відкрити налаштування для увімкнення GPS
                Intent intent = new Intent(GpsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}