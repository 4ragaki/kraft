package `fun`.aragaki.kraft.data.room.daos

import `fun`.aragaki.kraft.data.room.entities.PixivSite
import androidx.room.*

@Dao
interface PixivSitesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(pixiv: PixivSite)

    @Delete
    fun delete(pixiv: PixivSite)

    @Update
    fun update(pixiv: PixivSite)

    @Query("select * from PixivSites")
    fun query(): List<PixivSite>
}