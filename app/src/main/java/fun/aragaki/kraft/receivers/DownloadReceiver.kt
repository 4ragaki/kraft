package `fun`.aragaki.kraft.receivers

import `fun`.aragaki.kraft.worker.DownloadCompanion
import `fun`.aragaki.kraft.worker.DownloadParams
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import androidx.work.Data

class DownloadReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        when (intent?.action) {
            ACTION_RETRY -> {
                intent.getByteArrayExtra(EXTRA_TASK)
                    ?.let { DownloadCompanion.download(context!!, it) }
            }

            ACTION_CANCEL -> {
                intent.getByteArrayExtra(EXTRA_TASK)?.let {
                    NotificationManagerCompat.from(context!!)
                        .cancel(
                            DownloadCompanion.DOWNLOADS_TAG,
                            Data.fromByteArray(it).getInt(DownloadParams.notificationId, 0)
                        )
                }

            }
        }
    }

    companion object {
        const val ACTION_RETRY = "download.retry"
        const val ACTION_CANCEL = "download.cancel"

        const val EXTRA_TASK = "download.task"
    }
}