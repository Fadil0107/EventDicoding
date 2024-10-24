package com.dicoding.eventdicoding.data.retrofit

import com.dicoding.eventdicoding.data.response.ResponseEvent
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    fun getEvents(
        @Query("active") active: Int = 1,
        @Query("limit") limit : Int = 40,
        @Query("q") query : String? = null
    ): Call<ResponseEvent>
}