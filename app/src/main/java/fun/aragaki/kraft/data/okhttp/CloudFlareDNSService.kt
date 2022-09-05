package `fun`.aragaki.kraft.data.okhttp

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface CloudFlareDNSService {

    @GET("/dns-query")
    fun query(
        @Query("name") name: String,
        @Query("ct") ct: String? = "application/dns-json",
        @Query("type") type: String = "A",
        @Query("do") `do`: Boolean? = null,
        @Query("cd") cd: Boolean? = null
    ): Call<CloudFlareDNSResponse>

    companion object {
        operator fun invoke(client: OkHttpClient): CloudFlareDNSService {
            return Retrofit.Builder()
                .baseUrl("https://1.0.0.1/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CloudFlareDNSService::class.java)
        }
    }
}
