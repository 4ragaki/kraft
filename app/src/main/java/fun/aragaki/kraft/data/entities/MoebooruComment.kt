package `fun`.aragaki.kraft.data.entities

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class MoebooruComment(
    val body: String?,
    val created_at: String?,
    val creator: String?,
    val creator_id: Int?,
    val id: Int?,
    val post_id: Int?
)