package com.demo.upstox.data.remote

import com.demo.upstox.data.remote.model.response.HoldingsResponse
import retrofit2.http.GET

interface AppNetworkService {

    @GET("/")
    suspend fun getHoldings(): HoldingsResponse
}