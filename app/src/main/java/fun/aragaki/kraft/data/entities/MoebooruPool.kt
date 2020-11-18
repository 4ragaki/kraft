package `fun`.aragaki.kraft.data.entities

data class MoebooruPool(
    val created_at: String?,
    val description: String?,
    val id: Int?,
    val is_public: Boolean?,
    val name: String?,
    val post_count: Int?,
    val posts: List<MoebooruPost?>?,
    val updated_at: String?,
    val user_id: Int?
)