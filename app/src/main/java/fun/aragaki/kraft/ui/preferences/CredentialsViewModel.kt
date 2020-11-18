package `fun`.aragaki.kraft.ui.preferences

import `fun`.aragaki.kraft.Kraft
import `fun`.aragaki.kraft.Settings
import `fun`.aragaki.kraft.data.PKCEParameters
import `fun`.aragaki.kraft.data.servicewrappers.PixivWrapper
import `fun`.aragaki.kraft.kodein.PIXIV
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.instance

class CredentialsViewModel(app: Kraft) : AndroidViewModel(app) {
    private val pkceParameters by app.instance<PKCEParameters>()
    private val settings by app.instance<Settings>()
    private val pixiv by app.instance<PixivWrapper>(PIXIV)

    fun genPixivAuthIntent() = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("https://app-api.pixiv.net/web/v1/login?code_challenge=${pkceParameters.challenge}&code_challenge_method=S256&client=pixiv-android")
    )

    fun handle(uri: Uri, success: (host: String) -> Unit) = uri.run {
        when (scheme) {
            "pixiv" -> {
                getQueryParameter("code")?.takeIf { getQueryParameter("via") == "login" }
                    ?.let { code ->
                        viewModelScope.launch(Dispatchers.IO) {
                            pixiv.pixivToken(pkceParameters.verifier, code).run {
                                settings.edit {
                                    putString(settings.pixivAccessToken.key, access_token)
                                    putString(settings.pixivRefreshToken.key, refresh_token)
                                }
                                withContext(Dispatchers.Main) {
                                    success(pixiv.booru.authHost)
                                }
                            }
                        }
                    }
                true
            }
            else -> false
        }
    }
}