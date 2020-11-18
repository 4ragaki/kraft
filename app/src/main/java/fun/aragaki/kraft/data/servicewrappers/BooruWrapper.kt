package `fun`.aragaki.kraft.data.servicewrappers

import `fun`.aragaki.kraft.data.extensions.Post
import `fun`.aragaki.kraft.data.extensions.Tag
import `fun`.aragaki.kraft.extensions.splitByBlank
import android.net.Uri
import androidx.core.text.isDigitsOnly

interface BooruWrapper {
    val booru: Boorus
    val service: Any
    val dependencyTag: String?

    suspend fun post(id: Long): List<Post>
//    suspend fun list(): List<Post>

    fun matchPostId(uri: Uri) = booru.patternsPostId?.matchPattern(uri)?.toLongOrNull()
    fun matchPostsTags(uri: Uri) = booru.patternsPostsTags?.matchPattern(uri)

    private fun String.matchPattern(uri: Uri): String? {
        splitByBlank()?.forEach {
            it.split('#').run {
                if (Regex(this[0]).matches(uri.toString())) {
                    val fragment = this[1]
                    return if (fragment.isDigitsOnly()) uri.pathSegments[fragment.toInt()]
                    else uri.getQueryParameter(fragment)
                }
            }
        }
        return null
    }

    interface Feature
    interface Taggable : Feature {
        val safeTag: String
        suspend fun tags(tags: String?, page: Int): List<Post>
    }

    interface Popular : Feature {
        suspend fun popular(param: String): List<Post>
    }

    interface Votable : Feature {
        suspend fun vote(positive: Boolean, postId: Long): Boolean
    }

    interface Followable : Feature {
        suspend fun follow(positive: Boolean, userId: Long): Boolean
    }


    interface TagsListable : Feature {
        suspend fun tagsList(pattern: String, page: Int): List<Tag>
    }

    companion object {
        object Unsupported : Exception("Unsupported feature.")

        inline fun <reified F : Feature> BooruWrapper.doAs(business: F.() -> Unit) {
            if (this is F) business() else throw Unsupported
        }
    }
}