package `fun`.aragaki.kraft.data.entities

import `fun`.aragaki.kraft.data.extensions.*
import `fun`.aragaki.kraft.data.servicewrappers.BooruWrapper

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

        override lateinit var pWrapper: BooruWrapper
        override lateinit var pTitle: String
        override fun attachContext(wrapper: BooruWrapper) = extAttachContext(wrapper)
        override fun postId() = id
        override fun postPreview() = image_urls?.medium
        override fun preview() = extPreview()
        override fun info() = extInfo()
        override fun downloads(postNameFmt: String) = extDownloads(postNameFmt)
        override fun message(): Nothing? = null
    }
}