package `fun`.aragaki.kraft.data.entities

import `fun`.aragaki.kraft.data.extensions.*
import `fun`.aragaki.kraft.data.room.PostConverters
import `fun`.aragaki.kraft.data.servicewrappers.BooruWrapper
import androidx.annotation.Keep
import androidx.room.*
import kotlinx.serialization.Serializable

@Keep
@Serializable
@Entity(tableName = "SankakuPosts")
@TypeConverters(PostConverters::class)
data class SankakuPost(
    @Embedded
    val author: Author?,
    val change: Int?,
    val comment_count: String?,
    @Embedded
    val created_at: CreatedAt?,
    val fav_count: Int?,
    val file_size: Long?,
    val file_type: String?,
    val file_url: String?,
    val has_children: Boolean?,
    val has_comments: Boolean?,
    val has_notes: Boolean?,
    val height: Int?,

    @PrimaryKey
    val id: Long,
    val in_visible_pool: Boolean?,
    val is_favorited: Boolean?,
    val is_premium: Boolean?,
    val md5: String?,
    val parent_id: String?,
    val preview_height: Int?,
    val preview_url: String?,
    val preview_width: Int?,
    val rating: String?,
    val recommended_posts: Int?,
    val recommended_score: Int?,
    val sample_height: Int?,
    val sample_url: String?,
    val sample_width: Int?,
    val sequence: String?,
    val source: String?,
    val status: String?,
    val tags: List<Tag>?,
    val total_score: Int?,
    val user_vote: String?,
    val vote_count: Int?,
    val width: Int?
) : Post() {

    @Keep
    @Serializable
    data class Author(
        val avatar: String?,
        val avatar_rating: String?,
        @ColumnInfo(name = "author_id")
        val id: Long?,
        val name: String?
    )

    @Keep
    @Serializable
    data class CreatedAt(
        val json_class: String?,
        val n: String?,
        val s: Long?
    )

    @Keep
    @Serializable
    data class Tag(
        val count: Int?,
        val id: Int?,
        val locale: String?,
        val name: String,
        val name_en: String?,
        val name_ja: String?,
        val pool_count: Int?,
        val post_count: Int?,
        val rating: String?,
        val type: Int?
    )

    override fun postId() = id
    override fun postPreview() = preview_url
    override fun preview(wrapper: BooruWrapper) = extPreview(wrapper)
    override fun info(wrapper: BooruWrapper) = extInfo(wrapper)
    override fun downloads(wrapper: BooruWrapper, postNameFmt: String) =
        extDownloads(wrapper, postNameFmt)

    override fun message(): Nothing? = null

    override fun attachContext(wrapper: BooruWrapper) = extAttachContext(wrapper)
}