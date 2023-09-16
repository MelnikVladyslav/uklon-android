package com.example.uklon_android.interfaces;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class FileUploader {
    private static final String BASE_URL = "https://uklon.itstep.click/"; // Замініть це на базовий URL вашого API

    public static void uploadFile(@NonNull File imageFile, String userId) throws IOException {
        OkHttpClient client = new OkHttpClient();

        // Підготуйте запит для відправлення файлу на сервер
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("imageFile", imageFile.getName(), RequestBody.create(MediaType.parse("image/*"), imageFile))
                .addFormDataPart("userId", userId)
                .build();

        Log.d("userId fu: ", userId);
        Log.d("image fu: ", imageFile.getName());

        Request request = new Request.Builder()
                .url(BASE_URL + "api/login/upload-photo/") // Шлях для завантаження фото на сервер
                .post(requestBody)
                .build();

        // Відправте запит на сервер
        Response response = client.newCall(request).execute();

        if (response.isSuccessful()) {
            // Обробте успішну відповідь тут
            String responseBody = response.body().string();
            // Ваш код для обробки успішної відповіді
            Log.d("Responce", responseBody);
        } else {
            // Обробте помилку тут
            String errorBody = response.body().string();
            // Ваш код для обробки помилки
            Log.d("Responce", errorBody);
        }
    }
}
