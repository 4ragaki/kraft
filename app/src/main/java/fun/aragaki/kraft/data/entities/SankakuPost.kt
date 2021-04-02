package `fun`.aragaki.kraft.data.entities

import `fun`.aragaki.kraft.R
import `fun`.aragaki.kraft.data.servicewrappers.BooruWrapper
import `fun`.aragaki.kraft.data.servicewrappers.TagType
import `fun`.aragaki.kraft.ext.dateFormatter
import `fun`.aragaki.kraft.ext.extension
import `fun`.aragaki.kraft.ext.joinNoNull
import android.net.Uri
import android.text.format.Formatter

data class SankakuPost(
    val author: Author?,
    val change: Int?,
    val comment_count: Any?,
    val created_at: CreatedAt?,
    val fav_count: Int?,
    val file_size: Long?,
    val file_type: String?,
    val file_url: String?,
    val has_children: Boolean?,
    val has_comments: Boolean?,
    val has_notes: Boolean?,
    val height: Int?,
    val id: Long,
    val in_visible_pool: Boolean?,
    val is_favorited: Boolean?,
    val is_premium: Boolean?,
    val md5: String?,
    val parent_id: Any?,
    val preview_height: Int?,
    val preview_url: String?,
    val preview_width: Int?,
    val rating: String?,
    val recommended_posts: Int?,
    val recommended_score: Int?,
    val sample_height: Int?,
    val sample_url: String?,
    val sample_width: Int?,
    val sequence: Any?,
    val source: String?,
    val status: String?,
    val tags: List<Tag>?,
    val total_score: Int?,
    val user_vote: Any?,
    val vote_count: Int?,
    val width: Int?
) : Post() {
    override lateinit var pWrapper: BooruWrapper
    override lateinit var pTitle: String

    override fun attachContext(wrapper: BooruWrapper) = apply {
        pWrapper = wrapper
        pTitle = "${wrapper.booru.name} $id"
    }

    override fun postId() = id
    override fun postPreview() = preview_url

    override fun preview() = Preview(
        listOf(sample_url),
        pWrapper.dependencyTag, info(), getTags()
    )

    override fun info() = Info(
        author?.id,
        { author?.name },
        { author?.avatar },
        null,
        pTitle,
        Triple(true, null, null),
        true to null,
        false to fav_count.toString(),
        false to vote_count.toString(),
        false to rating,
        { c ->
            buildString {
                height?.let { h ->
                    width?.let { w ->
                        append(c.getString(R.string.fmt_post_info_size).format(h, w))
                        append("\n\n")
                    }
                }
                md5?.let {
                    append(c.getString(R.string.fmt_post_info_md5).format(it))
                    append("\n\n")
                }
                file_size?.let {
                    append(
                        c.getString(R.string.fmt_post_info_file_size)
                            .format(Formatter.formatFileSize(c, it))
                    )
                    append("\n\n")
                }
                file_url?.let {
                    append(c.getString(R.string.fmt_post_info_file_url).format(it))
                    append("\n\n")
                }
                parent_id?.let {
                    append(c.getString(R.string.fmt_post_info_parent).format(it))
                    append("\n\n")
                }
                source?.let {
                    append(c.getString(R.string.fmt_post_info_source).format(it))
                    append("\n\n")
                }
            }
        },
        dateFormatter.format(created_at?.s?.times(1000))
    )

    override fun downloads(postNameFmt: String) = file_url?.let {
        val filename = assignAttributes(
            postNameFmt, pWrapper.booru.name,
            id, Uri.parse(it).lastPathSegment?.extension ?: "",
            tags?.joinToString(" ") { tag -> tag.name }
        )
        listOf(Download(it, pWrapper.booru.folder, pWrapper.dependencyTag, filename))
    }

    override fun message(): Nothing? = null

    fun getTags(): Map<String, List<String>?> {
        val general = mutableListOf<String>()
        val artist = mutableListOf<String>()
        val studio = mutableListOf<String>()
        val copyright = mutableListOf<String>()
        val character = mutableListOf<String>()
        val genre = mutableListOf<String>()
        val medium = mutableListOf<String>()
        val meta = mutableListOf<String>()
        val undefined = mutableListOf<String>()

        tags?.forEach {
            when (it.type) {
                TagType.General.sankakuType -> general.add(joinTag(it))
                TagType.Artist.sankakuType -> artist.add(joinTag(it))
                TagType.Studio.sankakuType -> studio.add(joinTag(it))
                TagType.Copyright.sankakuType -> copyright.add(joinTag(it))
                TagType.Character.sankakuType -> character.add(joinTag(it))
                TagType.Genre.sankakuType -> genre.add(joinTag(it))
                TagType.Medium.sankakuType -> medium.add(joinTag(it))
                TagType.Meta.sankakuType -> meta.add(joinTag(it))
                else -> undefined.add(joinTag(it))
            }
        }
        return mapOf(
            TagType.General.key to general,
            TagType.Artist.key to artist,
            TagType.Studio.key to studio,
            TagType.Copyright.key to copyright,
            TagType.Character.key to character,
            TagType.Genre.key to genre,
            TagType.Medium.key to medium,
            TagType.Meta.key to meta,
            TagType.Undefined.key to undefined
        )
    }

    private fun joinTag(tag: Tag) =
        listOf(tag.name_en, tag.name_ja).joinNoNull("/")

    data class Author(
        val avatar: String?,
        val avatar_rating: String?,
        val id: Long?,
        val name: String?
    )

    data class CreatedAt(
        val json_class: String?,
        val n: String?,
        val s: Long?
    )

    data class Tag(
        val count: Int?,
        val id: Int?,
        val locale: String?,
        val name: String,
        val name_en: String?,
        val name_ja: String?,
        val pool_count: Int?,
        val post_count: Int?,
        val rating: Any?,
        val type: Int?
    )
}