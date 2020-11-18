package `fun`.aragaki.kraft.ui.preferences

import `fun`.aragaki.kraft.SHARE_IMAGEVIEW
import `fun`.aragaki.kraft.adapters.QuickAdapter
import `fun`.aragaki.kraft.databinding.FragmentWorksBinding
import `fun`.aragaki.kraft.databinding.ItemWorkBinding
import `fun`.aragaki.kraft.extensions.startActivity
import `fun`.aragaki.kraft.ui.ViewModelFactory
import `fun`.aragaki.kraft.ui.post.ImageViewActivity
import `fun`.aragaki.kraft.worker.DownloadOutput
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.closestDI
import org.kodein.di.instance

class WorksFragment : Fragment(), DIAware {
    override val di: DI by closestDI()
    private val viewModelFactory by instance<ViewModelFactory>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentWorksBinding.inflate(layoutInflater).apply {
        val viewModel by viewModels<WorksViewModel> { viewModelFactory }

        rvWorks.apply {
            layoutManager = LinearLayoutManager(requireContext())
            viewModel.works.observe(viewLifecycleOwner) { works ->
                works.sortByDescending { it.outputData.getLong(DownloadOutput.time, 0) }
                adapter = QuickAdapter(
                    works, { holder, i ->
                        requireContext().startActivity<ImageViewActivity>(
                            ActivityOptionsCompat.makeSceneTransitionAnimation(
                                requireActivity(), holder.binding.ivPreview,
                                SHARE_IMAGEVIEW
                            ).toBundle()
                        ) {
                            data = Uri.parse(works[i].outputData.getString(DownloadOutput.uri))
                        }
                    },
                    { viewGroup, _ ->
                        WorkHolder(
                            ItemWorkBinding.inflate(
                                layoutInflater, viewGroup, false
                            )
                        )
                    }) { holder, info ->
                    holder.bind(info)
                }
            }
        }
    }.root
}