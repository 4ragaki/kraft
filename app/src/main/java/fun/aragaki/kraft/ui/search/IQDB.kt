package `fun`.aragaki.kraft.ui.search

import `fun`.aragaki.kraft.*
import `fun`.aragaki.kraft.data.services.IQDBService
import `fun`.aragaki.kraft.extensions.startActivity
import `fun`.aragaki.kraft.ui.post.PostActivity
import android.annotation.SuppressLint
import android.content.res.Configuration
import android.net.Uri
import android.view.KeyEvent
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.viewinterop.AndroidView
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import kotlinx.coroutines.launch
import okhttp3.ResponseBody

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun IQDB(viewModel: SearchViewModel, onException: (Throwable) -> Unit) {
    val responsePair by viewModel.iqdbResponse.collectAsState(initial = null)
    val image by viewModel.image.collectAsState(initial = null)
    var webViewState: Uri? by remember { mutableStateOf(null) }
    val scope = rememberCoroutineScope()

    fun WebView.loadPage(data: Pair<Uri, ResponseBody>) {
        val resp = data.second
        val contentType = resp.contentType()
        loadDataWithBaseURL(
            IQDBService.BASE_URL,
            resp.string(),
            contentType?.let { "${it.type}/${it.subtype}" },
            contentType?.let { it.charset()?.name() },
            IQDBService.BASE_URL
        )
        webViewState = data.first
    }

    responsePair?.let { resp ->
        AndroidView(factory = {
            WebView(it).apply {
                settings.apply {
                    if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
                        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                            Configuration.UI_MODE_NIGHT_YES -> WebSettingsCompat.setForceDark(
                                this, WebSettingsCompat.FORCE_DARK_ON
                            )
                            Configuration.UI_MODE_NIGHT_NO, Configuration.UI_MODE_NIGHT_UNDEFINED -> WebSettingsCompat.setForceDark(
                                this, WebSettingsCompat.FORCE_DARK_OFF
                            )
                        }
                    }
                    javaScriptEnabled = true
                }

                webViewClient = object : WebViewClient() {
                    private val whiteList =
                        sequenceOf(HOST_SAUCENAO, HOST_IQDB, HOST_ASCII2D, HOST_GOOGLE, HOST_TINEYE)

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        scope.launch { viewModel.progress.emit(1f) }
                    }

                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        if (request?.url?.host in whiteList) return false
                        context.startActivity<PostActivity> {
                            data = request?.url
                        }
                        return true
                    }
                }
                webChromeClient = object : WebChromeClient() {
                    override fun onProgressChanged(view: WebView?, newProgress: Int) {
                        super.onProgressChanged(view, newProgress)
                        scope.launch { viewModel.progress.emit(newProgress / 100f) }
                    }

                    override fun onReceivedTitle(view: WebView?, title: String?) {
                        super.onReceivedTitle(view, title)
                        viewModel.title.tryEmit(title)
                    }
                }
                setOnKeyListener { _, keyCode, event ->
                    if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                        if (canGoBack()) {
                            goBack()
                            return@setOnKeyListener true
                        }
                    }
                    false
                }

                loadPage(resp)
            }
        }) {
            if (webViewState != responsePair?.first)
                responsePair?.let { resp -> it.loadPage(resp) }
        }
    } ?: run {
        Text(text = "Loading...")
    }

    if (responsePair?.first != image)
        image?.let { viewModel.iqdb(it) { onException(it) } }
}
