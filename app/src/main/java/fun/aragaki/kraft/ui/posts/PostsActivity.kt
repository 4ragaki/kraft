package `fun`.aragaki.kraft.ui.posts

import `fun`.aragaki.kraft.R
import `fun`.aragaki.kraft.databinding.ActivityPostsBinding
import `fun`.aragaki.kraft.extensions.findUrls
import `fun`.aragaki.kraft.ui.ViewModelFactory
import `fun`.aragaki.kraft.ui.base.BaseActivity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import org.kodein.di.DI
import org.kodein.di.android.retainedDI
import org.kodein.di.instance

class PostsActivity : BaseActivity() {
    override val di: DI by retainedDI { extend(parentDI) }
    private lateinit var binding: ActivityPostsBinding
    private val viewModelFactory by instance<ViewModelFactory>()
    private val viewModel by viewModels<PostsViewModel> { viewModelFactory }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        binding.apply {
            viewModel.apply {
                title.observe(this@PostsActivity) { this@PostsActivity.title = it }
            }
        }
        launch(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let { launch(it) }
    }

    private fun launch(intent: Intent) {
        val uri = if (intent.action == Intent.ACTION_SEND)
            Uri.parse(intent.getStringExtra(Intent.EXTRA_TEXT)?.findUrls()?.first())
        else intent.data!!

        supportFragmentManager.beginTransaction().replace(
            R.id.posts_container,
            PostsFragment::class.java, bundleOf(PostsFragment.EXTRA_URI to uri),
        )
            .commit()
    }
}