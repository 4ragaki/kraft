package `fun`.aragaki.kraft.data.features

@FunctionalInterface
interface PixivSearch {

    suspend fun pixivSearch(word: String, offset: Long): Any
}