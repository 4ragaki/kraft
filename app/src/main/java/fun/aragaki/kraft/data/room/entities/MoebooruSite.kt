package `fun`.aragaki.kraft.data.room.entities

import `fun`.aragaki.kraft.data.room.PostConverters
import `fun`.aragaki.kraft.data.servicewrappers.Boorus
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "MoebooruSites")
@TypeConverters(PostConverters::class)
class MoebooruSite(
    val id: String?,
    val credential: String?,
    val name: String,
    val authority: String,
    val scheme: String,
    val host: String,
    val hashSalt: String?,

    @PrimaryKey(autoGenerate = true)
    val subBooruId: Int = 0
) {
    fun toBooru() =
        Boorus.Moebooru(id, credential, subBooruId, name, authority, scheme, host, hashSalt)
}