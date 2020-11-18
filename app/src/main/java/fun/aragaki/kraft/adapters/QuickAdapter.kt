package `fun`.aragaki.kraft.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class QuickAdapter<B : ViewBinding, H : BaseHolder<B>, I>(
    private val items: List<I>,
    private val clicks: List<(H) -> Unit?>?,
    private val factory: (ViewGroup, Int) -> H,
    private val bind: (H, I) -> Unit
) : RecyclerView.Adapter<H>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = factory(parent, viewType)
    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: H, position: Int) {
        clicks?.let { holder.binding.root.setOnClickListener { clicks[position](holder) } }
        bind(holder, items[position])
    }
}