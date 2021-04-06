package `fun`.aragaki.kraft.data.room.entities

import `fun`.aragaki.kraft.data.entities.GelbooruPostResponse
import `fun`.aragaki.kraft.data.room.PostConverters
import `fun`.aragaki.kraft.extensions.splitByBlank
import androidx.room.Entity
import androidx.room.TypeConverters
import java.util.*

@Entity(tableName = "GelbooruPosts", primaryKeys = ["gelbooruId", "id"])
@TypeConverters(PostConverters::class)
data class GelbooruPost(

    val gelbooruId: Int,
    val id: Long,

    val tags: List<String>?,
    val rating: String?,
    val time: Long
) {
    companion object {
        operator fun invoke(
            gelbooruId: Int, post: GelbooruPostResponse.GelbooruPost
        ): GelbooruPost {
            return GelbooruPost(
                gelbooruId, post.id, post.tags.splitByBlank(), post.rating, Date().time
            )
        }
    }
}