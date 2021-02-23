package `fun`.aragaki.kraft.kodein

import `fun`.aragaki.kraft.Settings
import `fun`.aragaki.kraft.data.servicewrappers.*
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider

/**
 * supported by default
 */
const val YANDE_RE = "yande.re"
const val DANBOORU = "danbooru.donmai.us"
const val GELBOORU = "gelbooru.com"
const val SANKAKU = "chan.sankakucomplex.com"
const val KONACHAN = "konachan.com"
const val SAFEBOORU = "safebooru.donmai.us"
const val PIXIV = "pixiv.net"

val boorusModule = DI.Module("boorusModule") {

    bind<MoebooruWrapper>(YANDE_RE) with provider {
        val settings = instance<Settings>()
        instance<Boorus.Moebooru, MoebooruWrapper>(
            /**
             * Logging In
            Some actions may require you to log in. For any action you can always specify two parameters to identify yourself:

            login Your login name.
            password_hash Your SHA1 hashed password. Simply hashing your plain password will NOT work since Danbooru salts its passwords. The actual string that is hashed is "choujin-steiner--your-password--".
            Please be aware of the security risks involved in sending your password through an unencrypted channel. Although your password will be hashed, it is still theoretically possible for someone to steal your account by creating a fake cookie based on your hashed password.
             */
            arg = Boorus.Moebooru(
                settings.yandereUsername.value,
                settings.yandereHash.value,
                0,
                "Yande.re",
                "https",
                "yande.re",
                "choujin-steiner--your-password--",
                Boorus.Moebooru.patternPostId(YANDE_RE),
                Boorus.Moebooru.patternPostsTags(YANDE_RE)
            )
        )
    }

    bind<DanbooruWrapper>(DANBOORU) with provider {
        val settings = instance<Settings>()
        instance<Boorus.Danbooru, DanbooruWrapper>(
            arg = Boorus.Danbooru(
                settings.danbooruUsername.value,
                settings.danbooruHash.value,
                0,
                "Danbooru",
                "https",
                "danbooru.donmai.us",
                Boorus.Danbooru.patternPostId(DANBOORU),
                Boorus.Danbooru.patternPostsTags(DANBOORU)
            )
        )
    }

    bind<GelbooruWrapper>(GELBOORU) with provider {
        val settings = instance<Settings>()
        instance<Boorus.Gelbooru, GelbooruWrapper>(
            arg = Boorus.Gelbooru(
                settings.gelbooruUsername.value,
                settings.gelbooruHash.value,
                0,
                "Gelbooru",
                "https",
                "gelbooru.com",
                Boorus.Gelbooru.patternPostId(GELBOORU),
                Boorus.Gelbooru.patternPostsTags(GELBOORU)
            )
        )
    }

    bind<SankakuWrapper>(SANKAKU) with provider {
        val settings = instance<Settings>()
        instance<Boorus.Sankaku, SankakuWrapper>(
            arg = Boorus.Sankaku(
                settings.sankakuUsername.value,
                settings.sankakuHash.value,
                0,
                "Sankaku",
                "https",
                "capi-v2.sankakucomplex.com",
                "choujin-steiner--your-password--",
                Boorus.Sankaku.patternPostId(SANKAKU),
                Boorus.Sankaku.patternPostsTags(SANKAKU)
            )
        )
    }

    bind<MoebooruWrapper>(KONACHAN) with provider {
        val settings = instance<Settings>()
        instance<Boorus.Moebooru, MoebooruWrapper>(
            /**
             * Logging In
            Some actions may require you to log in. For any action you can always specify two parameters to identify yourself:

            login Your login name.
            password_hash Your SHA1 hashed password. Simply hashing your plain password will NOT work since Danbooru salts its passwords. The actual string that is hashed is "So-I-Heard-You-Like-Mupkids-?--your-password--".
            Please be aware of the security risks involved in sending your password through an unencrypted channel. Although your password will be hashed, it is still theoretically possible for someone to steal your account by creating a fake cookie based on your hashed password.
             */
            arg = Boorus.Moebooru(
                settings.konachanUsername.value,
                settings.konachanHash.value,
                1,
                "Konachan",
                "https",
                "konachan.com",
                "So-I-Heard-You-Like-Mupkids-?--your-password--",
                Boorus.Moebooru.patternPostId(KONACHAN),
                Boorus.Moebooru.patternPostsTags(KONACHAN)
            )
        )
    }

    bind<DanbooruWrapper>(SAFEBOORU) with provider {
        val settings = instance<Settings>()
        instance<Boorus.Danbooru, DanbooruWrapper>(
            arg = Boorus.Danbooru(
                settings.safebooruUsername.value,
                settings.safebooruHash.value,
                1,
                "Safebooru",
                "https",
                "safebooru.donmai.us",
                Boorus.Danbooru.patternPostId(SAFEBOORU),
                Boorus.Danbooru.patternPostsTags(SAFEBOORU)
            )
        )
    }

    bind<PixivWrapper>(PIXIV) with provider {
        val settings = instance<Settings>()
        instance<Boorus.Pixiv, PixivWrapper>(
            arg = Boorus.Pixiv(
                0, "Pixiv", "https", "oauth.secure.pixiv.net",
                "https", "app-api.pixiv.net", settings.pixivAccessToken.value,
                settings.pixivRefreshToken.value, { accessToken, refreshToken ->
                    settings.edit {
                        it.putString(settings.pixivAccessToken.key, accessToken)
                        it.putString(settings.pixivRefreshToken.key, refreshToken)
                    }
                }, Boorus.Pixiv.patternPostId(PIXIV), Boorus.Pixiv.patternPostsTags(PIXIV)
            )
        )
    }
}