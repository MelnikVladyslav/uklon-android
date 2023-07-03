package com.example.uklon_android.activities;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uklon_android.R;
import com.example.uklon_android.classes.Transport;
import com.example.uklon_android.interfaces.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    public ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.0.2:5289")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        apiService.getDataList().
        enqueue(new Callback<List<Transport>>() {
            @Override
            public void onResponse(Call<List<Transport>> call, Response<List<Transport>> response) {
                if (response.isSuccessful()) {
                    List<Transport> dataList = response.body();
                    TextView textView = findViewById(R.id.textView);
                    StringBuilder stringBuilder = new StringBuilder();
                    for (Transport data : dataList) {
                        stringBuilder.append(data.getId()).append(": ").append(data.getModel()).append("\n");
                    }
                    textView.setText(stringBuilder.toString());

                } else {
                    Toast.makeText(MainActivity.this, "Помилка обробки", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Transport>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Помилка: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}