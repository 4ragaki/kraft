package `fun`.aragaki.kraft.data.extensions

import `fun`.aragaki.kraft.R
import `fun`.aragaki.kraft.data.entities.DanbooruPost
import `fun`.aragaki.kraft.data.servicewrappers.BooruWrapper
import `fun`.aragaki.kraft.data.TagType
import `fun`.aragaki.kraft.extensions.dateFormatter
import `fun`.aragaki.kraft.extensions.splitByBlank
import android.text.format.Formatter
import java.text.SimpleDateFormat


fun DanbooruPost.extAttachContext(wrapper: BooruWrapper) = apply {
    booruId = wrapper.booru.booruId
}


fun DanbooruPost.extPreview(wrapper: BooruWrapper) = Post.Preview(
    large_file_url?.let { listOf(it) } ?: emptyList(), wrapper.dependencyTag, info(wrapper),
    mapOf(
        TagType.Artist to tag_string_artist?.splitByBlank(),
        TagType.Character to tag_string_character?.splitByBlank(),
        TagType.Copyright to tag_string_copyright?.splitByBlank(),
        TagType.General to tag_string_general?.splitByBlank(),
        TagType.Meta to tag_string_meta?.splitByBlank()
    )
)


fun DanbooruPost.extInfo(wrapper: BooruWrapper) = Post.Info(
    uploader_id, { uploader_name }, { null }, is_favorited,
    null, "${wrapper.booru.name} $id", Triple(true, null, null),
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


fun DanbooruPost.extDownload(wrapper: BooruWrapper, postNameFmt: String) = file_url?.let {
    listOf(
        Post.Download(
            it, wrapper.booru.folder, wrapper.dependencyTag,
            assignAttributes(postNameFmt, wrapper.booru.name, id, file_ext, tag_string)
        )
    )
}


private val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")