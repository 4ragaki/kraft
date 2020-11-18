package `fun`.aragaki.kraft.data.services

import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface IQDBService {

    @POST("/")
    @Multipart
    suspend fun search(@Part file: MultipartBody.Part): ResponseBody

    companion object {
        const val BASE_URL = "https://iqdb.org"

        operator fun invoke(
            client: OkHttpClient
        ): IQDBService {
            return Retrofit.Builder()
                .client(client)
                .baseUrl(BASE_URL)
                .build()
                .create(IQDBService::class.java)
        }
    }
}