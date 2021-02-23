package `fun`.aragaki.kraft.kodein

import `fun`.aragaki.kraft.Settings
import `fun`.aragaki.kraft.data.PKCEParameters
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import org.kodein.di.*

val supportModule = DI.Module("supportModule") {
    bind<SharedPreferences>() with singleton {
        PreferenceManager.getDefaultSharedPreferences(instance())
    }

    bind<Settings>() with singleton { Settings(instance(), instance()) }

    bind<PKCEParameters>() with provider { PKCEParameters() }
}