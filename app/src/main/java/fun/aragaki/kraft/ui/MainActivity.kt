package `fun`.aragaki.kraft.ui

import `fun`.aragaki.kraft.R
import `fun`.aragaki.kraft.Settings
import `fun`.aragaki.kraft.databinding.ActivityMainBinding
import `fun`.aragaki.kraft.extensions.getDocumentTree
import `fun`.aragaki.kraft.extensions.snack
import `fun`.aragaki.kraft.extensions.startActivity
import `fun`.aragaki.kraft.extensions.toast
import `fun`.aragaki.kraft.supportedLinks
import `fun`.aragaki.kraft.ui.base.BaseActivity
import `fun`.aragaki.kraft.ui.post.PostActivity
import `fun`.aragaki.kraft.ui.preferences.CredentialsViewModel
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.NonNull
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.getSystemService
import androidx.core.view.doOnLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.retainedDI
import org.kodein.di.instance
import java.util.*

class MainActivity : BaseActivity(), DIAware {
    override val di: DI by retainedDI { extend(parentDI) }
    private val viewModelFactory by instance<ViewModelFactory>()
    private val clipboard by lazy { getSystemService<ClipboardManager>() }
    private val credentialsVM by viewModels<CredentialsViewModel> { viewModelFactory }
    private val settings by instance<Settings>()
    private val biometricManager by lazy { BiometricManager.from(this) }

    val requestDocumentTree =
        registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) {
            it?.let {
                contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                settings.edit {
                    putString(settings.docTreeAuthority.key, it.authority)
                    putString(settings.docTreeId.key, DocumentsContract.getTreeDocumentId(it))
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)

        if (settings.bioAuth.value) {
            BiometricPrompt(this, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    finishAfterTransition()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    finishAfterTransition()
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    doCreate(binding)
                }
            }).authenticate(
                BiometricPrompt.PromptInfo.Builder()
                    .setTitle(getString(R.string.pref_title_bio_auth))
                    .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                    .build()
            )
        } else doCreate(binding)

    }

    private fun doCreate(binding: ActivityMainBinding) {
        setContentView(binding.apply {
            root.doOnLayout {
                if (settings.detectClipBoard.value) {
                    clipboard?.primaryClip?.getItemAt(0)?.text?.let out@{ str ->
                        val input = str.toString().lowercase(Locale.getDefault())
                        supportedLinks.forEach {
                            it.find(input)?.let { result ->
                                coordinator.snack(
                                    getString(R.string.snack_text_found_link),
                                    getString(R.string.snack_action_open_link)
                                ) {
                                    startActivity<PostActivity> { data = Uri.parse(result.value) }
                                }
                                return@out
                            }
                        }
                    }
                }
            }
        }.root)

        findNavController(R.id.mainHostFragment).let {
//            setupActionBarWithNavController(it)
            NavigationUI.setupWithNavController(binding.navView, it)
        }



        checkDocumentTree()

        dispatch(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let { dispatch(it) }
    }

    private fun dispatch(intent: Intent) {
        intent.data?.let {
            credentialsVM.handle(it) { host ->
                toast(getString(R.string.fmt_login_success).format(host))
                recreate()
            }
        }
    }

    private fun checkDocumentTree() {
        if (!isDocumentTreeValid()) {
            requestDocumentTree.launch(null)
        }
    }

    private fun isDocumentTreeValid(): Boolean {
        settings.docTreeAuthority.value?.let { authority ->
            settings.docTreeId.value?.let { treeId ->
                getDocumentTree(authority, treeId)?.run {
                    return canRead() and canWrite()
                }
            }
        }
        return false
    }

    override fun onSupportNavigateUp() =
        findNavController(R.id.mainHostFragment).navigateUp() || super.onSupportNavigateUp()
}