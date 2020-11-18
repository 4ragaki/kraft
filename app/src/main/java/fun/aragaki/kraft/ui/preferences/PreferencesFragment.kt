package `fun`.aragaki.kraft.ui.preferences

import `fun`.aragaki.kraft.R
import `fun`.aragaki.kraft.ui.MainActivity
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

class PreferencesFragment : PreferenceFragmentCompat() {
    private val selectDocTree by lazy { getString(R.string.pref_key_select_doc_tree) }
    private val checkCredentials by lazy { getString(R.string.pref_key_check_credential) }
    private val recentWorks by lazy { getString(R.string.pref_key_recent_works) }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        when (preference?.key) {
            selectDocTree -> {
                val mainActivity = requireActivity() as MainActivity
                mainActivity.requestDocumentTree.launch(null)
            }
            checkCredentials -> findNavController().navigate(R.id.nav_main_Preferences2Credentials)
            recentWorks -> findNavController().navigate(R.id.nav_main_Preferences2Works)
        }
        return super.onPreferenceTreeClick(preference)
    }
}