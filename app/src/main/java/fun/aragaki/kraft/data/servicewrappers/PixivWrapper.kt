package `fun`.aragaki.kraft.data.servicewrappers

import `fun`.aragaki.kraft.data.CredentialException
import `fun`.aragaki.kraft.data.entities.PixivRankResponse
import `fun`.aragaki.kraft.data.entities.PixivTokenResponse
import `fun`.aragaki.kraft.data.features.PixivSearch
import `fun`.aragaki.kraft.data.features.PixivToken
import `fun`.aragaki.kraft.data.features.Tags
import `fun`.aragaki.kraft.data.services.PixivAuth
import `fun`.aragaki.kraft.data.services.PixivService
import `fun`.aragaki.kraft.ext.toBearer
import okhttp3.OkHttpClient
import retrofit2.HttpException
import retrofit2.converter.gson.GsonConverterFactory

class PixivWrapper(
    override val booru: Boorus.Pixiv, client: OkHttpClient,
    converter: GsonConverterFactory, override val dependencyTag: String?
) : BooruWrapper, PixivToken, PixivSearch, Tags {
    private val auth = PixivAuth(client, converter, "${booru.oauthScheme}://${booru.oauthHost}")
    override val service = PixivService(client, converter, "${booru.scheme}://${booru.host}")

    private suspend fun updateToken(): String {
        kotlin.runCatching {
            booru.refreshToken?.let { if (it.isNotBlank()) return applyToken(pixivTokenRefresh(it)) }
        }.onFailure { it.printStackTrace() }
        checkAndRun { id, credential ->
            pixivToken(id, credential).also { return applyToken(it) }
        }
        throw CredentialException.PixivCredentialEmptyException
    }

    override suspend fun post(id: Long) = coroutineRetry {
        listOf(
            service.getIllustByID(getAccessToken(), id).illust
                ?.attachContext(this@PixivWrapper)
        )
    }

    //    TODO correct
    override suspend fun tags(tags: String?, page: Int) =
        service.search(getAccessToken(), tags, 0).illusts.map { it.attachContext(this) }

    override suspend fun pixivSearch(word: String, offset: Long): PixivRankResponse {
        return coroutineRetry {
            service.search(getAccessToken().toBearer(), word, offset)
        }
    }

    override suspend fun pixivToken(id: String, credential: String): PixivTokenResponse {
        return auth.login(id, credential)
    }

    override suspend fun pixivTokenRefresh(refresh_token: String): PixivTokenResponse {
        return auth.refresh(refresh_token)
    }

    private suspend inline fun <R> coroutineRetry(block: () -> R): R {
        kotlin.runCatching {
            return block()
        }.onFailure {
            if (it is HttpException && it.code() == 400) {
                updateToken()
                return block()
            } else {
                throw it
            }
        }
        throw Exception("service error.")
    }

    private suspend fun getAccessToken() =
        (booru.accessToken.takeIf { it?.isNotBlank() == true } ?: updateToken()).toBearer()

    private inline fun checkAndRun(block: (id: String, credential: String) -> Unit) {
        booru.id?.takeIf { it.isNotBlank() }?.let { id ->
            booru.credential?.takeIf { it.isNotEmpty() }
                ?.let { credential -> block(id, credential) }
        }
    }

    private suspend fun applyToken(token: PixivTokenResponse): String {
        booru.accessToken = token.response.access_token
        booru.refreshToken = token.response.refresh_token
        booru.tokenCallback(token.response.access_token, token.response.refresh_token)
        return token.response.access_token
    }
}