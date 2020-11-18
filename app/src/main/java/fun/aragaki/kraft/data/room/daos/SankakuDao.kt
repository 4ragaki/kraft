package `fun`.aragaki.kraft.data.room.daos

import `fun`.aragaki.kraft.data.servicewrappers.Boorus
import androidx.room.*

@Dao
interface SankakuDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(sankaku: Boorus.Sankaku)

    @Delete
    fun delete(sankaku: Boorus.Sankaku)

    @Update
    fun update(sankaku: Boorus.Sankaku)

    @Query("select * from Sankaku")
    fun query(): List<Boorus.Sankaku>
}