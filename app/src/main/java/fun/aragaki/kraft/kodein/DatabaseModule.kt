package `fun`.aragaki.kraft.kodein

import `fun`.aragaki.kraft.data.room.KraftDatabase
import `fun`.aragaki.kraft.data.room.daos.*
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

val databaseModule = DI.Module("databaseModule") {
    bind<KraftDatabase>() with singleton { KraftDatabase.build(instance()) }

    bind<MoebooruDao>() with singleton { instance<KraftDatabase>().moebooruSitesDao() }
    bind<DanbooruDao>() with singleton { instance<KraftDatabase>().danbooruSitesDao() }
    bind<GelbooruDao>() with singleton { instance<KraftDatabase>().gelbooruSitesDao() }
    bind<SankakuDao>() with singleton { instance<KraftDatabase>().sankakuSitesDao() }
    bind<PixivDao>() with singleton { instance<KraftDatabase>().pixivSitesDao() }

    bind<MoebooruPostsDao>() with singleton { instance<KraftDatabase>().moebooruPostsDao() }
    bind<DanbooruPostsDao>() with singleton { instance<KraftDatabase>().danbooruPostsDao() }
    bind<GelbooruPostsDao>() with singleton { instance<KraftDatabase>().gelbooruPostsDao() }
    bind<SankakuPostsDao>() with singleton { instance<KraftDatabase>().sankakuPostsDao() }
    bind<PixivIllustsDao>() with singleton { instance<KraftDatabase>().pixivIllustsDao() }
}