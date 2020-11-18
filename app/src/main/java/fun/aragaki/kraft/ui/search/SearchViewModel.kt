package `fun`.aragaki.kraft.ui.search

import `fun`.aragaki.kraft.Kraft
import `fun`.aragaki.kraft.Settings
import `fun`.aragaki.kraft.data.CredentialException
import `fun`.aragaki.kraft.data.entities.SauceNaoResponse
import `fun`.aragaki.kraft.data.services.IQDBService
import `fun`.aragaki.kraft.data.services.SauceNaoService
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.kodein.di.instance
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.max

@Suppress("UNNECESSARY_NOT_NULL_ASSERTION")
class SearchViewModel(app: Kraft) : AndroidViewModel(app) {
    private val settings by app.instance<Settings>()
    private val images = LinkedList<Uri>()
    private val _image = MutableSharedFlow<Uri>(1)
    val image: SharedFlow<Uri> = _image
    val title = MutableSharedFlow<String?>()
    val progress = MutableSharedFlow<Float>()

    val queueEnd = MutableSharedFlow<Boolean>(1)

    private val _sauceNaoResponse = MutableSharedFlow<Pair<Uri, SauceNaoResponse>>(1)
    val sauceNaoResponse: SharedFlow<Pair<Uri, SauceNaoResponse>> = _sauceNaoResponse
    private val _iqdbResponse = MutableSharedFlow<Pair<Uri, ResponseBody>>(1)
    val iqdbResponse: SharedFlow<Pair<Uri, ResponseBody>> = _iqdbResponse

    fun sauceNao(image: Uri, onFailure: (Throwable) -> Unit) {
        val sauceNaoService by getApplication<Kraft>().instance<SauceNaoService>()
        val settings by getApplication<Kraft>().instance<Settings>()
        image.lastPathSegment?.let { name ->
            scale(image, settings.scaleSource.value)?.let { bytes ->
                viewModelScope.launch(Dispatchers.Main) {
                    kotlin.runCatching {
                        _sauceNaoResponse.emit(image to withContext(Dispatchers.IO) {
                            sauceNaoService.search(settings.sauceNaoApiKey.value.takeIf { it?.isNotBlank() == true }
                                ?: throw CredentialException.SauceNaoApiKeyEmptyException,
                                MultipartBody.Part.createFormData(
                                    "file", name, bytes.toRequestBody()
                                ))
                        }!!)
                    }.onFailure {
                        onFailure(it)
                        it.printStackTrace()
                    }
                }
            }
        }
    }

    fun iqdb(image: Uri, onFailure: (Throwable) -> Unit) {
        viewModelScope.launch(Dispatchers.Main) {
            val iqdbService by getApplication<Kraft>().instance<IQDBService>()
            kotlin.runCatching {
                _iqdbResponse.emit(image to withContext(Dispatchers.IO) {
                    iqdbService.search(getImageAsPart(image))
                }!!)
            }.onFailure {
                onFailure(it)
            }
        }
    }

    private fun getImageAsPart(image: Uri, name: String = "file"): MultipartBody.Part {
        image.lastPathSegment?.let { filename ->
            scale(image, settings.scaleSource.value)?.let { bytes ->
                return MultipartBody.Part.createFormData(
                    name, filename, bytes.toRequestBody()
                )
            }
        }
        throw Exception("can't create MultipartBody.")
    }

    private fun scale(image: Uri, enabled: Boolean): ByteArray? {
        val ops = BitmapFactory.Options().apply { inJustDecodeBounds = true }.also {
            BitmapFactory.decodeStream(
                getApplication<Kraft>().contentResolver.openInputStream(image), null, it
            )
        }

        val outHeight = ops.outHeight
        val outWidth = ops.outWidth
        if (enabled && (outHeight > SCALE_SIZE || outWidth > SCALE_SIZE)) {
            ops.apply {
                inJustDecodeBounds = false
                inSampleSize = max(outHeight / SCALE_SIZE, outWidth / SCALE_SIZE)
            }

            BitmapFactory.decodeStream(
                getApplication<Kraft>().contentResolver.openInputStream(image), null, ops
            )?.let { bitmap ->
                return ByteArrayOutputStream().apply {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, this)
                }.toByteArray()
            }
        }

        return getApplication<Kraft>().contentResolver.openInputStream(image)?.readBytes()
    }

    fun queueOffer(uri: Uri) = images.offer(uri)
    fun queueOffer(uris: ArrayList<Uri>) = uris.forEach { images.offer(it) }
    fun queuePoll() = viewModelScope.launch {
        images.poll()?.let {
            _image.emit(it)
            if (images.size == 0) queueEnd.emit(true)
        } ?: queueEnd.emit(true)
    }

    companion object {
        const val SCALE_SIZE = 300
    }
}