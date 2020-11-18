package `fun`.aragaki.kraft.data.extensions

import `fun`.aragaki.kraft.R
import `fun`.aragaki.kraft.data.entities.MoebooruPost
import `fun`.aragaki.kraft.data.servicewrappers.BooruWrapper
import `fun`.aragaki.kraft.data.TagType
import `fun`.aragaki.kraft.extensions.dateFormatter
import `fun`.aragaki.kraft.extensions.extension
import `fun`.aragaki.kraft.extensions.splitByBlank
import android.text.format.Formatter


fun MoebooruPost.extAttachContext(wrapper: BooruWrapper) = apply {
    booruId = wrapper.booru.booruId
}


fun MoebooruPost.extPreview(wrapper: BooruWrapper) = Post.Preview(
    sample_url?.let { listOf(it) }?: emptyList(), wrapper.dependencyTag,
    info(wrapper), mapOf(TagType.Undefined to tags.splitByBlank())
)


fun MoebooruPost.extInfo(wrapper: BooruWrapper) = Post.Info(
    creator_id, { author }, { null }, null,
    null, "${wrapper.booru.name} $id", Triple(true, null, null),
    true to null, true to null,
    false to score.toString(), false to rating,
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


fun MoebooruPost.extDownloads(wrapper: BooruWrapper, postNameFmt: String) = file_url?.let {
    listOf(
        Post.Download(
            it, wrapper.booru.folder, wrapper.dependencyTag,
            assignAttributes(
                postNameFmt, wrapper.booru.name, id,
                file_ext ?: file_url.extension, tags
            )
        )
    )
}