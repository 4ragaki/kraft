package `fun`.aragaki.kraft.data.entities

import `fun`.aragaki.kraft.R
import `fun`.aragaki.kraft.data.servicewrappers.BooruWrapper
import `fun`.aragaki.kraft.data.servicewrappers.TagType
import `fun`.aragaki.kraft.ext.dateFormatter
import `fun`.aragaki.kraft.ext.extension
import `fun`.aragaki.kraft.ext.splitByBlank
import android.text.format.Formatter

data class MoebooruPost(
    val actual_preview_height: Int?,
    val actual_preview_width: Int?,
    val approver_id: Any?,
    val author: String?,
    val change: Int?,
    val created_at: Long?,
    val creator_id: Long?,
//    null on konachan.com
    val file_ext: String?,
    val file_size: Long?,
    val file_url: String?,
    val flag_detail: FlagDetail?,
    val frames: List<Any>?,
    val frames_pending: List<Any>?,
    val frames_pending_string: String?,
    val frames_string: String?,
    val has_children: Boolean?,
    val height: Int?,
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
    data class FlagDetail(
        val post_id: Long?,
        val reason: String?,
        val created_at: String?
    )

    override lateinit var pWrapper: BooruWrapper
    override lateinit var pTitle: String

    override fun attachContext(wrapper: BooruWrapper) = apply {
        pWrapper = wrapper
        pTitle = "${wrapper.booru.name} $id"
    }

    override fun postId() = id
    override fun postPreview() = preview_url

    override fun preview() = Preview(
        listOf(sample_url), pWrapper.dependencyTag,
        info(), mapOf(TagType.Undefined.key to tags.splitByBlank())
    )

    override fun info() = Info(
        creator_id,
        { author },
        { null },
        null,
        pTitle,
        Triple(true, null, null),
        true to null,
        true to null,
        false to score.toString(),
        false to rating,
        { c ->
            buildString {
                height?.let { h ->
                    width?.let { w ->
                        append(c.getString(R.string.fmt_post_info_size).format(h, w))
                        append("\n\n")
                    }
                }
                file_ext?.let {
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
        dateFormatter.format(created_at?.times(1000))
    )

    override fun downloads(postNameFmt: String) = file_url?.let {
        listOf(
            Download(
                it, pWrapper.booru.folder, pWrapper.dependencyTag,
                assignAttributes(
                    postNameFmt, pWrapper.booru.name, id,
                    file_ext ?: file_url.extension, tags
                )
            )
        )
    }

    override fun message() = flag_detail?.reason
}