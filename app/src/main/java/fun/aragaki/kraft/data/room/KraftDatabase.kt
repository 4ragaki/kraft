package `fun`.aragaki.kraft.data.room

import `fun`.aragaki.kraft.data.room.daos.*
import `fun`.aragaki.kraft.data.room.entities.*
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [MoebooruSite::class, MoebooruPost::class,
        DanbooruSite::class, DanbooruPost::class,
        GelbooruSite::class, GelbooruPost::class,
        SankakuSite::class, SankakuPost::class,
        PixivSite::class, PixivIllust::class],
    version = 1
)
abstract class KraftDatabase : RoomDatabase() {

    abstract fun moebooruSitesDao(): MoebooruSitesDao
    abstract fun danbooruSitesDao(): DanbooruSitesDao
    abstract fun gelbooruSitesDao(): GelbooruSitesDao
    abstract fun sankakuSitesDao(): SankakuSitesDao
    abstract fun pixivSitesDao(): PixivSitesDao

    abstract fun moebooruPostsDao(): MoebooruPostsDao
    abstract fun danbooruPostsDao(): DanbooruPostsDao
    abstract fun gelbooruPostsDao(): GelbooruPostsDao
    abstract fun sankakuPostsDao(): SankakuPostsDao
    abstract fun pixivIllustsDao(): PixivIllustsDao

    companion object {
        fun build(context: Context): KraftDatabase {
            return Room.databaseBuilder(
                context,
                KraftDatabase::class.java,
                "kraft.db"
            ).allowMainThreadQueries()
                .build()
        }
    }
}