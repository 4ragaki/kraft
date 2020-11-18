package `fun`.aragaki.kraft.data.room.daos

import `fun`.aragaki.kraft.data.entities.MoebooruPost
import `fun`.aragaki.kraft.data.entities.PixivIllustrationResponse
import androidx.paging.PagingSource
import androidx.room.*

@Dao
interface PixivIllustsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(post: PixivIllustrationResponse.PixivIllustration)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(post: List<PixivIllustrationResponse.PixivIllustration>)

    @Delete
    fun delete(post: PixivIllustrationResponse.PixivIllustration)

    @Update
    fun update(post: PixivIllustrationResponse.PixivIllustration)

    @Query("select * from PixivIllusts where booruId=:booruId and id=:id")
    fun query(booruId: Int, id: Long): PixivIllustrationResponse.PixivIllustration?

    @Query("select * from PixivIllusts where booruId=:booruId order by id desc")
    fun pagingSource(booruId: Int): PagingSource<Int, PixivIllustrationResponse.PixivIllustration>

    @Query("select * from PixivIllusts")
    fun query(): List<PixivIllustrationResponse.PixivIllustration>

    @Query("delete from PixivIllusts")
    fun clear()
}