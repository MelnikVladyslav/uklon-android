package com.example.uklon_android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uklon_android.R;
import com.example.uklon_android.classes.User;

public class SelCitActivity extends AppCompatActivity {

    EditText nc;
    Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_city);

        nc = findViewById(R.id.nameCity);
        send = findViewById(R.id.Send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelCitActivity.this, InfoInterCityActivity.class);
                intent.putExtra("endCity", nc.getText().toString());
                intent.putExtra("user", (User) getIntent().getSerializableExtra("user"));
                startActivity(intent);
            }
        });
    }
}