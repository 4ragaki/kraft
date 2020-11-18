package `fun`.aragaki.kraft.ui.posts

import `fun`.aragaki.kraft.data.extensions.Post
import `fun`.aragaki.kraft.data.servicewrappers.BooruWrapper
import `fun`.aragaki.kraft.databinding.ItemPostBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import coil.ImageLoader

class PostsAdapter(
    private val inflater: LayoutInflater,
    private val wrappers: List<BooruWrapper>,
    private val loader: ImageLoader
) :
    PagingDataAdapter<Post, PostHolder>(Comparator) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PostHolder(wrappers, ItemPostBinding.inflate(inflater, parent, false))

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        getItem(position)?.let { holder.bind(it, loader) }
    }

    companion object {
        private object Comparator : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post) =
                oldItem.postId() == newItem.postId()

            override fun areContentsTheSame(oldItem: Post, newItem: Post) =
                oldItem.postId() == newItem.postId()
        }
    }
}