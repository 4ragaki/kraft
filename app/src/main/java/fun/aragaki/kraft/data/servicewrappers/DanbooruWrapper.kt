package `fun`.aragaki.kraft.data.servicewrappers

import `fun`.aragaki.kraft.data.services.DanbooruService
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class DanbooruWrapper(
    override val booru: Boorus.Danbooru, client: OkHttpClient,
    converter: GsonConverterFactory, override val dependencyTag: String? = null
) : BooruWrapper, BooruWrapper.Taggable, BooruWrapper.Popular {
    override val safeTag = "rating:safe"

    override val service = DanbooruService(client, converter, "${booru.scheme}://${booru.host}")

    override suspend fun post(id: Long) =
        listOf(service.show(id).attachContext(this@DanbooruWrapper))

    override suspend fun tags(tags: String?, page: Int) = service.posts(
        tags, null, null, null, null, page
    ).map { it.attachContext(this) }

    //        "day" "week" "month"
    override suspend fun popular(param: String) =
        service.popular(generatePopularDateParam(), param).map { it.attachContext(this) }


    //        date=2020-01-10+22%3A08%3A43+-0500
    //            2020-01-03 22:02:49 -0500
    private fun generatePopularDateParam(): String {
        return SimpleDateFormat("yyyy-MM-dd+hh%3Amm%3Ass+-0500", Locale.US).format(Date())
    }
}