package `fun`.aragaki.kraft.ui.preferences

import `fun`.aragaki.kraft.R
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat

class CredentialsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences_credentials)
    }
}