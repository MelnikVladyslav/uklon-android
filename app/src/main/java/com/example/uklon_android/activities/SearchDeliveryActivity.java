package com.example.uklon_android.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.uklon_android.R;
import com.example.uklon_android.classes.Types;
import com.example.uklon_android.classes.User;
import com.example.uklon_android.interfaces.ApiService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchDeliveryActivity extends AppCompatActivity {

    String onePoint, twoPoint;
    User correctUser = new User(), driver = new User();
    float price = 0;
    Types curType = new Types();
    TextView tvTypeName, tvPrice, tvStartP, tvEndPoint;
    List<User> listUsers = new ArrayList<>();
    ApiService apiService;
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
        setContentView(R.layout.search_taxi);

        onePoint = (String) getIntent().getSerializableExtra("onePoint");
        twoPoint = (String) getIntent().getSerializableExtra("twoPoint");
        price = (float) getIntent().getSerializableExtra("price");
        correctUser = (User) getIntent().getSerializableExtra("user");
        curType = (Types) getIntent().getSerializableExtra("type");

        apiService = apiService.retrofit.create(ApiService.class);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                googleMap = map;
                // Налаштування карт, робота з мапою
                googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(SearchDeliveryActivity.this, R.raw.map));

                // Ініціалізація FusedLocationProviderClient
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(SearchDeliveryActivity.this);

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

        tvTypeName = findViewById(R.id.typeName);
        tvPrice = findViewById(R.id.price);
        tvStartP = findViewById(R.id.startPoint);
        tvEndPoint = findViewById(R.id.endPoint);

        tvPrice.setText(String.valueOf(price));
        tvStartP.setText(onePoint);
        tvEndPoint.setText(twoPoint);

        apiService.getUsers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                listUsers = response.body();

                for (User user: listUsers) {
                    if (Objects.equals(user.getRole(), "Driver"))
                    {
                        driver = user;
                        Log.d("Name driver: ", driver.getFirstName());
                    }
                }

                if(driver != null)
                {
                    Intent intent = new Intent(SearchDeliveryActivity.this, OrderDelActivity.class);
                    intent.putExtra("onePoint", onePoint);
                    intent.putExtra("twoPoint", twoPoint);
                    intent.putExtra("user", correctUser);
                    intent.putExtra("driver", driver);
                    intent.putExtra("price", price);
                    intent.putExtra("type", curType);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d("Error", t.getMessage());
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