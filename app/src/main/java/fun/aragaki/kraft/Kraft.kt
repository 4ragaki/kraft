package `fun`.aragaki.kraft

import `fun`.aragaki.kraft.data.room.daos.*
import `fun`.aragaki.kraft.data.servicewrappers.*
import `fun`.aragaki.kraft.kodein.*
import `fun`.aragaki.kraft.ui.ViewModelFactory
import android.app.Application
import android.content.Context
import com.google.android.material.color.DynamicColors
import org.kodein.di.*

class Kraft : Application(), DIAware {

    override val di: DI by DI.lazy {
        bind<Kraft>() with instance(this@Kraft)
        bind<Context>() with instance(this@Kraft)
        bind<ViewModelFactory>() with singleton { ViewModelFactory(this@Kraft) }
        import(supportModule)
        import(databaseModule)
        import(serviceModule)
        import(boorusModule)

        bind<List<DanbooruWrapper>>() with provider {
            mutableListOf<DanbooruWrapper>().also { result ->
                result.add(instance(DANBOORU))
                result.add(instance(SAFEBOORU))
                instance<DanbooruDao>().query().forEach {
                    result.add(instance(arg = it))
                }
            }
        }

        bind<List<MoebooruWrapper>>() with provider {
            mutableListOf<MoebooruWrapper>().also { result ->
                result.add(instance(YANDE_RE))
                result.add(instance(KONACHAN))
                instance<MoebooruDao>().query().forEach {
                    result.add(instance(arg = it))
                }
            }
        }

        bind<List<GelbooruWrapper>>() with provider {
            mutableListOf<GelbooruWrapper>().also { result ->
                result.add(instance(GELBOORU))
                instance<GelbooruDao>().query().forEach {
                    result.add(instance(arg = it))
                }
            }
        }

        bind<List<SankakuWrapper>>() with provider {
            mutableListOf<SankakuWrapper>().also { result ->
                result.add(instance(SANKAKU))
                instance<SankakuDao>().query().forEach {
                    result.add(instance(arg = it))
                }
            }
        }

        bind<List<PixivWrapper>>() with provider {
            mutableListOf<PixivWrapper>().also { result ->
                result.add(instance(PIXIV))
                instance<PixivDao>().apply {
                    query().forEach {
                        result.add(instance(arg = it))
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
        DynamicColors.applyToActivitiesIfAvailable(this)
        app = this
    }

    companion object {
        lateinit var app: Kraft
    }
}