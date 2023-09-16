package com.example.uklon_android.interfaces;

import com.example.uklon_android.DTOs.CardDTO;
import com.example.uklon_android.DTOs.OrderDTO;
import com.example.uklon_android.DTOs.PhoneNumberVerificationDto;
import com.example.uklon_android.DTOs.UserDTO;
import com.example.uklon_android.classes.Card;
import com.example.uklon_android.classes.Order;
import com.example.uklon_android.classes.Transport;
import com.example.uklon_android.classes.Types;
import com.example.uklon_android.classes.User;
import com.google.maps.internal.ApiResponse;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://uklon.itstep.click")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

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
    Call<OrderDTO> createOrder(@Body OrderDTO order);

    @GET("/api/order/get-orders")
    Call<List<Order>> getOrders();

    @GET("/api/types/get-types")
    Call<List<Types>> getTypes();

    @POST("/api/verif-phone")
    Call<Integer> verifPhone(@Body PhoneNumberVerificationDto phoneDTO);

    @POST("/api/verif-email")
    Call<Integer> verifEmail(@Body PhoneNumberVerificationDto phoneDTO);

    @POST("/api/login/login-phone")
    Call<String> loginPhone(@Body PhoneNumberVerificationDto phoneDTO);

    @POST("/api/login/login-email")
    Call<String> loginEmail(@Body PhoneNumberVerificationDto phoneDTO);

    @DELETE("/api/deleteuser")
    Call deleteUser(String userId);

    @Multipart
    @POST("/api/login/upload-photo")
    Call<ResponseBody> uploadPhoto(@Part MultipartBody.Part imageFile, @Part("userId") RequestBody userId);
}
