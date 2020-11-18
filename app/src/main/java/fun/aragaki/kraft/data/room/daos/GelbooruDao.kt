package `fun`.aragaki.kraft.data.room.daos

import `fun`.aragaki.kraft.data.servicewrappers.Boorus
import androidx.room.*

@Dao
interface GelbooruDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(gelbooru: Boorus.Gelbooru)

    @Delete
    fun delete(gelbooru: Boorus.Gelbooru)

    @Update
    fun update(gelbooru: Boorus.Gelbooru)

    @Query("select * from Gelbooru")
    fun query(): List<Boorus.Gelbooru>
}