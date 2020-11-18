package `fun`.aragaki.kraft.ui.posts

import `fun`.aragaki.kraft.R
import `fun`.aragaki.kraft.SCHEME
import `fun`.aragaki.kraft.adapters.BaseHolder
import `fun`.aragaki.kraft.data.extensions.Post
import `fun`.aragaki.kraft.data.servicewrappers.BooruWrapper
import `fun`.aragaki.kraft.databinding.ItemPostBinding
import `fun`.aragaki.kraft.extensions.find
import `fun`.aragaki.kraft.extensions.startActivity
import `fun`.aragaki.kraft.ui.post.PostActivity
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import coil.ImageLoader
import coil.load

class PostHolder(wrappers: List<BooruWrapper>, binding: ItemPostBinding) :
    BaseHolder<ItemPostBinding>(binding) {
    private lateinit var _post: Post

    init {
        binding.root.setOnClickListener { v ->
            if (this::_post.isInitialized) {
                wrappers.find(_post).let {
                    v.context.startActivity<PostActivity> {
                        data =
                            Uri.parse("$SCHEME://${it.booru.authority}/post?id=${_post.postId()}")
                    }
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
            tvId.text = "${post.postId()}"
        }
        _post = post
    }
}