package `fun`.aragaki.kraft.data.room.daos

import `fun`.aragaki.kraft.data.room.entities.SankakuSite
import androidx.room.*

@Dao
interface SankakuSitesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(sankaku: SankakuSite)

    @Delete
    fun delete(sankaku: SankakuSite)

    @Update
    fun update(sankaku: SankakuSite)

    @Query("select * from SankakuSites")
    fun query(): List<SankakuSite>
}