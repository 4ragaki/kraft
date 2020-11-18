package `fun`.aragaki.kraft.ui

import `fun`.aragaki.kraft.Kraft
import `fun`.aragaki.kraft.ui.post.PostViewModel
import `fun`.aragaki.kraft.ui.posts.PostsViewModel
import `fun`.aragaki.kraft.ui.preferences.CredentialsViewModel
import `fun`.aragaki.kraft.ui.preferences.WorksViewModel
import `fun`.aragaki.kraft.ui.search.SearchViewModel
import `fun`.aragaki.kraft.ui.tags.TagsViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(val app: Kraft) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) = when {
        modelClass.isAssignableFrom(SearchViewModel::class.java) -> SearchViewModel(app)
        modelClass.isAssignableFrom(PostViewModel::class.java) -> PostViewModel(app)
        modelClass.isAssignableFrom(PostsViewModel::class.java) -> PostsViewModel(app)
        modelClass.isAssignableFrom(WorksViewModel::class.java) -> WorksViewModel(app)
        modelClass.isAssignableFrom(CredentialsViewModel::class.java) -> CredentialsViewModel(app)
        modelClass.isAssignableFrom(TagsViewModel::class.java) -> TagsViewModel(app)
        else -> throw Exception("can not create ViewModel of ${modelClass.canonicalName}")
    } as T

}