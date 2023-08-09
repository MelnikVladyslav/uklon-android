package com.example.uklon_android.interfaces;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GeoNamesApiService {
    @GET("searchJSON")
    Call<GeoNamesResponse> getCities(
            @Query("country") String country, // Код країни (наприклад, "UA" для України)
            @Query("maxRows") int maxRows, // Максимальна кількість результатів
            @Query("username") String username // Ваш GeoNames користувач
    );
}
