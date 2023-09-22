package com.example.uklon_android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.uklon_android.DTOs.OrderDTO;
import com.example.uklon_android.R;
import com.example.uklon_android.classes.Order;
import com.example.uklon_android.classes.Transport;
import com.example.uklon_android.classes.Types;
import com.example.uklon_android.classes.User;
import com.example.uklon_android.interfaces.ApiService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDelActivity extends AppCompatActivity {

    ImageButton btnBack;
    TextView tvPrice, tvAdressStart, tvAdressEnd, tvSendUser;
    CheckBox dtd;
    Button btnNext;
    String startPoint, endPoint;
    float price = 0;
    User correctUser = new User();
    Transport transport = new Transport();
    List<Transport> trs = new ArrayList<>();
    OrderDTO order = new OrderDTO();
    ApiService apiService;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delivery_order_all_fields);

        btnBack = findViewById(R.id.back);
        tvPrice = findViewById(R.id.Price);
        tvAdressStart = findViewById(R.id.pointStart);
        tvAdressEnd = findViewById(R.id.endPoint);
        tvSendUser = findViewById(R.id.sendUser);
        dtd = findViewById(R.id.dtd);
        btnNext = findViewById(R.id.next);

        startPoint = (String) getIntent().getSerializableExtra("onePoint");
        endPoint = (String) getIntent().getSerializableExtra("twoPoint");
        price = (float) getIntent().getSerializableExtra("price");
        correctUser = (User) getIntent().getSerializableExtra("user");
        apiService = apiService.retrofit.create(ApiService.class);

        tvPrice.setText(String.valueOf(price));
        tvAdressStart.setText(startPoint);
        tvAdressEnd.setText(endPoint);
        tvSendUser.setText(correctUser.getFirstName() + " " + correctUser.getLastName());

        transport = new Transport();
        transport.setModel("Delivery car");
        transport.setDescription("Delivery car");
        transport.setType(1);
        trs.add(transport);

        order.setPrice(price);
        order.setType("Delivery");
        order.setStartPoint(startPoint);
        order.setEndPoint(endPoint);
        order.setDate(Calendar.getInstance().getTime());
        order.setUser(correctUser.getId());

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apiService.createOrder(order).enqueue(new Callback<OrderDTO>() {
                    @Override
                    public void onResponse(Call<OrderDTO> call, Response<OrderDTO> response) {
                        Intent intent = new Intent(OrderDelActivity.this, MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<OrderDTO> call, Throwable t) {
                        Log.d("Error", t.getMessage());
                    }
                });
            }
        });
    }
}