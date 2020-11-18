package `fun`.aragaki.kraft.data.entities

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Xml

@Xml
data class GelbooruTagsResponse(
    @Element(name = "tag")
    val tags: List<GelbooruTag>?
) {
    @Xml
    data class GelbooruTag(
        @Attribute(name = "type")
        val type: String?,
        @Attribute(name = "count")
        val count: String,
        @Attribute(name = "name")
        val name: String,
        @Attribute(name = "ambiguous")
        val ambiguous: String,
        @Attribute(name = "id")
        val id: String
    )
}