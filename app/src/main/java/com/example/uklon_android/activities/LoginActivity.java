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
        setContentView(R.layout.login_screen);

        phoneNumberEditText = findViewById(R.id.phoneNumberTextView);
        loginButton = findViewById(R.id.button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = phoneNumberEditText.getText().toString();

                //Створювати юзера і додати туди номер

                if (isValidPhoneNumber(phoneNumber)) {
                    Intent intent = new Intent(LoginActivity.this, PhoneCodeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Невірний номер телефону", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}