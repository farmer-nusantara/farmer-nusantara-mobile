package com.fahruaz.farmernusantara.api

import com.fahruaz.farmernusantara.response.auth.*
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService {
    @FormUrlEncoded
    @POST("auth/signup")
    fun registerUser(
        @Field(value = "email") email: String,
        @Field(value = "name") name: String,
        @Field(value = "phone") phone: String,
        @Field(value = "password") passwordResponse: String,
        @Field(value = "passwordConfirmation") passwordConfirmation: String,
    ): Call<RegisterMessageResponse>

    @FormUrlEncoded
    @POST("auth/email-token-activation")
    fun sendTokenActivationAccount(
        @Field(value = "email") email: String
    ): Call<SendTokenActivationMessageResponse>

    @FormUrlEncoded
    @PUT("auth/status-account")
    fun changeStatusAccount(
        @Field(value = "secretCode") secretCode: Int
    ): Call<ChangeStatusAccountMessageResponse>

    @FormUrlEncoded
    @POST("auth/signin")
    fun loginUser(
        @Field(value = "email") email: String,
        @Field(value = "password") password: String
    ): Call<SignInMessageResponse>

    @FormUrlEncoded
    @POST("auth/email-token-reset")
    fun sendTokenResetPassword(
        @Field(value = "email") email: String
    ): Call<TokenResetPasswordResponse>

}