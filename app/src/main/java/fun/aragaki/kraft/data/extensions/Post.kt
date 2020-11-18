package `fun`.aragaki.kraft.data.extensions

import `fun`.aragaki.kraft.data.TagType
import `fun`.aragaki.kraft.data.servicewrappers.BooruWrapper
import `fun`.aragaki.kraft.worker.Downloader
import android.content.Context

abstract class Post {
    var booruId: Int = 0
    abstract fun attachContext(wrapper: BooruWrapper): Post
    abstract fun postPreview(): String?
    abstract fun postId(): Long
    abstract fun preview(wrapper: BooruWrapper): Preview
    abstract fun info(wrapper: BooruWrapper): Info
    abstract fun downloads(wrapper: BooruWrapper, postNameFmt: String): List<Download>?
    abstract fun message(): String?

    fun assignAttributes(
        fmt: String, booru: String, id: Long, ext: String, tags: String?, part: Int? = null
    ) = fmt.assign(ATTR_BOORU, booru)
        .assign(ATTR_ID, id.toString())
        .assign(ATTR_PART, part?.toString())
        .assign(ATTR_TAGS, tags)
        .assign(ATTR_EXT, ext)

    private fun String.assign(attr: String, value: String?) = kotlin.run outer@{
        val block = """(\$\(.*)?$attr\)?"""
        if (value.isNullOrBlank()) kotlin.run {
            arrayOf(Regex("""\s*$block"""), Regex("""$block\s*""")).forEach { re ->
                re.find(this)?.let { return@outer this.replace(it.value, "") }
            }
            return this
        } else replace(Regex(block)) {
            it.value.replace(Regex(attr), value).removePrefix("\$(").removeSuffix(")")
        }
    }

    companion object {
        //IGNORE:Redundant character escape '\\}' in RegExp.
        private const val ATTR_BOORU = """\$\{booru\}"""
        private const val ATTR_ID = """\$\{id\}"""
        private const val ATTR_PART = """\$\{part\}"""
        private const val ATTR_TAGS = """\$\{tags\}"""
        private const val ATTR_EXT = """\$\{ext\}"""
    }


    class Info(
        val uploaderId: Long?,
        val uploaderName: suspend () -> String?,
        val uploaderAvatar: suspend () -> String?,
        var isVoted: Boolean?,
        var isFollowed: Boolean?,
        val title: String?,
//    gone,spanned,content
        val caption: Triple<Boolean, Boolean?, String?>,
//    gone,content
        val shows: Pair<Boolean, String?>,
        val likes: Pair<Boolean, String?>,
        val scores: Pair<Boolean, String?>,
        val rating: Pair<Boolean, String?>,
        val info: (Context) -> String,
        val date: String?
    )

    class Preview(
        val urls: List<String>,
        val dependencyTag: String?,
        val info: Info?,
        val tags: Map<TagType, List<String>?>
    )

    class Download(
        val url: String,
        val folder: List<String>?,
        val dependencyTag: String?,
        val filename: String,
        val headers: Array<String>? = null,
        val contentLength: Long = Downloader.UNDEFINED_CONTENT_LENGTH,
        val mime: String? = null
    )
}