package com.fahruaz.farmernusantara.api

import com.fahruaz.farmernusantara.response.auth.RegisterMessageResponse
import com.fahruaz.farmernusantara.response.auth.SendTokenResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

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
    fun sendToken(
        @Field(value = "email") email: String
    ): Call<SendTokenResponse>

}