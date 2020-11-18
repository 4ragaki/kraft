package `fun`.aragaki.kraft.data.extensions

import `fun`.aragaki.kraft.R
import `fun`.aragaki.kraft.data.entities.SankakuPost
import `fun`.aragaki.kraft.data.servicewrappers.BooruWrapper
import `fun`.aragaki.kraft.data.TagType
import `fun`.aragaki.kraft.extensions.dateFormatter
import `fun`.aragaki.kraft.extensions.extension
import `fun`.aragaki.kraft.extensions.joinNoNull
import android.net.Uri
import android.text.format.Formatter


fun SankakuPost.extAttachContext(wrapper: BooruWrapper) = apply {
    booruId = wrapper.booru.booruId
}


fun SankakuPost.extPreview(wrapper: BooruWrapper) = Post.Preview(
    sample_url?.let { listOf(it) } ?: emptyList(),
    wrapper.dependencyTag, info(wrapper), getTags()
)


fun SankakuPost.extInfo(wrapper: BooruWrapper) = Post.Info(
    author?.id, { author?.name }, { author?.avatar }, is_favorited,
    null, "${wrapper.booru.name} $id", Triple(true, null, null),
    true to null, false to fav_count.toString(),
    false to vote_count.toString(), false to rating,
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


fun SankakuPost.extDownloads(wrapper: BooruWrapper, postNameFmt: String) = file_url?.let {
    val filename = assignAttributes(
        postNameFmt, wrapper.booru.name,
        id, Uri.parse(it).lastPathSegment?.extension ?: "",
        tags?.joinToString(" ") { tag -> tag.name }
    )
    listOf(Post.Download(it, wrapper.booru.folder, wrapper.dependencyTag, filename))
}


fun SankakuPost.getTags(): Map<TagType, List<String>?> {
    val general = mutableListOf<String>()
    val artist = mutableListOf<String>()
    val studio = mutableListOf<String>()
    val copyright = mutableListOf<String>()
    val character = mutableListOf<String>()
    val genre = mutableListOf<String>()
    val medium = mutableListOf<String>()
    val meta = mutableListOf<String>()
    val undefined = mutableListOf<String>()

/*    tags?.forEach {
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
    }*/
    tags?.forEach {
        when (it.type) {
            TagType.General.sankakuType -> general.add(it.name)
            TagType.Artist.sankakuType -> artist.add(it.name)
            TagType.Studio.sankakuType -> studio.add(it.name)
            TagType.Copyright.sankakuType -> copyright.add(it.name)
            TagType.Character.sankakuType -> character.add(it.name)
            TagType.Genre.sankakuType -> genre.add(it.name)
            TagType.Medium.sankakuType -> medium.add(it.name)
            TagType.Meta.sankakuType -> meta.add(it.name)
            else -> undefined.add(it.name)
        }
    }
    return mapOf(
        TagType.General to general,
        TagType.Artist to artist,
        TagType.Studio to studio,
        TagType.Copyright to copyright,
        TagType.Character to character,
        TagType.Genre to genre,
        TagType.Medium to medium,
        TagType.Meta to meta,
        TagType.Undefined to undefined
    )
}

private fun joinTag(tag: SankakuPost.Tag) =
    listOf(tag.name_en, tag.name_ja).joinNoNull("/")