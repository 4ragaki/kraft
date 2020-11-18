package `fun`.aragaki.kraft.data.entities

import `fun`.aragaki.kraft.data.servicewrappers.BooruWrapper

abstract class Post {
    abstract var pWrapper: BooruWrapper
    abstract var pTitle: String

    abstract fun attachContext(wrapper: BooruWrapper): Post
    abstract fun postPreview(): String?
    abstract fun postId(): Long
    abstract fun preview(): Preview
    abstract fun downloads(postNameFmt: String): List<Download>?
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
}