package `fun`.aragaki.kraft.ui.posts

import `fun`.aragaki.kraft.Kraft
import `fun`.aragaki.kraft.PARAMETER_POSTS_TAGS
import `fun`.aragaki.kraft.SCHEME
import `fun`.aragaki.kraft.Settings
import `fun`.aragaki.kraft.data.UnsupportedException
import `fun`.aragaki.kraft.data.servicewrappers.*
import android.net.Uri
import androidx.lifecycle.*
import androidx.paging.*
import org.kodein.di.instance

class PostsViewModel(app: Kraft) : AndroidViewModel(app) {
    private val _title = MutableLiveData<String>()
    private val settings by app.instance<Settings>()
    val title: LiveData<String> = _title

    val source = MutableLiveData<Pair<BooruWrapper, String?>>()


    val flow = Transformations.map(source) { params ->
        val wrapper = params.first
        val tags = params.second
        _title.value = "${wrapper.booru.name} #$tags"
        Pager(PagingConfig(pageSize = 10), initialKey = wrapper.booru.pageStart) {
            PostsDataSource(wrapper as BooruWrapper.Taggable, tags, settings)
        }.flow.cachedIn(viewModelScope)
    }

    fun match(uri: Uri): Pair<BooruWrapper, String?> {
        val boorus by getApplication<Kraft>().instance<List<BooruWrapper>>()
        if (uri.scheme == SCHEME) {
            boorus.forEach {
                if (it.booru.authority.equals(uri.host, true) || it.booru.name.equals(
                        uri.host,
                        true
                    )
                )
                    return Pair(it, uri.getQueryParameter(PARAMETER_POSTS_TAGS))
            }
        } else {
            boorus.forEach { wrapper ->
                wrapper.matchPostsTags(uri)?.takeIf { t -> t.isNotBlank() }?.let {
                    return Pair(wrapper, it)
                }
            }
        }
        throw UnsupportedException(uri)
    }
}