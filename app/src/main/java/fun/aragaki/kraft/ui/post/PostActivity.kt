package `fun`.aragaki.kraft.ui.post

import `fun`.aragaki.kraft.PARAMETER_POSTS_TAGS
import `fun`.aragaki.kraft.R
import `fun`.aragaki.kraft.SCHEME
import `fun`.aragaki.kraft.data.extensions.Post
import `fun`.aragaki.kraft.data.servicewrappers.BooruWrapper
import `fun`.aragaki.kraft.extensions.find
import `fun`.aragaki.kraft.extensions.findUrls
import `fun`.aragaki.kraft.extensions.startActivity
import `fun`.aragaki.kraft.ui.ViewModelFactory
import `fun`.aragaki.kraft.ui.base.BaseActivity
import `fun`.aragaki.kraft.ui.base.TransparentSystemUIColors
import `fun`.aragaki.kraft.ui.posts.PostsActivity
import `fun`.aragaki.kraft.ui.theme.KraftTheme3
import `fun`.aragaki.kraft.ui.theme.md2.KraftTheme2
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import coil.ImageLoader
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.android.retainedDI
import org.kodein.di.instance
import androidx.compose.material3.Snackbar as Material3Snackbar


class PostActivity : BaseActivity() {
    override val di: DI by retainedDI { extend(parentDI) }
    private val viewModelFactory by instance<ViewModelFactory>()
    private val viewModel by viewModels<PostViewModel> { viewModelFactory }
    private val wrappers by instance<List<BooruWrapper>>()
    private val selectionFlow = MutableSharedFlow<Set<String>>(1)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        present(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        present(intent)
    }

    private fun present(intent: Intent) {
        val uri = if (intent.action == Intent.ACTION_SEND)
            Uri.parse(intent.getStringExtra(Intent.EXTRA_TEXT)?.findUrls()?.first())
        else intent.data!!
        viewModel.handle(uri)
        setContent {
            ProvideWindowInsets {
                KraftTheme2() {
                    Post()
                }
            }
        }
    }

    @OptIn(
        ExperimentalPagerApi::class,
        ExperimentalMaterialApi::class,
        ExperimentalPagerApi::class,
        ExperimentalAnimationApi::class
    )
    @Composable
    fun Post() {
        val posts by viewModel.posts.collectAsState(null)
        val pagerState = rememberPagerState()
        var voted by remember { mutableStateOf(false) }

        val scope = rememberCoroutineScope()
        var exceptionMsg: String? by remember { mutableStateOf(null) }
        val exception by viewModel.exception.collectAsState(initial = null)

        val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
        val haptic = LocalHapticFeedback.current

        KraftTheme3 {
            exception?.let { Exception(it) }
                ?: if (posts == null) Lottie(res = R.raw.loading_9329)

            posts?.let { posts->
                val post = posts[pagerState.currentPage]
                val wrapper = wrappers.find(post)

                TransparentSystemUIColors()
                BottomSheetScaffold(
                    scaffoldState = bottomSheetScaffoldState,
                    sheetBackgroundColor = MaterialTheme.colorScheme.surface.copy(0.95f),
                    sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                    modifier = Modifier.navigationBarsWithImePadding(),
                    snackbarHost = {
                        if (!exceptionMsg.isNullOrEmpty())
                            Material3Snackbar(action = {
                                TextButton(onClick = { exceptionMsg = null }) {
                                    Text(text = "Dismiss")
                                }
                            }) {
                                Text(text = exceptionMsg!!)
                            }
                    },
                    floatingActionButton = {
                        AnimatedContent(targetState = bottomSheetScaffoldState) { state ->
                            if (state.bottomSheetState.isCollapsed) FloatingActionButton(onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                downloadPost(posts, pagerState)
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_pref_works),
                                    contentDescription = "download"
                                )
                            }
                            else FloatingActionButton(onClick = {
                                startActivity<PostsActivity> {
                                    data = Uri.parse(
                                        "$SCHEME://${wrapper.booru.authority}/posts?${
                                            selectionFlow.replayCache.firstOrNull()
                                                ?.joinToString(" ")
                                                ?.let { "$PARAMETER_POSTS_TAGS=$it" }
                                        }"
                                    )
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "download"
                                )
                            }
                        }

                    },
                    sheetContent = {
                        val info = post.info(wrapper)
                        Column(
                            modifier = Modifier
                                .heightIn(min = 100.dp, max = 500.dp)
                                .verticalScroll(rememberScrollState())
                        ) {
                            Row {
                                AnimatedContent(targetState = bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                                    IconButton(onClick = {
                                        scope.launch {
                                            bottomSheetScaffoldState.bottomSheetState.animateTo(if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) BottomSheetValue.Expanded else BottomSheetValue.Collapsed)
                                        }
                                    }) {
                                        Icon(
                                            imageVector = if (it) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                                            contentDescription = "show/hide info",
                                            tint = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }

                                IconButton(onClick = {
                                    viewModel.vote(post.postId(), info.isVoted,
                                        {
                                            info.isVoted = it
                                            voted = info.isVoted ?: false
                                        }
                                    ) {
                                        it.message?.let { exceptionMsg = it }
                                        it.printStackTrace()
                                    }
                                }) {
                                    Icon(
                                        painter = painterResource(id = if (voted) R.drawable.ic_voted else R.drawable.ic_vote),
                                        contentDescription = "vote",
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                AnimatedVisibility(
                                    visible = bottomSheetScaffoldState.bottomSheetState.isExpanded,
                                    enter = fadeIn() + slideInVertically(),
                                    exit = fadeOut() + slideOutVertically()
                                ) {
                                    IconButton(onClick = {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        downloadPost(posts, pagerState)

                                    }) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_pref_works),
                                            contentDescription = "download",
                                            tint = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }

                            val imageLoader = remember {
                                val result by instance<ImageLoader>(wrapper.dependencyTag)
                                result
                            }
                            PostInfo(
                                info,
                                post.preview(wrapper).tags,
                                imageLoader,
                                wrapper,
                                selectionFlow
                            )
                        }
                    }) {

                    PostsPager(posts, pagerState, bottomSheetScaffoldState)
                }
            }
        }
    }


    @ExperimentalPagerApi
    private fun downloadPost(
        posts: List<Post>,
        pagerState: PagerState
    ) {
        viewModel.download(
            posts[pagerState.currentPage]
        )
    }


    @OptIn(ExperimentalMaterialApi::class)
    @ExperimentalPagerApi
    @Composable
    fun PostsPager(
        posts: List<Post>,
        pagerState: PagerState,
        bottomSheetScaffoldState: BottomSheetScaffoldState
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            HorizontalPager(
                count = posts.size,
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                val post = posts[page]
                val wrapper = wrappers.find(post)
                val loader by instance<ImageLoader>(wrapper.dependencyTag)

                Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        contentPadding = PaddingValues(bottom = 64.dp)
                    ) {
                        val urls = post.preview(wrapper).urls
                        items(urls.size) { anchor ->
                            var holder by remember { mutableStateOf(true) }
                            Image(
                                painter = rememberImagePainter(
                                    urls[anchor],
                                    imageLoader = loader
                                ) {
                                    size(OriginalSize)
                                    listener { request, metadata ->
                                        holder = false
                                    }
//                                    TODO gimme the bitmap
                                }, contentDescription = "", contentScale = ContentScale.FillWidth,
                                modifier = Modifier
                                    .fillMaxSize()
                            )
                            if (holder) Lottie(res = R.raw.loading_9329)
                        }
                    }
                }
            }
        }
    }
}