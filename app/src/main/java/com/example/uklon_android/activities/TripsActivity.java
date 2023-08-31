package com.example.uklon_android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.uklon_android.R;
import com.example.uklon_android.classes.Order;
import com.example.uklon_android.classes.User;
import com.example.uklon_android.interfaces.ApiService;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripsActivity extends AppCompatActivity {

    TextView tvListTrips;
    ApiService apiService;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);

        tvListTrips = findViewById(R.id.textView);

        userId = (String) getIntent().getSerializableExtra("userId");

        apiService = apiService.retrofit.create(ApiService.class);
        apiService.getOrders().enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                StringBuilder strBuild = new StringBuilder();

                for (Order order: response.body())
                {
                    if(Objects.equals(order.getUser(), userId)) {
                        strBuild.append("Name: ").append(order.getId()).append("\n Route: ").append(order.getStartPoint()).append(" - ").append(order.getEndPoint()).append("\n");
                    }
                }

                tvListTrips.setText(strBuild);
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {

            }
        });

    }
}