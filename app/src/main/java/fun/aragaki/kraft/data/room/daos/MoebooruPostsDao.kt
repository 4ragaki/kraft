package `fun`.aragaki.kraft.data.room.daos

import `fun`.aragaki.kraft.data.entities.MoebooruPost
import `fun`.aragaki.kraft.data.extensions.Post
import androidx.paging.PagingSource
import androidx.room.*

@Dao
interface MoebooruPostsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(post: MoebooruPost)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(post: List<MoebooruPost>)

    @Delete
    fun delete(post: MoebooruPost)

    @Update
    fun update(post: MoebooruPost)

    @Query("select * from MoebooruPosts where booruId=:booruId and id=:id")
    fun query(booruId: Int, id: Long): MoebooruPost?

    @Query("select * from MoebooruPosts where booruId=:booruId order by id desc")
    fun pagingSource(booruId: Int): PagingSource<Int, MoebooruPost>

    @Query("select * from MoebooruPosts")
    fun query(): List<MoebooruPost>

    @Query("delete from MoebooruPosts")
    fun clear()
}