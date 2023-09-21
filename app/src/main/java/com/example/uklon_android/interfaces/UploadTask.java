package com.example.uklon_android.interfaces;

import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class UploadTask extends AsyncTask<Void, Void, String> {

    private File imageFile;
    private String userId;

    public UploadTask(File imageFile, String userId) {
        this.imageFile = imageFile;
        this.userId = userId;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            FileUploader.uploadFile(imageFile, userId); // Викликайте вашу логіку завантаження файлу тут
            return "Success"; // Поверніть успішний результат
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Error: ", e.getMessage());
            return "Error: " + e.getMessage(); // Поверніть помилку у вигляді рядка
        }
    }

    @Override
    protected void onPostExecute(String result) {
        // Оновлюйте інтерфейс користувача на основі результату запиту
        if ("Success".equals(result)) {

        } else {
            // Виникла помилка
        }
    }
}
