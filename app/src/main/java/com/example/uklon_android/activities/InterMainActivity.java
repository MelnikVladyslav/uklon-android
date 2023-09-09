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

public class InterMainActivity extends AppCompatActivity {

    TextView tvPrice;
    LinearLayout llTypePay, llCom;
    Button btnNext;
    User correctUser = new User();
    float price = 1450;
    String onePoint, twoPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inercity_main);

        tvPrice = findViewById(R.id.price);
        llTypePay = findViewById(R.id.typePay);
        llCom = findViewById(R.id.com);
        btnNext = findViewById(R.id.next);

        correctUser = (User) getIntent().getSerializableExtra("user");
        onePoint = (String) getIntent().getSerializableExtra("startPoint");
        twoPoint = (String) getIntent().getSerializableExtra("endPoint");

        tvPrice.setText(String.valueOf(price));

        llTypePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InterMainActivity.this, PayInterActivity.class);
                intent.putExtra("user", (User) getIntent().getSerializableExtra("user"));
                intent.putExtra("price", price);
                startActivity(intent);
            }
        });

        llCom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InterMainActivity.this, ComInterActivity.class);
                intent.putExtra("price", price);
                startActivity(intent);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InterMainActivity.this, SearchInterActivity.class);
                intent.putExtra("onePoint", onePoint);
                intent.putExtra("user", correctUser);
                intent.putExtra("twoPoint", twoPoint);
                intent.putExtra("price", price);
                startActivity(intent);
            }
        });

    }
}