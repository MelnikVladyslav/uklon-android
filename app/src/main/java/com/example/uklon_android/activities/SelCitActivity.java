package com.example.uklon_android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uklon_android.R;
import com.example.uklon_android.classes.User;
import com.example.uklon_android.interfaces.City;
import com.example.uklon_android.interfaces.CityAdapter;
import com.example.uklon_android.interfaces.GeoNamesApiService;
import com.example.uklon_android.interfaces.GeoNamesResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SelCitActivity extends AppCompatActivity {

    CityAdapter cityAdapter;
    private RecyclerView recyclerView;
    Button btnNext;
    EditText edEnterName;
    String startCity = "Rivne";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_cit_pm);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.geonames.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        btnNext.findViewById(R.id.Send);
        edEnterName.findViewById(R.id.nameCity);

        GeoNamesApiService geoNamesApiService = retrofit.create(GeoNamesApiService.class);
        recyclerView = findViewById(R.id.recyclerView);

        String country = "UA"; // Код країни, наприклад, "UA" для України
        int maxRows = 100; // Максимальна кількість результатів
        String username = "vladadmin2222"; // Ваш GeoNames користувач

        Call<GeoNamesResponse> call = geoNamesApiService.getCities(country, maxRows, username);
        call.enqueue(new Callback<GeoNamesResponse>() {
            @Override
            public void onResponse(Call<GeoNamesResponse> call, Response<GeoNamesResponse> response) {
                if (response.isSuccessful()) {
                    GeoNamesResponse geoNamesResponse = response.body();
                    List<City> cities = geoNamesResponse.getGeonames();
                    List<String> citNames = new ArrayList<>();
                    for (City city : cities) {
                        String cityName = city.getName();
                        citNames.add(cityName);
                        Log.d("City", cityName);
                    }
                    cityAdapter = new CityAdapter(citNames);
                    cityAdapter.setPlaces(citNames);

                    recyclerView = findViewById(R.id.recyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(SelCitActivity.this, LinearLayoutManager.VERTICAL, false));
                    recyclerView.setAdapter(cityAdapter);
                } else {
                    Toast.makeText(SelCitActivity.this, "Помилка: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GeoNamesResponse> call, Throwable t) {
                Toast.makeText(SelCitActivity.this, "Помилка: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelCitActivity.this, InterMainActivity.class);
                intent.putExtra("user", (User) getIntent().getSerializableExtra("user"));
                intent.putExtra("startPoint", startCity);
                intent.putExtra("endPoint", String.valueOf(edEnterName.getText()));
            }
        });

    }
}