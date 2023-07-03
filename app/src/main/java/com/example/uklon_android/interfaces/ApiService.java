package com.example.uklon_android.interfaces;

import com.example.uklon_android.classes.Transport;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface ApiService {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5289")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    @GET("/api/transports")
    Call<List<Transport>> getDataList();
}
