package `fun`.aragaki.kraft.data.entities

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Xml

@Xml
data class GelbooruDeletedPostsResponse(
    @Element(name = "post")
    val posts: List<GelbooruDeletedPost>?
) {
    @Xml
    data class GelbooruDeletedPost(
        @Attribute(name = "deleted")
        val deleted: String?,
        @Attribute(name = "md5")
        val md5: String
    )
}