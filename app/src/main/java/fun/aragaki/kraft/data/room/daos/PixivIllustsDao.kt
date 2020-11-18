package `fun`.aragaki.kraft.data.room.daos

import `fun`.aragaki.kraft.data.room.entities.PixivIllust
import androidx.room.*

@Dao
interface PixivIllustsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(post: PixivIllust)

    @Delete
    fun delete(post: PixivIllust)

    @Update
    fun update(post: PixivIllust)

    @Query("select * from PixivIllusts where pixivId=:pixivId and id=:id")
    fun query(pixivId: Int, id: Long): PixivIllust?

    @Query("select * from PixivIllusts")
    fun query(): List<PixivIllust>
}