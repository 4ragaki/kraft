package `fun`.aragaki.kraft.data.room.daos

import `fun`.aragaki.kraft.data.servicewrappers.Boorus
import androidx.room.*

@Dao
interface DanbooruDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(danbooru: Boorus.Danbooru)

    @Delete
    fun delete(danbooru: Boorus.Danbooru)

    @Update
    fun update(danbooru: Boorus.Danbooru)

    @Query("select * from Danbooru")
    fun query(): List<Boorus.Danbooru>
}