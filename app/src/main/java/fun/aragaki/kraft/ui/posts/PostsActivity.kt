package `fun`.aragaki.kraft.ui.posts

import `fun`.aragaki.kraft.databinding.ActivityPostsBinding
import `fun`.aragaki.kraft.ext.findUrls
import `fun`.aragaki.kraft.ui.ViewModelFactory
import `fun`.aragaki.kraft.ui.base.BaseSwipeBackActivity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import coil.ImageLoader
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.android.retainedDI
import org.kodein.di.instance

class PostsActivity : BaseSwipeBackActivity() {
    override val di: DI by retainedDI {
        extend(parentDI)
    }
    private lateinit var binding: ActivityPostsBinding
    private val viewModel by viewModels<PostsViewModel> { ViewModelFactory }

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

        binding.apply {
            rvPosts.apply {
                layoutManager = GridLayoutManager(this@PostsActivity, 2)
                val params = viewModel.match(uri)
                val loader by instance<ImageLoader>(params.first.dependencyTag)
                adapter = PostsAdapter(layoutInflater, loader).also {
                    lifecycleScope.launch {
                        viewModel.postsFlow(params.first, params.second)
                            .collectLatest { pagingData -> it.submitData(pagingData) }
                    }
                    lifecycleScope.launch {
                        it.loadStateFlow.collectLatest {
                            anim.isVisible = it.refresh is LoadState.Loading
                        }
                    }
                }
            }
        }
    }
}