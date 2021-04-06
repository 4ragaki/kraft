package `fun`.aragaki.kraft.data.servicewrappers

import `fun`.aragaki.kraft.data.extensions.Post
import `fun`.aragaki.kraft.data.features.Popular
import `fun`.aragaki.kraft.data.features.Tags
import `fun`.aragaki.kraft.data.services.MoebooruService
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory

class MoebooruWrapper(
    override val booru: Boorus.Moebooru, client: OkHttpClient,
    converter: GsonConverterFactory, override val dependencyTag: String? = null
) : BooruWrapper, Tags, Popular {
    override val service = MoebooruService(client, converter, "${booru.scheme}://${booru.host}")

    override suspend fun post(id: Long) =
        service.postsList("id:$id").onEach { it.attachContext(this@MoebooruWrapper) }

    override suspend fun tags(tags: String?, page: Int) =
        service.postsList(tags, null, page).map { it.attachContext(this) }

    override suspend fun popular(param: String): List<Post> {
//        "1d" "1w" "1m" "1y"
        return service.popular(param)
    }
}