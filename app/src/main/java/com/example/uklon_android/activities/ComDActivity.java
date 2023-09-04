package com.example.uklon_android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uklon_android.R;

public class ComDActivity extends AppCompatActivity {

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
                Intent intent = new Intent(ComDActivity.this, DelMainActivity.class);
                intent.putExtra("price", (float) getIntent().getSerializableExtra("price"));
                startActivity(intent);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ComDActivity.this, DelMainActivity.class);
                intent.putExtra("price", (float) getIntent().getSerializableExtra("price"));
                startActivity(intent);
            }
        });

    }
}