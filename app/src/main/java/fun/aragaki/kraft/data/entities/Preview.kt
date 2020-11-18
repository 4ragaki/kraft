package `fun`.aragaki.kraft.data.entities

data class Preview(
    val urls: List<String?>,
    val dependencyTag: String?,
    val uploader: User?,
    val tags: Map<String, List<String>?>,
    val date: String?
)