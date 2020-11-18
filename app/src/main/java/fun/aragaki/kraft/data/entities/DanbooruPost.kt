package `fun`.aragaki.kraft.data.entities

import `fun`.aragaki.kraft.data.servicewrappers.BooruWrapper
import `fun`.aragaki.kraft.data.servicewrappers.TagType
import `fun`.aragaki.kraft.ext.splitByBlank

data class DanbooruPost(
    val approver_id: Int?,
    val bit_flags: Int?,
    val children_ids: Any?,
    val created_at: String?,
    val down_score: Int?,
    val fav_count: Int?,
    val file_ext: String,
    val file_size: Int?,
    val file_url: String?,
    val has_active_children: Boolean?,
    val has_children: Boolean?,
    val has_large: Boolean?,
    val has_visible_children: Boolean?,
    val id: Long,
    val image_height: Int?,
    val image_width: Int?,
    val is_banned: Boolean?,
    val is_deleted: Boolean?,
    val is_favorited: Boolean?,
    val is_flagged: Boolean?,
    val is_note_locked: Boolean?,
    val is_pending: Boolean?,
    val is_rating_locked: Boolean?,
    val is_status_locked: Boolean?,
    val keeper_data: String?,
    val large_file_url: String?,
    val last_comment_bumped_at: Any?,
    val last_commented_at: Any?,
    val last_noted_at: String?,
    val md5: String?,
    val parent_id: Int?,
    val pixiv_id: Int?,
    val pool_string: String?,
    val preview_file_url: String?,
    val rating: String?,
    val score: Int?,
    val source: String?,
    val tag_count: Int?,
    val tag_count_artist: Int?,
    val tag_count_character: Int?,
    val tag_count_copyright: Int?,
    val tag_count_general: Int?,
    val tag_count_meta: Int?,
    val tag_string: String?,
    val tag_string_artist: String?,
    val tag_string_character: String?,
    val tag_string_copyright: String?,
    val tag_string_general: String?,
    val tag_string_meta: String?,
    val up_score: Int?,
    val updated_at: String?,
    val uploader_id: Int?,
    val uploader_name: String?
) : Post() {
    override lateinit var pWrapper: BooruWrapper
    override lateinit var pTitle: String

    override fun attachContext(wrapper: BooruWrapper) = apply {
        pWrapper = wrapper
        pTitle = "${wrapper.booru.name} $id"
    }

    override fun postId() = id
    override fun postPreview() = preview_file_url

    override fun preview() = Preview(
        listOf(large_file_url), pWrapper.dependencyTag, null, mapOf(
            TagType.Artist.key to tag_string_artist?.splitByBlank(),
            TagType.Character.key to tag_string_character?.splitByBlank(),
            TagType.Copyright.key to tag_string_copyright?.splitByBlank(),
            TagType.General.key to tag_string_general?.splitByBlank(),
            TagType.Meta.key to tag_string_meta?.splitByBlank()
        ), created_at
    )

    override fun downloads(postNameFmt: String) =
        file_url?.let {
            listOf(
                Download(
                    it, pWrapper.booru.folder, pWrapper.dependencyTag,
                    assignAttributes(postNameFmt, pWrapper.booru.name, id, file_ext, tag_string)
                )
            )
        }

    override fun message(): Nothing? = null
}