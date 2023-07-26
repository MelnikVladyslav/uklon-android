package com.example.uklon_android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uklon_android.DTOs.UserDTO;
import com.example.uklon_android.R;
import com.example.uklon_android.classes.User;
import com.example.uklon_android.interfaces.ApiService;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    public ApiService apiService;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    User correctUser = new User();
    UserDTO sendUser = new UserDTO();
    Button backBtn;
    Button uploadBtn;
    ImageView avatarImg;
    EditText firstNameEdT;
    EditText lastNameEdT;
    EditText emailEdT;
    EditText phoneNumEdT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        apiService = apiService.retrofit.create(ApiService.class);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

        backBtn = findViewById(R.id.back);
        uploadBtn = findViewById(R.id.update);
        avatarImg = findViewById(R.id.avatar);
        firstNameEdT = findViewById(R.id.firstName);
        lastNameEdT = findViewById(R.id.lastName);
        emailEdT = findViewById(R.id.email);
        phoneNumEdT = findViewById(R.id.phoneNumber);

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
                                Picasso.get().load(acct.getPhotoUrl()).into(avatarImg);
                                firstNameEdT.setText(correctUser.getFirstName());
                                lastNameEdT.setText(correctUser.getLastName());
                                emailEdT.setText(correctUser.getEmail());
                                phoneNumEdT.setText(correctUser.getPhoneNumber());
                            }
                            else{
                                Toast.makeText(ProfileActivity.this, "Помилка: " + response.message(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Toast.makeText(ProfileActivity.this, "Помилка: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                                                    Picasso.get().load(imageUrl).into(avatarImg);
                                                    firstNameEdT.setText(correctUser.getFirstName());
                                                    lastNameEdT.setText(correctUser.getLastName());
                                                    emailEdT.setText(correctUser.getEmail());
                                                    phoneNumEdT.setText(correctUser.getPhoneNumber());
                                                }
                                                else{
                                                    Toast.makeText(ProfileActivity.this, "Помилка: " + response.message(), Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<User> call, Throwable t) {
                                                Toast.makeText(ProfileActivity.this, "Помилка: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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

        backBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                finish();
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                sendUser.setFirstName(firstNameEdT.getText().toString());
                sendUser.setLastName(lastNameEdT.getText().toString());
                sendUser.setEmail(emailEdT.getText().toString());
                sendUser.setPhoneNumber(phoneNumEdT.getText().toString());
                apiService.updateUser(sendUser, correctUser.getId()).
                        enqueue(new Callback<User>()
                        {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if (response.isSuccessful()) {
                                    correctUser = response.body();
                                    firstNameEdT.setText(correctUser.getFirstName());
                                    lastNameEdT.setText(correctUser.getLastName());
                                    emailEdT.setText(correctUser.getEmail());
                                    phoneNumEdT.setText(correctUser.getPhoneNumber());
                                }
                                else{
                                    Toast.makeText(ProfileActivity.this, "Помилка: " + response.message(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                Toast.makeText(ProfileActivity.this, "Помилка: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}