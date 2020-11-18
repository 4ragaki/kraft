package `fun`.aragaki.kraft.data.room.daos

import `fun`.aragaki.kraft.data.entities.DanbooruPost
import `fun`.aragaki.kraft.data.entities.MoebooruPost
import androidx.paging.PagingSource
import androidx.room.*

@Dao
interface DanbooruPostsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(post: DanbooruPost)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(post: List<DanbooruPost>)

    @Delete
    fun delete(post: DanbooruPost)

    @Update
    fun update(post: DanbooruPost)

    @Query("select * from DanbooruPosts where booruId=:booruId and id=:id")
    fun query(booruId: Int, id: Long): DanbooruPost?

    @Query("select * from DanbooruPosts where booruId=:booruId  order by id desc")
    fun pagingSource(booruId: Int): PagingSource<Int, DanbooruPost>

    @Query("select * from DanbooruPosts")
    fun query(): List<DanbooruPost>

    @Query("delete from DanbooruPosts")
    fun clear()

}