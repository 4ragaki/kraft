package `fun`.aragaki.kraft.data.room

import `fun`.aragaki.kraft.data.entities.*
import `fun`.aragaki.kraft.data.room.daos.*
import `fun`.aragaki.kraft.data.servicewrappers.Boorus
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Boorus.Moebooru::class, MoebooruPost::class,
        Boorus.Danbooru::class, DanbooruPost::class,
        Boorus.Gelbooru::class, GelbooruPostResponse.GelbooruPost::class,
        Boorus.Sankaku::class, SankakuPost::class,
        Boorus.Pixiv::class, PixivIllustrationResponse.PixivIllustration::class],
    version = 1
)
abstract class KraftDatabase : RoomDatabase() {

    abstract fun moebooruSitesDao(): MoebooruDao
    abstract fun danbooruSitesDao(): DanbooruDao
    abstract fun gelbooruSitesDao(): GelbooruDao
    abstract fun sankakuSitesDao(): SankakuDao
    abstract fun pixivSitesDao(): PixivDao

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