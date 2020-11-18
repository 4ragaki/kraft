package `fun`.aragaki.kraft.data.room.daos

import `fun`.aragaki.kraft.data.room.entities.DanbooruSite
import androidx.room.*

@Dao
interface DanbooruSitesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(danbooru: DanbooruSite)

    @Delete
    fun delete(danbooru: DanbooruSite)

    @Update
    fun update(danbooru: DanbooruSite)

    @Query("select * from DanbooruSites")
    fun query(): List<DanbooruSite>
}