package com.example.mostridatasca.network

import com.example.mostridatasca.model.ActiveObjectResponse
import com.example.mostridatasca.model.NearbyObject
import com.example.mostridatasca.model.Session
import com.example.mostridatasca.model.User
import com.example.mostridatasca.model.UserRank
import com.example.mostridatasca.model.VirtualObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

object MonstersApi {
    private const val BASE_URL = "https://develop.ewlab.di.unimi.it/mc/mostri/"
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    val retrofitService: MonstersApiService by lazy {
        retrofit.create(MonstersApiService::class.java)
    }
}

interface MonstersApiService {
    @POST("users")
    suspend fun getSession(): Session

    @GET("users/{id}")
    suspend fun getUser(
        @Path("id") id: Int,
        @Query("sid") sid: String
    ): User

    @GET("objects")
    suspend fun getNearbyObjects(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("sid") sid: String
    ): List<NearbyObject>

    @GET("objects/{id}")
    suspend fun getObject(
        @Path("id") id: Int,
        @Query("sid") sid: String
    ): VirtualObject

    @PATCH("users/{id}")
    @FormUrlEncoded
    suspend fun updateUser(
        @Path("id") id: Int,
        @Field("sid") sid: String,
        @Field("name") name: String,
        @Field("picture") picture: String?,
        @Field("positionshare") positionshare: Boolean
    )

    @GET("ranking")
    suspend fun getRankingList(
        @Query("sid") sid: String
    ): List<UserRank>

    @POST("objects/{id}/activate")
    @FormUrlEncoded
    suspend fun activateObject(
        @Path("id") id: Int,
        @Field("sid") sid: String
    ): ActiveObjectResponse
}


/*
    @POST("objects/{ID}/activate")
    @FormUrlEncoded
    Call<ActiveObjectResponse> activateObject(
            @Path("ID") int ID,
            @Field("sid") String sid
    );

    @GET("users/")
    Call<List<UsersResponse>> users(
            @Query("sid") String sid,
            @Query("lat") double lat,
            @Query("lon") double lon
    );
    */