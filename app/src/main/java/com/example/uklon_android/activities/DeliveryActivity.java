package com.example.uklon_android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.uklon_android.R;
import com.example.uklon_android.classes.User;

public class DeliveryActivity extends AppCompatActivity {

    ImageButton btnBack;
    Button btnSend, btnRec;
    String adressStart, adressEnd;
    User correctUser = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delivery);

        btnBack = findViewById(R.id.btnBackDel);
        btnSend = findViewById(R.id.SendDel);
        btnRec = findViewById(R.id.Resepient);

        adressStart = (String) getIntent().getSerializableExtra("onePoint");
        adressEnd = (String) getIntent().getSerializableExtra("twoPoint");
        correctUser = (User) getIntent().getSerializableExtra("user");

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DeliveryActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DeliveryActivity.this, DelMainActivity.class);
                intent.putExtra("onePoint", adressStart);
                intent.putExtra("twoPoint", adressEnd);
                intent.putExtra("user", correctUser);
                startActivity(intent);
            }
        });

        btnRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DeliveryActivity.this, DelMainActivity.class);
                intent.putExtra("onePoint", adressEnd);
                intent.putExtra("twoPoint", adressStart);
                intent.putExtra("user", correctUser);
                startActivity(intent);
            }
        });

    }
}