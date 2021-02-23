package `fun`.aragaki.kraft.data.features

interface PixivToken {

    suspend fun pixivToken(verifier: String, code: String): Any

    suspend fun pixivTokenRefresh(refresh_token: String): Any
}