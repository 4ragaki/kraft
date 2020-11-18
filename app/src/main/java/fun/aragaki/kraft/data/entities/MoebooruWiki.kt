package `fun`.aragaki.kraft.data.entities

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class MoebooruWiki(
    val body: String?,
    val created_at: String?,
    val id: Int?,
    val locked: Boolean?,
    val title: String?,
    val updated_at: String?,
    val updater_id: Int?,
    val version: Int?
)