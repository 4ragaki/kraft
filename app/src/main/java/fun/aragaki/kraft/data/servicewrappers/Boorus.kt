package `fun`.aragaki.kraft.data.servicewrappers

import `fun`.aragaki.kraft.POSTS_DIR
import `fun`.aragaki.kraft.data.room.PostConverters
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

sealed class Boorus : Booru {

    @Entity
    @TypeConverters(PostConverters::class)
    class Danbooru(
        val id: String?,
        val credential: String?,

        @PrimaryKey
        override val booruId: Int,
        override val name: String,
        override val authority: String,
        override val scheme: String,
        override val host: String,

        override val pageStart: Int,

        override val patternsPostId: String = patternPostId(authority),
        override val patternsPostsTags: String = patternPostsTags(authority),
        override val folder: List<String> = listOf(POSTS_DIR, name)
    ) : Boorus() {
        @Ignore
        override val booruType: Int = BooruType.Danbooru.ordinal

        companion object {
            fun patternPostId(host: String) =
                """(http|https)://(www.)?$host/posts/\S+#1 (http|https)://(www.)?$host/post/show/\S+#2"""

            fun patternPostsTags(host: String) =
                """(http|https)://(www.)?$host/posts\S+#tags"""
        }
    }

    @Entity
    @TypeConverters(PostConverters::class)
    class Moebooru(
        val id: String?,
        val credential: String?,

        @PrimaryKey
        override val booruId: Int,
        override val name: String,
        override val authority: String,
        override val scheme: String,
        override val host: String,

        override val pageStart: Int,

        val hashSalt: String?,
        override val patternsPostId: String = patternPostId(authority),
        override val patternsPostsTags: String = patternPostsTags(authority),
        override val folder: List<String> = listOf(POSTS_DIR, name)
    ) : Boorus() {
        @Ignore
        override val booruType: Int = BooruType.Moebooru.ordinal

        companion object {
            fun patternPostId(host: String) =
                """(http|https)://(www.)?$host/post/show/\S+#2"""

            fun patternPostsTags(host: String) =
                """(http|https)://(www.)?$host/post\S+#tags"""
        }
    }

    @Entity
    @TypeConverters(PostConverters::class)
    class Gelbooru(
        val id: String?,
        val credential: String?,

        @PrimaryKey
        override val booruId: Int,
        override val name: String,
        override val authority: String,
        override val scheme: String,
        override val host: String,

        override val pageStart: Int,

        override val patternsPostId: String = patternPostId(authority),
        override val patternsPostsTags: String = patternPostsTags(authority),
        override val folder: List<String> = listOf(POSTS_DIR, name)
    ) : Boorus() {
        @Ignore
        override val booruType: Int = BooruType.Gelbooru.ordinal

        companion object {
            fun patternPostId(host: String) =
                """(http|https)://(www.)?$host/index.php\S+#id"""

            fun patternPostsTags(host: String) =
                """(http|https)://(www.)?$host/index.php\S+#tags"""
        }
    }

    @Entity
    @TypeConverters(PostConverters::class)
    class Sankaku(
        val id: String?,
        val credential: String?,

        @PrimaryKey
        override val booruId: Int,
        override val name: String,
        override val authority: String,
        override val scheme: String,
        override val host: String,

        override val pageStart: Int,

        val hashSalt: String?,
        override val patternsPostId: String = patternPostId(authority),
        override val patternsPostsTags: String = patternPostsTags(authority),
        override val folder: List<String> = listOf(POSTS_DIR, name)
    ) : Boorus() {
        @Ignore
        override val booruType: Int = BooruType.Sankaku.ordinal

        companion object {
            fun patternPostId(host: String) =
                """(http|https)://(www.)?$host(/*)/post/show/\S+#2"""

            fun patternPostsTags(host: String) =
                """(http|https)://(www.)?$host\S+#tags (http|https)://(www.)?$host/post/index\S+#tags"""
        }
    }

    @Entity
    @TypeConverters(PostConverters::class)
    class Pixiv(
        @PrimaryKey
        override val booruId: Int,
        override val name: String,
        override val authority: String,

        val authScheme: String,
        val authHost: String,
        override val scheme: String,
        override val host: String,

        override val pageStart: Int,

        var accessToken: String?,
        var refreshToken: String?,

        override val patternsPostId: String = patternPostId(authority),
        override val patternsPostsTags: String = patternPostsTags(authority),
        override val folder: List<String> = listOf(POSTS_DIR, name)
    ) : Boorus() {
        @Ignore
        override val booruType: Int = BooruType.Pixiv.ordinal

        @Ignore
        lateinit var tokenCallback: suspend (accessToken: String, refreshToken: String) -> Unit

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