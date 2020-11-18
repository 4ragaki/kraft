package `fun`.aragaki.kraft

import `fun`.aragaki.kraft.data.room.daos.*
import `fun`.aragaki.kraft.data.servicewrappers.*
import `fun`.aragaki.kraft.kodein.*
import android.app.Application
import android.content.Context
import org.kodein.di.*

class Kraft : Application(), DIAware {

    override val di: DI by DI.lazy {
        bind<Context>() with singleton { this@Kraft }
        import(supportModule)
        import(databaseModule)
        import(serviceModule)
        import(boorusModule)

        bind<List<DanbooruWrapper>>() with provider {
            mutableListOf<DanbooruWrapper>().also { result ->
                result.add(instance(DANBOORU))
                result.add(instance(SAFEBOORU))
                instance<DanbooruSitesDao>().query().forEach {
                    result.add(instance(arg = it.toBooru()))
                }
            }
        }

        bind<List<MoebooruWrapper>>() with provider {
            mutableListOf<MoebooruWrapper>().also { result ->
                result.add(instance(YANDE_RE))
                result.add(instance(KONACHAN))
                instance<MoebooruSitesDao>().query().forEach {
                    result.add(instance(arg = it.toBooru()))
                }
            }
        }

        bind<List<GelbooruWrapper>>() with provider {
            mutableListOf<GelbooruWrapper>().also { result ->
                result.add(instance(GELBOORU))
                instance<GelbooruSitesDao>().query().forEach {
                    result.add(instance(arg = it.toBooru()))
                }
            }
        }

        bind<List<SankakuWrapper>>() with provider {
            mutableListOf<SankakuWrapper>().also { result ->
                result.add(instance(SANKAKU))
                instance<SankakuSitesDao>().query().forEach {
                    result.add(instance(arg = it.toBooru()))
                }
            }
        }

        bind<List<PixivWrapper>>() with provider {
            mutableListOf<PixivWrapper>().also { result ->
                result.add(instance(PIXIV))
                instance<PixivSitesDao>().apply {
                    query().forEach {
                        result.add(instance(arg = it.toBooru(this)))
                    }
                }
            }
        }

        bind<List<BooruWrapper>>() with provider {
            mutableListOf<BooruWrapper>().apply {
                addAll(instance<List<DanbooruWrapper>>())
                addAll(instance<List<MoebooruWrapper>>())
                addAll(instance<List<GelbooruWrapper>>())
                addAll(instance<List<SankakuWrapper>>())
                addAll(instance<List<PixivWrapper>>())
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        app = this
    }

    companion object {
        lateinit var app: Kraft
    }
}