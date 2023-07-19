package com.example.uklon_android.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uklon_android.DTOs.UserDTO;
import com.example.uklon_android.R;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public ApiService apiService;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    Button signOutBtn;
    User correctUser = new User();
    UserDTO sendUser = new UserDTO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiService = apiService.retrofit.create(ApiService.class);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        signOutBtn = findViewById(R.id.signOut);

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
                                TextView textView = findViewById(R.id.textView);
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append(correctUser.getFirstName() + " " + correctUser.getLastName()).append("\n").append(correctUser.getPhoneNumber());
                                textView.setText(stringBuilder.toString());
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
                                Log.d("json token", object.toString());
                                Log.d("first name", object.getString("first_name"));
                                sendUser.setFirstName(object.getString("first_name"));
                                sendUser.setLastName(object.getString("last_name"));
                                sendUser.setEmail(object.getString("email"));
                                //found in database
                                apiService.foundOrCreate(sendUser).
                                        enqueue(new Callback<User>()
                                        {
                                            @Override
                                            public void onResponse(Call<User> call, Response<User> response) {
                                                if (response.isSuccessful()) {
                                                    correctUser = response.body();
                                                    TextView textView = findViewById(R.id.textView);
                                                    StringBuilder stringBuilder = new StringBuilder();
                                                    stringBuilder.append(correctUser.getFirstName() + " " + correctUser.getLastName()).append("\n").append(correctUser.getPhoneNumber());
                                                    textView.setText(stringBuilder.toString());
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
            parameters.putString("fields", "first_name,last_name,email");
            request.setParameters(parameters);
            request.executeAsync();
        }

        signOutBtn.setOnClickListener(new View.OnClickListener() {
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
}