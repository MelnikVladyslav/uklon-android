package com.example.uklon_android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.uklon_android.DTOs.SelAdresessDTO;
import com.example.uklon_android.R;
import com.example.uklon_android.classes.User;
import com.example.uklon_android.interfaces.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelHome extends AppCompatActivity {

    ImageButton btnBack, btnEnter;
    EditText edName;
    SelAdresessDTO selAdress = new SelAdresessDTO();
    User correctUser = new User();
    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_house);

        btnBack = findViewById(R.id.back);
        btnEnter = findViewById(R.id.enter);
        edName = findViewById(R.id.nameHome);

        correctUser = (User) getIntent().getSerializableExtra("user");
        apiService = apiService.retrofit.create(ApiService.class);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelHome.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selAdress.setNameAdr(" ");
                selAdress.setNameHome(String.valueOf(edName.getText()));
                selAdress.setNameJob(" ");
                selAdress.setUserId(correctUser.getId());

                apiService.addSelAdrees(selAdress).enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        if(response.body() != null)
                        {
                            Intent intent = new Intent(SelHome.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {

                    }
                });
            }
        });
    }
}