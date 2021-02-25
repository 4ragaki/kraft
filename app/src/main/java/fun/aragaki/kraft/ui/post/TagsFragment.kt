package `fun`.aragaki.kraft.ui.post

import `fun`.aragaki.kraft.R
import `fun`.aragaki.kraft.data.servicewrappers.TagType
import `fun`.aragaki.kraft.databinding.FragmentPostTagBinding
import `fun`.aragaki.kraft.ui.ViewModelFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class TagsFragment : BottomSheetDialogFragment() {
    private val viewModel by viewModels<PostViewModel>({ requireActivity() }) { ViewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentPostTagBinding.inflate(inflater, container, false).apply {
        viewModel.post.observe(viewLifecycleOwner) {
            it?.preview()?.tags?.let { tags ->
                tags[TagType.Artist.key]?.forEach { tag ->
                    chips.addChip(tag, R.color.green_22_a400)
                }
                tags[TagType.Character.key]?.forEach { tag ->
                    chips.addChip(tag, R.color.pink_22_a400)
                }
                tags[TagType.Copyright.key]?.forEach { tag ->
                    chips.addChip(tag, R.color.orange_22_a400)
                }
                tags[TagType.Genre.key]?.forEach { tag ->
                    chips.addChip(tag, R.color.purple_22_a400)
                }
                tags[TagType.Medium.key]?.forEach { tag ->
                    chips.addChip(tag, R.color.cyan_22_a400)
                }
                tags[TagType.Meta.key]?.forEach { tag ->
                    chips.addChip(tag, R.color.red_22_a400)
                }
                tags[TagType.Studio.key]?.forEach { tag ->
                    chips.addChip(tag, R.color.deep_orange_22_a400)
                }
                tags[TagType.General.key]?.forEach { tag ->
                    chips.addChip(tag, null)
                }
                tags[TagType.Undefined.key]?.forEach { tag ->
                    chips.addChip(tag, null)
                }
            }
        }
    }.root

    private fun ChipGroup.addChip(str: String, @ColorRes color: Int?) {
        if (str.isNotEmpty()) {
            addView(Chip(requireContext()).apply {
                color?.let { setChipBackgroundColorResource(it) }
                text = str
            })
        }
    }
}