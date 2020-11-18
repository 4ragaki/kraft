package `fun`.aragaki.kraft.data.room.daos

import `fun`.aragaki.kraft.data.room.entities.DanbooruPost
import androidx.room.*

@Dao
interface DanbooruPostsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(post: DanbooruPost)

    @Delete
    fun delete(post: DanbooruPost)

    @Update
    fun update(post: DanbooruPost)

    @Query("select * from DanbooruPosts where danbooruId=:danbooruId and id=:id")
    fun query(danbooruId: Int, id: Long): DanbooruPost?

    @Query("select * from DanbooruPosts")
    fun query(): List<DanbooruPost>

}