package `fun`.aragaki.kraft.ui.search

import `fun`.aragaki.kraft.*
import `fun`.aragaki.kraft.data.services.IQDBService
import `fun`.aragaki.kraft.databinding.FragmentWebviewBinding
import `fun`.aragaki.kraft.ext.resolveColor
import `fun`.aragaki.kraft.ui.ViewModelFactory
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.closestDI

class WebViewFragment(private val image: Uri) : Fragment(), DIAware {
    override val di: DI by closestDI()

    lateinit var binding: FragmentWebviewBinding
    private val viewModel by viewModels<ReverseViewModel> { ViewModelFactory(Kraft.app) }

    private var mUrl: String = ""
    private var mTitle: String = ""
        set(value) {
            requireActivity().title = value
            field = value
        }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentWebviewBinding.inflate(inflater, container, false).apply {

        requireActivity().title = mTitle

        if (this@WebViewFragment::binding.isInitialized) {
            return binding.root
        }
        mTitle = getString(R.string.reverse_provider_iqdb)
        binding = this

        viewModel.iqdbResponse.observe(requireActivity()) {
            binding.swipeWeb.isRefreshing = false
            binding.progressLoading.visibility = View.GONE

            binding.web.loadDataWithBaseURL(
                IQDBService.BASE_URL, it.string(),
                it.contentType().toString(), null, IQDBService.BASE_URL
            )
        }

        swipeWeb.apply {
            setColorSchemeColors(requireActivity().resolveColor(R.attr.colorSecondary))
            setOnRefreshListener {
                binding.web.apply {
                    if (canGoBack()) reload() else request(image)
                }
            }
        }

        web.apply {
            registerForContextMenu(this)
            settings.javaScriptEnabled = true

            viewTreeObserver.addOnScrollChangedListener {
                binding.swipeWeb.isEnabled = scrollY == 0
            }

            webViewClient = object : WebViewClient() {
                private val whiteList =
                    sequenceOf(HOST_SAUCENAO, HOST_IQDB, HOST_ASCII2D, HOST_GOOGLE, HOST_TINEYE)

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    url?.let { mUrl = it }
                    binding.swipeWeb.isRefreshing = false
                }

                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    if (request?.url?.host in whiteList) return false
                    startActivity(
                        Intent.createChooser(
                            Intent(Intent(Intent.ACTION_VIEW, request?.url)), "Open url"
                        )
                    )
                    return true
                }
            }
            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                    if (newProgress == 100) {
                        binding.progressWeb.visibility = View.INVISIBLE
                    } else {
                        binding.progressWeb.apply {
                            visibility = View.VISIBLE
                            progress = newProgress
                            max = 100
                        }
                    }
                }

                override fun onReceivedTitle(view: WebView?, title: String?) {
                    super.onReceivedTitle(view, title)
                    title?.let {
                        mTitle = it
                    }
                }
            }
            setOnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    if (binding.web.canGoBack()) {
                        binding.web.goBack()
                        return@setOnKeyListener true
                    }
                }
                false
            }
        }

        request(image)
    }.root

    private fun request(image: Uri) {
        viewModel.iqdb(image) {
            binding.swipeWeb.isRefreshing = false
            Toast.makeText(Kraft.app, it.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menu.setHeaderTitle(mUrl)
        requireActivity().menuInflater.inflate(R.menu.menu_search_context_web, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_rev_open_in_browser -> startActivity(
                Intent.createChooser(Intent(Intent.ACTION_VIEW, Uri.parse(mUrl)), "Open")
            )
        }
        return super.onContextItemSelected(item)
    }

}