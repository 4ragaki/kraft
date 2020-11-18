package `fun`.aragaki.kraft.data.servicewrappers

enum class TagType(val key: String, val sankakuType: Int) {
    General("tag-type-general", 0),
    Artist("tag-type-artist", 1),
    Studio("tag-type-studio", 2),
    Copyright("tag-type-copyright", 3),
    Character("tag-type-character", 4),
    Genre("tag-type-genre", 5),
    Medium("tag-type-medium", 8),
    Meta("tag-type-meta", 9),

    Undefined("undefied", -1)
}