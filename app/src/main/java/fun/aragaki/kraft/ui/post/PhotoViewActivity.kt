package `fun`.aragaki.kraft.ui.post

import `fun`.aragaki.kraft.databinding.ActivityPhotoViewBinding
import `fun`.aragaki.kraft.ui.base.BaseActivity
import android.os.Bundle
import android.view.WindowManager
import coil.ImageLoader
import coil.api.load
import org.kodein.di.DI
import org.kodein.di.android.retainedDI
import org.kodein.di.instance

class PhotoViewActivity : BaseActivity() {
    override val di: DI by retainedDI {
        extend(parentDI)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPhotoViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        binding.photoView.apply {
            intent.getStringExtra(EXTRA_URL)?.let {
                val loader by instance<ImageLoader>(intent.getStringExtra(EXTRA_DEPENDENCY_TAG))
                load(it, loader)
                setOnClickListener { finishAfterTransition() }
            } ?: load(intent.data)
        }
    }

    companion object {
        const val EXTRA_URL = "img_url"
        const val EXTRA_DEPENDENCY_TAG = "loader_tag"
    }
}