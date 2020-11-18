package `fun`.aragaki.kraft.data.services

import `fun`.aragaki.kraft.data.entities.PixivTokenResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST


interface PixivAuth {


    @Headers(
        "User-Agent:PixivAndroidApp/5.0.134 (Android 6.0.1; D6653)",
        "Accept-Language:zh_CN"
    )
    @FormUrlEncoded
    @POST("/auth/token")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("client_id") client_id: String = CLIENT_ID,
        @Field("client_secret") client_secret: String = CLIENT_SECRET,
        @Field("device_token") device_token: String = DEVICE_TOKEN,
        @Field("get_secure_url") get_secure_url: String = GET_SECURE_URL,
        @Field("grant_type") grant_type: String = GRANT_TYPE_LOGIN,
        @Field("include_policy") include_policy: String = INCLUDE_POLICY
    ): PixivTokenResponse


    @Headers(
        "User-Agent:PixivAndroidApp/5.0.134 (Android 6.0.1; D6653)",
        "Accept-Language:zh_CN"
    )
    @FormUrlEncoded
    @POST("/auth/token")
    suspend fun refresh(
        @Field("refresh_token") refresh_token: String,
        @Field("client_id") client_id: String = CLIENT_ID,
        @Field("client_secret") client_secret: String = CLIENT_SECRET,
        @Field("grant_type") grant_type: String = GRANT_TYPE_REFRESH,
        @Field("device_token") device_token: String = DEVICE_TOKEN,
        @Field("get_secure_url") get_secure_url: String = GET_SECURE_URL,
        @Field("include_policy") include_policy: String = INCLUDE_POLICY
    ): PixivTokenResponse

    companion object {
        const val CLIENT_ID = "MOBrBDS8blbauoSck0ZfDbtuzpyT"
        const val CLIENT_SECRET = "lsACyCD94FhDUtGTXi3QzcFE2uU1hqtDaKeqrdwj"
        const val DEVICE_TOKEN = "pixiv"
        const val GET_SECURE_URL = "true"

        const val GRANT_TYPE_LOGIN = "password"
        const val GRANT_TYPE_REFRESH = "refresh_token"

        const val INCLUDE_POLICY = "true"

        operator fun invoke(
            client: OkHttpClient, converter: GsonConverterFactory,
            baseUrl: String
        ): PixivAuth {
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(converter)
                .build()
                .create(PixivAuth::class.java)
        }
    }
}