package `fun`.aragaki.kraft.ui.post

import `fun`.aragaki.kraft.R
import `fun`.aragaki.kraft.adapters.BaseHolder
import `fun`.aragaki.kraft.databinding.ItemPostPreviewBinding
import androidx.core.view.isVisible
import coil.ImageLoader
import coil.api.load
import com.airbnb.lottie.LottieDrawable

class PostPreviewHolder(binding: ItemPostPreviewBinding) :
    BaseHolder<ItemPostPreviewBinding>(binding) {

    fun bind(url: String?, loader: ImageLoader) {
        if (url.isNullOrBlank()) {
            binding.animation.apply {
                repeatCount = 1
                setAnimation(R.raw._404_animation)
                playAnimation()
            }
        } else {
            binding.ivPreview.load(url, loader) {
                crossfade(true)
                listener(
                    onStart = {
                        binding.animation.isVisible = true
                        binding.animation.apply {
                            repeatCount = LottieDrawable.INFINITE
                            setAnimation(R.raw.ripple)
                            playAnimation()
                        }
                    },
                    onCancel = { binding.animation.isVisible = false },
                    onError = { _, _ ->
                        binding.animation.apply {
                            repeatCount = 1
                            setAnimation(R.raw.loading_fail)
                            playAnimation()
                        }
                    },
                    onSuccess = { _, _ -> binding.animation.isVisible = false }
                )
            }
        }
    }

    fun select(select: Boolean) = if (select) itemView.alpha = 0.3f else itemView.alpha = 1.0f
}