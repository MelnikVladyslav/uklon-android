package com.example.uklon_android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uklon_android.DTOs.PhoneNumberVerificationDto;
import com.example.uklon_android.R;
import com.example.uklon_android.interfaces.ApiService;

import java.util.Objects;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerificationEmailActivity extends AppCompatActivity {

    EditText editText1, editText2, editText3, editText4;
    ApiService apiService;
    PhoneNumberVerificationDto phoneDTO = new PhoneNumberVerificationDto();
    Integer code = 0;
    Integer codeEnter = 0;

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

        phoneDTO.setEmail((String) getIntent().getSerializableExtra("email"));


        apiService.verifEmail(phoneDTO).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                code = response.body();
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });

        btnNext = findViewById(R.id.next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String firstNumber = editText1.getText().toString();
                String secondNumber = editText2.getText().toString();
                String thirdNumber = editText3.getText().toString();
                String fourthNumber = editText4.getText().toString();

                String concatenatedNumbers = firstNumber + secondNumber + thirdNumber + fourthNumber;

                codeEnter = Integer.parseInt(concatenatedNumbers);

                if(Objects.equals(code, codeEnter))
                {
                    Intent intent = new Intent(VerificationEmailActivity.this, EnterPassEmailActivity.class);
                    intent.putExtra("phoneDTO", phoneDTO);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(VerificationEmailActivity.this, "Невірний код", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}