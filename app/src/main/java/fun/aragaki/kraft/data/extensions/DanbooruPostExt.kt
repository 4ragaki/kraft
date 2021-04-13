package `fun`.aragaki.kraft.data.extensions

import `fun`.aragaki.kraft.R
import `fun`.aragaki.kraft.data.entities.DanbooruPost
import `fun`.aragaki.kraft.data.servicewrappers.BooruWrapper
import `fun`.aragaki.kraft.data.servicewrappers.TagType
import `fun`.aragaki.kraft.extensions.dateFormatter
import `fun`.aragaki.kraft.extensions.splitByBlank
import android.text.format.Formatter
import java.text.SimpleDateFormat


fun DanbooruPost.extAttachContext(wrapper: BooruWrapper) = apply {
    pWrapper = wrapper
    pTitle = "${wrapper.booru.name} $id"
}


fun DanbooruPost.extPreview() = Post.Preview(
    listOf(large_file_url), pWrapper.dependencyTag, info(),
    mapOf(
        TagType.Artist.key to tag_string_artist?.splitByBlank(),
        TagType.Character.key to tag_string_character?.splitByBlank(),
        TagType.Copyright.key to tag_string_copyright?.splitByBlank(),
        TagType.General.key to tag_string_general?.splitByBlank(),
        TagType.Meta.key to tag_string_meta?.splitByBlank()
    )
)


fun DanbooruPost.extInfo() = Post.Info(
    uploader_id, { uploader_name }, { null }, is_favorited,
    null, pTitle, Triple(true, null, null),
    true to null, false to fav_count.toString(),
    false to score.toString(), false to rating,
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


fun DanbooruPost.extDownload(postNameFmt: String) = file_url?.let {
    listOf(
        Post.Download(
            it, pWrapper.booru.folder, pWrapper.dependencyTag,
            assignAttributes(postNameFmt, pWrapper.booru.name, id, file_ext, tag_string)
        )
    )
}


private val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")