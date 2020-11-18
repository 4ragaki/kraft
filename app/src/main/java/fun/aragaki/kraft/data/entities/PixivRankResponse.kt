package `fun`.aragaki.kraft.data.entities

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class PixivRankResponse(
    val illusts: List<PixivIllustrationResponse.PixivIllustration>,
    val next_url: String,
    val search_span_limit: Int
)