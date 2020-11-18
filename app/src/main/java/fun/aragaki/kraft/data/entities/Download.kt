package `fun`.aragaki.kraft.data.entities

import `fun`.aragaki.kraft.worker.Downloader

class Download(
    val url: String,
    val folder: Array<String>?,
    val dependencyTag: String?,
    val filename: String,
    val headers: Array<String>? = null,
    val contentLength: Long = Downloader.UNDEFINED_CONTENT_LENGTH,
    val mime: String? = null
)
