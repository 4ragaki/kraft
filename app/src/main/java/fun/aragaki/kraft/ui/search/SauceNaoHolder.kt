package `fun`.aragaki.kraft.ui.search

import `fun`.aragaki.kraft.adapters.BaseHolder
import `fun`.aragaki.kraft.data.entities.SauceNaoResponse
import `fun`.aragaki.kraft.databinding.ItemSaucenaoBinding
import android.annotation.SuppressLint
import coil.api.load

class SauceNaoHolder(binding: ItemSaucenaoBinding) : BaseHolder<ItemSaucenaoBinding>(binding) {
    private lateinit var _result: SauceNaoResponse.Result

    @SuppressLint("SetTextI18n")
    fun bind(result: SauceNaoResponse.Result) {
        _result = result

        binding.apply {
            ivPreview.load(result.header?.thumbnail) { crossfade(true) }
            result.data?.let {
                tvInfo.text = it.title ?: it.pixiv_id?.let { id -> "Pixiv $id" }
                        ?: it.danbooru_id?.let { id -> "Danbooru $id" }
                        ?: it.sankaku_id?.let { id -> "Sankaku $id" }
                        ?: it.seiga_id?.let { id -> "Seiga $id" }
                        ?: it.pawoo_id?.let { id -> "Pawoo $id" }
                        ?: it.getchu_id?.let { id -> "Getchu $id" }
                        ?: it.da_id?.let { id -> "Da $id" }
                        ?: it.bcy_id?.let { id -> "BCY $id" }
                        ?: it.anidb_aid?.let { id -> "AniDB $id" }
                        ?: it.characters ?: it.author_name ?: "<empty>"
            }
            tvUrls.text = result.data?.ext_urls?.joinToString("\n")
            tvSimilarity.text = "${result.header?.similarity}%"
        }
    }
}