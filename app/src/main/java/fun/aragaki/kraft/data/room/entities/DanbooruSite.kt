package `fun`.aragaki.kraft.data.room.entities

import `fun`.aragaki.kraft.data.room.PostConverters
import `fun`.aragaki.kraft.data.servicewrappers.Boorus
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "DanbooruSites")
@TypeConverters(PostConverters::class)
class DanbooruSite(
    val id: String?,
    val credential: String?,
    val name: String,
    val scheme: String,
    val host: String,
    val patternsPostId: String,
    val patternsPostsTags: String,
    val folder: List<String>,

    @PrimaryKey(autoGenerate = true)
    val subBooru: Int = 0
) {
    fun toBooru() = Boorus.Danbooru(
        id, credential, subBooru, name,
        scheme, host, patternsPostId, patternsPostsTags, folder.toTypedArray()
    )
}