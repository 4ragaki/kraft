package `fun`.aragaki.kraft.data.room.entities

import `fun`.aragaki.kraft.data.room.PostConverters
import `fun`.aragaki.kraft.ext.splitByBlank
import androidx.room.Entity
import androidx.room.TypeConverters
import java.util.*
import `fun`.aragaki.kraft.data.entities.DanbooruPost as Response

@Entity(tableName = "DanbooruPosts", primaryKeys = ["danbooruId", "id"])
@TypeConverters(PostConverters::class)
data class DanbooruPost(

    val danbooruId: Int,
    val id: Long,

    val artist: List<String>?,
    val character: List<String>?,
    val copyright: List<String>?,
    val general: List<String>?,
    val meta: List<String>?,

    val rating: String?,

    val time: Long
) {
    companion object {
        operator fun invoke(danbooruId: Int, post: Response): DanbooruPost {
            return DanbooruPost(
                danbooruId, post.id,
                post.tag_string_artist?.splitByBlank(),
                post.tag_string_character?.splitByBlank(),
                post.tag_string_copyright?.splitByBlank(),
                post.tag_string_general?.splitByBlank(),
                post.tag_string_meta?.splitByBlank(),
                post.rating, Date().time
            )
        }
    }
}