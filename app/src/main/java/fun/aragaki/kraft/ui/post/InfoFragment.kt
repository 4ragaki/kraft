package `fun`.aragaki.kraft.ui.post

import `fun`.aragaki.kraft.R
import `fun`.aragaki.kraft.databinding.FragmentPostInfoBinding
import `fun`.aragaki.kraft.ui.ViewModelFactory
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil.ImageLoader
import coil.api.load
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.closestDI
import org.kodein.di.instance

class InfoFragment : BottomSheetDialogFragment(), DIAware {
    override val di: DI by closestDI()
    private val viewModel by viewModels<PostViewModel>({ requireActivity() }) { ViewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentPostInfoBinding.inflate(inflater, container, false).apply {
        viewModel.post.observe(viewLifecycleOwner) {

            lifecycleScope.launch(Dispatchers.Main) {
                val preview = it.preview()
                val loader by instance<ImageLoader>(tag = preview.dependencyTag)

                preview.info?.let { info ->
                    withContext(Dispatchers.IO) { info.uploaderAvatar() }?.let {
                        ivAvatar.load(it, loader) {
                            error(R.mipmap.ic_launcher_round)
                        }
                    }

                    info.title?.let { title -> tvTitle.text = title }
                    withContext(Dispatchers.IO) { info.uploaderName() }?.let { name ->
                        tvUser.text = name
                    }
                    btnFollow.load(if (info.isFollowed == true) R.drawable.ic_post_user_unfollow else R.drawable.ic_post_user_follow)
                    info.caption?.let { caption ->
                        tvCaption.text =
                            if (caption.first) Html.fromHtml(caption.second) else caption.second
                    }
                    tvShows.show(info.shows)
                    tvLikes.show(info.likes)
                    tvScores.show(info.scores)
                    tvRating.show(info.rating)
                    tvInfo.text = info.info { id -> getString(id) }
                    tvDate.text = info.date
                }
            }
        }
    }.root

    private fun TextView.show(data: Pair<Boolean, String?>) {
        isGone = data.first
        text = data.second
    }
}