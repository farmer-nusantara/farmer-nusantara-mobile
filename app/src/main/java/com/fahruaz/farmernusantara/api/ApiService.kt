package com.fahruaz.farmernusantara.api

import com.fahruaz.farmernusantara.response.auth.*
import com.fahruaz.farmernusantara.response.farmland.CreateFarmlandResponse
import com.fahruaz.farmernusantara.response.file.UploadImageToStorageResponse
import com.fahruaz.farmernusantara.response.profile.GetProfileResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

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

    @FormUrlEncoded
    @POST("auth/check-token-reset")
    fun checkingTokenResetPassword(
        @Field(value = "secretCode") secretCode: Int
    ): Call<CheckTokenResetMessageResponse>

    @FormUrlEncoded
    @PUT("auth/change-password")
    fun changePasswordAccount(
        @Field(value = "email") email: String,
        @Field(value = "newPassword") newPassword: String,
        @Field(value = "passwordConfirmation") passwordConfirmation: String
    ): Call<ChangePasswordMessageResponse>

    @GET("auth/user/{id}")
    fun getUserData(
        @Header("Authorization") authHeader: String,
        @Path("id") id: String
    ): Call<GetProfileResponse>

    @FormUrlEncoded
    @POST("farmland")
    fun createFarmland(
        @Header("Authorization") token: String,
        @Field(value = "farmName") farmName: String,
        @Field(value = "owner") owner: String,
        @Field(value = "markColor") markColor: String,
        @Field(value = "plantType") plantType: String,
        @Field(value = "location") location: String,
        @Field(value = "imageUrl") imageUrl: String
    ): Call<CreateFarmlandResponse>

    @Multipart
    @POST("file/uploads/{owner}")
    fun uploadImageToStorage(
        @Header("Authorization") token: String,
        @Path("owner") owner: String,
        @Part file: MultipartBody.Part,
    ): Call<UploadImageToStorageResponse>

    @Multipart
    @POST("file/uploads/{owner}")
    suspend fun uploadImageToStorage2(
        @Header("Authorization") token: String,
        @Path("owner") owner: String,
        @Part file: MultipartBody.Part,
    ): UploadImageToStorageResponse

    @FormUrlEncoded
    @PUT("auth/user/{id}")
    fun editProfile(
        @Header("Authorization") authHeader: String,
        @Path("id") id: String,
        @Field(value = "email") email: String,
        @Field(value = "name") name: String,
        @Field(value = "phone") phone: String
    ): Call<GetProfileResponse>

}