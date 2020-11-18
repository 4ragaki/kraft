package `fun`.aragaki.kraft.ui.post

import `fun`.aragaki.kraft.PARAMETER_POSTS_TAGS
import `fun`.aragaki.kraft.R
import `fun`.aragaki.kraft.SCHEME
import `fun`.aragaki.kraft.data.TagType
import `fun`.aragaki.kraft.data.UnsupportedException
import `fun`.aragaki.kraft.data.extensions.Post
import `fun`.aragaki.kraft.data.servicewrappers.BooruWrapper
import `fun`.aragaki.kraft.data.servicewrappers.BooruWrapper.Companion.doAs
import `fun`.aragaki.kraft.extensions.startActivity
import `fun`.aragaki.kraft.extensions.toast
import `fun`.aragaki.kraft.ui.posts.PostsActivity
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.TextView
import androidx.annotation.RawRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import coil.ImageLoader
import coil.compose.rememberImagePainter
import com.airbnb.lottie.compose.*
import com.google.accompanist.insets.systemBarsPadding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch


@Composable
fun PostInfo(
    info: Post.Info,
    tags: Map<TagType, List<String>?>,
    imageLoader: ImageLoader,
    wrapper: BooruWrapper,
    selectionFlow: MutableSharedFlow<Set<String>>
) {
    val scope = rememberCoroutineScope()

    var uploaderAvatar by remember { mutableStateOf("") }
    var uploaderName by remember { mutableStateOf("Loading") }

    Column(
        Modifier.padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = rememberImagePainter(uploaderAvatar, imageLoader = imageLoader) {
                    error(R.mipmap.ic_launcher_round)
                },
                contentScale = ContentScale.Crop,
                contentDescription = "UploaderAvatar",
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                Modifier.height(54.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = info.title ?: "No Title",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f), textAlign = TextAlign.Center
                )
                Text(
                    text = uploaderName,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f), textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.weight(1f))

            val followed = info.isFollowed == true
            IconButton(onClick = {
                if (followed) wrapper.doAs<BooruWrapper.Followable> {
                    scope.launch {
                        info.uploaderId?.let { follow(!followed, it) }
                    }
                }
            }) {
                Icon(
                    painter = rememberImagePainter(if (followed) R.drawable.ic_post_user_unfollow else R.drawable.ic_post_user_follow),
                    contentDescription = "Follow Status", tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        val caption = info.caption
        val colorOnSurface = MaterialTheme.colorScheme.onSurface.toArgb()
        if (!caption.first) {
            caption.third?.let {
                if (caption.second == true) AndroidView(factory = { ctx ->
                    TextView(ctx).apply {
                        text = HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_COMPACT)
                        setTextColor(colorOnSurface)
                    }
                }, Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp))
                else Text(
                    text = it,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
                )
            }
        }

        Row(Modifier.padding(vertical = 16.dp)) {
            val shows = info.shows
            if (!shows.first) Row(
                Modifier.weight(1f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "${shows.second}", color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_post_show),
                    contentDescription = "PostShowCount", tint = MaterialTheme.colorScheme.onSurface
                )
            }

            val likes = info.likes
            if (!likes.first) Row(
                Modifier.weight(1f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "${likes.second}", color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_post_bookmark),
                    contentDescription = "PostLikeCount", tint = MaterialTheme.colorScheme.onSurface
                )
            }

            val scores = info.scores
            if (!scores.first) Row(
                Modifier.weight(1f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "${scores.second}", color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_post_score),
                    contentDescription = "PostScore", tint = MaterialTheme.colorScheme.onSurface
                )
            }

            val rating = info.rating
            if (!rating.first) Row(
                Modifier.weight(1f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "${rating.second}", color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_post_rating),
                    contentDescription = "PostRating", tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        PostTags(wrapper.booru.authority, tags, selectionFlow)
    }

    LaunchedEffect(key1 = "PostInfo") {
        scope.launch(Dispatchers.IO) {
            uploaderAvatar = info.uploaderAvatar() ?: "Unknown"
            uploaderName = info.uploaderName() ?: "Unknown"
        }
    }
}


@Composable
fun Lottie(
    @RawRes res: Int,
    iterations: Int = LottieConstants.IterateForever,
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(res))
    val progress by animateLottieCompositionAsState(composition, iterations = iterations)
    LottieAnimation(composition = composition, progress = progress, modifier = modifier)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Activity.Exception(throwable: Throwable) {
    Scaffold(topBar = {
        SmallTopAppBar(title = { Text(text = "Error") }, navigationIcon = {
            IconButton(onClick = { finishAfterTransition() }) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "close")
            }
        })
    }, modifier = Modifier.systemBarsPadding()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            when (throwable) {
                is UnsupportedException -> {
                    Lottie(res = R.raw.empty_list, modifier = Modifier.size(240.dp))

                    Text(
                        text = throwable.message ?: "unsupported",
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    TextButton(onClick = {
                        startActivity(
                            Intent.createChooser(
                                Intent(Intent.ACTION_VIEW, throwable.uri),
                                "Choose:"
                            )
                        )
                    }) {
                        Text(
                            text = "Open", color = Color(0xff64B5F6),
                            fontSize = 18.sp,
                            textDecoration = TextDecoration.Underline
                        )
                    }
                }
                else -> {
                    Lottie(res = R.raw.page_not_found, modifier = Modifier.size(240.dp))
                    Text(
                        text = throwable.message
                            ?: "an exception occurred.\n${throwable.javaClass.canonicalName}",
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun PostTags(
    authority: String,
    tags: Map<TagType, List<String>?>,
    selectionFlow: MutableSharedFlow<Set<String>>
) {
    val context = LocalContext.current
    tags.forEach {
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = it.key.text),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 8.dp)
            )

            val selection by selectionFlow.collectAsState(initial = emptySet())
            val scope = rememberCoroutineScope()

            var tagMenuFor: String? by remember { mutableStateOf(null) }

            DropdownMenu(
                expanded = tagMenuFor != null,
                onDismissRequest = { tagMenuFor = null },
            ) {
                Text(
                    text = "$tagMenuFor:",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )

                DropdownMenuItem(onClick = {
                    tagMenuFor = null
                }) {
                    Text(text = "Wiki")
                }
                DropdownMenuItem(onClick = {
                    Intent(Intent.ACTION_PROCESS_TEXT).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_PROCESS_TEXT, tagMenuFor)
                    }.run {
                        val activities =
                            context.packageManager.queryIntentActivities(this, 0)
                        if (activities.isNotEmpty()) context.startActivity(this)
                        else context.toast("There's not a supported application.")
                    }

                    tagMenuFor = null
                }) {
                    Text(text = "Translate")
                }
                DropdownMenuItem(onClick = {
                    context.startActivity<PostsActivity> {
                        data = Uri.parse("$SCHEME://$authority?$PARAMETER_POSTS_TAGS=$tagMenuFor")
                    }
                    tagMenuFor = null
                }) {
                    Text(text = "Search")
                }
            }

            it.value?.forEach { tag ->
                ChipAlternate(tag, tag in selection, { selected ->
                    scope.launch {
                        selectionFlow.emit(selection.toMutableSet()
                            .apply { if (selected) remove(tag) else add(tag) })
                    }
                }) { tagMenuFor = it }
            }
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChipAlternate(
    text: String,
    selected: Boolean = false,
    click: (selected: Boolean) -> Unit,
    longClick: (text: String) -> Unit
) {
    Surface(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .combinedClickable(onClick = { click(selected) }, onLongClick = { longClick(text) }),
        tonalElevation = 8.dp,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.primary
        ),
        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp),
            color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
        )
    }
}