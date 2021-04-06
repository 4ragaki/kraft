package `fun`.aragaki.kraft.extensions

import java.math.BigInteger
import java.net.URLDecoder
import java.net.URLEncoder
import java.security.MessageDigest
import java.text.DateFormat
import java.text.SimpleDateFormat

val String.nameWithoutExt: String get() = substringBeforeLast(".")

val String.extension: String get() = substringAfterLast('.', "")

fun String.splitByBlank(): List<String>? {
    trim()
    return if (isNotBlank()) split(Regex("\\s+")) else null
}

fun String.hash(algorithm: String): String {
    MessageDigest.getInstance(algorithm).apply {
        update(toByteArray())
        return BigInteger(1, digest()).toString(16)
    }
}

fun String.toBearer() = "Bearer $this"

fun String.findUrls() = Regex("""(http|https)://\S+""").findAll(this).map { it.value }

fun String.urlEncode() = URLEncoder.encode(this, "utf-8")
fun String.urlDecode() = URLDecoder.decode(this, "utf-8")

val dateFormatter: DateFormat = SimpleDateFormat.getDateTimeInstance()