package com.example.uklon_android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.uklon_android.DTOs.PhoneNumberVerificationDto;
import com.example.uklon_android.R;
import com.example.uklon_android.classes.User;
import com.example.uklon_android.interfaces.ApiService;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmailActivity extends AppCompatActivity {

    EditText editEmail;
    Button btnNext;
    PhoneNumberVerificationDto phoneDTO = new PhoneNumberVerificationDto();
    ApiService apiService;
    User corUser = new User();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        editEmail = findViewById(R.id.editEmail);
        btnNext = findViewById(R.id.Next);
        apiService = apiService.retrofit.create(ApiService.class);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneDTO.setEmail(String.valueOf(editEmail.getText()));

                apiService.getUsers().enqueue(new Callback<List<User>>() {
                    @Override
                    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                        for (User user:response.body()) {
                            if (Objects.equals(user.getEmail(), phoneDTO.getEmail()))
                            {
                                corUser = user;
                                Intent intent = new Intent(EmailActivity.this, MainActivity.class);
                                intent.putExtra("user", corUser);
                                startActivity(intent);
                            }
                        }

                        if(corUser == null)
                        {
                            Intent intent = new Intent(EmailActivity.this, VerificationEmailActivity.class);
                            intent.putExtra("phoneDTO", phoneDTO);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<User>> call, Throwable t) {

                    }
                });
            }
        });

    }
}