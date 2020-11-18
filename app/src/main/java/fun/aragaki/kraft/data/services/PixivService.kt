package `fun`.aragaki.kraft.data.services

import `fun`.aragaki.kraft.data.entities.PixivErrorResponse
import `fun`.aragaki.kraft.data.entities.PixivIllustrationResponse
import `fun`.aragaki.kraft.data.entities.PixivRankResponse
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import retrofit2.http.POST

import retrofit2.http.FormUrlEncoded


interface PixivService {
    @Headers(
        "User-Agent:PixivAndroidApp/5.0.134 (Android 6.0.1; D6653)",
        "Accept-Language:zh_CN"
    )
    @GET("v1/search/illust")
    suspend fun search(
        @Header("Authorization") token: String,
        @Query("word") word: String?,
        @Query("offset") offset: Long?,
        @Query("sort") sort: String = "popular_desc",
        @Query("search_target") search_target: String = "partial_match_for_tags",
        @Query("filter") filter: String = "for_android",
        @Query("include_translated_tag_results") include_translated_tag_results: Boolean = true
    ): PixivRankResponse

    @GET("v1/illust/detail")
    suspend fun getIllustByID(
        @Header("Authorization") token: String,
        @Query("illust_id") illust_id: Long,
    ): PixivIllustrationResponse

    @FormUrlEncoded
    @POST("v1/user/follow/add")
    suspend fun follow(
        @Header("Authorization") token: String?,
        @Field("user_id") user_id: Long,
        @Field("restrict") followType: String = Restrict.PUBLIC.value
    ): PixivErrorResponse

    @FormUrlEncoded
    @POST("v1/user/follow/delete")
    suspend fun unfollow(
        @Header("Authorization") token: String?,
        @Field("user_id") user_id: Long
    ): PixivErrorResponse

    @FormUrlEncoded
    @POST("v2/illust/bookmark/add")
    suspend fun bookmarkAdd(
        @Header("Authorization") token: String,
        @Field("illust_id") illust_id: Long,
        @Field("restrict") restrict: String = Restrict.PUBLIC.value
    ): PixivErrorResponse

    @FormUrlEncoded
    @POST("v1/illust/bookmark/delete")
    suspend fun bookmarkDelete(
        @Header("Authorization") token: String,
        @Field("illust_id") illust_id: Long
    ): PixivErrorResponse


    companion object {
        enum class Restrict(val value: String) {
            PUBLIC("public"), PRIVATE("private")
        }

        operator fun invoke(
            client: OkHttpClient, converter: GsonConverterFactory,
            baseUrl: String
        ): PixivService {
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(converter)
                .build()
                .create(PixivService::class.java)
        }
    }
}