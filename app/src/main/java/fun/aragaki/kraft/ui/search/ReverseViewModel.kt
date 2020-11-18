package `fun`.aragaki.kraft.ui.search

import `fun`.aragaki.kraft.Kraft
import `fun`.aragaki.kraft.Settings
import `fun`.aragaki.kraft.data.entities.SauceNaoResponse
import `fun`.aragaki.kraft.data.services.IQDBService
import `fun`.aragaki.kraft.data.services.SauceNaoService
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.kodein.di.instance
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.math.max

class ReverseViewModel(app: Kraft) : AndroidViewModel(app) {
    private val images = LinkedList<Uri>()
    private val _image = MutableLiveData<Uri>()
    val image: LiveData<Uri> = _image

    private val _sauceNaoResponse = MutableLiveData<SauceNaoResponse>()
    val saucenaoResponse: LiveData<SauceNaoResponse> = _sauceNaoResponse
    private val _iqdbResponse = MutableLiveData<ResponseBody>()
    val iqdbResponse: LiveData<ResponseBody> = _iqdbResponse

    fun sauceNao(image: Uri, onFailure: (Throwable) -> Unit) {
        val sauceNaoService by getApplication<Kraft>().instance<SauceNaoService>()
        val settings by getApplication<Kraft>().instance<Settings>()
        image.lastPathSegment?.let { name ->
            scale(image)?.let { bytes ->
                viewModelScope.launch(Dispatchers.Main) {
                    kotlin.runCatching {
                        _sauceNaoResponse.value = withContext(Dispatchers.IO) {
                            sauceNaoService.search(
                                settings.sauceNaoApiKey.value ?: "",
                                MultipartBody.Part.createFormData(
                                    "file", name, bytes.toRequestBody()
                                )
                            )
                        }!!
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
                _iqdbResponse.value = withContext(Dispatchers.IO) {
                    iqdbService.search(getImageAsPart(image))
                }!!
            }.onFailure {
                onFailure(it)
            }
        }
    }

    private fun getImageAsPart(image: Uri, name: String = "file"): MultipartBody.Part {
        image.lastPathSegment?.let { filename ->
            scale(image)?.let { bytes ->
                return MultipartBody.Part.createFormData(
                    name, filename, bytes.toRequestBody()
                )
            }
        }
        throw Exception("can't create MultipartBody.")
    }

    private fun scale(image: Uri): ByteArray? {
        val ops = BitmapFactory.Options().apply { inJustDecodeBounds = true }.also {
            BitmapFactory.decodeStream(
                getApplication<Kraft>().contentResolver.openInputStream(image),
                null, it
            )
        }

        val outHeight = ops.outHeight
        val outWidth = ops.outWidth
        if (outHeight > SCALE_SIZE || outWidth > SCALE_SIZE) {
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
    fun queueEnd() = images.size == 0
    fun queuePoll() {
        _image.value = images.poll()
    }

    companion object {
        const val SCALE_SIZE = 300
    }
}