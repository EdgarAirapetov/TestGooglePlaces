package com.edgarairapetov.testgoogleplaces.data.api

import com.edgarairapetov.testgoogleplaces.data.api.wrapper.place.PlacesResponse
import com.edgarairapetov.testgoogleplaces.data.api.wrapper.direction.DirectionResponse
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
    ): Response<DirectionResponse>

}