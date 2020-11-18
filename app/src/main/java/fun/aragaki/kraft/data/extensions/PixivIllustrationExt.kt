package `fun`.aragaki.kraft.data.extensions

import `fun`.aragaki.kraft.R
import `fun`.aragaki.kraft.data.entities.PixivIllustrationResponse
import `fun`.aragaki.kraft.data.servicewrappers.BooruWrapper
import `fun`.aragaki.kraft.data.TagType
import `fun`.aragaki.kraft.extensions.dateFormatter
import `fun`.aragaki.kraft.extensions.extension
import `fun`.aragaki.kraft.extensions.joinNoNull
import java.text.SimpleDateFormat


fun PixivIllustrationResponse.PixivIllustration.extAttachContext(wrapper: BooruWrapper) = apply {
    booruId = wrapper.booru.booruId
}


fun PixivIllustrationResponse.PixivIllustration.extPreview(wrapper: BooruWrapper): Post.Preview {
    val urls =
        if (meta_pages?.isNotEmpty() == true) meta_pages.map { it?.image_urls?.large!! }
        else image_urls?.large?.let { listOf(it) } ?: emptyList()
    return Post.Preview(urls, wrapper.dependencyTag, info(wrapper), getTags())
}


fun PixivIllustrationResponse.PixivIllustration.extInfo(wrapper: BooruWrapper) = Post.Info(
    user?.id, { user?.name }, { user?.profile_image_urls?.medium }, is_bookmarked,
    user?.is_followed, title, Triple(false, second = true, third = caption),
    false to total_view.toString(), false to total_bookmarks.toString(),
    true to null, true to null,
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


fun PixivIllustrationResponse.PixivIllustration.extDownloads(
    wrapper: BooruWrapper,
    postNameFmt: String
) =
    meta_pages?.mapIndexed { index, it ->
        val filename = assignAttributes(
            postNameFmt, wrapper.booru.name, id,
            it?.image_urls?.original?.extension!!, part = index, tags = joinTags()
        )
        Post.Download(
            it.image_urls.original, wrapper.booru.folder, wrapper.dependencyTag, filename
        )
    }?.takeIf { it.isNotEmpty() } ?: meta_single_page?.original_image_url?.let {
        val filename = assignAttributes(
            postNameFmt, wrapper.booru.name, id, it.extension, joinTags()
        )
        listOf(Post.Download(it, wrapper.booru.folder, wrapper.dependencyTag, filename))
    }


private fun PixivIllustrationResponse.PixivIllustration.joinTags() =
    tags?.joinToString(" ") { tag -> tag.name }

fun PixivIllustrationResponse.PixivIllustration.getTags() =
    mapOf(TagType.Undefined to tags?.map {
        listOf(it.name, it.translated_name).joinNoNull("/")
    })

private val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sssZ")