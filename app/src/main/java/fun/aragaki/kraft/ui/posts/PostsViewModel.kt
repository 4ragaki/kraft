package `fun`.aragaki.kraft.ui.posts

import `fun`.aragaki.kraft.Kraft
import `fun`.aragaki.kraft.PARAMETER_POSTS_TAGS
import `fun`.aragaki.kraft.SCHEME
import `fun`.aragaki.kraft.data.UnsupportedException
import `fun`.aragaki.kraft.data.extensions.Post
import `fun`.aragaki.kraft.data.features.Tags
import `fun`.aragaki.kraft.data.servicewrappers.BooruWrapper
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import org.kodein.di.instance

class PostsViewModel(app: Kraft) : AndroidViewModel(app) {
    private val _title = MutableLiveData<String>()
    val title: LiveData<String> = _title

    fun postsFlow(wrapper: BooruWrapper, tags: String?): Flow<PagingData<Post>> {
        _title.value = "${wrapper.booru.name} #$tags"
        return Pager(PagingConfig(pageSize = 10)) {
            PostsDataSource(wrapper as Tags, tags)
        }.flow.cachedIn(viewModelScope)
    }

    fun match(uri: Uri): Pair<BooruWrapper, String?> {
        val boorus by getApplication<Kraft>().instance<List<BooruWrapper>>()
        if (uri.scheme == SCHEME) {
            boorus.forEach {
                if (it.booru.host.equals(uri.host, true) || it.booru.name.equals(uri.host, true))
                    return Pair(it, uri.getQueryParameter(PARAMETER_POSTS_TAGS))
            }
        } else {
            boorus.forEach { wrapper ->
                wrapper.matchPostsTags(uri)?.takeIf { t -> t.isNotBlank() }?.let {
                    return Pair(wrapper, it)
                }
            }
        }
        throw UnsupportedException
    }
}