package com.example.uklon_android.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.uklon_android.R;
import com.example.uklon_android.classes.Types;
import com.example.uklon_android.classes.User;
import com.example.uklon_android.interfaces.ApiService;
import com.example.uklon_android.interfaces.CarCardsAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderTaxiActivity extends AppCompatActivity implements CarCardsAdapter.OnCarClickListener {

    CarCardsAdapter cardsAdapter;
    RecyclerView recyclerView;
    ApiService apiService;
    TextView tvPrice;
    LinearLayout llTypePay, llCom, llServ;
    Button btnNext;
    ImageButton btnBack;
    Types curType = new Types();
    private GoogleMap googleMap;
    private SupportMapFragment mapFragment;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    Location location;
    double latitude;
    double longitude;
    LatLng currentLatLng;
    Marker myLocationMarker;
    float price = 0;
    float priceADS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order);

        apiService = apiService.retrofit.create(ApiService.class);
        tvPrice = findViewById(R.id.priceText);
        recyclerView = findViewById(R.id.recyclerView);
        llTypePay = findViewById(R.id.typePayments);
        llCom = findViewById(R.id.comm);
        llServ = findViewById(R.id.service);
        btnNext = findViewById(R.id.Next);
        btnBack = findViewById(R.id.button);

        if(getIntent().getSerializableExtra("price") != null)
        {
            price = (float) getIntent().getSerializableExtra("price");
            tvPrice.setText(String.valueOf(price));
        }

        if (getIntent().getSerializableExtra("priceAS") != null) {
            if ((int) getIntent().getSerializableExtra("priceAS") != 0) {
                int p = (int) getIntent().getSerializableExtra("priceAS");
                if (priceADS <= p) {
                    priceADS = p;
                    price += priceADS;
                }
                if (priceADS > p) {
                    priceADS = p;
                    price -= priceADS;
                }
                tvPrice.setText(String.valueOf(price));
            }
        }


        apiService.getTypes().enqueue(new Callback<List<Types>>() {
            @Override
            public void onResponse(Call<List<Types>> call, Response<List<Types>> response) {
                cardsAdapter = new CarCardsAdapter(response.body(), OrderTaxiActivity.this); // this посилається на OnCarClickListener
                cardsAdapter.setCars(response.body());
                recyclerView.setLayoutManager(new LinearLayoutManager(OrderTaxiActivity.this, LinearLayoutManager.HORIZONTAL, false));
                recyclerView.setAdapter(cardsAdapter);
            }

            @Override
            public void onFailure(Call<List<Types>> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                googleMap = map;
                // Налаштування карт, робота з мапою
                googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(OrderTaxiActivity.this, R.raw.map));

                // Ініціалізація FusedLocationProviderClient
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(OrderTaxiActivity.this);

                // Налаштування LocationRequest для отримання місцезнаходження
                locationRequest = new LocationRequest();
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                locationRequest.setInterval(5000);
                locationRequest.setFastestInterval(2000);

                // Створення LocationCallback для отримання змін місцезнаходження
                locationCallback = new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        if (locationResult != null) {
                            location = locationResult.getLastLocation();
                            // Отримання координат місцезнаходження і оновлення мапи
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            currentLatLng = new LatLng(latitude, longitude);
                            if (myLocationMarker == null) {
                                // Якщо маркер ще не створений, створюємо його
                                MarkerOptions markerOptions = new MarkerOptions()
                                        .position(currentLatLng)
                                        .title("Моє розташування");
                                myLocationMarker = googleMap.addMarker(markerOptions);
                            } else {
                                // Інакше оновлюємо координати маркера
                                myLocationMarker.setPosition(currentLatLng);
                            }
                            googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
                            float zoomLevel = 18.0f;
                            googleMap.animateCamera(CameraUpdateFactory.zoomTo(zoomLevel));
                        }
                    }
                };


                // Запуск отримання місцезнаходження
                startLocationUpdates();
            }
        });

        llTypePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderTaxiActivity.this, PayTActivity.class);
                intent.putExtra("user", (User) getIntent().getSerializableExtra("user"));
                startActivity(intent);
            }
        });

        llCom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderTaxiActivity.this, ComActivity.class);
                intent.putExtra("price", price);
                startActivity(intent);
            }
        });

        llServ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderTaxiActivity.this, ADSActivity.class);
                intent.putExtra("user", (User) getIntent().getSerializableExtra("user"));
                intent.putExtra("price", price);
                intent.putExtra("onePoint", (String) getIntent().getSerializableExtra("onePoint"));
                intent.putExtra("twoPoint", (String) getIntent().getSerializableExtra("twoPoint"));
                intent.putExtra("price", price);
                startActivity(intent);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderTaxiActivity.this, WhereToGoActivity.class);
                startActivity(intent);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderTaxiActivity.this, SearchTaxiActivity.class);
                intent.putExtra("onePoint", (String) getIntent().getSerializableExtra("onePoint"));
                intent.putExtra("user", (User) getIntent().getSerializableExtra("user"));
                intent.putExtra("twoPoint", (String) getIntent().getSerializableExtra("twoPoint"));
                intent.putExtra("price", price);
                intent.putExtra("type", curType);
                startActivity(intent);
            }
        });



    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Перевірка дозволів на місцезнаходження
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 404);
            return;
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    public void onCarClick(Types selectedType) {
        // Отримайте дані про обраний тип авто тут
        // Наприклад, ви можете встановити їх у змінну curType
        curType = selectedType;

        if (curType != null)
        {
            price = curType.getPrice();
            tvPrice.setText(String.valueOf(price));
        }

        // Тут ви можете зробити будь-яку додаткову роботу з обраним типом авто
    }
}