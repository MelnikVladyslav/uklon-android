package com.example.uklon_android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uklon_android.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class LoginActivity extends AppCompatActivity {
    private EditText phoneNumberEditText;
    private Button loginPhoneButton;
    private Button loginGoogleButton;
    private Button loginFacebookButton;

    private CallbackManager callbackManager;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;


    private boolean isValidPhoneNumber(String phoneNumber) {
        Pattern pattern = Pattern.compile("^\\[0-9]{10,13}$");
        Matcher matcher = pattern.matcher(phoneNumber);
        boolean isValid = matcher.matches();

        if (isValid) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        phoneNumberEditText = findViewById(R.id.phoneNumberTextView);
        loginPhoneButton = findViewById(R.id.next);
        loginGoogleButton = findViewById(R.id.Google_button);
        loginFacebookButton = findViewById(R.id.Facebook_button);

        callbackManager = CallbackManager.Factory.create();
        FacebookSdk.setClientToken("6c72d4e2d174db3d1e5eb38069c09b0c");
        FacebookSdk.sdkInitialize(getApplicationContext());

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        if(isLoggedIn){
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();

        }

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!=null){
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }

        loginPhoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = "+380" + phoneNumberEditText.getText().toString();

                //Створювати юзера і додати туди номер

                if (isValidPhoneNumber(phoneNumber)) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Невірний номер телефону", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loginGoogleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        loginFacebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Викликати метод для входу через Facebook
                loginWithFacebook();
            }
        });
    }

    void signIn(){
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent,1000);
    }

    private void loginWithFacebook() {
        // Створити об'єкт LoginManager для управління входом через Facebook
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email"));

        // Налаштувати зворотній виклик для отримання результату входу
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancel() {
                // Скасований вхід через Facebook
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "Помилка: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                task.getResult(ApiException.class);
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            } catch (ApiException e) {
                Toast.makeText(LoginActivity.this, "Помилка відправки", Toast.LENGTH_SHORT).show();
            }
        }

    }

}