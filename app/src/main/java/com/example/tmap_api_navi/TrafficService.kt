package com.example.tmap_api_navi

import com.example.tmap_api_navi.retrofit.TMAPtraffic
import com.example.tmap_api_navi.retrofit.TmapAllTraffic
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface TrafficService {
    @Headers(
        "accept: application/json",
        "content-type: application/json",
        "appKey: e8wHh2tya84M88aReEpXCa5XTQf3xgo01aZG39k5" // 여기에 본인의 앱 키를 입력하세요
    )
    @POST("transit/routes")
//    suspend fun getTMAPTraffic(@Body request: TMAPRequest): TMAPtraffic
    fun getAllTraffic(@Body request: TrafficRequest): Call<TmapAllTraffic>
}

data class TrafficRequest(
    val startX: String,
    val startY: String,
    val endX: String,
    val endY: String,
    val lang: Int,
    val format: String,
    val count: Int
)