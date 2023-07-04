package com.example.uklon_android.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uklon_android.R;

public class LoginPasswordActivity extends AppCompatActivity {

    private EditText passwordEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_password);

        passwordEditText = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.buttonLogin);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Зробити запрос на сервер для пошуку юзера за номером телефону, або іншими даними
                //Якщо є перевірити пароль
                //Якщо ні то створити user і добавити у базу
            }
        });
    }
}