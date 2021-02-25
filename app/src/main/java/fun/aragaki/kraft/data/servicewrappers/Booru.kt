package `fun`.aragaki.kraft.data.servicewrappers

interface Booru {
    val booruType: Int
    val booruSubId: Int

    val name: String
    val authority: String
    val scheme: String
    val host: String

    //  TODO supporting more contents
    val patternsPostId: String?
    val patternsPostsTags: String?

    val folder: Array<String>
}