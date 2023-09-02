package com.example.uklon_android.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uklon_android.DTOs.UserDTO;
import com.example.uklon_android.R;
import com.example.uklon_android.classes.User;
import com.example.uklon_android.interfaces.ApiService;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    public ApiService apiService;
    User correctUser = new User();
    ImageButton backBtn;
    ImageView avatarImg;
    TextView firstNameEdT;
    TextView emailEdT;
    LinearLayout llPerData;
    String urlAv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_main);

        apiService = apiService.retrofit.create(ApiService.class);
        correctUser = (User) getIntent().getSerializableExtra("user");

        backBtn = findViewById(R.id.back);
        avatarImg = findViewById(R.id.avatar);
        firstNameEdT = findViewById(R.id.firstName);
        firstNameEdT.setText(correctUser.getFirstName());
        emailEdT = findViewById(R.id.email);
        llPerData = findViewById(R.id.PersonalData);
        emailEdT.setText(correctUser.getEmail());


        //Avatar
        if(getIntent().getSerializableExtra("uriImg") != null) {
            urlAv = (String) getIntent().getSerializableExtra("uriImg");
            Picasso.get().load(urlAv).into(avatarImg);
        }

        backBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                finish();
            }
        });

        llPerData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, PersonalDataActivity.class);
                intent.putExtra("user", correctUser);
                if(urlAv != null) {
                    intent.putExtra("uriImg", urlAv.toString());
                }
                startActivity(intent);
            }
        });

    }
}