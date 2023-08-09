package com.example.uklon_android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uklon_android.R;
import com.example.uklon_android.classes.Order;
import com.example.uklon_android.classes.Transport;
import com.example.uklon_android.classes.Types;
import com.example.uklon_android.classes.User;
import com.example.uklon_android.interfaces.ApiService;
import com.example.uklon_android.interfaces.CarCardsAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoStOrderActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CarCardsAdapter carsAdapter;
    Order sendOrder = new Order();
    User corUser = new User();
    List<Transport> trs = new ArrayList<>();
    ApiService apiService;
    Types typeCar = new Types();
    Button updateBtn, doneBtn, asBtn, cashBtn, comBtn;
    TextView priceTV;
    float pr = 0;
    int prAS = 0;
    boolean isPayPal = true;
    List<Types> t = new ArrayList<>();
    Transport tr = new Transport();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_st_order);

        corUser = (User) getIntent().getSerializableExtra("user");

        updateBtn = findViewById(R.id.update);
        asBtn = findViewById(R.id.serv);
        doneBtn = findViewById(R.id.done);
        cashBtn = findViewById(R.id.cash);
        comBtn = findViewById(R.id.comment);
        priceTV = findViewById(R.id.price);

        apiService = apiService.retrofit.create(ApiService.class);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        carsAdapter = new CarCardsAdapter(t);

        apiService.getTypes()
                .enqueue(new Callback<List<Types>>() {
                    @Override
                    public void onResponse(Call<List<Types>> call, Response<List<Types>> response) {
                        t = response.body();
                        if(t != null) {
                            carsAdapter = new CarCardsAdapter(t);
                            carsAdapter.setCars(t);
                            recyclerView.setAdapter(carsAdapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Types>> call, Throwable t) {

                    }
                });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(carsAdapter.getType() != null) {
                    typeCar = carsAdapter.getType();
                    pr = typeCar.getPrice();
                    priceTV.setText(String.valueOf(pr));
                }
                if((int) getIntent().getSerializableExtra("priceAS") != 0)
                {
                    int p = (int) getIntent().getSerializableExtra("priceAS");
                    if(prAS != p) {
                        prAS = p;
                        pr += prAS;
                    }
                    priceTV.setText(String.valueOf(pr));
                }
            }
        });

        asBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InfoStOrderActivity.this, ASActivity.class);
                intent.putExtra("user", corUser);
                intent.putExtra("onePoint", (String) getIntent().getSerializableExtra("onePoint"));
                intent.putExtra("twoPoint", (String) getIntent().getSerializableExtra("twoPoint"));
                startActivity(intent);
            }
        });

        comBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InfoStOrderActivity.this, ComActivity.class);
                startActivity(intent);
            }
        });

        cashBtn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InfoStOrderActivity.this, PayTActivity.class);
                intent.putExtra("user", corUser);
                startActivity(intent);
            }
        }));

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isPatPal = (Boolean) getIntent().getSerializableExtra("isPatPal");
                if (isPatPal != null && isPatPal) {
                    isPayPal = false;
                }

                sendOrder = new Order();
                sendOrder.setUser(corUser.getId());
                sendOrder.setType("Taxi");
                sendOrder.setStartPoint((String) getIntent().getSerializableExtra("onePoint"));
                sendOrder.setEndPoint((String) getIntent().getSerializableExtra("twoPoint"));
                sendOrder.setPrice(pr);


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
                                Toast.makeText(InfoStOrderActivity.this, "Responce: " + response.message(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(InfoStOrderActivity.this, MainActivity.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure(Call<Order> call, Throwable t) {
                                Toast.makeText(InfoStOrderActivity.this, "Помилка: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}