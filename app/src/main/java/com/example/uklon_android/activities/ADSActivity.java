package com.example.uklon_android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.uklon_android.R;
import com.example.uklon_android.classes.User;

public class ADSActivity extends AppCompatActivity {

    int prAS = 0;
    ImageButton backBtn;
    LinearLayout DWP, ED, EL;
    Button doneBtn;
    TextView tp;
    boolean isDWP = false;
    boolean isED = false;
    boolean isEL = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aditional_service);

        backBtn = findViewById(R.id.backASBtn);
        DWP = findViewById(R.id.RWP);
        ED = findViewById(R.id.ED);
        EL = findViewById(R.id.EL);
        tp = findViewById(R.id.TP);
        doneBtn = findViewById(R.id.Send);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ADSActivity.this, OrderTaxiActivity.class);
                intent.putExtra("user", (User) getIntent().getSerializableExtra("user"));
                startActivity(intent);
            }
        });

        DWP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isDWP = !isDWP;
                if(isDWP)
                {
                    prAS += 20;
                    tp.setText(String.valueOf(prAS));
                }
                else {
                    prAS -= 20;
                    tp.setText(String.valueOf(prAS));
                }
            }
        });
        ED.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isED = !isED;
                if(isED)
                {
                    prAS += 10;
                    tp.setText(String.valueOf(prAS));
                }
                else {
                    prAS -= 10;
                    tp.setText(String.valueOf(prAS));
                }
            }
        });
        EL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEL = !isEL;
                if(isEL)
                {
                    prAS += 30;
                    tp.setText(String.valueOf(prAS));
                }
                else {
                    prAS -= 30;
                    tp.setText(String.valueOf(prAS));
                }
            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ADSActivity.this, OrderTaxiActivity.class);
                intent.putExtra("priceAS", prAS);
                intent.putExtra("price", (float) getIntent().getSerializableExtra("price"));
                intent.putExtra("user", (User) getIntent().getSerializableExtra("user"));
                intent.putExtra("onePoint", (String) getIntent().getSerializableExtra("onePoint"));
                intent.putExtra("twoPoint", (String) getIntent().getSerializableExtra("twoPoint"));
                startActivity(intent);
            }
        });
    }
}