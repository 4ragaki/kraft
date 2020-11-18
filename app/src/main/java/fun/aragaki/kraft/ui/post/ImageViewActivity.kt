package `fun`.aragaki.kraft.ui.post

import `fun`.aragaki.kraft.ui.base.AdaptSystemUIColors
import `fun`.aragaki.kraft.ui.base.BaseActivity
import `fun`.aragaki.kraft.ui.theme.KraftTheme3
import android.net.Uri
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.offset
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import coil.ImageLoader
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.VerticalPager
import com.google.accompanist.pager.rememberPagerState
import org.kodein.di.DI
import org.kodein.di.android.retainedDI
import org.kodein.di.instance
import kotlin.math.roundToInt


class ImageViewActivity : BaseActivity() {
    override val di: DI by retainedDI { extend(parentDI) }


    @OptIn(ExperimentalPagerApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        setContent {
            KraftTheme3 {
                AdaptSystemUIColors()
                Surface(color = MaterialTheme.colors.background) {
                    val modifier = Modifier.clickable { finishAfterTransition() }

                    intent.getStringArrayExtra(EXTRA_URLS)?.let {
                        val loader by instance<ImageLoader>(
                            intent.getStringExtra(
                                EXTRA_DEPENDENCY_TAG
                            )
                        )
                        ImageView(
                            it,
                            intent.getIntExtra(EXTRA_ANCHOR, 0),
                            loader,
                            modifier
                        )
                    } ?: intent.data?.let { ImageView(it, modifier) }
                }
            }
        }
    }

    companion object {
        const val EXTRA_URLS = "img.urls"
        const val EXTRA_ANCHOR = "img.anchor"
        const val EXTRA_DEPENDENCY_TAG = "loader.tag"
    }
}

@ExperimentalPagerApi
@Composable
fun ImageView(imgs: Array<String>, anchor: Int, loader: ImageLoader, modifier: Modifier) {
    val state = rememberPagerState()
    VerticalPager(count = imgs.size, state = state) { page ->
        ZoomableImage(
            painter = rememberImagePainter(imgs[page], imageLoader = loader),
            modifier = modifier
        )
    }
    LaunchedEffect(key1 = "move2anchor") {
        state.scrollToPage(anchor)
    }
}

@Composable
fun ImageView(img: Uri, modifier: Modifier) {
    Image(
        painter = rememberImagePainter(img),
        contentDescription = "post",
        modifier = modifier
    )
}

@Composable
fun ZoomableImage(
    painter: Painter,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit
) {
    var size by remember { mutableStateOf(IntSize(0, 0)) }
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
//    var rotate by remember { mutableStateOf(0f) }

    Image(
        painter = painter,
        contentDescription = null,
        contentScale = contentScale,
        modifier = modifier
            .onSizeChanged { size = it }
            .pointerInput(Unit) {
                detectTransformGestures { centroid, pan, zoom, rotation ->
                    offset = scaledOffset(size, scale, offset, pan)
                    scale = maxOf(1f, minOf(2f, scale * zoom))
                }
            }
            .scale(scale)
            .offset {
                IntOffset(offset.x.roundToInt(), offset.y.roundToInt())
            })
//            .rotate(rotate)
}

fun scaledOffset(size: IntSize, scale: Float, offset: Offset, pan: Offset): Offset {
    val panX = pan.x
    val panY = pan.y

    val halfWidth = size.width / 2f
    val x =
        if (panX > 0) minOf((scale * halfWidth) - halfWidth, offset.x + panX)
        else maxOf(halfWidth - (scale * halfWidth), offset.x + panX)
    val halfHeight = size.height / 2f
    val y =
        if (panY > 0) minOf((scale * halfHeight) - halfHeight, offset.y + panY)
        else maxOf(halfHeight - (scale * halfHeight), offset.y + panY)

    return Offset(x, y)
}

