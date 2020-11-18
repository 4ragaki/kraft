package `fun`.aragaki.kraft.data.room.daos

import `fun`.aragaki.kraft.data.room.entities.GelbooruPost
import androidx.room.*

@Dao
interface GelbooruPostsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(post: GelbooruPost)

    @Delete
    fun delete(post: GelbooruPost)

    @Update
    fun update(post: GelbooruPost)

    @Query("select * from GelbooruPosts where gelbooruId=:gelbooruId and id=:id")
    fun query(gelbooruId: Int, id: Long): GelbooruPost?

    @Query("select * from GelbooruPosts")
    fun query(): List<GelbooruPost>
}