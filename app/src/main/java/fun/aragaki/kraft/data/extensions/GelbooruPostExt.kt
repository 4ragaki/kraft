package `fun`.aragaki.kraft.data.extensions

import `fun`.aragaki.kraft.R
import `fun`.aragaki.kraft.data.entities.GelbooruPostResponse
import `fun`.aragaki.kraft.data.servicewrappers.BooruWrapper
import `fun`.aragaki.kraft.data.servicewrappers.TagType
import `fun`.aragaki.kraft.extensions.extension
import `fun`.aragaki.kraft.extensions.splitByBlank
import java.text.SimpleDateFormat


fun GelbooruPostResponse.GelbooruPost.extAttachContext(wrapper: BooruWrapper) = apply {
    pWrapper = wrapper
    pTitle = "${wrapper.booru.name} $id"
}


fun GelbooruPostResponse.GelbooruPost.extPreview() = Post.Preview(
    listOf(sampleUrl), pWrapper.dependencyTag, info(),
    mapOf(TagType.Undefined.key to tags.splitByBlank())
)


fun GelbooruPostResponse.GelbooruPost.extInfo() = Post.Info(
    creatorId, { null }, { null },null,
    null, pTitle, Triple(true, null, null),
    true to null, true to null,
    false to score.toString(), false to rating,
    { c ->
        buildString {
            height.let { h ->
                width.let { w ->
                    append(c.getString(R.string.fmt_post_info_size).format(h, w))
                    append("\n\n")
                }
            }
            fileUrl?.let {
                append(c.getString(R.string.fmt_post_info_file_url).format(it))
                append("\n\n")
            }
            source.let {
                append(c.getString(R.string.fmt_post_info_source).format(source))
                append("\n\n")
            }
        }
    },
    createdAt
//            TODO WTF is this shit
//            dateFormatter.format(formatter.parse(createdAt))
)


fun GelbooruPostResponse.GelbooruPost.extDownloads(postNameFmt: String) = fileUrl?.let {
    listOf(
        Post.Download(
            it, pWrapper.booru.folder, pWrapper.dependencyTag,
            assignAttributes(postNameFmt, pWrapper.booru.name, id, fileUrl.extension, tags)
        )
    )
}

private val formatter = SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy")