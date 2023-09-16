package com.example.uklon_android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uklon_android.DTOs.PhoneNumberVerificationDto;
import com.example.uklon_android.R;

import java.util.Objects;

public class EnterPassEmailActivity extends AppCompatActivity {

    ImageButton btnBack, btnShowPass, btnShowConPass;
    EditText edtPass, edtConfPass;
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_password);

        btnBack = findViewById(R.id.back);
        btnShowPass = findViewById(R.id.showPass);
        btnShowPass = findViewById(R.id.showConPass);
        edtPass = findViewById(R.id.enterPass);
        edtConfPass = findViewById(R.id.confirmPass);
        btnNext = findViewById(R.id.next);

        edtPass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

        edtPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

        edtPass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == btnShowPass.getId()) {
                    if (edtPass.getTransformationMethod() == HideReturnsTransformationMethod.getInstance()) {
                        edtPass.setTransformationMethod(null);
                    } else {
                        edtPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    }
                    return true;
                }
                return false;
            }
        });

        edtConfPass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

        edtConfPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

        edtConfPass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == btnShowConPass.getId()) {
                    if (edtPass.getTransformationMethod() == HideReturnsTransformationMethod.getInstance()) {
                        edtPass.setTransformationMethod(null);
                    } else {
                        edtPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    }
                    return true;
                }
                return false;
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EnterPassEmailActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Objects.equals(edtPass.getText(), edtConfPass.getText()))
                {
                    Intent intent = new Intent(EnterPassEmailActivity.this, MainActivity.class);
                    intent.putExtra("phoneDTO", (PhoneNumberVerificationDto) getIntent().getSerializableExtra("phoneDTO"));
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(EnterPassEmailActivity.this, "Паролі не однакові", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}