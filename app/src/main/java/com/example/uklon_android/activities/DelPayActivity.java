package com.example.uklon_android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uklon_android.R;
import com.example.uklon_android.classes.Order;
import com.example.uklon_android.classes.User;
import com.example.uklon_android.interfaces.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DelPayActivity extends AppCompatActivity {

    Button btnOk;
    TextView textPr;
    Order sendOrd;
    User curUser;
    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_del_pay);

        btnOk = findViewById(R.id.button2);
        textPr = findViewById(R.id.textView);
        curUser = (User) getIntent().getSerializableExtra("user");
        apiService = apiService.retrofit.create(ApiService.class);

        int pr = (int) getIntent().getSerializableExtra("price");
        textPr.setText(String.valueOf(pr));

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendOrd = new Order();
                sendOrd.setEndPoint((String) getIntent().getSerializableExtra("point2"));
                sendOrd.setStartPoint((String) getIntent().getSerializableExtra("point1"));
                sendOrd.setType("Delivery");
                sendOrd.setUser(curUser.getId());



                apiService.createOrder(sendOrd)
                        .enqueue(new Callback<Order>() {
                            @Override
                            public void onResponse(Call<Order> call, Response<Order> response) {
                                Toast.makeText(DelPayActivity.this, "Успішно: " + response.message(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(DelPayActivity.this, MainActivity.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure(Call<Order> call, Throwable t) {
                                Toast.makeText(DelPayActivity.this, "Помилка: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }
}