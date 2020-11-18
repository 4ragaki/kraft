package `fun`.aragaki.kraft.data.room.daos

import `fun`.aragaki.kraft.data.servicewrappers.Boorus
import androidx.room.*

@Dao
interface PixivDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(pixiv: Boorus.Pixiv)

    @Delete
    fun delete(pixiv: Boorus.Pixiv)

    @Update
    fun update(pixiv: Boorus.Pixiv)

    @Query("select * from Pixiv")
    fun query(): List<Boorus.Pixiv>
}