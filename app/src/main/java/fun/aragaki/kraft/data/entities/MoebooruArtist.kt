package `fun`.aragaki.kraft.data.entities

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class MoebooruArtist(
    val alias_id: Int?,
    val group_id: Int?,
    val id: Int?,
    val name: String?,
    val urls: List<String?>?
)