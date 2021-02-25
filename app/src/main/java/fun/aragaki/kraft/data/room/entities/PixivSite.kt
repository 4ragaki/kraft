package `fun`.aragaki.kraft.data.room.entities

import `fun`.aragaki.kraft.data.room.PostConverters
import `fun`.aragaki.kraft.data.room.daos.PixivSitesDao
import `fun`.aragaki.kraft.data.servicewrappers.Boorus
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "PixivSites")
@TypeConverters(PostConverters::class)
class PixivSite(
    val id: String,
    val credential: String,
    val name: String,
    val authority: String,
    val authScheme: String,
    val authHost: String,
    val scheme: String,
    val host: String,

    var accessToken: String? = null,
    var refreshToken: String? = null,

    @PrimaryKey(autoGenerate = true)
    val subBooruId: Int = 0
) {
    fun toBooru(pixivSitesDao: PixivSitesDao) = Boorus.Pixiv(
        subBooruId, name, authority, authScheme, authHost,
        scheme, host, accessToken, refreshToken, { accessToken, refreshToken ->
            this.accessToken = accessToken
            this.refreshToken = refreshToken
            pixivSitesDao.update(this)
        }
    )
}