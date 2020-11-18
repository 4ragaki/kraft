package `fun`.aragaki.kraft.data.services

import `fun`.aragaki.kraft.data.entities.SankakuPost
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SankakuService {

    @GET("/posts/{postId}")
    suspend fun id(@Path("postId") postId: Long): SankakuPost

    @GET("/posts")
    suspend fun posts(@Query("tags") tags: String?, @Query("page") page: Int): List<SankakuPost>

    companion object {
        operator fun invoke(
            client: OkHttpClient,
            converterFactory: GsonConverterFactory,
            baseUrl: String
        ): SankakuService {
            return Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(converterFactory)
                .build()
                .create(SankakuService::class.java)
        }
    }
}