package com.example.uklon_android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uklon_android.R;

public class PhoneCodeActivity extends AppCompatActivity {

    private EditText codeEditText;
    private Button loginButton;
    public String codeGen;

    private boolean isValidCode(String code)
    {
        if (code == codeGen)
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_code);

        codeEditText = findViewById(R.id.editTextCode);
        loginButton = findViewById(R.id.buttonLogin);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Виконати логіку для перевірки номера телефону та входу
                // наприклад, відправити OTP-повідомлення на номер телефону та перехід до наступної активності

                // Приклад перехіду до наступної активності після успішного входу

                String codeEntred = codeEditText.getText().toString();

                if (isValidCode(codeEntred)) {
                    Intent intent = new Intent(PhoneCodeActivity.this, EnterNamesActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(PhoneCodeActivity.this, "Невірний код", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}