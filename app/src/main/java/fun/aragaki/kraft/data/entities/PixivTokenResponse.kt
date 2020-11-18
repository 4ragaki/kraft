package `fun`.aragaki.kraft.data.entities


import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class PixivTokenResponse(
    val access_token: String,
    val expires_in: Int,
    val refresh_token: String,
    val response: Response,
    val scope: String,
    val token_type: String,
    val user: User
) {
    @Keep
    @Serializable
    data class Response(
        val access_token: String,
        val expires_in: Int,
        val refresh_token: String,
        val scope: String,
        val token_type: String,
        val user: User
    ) {
        @Keep
        @Serializable
        data class User(
            val account: String,
            val id: String,
            val is_mail_authorized: Boolean,
            val is_premium: Boolean,
            val mail_address: String,
            val name: String,
            val profile_image_urls: ProfileImageUrls,
            val require_policy_agreement: Boolean,
            val x_restrict: Int
        ) {
            @Keep
            @Serializable
            data class ProfileImageUrls(
                val px_16x16: String,
                val px_170x170: String,
                val px_50x50: String
            )
        }
    }

    @Keep
    @Serializable
    data class User(
        val account: String,
        val id: String,
        val is_mail_authorized: Boolean,
        val is_premium: Boolean,
        val mail_address: String,
        val name: String,
        val profile_image_urls: ProfileImageUrls,
        val require_policy_agreement: Boolean,
        val x_restrict: Int
    ) {
        @Keep
        @Serializable
        data class ProfileImageUrls(
            val px_16x16: String,
            val px_170x170: String,
            val px_50x50: String
        )
    }
}