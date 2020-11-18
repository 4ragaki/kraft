package `fun`.aragaki.kraft.data.entities

import `fun`.aragaki.kraft.data.extensions.Tag
import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class MoebooruTag(
    val ambiguous: Boolean?,
    val count: Int?,
    val id: Long?,
    val name: String?,
    val type: Int?
) : Tag() {
    override fun tagGetName() = name

    override fun tagGetId() = id

    override fun tagGetType() = type
}