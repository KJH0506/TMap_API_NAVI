package com.example.tmap_api_navi

import com.example.tmap_api_navi.retrofit.TMAPtraffic
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

//interface TMAPService {
//    @POST("transit/routes/sub")
//    suspend fun getTMAPTraffic(
//        @Body("startX") startX: String,
//        @Query("startY") startY: String,
//        @Query("endX") endX: String,
//        @Query("endY") endY: String,
//        @Query("format") format: String,
//        @Query("count") count: Int,
//        @Header("appKey") appKey: String
//    ): TMAPtraffic
//}

interface TMAPService {
    @Headers(
        "accept: application/json",
        "content-type: application/json",
        "appKey: e8wHh2tya84M88aReEpXCa5XTQf3xgo01aZG39k5" // 여기에 본인의 앱 키를 입력하세요
    )
    @POST("transit/routes/sub")
//    suspend fun getTMAPTraffic(@Body request: TMAPRequest): TMAPtraffic
    fun getTMAPTraffic(@Body request: TMAPRequest): Call<TMAPtraffic>
}

data class TMAPRequest(
    val startX: String,
    val startY: String,
    val endX: String,
    val endY: String,
    val format: String,
    val count: Int
)