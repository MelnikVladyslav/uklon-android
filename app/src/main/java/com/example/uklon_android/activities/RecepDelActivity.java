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

public class RecepDelActivity extends AppCompatActivity {

    String point1, point2;
    EditText startAd, endAd;
    ImageButton backBtn;
    Button doneBtn;
    List<String> placeNames = new ArrayList<>();
    private RecyclerView recyclerView;
    private PlacesAdapter placesAdapter;
    PlacesClient placesClient;
    String ApiKey = "AIzaSyD3XTBjDPC3c5VrztOIBq1WVWvuxTGXd2E";
    User correctUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recep_del);

        backBtn = findViewById(R.id.backToMain);
        startAd = findViewById(R.id.startPos);
        endAd = findViewById(R.id.endAdress);
        doneBtn = findViewById(R.id.Send);

        point1 = (String) getIntent().getSerializableExtra("onePoint");
        point2 = (String) getIntent().getSerializableExtra("twoPoint");
        correctUser = (User) getIntent().getSerializableExtra("user");
        endAd.setText(point1);
        if(point2 != null)
        {
            startAd.setText(point2);
        }

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

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecepDelActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecepDelActivity.this, InfoDelActivity.class);
                intent.putExtra("OnePoint", point1);
                intent.putExtra("TwoPoint", point2);
                intent.putExtra("who", "rec");
                intent.putExtra("user", correctUser);
                startActivity(intent);
            }
        });
    }
}