package `fun`.aragaki.kraft.data.entities

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class MoebooruNote(
    val body: String?,
    val created_at: String?,
    val creator_id: Int?,
    val height: Int?,
    val id: Int?,
    val is_active: Boolean?,
    val post_id: Int?,
    val updated_at: String?,
    val version: Int?,
    val width: Int?,
    val x: Int?,
    val y: Int?
)