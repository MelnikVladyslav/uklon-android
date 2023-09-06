package com.example.uklon_android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.uklon_android.R;
import com.example.uklon_android.classes.User;

public class DriverMainActivity extends AppCompatActivity {

    TextView tvPrice;
    LinearLayout llTypePay, llCom;
    Button btnNext;
    User correctUser = new User();
    float price = 145;
    String onePoint, twoPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_main);

        tvPrice = findViewById(R.id.price);
        llTypePay = findViewById(R.id.typePay);
        llCom = findViewById(R.id.com);
        btnNext = findViewById(R.id.next);

        correctUser = (User) getIntent().getSerializableExtra("user");
        onePoint = (String) getIntent().getSerializableExtra("onePoint");
        twoPoint = (String) getIntent().getSerializableExtra("twoPoint");

        tvPrice.setText(String.valueOf(price));

        llTypePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DriverMainActivity.this, PayDrActivity.class);
                intent.putExtra("user", (User) getIntent().getSerializableExtra("user"));
                intent.putExtra("price", price);
                startActivity(intent);
            }
        });

        llCom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DriverMainActivity.this, ComDrActivity.class);
                intent.putExtra("price", price);
                startActivity(intent);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DriverMainActivity.this, SearchDriverActivity.class);
                intent.putExtra("onePoint", onePoint);
                intent.putExtra("user", correctUser);
                intent.putExtra("twoPoint", twoPoint);
                intent.putExtra("price", price);
                startActivity(intent);
            }
        });

    }
}