package `fun`.aragaki.kraft.ui.preferences

import `fun`.aragaki.kraft.R
import `fun`.aragaki.kraft.ui.ViewModelFactory
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.closestDI
import org.kodein.di.instance

class CredentialsFragment : PreferenceFragmentCompat(), DIAware {
    override val di: DI by closestDI()
    private val viewModelFactory by instance<ViewModelFactory>()
    private val viewModel by viewModels<CredentialsViewModel>({ requireActivity() }) { viewModelFactory }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences_credentials)

        findPreference<Preference>(getString(R.string.pref_key_cre_pixiv_login))?.setOnPreferenceClickListener {
            startActivity(viewModel.genPixivAuthIntent())
            true
        }
    }
}