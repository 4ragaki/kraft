package `fun`.aragaki.kraft

const val POSTS_DIR = "posts"
const val SCHEME = "kraft"
const val PARAMETER_POST_ID = "id"
const val PARAMETER_POSTS_TAGS = "tags"


const val TAG_OKHTTP_LOG = "okhttp.log"

const val TAG_PIXIV = "pixiv"

const val HOST_SAUCENAO = "saucenao.com"
const val HOST_IQDB = "iqdb.org"
const val HOST_ASCII2D = "ascii2d.net"
const val HOST_GOOGLE = "www.google.com"
const val HOST_TINEYE = "tineye.com"

const val SHARE_IMAGEVIEW = "photo"


val supportedLinks = arrayOf(Regex("$SCHEME://.+"), Regex("(http|https)://.+"))
