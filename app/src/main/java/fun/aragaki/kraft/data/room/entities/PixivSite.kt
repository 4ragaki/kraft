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
    val oauthScheme: String,
    val oauthHost: String,
    val scheme: String,
    val host: String,
    val patternsPostId: String?,
    val patternsPostsTags: String,
    val folder: List<String>,

    var accessToken: String? = null,
    var refreshToken: String? = null,

    @PrimaryKey(autoGenerate = true)
    val subBooruId: Int = 0
) {
    fun toBooru(pixivSitesDao: PixivSitesDao) = Boorus.Pixiv(
        id, credential, subBooruId,
        name, oauthScheme, oauthHost,
        scheme, host, accessToken, refreshToken, { accessToken, refreshToken ->
            this.accessToken = accessToken
            this.refreshToken = refreshToken
            pixivSitesDao.update(this)
        }, patternsPostId, patternsPostsTags, folder.toTypedArray()
    )
}