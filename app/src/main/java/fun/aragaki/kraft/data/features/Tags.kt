package `fun`.aragaki.kraft.data.features

import `fun`.aragaki.kraft.data.extensions.Post

@FunctionalInterface
interface Tags {

    suspend fun tags(tags: String?, page: Int): List<Post>
}