package `fun`.aragaki.kraft.data.room.entities

import `fun`.aragaki.kraft.data.room.PostConverters
import `fun`.aragaki.kraft.data.servicewrappers.Boorus
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "SankakuSites")
@TypeConverters(PostConverters::class)
class SankakuSite(
    val id: String?,
    val credential: String?,
    val name: String,
    val scheme: String,
    val host: String,
    val hashSalt: String?,
    val patternsPostId: String?,
    val patternsPostsTags: String,
    val folder: List<String>,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "subBooruId")
    val subBooruId: Int = 0
) {
    fun toBooru() = Boorus.Sankaku(
        id, credential, subBooruId, name, scheme,
        host, hashSalt, patternsPostId, patternsPostsTags, folder.toTypedArray()
    )
}