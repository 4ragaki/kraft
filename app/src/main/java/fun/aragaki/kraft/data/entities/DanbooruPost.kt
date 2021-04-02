package `fun`.aragaki.kraft.data.entities

import `fun`.aragaki.kraft.R
import `fun`.aragaki.kraft.data.servicewrappers.BooruWrapper
import `fun`.aragaki.kraft.data.servicewrappers.TagType
import `fun`.aragaki.kraft.ext.dateFormatter
import `fun`.aragaki.kraft.ext.splitByBlank
import android.text.format.Formatter
import java.text.SimpleDateFormat

data class DanbooruPost(
    val approver_id: Int?,
    val bit_flags: Int?,
    val children_ids: Any?,
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
    val last_comment_bumped_at: Any?,
    val last_commented_at: Any?,
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
    override lateinit var pWrapper: BooruWrapper
    override lateinit var pTitle: String

    override fun attachContext(wrapper: BooruWrapper) = apply {
        pWrapper = wrapper
        pTitle = "${wrapper.booru.name} $id"
    }

    override fun postId() = id
    override fun postPreview() = preview_file_url

    override fun preview() = Preview(
        listOf(large_file_url), pWrapper.dependencyTag, info(),
        mapOf(
            TagType.Artist.key to tag_string_artist?.splitByBlank(),
            TagType.Character.key to tag_string_character?.splitByBlank(),
            TagType.Copyright.key to tag_string_copyright?.splitByBlank(),
            TagType.General.key to tag_string_general?.splitByBlank(),
            TagType.Meta.key to tag_string_meta?.splitByBlank()
        )
    )

    override fun info() = Info(
        uploader_id,
        { uploader_name },
        { null },
        null,
        pTitle,
        Triple(true, null, null),
        true to null,
        false to fav_count.toString(),
        false to score.toString(),
        false to rating,
        { c ->
            buildString {
                image_height?.let { h ->
                    image_width?.let { w ->
                        append(c.getString(R.string.fmt_post_info_size).format(h, w))
                        append("\n\n")
                    }
                }
                file_ext.let {
                    append(c.getString(R.string.fmt_post_info_ext).format(it))
                    append("\n\n")
                }
                md5?.let {
                    append(c.getString(R.string.fmt_post_info_md5).format(it))
                    append("\n\n")
                }
                file_size?.let {
                    append(
                        c.getString(R.string.fmt_post_info_file_size)
                            .format(Formatter.formatFileSize(c, it))
                    )
                    append("\n\n")
                }
                file_url?.let {
                    append(c.getString(R.string.fmt_post_info_file_url).format(it))
                    append("\n\n")
                }
                children_ids?.let {
                    append(c.getString(R.string.fmt_post_info_children).format(it))
                    append("\n\n")
                }
                parent_id?.let {
                    append(c.getString(R.string.fmt_post_info_parent).format(it))
                    append("\n\n")
                }
                source?.let {
                    append(c.getString(R.string.fmt_post_info_source).format(it))
                    append("\n\n")
                }
            }
        },
        dateFormatter.format(formatter.parse(created_at))
    )

    override fun downloads(postNameFmt: String) =
        file_url?.let {
            listOf(
                Download(
                    it, pWrapper.booru.folder, pWrapper.dependencyTag,
                    assignAttributes(postNameFmt, pWrapper.booru.name, id, file_ext, tag_string)
                )
            )
        }

    override fun message(): Nothing? = null

    companion object {
        private val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    }
}