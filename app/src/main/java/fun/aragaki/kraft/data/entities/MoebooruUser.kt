package `fun`.aragaki.kraft.data.entities

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class MoebooruUser(
    val blacklisted_tags: List<String?>?,
    val id: Int?,
    val name: String?
)