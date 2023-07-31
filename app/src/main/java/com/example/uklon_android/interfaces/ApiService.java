package com.example.uklon_android.interfaces;

import com.example.uklon_android.DTOs.CardDTO;
import com.example.uklon_android.DTOs.UserDTO;
import com.example.uklon_android.classes.Card;
import com.example.uklon_android.classes.Transport;
import com.example.uklon_android.classes.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5289")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @GET("/api/transports")
    Call<List<Transport>> getDataList();

    @POST("/api/login/found-or-create-user")
    Call<User> foundOrCreate(@Body UserDTO user);

    @POST("/api/cards/add-card")
    Call<Card> addCard(@Body CardDTO card);

    @PUT("/api/login/update-user/{id}")
    Call<User> updateUser(@Body UserDTO user, @Path("id")String id);

    @PUT("/api/login/register-driver")
    Call<User> registerDriver(@Body UserDTO user);
}
