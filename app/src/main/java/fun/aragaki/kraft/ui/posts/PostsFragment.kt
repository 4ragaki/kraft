package `fun`.aragaki.kraft.ui.posts

import `fun`.aragaki.kraft.data.servicewrappers.BooruWrapper
import `fun`.aragaki.kraft.databinding.FragmentPostsBinding
import `fun`.aragaki.kraft.extensions.toast
import `fun`.aragaki.kraft.ui.ViewModelFactory
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import androidx.compose.ui.unit.dp
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import coil.ImageLoader
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.closestDI
import org.kodein.di.instance
import kotlin.math.ceil
import kotlin.math.floor

class PostsFragment : Fragment(), DIAware {
    override val di: DI by closestDI()
    private val viewModelFactory by instance<ViewModelFactory>()
    private val viewModel by viewModels<PostsViewModel>({ requireActivity() }) { viewModelFactory }
    private val wrappers by instance<List<BooruWrapper>>()
    lateinit var binding: FragmentPostsBinding

    @OptIn(ExperimentalPagingApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentPostsBinding.inflate(inflater, container, false).apply {
        binding = this
        setHasOptionsMenu(true)

        kotlin.runCatching {
            val uri = (arguments?.getParcelable(EXTRA_URI)
                ?: Uri.parse("kraft://yande.re/posts"))

            rvPosts.apply {
                val displayMetrics = resources.displayMetrics
                layoutManager = GridLayoutManager(
                    requireContext(),
                    floor(
                        displayMetrics.widthPixels / TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            180f,
                            displayMetrics
                        )
                    ).toInt()
                )
                val params = viewModel.match(uri)
                val loader by instance<ImageLoader>(params.first.dependencyTag)
                viewModel.source.value = params
                viewModel.flow.observe(viewLifecycleOwner) {
                    adapter = PostsAdapter(layoutInflater, wrappers, loader).also { adapter ->

/*                    viewModel.postsFlow(params.first, params.second)
                        .collectLatest { pagingData -> it.submitData(pagingData) } */

                        adapter.addLoadStateListener {
                            anim.isVisible = it.refresh is LoadState.Loading
                        }

                        lifecycleScope.launch {
                            it.collectLatest { pagingData -> adapter.submitData(pagingData) }
                        }
                    }
                }
            }
        }.onFailure {
            requireActivity().toast(it.message)
            it.printStackTrace()
        }
    }.root

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        wrappers.forEachIndexed { i, w -> menu.add(0, i, i, w.booru.name) }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) requireActivity().finishAfterTransition()
        else viewModel.source.value = wrappers[item.itemId] to null
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val EXTRA_URI = "posts.uri"
    }
}