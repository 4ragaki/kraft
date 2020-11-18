package `fun`.aragaki.kraft.kodein

import `fun`.aragaki.kraft.Settings
import `fun`.aragaki.kraft.data.PKCEParameters
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import coil.decode.GifDecoder
import coil.decode.SvgDecoder
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

val supportModule = DI.Module("supportModule") {
    bind<SharedPreferences>() with singleton {
        PreferenceManager.getDefaultSharedPreferences(instance())
    }

    bind<Settings>() with singleton { Settings(instance(), instance()) }

    bind<PKCEParameters>() with singleton { PKCEParameters() }

    bind<GifDecoder>() with singleton { GifDecoder() }
    bind<SvgDecoder>() with singleton { SvgDecoder(instance()) }
}