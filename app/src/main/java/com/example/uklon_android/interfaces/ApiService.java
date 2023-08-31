package com.example.uklon_android.interfaces;

import com.example.uklon_android.DTOs.CardDTO;
import com.example.uklon_android.DTOs.PhoneNumberVerificationDto;
import com.example.uklon_android.DTOs.UserDTO;
import com.example.uklon_android.classes.Card;
import com.example.uklon_android.classes.Order;
import com.example.uklon_android.classes.Transport;
import com.example.uklon_android.classes.Types;
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
            .baseUrl("https://uklon.itstep.click")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @GET("/api/transports")
    Call<List<Transport>> getDataList();

    @GET("/api/login/get-users")
    Call<List<User>> getUsers();

    @POST("/api/login/found-or-create-user")
    Call<User> foundOrCreate(@Body UserDTO user);

    @POST("/api/cards/add-card")
    Call<Card> addCard(@Body CardDTO card);

    @GET("/api/cards/get-cards")
    Call<List<Card>> getCard();

    @PUT("/api/login/update-user/{id}")
    Call<User> updateUser(@Body UserDTO user, @Path("id")String id);

    @PUT("/api/login/register-driver")
    Call<User> registerDriver(@Body UserDTO user);

    @POST("/api/order/create-order")
    Call<Order> createOrder(@Body Order order);

    @GET("/api/order/get-orders")
    Call<List<Order>> getOrders();

    @GET("/api/types/get-types")
    Call<List<Types>> getTypes();

    @POST("/api/verif-phone")
    Call<Boolean> verifPhone(@Body PhoneNumberVerificationDto phoneDTO);

    @POST("api/login/login-phone")
    Call<String> loginPhone(@Body PhoneNumberVerificationDto phoneDTO);
}
