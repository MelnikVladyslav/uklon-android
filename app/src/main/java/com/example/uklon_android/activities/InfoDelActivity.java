package com.example.uklon_android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uklon_android.R;
import com.example.uklon_android.classes.User;

public class InfoDelActivity extends AppCompatActivity {

    String point1, point2, who;
    Button backBtn, contBtn;
    TextView priceView, startAd, endAd;
    Switch swDtD;
    int price = 50;
    User correctUser = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_del);

        backBtn = findViewById(R.id.btnBack);
        contBtn = findViewById(R.id.Continue);
        priceView = findViewById(R.id.textPrice);
        startAd = findViewById(R.id.StartAdress);
        endAd = findViewById(R.id.EndAdress);
        swDtD = findViewById(R.id.isDtD);

        correctUser = (User) getIntent().getSerializableExtra("user");
        point1 = (String) getIntent().getSerializableExtra("onePoint");
        point2 = (String) getIntent().getSerializableExtra("twoPoint");
        who = (String) getIntent().getSerializableExtra("who");

        priceView.setText(String.valueOf(price));
        if(swDtD.isActivated())
        {
            price += 20;
            priceView.setText(price);
        }
        if(!swDtD.isActivated())
        {
            price -= 20;
            priceView.setText(price);
        }

        if(who == "rec")
        {
            endAd.setText(point1);
            startAd.setText(point2);
        }
        if(who == "send")
        {
            startAd.setText(point1);
            endAd.setText(point2);
        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InfoDelActivity.this, MainActivity.class);
                intent.putExtra("isDel", false);
                startActivity(intent);
            }
        });

        contBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InfoDelActivity.this, DelPayActivity.class);
                intent.putExtra("point1", point1);
                intent.putExtra("point2", point2);
                intent.putExtra("price", price);
                intent.putExtra("user", correctUser);
                startActivity(intent);
            }
        });
    }
}