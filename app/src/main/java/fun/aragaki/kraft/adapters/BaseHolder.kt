package `fun`.aragaki.kraft.adapters

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseHolder<B : ViewBinding>(val binding: B) :
    RecyclerView.ViewHolder(binding.root)