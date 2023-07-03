package com.example.uklon_android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uklon_android.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private EditText phoneNumberEditText;
    private Button loginButton;

    private boolean isValidPhoneNumber(String phoneNumber) {
        Pattern pattern = Pattern.compile("^\\+38[0-9]{10,13}$");
        Matcher matcher = pattern.matcher(phoneNumber);
        boolean isValid = matcher.matches();

        if (isValid) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phoneNumberEditText = findViewById(R.id.editTextPhoneNumber);
        loginButton = findViewById(R.id.buttonLogin);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = phoneNumberEditText.getText().toString();

                if (isValidPhoneNumber(phoneNumber)) {
                    // Виконати логіку для перевірки номера телефону та входу
                    // наприклад, відправити OTP-повідомлення на номер телефону та перехід до наступної активності

                    // Приклад перехіду до наступної активності після успішного входу
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // Закрити поточну активність, щоб користувач не міг повернутися назад до сторінки входу
                } else {
                    Toast.makeText(LoginActivity.this, "Невірний номер телефону", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}