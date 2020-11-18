package `fun`.aragaki.kraft.data.room.daos

import `fun`.aragaki.kraft.data.room.entities.MoebooruPost
import androidx.room.*

@Dao
interface MoebooruPostsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(post: MoebooruPost)

    @Delete
    fun delete(post: MoebooruPost)

    @Update
    fun update(post: MoebooruPost)

    @Query("select * from MoebooruPosts where moebooruId=:moebooruId and id=:id")
    fun query(moebooruId: Int, id: Long): MoebooruPost?

    @Query("select * from MoebooruPosts")
    fun query(): List<MoebooruPost>
}