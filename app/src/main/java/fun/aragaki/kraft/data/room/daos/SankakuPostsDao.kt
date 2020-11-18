/*
 * Copyright (c) 2020 by Aragaki <4ragaki@gmail.com>, All rights reserved.
 */

package `fun`.aragaki.kraft.data.room.daos

import `fun`.aragaki.kraft.data.entities.MoebooruPost
import `fun`.aragaki.kraft.data.entities.SankakuPost
import androidx.paging.PagingSource
import androidx.room.*

@Dao
interface SankakuPostsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(post: SankakuPost)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(post: List<SankakuPost>)

    @Delete
    fun delete(post: SankakuPost)

    @Update
    fun update(post: SankakuPost)

    @Query("select * from SankakuPosts where booruId=:booruId and id=:id")
    fun query(booruId: Int, id: Long): SankakuPost?

    @Query("select * from SankakuPosts where booruId=:booruId order by id desc")
    fun pagingSource(booruId: Int): PagingSource<Int, SankakuPost>

    @Query("select * from SankakuPosts")
    fun query(): List<SankakuPost>

    @Query("delete from SankakuPosts")
    fun clear()
}