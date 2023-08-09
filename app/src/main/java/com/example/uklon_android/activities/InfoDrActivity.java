package com.example.uklon_android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uklon_android.R;
import com.example.uklon_android.classes.Order;
import com.example.uklon_android.classes.Transport;
import com.example.uklon_android.classes.User;
import com.example.uklon_android.interfaces.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoDrActivity extends AppCompatActivity {

    String point1, point2;
    Button backBtn, contBtn;
    TextView priceView, startAd, endAd;
    Switch swDtD;
    int price = 140;
    User correctUser = new User();
    ApiService apiService;
    Order sendOrder = new Order();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_dr);

        backBtn = findViewById(R.id.btnBack);
        contBtn = findViewById(R.id.Continue);
        priceView = findViewById(R.id.textPrice);
        startAd = findViewById(R.id.StartAdress);
        endAd = findViewById(R.id.EndAdress);
        swDtD = findViewById(R.id.isDtD);

        correctUser = (User) getIntent().getSerializableExtra("user");
        point1 = (String) getIntent().getSerializableExtra("onePoint");
        point2 = (String) getIntent().getSerializableExtra("twoPoint");
        apiService = apiService.retrofit.create(ApiService.class);

        priceView.setText(String.valueOf(price));
        if(swDtD.isActivated())
        {
            price += 20;
            priceView.setText(String.valueOf(price));
        }
        if(!swDtD.isActivated())
        {
            price -= 20;
            priceView.setText(String.valueOf(price));
        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InfoDrActivity.this, MainActivity.class);
                intent.putExtra("isDel", false);
                startActivity(intent);
            }
        });

        contBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendOrder = new Order();
                sendOrder.setUser(correctUser.getId());
                sendOrder.setType("Taxi");
                sendOrder.setStartPoint((String) getIntent().getSerializableExtra("onePoint"));
                sendOrder.setEndPoint((String) getIntent().getSerializableExtra("twoPoint"));
                sendOrder.setPrice(price);


                Transport tr = new Transport();
                List<Transport> trs = new ArrayList<>();
                tr.setModel("New car");
                tr.setImage("https://cdn2.rcstatic.com/images/rc-guides/Economy_Cars/stan.jpg");
                tr.setType(1);
                tr.setDescription("new car");
                trs.add(tr);
                sendOrder.setTransports(trs);
                Log.d("transports:", sendOrder.getTransports().toString());

                apiService.createOrder(sendOrder)
                        .enqueue(new Callback<Order>() {
                            @Override
                            public void onResponse(Call<Order> call, Response<Order> response) {
                                sendOrder = response.body();
                                Toast.makeText(InfoDrActivity.this, "Responce: " + response.message(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(InfoDrActivity.this, MainActivity.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure(Call<Order> call, Throwable t) {
                                Toast.makeText(InfoDrActivity.this, "Помилка: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}