package `fun`.aragaki.kraft.ui.tags

import `fun`.aragaki.kraft.SCHEME
import `fun`.aragaki.kraft.adapters.BaseHolder
import `fun`.aragaki.kraft.data.extensions.Tag
import `fun`.aragaki.kraft.databinding.ItemTagsTagBinding
import `fun`.aragaki.kraft.extensions.startActivity
import `fun`.aragaki.kraft.kodein.YANDE_RE
import `fun`.aragaki.kraft.ui.posts.PostsActivity
import android.net.Uri

class TagHolder(binding: ItemTagsTagBinding) : BaseHolder<ItemTagsTagBinding>(binding) {
    lateinit var tag: Tag

    init {
        binding.root.setOnClickListener {
            if (this::tag.isInitialized) {
                it.context.startActivity<PostsActivity> {
                    data = Uri.parse("$SCHEME://$YANDE_RE/posts?tags=${tag.tagGetName()}")
                }
            }
        }
    }

    fun bind(item: Tag?) = binding.apply {
        tvName.text = item?.tagGetName()
        tvCount.text = item?.tagGetType().toString()
        item?.let { tag = it }
    }
}