package `fun`.aragaki.kraft.ui

import `fun`.aragaki.kraft.R
import `fun`.aragaki.kraft.Settings
import `fun`.aragaki.kraft.databinding.ActivityMainBinding
import `fun`.aragaki.kraft.databinding.FragmentMainBinding
import `fun`.aragaki.kraft.ext.getDocumentTree
import `fun`.aragaki.kraft.ext.registerRequestDocumentTree
import `fun`.aragaki.kraft.ui.base.BaseActivity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.activity.result.ActivityResultLauncher
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
            btnStartPost.setOnClickListener {
                startActivity(
                    Intent.createChooser(
                        Intent(
                            Intent.ACTION_VIEW, Uri.parse(etUrl.text.toString())
                        ), getString(R.string.title_choose)
                    )
                )
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