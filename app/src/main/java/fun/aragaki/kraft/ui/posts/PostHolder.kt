package `fun`.aragaki.kraft.ui.posts

import `fun`.aragaki.kraft.R
import `fun`.aragaki.kraft.SCHEME
import `fun`.aragaki.kraft.adapters.BaseHolder
import `fun`.aragaki.kraft.data.extensions.Post
import `fun`.aragaki.kraft.databinding.ItemPostBinding
import `fun`.aragaki.kraft.extensions.startActivity
import `fun`.aragaki.kraft.ui.post.PostActivity
import android.net.Uri
import coil.ImageLoader
import coil.api.load

class PostHolder(binding: ItemPostBinding) : BaseHolder<ItemPostBinding>(binding) {
    private lateinit var _post: Post

    init {
        binding.root.setOnClickListener { v ->
            if (this::_post.isInitialized) {
                v.context.startActivity<PostActivity> {
                    data =
                        Uri.parse("$SCHEME://${_post.pWrapper.booru.host}/post?id=${_post.postId()}")
                }
            }
        }
    }

    fun bind(post: Post, loader: ImageLoader) {
        binding.apply {
            with(post.postPreview()) {
                if (isNullOrBlank()) {
                    ivPost.load(R.drawable.illu_post_url_empty)
                } else {
                    ivPost.load(this, loader) {
                        crossfade(true)
                    }
                }
            }
        }
        _post = post
    }
}