package `fun`.aragaki.kraft.data.room.entities

import `fun`.aragaki.kraft.data.room.PostConverters
import `fun`.aragaki.kraft.data.servicewrappers.TagType
import androidx.room.Entity
import androidx.room.TypeConverters
import java.util.*
import `fun`.aragaki.kraft.data.entities.SankakuPost as Response

@Entity(tableName = "SankakuPosts", primaryKeys = ["sankakuId", "id"])
@TypeConverters(PostConverters::class)
data class SankakuPost(

    val sankakuId: Int,
    val id: Long,

    val general: List<String>?,
    val artist: List<String>?,
    val studio: List<String>?,
    val copyright: List<String>?,
    val character: List<String>?,
    val genre: List<String>?,
    val medium: List<String>?,
    val meta: List<String>?,
    val undefined: List<String>?,

    val rating: String?,

    val time: Long
) {
    companion object {
        operator fun invoke(sankakuId: Int, post: Response): SankakuPost {
            val tags = post.getTags()
            return SankakuPost(
                sankakuId,
                post.id,
                tags[TagType.General.key],
                tags[TagType.Artist.key],
                tags[TagType.Studio.key],
                tags[TagType.Copyright.key],
                tags[TagType.Character.key],
                tags[TagType.Genre.key],
                tags[TagType.Medium.key],
                tags[TagType.Meta.key],
                tags[TagType.Undefined.key],
                post.rating,
                Date().time
            )
        }
    }
}