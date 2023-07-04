package com.example.uklon_android.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uklon_android.R;

public class EnterNamesActivity extends AppCompatActivity {

    private EditText firstNameEditText;
    private EditText surnameEditText;
    private Button enterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_names);

        firstNameEditText = findViewById(R.id.editTextFirstName);
        surnameEditText = findViewById(R.id.editTextSurname);
        enterButton = findViewById(R.id.buttonEnter);

        //Зробити перевірку імен і додати до створеного юзера


    }
}