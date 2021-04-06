package `fun`.aragaki.kraft.ui.post

import `fun`.aragaki.kraft.PARAMETER_POSTS_TAGS
import `fun`.aragaki.kraft.R
import `fun`.aragaki.kraft.SCHEME
import `fun`.aragaki.kraft.data.servicewrappers.TagType
import `fun`.aragaki.kraft.databinding.FragmentPostTagBinding
import `fun`.aragaki.kraft.extensions.view
import `fun`.aragaki.kraft.ui.ViewModelFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.annotation.ColorRes
import androidx.core.view.forEach
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class TagsFragment : BottomSheetDialogFragment() {
    private val viewModel by viewModels<PostViewModel>({ requireActivity() }) { ViewModelFactory }
    lateinit var binding: FragmentPostTagBinding
    private val listener =
        CompoundButton.OnCheckedChangeListener { _, _ ->
            binding.fabSearch.isVisible = true
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentPostTagBinding.inflate(inflater, container, false).apply {
        viewModel.post.observe(viewLifecycleOwner) { post ->
            fabSearch.setOnClickListener {
                buildString {
                    chips.forEach { chip ->
                        chip as Chip
                        if (chip.isChecked) {
                            append(chip.text)
                            append(" ")
                        }
                    }
                    requireContext().view(
                        "$SCHEME://${post.pWrapper.booru.host}/posts?$PARAMETER_POSTS_TAGS=${toString()}"
                    )
                }
                dismiss()
            }

            post?.preview()?.tags?.let { tags ->
                tags[TagType.Artist.key]?.forEach { tag ->
                    chips.addChip(inflater, tag, R.color.green_a400)
                }
                tags[TagType.Character.key]?.forEach { tag ->
                    chips.addChip(inflater, tag, R.color.pink_a400)
                }
                tags[TagType.Copyright.key]?.forEach { tag ->
                    chips.addChip(inflater, tag, R.color.orange_a400)
                }
                tags[TagType.Genre.key]?.forEach { tag ->
                    chips.addChip(inflater, tag, R.color.purple_a400)
                }
                tags[TagType.Medium.key]?.forEach { tag ->
                    chips.addChip(inflater, tag, R.color.cyan_a400)
                }
                tags[TagType.Meta.key]?.forEach { tag ->
                    chips.addChip(inflater, tag, R.color.red_a400)
                }
                tags[TagType.Studio.key]?.forEach { tag ->
                    chips.addChip(inflater, tag, R.color.deep_orange_a400)
                }
                tags[TagType.General.key]?.forEach { tag ->
                    chips.addChip(inflater, tag, null)
                }
                tags[TagType.Undefined.key]?.forEach { tag ->
                    chips.addChip(inflater, tag, null)
                }
            }
        }
    }.also { binding = it }.root

    private fun ChipGroup.addChip(inflater: LayoutInflater, str: String, @ColorRes color: Int?) =
        if (str.isNotEmpty()) {
            addView((inflater.inflate(R.layout.item_tag, this, false) as Chip).apply {
                setOnCheckedChangeListener(listener)
                color?.let { setTextColor(requireContext().getColor(color)) }
                text = str
            })
        } else Unit
}