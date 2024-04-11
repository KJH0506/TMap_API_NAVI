import com.example.tmap_api_navi.TMAPService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TmapCilient{
    private const val baseUrl = "https://apis.openapi.sk.com/"

    private val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build()

    val service = retrofit.create(TMAPService::class.java)
}