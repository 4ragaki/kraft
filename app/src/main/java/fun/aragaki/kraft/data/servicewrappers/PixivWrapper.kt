package `fun`.aragaki.kraft.data.servicewrappers

import `fun`.aragaki.kraft.data.CredentialException
import `fun`.aragaki.kraft.data.entities.PixivRankResponse
import `fun`.aragaki.kraft.data.entities.PixivTokenResponse
import `fun`.aragaki.kraft.data.services.PixivAuth
import `fun`.aragaki.kraft.data.services.PixivService
import `fun`.aragaki.kraft.extensions.toBearer
import okhttp3.OkHttpClient
import retrofit2.HttpException
import retrofit2.converter.gson.GsonConverterFactory

class PixivWrapper(
    override val booru: Boorus.Pixiv, client: OkHttpClient,
    converter: GsonConverterFactory, override val dependencyTag: String?
) : BooruWrapper, BooruWrapper.Taggable, BooruWrapper.Votable, BooruWrapper.Followable {
    //TODO
    override val safeTag = ""
    private val auth = PixivAuth(client, converter, "${booru.authScheme}://${booru.authHost}")
    override val service = PixivService(client, converter, "${booru.scheme}://${booru.host}")

    private suspend fun updateToken(): String {
        kotlin.runCatching {
            booru.refreshToken?.let { if (it.isNotBlank()) return applyToken(pixivTokenRefresh(it)) }
        }.onFailure { it.printStackTrace() }
        throw CredentialException.PixivCredentialEmptyException
    }

    override suspend fun post(id: Long) = coroutineRetry {
        service.getIllustByID(getAccessToken(), id).illust
            ?.attachContext(this@PixivWrapper)?.let { listOf(it) } ?: emptyList()
    }

    //    TODO correct
    override suspend fun tags(tags: String?, page: Int) =
        service.search(getAccessToken(), tags, 0).illusts.map { it.attachContext(this) }

    suspend fun pixivSearch(word: String, offset: Long): PixivRankResponse {
        return coroutineRetry {
            service.search(getAccessToken().toBearer(), word, offset)
        }
    }

    override suspend fun vote(positive: Boolean, postId: Long) = if (positive) {
        service.bookmarkAdd(getAccessToken(), postId)
        true
    } else {
        service.bookmarkDelete(getAccessToken(), postId)
        false
    }

    override suspend fun follow(positive: Boolean, userId: Long) = if (positive) {
        service.follow(getAccessToken(), userId)
        true
    } else {
        service.unfollow(getAccessToken(), userId)
        false
    }

    suspend fun pixivToken(verifier: String, code: String): PixivTokenResponse {
        return auth.login(verifier, code)
    }

    private suspend fun pixivTokenRefresh(refresh_token: String): PixivTokenResponse {
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

    private suspend fun applyToken(token: PixivTokenResponse): String {
        booru.accessToken = token.response.access_token
        booru.refreshToken = token.response.refresh_token
        booru.tokenCallback(token.response.access_token, token.response.refresh_token)
        return token.response.access_token
    }
}