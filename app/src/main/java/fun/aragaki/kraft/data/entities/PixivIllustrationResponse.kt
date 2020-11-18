package `fun`.aragaki.kraft.data.entities

import `fun`.aragaki.kraft.data.extensions.*
import `fun`.aragaki.kraft.data.room.PostConverters
import `fun`.aragaki.kraft.data.servicewrappers.BooruWrapper
import androidx.annotation.Keep
import androidx.room.*
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class PixivIllustrationResponse(
    val illust: PixivIllustration?
) {
    @Keep
    @Serializable
    @Entity(tableName = "PixivIllusts")
    @TypeConverters(PostConverters::class)
    data class PixivIllustration(
        val caption: String?,
        val create_date: String?,
        val height: Int?,
        @PrimaryKey
        val id: Long,
        @Embedded
        val image_urls: ImageUrls?,
        val is_bookmarked: Boolean?,
        val is_muted: Boolean?,
        val meta_pages: List<MetaPage?>?,
        @Embedded
        val meta_single_page: MetaSinglePage?,
        val page_count: Int?,
        val restrict: Int?,
        val sanity_level: Int?,
        @Embedded
        val series: Series?,
        val tags: List<Tag>?,
        val title: String?,
        val tools: List<String?>?,
        val total_bookmarks: Int?,
        val total_comments: Int?,
        val total_view: Int?,
        val type: String?,
        @Embedded
        val user: User?,
        val visible: Boolean?,
        val width: Int?,
        val x_restrict: Int?
    ) : Post() {

        @Keep
        @Serializable
        class ImageUrls(
            val large: String?,
            val medium: String?,
            val square_medium: String?
        )

        @Keep
        @Serializable
        data class MetaPage(
            @Embedded
            val image_urls: ImageUrls?
        ) {
            @Keep
            @Serializable
            data class ImageUrls(
                val large: String?,
                val medium: String?,
                val original: String?,
                val square_medium: String?
            )
        }

        @Keep
        @Serializable
        class MetaSinglePage(
            val original_image_url: String?
        )

        @Keep
        @Serializable
        data class Series(
            @ColumnInfo(name = "series_id")
            val id: Int?,
            @ColumnInfo(name = "series_title")
            val title: String?
        )

        @Keep
        @Serializable
        data class Tag(
            val name: String,
            val translated_name: String?
        )

        @Keep
        @Serializable
        data class User(
            val account: String?,
            @ColumnInfo(name = "user_id")
            val id: Long?,
            val is_followed: Boolean?,
            val name: String?,
            @Embedded
            val profile_image_urls: ProfileImageUrls?
        ) {
            @Keep
            @Serializable
            data class ProfileImageUrls(
                @ColumnInfo(name = "profile_image_url_medium")
                val medium: String?
            )
        }

        override fun postId() = id
        override fun postPreview() = image_urls?.medium
        override fun preview(wrapper: BooruWrapper) = extPreview(wrapper)
        override fun info(wrapper: BooruWrapper) = extInfo(wrapper)
        override fun downloads(wrapper: BooruWrapper, postNameFmt: String) =
            extDownloads(wrapper, postNameFmt)

        override fun message(): Nothing? = null

        override fun attachContext(wrapper: BooruWrapper) = extAttachContext(wrapper)
    }
}