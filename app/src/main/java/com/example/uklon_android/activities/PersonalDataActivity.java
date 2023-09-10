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
    Button backBtn;
    Button uploadBtn;
    ImageButton avatarImg;
    EditText firstNameEdT;
    EditText lastNameEdT;
    EditText emailEdT;
    EditText phoneNumEdT;
    String urlAv;
    private static final int PICK_IMAGE_REQUEST = 1;

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

        avatarImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImageFromGallery(view);
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
                sendUser.setUrl(urlAv);
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
                                    if(correctUser.getUrl() != null) {
                                        Picasso.get().load(correctUser.getUrl()).into(avatarImg);
                                    }
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

    public void pickImageFromGallery(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private String getPathFromUri(Uri contentUri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();

            // Отримайте фактичний шлях до вибраної фотографії і використовуйте його
            String imagePath = getPathFromUri(selectedImageUri);
            // Тут ви можете робити що завгодно з вибраною фотографією, наприклад, відображати її у віджеті ImageView
            avatarImg.setImageURI(selectedImageUri);
            urlAv = imagePath;
        }
    }
}