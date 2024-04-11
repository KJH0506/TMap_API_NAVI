package com.example.tmap_api_navi

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tmap_api_navi.retrofit.TMAPtraffic
import com.example.tmap_api_navi.retrofit.TmapAllTraffic
import com.skt.tmap.TMapData
import com.skt.tmap.TMapData.OnFindPathDataWithTypeListener
import com.skt.tmap.TMapPoint
import com.skt.tmap.TMapView
import com.skt.tmap.overlay.TMapMarkerItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    private lateinit var tMapView: TMapView

    // 됨
//    val startPoint = TMapPoint(37.472678, 126.920928)
//    val endPoint = TMapPoint(37.405619, 127.091903)
    lateinit var startPoint:TMapPoint
    lateinit var endPoint:TMapPoint

    private lateinit var startInput:EditText
    private lateinit var endInput:EditText

    private val tmapdata = TMapData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        startInput = findViewById<EditText>(R.id.startInput)
        endInput = findViewById<EditText>(R.id.endInput)

        //////////
//        val retrofit = Retrofit.Builder()
//            .baseUrl("https://apis.openapi.sk.com/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        // Retrofit을 사용하여 서비스 인터페이스 생성
//        val service = retrofit.create(TMAPService::class.java)
//
//        // 서버로부터 TMAP API 응답 받아오기  e8wHh2tya84M88aReEpXCa5XTQf3xgo01aZG39k5
//        GlobalScope.launch(Dispatchers.Main) {
//            try {
//                val request = TMAPRequest(
//                    startX = "126.926493082645",
//                    startY = "37.6134436427887",
//                    endX = "127.126936754911",
//                    endY = "37.5004198786564",
//                    format = "json",
//                    count = 10
//                )
//
//                val response = service.getTMAPTraffic(request)
//
//                // 응답 처리
//                handleResponse(response)
//            } catch (e: Exception) {
//                // 에러 발생 시 처리
//                Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
//            }
//        }
//        /////

        loadTrafficInfo(this)

        var response:TMAPtraffic




        settingAPI()
    }



    fun loadTrafficInfo(mCallback:MainActivity){


        //  요약 Traffic API
        val request = TMAPRequest(
            startX = "126.926493082645",
            startY = "37.6134436427887",
            endX = "127.126936754911",
            endY = "37.5004198786564",
            format = "json",
            count = 10
        )

        val call = TmapCilient.service.getTMAPTraffic(request)

        call.enqueue(object : Callback<TMAPtraffic> {
            override fun onResponse(call: Call<TMAPtraffic>, response: Response<TMAPtraffic>) {
                if(response.isSuccessful()){
                    mCallback.handleResponse(response.body()!!)
                }
            }

            override fun onFailure(call: Call<TMAPtraffic>, t: Throwable) {

            }

        })



        //  전체 Traffic API
        val trafficrequest = TrafficRequest(
            startX = "126.926493082645",
            startY = "37.6134436427887",
            endX = "127.126936754911",
            endY = "37.5004198786564",
            lang = 0,
            format = "json",
            count = 10
        )

        val callAll = TrafficClient.service.getAllTraffic(trafficrequest)

        callAll.enqueue(object : Callback<TmapAllTraffic>{
            override fun onResponse(call: Call<TmapAllTraffic>, response: Response<TmapAllTraffic>) {
                if(response.isSuccessful()){
                    Log.d("걸음수", response.body()?.metaData?.plan?.itineraries?.get(0)?.legs.toString())
//                    mCallback.handleResponse(response.body()!!)
                }
            }

            override fun onFailure(call: Call<TmapAllTraffic>, t: Throwable) {

            }

        })


    }



    // 요약정보 Handler
    private fun handleResponse(response: TMAPtraffic) {
        // 응답 데이터 사용 예시
        val itineraries = response.metaData.plan.itineraries
        if (itineraries.isNotEmpty()) {
            val itinerary = itineraries[0]
            val totalDistance = itinerary.totalDistance
            val totalTime = itinerary.totalTime

            // 예시: 응답 데이터를 사용하여 UI 업데이트
            // textViewTotalDistance.text = "Total Distance: $totalDistance"
            // textViewTotalTime.text = "Total Time: $totalTime"
        } else {
            Toast.makeText(this, "No itineraries found", Toast.LENGTH_SHORT).show()
        }
    }
    ////


    //37.652233,126.800899 37.642526,126.836339
    fun searchRoute(v:View){
        tMapView.removeAllTMapPolyLine()
        tMapView.removeAllTMapMarkerItem()

        try {
            val s = startInput.text.toString().split(",")
            val e = endInput.text.toString().split(",")

            startPoint = TMapPoint(s[0].toDouble(), s[1].toDouble())
            endPoint = TMapPoint(e[0].toDouble(), e[1].toDouble())

            startInput.setText("")
            endInput.setText("")

            val tMapContainer = findViewById<LinearLayout>(R.id.linerarLayoutTmap)

            drawRoute(startPoint, endPoint, tMapContainer)
        }
        catch(e:Exception){
            //
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun settingAPI(){
        startPoint = TMapPoint(37.472678, 126.920928)
        endPoint = TMapPoint(37.405619, 127.091903)

        tMapView = TMapView(this)
        tMapView.setSKTMapApiKey("skiGDsaPMI4lDE0AFUVIIa9cOhymri9g8t7qGxMZ")

        val tMapContainer = findViewById<LinearLayout>(R.id.linerarLayoutTmap)

        //앱로딩 완료후
        firstMap(startPoint, endPoint, tMapContainer)

    }


    private fun firstMap(startPoint: TMapPoint, endPoint: TMapPoint, tMapContainer: LinearLayout) {
        tMapView.setOnMapReadyListener {

            // 경로 띄우는 부분
            drawRoute(startPoint, endPoint, tMapContainer)
        }
    }

    private fun drawRoute(
        startPoint: TMapPoint,
        endPoint: TMapPoint,
        tMapContainer: LinearLayout
    ) {
        tmapdata.findPathDataWithType(
            TMapData.TMapPathType.PEDESTRIAN_PATH,
            startPoint,
            endPoint,
            null,
            0,
            OnFindPathDataWithTypeListener() {
                tMapView.addTMapPolyLine(it)
            }
        )

        // 시작점 마커
        val start_marker = TMapMarkerItem()
        start_marker.id = "Start"
        start_marker.setTMapPoint(startPoint.latitude, startPoint.longitude)
        start_marker.icon = BitmapFactory.decodeResource(resources, com.skt.tmap.R.drawable.poi)
        // 필요 없긴 함
        start_marker.setPosition(0.5F, 0.5F) // 마커의 중심점을 아이콘의 중앙으로 설정
        start_marker.setPosition(0.5F, 1.0F)  // 마커의 중심점을 하단, 중앙으로 설정

        tMapView.addTMapMarkerItem(start_marker)

        // 도착점 마커
        val end_marker = TMapMarkerItem()
        end_marker.id = "End"
        end_marker.setTMapPoint(endPoint.latitude, endPoint.longitude)
        end_marker.icon = BitmapFactory.decodeResource(resources, com.skt.tmap.R.drawable.poi)
        // 필요 없긴 함
        end_marker.setPosition(0.5F, 0.5F) // 마커의 중심점을 아이콘의 중앙으로 설정
        end_marker.setPosition(0.5F, 1.0F)  // 마커의 중심점을 하단, 중앙으로 설정

        tMapView.addTMapMarkerItem(end_marker)


        // 여기에 누르면 POI띄우기 구현
        //            tMapView.setOnClickListenerCallback(object : OnClickListenerCallback {
        //
        //                override fun onPressDown(
        //                    p0: java.util.ArrayList<TMapMarkerItem>?,
        //                    p1: java.util.ArrayList<TMapPOIItem>?,
        //                    p2: TMapPoint?,
        //                    p3: PointF?
        //                ) {
        //                    Toast.makeText(this@MainActivity, "onPressDown", Toast.LENGTH_SHORT).show()
        //                }
        //
        //                override fun onPressUp(
        //                    p0: java.util.ArrayList<TMapMarkerItem>?,
        //                    p1: java.util.ArrayList<TMapPOIItem>?,
        //                    p2: TMapPoint?,
        //                    p3: PointF?
        //                ) {
        //                    Toast.makeText(this@MainActivity, "onPressUp", Toast.LENGTH_SHORT).show()
        //                }
        //            })

        tMapContainer.addView(tMapView)
    }
}

//TMapView tmapView = new TMapView(this);
//tmapview.onResume();