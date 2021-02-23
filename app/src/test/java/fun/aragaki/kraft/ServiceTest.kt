package `fun`.aragaki.kraft

import `fun`.aragaki.kraft.data.services.PixivAuth
import `fun`.aragaki.kraft.ui.preferences.CredentialsFragment
import org.junit.Test
import retrofit2.Retrofit
import java.io.File


class ServiceTest {

    @Test
    fun pixivLogin() {
        Retrofit.Builder().build().create(PixivAuth::class.java).login(CredentialsFragment.verifier,"6SKTF6fL7GKTeZ6k3DrHu65QrHRRu_zr").let {
            File("/home/aragaki/Desktop/response.json").writeText(it.string())
        }
    }
}