package com.example.uklon_android.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.uklon_android.DTOs.CardDTO;
import com.example.uklon_android.DTOs.UserDTO;
import com.example.uklon_android.R;
import com.example.uklon_android.classes.Card;
import com.example.uklon_android.classes.User;
import com.example.uklon_android.interfaces.ApiService;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public ApiService apiService;
    private GoogleMap googleMap;
    private SupportMapFragment mapFragment;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Button showMenuButton;
    private static final int SCAN_REQUEST_CODE = 101;
    User correctUser = new User();
    UserDTO sendUser = new UserDTO();
    CardDTO newCard = new CardDTO();
    GoogleSignInClient gsc;
    GoogleSignInOptions gso;
    Marker myLocationMarker;
    Uri urlAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiService = apiService.retrofit.create(ApiService.class);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!=null){
            sendUser.setEmail(acct.getEmail());
            sendUser.setFirstName(acct.getFamilyName());
            sendUser.setLastName(acct.getGivenName());
            //found in database
            apiService.foundOrCreate(sendUser).
                    enqueue(new Callback<User>()
                    {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.isSuccessful()) {
                                correctUser = response.body();
                                urlAvatar = acct.getPhotoUrl();
                            }
                            else{
                                Toast.makeText(MainActivity.this, "Помилка: " + response.message(), Toast.LENGTH_SHORT).show();
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
                            Location location = locationResult.getLastLocation();
                            // Отримання координат місцезнаходження і оновлення мапи
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            LatLng currentLatLng = new LatLng(latitude, longitude);
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

        if(isLoggedIn){
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
                                String imageUrl = object.getJSONObject("picture")
                                        .getJSONObject("data")
                                        .getString("url");
                                //found in database
                                apiService.foundOrCreate(sendUser).
                                        enqueue(new Callback<User>()
                                        {
                                            @Override
                                            public void onResponse(Call<User> call, Response<User> response) {
                                                if (response.isSuccessful()) {
                                                    correctUser = response.body();
                                                    urlAvatar = Uri.parse(imageUrl);
                                                }
                                                else{
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

        showMenuButton = findViewById(R.id.button);
        showMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Показати попап-меню при натисканні кнопки
                View popupView = getLayoutInflater().inflate(R.layout.popup_menu_layout, null);
                PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                // Знаходження кнопок в меню
                Button btnOption1 = popupView.findViewById(R.id.btnOption1);
                Button btnScanCard = popupView.findViewById(R.id.btnScan);
                Button btnSignOut = popupView.findViewById(R.id.BtnSign);
                Button btnPay = popupView.findViewById(R.id.btnPay);
                Button btnRegDr = popupView.findViewById(R.id.btnRegDr);

                // Обробка натискання кнопок
                // Profile
                btnOption1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                        intent.putExtra("user", correctUser);
                        intent.putExtra("uriImg", urlAvatar);
                        startActivity(intent);
                        finish();
                    }
                });

                // Scan card
                btnScanCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        scanCard();
                    }
                });

                //sign out
                btnSignOut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                           if(gsc != null) {
                               signOut();
                           }
                           if(isLoggedIn)
                           {
                               signFacebook();
                           }
                    }
                });

                //pay
                btnPay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, PayActivity.class);
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

                // Відображення меню
                popupWindow.showAsDropDown(v);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SCAN_REQUEST_CODE) {
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

                // Николи не передавайте свій cardNumber напряму через ненадійні додатки.
                newCard.setNumber(scanResult.getRedactedCardNumber());

                newCard.setUserId(correctUser.getId());


                apiService.addCard(newCard).enqueue(new Callback<Card>() {
                    @Override
                    public void onResponse(Call<Card> call, Response<Card> response) {
                        Toast.makeText(MainActivity.this, "Картку додано " + response.hashCode(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Card> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Помилка: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    public void scanCard() {
        Intent scanIntent = new Intent(this, CardIOActivity.class);

        // встановіть потрібні налаштування
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true);
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false);
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CARDHOLDER_NAME, true);

        // почати сканування
        startActivityForResult(scanIntent, SCAN_REQUEST_CODE);
    }

    void signOut(){
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    void signFacebook(){
        LoginManager.getInstance().logOut();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
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
}