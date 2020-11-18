package `fun`.aragaki.kraft.data.entities

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class MoebooruForum(
    val body: String?,
    val creator: String?,
    val creator_id: Int?,
    val id: Int?,
    val pages: Int?,
    val parent_id: String?,
    val title: String?,
    val updated_at: String?
)