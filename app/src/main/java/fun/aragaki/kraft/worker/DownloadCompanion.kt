package `fun`.aragaki.kraft.worker

import `fun`.aragaki.kraft.R
import `fun`.aragaki.kraft.data.extensions.Post
import `fun`.aragaki.kraft.receivers.DownloadReceiver
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.*

object DownloadCompanion {
    private var AVAILABLE_NOTIFICATION_ID = 0
    private var AVAILABLE_REQUEST_CODE = 0
    const val DOWNLOADS_TAG = "downloads"

    fun download(context: Context, download: Post.Download) {
        val data = Data.Builder()
            .putString(DownloadParams.url, download.url)
            .apply {
                download.folder?.let {
                    putStringArray(DownloadParams.folder, it.toTypedArray())
                }
            }
            .putString(DownloadParams.filename, download.filename)
            .apply {
                download.headers?.let {
                    putStringArray(DownloadParams.headers, it)
                }
            }
            .putLong(DownloadParams.contentLength, download.contentLength)
            .putString(DownloadParams.mime, download.mime)
            .putInt(DownloadParams.notificationId, AVAILABLE_NOTIFICATION_ID++)
            .putString(DownloadParams.dependencyTag, download.dependencyTag)
            .build()
        WorkManager.getInstance(context).enqueue(
            OneTimeWorkRequest.Builder(DownloadWorker::class.java)
                .setConstraints(buildConstraints())
                .setInputData(data)
                .addTag(DOWNLOADS_TAG)
                .build()
        )
    }

    fun download(context: Context, task: ByteArray) {
        WorkManager.getInstance(context).enqueue(
            OneTimeWorkRequest.Builder(DownloadWorker::class.java)
                .setConstraints(buildConstraints())
                .setInputData(Data.fromByteArray(task))
                .addTag(DOWNLOADS_TAG)
                .build()
        )
    }

    private fun buildConstraints() = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    class NotificationHelper(
        private val context: Context,
        private val mgr: NotificationManagerCompat,
        private val notificationId: Int
    ) {

        fun registerChannel() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mgr.createNotificationChannel(
                    NotificationChannel(
                        NOTIFICATION_CHANNEL_ID,
                        context.getString(R.string.downloads_channel_name),
                        NotificationManager.IMPORTANCE_LOW
                    )
                )
            }
        }

        fun notifyProgress(max: Int, progress: Int, filename: String) {
            mgr.notify(
                DOWNLOADS_TAG, notificationId,
                buildProgressNotification(max, progress, filename)
            )
        }

        fun notifySuccess(title: String, uri: Uri) {
            mgr.notify(DOWNLOADS_TAG, notificationId, buildSuccessNotification(title, uri))
        }

        fun notifyFailed(title: String, content: String, task: ByteArray) {
            mgr.notify(
                DOWNLOADS_TAG,
                notificationId,
                buildFailureNotification(title, content, task)
            )
        }

        private fun buildProgressNotification(
            max: Int, progress: Int, title: String
        ): Notification =
            NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_download_progressing)
                .setCategory(NotificationCompat.CATEGORY_PROGRESS)
                .setContentTitle(title)
                .setProgress(max, progress, false)
                .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_SUMMARY)
                .setOnlyAlertOnce(true)
                .setOngoing(true)
                .build()

        private fun buildSuccessNotification(title: String, uri: Uri): Notification {
            val bigPicture = NotificationCompat.BigPictureStyle().bigPicture(
                BitmapFactory.decodeFileDescriptor(
                    context.contentResolver.openFileDescriptor(uri, "r")?.fileDescriptor
                )
            )
            val pendingIntent = PendingIntent.getActivity(
                context, AVAILABLE_REQUEST_CODE++,
                Intent(Intent.ACTION_VIEW, uri),
                PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
            )
            return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_download_success)
                .setStyle(bigPicture)
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_SUMMARY)
                .setAutoCancel(true)
                .build()
        }

        private fun buildFailureNotification(
            title: String, msg: String, task: ByteArray
        ): Notification {
            val retry = PendingIntent.getBroadcast(
                context, AVAILABLE_REQUEST_CODE++,
                Intent(context, DownloadReceiver::class.java).apply {
                    action = DownloadReceiver.ACTION_RETRY
                    putExtra(DownloadReceiver.EXTRA_TASK, task)
                }, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
            )
            val cancel = PendingIntent.getBroadcast(
                context, AVAILABLE_REQUEST_CODE++,
                Intent(context, DownloadReceiver::class.java).apply {
                    action = DownloadReceiver.ACTION_CANCEL
                    putExtra(DownloadReceiver.EXTRA_TASK, task)
                }, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
            )
            return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_download_failure)
                .setContentTitle(title)
                .setContentText(msg)
                .addAction(
                    R.drawable.ic_launcher_foreground,
                    context.getString(R.string.download_action_retry), retry
                )
                .addAction(
                    R.drawable.ic_launcher_foreground,
                    context.getString(R.string.download_action_cancel),
                    cancel
                )
                .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_SUMMARY)
                .setOngoing(true)
                .build()
        }

        companion object {
            private const val NOTIFICATION_CHANNEL_ID = "downloads"
        }
    }
}