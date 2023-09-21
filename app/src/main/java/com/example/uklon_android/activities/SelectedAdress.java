package com.example.uklon_android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.uklon_android.R;
import com.example.uklon_android.classes.SelAdress;
import com.example.uklon_android.classes.User;
import com.example.uklon_android.interfaces.ApiService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectedAdress extends AppCompatActivity {

    ImageButton btnBack, btnAddHome, btnAddJob, btnAddFav;
    TextView tvHome, tvJob, tvFav;
    ApiService apiService;
    List<SelAdress> listSelAd = new ArrayList<>();
    User correctUser =new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_addresses);

        apiService = apiService.retrofit.create(ApiService.class);
        correctUser = (User) getIntent().getSerializableExtra("user");

        tvHome = findViewById(R.id.tvHome);
        tvJob = findViewById(R.id.tvJob);
        tvFav = findViewById(R.id.tvFav);
        btnBack =findViewById(R.id.back);
        btnAddHome =findViewById(R.id.addHome);
        btnAddJob =findViewById(R.id.addJob);
        btnAddFav =findViewById(R.id.addFav);

        apiService.getSelAdreses().enqueue(new Callback<List<SelAdress>>() {
            @Override
            public void onResponse(Call<List<SelAdress>> call, Response<List<SelAdress>> response) {
                if(response.body() != null)
                {
                    listSelAd = response.body();

                    for (SelAdress item: listSelAd)
                    {
                        if(Objects.equals(item.getUser(), correctUser.getId()))
                        {
                            if(item.getNameHome() != " ") {
                                tvHome.setText(item.getNameHome());
                            }
                            if(item.getNameJob() != " ") {
                                tvJob.setText(item.getNameJob());
                            }
                            if(item.getNameAdr() != " ") {
                                tvFav.setText(item.getNameAdr());
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<SelAdress>> call, Throwable t) {

            }
        });

        btnAddFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectedAdress.this, SelFav.class);
                intent.putExtra("user", correctUser);
                startActivity(intent);
            }
        });
    }
}