package `fun`.aragaki.kraft.data.room.daos

import `fun`.aragaki.kraft.data.room.entities.MoebooruSite
import androidx.room.*

@Dao
interface MoebooruSitesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(moebooru: MoebooruSite)

    @Delete
    fun delete(moebooru: MoebooruSite)

    @Update
    fun update(moebooru: MoebooruSite)

    @Query("select * from MoebooruSites")
    fun query(): List<MoebooruSite>
}