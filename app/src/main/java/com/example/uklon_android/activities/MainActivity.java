package com.example.uklon_android.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uklon_android.DTOs.PhoneNumberVerificationDto;
import com.example.uklon_android.DTOs.UserDTO;
import com.example.uklon_android.R;
import com.example.uklon_android.classes.User;
import com.example.uklon_android.interfaces.ApiService;
import com.example.uklon_android.interfaces.PlacesAdapter;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements PlacesAdapter.OnPlaceClickListener {

    public ApiService apiService;
    private GoogleMap googleMap;
    private SupportMapFragment mapFragment;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private ImageButton showMenuButton;
    private static final int LOCATION_REQUEST = 500;
    User correctUser = new User();
    UserDTO sendUser = new UserDTO();
    GoogleSignInClient gsc;
    GoogleSignInOptions gso;
    Marker myLocationMarker;
    Uri urlAvatar;
    Geocoder geocoder;
    Location location;
    double latitude;
    double longitude;
    LatLng currentLatLng;
    String addressStrStart;
    String addressStrEnd;
    List<String> placeNames = new ArrayList<>();
    private RecyclerView recyclerView;
    private PlacesAdapter placesAdapter;
    PlacesClient placesClient;
    String ApiKey = "AIzaSyD3XTBjDPC3c5VrztOIBq1WVWvuxTGXd2E";
    boolean isExpanded = false;
    String imageUrl;
    PhoneNumberVerificationDto currentPhoneDTO;
    String tokenCurUser = "";
    List<User> listUser = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Places.initialize(getApplicationContext(), ApiKey);
        placesClient = Places.createClient(this);
        List<String> placesList = new ArrayList<>(); // Список місць
        placesAdapter = new PlacesAdapter(placesList, MainActivity.this);
        geocoder = new Geocoder(this, Locale.getDefault());
        apiService = apiService.retrofit.create(ApiService.class);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        addressStrEnd = (String) getIntent().getSerializableExtra("endAdress");

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
                }
            } else {
                Exception exception = task.getException();
                if (exception != null) {
                    Log.e("MainActivity", "Error fetching places: " + exception.getMessage());
                }
            }
        });

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            sendUser.setEmail(acct.getEmail());
            sendUser.setFirstName(acct.getFamilyName());
            sendUser.setLastName(acct.getGivenName());
            //found in database
            apiService.foundOrCreate(sendUser).
                    enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.isSuccessful()) {
                                correctUser = response.body();
                                urlAvatar = acct.getPhotoUrl();
                            } else {
                                Toast.makeText(MainActivity.this, "Помилка обробки: " + response.message(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Toast.makeText(MainActivity.this, "Помилка: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        //location and map
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // GPS вимкнений, відобразити сповіщення або запропонувати користувачеві увімкнути GPS
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Увімкнути GPS");
            builder.setMessage("Ваш GPS вимкнений, увімкнути його?");
            builder.setPositiveButton("Так", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Відкрити налаштування для увімкнення GPS
                    Intent intent = new Intent(MainActivity.this, GpsActivity.class);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("Ні", null);
            builder.create().show();
        }

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                googleMap = map;
                // Налаштування карт, робота з мапою
                googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(MainActivity.this, R.raw.map));

                // Ініціалізація FusedLocationProviderClient
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);

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

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        if (isLoggedIn) {
            GraphRequest request = GraphRequest.newMeRequest(
                    accessToken,
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {
                            try {
                                sendUser.setFirstName(object.getString("first_name"));
                                sendUser.setLastName(object.getString("last_name"));
                                sendUser.setEmail(object.getString("email"));
                                imageUrl = object.getJSONObject("picture")
                                        .getJSONObject("data")
                                        .getString("url");
                                //found in database
                                apiService.foundOrCreate(sendUser).
                                        enqueue(new Callback<User>() {
                                            @Override
                                            public void onResponse(Call<User> call, Response<User> response) {
                                                if (response.isSuccessful()) {
                                                    correctUser = response.body();
                                                    urlAvatar = Uri.parse(imageUrl);
                                                } else {
                                                    Toast.makeText(MainActivity.this, "Помилка: " + response.message(), Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<User> call, Throwable t) {
                                                Toast.makeText(MainActivity.this, "Помилка: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "first_name,last_name,email,picture");
            request.setParameters(parameters);
            request.executeAsync();
        }

        if(getIntent().getSerializableExtra("phoneDTO") != null)
        {
            currentPhoneDTO = (PhoneNumberVerificationDto) getIntent().getSerializableExtra("phoneDTO");
            if(currentPhoneDTO.getPhoneNumber() != null) {
                apiService.loginPhone(currentPhoneDTO).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        tokenCurUser = response.body();

                        apiService.getUsers().enqueue(new Callback<List<User>>() {
                            @Override
                            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                                listUser = response.body();

                                for (User user : listUser) {
                                    if(Objects.equals(user.getToken(), tokenCurUser))
                                    {
                                        correctUser = user;
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<List<User>> call, Throwable t) {

                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }
            if(currentPhoneDTO.getEmail() != null) {
                apiService.loginEmail(currentPhoneDTO).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        tokenCurUser = response.body();

                        apiService.getUsers().enqueue(new Callback<List<User>>() {
                            @Override
                            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                                listUser = response.body();

                                for (User user : listUser) {
                                    if(Objects.equals(user.getToken(), tokenCurUser))
                                    {
                                        correctUser = user;
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<List<User>> call, Throwable t) {

                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }
        }

        if(getIntent().getSerializableExtra("user") != null)
        {
            correctUser = (User) getIntent().getSerializableExtra("user");
        }

        showMenuButton = findViewById(R.id.button);
        showMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View popupView = getLayoutInflater().inflate(R.layout.popup_menu_layout, null);
                    // Показати попап-меню при натисканні кнопки
                    PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    // Знаходження кнопок в меню
                    ImageButton btnOption1 = popupView.findViewById(R.id.btnOption1);
                    ImageButton btnSelCity = popupView.findViewById(R.id.selCityBtn);
                    ImageView avatar = popupView.findViewById(R.id.avatar);
                    LinearLayout btnRegDr = popupView.findViewById(R.id.btnRegDr);
                    LinearLayout lltypePay = popupView.findViewById(R.id.typePay);
                    LinearLayout lltrips = popupView.findViewById(R.id.Trips);

                    //Avatar
                    if(acct != null)
                    {
                        Picasso.get().load(acct.getPhotoUrl()).into(avatar);
                        urlAvatar = acct.getPhotoUrl();
                    }
                    if(isLoggedIn)
                    {
                        Picasso.get().load(imageUrl).into(avatar);
                        urlAvatar = Uri.parse(imageUrl);
                    }

                    // Обробка натискання кнопок
                    // Profile
                    TextView nameUser = popupView.findViewById(R.id.nameUser);
                    nameUser.setText(correctUser.getFirstName());
                    btnOption1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                            intent.putExtra("user", correctUser);
                            if(urlAvatar != null) {
                                intent.putExtra("uriImg", urlAvatar.toString());
                            }
                            startActivity(intent);
                            finish();
                        }
                    });

                    //register driver
                    btnRegDr.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, RegDriverActivity.class);
                            intent.putExtra("user", correctUser);
                            startActivity(intent);
                            finish();
                        }
                    });

                    //select city
                    btnSelCity.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MainActivity.this, SelCityActivity.class);
                            startActivity(intent);
                        }
                    });

                    //type payment
                    lltypePay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MainActivity.this, PayMActivity.class);
                            intent.putExtra("user", correctUser);
                            startActivity(intent);
                        }
                    });

                    //trips
                    lltrips.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MainActivity.this, TripsActivity.class);
                            intent.putExtra("userId", correctUser.getId());
                            startActivity(intent);
                        }
                    });

                if(!isExpanded) {
                    // Відображення меню
                    popupWindow.showAsDropDown(v);
                }
                else {
                    popupWindow.dismiss();
                }

                isExpanded = !isExpanded;
            }
        });

        // Знаходимо макет bottom_sheet_layout.xml
        View bottomSheet = findViewById(R.id.popup_menu_main);
        bottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetDialog();
            }
        });
    }

    private void showBottomSheetDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        if (bottomSheetDialog.isShowing() == false) {
            // Створюємо новий BottomSheetDialog
            bottomSheetDialog = new BottomSheetDialog(this);
            bottomSheetDialog.setContentView(R.layout.bottom_menu);

            Button extranceBtn = bottomSheetDialog.findViewById(R.id.btnExtr);
            TextView pointStart = bottomSheetDialog.findViewById(R.id.pointStart);
            EditText pointEnd = bottomSheetDialog.findViewById(R.id.where_to_go);
            ImageButton btnAddHome = bottomSheetDialog.findViewById(R.id.addHome);
            ImageButton btnAddWork = bottomSheetDialog.findViewById(R.id.addWork);
            ImageButton btnLike = bottomSheetDialog.findViewById(R.id.Like);
            ImageButton btnDelivery = bottomSheetDialog.findViewById(R.id.delivery);
            ImageButton btnDriver = bottomSheetDialog.findViewById(R.id.driver);
            ImageButton btnInterCity = bottomSheetDialog.findViewById(R.id.intercity);

            latitude = location.getLatitude();
            longitude = location.getLongitude();

            if(addressStrEnd != null)
            {
                pointEnd.setText(addressStrEnd);
            }

            try {
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

                if (addresses != null && addresses.size() > 0) {
                    Address address = addresses.get(0);
                    String streetName = address.getThoroughfare(); // Назва вулиці
                    String houseNumber = address.getSubThoroughfare(); // Номер будинку
                    addressStrStart = streetName + " ," + houseNumber;

                    // Використовуйте streetName та houseNumber за потреби
                    pointStart.setText(addressStrStart);
                } else {
                    // Адресу не знайдено
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            ImageButton showRouteButton = bottomSheetDialog.findViewById(R.id.showRouteButton); // Замість R.id.showRouteButton вкажіть ID вашої кнопки для показу маршруту

            showRouteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addressStrEnd = String.valueOf(pointEnd.getText());
                    Intent intent = new Intent(MainActivity.this, WhereToGoActivity.class);
                    intent.putExtra("startLoc", addressStrStart);
                    if(addressStrEnd != null)
                    {
                        intent.putExtra("twoPoint", addressStrEnd);
                    }
                    intent.putExtra("user", correctUser);
                    startActivity(intent);
                }
            });

            recyclerView = bottomSheetDialog.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setAdapter(placesAdapter);

            btnDelivery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addressStrEnd = String.valueOf(pointEnd.getText());
                    Intent intent = new Intent(MainActivity.this, DeliveryActivity.class);
                    intent.putExtra("onePoint", addressStrStart);
                    intent.putExtra("user", correctUser);
                    if(addressStrEnd != null)
                    {
                        intent.putExtra("twoPoint", addressStrEnd);
                    }
                    startActivity(intent);
                }
            });

            btnDriver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addressStrEnd = String.valueOf(pointEnd.getText());
                    Intent intent = new Intent(MainActivity.this, EnterTypeAutoActivity.class);
                    intent.putExtra("onePoint", addressStrStart);
                    intent.putExtra("user", correctUser);
                    if(addressStrEnd != null)
                    {
                        intent.putExtra("twoPoint", addressStrEnd);
                    }
                    startActivity(intent);
                }
            });

            btnInterCity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, SelCitActivity.class);
                    intent.putExtra("user", correctUser);
                    startActivity(intent);
                }
            });

            // Налаштовуємо анімацію для відкриття та закриття BottomSheetDialog
            // Ви можете змінити анімацію на ваш смак
            bottomSheetDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

            // Показуємо BottomSheetDialog
            bottomSheetDialog.show();
        } else {
            // Закриваємо BottomSheetDialog
            bottomSheetDialog.dismiss();
            // Встановлюємо змінну bottomSheetDialog як null, щоб після закриття він знову міг з'явитися
            bottomSheetDialog = null;
        }
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

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public void onPlaceClick(String selectedPlace) {
        if(selectedPlace != null) {
            addressStrEnd = selectedPlace;
        }
    }
}