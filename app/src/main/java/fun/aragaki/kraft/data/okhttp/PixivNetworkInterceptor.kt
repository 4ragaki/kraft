package `fun`.aragaki.kraft.data.okhttp

import okhttp3.Interceptor
import okhttp3.Response

object PixivNetworkInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val pixivHeaders = PixivHeaders()
        val request = chain.request().newBuilder()
            .addHeader("X-Client-Time", pixivHeaders.xClientTime)
            .addHeader("X-Client-Hash", pixivHeaders.xClientHash)
            .addHeader("Referer", "https://app-api.pixiv.net/")
            .build()

        return chain.proceed(request)
    }
}