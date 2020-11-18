package `fun`.aragaki.kraft.data

import `fun`.aragaki.kraft.R
import androidx.annotation.StringRes

enum class TagType(val key: String, val sankakuType: Int, @StringRes val text: Int) {
    General("tag-type-general", 0, R.string.post_tag_type_general),
    Artist("tag-type-artist", 1, R.string.post_tag_type_artist),
    Studio("tag-type-studio", 2, R.string.post_tag_type_studio),
    Copyright("tag-type-copyright", 3, R.string.post_tag_type_copyright),
    Character("tag-type-character", 4, R.string.post_tag_type_character),
    Genre("tag-type-genre", 5, R.string.post_tag_type_genre),
    Medium("tag-type-medium", 8, R.string.post_tag_type_medium),
    Meta("tag-type-meta", 9, R.string.post_tag_type_meta),

    Undefined("undefined", -1, R.string.post_tag_type_undefined)
}