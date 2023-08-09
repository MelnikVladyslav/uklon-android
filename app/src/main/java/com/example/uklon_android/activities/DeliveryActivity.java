package com.example.uklon_android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uklon_android.R;

public class DeliveryActivity extends AppCompatActivity {

    ImageButton btnBack;
    Button btnSend, btnRecep;
    String point1, point2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delivery);

        btnBack = findViewById(R.id.btnBackDel);
        btnSend = findViewById(R.id.SendDel);
        btnRecep = findViewById(R.id.Resepient);

        point1 = (String) getIntent().getSerializableExtra("onePoint");
        point2 = (String) getIntent().getSerializableExtra("twoPoint");

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
                Intent intent = new Intent(DeliveryActivity.this, SendDelActivity.class);
                intent.putExtra("onePoint", point1);
                if(point2 != null)
                {
                    intent.putExtra("twoPoint", point2);
                }
                startActivity(intent);
            }
        });

        btnRecep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DeliveryActivity.this, RecepDelActivity.class);
                intent.putExtra("onePoint", point1);
                if(point2 != null)
                {
                    intent.putExtra("twoPoint", point2);
                }
                startActivity(intent);
            }
        });
    }
}