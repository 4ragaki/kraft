package `fun`.aragaki.kraft.data.features

import `fun`.aragaki.kraft.data.entities.Post

@FunctionalInterface
interface Popular {

    suspend fun popular(param: String): List<Post>
}