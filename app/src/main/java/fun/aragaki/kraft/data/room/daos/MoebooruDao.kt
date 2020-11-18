package `fun`.aragaki.kraft.data.room.daos

import `fun`.aragaki.kraft.data.servicewrappers.Boorus
import androidx.room.*

@Dao
interface MoebooruDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(moebooru: Boorus.Moebooru)

    @Delete
    fun delete(moebooru: Boorus.Moebooru)

    @Update
    fun update(moebooru: Boorus.Moebooru)

    @Query("select * from Moebooru")
    fun query(): List<Boorus.Moebooru>
}