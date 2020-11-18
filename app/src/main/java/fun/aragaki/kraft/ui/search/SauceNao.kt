package `fun`.aragaki.kraft.ui.search

import `fun`.aragaki.kraft.R
import `fun`.aragaki.kraft.data.entities.SauceNaoResponse
import `fun`.aragaki.kraft.extensions.startActivity
import `fun`.aragaki.kraft.ui.post.Lottie
import `fun`.aragaki.kraft.ui.post.PostActivity
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.rememberInsetsPaddingValues


@Composable
fun SauceNao(viewModel: SearchViewModel, onException: (Throwable) -> Unit) {
    val responsePair by viewModel.sauceNaoResponse.collectAsState(null)

    responsePair?.second?.let { resp ->
        resp.results?.let {
            LazyColumn(
/*                contentPadding = rememberInsetsPaddingValues(
                    insets = LocalWindowInsets.current.systemBars,
                    applyTop = true,
                    applyBottom = true,
                )*/
            ) {
                items(it) { result ->
                    SauceNaoResult(result)
                }
            }
        }
    } ?: run {
        Lottie(res = R.raw.loading_9329)
    }

    val image by viewModel.image.collectAsState(initial = null)
    if (responsePair?.first != image) image?.let { viewModel.sauceNao(it) { onException(it) } }
}

@Composable
fun SauceNaoResult(result: SauceNaoResponse.Result) {
    val context = LocalContext.current
    val previewWidth = 120.dp
    val previewHeight = 160.dp

    Row(modifier = Modifier
        .fillMaxWidth()
        .height(previewHeight)
        .padding(horizontal = 16.dp, vertical = 12.dp)
        .clickable {
            result.data?.ext_urls
                ?.firstOrNull()
                ?.let {
                    context.startActivity<PostActivity> {
                        data = Uri.parse(it)
                    }
                }
        }) {

        Image(
            painter = rememberImagePainter(result.header?.thumbnail),
            contentDescription = "preview",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(width = previewWidth, height = previewHeight)
        )
        Column(
            Modifier
                .fillMaxWidth()
                .height(previewHeight)
                .padding(start = 12.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.End
        ) {
            result.data?.let {
                Text(text = it.title ?: it.pixiv_id?.let { id -> "Pixiv $id" }
                ?: it.danbooru_id?.let { id -> "Danbooru $id" }
                ?: it.sankaku_id?.let { id -> "Sankaku $id" }
                ?: it.seiga_id?.let { id -> "Seiga $id" } ?: it.pawoo_id?.let { id -> "Pawoo $id" }
                ?: it.getchu_id?.let { id -> "Getchu $id" } ?: it.da_id?.let { id -> "Da $id" }
                ?: it.bcy_id?.let { id -> "BCY $id" } ?: it.anidb_aid?.let { id -> "AniDB $id" }
                ?: it.characters ?: it.author_name ?: "<empty>",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.fillMaxWidth())
            }

            result.data?.ext_urls?.joinToString("\n")?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Text(
                text = "${result.header?.similarity}%",
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodyMedium
            )

            Divider()
        }
    }
}
