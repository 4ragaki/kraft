package `fun`.aragaki.kraft.ui.tags

import `fun`.aragaki.kraft.Settings
import `fun`.aragaki.kraft.data.extensions.Post
import `fun`.aragaki.kraft.data.extensions.Tag
import `fun`.aragaki.kraft.data.servicewrappers.BooruWrapper
import androidx.paging.PagingSource
import androidx.paging.PagingState

class TagsDataSource(
    private val backend: BooruWrapper.TagsListable,
    private val pattern: String
) : PagingSource<Int, Tag>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Tag> {
        kotlin.runCatching {
            val pageNumber = params.key ?: 0
            val posts = backend.tagsList(pattern, pageNumber)
            return LoadResult.Page(data = posts, prevKey = null, nextKey = pageNumber + 1)
        }.onFailure {
            it.printStackTrace()
            return LoadResult.Error(it)
        }
        return LoadResult.Error(Exception())
    }

    override fun getRefreshKey(state: PagingState<Int, Tag>) = 0
}