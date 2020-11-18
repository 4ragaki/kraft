package `fun`.aragaki.kraft.data.services

import `fun`.aragaki.kraft.data.entities.*
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

/**
 * API 1.13.0+update.3
 * @author Aragaki
 *
 * tested on yande.re/konachan.com
 * @see <a href="https://yande.re/help/api">yande.re API Documentation</a>*/
interface MoebooruService {


    /**
     * Posts
     * List
    The base URL is /post.xml.

     * @param limit How many posts you want to retrieve. There is a hard limit of 100 posts per request.
     * @param page The page number.
     * @param tags The tags to search for. Any tag combination that works on the web site will work here. This includes all the meta-tags.*/
    @GET("/post.json")
    suspend fun postsList(
        @Query("tags") tags: String?,
        @Query("limit") limit: Int? = null,
        @Query("page") page: Int? = null
    ): List<MoebooruPost>


    /**
     * Posts
     * Create
    The base URL is /post/create.xml.
    There are only two mandatory fields: you need to supply the tags, and you need to supply the file,
    either through a multipart form or through a source URL.

     * @param post[tags] A space delimited list of tags.
     * @param post[file] The file data encoded as a multipart form.
     * @param post[rating] The rating for the post. Can be: safe, questionable, or explicit.
     * @param post[source] If this is a URL, Danbooru will download the file.
     * @param post[is_rating_locked] Set to true to prevent others from changing the rating.
     * @param post[is_note_locked] Set to true to prevent others from adding notes.
     * @param post[parent_id] The ID of the parent post.
     * @param md5 Supply an MD5 if you want Danbooru to verify the file after uploading. If the MD5 doesn't match, the post is destroyed.
    If the call fails, the following response reasons are possible:

    MD5 mismatch This means you supplied an MD5 parameter and what Danbooru got doesn't match. Try uploading the file again.
    duplicate This post already exists in Danbooru (based on the MD5 hash). An additional attribute called location will be set, pointing to the (relative) URL of the original post.
    other Any other error will have its error message printed.
    If the post upload succeeded, you'll get an attribute called location in the response pointing to the relative URL of your newly uploaded post.*/
    @Multipart
    @POST("/post/create.json")
    suspend fun postsCreate(
        @Query("login") login: String,
        @Query("password_hash") password_hash: String,
        @Query("post[tags]") tags: String?,
        @Part("post[file]") file: MultipartBody.Part?,
        @Query("post[rating]") rating: String?,
        @Query("post[source]") source: String?,
        @Query("post[is_rating_locked]") is_rating_locked: Boolean?,
        @Query("post[is_note_locked]") is_note_locked: Boolean?,
        @Query("post[parent_id]") parent_id: Long?,
        @Query("md5") md5: String?
    ): ResponseBody


    /**
     * Posts
     * Update
    The base URL is /post/update.xml.
    Only the id parameter is required. Leave the other parameters blank if you don't want to change them.

     * @param id The id number of the post to update.
     * @param post[tags] A space delimited list of tags.
     * @param post[file] The file data encoded as a multipart form.
     * @param post[rating] The rating for the post. Can be: safe, questionable, or explicit.
     * @param post[source] If this is a URL, Danbooru will download the file.
     * @param post[is_rating_locked] Set to true to prevent others from changing the rating.
     * @param post[is_note_locked] Set to true to prevent others from adding notes.
     * @param post[parent_id] The ID of the parent post.
    Destroy*/
    @Multipart
    @POST("/post/update.json")
    suspend fun postsUpdate(
        @Query("login") login: String,
        @Query("password_hash") password_hash: String,
        @Query("id") id: Long,
        @Query("post[tags]") tags: String?,
        @Query("post[file]") file: MultipartBody.Part?,
        @Query("post[rating]") rating: String?,
        @Query("post[source]") source: String?,
        @Query("post[is_rating_locked]") is_rating_locked: Boolean?,
        @Query("post[is_note_locked]") is_note_locked: Boolean?,
        @Query("post[parent_id]") parent_id: Long?
    ): ResponseBody


    /**
     * Posts
     * Destroy
    You must be logged in to use this action. You must also be the user who uploaded the post (or you must be a moderator).

     * @param id The id number of the post to delete.
     */
    @POST("/post/destroy.json")
    suspend fun postsDestroy(
        @Query("login") login: String,
        @Query("password_hash") password_hash: String,
        @Query("id") id: Long
    ): ResponseBody


    /**
     * Posts
     * Revert Tags
    This action reverts a post to a previous set of tags. The base URL is /post/revert_tags.xml.

     * @param id The post id number to update.
     * @param history_id The id number of the tag history.*/
    @POST("/post/revert_tags.json")
    suspend fun postsRevertTags(
        @Query("login") login: String,
        @Query("password_hash") password_hash: String,
        @Query("id") id: Long,
        @Query("history_id") history_id: Long
    ): ResponseBody


    /**
     * Posts
     * Vote
    This action lets you vote for a post. You can only vote once per post per IP address. The base URL is /post/vote.xml.

     * @param id The post id number to update.
     * @param score Set to 1 to vote up and -1 to vote down. All other values will be ignored.
    If the call did not succeed, the following reasons are possible:

    already voted You have already voted for this post.
    invalid score You have supplied an invalid score.*/
    @POST("/post/vote.json")
    suspend fun postsVote(
        @Query("login") login: String,
        @Query("password_hash") password_hash: String,
        @Query("id") id: Long,
        @Query("score") score: Int
    ): ResponseBody


    /**
     * Tags
     * List
    The base URL is /tag.xml.

     * @param limit How many tags to retrieve. Setting this to 0 will return every tag.
     * @param page The page number.
     * @param order Can be date, count, or name.
     * @param id The id number of the tag.
     * @param after_id Return all tags that have an id number greater than this.
     * @param name The exact name of the tag.
     * @param name_pattern Search for any tag that has this parameter in its name.*/
    @GET("/tag.json")
    suspend fun tagsList(
        @Query("limit") limit: Int?,
        @Query("page") page: Int?,
        @Query("order") order: String?,
        @Query("id") id: Long?,
        @Query("after_id") after_id: Long?,
        @Query("name") name: String?,
        @Query("name_pattern") name_pattern: String?
    ): List<MoebooruTag>


    /**
     * Tags
     * Update
    The base URL is /tag/update.xml.

     * @param name The name of the tag to update.
     * @param tag[tag_type] The tag type. General: 0, artist: 1, copyright: 3, character: 4.
     * @param tag[is_ambiguous] Whether or not this tag is ambiguous. Use 1 for true and 0 for false.*/
    @POST("/tag/update.json")
    suspend fun tagsUpdate(
        @Query("login") login: String,
        @Query("password_hash") password_hash: String,
        @Query("name") name: String,
        @Query("tag[tag_type]") tag_type: Int,
        @Query("tag[is_ambiguous]") is_ambiguous: Int
    ): ResponseBody


    /**
     * Tags
     * Related
    The base URL is /tag/related.xml.

     * @param tags The tag names to query.
     * @param type Restrict results to this tag type (can be general, artist, copyright, or character).*/
    @GET("/tag/related.json")
    suspend fun tagsRelated(
        @Query("tags") tags: String,
        @Query("type") type: String
    ): ResponseBody


    /**
     * Artists
     * List
    The base URL is /artist.xml.

     * @param name The name (or a fragment of the name) of the artist.
     * @param order Can be date or name.
     * @param page The page number.*/
    @GET("/artist.json")
    suspend fun artistsList(
        @Query("name") name: String?,
        @Query("order") order: String?,
        @Query("page") page: Int?
    ): List<MoebooruArtist>


    /**
     * Artists
     * Create
    The base URL is /artist/create.xml.

     * @param artist[name] The artist's name.
     * @param artist[urls] A list of URLs associated with the artist, whitespace delimited.
     * @param artist[alias] The artist that this artist is an alias for. Simply enter the alias artist's name.
     * @param artist[group] The group or cicle that this artist is a member of. Simply enter the group's name.*/
    @POST("/artist/create.json")
    suspend fun artistsCreate(
        @Query("login") login: String,
        @Query("password_hash") password_hash: String,
        @Query("artist[name]") name: String,
        @Query("artist[urls]") urls: String?,
        @Query("artist[alias]") alias: String?,
        @Query("artist[group]") group: String?
    ): ResponseBody


    /**
     * Artists
     * Update
    The base URL is /artist/update.xml.
    Only the id parameter is required. The other parameters are optional.

     * @param id The id of thr artist to update.
     * @param artist[name] The artist's name.
     * @param artist[urls] A list of URLs associated with the artist, whitespace delimited.
     * @param artist[alias] The artist that this artist is an alias for. Simply enter the alias artist's name.
     * @param artist[group] The group or cicle that this artist is a member of. Simply enter the group's name.*/
    @POST("/artist/update.json")
    suspend fun artistsUpdate(
        @Query("login") login: String,
        @Query("password_hash") password_hash: String,
        @Query("id") id: Long,
        @Query("artist[name]") name: String?,
        @Query("artist[urls]") urls: String?,
        @Query("artist[alias]") alias: String?,
        @Query("artist[group]") group: String?
    ): ResponseBody


    /**
     * Artists
     * Destroy
    The base URL is /artist/destroy.xml.
    You must be logged in to delete artists.

     * @param id The id of the artist to destroy.*/
    @POST("/artist/destroy.json")
    suspend fun artistsDestroy(
        @Query("login") login: String,
        @Query("password_hash") password_hash: String,
        @Query("id") id: Long
    ): ResponseBody


    /**
     * working comment api
     */
    @GET("/comment.json")
    suspend fun commentList(@Query("post_id") post_id: Long): List<MoebooruComment>

    /**
     * doesn't work,no documentation use commentList(post_id:Long)
     * Comments
     * Show
    The base URL is /comment/show.xml.
    This retrieves a single comment.

     * @param id The id number of the comment to retrieve.*/
    @GET("/comment/show.json")
    suspend fun commentsShow(
        @Query("id") id: Long
    ): ResponseBody


    /**
     * Comments
     * Create
    The base URL is /comment/create.xml.

     * @param comment[anonymous] Set to 1 if you want to post this comment anonymously.
     * @param comment[post_id] The post id number to which you are responding.
     * @param comment[body] The body of the comment.*/
    @POST("/comment/create.json")
    suspend fun commentsCreate(
        @Query("login") login: String,
        @Query("password_hash") password_hash: String,
        @Query("comment[anonymous]") anonymous: Int,
        @Query("comment[post_id]") post_id: Long,
        @Query("comment[body]") body: String
    ): MoebooruResponse


    /**
     * doesn't work
     * Comments
     * Destroy
    The base url is /comment/destroy.xml.
    You must be logged in to use this action. You must also be the owner of the comment, or you must be a moderator.

     * @param id The id number of the comment to delete.*/
    @POST("/comment/destroy.json")
    suspend fun commentsDestroy(
        @Query("login") login: String,
        @Query("password_hash") password_hash: String,
        @Query("id") id: Long
    ): ResponseBody


    /**
     * Wiki
    All titles must be exact (but case and whitespace don't matter).

     * List
    The base URL is /wiki.xml.
    This retrieves a list of every wiki page.

     * @param order How you want the pages ordered. Can be: title, date.
     * @param limit The number of pages to retrieve.
     * @param page The page number.
     * @param query A word or phrase to search for.*/
    @GET("/wiki.json")
    suspend fun wikiList(
        @Query("order") order: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("page") page: Int? = null,
        @Query("query") query: String? = null
    ): List<MoebooruWiki>


    /**
     * Wiki
     * Create
    The base URL is /wiki/create.xml.

     * @param wiki_page[title] The title of the wiki page.
     * @param wiki_page[body] The body of the wiki page.*/
    @POST("/wiki/create.json")
    suspend fun wikiCreate(
        @Query("login") login: String,
        @Query("password_hash") password_hash: String,
        @Query("wiki_page[title]") title: String,
        @Query("wiki_page[body]") body: String
    ): ResponseBody


    /**
     * Wiki
     * Update
    The base URL is /wiki/update.xml.
    Potential error reasons: "Page is locked"

     * @param title The title of the wiki page to update.
     * @param wiki_page[title] The new title of the wiki page.
     * @param wiki_page[body] The new body of the wiki page.*/
    @POST("/wiki/update.json")
    suspend fun wikiUpdate(
        @Query("login") login: String,
        @Query("password_hash") password_hash: String,
        @Query("title") title: String,
        @Query("wiki_page[title]") new_title: String?,
        @Query("wiki_page[body]") body: String?
    ): ResponseBody


    /**
     * Wiki
     * Show
    The base URL is /wiki/show.xml.
    Potential error reasons: "artist type"

     * @param title The title of the wiki page to retrieve.
     * @param version The version of the page to retrieve.*/
    @GET("/wiki/show.json")
    suspend fun wikiShow(
        @Query("title") title: String,
        @Query("version") version: String?
    ): ResponseBody


    /**
     * Wiki
     * Destroy
    The base URL is /wiki/destroy.xml.
    You must be logged in as a moderator to use this action.

     * @param title The title of the page to delete.*/
    @POST("/wiki/destroy.json")
    suspend fun wikiDestroy(
        @Query("login") login: String,
        @Query("password_hash") password_hash: String,
        @Query("title") title: String
    ): ResponseBody


    /**
     * Wiki
     * Lock
    The base URL is /wiki/lock.xml.
    You must be logged in as a moderator to use this action.

     * @param title The title of the page to lock.*/
    @POST("/wiki/lock.json")
    suspend fun wikiLock(
        @Query("login") login: String,
        @Query("password_hash") password_hash: String,
        @Query("title") title: String
    ): ResponseBody


    /**
     * Wiki
     * Unlock
    The base URL is /wiki/unlock.xml.
    You must be logged in as a moderator to use this action.

     * @param title The title of the page to unlock.*/
    @POST("/wiki/unlock.json")
    suspend fun wikiUnlock(
        @Query("login") login: String,
        @Query("password_hash") password_hash: String,
        @Query("title") title: String
    ): ResponseBody


    /**
     * Wiki
     * Revert
    The base URL is /wiki/revert.xml.
    Potential error reasons: "Page is locked"

     * @param title The title of the wiki page to update.
     * @param version The version to revert to.*/
    @POST("/wiki/revert.json")
    suspend fun wikiRevert(
        @Query("login") login: String,
        @Query("password_hash") password_hash: String,
        @Query("title") title: String,
        @Query("version") version: String
    ): ResponseBody


    /**
     * Wiki
     * History
    The base URL is /wiki/history.xml.

     * @param title The title of the wiki page to retrieve versions for.*/
    @GET("/wiki/history.json")
    suspend fun wikiHistory(
        @Query("title") title: String
    ): ResponseBody


    /**
     * Notes
     * List
    The base URL is /note.xml.

     * @param post_id The post id number to retrieve notes for.*/
    @GET("/note.json")
    suspend fun notesList(
        @Query("post_id") post_id: Long
    ): List<MoebooruNote>


    /**
     * Notes
     * Search
    The base URL is /note/search.xml.

     * @param query A word or phrase to search for.*/
    @GET("/note/search.json")
    suspend fun notesSearch(
        @Query("query") query: String
    ): ResponseBody


    /**
     * Notes
     * History
    The base URL is /note/history.xml.
    You can either specify id, post_id, or nothing. Specifying nothing will give you a list of every note verison.

     * @param limit How many versions to retrieve.
     * @param page The offset.
     * @param post_id The post id number to retrieve note versions for.
     * @param id The note id number to retrieve versions for.*/
    @GET("/note/history.json")
    suspend fun notesHistory(
        @Query("limit") limit: Int?,
        @Query("page") page: Int?,
        @Query("post_id") post_id: Long?,
        @Query("id") id: Long?
    ): ResponseBody


    /**
     * Notes
     * Revert
    The base URL is /note/revert.xml.
    Potential error reasons: "Post is locked"

     * @param id The note id to update.
     * @param version The version to revert to.*/
    @POST("/note/revert.json")
    suspend fun notesRevert(
        @Query("login") login: String,
        @Query("password_hash") password_hash: String,
        @Query("id") id: Long,
        @Query("version") version: String
    ): ResponseBody


    /**
     * Notes
     * Create/Update
    The base URL is /note/update.xml.
    Notes differ from the other controllers in that the interface for creation and updates is the same. If you supply an id parameter, then Danbooru will assume you're updating an existing note. Otherwise, it will create a new note. Potential error reasons: "Post is locked"

     * @param id If you are updating a note, this is the note id number to update.
     * @param note[post_id] The post id number this note belongs to.
     * @param note[x] The x coordinate of the note.
     * @param note[y] The y coordinate of the note.
     * @param note[width] The width of the note.
     * @param note[height] The height of the note.
     * @param note[is_active] Whether or not the note is visible. Set to 1 for active, 0 for inactive.
     * @param note[body] The note message.*/
    @POST("/note/update.json")
    suspend fun notesCreateUpdate(
        @Query("login") login: String,
        @Query("password_hash") password_hash: String,
        @Query("id") id: Long?,
        @Query("note[post_id]") post_id: Long,
        @Query("note[x]") x: String?,
        @Query("note[y]") y: String?,
        @Query("note[width]") width: String?,
        @Query("note[height]") height: String?,
        @Query("note[is_active]") is_active: String?,
        @Query("note[body]") body: String?
    ): ResponseBody


    /**
     * Users
     * Search
    The base URL is /user.xml.
    If you don't specify any parameters you'll get a listing of all users.

     * @param id The id number of the user.
     * @param name The name of the user.*/
    @GET("/user.json")
    suspend fun usersSearch(
        @Query("id") id: Long?,
        @Query("name") name: String?
    ): List<MoebooruUser>


    /**
     * Forum
     * List
    The base URL is /forum.xml.
    If you don't specify any parameters you'll get a list of all the parent topics.

     * @param parent_id The parent ID number. You'll return all the responses to that forum post.*/
    @GET("/forum.json")
    suspend fun forumList(
        @Query("parent_id") parent_id: Long?
    ): List<MoebooruForum>


    /**
     * Pools
     * List Pools
    The base URL is /pool.xml.
    If you don't specify any parameters you'll get a list of all pools.

     * @param query The title.
     * @param page The page.*/
    @GET("/pool.json")
    suspend fun poolsListPools(
        @Query("query") query: String?,
        @Query("page") page: Long?
    ): List<MoebooruPool>


    /**
     * Pools
     * List Posts
    The base URL is /pool/show.xml.
    If you don't specify any parameters you'll get a list of all pools.

     * @param id The pool id number.
     * @param page The page.*/
    @GET("/pool/show.json")
    suspend fun poolsListPosts(
        @Query("id") id: Long?,
        @Query("page") page: Long?
    ): MoebooruPool


    /**
     * Pools
     * Update
    The base URL is /pool/update.xml.

     * @param id The pool id number.
     * @param pool[name] The name.
     * @param pool[is_public] 1 or 0, whether or not the pool is public.
     * @param pool[description] A description of the pool.*/
    @POST("/pool/update.json")
    suspend fun poolsUpdate(
        @Query("login") login: String,
        @Query("password_hash") password_hash: String,
        @Query("id") id: Long,
        @Query("pool[name]") name: String?,
        @Query("pool[is_public]") is_public: Int?,
        @Query("pool[description]") description: String?
    ): ResponseBody


    /**
     * Pools
     * Create
    The base URL is /pool/create.xml.

     * @param pool[name] The name.
     * @param pool[is_public] 1 or 0, whether or not the pool is public.
     * @param pool[description] A description of the pool.*/
    @POST("/pool/create.json")
    suspend fun poolsCreate(
        @Query("login") login: String,
        @Query("password_hash") password_hash: String,
        @Query("pool[name]") name: String,
        @Query("pool[is_public]") is_public: Int?,
        @Query("pool[description]") description: String?
    ): ResponseBody


    /**
     * Pools
     * Destroy
    The base URL is /pool/destroy.xml.

     * @param id The pool id number.*/
    @POST("/pool/destroy.json")
    suspend fun poolsDestroy(
        @Query("login") login: String,
        @Query("password_hash") password_hash: String,
        @Query("id") id: Long
    ): ResponseBody


    /**
     * Pools
     * Add Post
    The base URL is /pool/add_post.xml.
    Potential error reasons: "Post already exists", "access denied"

     * @param pool_id The pool to add the post to.
     * @param post_id The post to add.*/
    @POST("/pool/add_post.json")
    suspend fun poolsAddPost(
        @Query("login") login: String,
        @Query("password_hash") password_hash: String,
        @Query("pool_id") pool_id: Long,
        @Query("post_id") post_id: Long
    ): ResponseBody


    /**
     * Pools
     * Remove Post
    The base URL is /pool/remove_post.xml.
    Potential error reasons: "access denied"

     * @param pool_id The pool to remove the post from.
     * @param post_id The post to remove.*/
    @POST("/pool/remove_post.json")
    suspend fun poolsRemovePost(
        @Query("login") login: String,
        @Query("password_hash") password_hash: String,
        @Query("pool_id") pool_id: Long,
        @Query("post_id") post_id: Long
    ): ResponseBody


    /**
     * Favorites
     * List Users
    The base URL is /favorite/list_users.json.
    There is no XML API for this action.

     * @param id The post id.*/
    @GET("/favorite/list_users.json")
    suspend fun favoritesListUsers(
        @Query("id") id: Long
    ): MoebooruFavorite


    /**
     * @param period 1d 1w 1m 1y
     * @return popular posts during give period
     */
    @GET("/post/popular_recent.json")
    suspend fun popular(@Query("period") period: String?): List<MoebooruPost>


    companion object {
        operator fun invoke(
            client: OkHttpClient,
            converterFactory: GsonConverterFactory,
            baseUrl: String
        ): MoebooruService {
            return Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(converterFactory)
                .build()
                .create(MoebooruService::class.java)
        }
    }
}