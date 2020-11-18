package `fun`.aragaki.kraft.data.services

import `fun`.aragaki.kraft.data.entities.GelbooruCommentsResponse
import `fun`.aragaki.kraft.data.entities.GelbooruDeletedPostsResponse
import `fun`.aragaki.kraft.data.entities.GelbooruPostResponse
import `fun`.aragaki.kraft.data.entities.GelbooruTagsResponse
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

interface GelbooruService {


    /**
     * Posts List
    Url for API access: /index.php?page=dapi&s=post&q=index

     * @param limit How many posts you want to retrieve. There is a default limit of 100 posts per request.
     * @param pid The page number.
     * @param tags The tags to search for. Any tag combination that works on the web site will work here. This includes all the meta-tags. See cheatsheet for more information.
     * @param cid Change ID of the post. This is in Unix time so there are likely others with the same value if updated at the same time.
     * @param id The post id.
     * @param json Set to 1 for JSON formatted response.*/
    @GET("/index.php")
    suspend fun postsList(
        @Query("id") id: Long?,
        @Query("tags") tags: String?,
        @Query("pid") pid: Int?,
        @Query("limit") limit: Int? = null,
        @Query("cid") cid: Int? = null,
        @Query("json") json: Int? = 0,
        @Query("page") page: String = "dapi",
        @Query("s") s: String = "post",
        @Query("q") q: String = "index"
    ): GelbooruPostResponse

    /**
     * Tag List

    Url for API access: /index.php?page=dapi&s=tag&q=index

     * @param id The tag's id in the database. This is useful to grab a specific tag if you already know this value.
     * @param limit How many tags you want to retrieve. There is a default limit of 100 per request.
     * @param after_id Grab tags whose ID is greater than this value.
     * @param name Find tag information based on this value.
     * @param names Separated by spaces, get multiple results to tags you specify if it exists. (schoolgirl moon cat)
     * @param name_pattern A wildcard search for your query using LIKE. Use _ for single character wildcards or % for multi-character wildcards. (%choolgirl% would act as *choolgirl* wildcard search.)
     * @param json Set to 1 for JSON formatted response.
     * @param order Order by field specified (ASC or DESC)
     * @param orderby Order by a field.- date- count- name*/
    @GET("/index.php")
    suspend fun tagsList(
        @Query("id") id: Long? = 0,
        @Query("limit") limit: Int? = 0,
        @Query("after_id") after_id: Long? = 0,
        @Query("name") name: String?,
        @Query("names") names: String?,
        @Query("name_pattern") name_pattern: String?,
        @Query("order") order: String?,
        @Query("orderby") orderby: String?,
        @Query("page") page: String = "dapi",
        @Query("s") s: String = "tag",
        @Query("q") q: String = "index"
    ): GelbooruTagsResponse

    /**
     * Comments List

    Url for API access: /index.php?page=dapi&s=comment&q=index

     * @param post_id The id number of the comment to retrieve.*/
    @GET("/index.php")
    suspend fun commentsList(
        @Query("post_id") post_id: Long?,
        @Query("json") json: Int? = 0,
        @Query("page") page: String = "dapi",
        @Query("s") s: String = "comment",
        @Query("q") q: String = "index"
    ): GelbooruCommentsResponse

    /**
     * Deleted Images

    Url for API access: /index.php?page=dapi&s=post&q=index&deleted=show

     * @param last_id A numerical value. Will return everything above this number.*/
    @GET("/index.php")
    suspend fun deletedImages(
        @Query("last_id") last_id: Long?,
        @Query("json") json: Int? = 0,
        @Query("page") page: String = "dapi",
        @Query("s") s: String = "post",
        @Query("q") q: String = "index",
        @Query("deleted") deleted: String = "show"
    ): GelbooruDeletedPostsResponse

    companion object {
        operator fun invoke(
            client: OkHttpClient,
            converterFactory: TikXmlConverterFactory,
            baseUrl: String
        ): GelbooruService {
            return Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(converterFactory)
                .build()
                .create(GelbooruService::class.java)
        }
    }
}