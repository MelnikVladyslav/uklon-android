package com.example.uklon_android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uklon_android.DTOs.CardDTO;
import com.example.uklon_android.R;
import com.example.uklon_android.classes.Card;
import com.example.uklon_android.classes.User;
import com.example.uklon_android.interfaces.ApiService;

import java.util.List;
import java.util.Objects;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayTActivity extends AppCompatActivity {

    RadioButton DC, C;
    LinearLayout SC;
    boolean isDC = false;
    boolean isC = false;
    CardDTO newCard = new CardDTO();
    User correctUser = new User();
    ApiService apiService;
    TextView tvListCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_method);
        apiService = apiService.retrofit.create(ApiService.class);
        correctUser = (User) getIntent().getSerializableExtra("user");

        ImageButton back = findViewById(R.id.back);
        Button done = findViewById(R.id.Send);
        DC = findViewById(R.id.DC);
        C = findViewById(R.id.C);
        SC = findViewById(R.id.addCard);
        tvListCards = findViewById(R.id.listCards);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PayTActivity.this, OrderTaxiActivity.class);
                intent.putExtra("price", (float) getIntent().getSerializableExtra("price"));
                intent.putExtra("user", (User) getIntent().getSerializableExtra("user"));
                startActivity(intent);
            }
        });

        apiService.getCard().enqueue(new Callback<List<Card>>() {
            @Override
            public void onResponse(Call<List<Card>> call, Response<List<Card>> response) {
                StringBuilder strBuild = new StringBuilder();
                for (Card curCard: response.body())
                {
                    Log.d("user id: ", correctUser.getId());
                    if(Objects.equals(curCard.getUser(), correctUser.getId()))
                    {
                        strBuild.append(curCard.getNumber()).append("\n");
                    }
                }
                tvListCards.setText(strBuild);
            }

            @Override
            public void onFailure(Call<List<Card>> call, Throwable t) {

            }
        });

        RadioGroup radioGroup = findViewById(R.id.rd);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == R.id.DC) {
                        isC = false;
                        isDC = true;
                        C.setChecked(false);
                        DC.setChecked(true);
                    }
                    if (checkedId == R.id.C) {
                        isC = true;
                        isDC = false;
                        DC.setChecked(false);
                        C.setChecked(true);
                    }
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PayTActivity.this, OrderTaxiActivity.class);
                intent.putExtra("price", (float) getIntent().getSerializableExtra("price"));
                intent.putExtra("user", (User) getIntent().getSerializableExtra("user"));
                startActivity(intent);
            }
        });

        SC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent scanIntent = new Intent(PayTActivity.this, CardIOActivity.class);

                    // встановіть потрібні налаштування
                    scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true);
                    scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false);
                    scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CARDHOLDER_NAME, true);

                    // почати сканування
                    startActivityForResult(scanIntent, 101);
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
                        Toast.makeText(PayTActivity.this, "Картку додано " + response.hashCode(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Card> call, Throwable t) {
                        Toast.makeText(PayTActivity.this, "Помилка: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                apiService.getCard().enqueue(new Callback<List<Card>>() {
                    @Override
                    public void onResponse(Call<List<Card>> call, Response<List<Card>> response) {
                        StringBuilder strBuild = new StringBuilder();
                        for (Card curCard: response.body())
                        {
                            if(Objects.equals(curCard.getUser(), correctUser.getId()))
                            {
                                strBuild.append(curCard.getNumber()).append("\n");
                            }
                        }
                        tvListCards.setText(strBuild);
                    }

                    @Override
                    public void onFailure(Call<List<Card>> call, Throwable t) {

                    }
                });
            }
        }
    }
}