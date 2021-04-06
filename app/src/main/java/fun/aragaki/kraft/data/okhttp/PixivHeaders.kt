package `fun`.aragaki.kraft.data.okhttp

import `fun`.aragaki.kraft.extensions.hash
import java.text.SimpleDateFormat
import java.util.*


/**
 * @author Aragaki
 *
 * thanks to https://github.com/upbit/pixivpy/issues/83
 */
class PixivHeaders {
    val xClientTime: String = format.format(Date())
    val xClientHash =
        "${xClientTime}28c1fdd170a5204386cb1313c7077b34f83e4aaf4aa829ce78c231e05b0bae2c".hash("md5")

    companion object {
        private val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ", Locale.US)
    }
}
