package `fun`.aragaki.kraft.ui.search

import `fun`.aragaki.kraft.R
import `fun`.aragaki.kraft.data.CredentialException
import `fun`.aragaki.kraft.extensions.startActivity
import `fun`.aragaki.kraft.extensions.toast
import `fun`.aragaki.kraft.ui.JumpActivity
import `fun`.aragaki.kraft.ui.ViewModelFactory
import `fun`.aragaki.kraft.ui.base.BaseActivity
import `fun`.aragaki.kraft.ui.base.TransparentSystemUIColors
import `fun`.aragaki.kraft.ui.theme.KraftTheme3
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.enterAlwaysScrollBehavior
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.*
import kotlinx.coroutines.flow.MutableSharedFlow
import org.kodein.di.DI
import org.kodein.di.android.retainedDI
import org.kodein.di.instance

class ReverseComposeActivity : BaseActivity() {
    override val di: DI by retainedDI { extend(parentDI) }
    private val viewModelFactory by instance<ViewModelFactory>()
    private val viewModel by viewModels<SearchViewModel> { viewModelFactory }
    private var serviceIndex = 0
    private val services = arrayOf(ReverseScreen.SauceNao, ReverseScreen.IQDB)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        WindowCompat.setDecorFitsSystemWindows(window,false)

        when (intent?.action) {
            Intent.ACTION_SEND -> {
                intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)
                    ?.let { viewModel.queueOffer(it) }
            }
            Intent.ACTION_SEND_MULTIPLE -> {
                intent.getParcelableArrayListExtra<Uri>(Intent.EXTRA_STREAM)
                    ?.let { viewModel.queueOffer(it) }
            }
        }
        viewModel.queuePoll()

        setContent {
            KraftTheme3 {
                TransparentSystemUIColors(
                    MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.surface
                )
                ProvideWindowInsets {
                    ReverseCompose()
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ReverseCompose() {
        var snackEvent: Any? by remember { mutableStateOf(null) }
        var title by remember { mutableStateOf("Search") }
        val navController = rememberNavController()

        Scaffold(topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = title) },
                navigationIcon = {
                    IconButton(onClick = { finishAfterTransition() }) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = "finish")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        val screen = services[++serviceIndex % services.size]
                        navController.navigate(screen.route)
                        title = getString(screen.title)
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_reverse_service_next),
                            contentDescription = "forward"
                        )
                    }
                    val queueEnd by viewModel.queueEnd.collectAsState(false)
                    if (!queueEnd) IconButton(onClick = { viewModel.queuePoll() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "forward"
                        )
                    }
                },
//                not working?!
//                scrollBehavior = enterAlwaysScrollBehavior(rememberTopAppBarScrollState()),
                modifier = Modifier.statusBarsPadding()
            )
            val progress by viewModel.progress.collectAsState(initial = 0f)
            if (progress !in arrayOf(0f, 1f)) LinearProgressIndicator(
                progress, Modifier.fillMaxWidth()
            )
        }, snackbarHost = {
            snackEvent?.let {
                if (it is CredentialException) Snackbar(action = {
                    TextButton(onClick = {
                        startActivity<JumpActivity> {
                            putExtra(
                                JumpActivity.EXTRA_DESTINATION, R.id.nav_dest_credentials
                            )
                        }
                        snackEvent = null
                    }) {
                        Text(text = stringResource(id = R.string.action_jump_to))
                    }
                }, modifier = Modifier.navigationBarsWithImePadding()) {
                    Text(text = it.message ?: "")
                }
            }
        }) { padding ->
            NavHost(
                navController = navController,
                startDestination = ReverseScreen.SauceNao.route,
                modifier = Modifier.padding(paddingValues = padding)
            ) {
                composable(ReverseScreen.SauceNao.route) {
                    SauceNao(viewModel) {
                        if (it is CredentialException) snackEvent = it
                        else toast(it.message)
                    }
                }
                composable(ReverseScreen.IQDB.route) {
                    IQDB(viewModel) {
                        toast(it.message)
                    }
                }
            }
        }
    }

    @Composable
    fun QueueEnd() {
        Text(
            text = "queue finished.",
            modifier = Modifier.fillMaxSize(),
            textAlign = TextAlign.Center
        )
    }
}

enum class ReverseScreen(val route: String, @StringRes val title: Int) {
    SauceNao("saucenao", R.string.reverse_provider_saucenao), IQDB(
        "iqdb", R.string.reverse_provider_iqdb
    )
}