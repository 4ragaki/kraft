package `fun`.aragaki.kraft.data.extensions

import `fun`.aragaki.kraft.R
import `fun`.aragaki.kraft.data.entities.PixivIllustrationResponse
import `fun`.aragaki.kraft.data.servicewrappers.BooruWrapper
import `fun`.aragaki.kraft.data.servicewrappers.TagType
import `fun`.aragaki.kraft.extensions.dateFormatter
import `fun`.aragaki.kraft.extensions.extension
import `fun`.aragaki.kraft.extensions.joinNoNull
import java.text.SimpleDateFormat


fun PixivIllustrationResponse.PixivIllustration.extAttachContext(wrapper: BooruWrapper) = apply {
    pWrapper = wrapper
    pTitle = "${wrapper.booru.name} $id"
}


fun PixivIllustrationResponse.PixivIllustration.extPreview(): Post.Preview {
    val urls =
        if (meta_pages?.isNotEmpty() == true) meta_pages.map { it?.image_urls?.large!! }
        else listOf(image_urls?.large)
    return Post.Preview(urls, pWrapper.dependencyTag, info(), getTags())
}


fun PixivIllustrationResponse.PixivIllustration.extInfo() = Post.Info(
    id,
    { user?.name },
    { user?.profile_image_urls?.medium },
    user?.is_followed,
    title,
    Triple(false, second = true, third = caption),
    false to total_view.toString(),
    false to total_bookmarks.toString(),
    true to null,
    true to null,
    { c ->
        buildString {
            height?.let { h ->
                width?.let { w ->
                    append(c.getString(R.string.fmt_post_info_size).format(h, w))
                    append("\n\n")
                }
            }
        }
    }, dateFormatter.format(formatter.parse(create_date))
)


fun PixivIllustrationResponse.PixivIllustration.extDownloads(postNameFmt: String) =
    meta_pages?.mapIndexed { index, it ->
        val filename = assignAttributes(
            postNameFmt, pWrapper.booru.name, id,
            it?.image_urls?.original?.extension!!, part = index, tags = joinTags()
        )
        Post.Download(
            it.image_urls.original, pWrapper.booru.folder, pWrapper.dependencyTag, filename
        )
    }?.takeIf { it.isNotEmpty() } ?: meta_single_page?.original_image_url?.let {
        val filename = assignAttributes(
            postNameFmt, pWrapper.booru.name, id, it.extension, joinTags()
        )
        listOf(Post.Download(it, pWrapper.booru.folder, pWrapper.dependencyTag, filename))
    }


private fun PixivIllustrationResponse.PixivIllustration.joinTags() =
    tags?.joinToString(" ") { tag -> tag.name }

fun PixivIllustrationResponse.PixivIllustration.getTags() =
    mapOf(TagType.Undefined.key to tags?.map {
        listOf(it.name, it.translated_name).joinNoNull("/")
    })

private val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sssZ")