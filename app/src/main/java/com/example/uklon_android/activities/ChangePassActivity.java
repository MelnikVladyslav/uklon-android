package com.example.uklon_android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uklon_android.DTOs.ChangeDTO;
import com.example.uklon_android.DTOs.PhoneNumberVerificationDto;
import com.example.uklon_android.R;
import com.example.uklon_android.classes.User;
import com.example.uklon_android.interfaces.ApiService;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassActivity extends AppCompatActivity {

    ImageButton btnBack, btnShowPass, btnShowConPass;
    EditText edtPass, edtConfPass;
    Button btnNext;
    User correctUser = new User();
    ChangeDTO changeDTO = new ChangeDTO();
    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_password);

        apiService = apiService.retrofit.create(ApiService.class);

        btnBack = findViewById(R.id.back);
        btnShowPass = findViewById(R.id.showPass);
        btnShowConPass = findViewById(R.id.showConPass);
        edtPass = findViewById(R.id.enterPass);
        edtConfPass = findViewById(R.id.confirmPass);
        btnNext = findViewById(R.id.next);

        correctUser = (User) getIntent().getSerializableExtra("user");


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChangePassActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("one p:", String.valueOf(edtPass.getText()));
                Log.d("two p:", String.valueOf(edtConfPass.getText()));
                if(Objects.equals(String.valueOf(edtPass.getText()), String.valueOf(edtConfPass.getText())))
                {
                    changeDTO.setUserId(correctUser.getId());
                    changeDTO.setPassword(String.valueOf(edtConfPass.getText()));
                    apiService.changePassword(changeDTO).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if(response.body() != null)
                            {
                                Intent intent = new Intent(ChangePassActivity.this, MainActivity.class);
                                intent.putExtra("user", response.body());
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {

                        }
                    });

                }
                else
                {
                    Toast.makeText(ChangePassActivity.this, "Паролі не однакові", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}