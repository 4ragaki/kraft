package `fun`.aragaki.kraft.ui.tags

import `fun`.aragaki.kraft.Kraft
import `fun`.aragaki.kraft.data.extensions.Tag
import `fun`.aragaki.kraft.databinding.FragmentTagsBinding
import `fun`.aragaki.kraft.databinding.ItemTagsTagBinding
import `fun`.aragaki.kraft.ui.ViewModelFactory
import android.os.Bundle
import android.view.*
import androidx.compose.ui.unit.dp
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.closestDI

class TagsFragment : Fragment(), DIAware {
    override val di: DI by closestDI()
    private val viewModel by viewModels<TagsViewModel>() { ViewModelFactory(Kraft.app) }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentTagsBinding.inflate(inflater, container, false).apply {
        rvTags.apply {
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                MaterialDividerItemDecoration(
                    requireContext(),
                    MaterialDividerItemDecoration.VERTICAL
                )
            )
        }

        lifecycleScope.launch {
            val adapter = object : PagingDataAdapter<Tag, TagHolder>(object :
                DiffUtil.ItemCallback<Tag>() {
                override fun areItemsTheSame(oldItem: Tag, newItem: Tag) =
                    oldItem.tagGetId() == newItem.tagGetId()

                override fun areContentsTheSame(oldItem: Tag, newItem: Tag) =
                    oldItem.tagGetId() == newItem.tagGetId()
            }) {
                override fun onBindViewHolder(holder: TagHolder, position: Int) {
                    holder.bind(getItem(position))
                }

                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
                    TagHolder(ItemTagsTagBinding.inflate(layoutInflater, parent, false))

            }
            rvTags.adapter = adapter

            viewModel.flow.collectLatest {
                println("new tags flow")
                it.collectLatest {
                    adapter.submitData(it)
                }
            }
        }

        etTags.doOnTextChanged { text, start, before, count ->
            viewModel.retrieve(text.toString())
        }

    }.root
}