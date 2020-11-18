package `fun`.aragaki.kraft.ui.preferences

import `fun`.aragaki.kraft.Kraft
import `fun`.aragaki.kraft.worker.DownloadCompanion
import androidx.lifecycle.AndroidViewModel
import androidx.work.WorkManager
import androidx.work.WorkQuery

class WorksViewModel(kraft: Kraft) : AndroidViewModel(kraft) {
    val works by lazy {
        WorkManager.getInstance(kraft).getWorkInfosLiveData(
            WorkQuery.Builder.fromTags(listOf(DownloadCompanion.DOWNLOADS_TAG)).build()
        )
    }
}