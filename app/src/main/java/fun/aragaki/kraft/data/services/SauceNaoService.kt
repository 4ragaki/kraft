package `fun`.aragaki.kraft.data.services

import `fun`.aragaki.kraft.data.entities.SauceNaoResponse
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface SauceNaoService {

    @Multipart
    @POST("/search.php")
    suspend fun search(
        @Query("api_key") api_key: String,
        @Part file: MultipartBody.Part,
        @Query("output_type") output_type: Int = 2,
        @Query("testmode") testmode: Int? = 0,
        @Query("dbmask") dbmask: Int = 999,
        @Query("dbmaski") dbmaski: Int? = null,
        @Query("db") db: Int? = null,
        @Query("numres") numres: Int? = null
    ): SauceNaoResponse

    companion object {
        operator fun invoke(
            client: OkHttpClient,
            converterFactory: GsonConverterFactory
        ): SauceNaoService {
            return Retrofit.Builder()
                .client(client)
                .baseUrl("https://saucenao.com")
                .addConverterFactory(converterFactory)
                .build()
                .create(SauceNaoService::class.java)
        }
    }
}