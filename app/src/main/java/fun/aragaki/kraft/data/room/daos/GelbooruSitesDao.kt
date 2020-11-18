package `fun`.aragaki.kraft.data.room.daos

import `fun`.aragaki.kraft.data.room.entities.GelbooruSite
import androidx.room.*

@Dao
interface GelbooruSitesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(gelbooru: GelbooruSite)

    @Delete
    fun delete(gelbooru: GelbooruSite)

    @Update
    fun update(gelbooru: GelbooruSite)

    @Query("select * from GelbooruSites")
    fun query(): List<GelbooruSite>
}