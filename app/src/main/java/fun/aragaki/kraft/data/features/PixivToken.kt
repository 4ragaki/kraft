package `fun`.aragaki.kraft.data.features

interface PixivToken {

    suspend fun pixivToken(id: String, credential: String): Any

    suspend fun pixivTokenRefresh(refresh_token: String): Any
}