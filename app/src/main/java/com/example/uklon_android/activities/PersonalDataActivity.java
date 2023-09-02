package com.example.uklon_android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.uklon_android.DTOs.UserDTO;
import com.example.uklon_android.R;
import com.example.uklon_android.classes.User;
import com.example.uklon_android.interfaces.ApiService;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalDataActivity extends AppCompatActivity {

    public ApiService apiService;
    User correctUser = new User();
    UserDTO sendUser = new UserDTO();
    Button backBtn;
    Button uploadBtn;
    ImageView avatarImg;
    EditText firstNameEdT;
    EditText lastNameEdT;
    EditText emailEdT;
    EditText phoneNumEdT;
    String urlAv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);

        apiService = apiService.retrofit.create(ApiService.class);
        correctUser = (User) getIntent().getSerializableExtra("user");

        backBtn = findViewById(R.id.back);
        uploadBtn = findViewById(R.id.update);
        avatarImg = findViewById(R.id.avatar);
        firstNameEdT = findViewById(R.id.firstName);
        firstNameEdT.setText(correctUser.getFirstName());
        emailEdT = findViewById(R.id.email);
        emailEdT.setText(correctUser.getEmail());
        lastNameEdT = findViewById(R.id.lastName);
        phoneNumEdT = findViewById(R.id.phoneNumber);
        lastNameEdT.setText(correctUser.getLastName());
        phoneNumEdT.setText(correctUser.getPhoneNumber());

        //Avatar
        if(getIntent().getSerializableExtra("uriImg") != null) {
            urlAv = (String) getIntent().getSerializableExtra("uriImg");
            Picasso.get().load(urlAv).into(avatarImg);
        }

        backBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonalDataActivity.this, ProfileActivity.class);
                intent.putExtra("user", correctUser);
                if(urlAv != null) {
                    intent.putExtra("uriImg", urlAv.toString());
                }
                startActivity(intent);
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
                                    Toast.makeText(PersonalDataActivity.this, "Помилка: " + response.message(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                Toast.makeText(PersonalDataActivity.this, "Помилка: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}