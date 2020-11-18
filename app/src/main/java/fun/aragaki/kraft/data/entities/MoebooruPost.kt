package `fun`.aragaki.kraft.data.entities

import `fun`.aragaki.kraft.data.extensions.*
import `fun`.aragaki.kraft.data.room.PostConverters
import `fun`.aragaki.kraft.data.servicewrappers.BooruWrapper
import androidx.annotation.Keep
import androidx.room.*
import kotlinx.serialization.Serializable

@Keep
@Serializable
@Entity(tableName = "MoebooruPosts")
@TypeConverters(PostConverters::class)
data class MoebooruPost(
    val actual_preview_height: Int?,
    val actual_preview_width: Int?,
    val approver_id: String?,
    val author: String?,
    val change: Int?,
    val created_at: Long?,
    val creator_id: Long?,
//    null on konachan.com
    val file_ext: String?,
    val file_size: Long?,
    val file_url: String?,
    @Embedded
    val flag_detail: FlagDetail?,
    val frames: List<String>?,
    val frames_pending: List<String>?,
    val frames_pending_string: String?,
    val frames_string: String?,
    val has_children: Boolean?,
    val height: Int?,
    @PrimaryKey
    val id: Long,
    val is_held: Boolean?,
    val is_note_locked: Boolean?,
    val is_pending: Boolean?,
    val is_rating_locked: Boolean?,
    val is_shown_in_index: Boolean?,
    val jpeg_file_size: Int?,
    val jpeg_height: Int?,
    val jpeg_url: String?,
    val jpeg_width: Int?,
    val last_commented_at: Int?,
    val last_noted_at: Int?,
    val md5: String?,
    val parent_id: Int?,
    val preview_height: Int?,
    val preview_url: String?,
    val preview_width: Int?,
    val rating: String?,
    val sample_file_size: Int?,
    val sample_height: Int?,
    val sample_url: String?,
    val sample_width: Int?,
    val score: Int?,
    val source: String?,
    val status: String?,
    val tags: String,
    val updated_at: Int?,
    val width: Int?
) : Post() {

    @Keep
    @Serializable
    data class FlagDetail(
        val post_id: Long?,
        val reason: String?,
        @ColumnInfo(name = "flag_created_at")
        val created_at: String?
    )

    override fun postId() = id
    override fun postPreview() = preview_url
    override fun preview(wrapper: BooruWrapper) = extPreview(wrapper)
    override fun info(wrapper: BooruWrapper) = extInfo(wrapper)
    override fun downloads(wrapper: BooruWrapper, postNameFmt: String) =
        extDownloads(wrapper, postNameFmt)

    override fun message() = flag_detail?.reason

    override fun attachContext(wrapper: BooruWrapper) = extAttachContext(wrapper)
}