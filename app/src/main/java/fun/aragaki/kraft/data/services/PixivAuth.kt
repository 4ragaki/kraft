package `fun`.aragaki.kraft.data.services

import `fun`.aragaki.kraft.data.entities.PixivTokenResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface PixivAuth {
    @FormUrlEncoded
    @POST("/auth/token")
    suspend fun login(
        @Field("code_verifier") code_verifier: String,
        @Field("code") code: String,
        @Field("client_id") client_id: String = CLIENT_ID,
        @Field("client_secret") client_secret: String = CLIENT_SECRET,
        @Field("grant_type") grant_type: String = "authorization_code",
        @Field("redirect_uri") redirect_uri: String = "https://app-api.pixiv.net/web/v1/users/auth/pixiv/callback",
        @Field("include_policy") include_policy: Boolean = true
    ): PixivTokenResponse


    @FormUrlEncoded
    @POST("/auth/token")
    suspend fun refresh(
        @Field("refresh_token") refresh_token: String,
        @Field("client_id") client_id: String = CLIENT_ID,
        @Field("client_secret") client_secret: String = CLIENT_SECRET,
        @Field("grant_type") grant_type: String = "refresh_token",
        @Field("include_policy") include_policy: Boolean = true
    ): PixivTokenResponse

    companion object {
        const val CLIENT_ID = "MOBrBDS8blbauoSck0ZfDbtuzpyT"
        const val CLIENT_SECRET = "lsACyCD94FhDUtGTXi3QzcFE2uU1hqtDaKeqrdwj"

        operator fun invoke(
            client: OkHttpClient, converter: GsonConverterFactory, baseUrl: String
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