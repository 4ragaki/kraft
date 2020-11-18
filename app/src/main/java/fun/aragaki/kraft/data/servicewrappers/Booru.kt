package `fun`.aragaki.kraft.data.servicewrappers

interface Booru {
    val booruType: Int
    val booruId: Int

    val name: String
    val authority: String
    val scheme: String
    val host: String

    val pageStart: Int

    //  TODO supporting more contents
    val patternsPostId: String?
    val patternsPostsTags: String?

    val folder: List<String>
}