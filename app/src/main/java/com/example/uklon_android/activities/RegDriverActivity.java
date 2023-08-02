package com.example.uklon_android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uklon_android.DTOs.CardDTO;
import com.example.uklon_android.DTOs.UserDTO;
import com.example.uklon_android.R;
import com.example.uklon_android.classes.Card;
import com.example.uklon_android.classes.User;
import com.example.uklon_android.interfaces.ApiService;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegDriverActivity extends AppCompatActivity {

    User correctUser = new User();
    UserDTO sendUser = new UserDTO();
    CardDTO newCard = new CardDTO();
    public ApiService apiService;
    Button btnEnter;
    EditText firstNameEdT;
    EditText lastNameEdT;
    EditText emailEdT;
    EditText phoneNumEdT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_driver);

        correctUser = (User) getIntent().getSerializableExtra("user");
        apiService = apiService.retrofit.create(ApiService.class);
        btnEnter = findViewById(R.id.btnEnt);
        firstNameEdT = findViewById(R.id.firstName);
        lastNameEdT = findViewById(R.id.lastName);
        emailEdT = findViewById(R.id.email);
        phoneNumEdT = findViewById(R.id.phoneNumber);
        firstNameEdT.setText(correctUser.getFirstName());
        lastNameEdT.setText(correctUser.getLastName());
        emailEdT.setText(correctUser.getEmail());
        phoneNumEdT.setText(correctUser.getPhoneNumber());
        sendUser.setFirstName(correctUser.getFirstName());
        sendUser.setLastName(correctUser.getLastName());
        sendUser.setEmail(correctUser.getEmail());

        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apiService.registerDriver(sendUser)
                        .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        scanCard();
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(RegDriverActivity.this, "Помилка: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101) {
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

                // Николи не передавайте свій cardNumber напряму через ненадійні додатки.
                newCard.setNumber(scanResult.getRedactedCardNumber());

                newCard.setUserId(correctUser.getId());


                apiService.addCard(newCard).enqueue(new Callback<Card>() {
                    @Override
                    public void onResponse(Call<Card> call, Response<Card> response) {
                        Intent intent = new Intent(RegDriverActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(RegDriverActivity.this, "Картку додано " + response.hashCode(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Card> call, Throwable t) {
                        Toast.makeText(RegDriverActivity.this, "Помилка: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
        startActivityForResult(scanIntent, 101);
    }
}