package `fun`.aragaki.kraft.data.servicewrappers

import `fun`.aragaki.kraft.data.services.GelbooruService
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import okhttp3.OkHttpClient

class GelbooruWrapper(
    override val booru: Boorus.Gelbooru, client: OkHttpClient,
    converter: TikXmlConverterFactory, override val dependencyTag: String? = null
) : BooruWrapper, BooruWrapper.Taggable {
    override val safeTag = "rating:safe"
    override val service = GelbooruService(client, converter, "${booru.scheme}://${booru.host}")

    override suspend fun post(id: Long) =
        service.postsList(id, null, null, null)
            .posts.onEach { it.attachContext(this@GelbooruWrapper) }

    //        "+sort%3ascore"
    override suspend fun tags(tags: String?, page: Int) =
        service.postsList(null, tags, page).posts.map { it.attachContext(this) }
}