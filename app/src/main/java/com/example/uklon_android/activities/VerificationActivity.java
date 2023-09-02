package com.example.uklon_android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.uklon_android.DTOs.PhoneNumberVerificationDto;
import com.example.uklon_android.R;
import com.example.uklon_android.interfaces.ApiService;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerificationActivity extends AppCompatActivity {

    EditText editText1, editText2, editText3, editText4;
    ApiService apiService;
    PhoneNumberVerificationDto phoneDTO = new PhoneNumberVerificationDto();
    String code = "";

    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verification);

        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);
        editText4 = findViewById(R.id.editText4);
        apiService = apiService.retrofit.create(ApiService.class);

        editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    editText2.requestFocus();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        editText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    editText3.requestFocus();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        editText3.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    editText4.requestFocus();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        phoneDTO.setPhoneNumber((String) getIntent().getSerializableExtra("phone"));

        for (int i = 0; i < 4; i++) {
            int randomNumber = new Random().nextInt(10);
            code += String.valueOf(randomNumber);
        }

        apiService.verifPhone(phoneDTO).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {

            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });

        btnNext = findViewById(R.id.next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apiService.loginPhone(phoneDTO).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Intent intent = new Intent(VerificationActivity.this, MainActivity.class);
                        intent.putExtra("phoneDTO", phoneDTO);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }
        });

    }
}