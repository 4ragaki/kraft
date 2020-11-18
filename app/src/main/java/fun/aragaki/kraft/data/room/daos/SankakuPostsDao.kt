/*
 * Copyright (c) 2020 by Aragaki <4ragaki@gmail.com>, All rights reserved.
 */

package `fun`.aragaki.kraft.data.room.daos

import `fun`.aragaki.kraft.data.room.entities.SankakuPost
import androidx.room.*

@Dao
interface SankakuPostsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(post: SankakuPost)

    @Delete
    fun delete(post: SankakuPost)

    @Update
    fun update(post: SankakuPost)

    @Query("select * from SankakuPosts where sankakuId=:sankakuId and id=:id")
    fun query(sankakuId: Int, id: Long): SankakuPost?

    @Query("select * from SankakuPosts")
    fun query(): List<SankakuPost>
}