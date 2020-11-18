package `fun`.aragaki.kraft.ui

import `fun`.aragaki.kraft.Kraft
import `fun`.aragaki.kraft.ui.post.PostViewModel
import `fun`.aragaki.kraft.ui.posts.PostsViewModel
import `fun`.aragaki.kraft.ui.search.ReverseViewModel
import `fun`.aragaki.kraft.ui.preferences.WorksViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val app: Kraft) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>) = when {
        modelClass.isAssignableFrom(ReverseViewModel::class.java) -> ReverseViewModel(app) as T
        modelClass.isAssignableFrom(PostViewModel::class.java) -> PostViewModel(app) as T
        modelClass.isAssignableFrom(PostsViewModel::class.java) -> PostsViewModel(app) as T
        modelClass.isAssignableFrom(WorksViewModel::class.java) -> WorksViewModel(app) as T
        else -> throw Exception("can not create ViewModel of ${modelClass.canonicalName}")
    }
}