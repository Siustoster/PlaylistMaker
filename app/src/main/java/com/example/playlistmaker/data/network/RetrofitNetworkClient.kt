package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.TrackApiRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(private val itunesService: ItunesInterfaceApi) : NetworkClient {


    override suspend fun doRequest(dto: Any): Response {
        if (dto is TrackApiRequest) {
            return withContext(Dispatchers.IO) {
                try {
                    val resp = itunesService.search(dto.term)
                    resp.apply {
                        resultCode = 200
                    }
                } catch (e: Throwable) {
                    Response().apply { resultCode = 500 }
                }
            }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }
}