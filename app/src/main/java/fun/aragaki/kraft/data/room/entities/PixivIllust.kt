package `fun`.aragaki.kraft.data.room.entities

import `fun`.aragaki.kraft.data.room.PostConverters
import `fun`.aragaki.kraft.data.servicewrappers.TagType
import androidx.room.Entity
import androidx.room.TypeConverters
import java.util.*
import `fun`.aragaki.kraft.data.entities.PixivIllustrationResponse.PixivIllustration as Response

@Entity(tableName = "PixivIllusts", primaryKeys = ["pixivId", "id"])
@TypeConverters(PostConverters::class)
data class PixivIllust(
    val pixivId: Int,
    val id: Long,
    val tags: List<String>?,

    val xRestrict: Int?,
    val time: Long
) {
    companion object {
        operator fun invoke(pixivId: Int, post: Response): PixivIllust {
            return PixivIllust(
                pixivId, post.id,
                post.getTags()[TagType.Undefined.key],
                post.x_restrict,
                Date().time
            )
        }
    }
}