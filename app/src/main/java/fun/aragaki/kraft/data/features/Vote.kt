package `fun`.aragaki.kraft.data.features

@FunctionalInterface
interface Vote {

    suspend fun vote(postId: Long, username: String, credential: String)
}