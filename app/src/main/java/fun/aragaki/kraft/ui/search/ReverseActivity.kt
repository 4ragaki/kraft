package `fun`.aragaki.kraft.ui.search

import `fun`.aragaki.kraft.HOST_IQDB
import `fun`.aragaki.kraft.HOST_SAUCENAO
import `fun`.aragaki.kraft.R
import `fun`.aragaki.kraft.databinding.ActivityReverseBinding
import `fun`.aragaki.kraft.ext.applyVerticalInsets
import `fun`.aragaki.kraft.ext.vbrClick
import `fun`.aragaki.kraft.ui.ViewModelFactory
import `fun`.aragaki.kraft.ui.base.BaseSwipeBackActivity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Vibrator
import android.view.*
import androidx.activity.viewModels
import androidx.core.content.getSystemService
import androidx.core.view.marginBottom
import androidx.core.view.updateLayoutParams
import androidx.core.view.updateMargins
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import org.kodein.di.DI
import org.kodein.di.android.retainedDI
import kotlin.collections.set

class ReverseActivity : BaseSwipeBackActivity() {
    override val di: DI by retainedDI {
        extend(parentDI)
    }

    private var serviceIndex = 0
    private val services = arrayOf(
        { switchAndCache(HOST_SAUCENAO) { SauceNaoFragment(image) } },
        { switchAndCache(HOST_IQDB) { WebViewFragment(image) } })
    private var service = services[serviceIndex]
    private val fragmentCache = mutableMapOf<String, Fragment>()
    private val viewModel by viewModels<ReverseViewModel> { ViewModelFactory }
    private lateinit var image: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityReverseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        viewModel.image.observe(this) {
            if (viewModel.queueEnd()) {
                binding.fabNext.visibility = View.GONE
            }
            fragmentCache.clear()
            image = it
            service()
        }

        binding.apply {
            val vibrator = getSystemService<Vibrator>()
            fabNext.setOnClickListener {
                vibrator?.vbrClick()
                viewModel.queuePoll()
            }

            val appBarPaddingTop = appbarLayout.paddingTop
            val fabNextMarginBottom = fabNext.marginBottom
            root.applyVerticalInsets(
                window, arrayOf(
                    { appbarLayout.updatePadding(top = appBarPaddingTop + it.top) },
                    {
                        fabNext.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                            updateMargins(bottom = fabNextMarginBottom + it.bottom)
                        }
                    }
                )
            )
        }

        when (intent?.action) {
            Intent.ACTION_SEND -> {
                intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)
                    ?.let { viewModel.queueOffer(it) }
            }
            Intent.ACTION_SEND_MULTIPLE -> {
                intent.getParcelableArrayListExtra<Uri>(Intent.EXTRA_STREAM)?.let {
                    it.forEach { uri ->
                        viewModel.queueOffer(uri)
                    }
                }
            }
        }

        viewModel.queuePoll()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_rev_switch -> {
                serviceIndex = ++serviceIndex % services.size
                service = services[serviceIndex]
                service()
            }

            android.R.id.home -> {
                finishAfterTransition()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun switchAndCache(key: String, creator: () -> Fragment) {
        supportFragmentManager.beginTransaction().run {
            replace(
                R.id.fragmentSearch,
                fragmentCache[key] ?: creator().also { fragmentCache[key] = it })
            commit()
        }

    }
}