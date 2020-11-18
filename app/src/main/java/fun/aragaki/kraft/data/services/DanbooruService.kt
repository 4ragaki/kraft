package `fun`.aragaki.kraft.data.services

import `fun`.aragaki.kraft.data.entities.DanbooruPost
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DanbooruService {

    /**
    Show
    The base URL is GET /posts/$id.json where $id is the post id.*/

    @GET("/posts/{id}.json")
    suspend fun show(@Path("id") id: Long): DanbooruPost


    /**
    Listing
    The base URL is GET /posts.json.

    limit How many posts you want to retrieve.
    page The page number.
    tags The tags to search for. Any tag combination that works on the web site will work here. This includes all the meta-tags.
    md5 The md5 of the image to search for.
    random Can be: true, false
    raw When this parameter is set the tags parameter will not be parsed for aliased tags, metatags or multiple tags, and will instead be parsed as a single literal tag.*/

    @GET("/posts.json")
    suspend fun posts(
        @Query("tags") tags: String?,
        @Query("md5") md5: String?,
        @Query("random") random: Boolean?,
        @Query("raw") raw: Boolean?,
        @Query("limit") limit: Int?,
        @Query("page") page: Int?
    ): List<DanbooruPost>

    @GET("/explore/posts/popular.json")
    suspend fun popular(
        @Query("date", encoded = true) date: String,
        @Query("scale") scale: String
    ): List<DanbooruPost>


    companion object {
        operator fun invoke(
            client: OkHttpClient,
            converterFactory: GsonConverterFactory,
            baseUrl: String
        ): DanbooruService {
            return Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(converterFactory)
                .build()
                .create(DanbooruService::class.java)
        }
    }
}