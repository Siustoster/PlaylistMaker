package com.example.playlistmaker

import com.example.playlistmaker.model.api.TrackApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesInterfaceApi {

    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<TrackApiResponse>
}