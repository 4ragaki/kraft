package `fun`.aragaki.kraft.data.servicewrappers

import `fun`.aragaki.kraft.data.extensions.Post
import `fun`.aragaki.kraft.data.services.SankakuService
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory

class SankakuWrapper(
    override val booru: Boorus.Sankaku, client: OkHttpClient,
    converter: GsonConverterFactory, override val dependencyTag: String? = null
) : BooruWrapper, BooruWrapper.Taggable, BooruWrapper.Popular {
    override val safeTag = "rating:safe"
    override val service = SankakuService(client, converter, "${booru.scheme}://${booru.host}")

    override suspend fun post(id: Long) =
        listOf(service.id(id).attachContext(this@SankakuWrapper))

    override suspend fun tags(tags: String?, page: Int) =
        service.posts(tags, page).map { it.attachContext(this) }

    override suspend fun popular(param: String): List<Post> {
        return service.posts("order:popular", 1)
    }
}