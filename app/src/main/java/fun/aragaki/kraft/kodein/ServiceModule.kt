package `fun`.aragaki.kraft.kodein

import `fun`.aragaki.kraft.BuildConfig
import `fun`.aragaki.kraft.TAG_OKHTTP_LOG
import `fun`.aragaki.kraft.TAG_PIXIV
import `fun`.aragaki.kraft.data.okhttp.LogInterceptor
import `fun`.aragaki.kraft.data.okhttp.PixivNetworkInterceptor
import `fun`.aragaki.kraft.data.services.IQDBService
import `fun`.aragaki.kraft.data.services.SauceNaoService
import `fun`.aragaki.kraft.data.servicewrappers.*
import android.content.Context
import coil.Coil
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.SvgDecoder
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.*
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

val serviceModule = DI.Module("serviceModule") {
    bind<OkHttpClient>() with singleton {
        OkHttpClient.Builder()
            .addInterceptor(instance<String, HttpLoggingInterceptor>(arg = "defaultClient"))
            .build()
    }

    bind<OkHttpClient>(TAG_PIXIV) with singleton {
        OkHttpClient.Builder()
            .addInterceptor(instance<String, HttpLoggingInterceptor>(arg = "pixivClient"))
            .addNetworkInterceptor(PixivNetworkInterceptor)
            .build()
    }

    bind<File>(TAG_OKHTTP_LOG) with singleton {
        File(
            instance<Context>().cacheDir,
            "okhttp-${SimpleDateFormat("yy-MM-dd", Locale.getDefault()).format(Date())}.log"
        )
    }

    bind<HttpLoggingInterceptor>() with multiton { tag: String ->
        HttpLoggingInterceptor(
            LogInterceptor(instance(TAG_OKHTTP_LOG), tag)
        ).apply {
            level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BASIC else HttpLoggingInterceptor.Level.BASIC
        }
    }

    bind<ImageLoader>() with singleton {
        ImageLoader.Builder(instance())
            .okHttpClient(instance<OkHttpClient>())
            .componentRegistry {
                add(instance<GifDecoder>())
                add(instance<SvgDecoder>())
            }
            .build()
    }

    bind<ImageLoader>(TAG_PIXIV) with singleton {
        ImageLoader.Builder(instance())
            .okHttpClient(instance<OkHttpClient>(TAG_PIXIV))
            .componentRegistry {
                add(instance<GifDecoder>())
                add(instance<SvgDecoder>())
            }
            .build()
    }

    bind<GsonConverterFactory>() with singleton {
        GsonConverterFactory.create()
    }

    bind<TikXmlConverterFactory>() with singleton {
        TikXmlConverterFactory.create(
            TikXml.Builder()
                .exceptionOnUnreadXml(false)
                .build()
        )
    }


    bind<MoebooruWrapper>() with multiton { moebooru: Boorus.Moebooru ->
        MoebooruWrapper(moebooru, instance(), instance())
    }


    bind<DanbooruWrapper>() with multiton { danbooru: Boorus.Danbooru ->
        DanbooruWrapper(danbooru, instance(), instance())
    }


    bind<GelbooruWrapper>() with multiton { gelbooru: Boorus.Gelbooru ->
        GelbooruWrapper(gelbooru, instance(), instance())
    }


    bind<SankakuWrapper>() with multiton { sankaku: Boorus.Sankaku ->
        SankakuWrapper(sankaku, instance(), instance())
    }

    bind<PixivWrapper>() with multiton { pixiv: Boorus.Pixiv ->
        PixivWrapper(pixiv, instance(TAG_PIXIV), instance(), TAG_PIXIV)
    }

    bind<SauceNaoService>() with singleton { SauceNaoService(instance(), instance()) }

    bind<IQDBService>() with singleton { IQDBService(instance()) }
}