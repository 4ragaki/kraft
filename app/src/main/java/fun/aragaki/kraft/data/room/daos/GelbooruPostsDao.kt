package `fun`.aragaki.kraft.data.room.daos

import `fun`.aragaki.kraft.data.entities.GelbooruPostResponse
import `fun`.aragaki.kraft.data.entities.MoebooruPost
import androidx.paging.PagingSource
import androidx.room.*

@Dao
interface GelbooruPostsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(post: GelbooruPostResponse.GelbooruPost)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(post: List<GelbooruPostResponse.GelbooruPost>)

    @Delete
    fun delete(post: GelbooruPostResponse.GelbooruPost)

    @Update
    fun update(post: GelbooruPostResponse.GelbooruPost)

    @Query("select * from GelbooruPosts where booruId=:booruId and id=:id")
    fun query(booruId: Int, id: Long): GelbooruPostResponse.GelbooruPost?

    @Query("select * from GelbooruPosts where booruId=:booruId order by id desc")
    fun pagingSource(booruId: Int): PagingSource<Int, GelbooruPostResponse.GelbooruPost>

    @Query("select * from GelbooruPosts")
    fun query(): List<GelbooruPostResponse.GelbooruPost>

    @Query("delete from GelbooruPosts")
    fun clear()
}