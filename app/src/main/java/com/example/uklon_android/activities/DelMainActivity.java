package com.example.uklon_android.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.uklon_android.R;
import com.example.uklon_android.classes.User;
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

public class DelMainActivity extends AppCompatActivity {

    ImageButton btnBack;
    TextView tvPrice;
    LinearLayout llTypePay, llCom, llADS;
    Button btnNext;
    String startPoint, endPoint;
    User correctUser = new User();
    float price = 100;
    float priceADS = 0;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delivery_main);

        btnBack = findViewById(R.id.button);
        tvPrice = findViewById(R.id.price);
        llTypePay = findViewById(R.id.typePay);
        llCom = findViewById(R.id.comment);
        llADS = findViewById(R.id.ads);
        btnNext = findViewById(R.id.next);

        startPoint = (String) getIntent().getSerializableExtra("onePoint");
        endPoint = (String) getIntent().getSerializableExtra("twoPoint");
        correctUser = (User) getIntent().getSerializableExtra("user");

        tvPrice.setText(String.valueOf(price));
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

        llTypePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DelMainActivity.this, PayDActivity.class);
                intent.putExtra("user", (User) getIntent().getSerializableExtra("user"));
                intent.putExtra("price", price);
                startActivity(intent);
            }
        });

        llCom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DelMainActivity.this, ComDActivity.class);
                intent.putExtra("price", price);
                startActivity(intent);
            }
        });

        llADS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DelMainActivity.this, ADSDActivity.class);
                intent.putExtra("user", (User) getIntent().getSerializableExtra("user"));
                intent.putExtra("price", price);
                intent.putExtra("onePoint", (String) getIntent().getSerializableExtra("onePoint"));
                intent.putExtra("twoPoint", (String) getIntent().getSerializableExtra("twoPoint"));
                startActivity(intent);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DelMainActivity.this, DeliveryActivity.class);
                startActivity(intent);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DelMainActivity.this, OrderDelActivity.class);
                intent.putExtra("onePoint", startPoint);
                intent.putExtra("twoPoint", endPoint);
                intent.putExtra("price", price);
                intent.putExtra("user", correctUser);
                startActivity(intent);
            }
        });

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                googleMap = map;
                // Налаштування карт, робота з мапою
                googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(DelMainActivity.this, R.raw.map));

                // Ініціалізація FusedLocationProviderClient
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(DelMainActivity.this);

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
}