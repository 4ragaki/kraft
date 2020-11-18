package `fun`.aragaki.kraft.ui.preferences

import `fun`.aragaki.kraft.R
import `fun`.aragaki.kraft.ui.MainActivity
import android.os.Bundle
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.*
import androidx.biometric.BiometricPrompt
import androidx.core.content.getSystemService
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat

class PreferencesFragment : PreferenceFragmentCompat() {
    private val selectDocTree by lazy { getString(R.string.pref_key_select_doc_tree) }
    private val checkCredentials by lazy { getString(R.string.pref_key_check_credential) }
    private val recentWorks by lazy { getString(R.string.pref_key_recent_works) }
    private val biometricManager by lazy { BiometricManager.from(requireContext()) }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)

        findPreference<SwitchPreferenceCompat>(getString(R.string.pref_key_bio_auth))?.apply {
            isEnabled =
                biometricManager.canAuthenticate(BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS

            setOnPreferenceChangeListener { preference, newValue ->
                BiometricPrompt(this@PreferencesFragment,
                    object : BiometricPrompt.AuthenticationCallback() {

                        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                            super.onAuthenticationSucceeded(result)
                            isChecked = !isChecked
                        }
                    }).authenticate(
                    BiometricPrompt.PromptInfo.Builder()
                        .setTitle(getString(R.string.pref_title_bio_auth))
                        .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
                        .build()
                )
                false
            }
        }
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        when (preference.key) {
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