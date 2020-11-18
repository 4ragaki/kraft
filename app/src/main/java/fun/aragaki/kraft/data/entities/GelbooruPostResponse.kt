package `fun`.aragaki.kraft.data.entities

import `fun`.aragaki.kraft.data.extensions.*
import `fun`.aragaki.kraft.data.room.PostConverters
import `fun`.aragaki.kraft.data.servicewrappers.BooruWrapper
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Xml

@Xml
data class GelbooruPostResponse(
    @Attribute(name = "count")
    val count: Int,
    @Attribute(name = "offset")
    val offset: Int,
    @Element(name = "post")
    val posts: List<GelbooruPost>
) {
    @Xml
    @Entity(tableName = "GelbooruPosts")
    @TypeConverters(PostConverters::class)
    data class GelbooruPost(
        @PrimaryKey
        @Attribute(name = "id")
        val id: Long,
        @Attribute(name = "width")
        val width: Int,
        @Attribute(name = "height")
        val height: Int,
        @Attribute(name = "score")
        val score: Int,
        @Attribute(name = "file_url")
        val fileUrl: String?,
        @Attribute(name = "sample_url")
        val sampleUrl: String?,
        @Attribute(name = "sample_width")
        val sampleWidth: Int,
        @Attribute(name = "sample_height")
        val sampleHeight: Int,
        @Attribute(name = "preview_url")
        val previewUrl: String?,
        @Attribute(name = "rating")
        val rating: String,
        @Attribute(name = "tags")
        val tags: String,
        @Attribute(name = "creator_id")
        val creatorId: Long,
        @Attribute(name = "has_children")
        val hasChildren: Boolean,
        @Attribute(name = "created_at")
        val createdAt: String,
        @Attribute(name = "source")
        val source: String,
        @Attribute(name = "preview_width")
        val previewWidth: Int,
        @Attribute(name = "preview_height")
        val previewHeight: Int
    ) : Post() {
        override fun postId() = id
        override fun postPreview() = previewUrl
        override fun preview(wrapper: BooruWrapper) = extPreview(wrapper)
        override fun info(wrapper: BooruWrapper) = extInfo(wrapper)
        override fun downloads(wrapper: BooruWrapper, postNameFmt: String) =
            extDownloads(wrapper, postNameFmt)

        override fun message(): Nothing? = null
        override fun attachContext(wrapper: BooruWrapper) = extAttachContext(wrapper)
    }
}