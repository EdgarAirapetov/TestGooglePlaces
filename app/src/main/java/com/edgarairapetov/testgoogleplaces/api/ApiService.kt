package com.edgarairapetov.testgoogleplaces.api

import com.edgarairapetov.testgoogleplaces.api.response.PlacesResponse
import com.edgarairapetov.testgoogleplaces.api.response.RouteResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {

    @GET("place/nearbysearch/json")
    suspend fun getPlaces(
        @Query("type") type: String,
        @Query("location") location: String,
        @Query("radius") radius: Int

    ): Response<PlacesResponse>

    @GET("directions/json")
    suspend fun getRoute(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("mode") mode: String
    ): Response<RouteResponse>

}