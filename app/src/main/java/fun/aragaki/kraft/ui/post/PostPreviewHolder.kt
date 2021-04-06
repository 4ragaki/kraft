package `fun`.aragaki.kraft.ui.post

import `fun`.aragaki.kraft.R
import `fun`.aragaki.kraft.adapters.BaseHolder
import `fun`.aragaki.kraft.databinding.ItemPostPreviewBinding
import `fun`.aragaki.kraft.extensions.play
import androidx.core.view.isVisible
import coil.ImageLoader
import coil.api.load

class PostPreviewHolder(binding: ItemPostPreviewBinding) :
    BaseHolder<ItemPostPreviewBinding>(binding) {

    fun bind(url: String?, loader: ImageLoader) {
        if (url.isNullOrBlank()) binding.ivPreview.load(R.drawable.illu_post_url_empty)
        else binding.ivPreview.load(url, loader) {
            crossfade(true)
            listener(
                onStart = {
                    binding.anim.apply {
                        isVisible = true
                        play(R.raw.ripple)
                    }
                },
                onCancel = { binding.anim.isVisible = false },
                onError = { _, _ -> binding.anim.isVisible = false },
                onSuccess = { _, _ -> binding.anim.isVisible = false }
            )
            error(R.drawable.illu_post_load_error)
        }
    }

    fun select(select: Boolean) = if (select) itemView.alpha = 0.3f else itemView.alpha = 1.0f
}