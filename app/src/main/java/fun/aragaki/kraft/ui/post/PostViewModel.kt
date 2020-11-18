package `fun`.aragaki.kraft.ui.post

import `fun`.aragaki.kraft.Kraft
import `fun`.aragaki.kraft.PARAMETER_POST_ID
import `fun`.aragaki.kraft.SCHEME
import `fun`.aragaki.kraft.Settings
import `fun`.aragaki.kraft.data.UnsupportedException
import `fun`.aragaki.kraft.data.extensions.Post
import `fun`.aragaki.kraft.data.servicewrappers.BooruWrapper
import `fun`.aragaki.kraft.data.servicewrappers.BooruWrapper.Companion.doAs
import `fun`.aragaki.kraft.extensions.find
import `fun`.aragaki.kraft.worker.DownloadCompanion
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.instance

class PostViewModel(app: Kraft) : AndroidViewModel(app) {
    private val settings by app.instance<Settings>()

    private val wrappers by app.instance<List<BooruWrapper>>()
    private val _posts = MutableSharedFlow<List<Post>>(1)
    val posts: SharedFlow<List<Post>> = _posts
    val exception = MutableSharedFlow<Throwable>(1)

    fun handle(uri: Uri) {
        val boorus by getApplication<Kraft>().instance<List<BooruWrapper>>()

        viewModelScope.launch(Dispatchers.Main) {
            kotlin.runCatching out@{
                if (uri.scheme.equals(SCHEME, true)) {
                    boorus.forEach {
                        if (it.booru.authority.equals(uri.host, true)
                            or it.booru.name.equals(uri.host, true)
                        ) {
                            kotlin.runCatching {
                                uri.getQueryParameter(PARAMETER_POST_ID)?.toLong()!!
                            }.onSuccess { id ->
                                retrieve(it, id)
                                return@out
                            }.onFailure { throw NullPointerException("parameter:$PARAMETER_POST_ID is null.") }
                        }
                    }
                } else {
                    boorus.forEach { wrapper ->
                        wrapper.matchPostId(uri)?.let { id ->
                            retrieve(wrapper, id)
                            return@out
                        }
                    }
                }
                throw UnsupportedException(uri)
            }.onFailure {
                exception.emit(it)
                it.printStackTrace()
            }
        }
    }

    private suspend fun retrieve(wrapper: BooruWrapper, id: Long) {
        _posts.emit(wrapper.post(id))
    }


    fun download(post: Post, selection: Set<Int>? = null) {
        settings.fmtPostName.value?.let { fmt ->

            wrappers.find(post).let { wrapper ->
                post.downloads(wrapper, fmt)?.forEachIndexed { i, download ->

                    selection?.also {
                        if (i in selection)
                            DownloadCompanion.download(getApplication(), download)
                    } ?: DownloadCompanion.download(getApplication(), download)
                }
            }
        }
    }

    fun vote(
        postId: Long, positive: Boolean?,
        callback: (Boolean) -> Unit, error: (Throwable) -> Unit
    ) = viewModelScope.launch {
        posts.collectLatest {
            it.forEach { post ->
                wrappers.find(post).let { wrapper ->
                    viewModelScope.launch(Dispatchers.IO) {
                        kotlin.runCatching {
                            wrapper.doAs<BooruWrapper.Votable> {
                                val result = vote(positive != true, postId)
                                withContext(Dispatchers.Main) { callback(result) }
                            }
                        }.onFailure { error(it) }
                    }
                }
            }
        }
    }

    suspend fun follow(
        userId: Long, positive: Boolean?,
        callback: (Boolean) -> Unit, error: (Throwable) -> Unit
    ) = viewModelScope.launch {
        posts.collectLatest {
            it.forEach { post ->
                wrappers.find(post).let { wrapper ->
                    viewModelScope.launch(Dispatchers.IO) {
                        kotlin.runCatching {
                            wrapper.doAs<BooruWrapper.Followable> {
                                val result = follow(positive != true, userId)
                                withContext(Dispatchers.Main) { callback(result) }
                            }
                        }.onFailure { error(it) }
                    }
                }
            }
        }
    }
}