package `fun`.aragaki.kraft.data.features

@FunctionalInterface
interface Favorite {

    suspend fun favorite(postId: Long, username: String, credential: String)
}