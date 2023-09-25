package com.example.uklon_android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.uklon_android.DTOs.UserDTO;
import com.example.uklon_android.R;
import com.example.uklon_android.classes.User;
import com.example.uklon_android.interfaces.ApiService;
import com.google.android.gms.cast.framework.media.ImagePicker;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalDataActivity extends AppCompatActivity {

    public ApiService apiService;
    User correctUser = new User();
    UserDTO sendUser = new UserDTO();
    ImageButton backBtn;
    LinearLayout uploadBtn;
    EditText firstNameEdT;
    EditText emailEdT;
    EditText phoneNumEdT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_data);

        apiService = apiService.retrofit.create(ApiService.class);
        correctUser = (User) getIntent().getSerializableExtra("user");

        backBtn = findViewById(R.id.back);
        uploadBtn = findViewById(R.id.update);
        firstNameEdT = findViewById(R.id.firstName);
        firstNameEdT.setText(correctUser.getFirstName());
        emailEdT = findViewById(R.id.email);
        emailEdT.setText(correctUser.getEmail());
        phoneNumEdT = findViewById(R.id.phoneNumber);
        phoneNumEdT.setText(correctUser.getPhoneNumber());

        backBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonalDataActivity.this, ProfileActivity.class);
                intent.putExtra("user", correctUser);
                startActivity(intent);
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                sendUser.setFirstName(firstNameEdT.getText().toString());
                sendUser.setLastName(correctUser.getLastName());
                sendUser.setEmail(emailEdT.getText().toString());
                sendUser.setPhoneNumber(phoneNumEdT.getText().toString());
                sendUser.setUrl(correctUser.getUrl());
                apiService.updateUser(sendUser, correctUser.getId()).
                        enqueue(new Callback<User>()
                        {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if (response.isSuccessful()) {
                                    correctUser = response.body();
                                    firstNameEdT.setText(correctUser.getFirstName());
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