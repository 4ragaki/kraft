package `fun`.aragaki.kraft.ui.post

import `fun`.aragaki.kraft.R
import `fun`.aragaki.kraft.SHARE_IMAGEVIEW
import `fun`.aragaki.kraft.data.extensions.Post
import `fun`.aragaki.kraft.databinding.ItemPostPreviewBinding
import `fun`.aragaki.kraft.extensions.vbrLongClick
import android.content.Intent
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.appcompat.view.ActionMode
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import org.kodein.di.DIAware
import org.kodein.di.instance

class PostPreviewsAdapter(private val post: Post, private val di: DIAware) :
    RecyclerView.Adapter<PostPreviewHolder>() {
    private val activity by di.instance<PostActivity>()
    private val viewModel by di.instance<PostViewModel>()
    private val inflater by di.instance<LayoutInflater>()
    private val vibrator by di.instance<Vibrator>()
    private val urls = post.preview().urls

    private var isSelectionMode = false
    private val selection = mutableSetOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PostPreviewHolder(
        ItemPostPreviewBinding.inflate(inflater, parent, false)
    )

    override fun onBindViewHolder(holder: PostPreviewHolder, position: Int) {
        val imageLoader by di.instance<ImageLoader>(post.pWrapper.dependencyTag)
        val url = urls[position]
        holder.apply {
            bind(url, imageLoader)
            select(position in selection)

            itemView.apply {
                setOnLongClickListener {
                    vibrator.vbrLongClick()
                    isSelectionMode = true
                    select(true)
                    activity.apply { actionMode = startSupportActionMode(ActionCallback()) }
                    selection.add(position)
                }

                setOnClickListener {
                    if (isSelectionMode) {
                        if (position in selection) {
                            selection.remove(position)
                            select(false)
                        } else {
                            selection.add(position)
                            select(true)
                        }
                    } else {
                        activity.startActivity(
                            Intent(activity, PhotoViewActivity::class.java).apply {
                                putExtra(PhotoViewActivity.EXTRA_URL, url)
                                putExtra(
                                    PhotoViewActivity.EXTRA_DEPENDENCY_TAG,
                                    post.pWrapper.dependencyTag
                                )
                            },
                            ActivityOptionsCompat.makeSceneTransitionAnimation(
                                activity, holder.binding.ivPreview, SHARE_IMAGEVIEW
                            ).toBundle()
                        )
                    }
                }
            }
        }
    }

    override fun getItemCount() = urls.size


    inner class ActionCallback : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            mode?.menuInflater?.inflate(R.menu.menu_post_selected, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            return true
        }

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            when (item?.itemId) {
                R.id.menu_download -> {
                    viewModel.download(selection)
                    mode?.finish()
                }
            }
            return true
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            isSelectionMode = false
            selection.clear()
            notifyDataSetChanged()
        }
    }
}