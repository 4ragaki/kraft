package `fun`.aragaki.kraft.data.features

@FunctionalInterface
interface Comment {

    suspend fun comment(comment: String, username: String, credential: String)
}