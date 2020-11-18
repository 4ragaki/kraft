package `fun`.aragaki.kraft.data.servicewrappers

import `fun`.aragaki.kraft.POSTS_DIR

sealed class Boorus : Booru {

    class Danbooru(
        override val id: String?,
        override val credential: String?,

        override val booruSubId: Int,
        override val name: String,
        override val scheme: String,
        override val host: String,

        override val patternsPostId: String?,
        override val patternsPostsTags: String?,
        override val folder: Array<String> = arrayOf(POSTS_DIR, name)
    ) : Boorus() {
        override val booruType: Int = BooruType.Danbooru.ordinal

        companion object {
            fun patternPostId(host: String) =
                """(http|https)://(www.)?$host/posts/\S+#1 (http|https)://(www.)?$host/post/show/\S+#2"""

            fun patternPostsTags(host: String) =
                """(http|https)://(www.)?$host/posts\S+#tags"""
        }
    }

    class Moebooru(
        override val id: String?,
        override val credential: String?,

        override val booruSubId: Int,
        override val name: String,
        override val scheme: String,
        override val host: String,

        val hashSalt: String?,
        override val patternsPostId: String?,
        override val patternsPostsTags: String?,
        override val folder: Array<String> = arrayOf(POSTS_DIR, name)
    ) : Boorus() {
        override val booruType: Int = BooruType.Moebooru.ordinal

        companion object {
            fun patternPostId(host: String) =
                """(http|https)://(www.)?$host/post/show/\S+#2"""

            fun patternPostsTags(host: String) =
                """(http|https)://(www.)?$host/post\S+#tags"""
        }
    }

    class Gelbooru(
        override val id: String?,
        override val credential: String?,

        override val booruSubId: Int,
        override val name: String,
        override val scheme: String,
        override val host: String,

        override val patternsPostId: String?,
        override val patternsPostsTags: String?,
        override val folder: Array<String> = arrayOf(POSTS_DIR, name)
    ) : Boorus() {
        override val booruType: Int = BooruType.Gelbooru.ordinal

        companion object {
            fun patternPostId(host: String) =
                """(http|https)://(www.)?$host/index.php\S+#id"""

            fun patternPostsTags(host: String) =
                """(http|https)://(www.)?$host/index.php\S+#tags"""
        }
    }

    class Sankaku(
        override val id: String?,
        override val credential: String?,

        override val booruSubId: Int,
        override val name: String,
        override val scheme: String,
        override val host: String,

        val hashSalt: String?,
        override val patternsPostId: String?,
        override val patternsPostsTags: String?,
        override val folder: Array<String> = arrayOf(POSTS_DIR, name)
    ) : Boorus() {
        override val booruType: Int = BooruType.Sankaku.ordinal

        companion object {
            fun patternPostId(host: String) =
                """(http|https)://(www.)?$host/post/show/\S+#2"""

            fun patternPostsTags(host: String) =
                """(http|https)://(www.)?$host\S+#tags (http|https)://(www.)?$host/post/index\S+#tags"""
        }
    }

    class Pixiv(
        override val id: String?,
        override val credential: String?,

        override val booruSubId: Int,
        override val name: String,

        val oauthScheme: String,
        val oauthHost: String,
        override val scheme: String,
        override val host: String,

        var accessToken: String?,
        var refreshToken: String?,
        val tokenCallback: suspend (accessToken: String, refreshToken: String) -> Unit,

        override val patternsPostId: String?,
        override val patternsPostsTags: String?,
        override val folder: Array<String> = arrayOf(POSTS_DIR, name)
    ) : Boorus() {
        override val booruType: Int = BooruType.Pixiv.ordinal

        companion object {
            fun patternPostId(host: String) =
                """(http|https)://(www.)?$host/artworks/\S+#1 (http|https)://(www.)?$host/member_illust.php\S+#illust_id (http|https)://(www.)?$host/i/\S+#1"""

            fun patternPostsTags(host: String) =
                """(http|https)://(www.)?$host/tags/\S+#1"""
        }
    }

    enum class BooruType {
        Moebooru, Danbooru, Gelbooru, Sankaku, Pixiv
    }
}