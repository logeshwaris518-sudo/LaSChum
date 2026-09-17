package com.example.littleacechatbot;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @POST("/chat")
    Call<ChatResponse> sendMessage(@Body ChatRequest request);

    @GET("/chat/history/{username}/{role}")
    Call<List<ChatMessage>> getChatHistory(@Path("username") String username, @Path("role") String role);

    @POST("/signup")
    Call<AuthResponse> signup(@Body AuthRequest request);

    @POST("/login")
    Call<AuthResponse> login(@Body AuthRequest request);
}