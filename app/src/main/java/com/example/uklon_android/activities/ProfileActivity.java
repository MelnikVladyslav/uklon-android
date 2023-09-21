package com.example.uklon_android.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.uklon_android.DTOs.UserDTO;
import com.example.uklon_android.R;
import com.example.uklon_android.classes.User;
import com.example.uklon_android.interfaces.ApiService;
import com.example.uklon_android.interfaces.DeleteTask;
import com.example.uklon_android.interfaces.FileUploader;
import com.example.uklon_android.interfaces.UploadTask;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private static final int REQUEST_READ_EXTERNAL_STORAGE = 1;
    public ApiService apiService;
    User correctUser = new User();
    ImageButton backBtn;
    ImageButton avatarImg;
    TextView firstNameEdT;
    TextView emailEdT;
    LinearLayout llPerData, llExit, llDelete;
    String urlAv;
    GoogleSignInClient gsc;
    GoogleSignInOptions gso;
    Uri selectedImageUri;
    String imagePath;
    private static final int PICK_IMAGE_REQUEST = 1;

    UploadDTO uploadDTO = new UploadDTO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_main);

        // Перевірка і запрос дозволу на читання зберігається тут для Android 6.0 і вище
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Якщо дозвіл не надано, запросимо його в користувача
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            // Якщо дозвіл надано, ви можете продовжити виконувати свої дії
        }


        apiService = apiService.retrofit.create(ApiService.class);
        correctUser = (User) getIntent().getSerializableExtra("user");

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        backBtn = findViewById(R.id.back);
        avatarImg = findViewById(R.id.avatar);
        firstNameEdT = findViewById(R.id.firstName);
        firstNameEdT.setText(correctUser.getFirstName());
        emailEdT = findViewById(R.id.email);
        llPerData = findViewById(R.id.PersonalData);
        llExit = findViewById(R.id.signOut);
        llDelete = findViewById(R.id.delete);
        emailEdT.setText(correctUser.getEmail());

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        //Avatar
        if(getIntent().getSerializableExtra("uriImg") != null) {
            urlAv = (String) getIntent().getSerializableExtra("uriImg");
            Picasso.get().load(urlAv).into(avatarImg);
            avatarImg.setBackground(null);
        }

        backBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                intent.putExtra("user", correctUser);
                startActivity(intent);
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

        //sign out
        llExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gsc != null) {
                    signOut();
                }
                if (isLoggedIn) {
                    signFacebook();
                }
            }
        });

        llDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        //Avatar
        avatarImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImageFromGallery(view);
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Дозвіл на читання надано, ви можете продовжити виконувати свої дії
            } else {
                // Користувач відхилив запит на читання дозволу, обробіть це відповідно
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();

            // Отримайте фактичний шлях до вибраної фотографії і використовуйте його
            imagePath = getPathFromUri(selectedImageUri);


            // Отримайте файл з фактичним шляхом
            File imageFile = new File(imagePath);

            new UploadTask(imageFile, correctUser.getId()).execute();

            // Тут ви можете робити що завгодно з вибраною фотографією, наприклад, відображати її у віджеті ImageView
            avatarImg.setImageURI(selectedImageUri);
            urlAv = imagePath;
        }
    }

    void signOut(){
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    void signFacebook(){
        LoginManager.getInstance().logOut();
        startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
        finish();
    }
}