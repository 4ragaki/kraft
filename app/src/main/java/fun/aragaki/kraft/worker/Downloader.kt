package `fun`.aragaki.kraft.worker

import `fun`.aragaki.kraft.BuildConfig
import android.content.Context
import android.util.Log
import androidx.documentfile.provider.DocumentFile
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.internal.toLongOrDefault
import okio.buffer
import okio.sink

class Downloader(val client: OkHttpClient) {

    private fun getContentLength(url: String, headers: Headers?) = client.newCall(
        Request.Builder()
            .head()
            .url(url)
            .apply { headers?.let { headers(it) } }
            .build()
    ).execute().headers["Content-Length"]?.toLongOrDefault(-1L) ?: -1L

    fun download(
        context: Context, url: String, headers: Headers?, file: DocumentFile,
        _contentLength: Long = UNDEFINED_CONTENT_LENGTH,
        progressCallback: (max: Int, progress: Int) -> Unit, successCallback: () -> Unit
    ) {
        val contentLength =
            if (_contentLength == UNDEFINED_CONTENT_LENGTH) getContentLength(
                url, headers
            ) else _contentLength

        val length = file.length()
        if (length != contentLength) {
            progressCallback(100, 0)

            context.contentResolver.openOutputStream(file.uri)?.let { out ->
                val sink = out.sink().buffer()
                val request = Request.Builder()
                    .get()
                    .url(url)
                    .apply { headers?.let { headers(it) } }
                    .build()

                var totalRead = 0
                val source = client.newCall(request).execute().body?.source()!!
                val buffer = ByteArray(BUFFER_SIZE)
                var startTime = System.currentTimeMillis()
                while (!source.exhausted()) {
                    val read = source.read(buffer, 0, BUFFER_SIZE)
                    totalRead += if (read == -1) 0 else read
                    sink.write(buffer, 0, read)
                    val time = System.currentTimeMillis()
                    if (time - startTime > CALLBACK_INTERVAL) {
                        if (BuildConfig.DEBUG) Log.i(tag, "$read $totalRead $contentLength $url")
                        progressCallback(
                            100, ((totalRead.toFloat() / contentLength.toFloat()) * 100f).toInt()
                        )
                        startTime = time
                    }
                }

                sink.flush()
                sink.close()
                source.close()
                successCallback()
            }
        } else {
            successCallback()
        }
    }

    companion object {
//        init {
//            System.loadLibrary("kraft")
//        }

        const val CALLBACK_INTERVAL = 1000
        const val BUFFER_SIZE = 8 * 1024
        const val tag = "Downloader"
        const val UNDEFINED_CONTENT_LENGTH = -1L
    }
}