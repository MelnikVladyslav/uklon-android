package com.example.uklon_android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uklon_android.R;
import com.example.uklon_android.classes.User;

public class EnterTypeAutoActivity extends AppCompatActivity {

    RadioGroup rg;
    Button btnDone;
    boolean isA = false, isM = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_type_auto);

        rg = findViewById(R.id.typeAuto);
        btnDone = findViewById(R.id.button3);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.radioButton)
                {
                    isA = true;
                    isM = false;
                }
                else if(i == R.id.radioButton2)
                {
                    isA = false;
                    isM = true;
                }
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EnterTypeAutoActivity.this, WhereToGoDrActivity.class);
                intent.putExtra("startLoc", (String) getIntent().getSerializableExtra("onePoint"));
                intent.putExtra("user", (User) getIntent().getSerializableExtra("user"));
                startActivity(intent);
            }
        });
    }
}