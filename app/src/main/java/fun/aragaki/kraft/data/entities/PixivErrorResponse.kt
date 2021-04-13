package `fun`.aragaki.kraft.data.entities


import androidx.annotation.Keep

@Keep
data class PixivErrorResponse(
    val error: Error?
) {
    @Keep
    data class Error(
        val message: String?,
        val reason: String?,
        val user_message: String?,
        val user_message_details: UserMessageDetails?
    ) {
        @Keep
        class UserMessageDetails(
        )
    }
}