package `fun`.aragaki.kraft.data.entities

import `fun`.aragaki.kraft.data.servicewrappers.BooruWrapper
import `fun`.aragaki.kraft.data.servicewrappers.TagType
import `fun`.aragaki.kraft.ext.extension
import `fun`.aragaki.kraft.ext.splitByBlank
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
    data class GelbooruPost(
        @Attribute(name = "id")
        val id: Long,
        @Attribute(name = "width")
        val width: Int,
        @Attribute(name = "height")
        val height: Int,
        @Attribute(name = "score")
        val score: String,
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
        val creatorId: Int,
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
        override lateinit var pWrapper: BooruWrapper
        override lateinit var pTitle: String

        override fun attachContext(wrapper: BooruWrapper) = apply {
            pWrapper = wrapper
            pTitle = "${wrapper.booru.name} $id"
        }

        override fun postId() = id
        override fun postPreview() = previewUrl

        override fun preview() = Preview(
            listOf(sampleUrl), pWrapper.dependencyTag, null,
            mapOf(TagType.Undefined.key to tags.splitByBlank()), createdAt
        )

        override fun downloads(postNameFmt: String) = fileUrl?.let {
            listOf(
                Download(
                    it, pWrapper.booru.folder, pWrapper.dependencyTag,
                    assignAttributes(postNameFmt, pWrapper.booru.name, id, fileUrl.extension, tags)
                )
            )
        }

        override fun message(): Nothing? = null
    }
}