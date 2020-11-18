package `fun`.aragaki.kraft.data.room.entities

import `fun`.aragaki.kraft.data.room.PostConverters
import `fun`.aragaki.kraft.ext.splitByBlank
import androidx.room.Entity
import androidx.room.TypeConverters
import java.util.*
import `fun`.aragaki.kraft.data.entities.MoebooruPost as Response

@Entity(tableName = "MoebooruPosts", primaryKeys = ["moebooruId", "id"])
@TypeConverters(PostConverters::class)
data class MoebooruPost(

    val moebooruId: Int,
    val id: Long,

    val tags: List<String>?,
    val rating: String?,
    val time: Long
) {
    companion object {
        operator fun invoke(moebooruId: Int, post: Response): MoebooruPost {
            return MoebooruPost(
                moebooruId, post.id, post.tags.splitByBlank(), post.rating, Date().time
            )
        }
    }
}