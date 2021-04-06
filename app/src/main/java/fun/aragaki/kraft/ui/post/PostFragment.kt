package `fun`.aragaki.kraft.ui.post

import `fun`.aragaki.kraft.data.extensions.Post
import `fun`.aragaki.kraft.databinding.FragmentPostBinding
import `fun`.aragaki.kraft.extensions.snack
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import org.kodein.di.DIAware
import org.kodein.di.android.x.closestDI
import org.kodein.di.instance

class PostFragment : Fragment(), DIAware {
    override val di by closestDI()

    private val viewModel by instance<PostViewModel>()
    private lateinit var binding: FragmentPostBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentPostBinding.inflate(inflater, container, false).also { binding = it }.apply {
        viewModel.apply {
            post.observe(viewLifecycleOwner) { launch(it) }
            posts.observe(viewLifecycleOwner) {
                AlertDialog.Builder(requireContext())
                    .setSingleChoiceItems(it?.map { post -> post?.pTitle }?.toTypedArray(), 0)
                    { _, which -> it?.get(which)?.let { post -> viewModel.show(post) } }.show()
            }
        }
    }.root


    private fun launch(post: Post) {
        requireActivity().title = post.pTitle
        binding.apply {
            rvPreviews.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = PostPreviewsAdapter(post, this@PostFragment)
                post.message()?.takeIf { it.isNotBlank() }?.let { root.snack(it) }
            }
        }
    }
}