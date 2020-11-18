package `fun`.aragaki.kraft.data.room.mediator

import `fun`.aragaki.kraft.data.extensions.Post
import `fun`.aragaki.kraft.data.room.KraftDatabase
import `fun`.aragaki.kraft.data.room.daos.MoebooruPostsDao
import `fun`.aragaki.kraft.data.servicewrappers.MoebooruWrapper
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction


@ExperimentalPagingApi
class PostsMediator<P : Post>(
    private val fetch: suspend (page: Int) -> Unit
) : RemoteMediator<Int, P>() {
    private var page = 0

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, P>
    ): MediatorResult {
        kotlin.runCatching {
            when (loadType) {

                LoadType.REFRESH -> {
                    fetch(0)
                }
                LoadType.PREPEND -> return MediatorResult.Success(true)
                LoadType.APPEND -> fetch(page++)
            }

            return MediatorResult.Success(false)
        }.onFailure {
            return MediatorResult.Error(it)
        }
        return MediatorResult.Error(Exception())
    }
}