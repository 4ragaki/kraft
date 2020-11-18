package `fun`.aragaki.kraft.data.entities


import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class PixivErrorResponse(
    val error: Error?
) {
    @Keep
    @Serializable
    data class Error(
        val message: String?,
        val reason: String?,
        val user_message: String?,
        val user_message_details: UserMessageDetails?
    ) {
        @Keep
        @Serializable
        class UserMessageDetails(
        )
    }
}