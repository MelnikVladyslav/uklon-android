package com.example.uklon_android.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uklon_android.R;
import com.example.uklon_android.classes.User;
import com.example.uklon_android.interfaces.PlacesAdapter;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WhereToGoActivity extends AppCompatActivity {

    List<String> placeNames = new ArrayList<>();
    private RecyclerView recyclerView;
    private PlacesAdapter placesAdapter;
    double latitude,longitude;
    String curAdress;
    PlacesClient placesClient;
    String ApiKey = "AIzaSyD3XTBjDPC3c5VrztOIBq1WVWvuxTGXd2E";
    EditText startEdit;
    EditText endEdit;
    ImageButton btnBack;
    Button btnDone;
    User correctUser = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.where_to_go);

        latitude = (double) getIntent().getSerializableExtra("lat");
        longitude = (double) getIntent().getSerializableExtra("lon");
        curAdress = (String) getIntent().getSerializableExtra("startLoc");
        correctUser = (User) getIntent().getSerializableExtra("user");

        startEdit = findViewById(R.id.startPos);
        endEdit = findViewById(R.id.endAdress);
        btnBack = findViewById(R.id.backToMain);
        btnDone = findViewById(R.id.Send);
        startEdit.setText(curAdress);

        Places.initialize(getApplicationContext(), ApiKey);
        placesClient = Places.createClient(this);
        List<String> placesList = new ArrayList<>(); // Список місць
        placesAdapter = new PlacesAdapter(placesList);

        // Здійснюємо запит до Google Places API за допомогою PlacesClient
        List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME);
        FindCurrentPlaceRequest requestPlace = FindCurrentPlaceRequest.newInstance(placeFields);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<FindCurrentPlaceResponse> placeResponseTask = placesClient.findCurrentPlace(requestPlace);
        placeResponseTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FindCurrentPlaceResponse response = task.getResult();
                Log.d("result: ", response.getPlaceLikelihoods().toString());
                if (response != null) {
                    List<PlaceLikelihood> placeLikelihoods = response.getPlaceLikelihoods();
                    for (PlaceLikelihood placeLikelihood : placeLikelihoods) {
                        Log.d("places: ", placeLikelihood.getPlace().toString());
                        Place place = placeLikelihood.getPlace();
                        placeNames.add(place.getName().toString());
                    }
                    Log.d("List place name: ", placeNames.toString());

                    placesAdapter.setPlaces(placeNames);

                    recyclerView = findViewById(R.id.recyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                    recyclerView.setAdapter(placesAdapter);
                }
            } else {
                Exception exception = task.getException();
                if (exception != null) {
                    Log.e("MainActivity", "Error fetching places: " + exception.getMessage());
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WhereToGoActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WhereToGoActivity.this, InfoStOrderActivity.class);
                intent.putExtra("onePoint", startEdit.getText().toString());
                intent.putExtra("user", correctUser);
                intent.putExtra("twoPoint", endEdit.getText().toString());
                startActivity(intent);
            }
        });

    }
}