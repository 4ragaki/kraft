package `fun`.aragaki.kraft.data

import android.net.Uri

class UnsupportedException(val uri: Uri) : Exception("$uri Unsupported")