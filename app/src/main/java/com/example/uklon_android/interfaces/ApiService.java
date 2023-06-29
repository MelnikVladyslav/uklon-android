package com.example.uklon_android.interfaces;

import com.example.uklon_android.classes.Transport;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("/api/Transports")
    Call<List<Transport>> getDataList();
}
