package `fun`.aragaki.kraft.data.entities

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Xml

@Xml
data class GelbooruCommentsResponse(
    @Attribute(name = "type")
    val type: String?,
    @Element(name = "comment")
    val comments: List<GelbooruComment>?
) {
    @Xml
    data class GelbooruComment(
        @Attribute(name = "created_at")
        val created_at: String?,
        @Attribute(name = "post_id")
        val post_id: String,
        @Attribute(name = "body")
        val body: String,
        @Attribute(name = "creator")
        val creator: String,
        @Attribute(name = "id")
        val id: String,
        @Attribute(name = "creator_id")
        val creator_id: String
    )
}