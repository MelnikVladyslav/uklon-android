package com.example.uklon_android.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.uklon_android.R;
import com.example.uklon_android.classes.Order;
import com.example.uklon_android.classes.Transport;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTActivity extends AppCompatActivity {

    TextView tvCancel, tvNameTr, tvNameDr, tvPrice, tvStartP, tvEndP;
    Button btnOk;
    String onePoint, twoPoint, nameTr;
    User correctUser = new User(), driver = new User();
    Transport transport = new Transport();
    Types curType = new Types();
    float price = 0;
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
    ApiService apiService;
    int idT = 0;
    Order order = new Order();
    List<Transport> trs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.arrives_taxi);

        apiService = apiService.retrofit.create(ApiService.class);
        tvCancel = findViewById(R.id.CancelOrder);
        tvNameTr = findViewById(R.id.NameTr);
        tvNameDr = findViewById(R.id.nameDr);
        tvPrice = findViewById(R.id.textPrice);
        tvStartP = findViewById(R.id.startPoint);
        tvEndP = findViewById(R.id.endPoint);
        btnOk = findViewById(R.id.ok);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                googleMap = map;
                // Налаштування карт, робота з мапою
                googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(OTActivity.this, R.raw.map));

                // Ініціалізація FusedLocationProviderClient
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(OTActivity.this);

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

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OTActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        nameTr = String.valueOf(tvNameTr.getText());
        onePoint = (String) getIntent().getSerializableExtra("onePoint");
        twoPoint = (String) getIntent().getSerializableExtra("twoPoint");
        correctUser = (User) getIntent().getSerializableExtra("user");
        driver = (User) getIntent().getSerializableExtra("driver");
        price = (float) getIntent().getSerializableExtra("price");
        curType = (Types) getIntent().getSerializableExtra("type");

        tvStartP.setText(onePoint);
        tvEndP.setText(twoPoint);
        tvPrice.setText(String.valueOf(price));
        tvNameDr.setText(String.valueOf(driver.getFirstName()));

        transport = new Transport();
        transport.setModel(nameTr);
        transport.setDescription("Taxi car");
        if (curType.getName() != null)
        {
            apiService.getTypes().enqueue(new Callback<List<Types>>() {
                @Override
                public void onResponse(Call<List<Types>> call, Response<List<Types>> response) {
                    List<Types> types = response.body();

                    for (Types type: types)
                    {
                        if(Objects.equals(curType.getName(), type.getName()))
                        {
                            idT = type.getId();
                            transport.setId(idT);
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Types>> call, Throwable t) {

                }
            });
        }
        trs.add(transport);

        order.setTransports(trs);
        order.setPrice(price);
        order.setType("Taxi");
        order.setStartPoint(onePoint);
        order.setEndPoint(twoPoint);
        order.setDate(Calendar.getInstance().getTime());
        order.setUser(correctUser.getId());

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apiService.createOrder(order).enqueue(new Callback<Order>() {
                    @Override
                    public void onResponse(Call<Order> call, Response<Order> response) {
                        Intent intent = new Intent(OTActivity.this, MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<Order> call, Throwable t) {
                        Log.d("Error", t.getMessage());
                    }
                });
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