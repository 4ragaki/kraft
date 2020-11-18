package `fun`.aragaki.kraft.ui.tags

import `fun`.aragaki.kraft.Kraft
import `fun`.aragaki.kraft.data.servicewrappers.BooruWrapper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.kodein.di.instance

class TagsViewModel(app: Kraft) : AndroidViewModel(app) {
    private val boorus by app.instance<List<BooruWrapper>>()
    val service = boorus.filter { it is BooruWrapper.TagsListable }
        .firstOrNull() as BooruWrapper.TagsListable
    private val pattern = MutableSharedFlow<String>(replay = 1).apply {
        viewModelScope.launch { emit("") }
    }
    val flow = pattern.map {
        Pager(PagingConfig(10)) {
            TagsDataSource(service, it)
        }.flow
    }


    fun retrieve(text: String) = viewModelScope.launch {
        pattern.emit(text)
    }
}