package `fun`.aragaki.kraft.ui.posts

import `fun`.aragaki.kraft.Settings
import `fun`.aragaki.kraft.data.extensions.Post
import `fun`.aragaki.kraft.data.servicewrappers.BooruWrapper
import androidx.paging.PagingSource
import androidx.paging.PagingState

class PostsDataSource(
    private val backend: BooruWrapper.Taggable,
    private val tags: String?,
    private val settings: Settings
) : PagingSource<Int, Post>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Post> {
        kotlin.runCatching {
            val pageNumber = params.key ?: 0
            val posts = backend.tags(
                if (settings.nsfw.value || tags?.contains(backend.safeTag) == true) tags else buildString {
                    append(backend.safeTag)
                    tags?.let {
                        append(" ")
                        append(it)
                    }
                },
                pageNumber
            )
            return LoadResult.Page(data = posts, prevKey = null, nextKey = pageNumber + 1)
        }.onFailure {
            it.printStackTrace()
            return LoadResult.Error(it)
        }
        return LoadResult.Error(Exception())
    }

    override fun getRefreshKey(state: PagingState<Int, Post>) = 0
}