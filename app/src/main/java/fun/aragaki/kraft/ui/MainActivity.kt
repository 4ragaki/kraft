package `fun`.aragaki.kraft.ui

import `fun`.aragaki.kraft.R
import `fun`.aragaki.kraft.Settings
import `fun`.aragaki.kraft.databinding.ActivityMainBinding
import `fun`.aragaki.kraft.databinding.FragmentMainBinding
import `fun`.aragaki.kraft.ext.findUrls
import `fun`.aragaki.kraft.ext.getDocumentTree
import `fun`.aragaki.kraft.ext.registerRequestDocumentTree
import `fun`.aragaki.kraft.ext.toast
import `fun`.aragaki.kraft.ui.base.BaseActivity
import `fun`.aragaki.kraft.ui.preferences.CredentialsViewModel
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import org.kodein.di.DI
import org.kodein.di.android.retainedDI
import org.kodein.di.instance

class MainActivity : BaseActivity() {
    override val di: DI by retainedDI {
        extend(parentDI)
    }
    private val credentialsVM by viewModels<CredentialsViewModel> { ViewModelFactory }
    private val settings by instance<Settings>()
    lateinit var requestDocumentTree: ActivityResultLauncher<Uri>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        setupActionBarWithNavController(findNavController(R.id.mainHostFragment))
        requestDocumentTree = registerRequestDocumentTree(settings)

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

    class MainFragment : Fragment() {
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ) = FragmentMainBinding.inflate(inflater, container, false).apply {
            setHasOptionsMenu(true)
            btnLaunch.setOnClickListener {
                startActivity(
                    Intent.createChooser(
                        Intent(
                            Intent.ACTION_VIEW, Uri.Builder()
                                .scheme(etScheme.text.toString())
                                .authority(etAuthority.text.toString())
                                .path(etPath.text.toString())
                                .encodedQuery(etQuery.text.toString())
                                .build()
                        ), getString(R.string.title_choose)
                    )
                )
            }

            requireActivity().intent.let {
                if (it.action == Intent.ACTION_SEND)
                    Uri.parse(it.getStringExtra(Intent.EXTRA_TEXT)?.findUrls()?.first())
                else it.data
            }?.let {
                etScheme.setText(it.scheme)
                etAuthority.setText(it.authority)
                etPath.setText(it.path)
                etQuery.setText(it.query)
            }
        }.root

        override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
            inflater.inflate(R.menu.menu_main, menu)
            super.onCreateOptionsMenu(menu, inflater)
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.action_main_preferences -> findNavController().navigate(R.id.nav_main_Home2Preferences)
            }
            return super.onOptionsItemSelected(item)
        }
    }
}