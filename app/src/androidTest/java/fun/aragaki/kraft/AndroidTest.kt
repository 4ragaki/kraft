package `fun`.aragaki.kraft

import `fun`.aragaki.kraft.data.servicewrappers.Boorus
import `fun`.aragaki.kraft.data.servicewrappers.PixivWrapper
import `fun`.aragaki.kraft.kodein.*
import androidx.test.runner.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.kodein.di.DI
import org.kodein.di.instance


@RunWith(AndroidJUnit4::class)
class AndroidTest {
    private val di by DI.lazy {
        import(boorusModule)
        import(databaseModule)
        import(serviceModule)
        import(supportModule)
    }

    @Test
    fun pixivBookmarkTest() = runBlocking {
        val pixiv by di.instance<PixivWrapper>(PIXIV)
        pixiv.vote(true, 82301307)
    }
}