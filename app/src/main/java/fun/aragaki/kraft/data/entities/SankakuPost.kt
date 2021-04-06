package `fun`.aragaki.kraft.data.entities

import `fun`.aragaki.kraft.data.extensions.*
import `fun`.aragaki.kraft.data.servicewrappers.BooruWrapper

data class SankakuPost(
    val author: Author?,
    val change: Int?,
    val comment_count: Any?,
    val created_at: CreatedAt?,
    val fav_count: Int?,
    val file_size: Long?,
    val file_type: String?,
    val file_url: String?,
    val has_children: Boolean?,
    val has_comments: Boolean?,
    val has_notes: Boolean?,
    val height: Int?,
    val id: Long,
    val in_visible_pool: Boolean?,
    val is_favorited: Boolean?,
    val is_premium: Boolean?,
    val md5: String?,
    val parent_id: Any?,
    val preview_height: Int?,
    val preview_url: String?,
    val preview_width: Int?,
    val rating: String?,
    val recommended_posts: Int?,
    val recommended_score: Int?,
    val sample_height: Int?,
    val sample_url: String?,
    val sample_width: Int?,
    val sequence: Any?,
    val source: String?,
    val status: String?,
    val tags: List<Tag>?,
    val total_score: Int?,
    val user_vote: Any?,
    val vote_count: Int?,
    val width: Int?
) : Post() {
    data class Author(
        val avatar: String?,
        val avatar_rating: String?,
        val id: Long?,
        val name: String?
    )

    data class CreatedAt(
        val json_class: String?,
        val n: String?,
        val s: Long?
    )

    data class Tag(
        val count: Int?,
        val id: Int?,
        val locale: String?,
        val name: String,
        val name_en: String?,
        val name_ja: String?,
        val pool_count: Int?,
        val post_count: Int?,
        val rating: Any?,
        val type: Int?
    )

    override lateinit var pWrapper: BooruWrapper
    override lateinit var pTitle: String
    override fun attachContext(wrapper: BooruWrapper) = extAttachContext(wrapper)
    override fun postId() = id
    override fun postPreview() = preview_url
    override fun preview() = extPreview()
    override fun info() = extInfo()
    override fun downloads(postNameFmt: String) = extDownloads(postNameFmt)
    override fun message(): Nothing? = null
}