package com.example.uklon_android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.uklon_android.R;

public class ComActivity extends AppCompatActivity {

    ImageButton btnBack;
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment);

        btnBack = findViewById(R.id.back);
        btnNext = findViewById(R.id.Send);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ComActivity.this, OrderTaxiActivity.class);
                intent.putExtra("price", (float) getIntent().getSerializableExtra("price"));
                startActivity(intent);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ComActivity.this, OrderTaxiActivity.class);
                intent.putExtra("price", (float) getIntent().getSerializableExtra("price"));
                startActivity(intent);
            }
        });

    }
}