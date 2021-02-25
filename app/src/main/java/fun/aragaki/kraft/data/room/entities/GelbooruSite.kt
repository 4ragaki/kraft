package `fun`.aragaki.kraft.data.room.entities

import `fun`.aragaki.kraft.data.room.PostConverters
import `fun`.aragaki.kraft.data.servicewrappers.Boorus
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "GelbooruSites")
@TypeConverters(PostConverters::class)
class GelbooruSite(
    val id: String?,
    val credential: String?,
    val name: String,
    val authority: String,
    val scheme: String,
    val host: String,

    @PrimaryKey(autoGenerate = true)
    val subBooruId: Int = 0
) {
    fun toBooru() = Boorus.Gelbooru(id, credential, subBooruId, name, authority, scheme, host)
}