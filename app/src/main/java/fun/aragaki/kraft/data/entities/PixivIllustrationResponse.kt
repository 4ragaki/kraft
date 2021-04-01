package `fun`.aragaki.kraft.data.entities

import `fun`.aragaki.kraft.R
import `fun`.aragaki.kraft.data.servicewrappers.BooruWrapper
import `fun`.aragaki.kraft.data.servicewrappers.TagType
import `fun`.aragaki.kraft.ext.dateFormatter
import `fun`.aragaki.kraft.ext.extension
import `fun`.aragaki.kraft.ext.joinNoNull
import java.text.SimpleDateFormat

data class PixivIllustrationResponse(
    val illust: PixivIllustration?
) {
    data class PixivIllustration(
        val caption: String?,
        val create_date: String?,
        val height: Int?,
        val id: Long,
        val image_urls: ImageUrls?,
        val is_bookmarked: Boolean?,
        val is_muted: Boolean?,
        val meta_pages: List<MetaPage?>?,
        val meta_single_page: MetaSinglePage?,
        val page_count: Int?,
        val restrict: Int?,
        val sanity_level: Int?,
        val series: Series?,
        val tags: List<Tag>?,
        val title: String?,
        val tools: List<Any?>?,
        val total_bookmarks: Int?,
        val total_comments: Int?,
        val total_view: Int?,
        val type: String?,
        val user: User?,
        val visible: Boolean?,
        val width: Int?,
        val x_restrict: Int?
    ) : Post() {
        override lateinit var pWrapper: BooruWrapper
        override lateinit var pTitle: String

        override fun attachContext(wrapper: BooruWrapper) = apply {
            pWrapper = wrapper
            pTitle = "${wrapper.booru.name} $id"
        }

        fun getTags() = mapOf(TagType.Undefined.key to tags?.map {
            listOf(it.name, it.translated_name).joinNoNull("/")
        })

        override fun postId() = id
        override fun postPreview() = image_urls?.medium

        override fun preview(): Preview {
            val urls =
                if (meta_pages?.isNotEmpty() == true) meta_pages.map { it?.image_urls?.large!! }
                else listOf(image_urls?.large)
            return Preview(urls, pWrapper.dependencyTag, info(), getTags())
        }

        override fun info() = Info(
            id,
            { user?.name },
            { user?.profile_image_urls?.medium },
            user?.is_followed,
            title,
            true to caption,
            false to total_view.toString(),
            false to total_bookmarks.toString(),
            true to null,
            true to null,
            {
                buildString {
                    height?.let { h ->
                        width?.let { w ->
                            append(it(R.string.fmt_post_info_size).format(h, w))
                            append("\n\n")
                        }
                    }
                }
            }, dateFormatter.format(formatter.parse(create_date))
        )

        override fun downloads(postNameFmt: String) =
            meta_pages?.mapIndexed { index, it ->
                val filename = assignAttributes(
                    postNameFmt, pWrapper.booru.name, id,
                    it?.image_urls?.original?.extension!!, part = index, tags = joinTags()
                )
                Download(
                    it.image_urls.original, pWrapper.booru.folder, pWrapper.dependencyTag, filename
                )
            }?.takeIf { it.isNotEmpty() } ?: meta_single_page?.original_image_url?.let {
                val filename = assignAttributes(
                    postNameFmt, pWrapper.booru.name, id, it.extension, joinTags()
                )
                listOf(Download(it, pWrapper.booru.folder, pWrapper.dependencyTag, filename))
            }

        override fun message(): Nothing? = null

        private fun joinTags() = tags?.joinToString(" ") { tag -> tag.name }

        class ImageUrls(
            val large: String?,
            val medium: String?,
            val square_medium: String?
        )

        data class MetaPage(
            val image_urls: ImageUrls?
        ) {
            data class ImageUrls(
                val large: String?,
                val medium: String?,
                val original: String?,
                val square_medium: String?
            )
        }

        class MetaSinglePage(
            val original_image_url: String?
        )

        data class Series(
            val id: Int?,
            val title: String?
        )

        data class Tag(
            val name: String,
            val translated_name: String?
        )

        data class User(
            val account: String?,
            val id: Long?,
            val is_followed: Boolean?,
            val name: String?,
            val profile_image_urls: ProfileImageUrls?
        ) {
            data class ProfileImageUrls(
                val medium: String?
            )
        }
    }

    companion object {
        private val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sssZ")
    }
}