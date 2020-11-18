package `fun`.aragaki.kraft.data.okhttp

import android.util.Log
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File

class LogInterceptor(private val file: File, private val tag: String) :
    HttpLoggingInterceptor.Logger {

    override fun log(message: String) {

        Log.i(tag, message)

//        if (BuildConfig.DEBUG) {
//            file.appendText(buildString {
//                append(message)
//                append("\n")
//            })
//    }
    }
}