package `fun`.aragaki.kraft.data.entities

import `fun`.aragaki.kraft.data.extensions.*
import `fun`.aragaki.kraft.data.room.PostConverters
import `fun`.aragaki.kraft.data.servicewrappers.BooruWrapper
import `fun`.aragaki.kraft.data.servicewrappers.DanbooruWrapper
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.serialization.Serializable

@Entity(tableName = "DanbooruPosts")
@Keep
@Serializable
@TypeConverters(PostConverters::class)
data class DanbooruPost(
    val approver_id: Int?,
    val bit_flags: Int?,
    val children_ids: String?,
    val created_at: String?,
    val down_score: Int?,
    val fav_count: Int?,
    val file_ext: String,
    val file_size: Long?,
    val file_url: String?,
    val has_active_children: Boolean?,
    val has_children: Boolean?,
    val has_large: Boolean?,
    val has_visible_children: Boolean?,

    @PrimaryKey
    val id: Long,
    val image_height: Int?,
    val image_width: Int?,
    val is_banned: Boolean?,
    val is_deleted: Boolean?,
    val is_favorited: Boolean?,
    val is_flagged: Boolean?,
    val is_note_locked: Boolean?,
    val is_pending: Boolean?,
    val is_rating_locked: Boolean?,
    val is_status_locked: Boolean?,
    val keeper_data: String?,
    val large_file_url: String?,
    val last_comment_bumped_at: String?,
    val last_commented_at: String?,
    val last_noted_at: String?,
    val md5: String?,
    val parent_id: Int?,
    val pixiv_id: Int?,
    val pool_string: String?,
    val preview_file_url: String?,
    val rating: String?,
    val score: Int?,
    val source: String?,
    val tag_count: Int?,
    val tag_count_artist: Int?,
    val tag_count_character: Int?,
    val tag_count_copyright: Int?,
    val tag_count_general: Int?,
    val tag_count_meta: Int?,
    val tag_string: String?,
    val tag_string_artist: String?,
    val tag_string_character: String?,
    val tag_string_copyright: String?,
    val tag_string_general: String?,
    val tag_string_meta: String?,
    val up_score: Int?,
    val updated_at: String?,
    val uploader_id: Long?,
    val uploader_name: String?
) : Post() {
    override fun postId() = id
    override fun postPreview() = preview_file_url
    override fun preview(wrapper: BooruWrapper) = extPreview(wrapper)
    override fun info(wrapper: BooruWrapper) = extInfo(wrapper)
    override fun downloads(wrapper: BooruWrapper, postNameFmt: String) =
        extDownload(wrapper, postNameFmt)

    override fun message(): Nothing? = null
    override fun attachContext(wrapper: BooruWrapper) = extAttachContext(wrapper)
}