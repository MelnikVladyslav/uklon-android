package com.example.uklon_android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.uklon_android.R;
import com.example.uklon_android.classes.User;

public class EnterTypeAutoActivity extends AppCompatActivity {

    private RadioGroup radioGroup;
    private RadioButton selectedRadioButton;
    Button btnNext;
    String typeAuto = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_transmission);

        radioGroup = findViewById(R.id.radioGroup);
        btnNext = findViewById(R.id.next);
        selectedRadioButton = findViewById(radioGroup.getCheckedRadioButtonId());

        // Додайте слухача для RadioGroup, щоб відстежувати зміни вибору
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectedRadioButton = findViewById(checkedId);

                if (checkedId == R.id.automatic)
                {
                    typeAuto = "Automatic";
                    Log.d("type", typeAuto);
                }
                if (checkedId == R.id.manual)
                {
                    typeAuto = "Manual";
                    Log.d("type", typeAuto);
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EnterTypeAutoActivity.this, WhereToGoDriverActivity.class);
                intent.putExtra("onePoint", (String) getIntent().getSerializableExtra("onePoint"));
                intent.putExtra("user", (User) getIntent().getSerializableExtra("user"));
                intent.putExtra("twoPoint", (String) getIntent().getSerializableExtra("twoPoint"));
                startActivity(intent);
            }
        });
    }
}