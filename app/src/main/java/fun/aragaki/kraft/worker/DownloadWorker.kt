package `fun`.aragaki.kraft.worker

import `fun`.aragaki.kraft.R
import `fun`.aragaki.kraft.Settings
import `fun`.aragaki.kraft.extensions.getDocumentTree
import android.content.Context
import androidx.core.app.NotificationManagerCompat
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import okhttp3.Headers
import okhttp3.OkHttpClient
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI
import org.kodein.di.instance

class DownloadWorker(private val context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters), DIAware {
    override val di: DI by closestDI(context)

    private val settings by instance<Settings>()
    private lateinit var helper: DownloadCompanion.NotificationHelper

    override fun doWork(): Result {
        val retry =
            settings.failureAction.value == context.getString(R.string.failure_action_value_retry)
        inputData.apply {
            helper = DownloadCompanion.NotificationHelper(
                context,
                NotificationManagerCompat.from(context),
                getInt(DownloadParams.notificationId, 0)
            )

            getString(DownloadParams.url)?.let { url ->
                getString(DownloadParams.filename)?.let { filename ->
                    helper.registerChannel()
                    val docTreeAuthority = settings.docTreeAuthority.value
                    val docTreeId = settings.docTreeId.value
                    val folder = getStringArray(DownloadParams.folder)
                    val client by instance<OkHttpClient>(getString(DownloadParams.dependencyTag))

                    val title = buildString {
                        append(folder?.joinToString("/"))
                        append('/')
                        append(filename)
                    }
                    kotlin.runCatching {
                        var documentTree = context.getDocumentTree(docTreeAuthority!!, docTreeId!!)

                        synchronized(DownloadWorker::class) {
                            folder?.forEach { dir ->
                                documentTree =
                                    documentTree?.findFile(dir).takeIf { it?.isDirectory ?: false }
                                        ?: documentTree?.createDirectory(dir)
                            }
                        }

                        val headers = getStringArray(DownloadParams.headers)
                            ?.let { Headers.headersOf(*it) }
                        val downloader = Downloader(client)

                        val contentLength = getLong(
                            DownloadParams.contentLength,
                            Downloader.UNDEFINED_CONTENT_LENGTH
                        )
                        val mime = getString(DownloadParams.mime) ?: "*/*"

                        val file = synchronized(DownloadWorker::class) {
                            documentTree?.findFile(filename)
                                ?: documentTree?.createFile(mime, filename)!!
                        }
                        downloader.download(
                            context, url, headers, file, _contentLength = contentLength,
                            progressCallback = { max, progress ->
                                helper.notifyProgress(max, progress, title)
                            }, successCallback = {
                                helper.notifySuccess(title, file.uri)
                            }
                        )

                        return Result.success(
                            Data.Builder()
                                .putString(DownloadOutput.filename, filename)
                                .putLong(DownloadOutput.time, System.currentTimeMillis())
                                .putString(DownloadOutput.uri, file.uri.toString())
                                .build()
                        )
                    }.onFailure {
                        return if (retry) Result.retry()
                        else {
                            helper.notifyFailed(title, it.message ?: "", toByteArray())
                            Result.failure()
                        }
                    }
                }
            }
        }
        return if (retry) Result.retry() else Result.failure()
    }
}