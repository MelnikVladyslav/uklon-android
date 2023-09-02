package com.example.uklon_android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.uklon_android.DTOs.PhoneNumberVerificationDto;
import com.example.uklon_android.R;
import com.example.uklon_android.classes.User;
import com.example.uklon_android.interfaces.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnterNameActivity extends AppCompatActivity {

    ImageButton btnBack;
    EditText etName, etSurname;
    Button btnNext;
    ApiService apiService;
    PhoneNumberVerificationDto phoneDTO = new PhoneNumberVerificationDto();
    User corUser = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entering_name);

        btnBack = findViewById(R.id.back);
        etName = findViewById(R.id.NameInput);
        etSurname = findViewById(R.id.SurnameInput);
        btnNext = findViewById(R.id.next);

        apiService = apiService.retrofit.create(ApiService.class);

        phoneDTO = (PhoneNumberVerificationDto) getIntent().getSerializableExtra("phoneDTO");

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EnterNameActivity.this, EmailActivity.class);
                startActivity(intent);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder nameBuild = new StringBuilder().append(etName.getText()).append(" ").append(etSurname.getText());

                phoneDTO.setPhoneNumber(String.valueOf(nameBuild));
                apiService.loginEmail(phoneDTO).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String token = response.body();

                        apiService.getUsers().enqueue(new Callback<List<User>>() {
                            @Override
                            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                                for (User user: response.body()) {
                                    if(user.getToken() == token)
                                    {
                                        corUser = user;
                                        Intent intent = new Intent(EnterNameActivity.this, MainActivity.class);
                                        intent.putExtra("user", corUser);
                                        startActivity(intent);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<List<User>> call, Throwable t) {

                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }
        });

    }
}