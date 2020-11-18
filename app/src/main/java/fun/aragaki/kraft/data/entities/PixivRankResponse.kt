package `fun`.aragaki.kraft.data.entities

data class PixivRankResponse(
    val illusts: List<PixivIllustrationResponse.PixivIllustration>,
    val next_url: String,
    val search_span_limit: Int
)