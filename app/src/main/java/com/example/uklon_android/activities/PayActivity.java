package com.example.uklon_android.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uklon_android.R;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

public class PayActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final String PAYPAL_CLIENT_ID = "ASmywcHfa6i5ev-wG8Qhiqb4SjgHbQBeOdJVMP5apfPCC9TPgpBas9vE62fOH9x15WJefBGpN1Cn1TXL";

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX) // Замініть на ENVIRONMENT_PRODUCTION для реальних транзакцій
            .clientId(PAYPAL_CLIENT_ID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        // Виконайте оплату після успішної авторизації користувача на PayPal
        makePayment();
    }

    private void makePayment() {
        // Загальна сума платежу
        BigDecimal amount = new BigDecimal("10.00");

        PayPalPayment payment = new PayPalPayment(amount, "USD", "Payment for your service",
                PayPalPayment.PAYMENT_INTENT_ORDER);

        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    try {
                        // Отримання ID платежу
                        String paymentId = confirmation.toJSONObject().getJSONObject("response").getString("id");
                        // Виконайте необхідні дії для успішної оплати
                        Toast.makeText(this, "Payment successful. ID: " + paymentId, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Payment cancelled", Toast.LENGTH_SHORT).show();
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Toast.makeText(this, "Invalid payment or configuration", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }
}