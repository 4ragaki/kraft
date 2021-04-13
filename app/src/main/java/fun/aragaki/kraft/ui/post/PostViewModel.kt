package `fun`.aragaki.kraft.ui.post

import `fun`.aragaki.kraft.Kraft
import `fun`.aragaki.kraft.PARAMETER_POST_ID
import `fun`.aragaki.kraft.SCHEME
import `fun`.aragaki.kraft.Settings
import `fun`.aragaki.kraft.data.UnsupportedException
import `fun`.aragaki.kraft.data.extensions.Post
import `fun`.aragaki.kraft.data.servicewrappers.BooruWrapper
import `fun`.aragaki.kraft.data.servicewrappers.BooruWrapper.Companion.doAs
import `fun`.aragaki.kraft.worker.DownloadCompanion
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.instance

class PostViewModel(app: Kraft) : AndroidViewModel(app) {
    private val settings by app.instance<Settings>()

    private val _posts = MutableLiveData<List<Post?>>()
    val posts: LiveData<List<Post?>> = _posts
    private val _post = MutableLiveData<Post>()
    val post: LiveData<Post> = _post

    fun handle(uri: Uri, onSuccess: () -> Unit, onException: (throwable: Throwable) -> Unit) {
        val boorus by getApplication<Kraft>().instance<List<BooruWrapper>>()

        viewModelScope.launch(Dispatchers.Main) {
            kotlin.runCatching out@{
                if (uri.scheme.equals(SCHEME, true)) {
                    boorus.forEach {
                        if (it.booru.authority.equals(uri.host, true)
                            or it.booru.name.equals(uri.host, true)
                        ) {
                            retrieve(it, uri.getQueryParameter(PARAMETER_POST_ID)?.toLong()!!)
                            onSuccess()
                            return@out
                        }
                    }
                } else {
                    boorus.forEach { wrapper ->
                        wrapper.matchPostId(uri)?.let { id ->
                            retrieve(wrapper, id)
                            onSuccess()
                            return@out
                        }
                    }
                }
                throw UnsupportedException
            }.onFailure {
                onException(it)
                it.printStackTrace()
            }
        }
    }

    private suspend fun retrieve(wrapper: BooruWrapper, id: Long) {
        val posts = wrapper.post(id)
        if (posts.size == 1) _post.value = posts.first()
        else _posts.value = posts
    }

    fun show(post: Post) {
        _post.value = post
    }

    fun download(selection: Set<Int>? = null) {
        settings.fmtPostName.value?.let {
            val value = post.value!!
            value.downloads(it)?.forEachIndexed { i, download ->

                selection?.also {
                    if (i in selection)
                        DownloadCompanion.download(getApplication(), download)
                } ?: DownloadCompanion.download(getApplication(), download)
            }
        }
    }

    fun vote(
        postId: Long, positive: Boolean?,
        callback: (Boolean) -> Unit, error: (Throwable) -> Unit
    ) {
        post.value?.pWrapper?.let {
            viewModelScope.launch(Dispatchers.IO) {
                kotlin.runCatching {
                    it.doAs<BooruWrapper.Votable> {
                        val result = vote(positive != true, postId)
                        withContext(Dispatchers.Main) { callback(result) }
                    }
                }.onFailure { error(it) }
            }
        }
    }

    fun follow(
        userId: Long, positive: Boolean?,
        callback: (Boolean) -> Unit, error: (Throwable) -> Unit
    ) {
        post.value?.pWrapper?.let {
            viewModelScope.launch(Dispatchers.IO) {
                kotlin.runCatching {
                    it.doAs<BooruWrapper.Followable> {
                        val result = follow(positive != true, userId)
                        withContext(Dispatchers.Main) { callback(result) }
                    }
                }.onFailure { error(it) }
            }
        }
    }
}