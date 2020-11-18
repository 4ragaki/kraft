package `fun`.aragaki.kraft.ui.preferences

import `fun`.aragaki.kraft.adapters.BaseHolder
import `fun`.aragaki.kraft.databinding.ItemWorkBinding
import `fun`.aragaki.kraft.worker.DownloadOutput
import android.net.Uri
import androidx.work.WorkInfo
import coil.load

class WorkHolder(binding: ItemWorkBinding) : BaseHolder<ItemWorkBinding>(binding) {

    fun bind(info: WorkInfo) {
        val uri = Uri.parse(info.outputData.getString(DownloadOutput.uri))
        binding.apply {
            ivPreview.load(uri) {
                crossfade(true)
            }
            tvName.text = info.outputData.getString(DownloadOutput.filename)
        }
    }
}