package com.uezo.uezo_av2;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserAPIService {
    @GET("users")
    Call<JsonArray> getUsers();

    @GET("users/{username}")
    Call<JsonObject> getUserByUsername(@Path("username") String username);

    @POST("users")
    Call<Void> addUser(@Body JsonObject requestBody);
}
