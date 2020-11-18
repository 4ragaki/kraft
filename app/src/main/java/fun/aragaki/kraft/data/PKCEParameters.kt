package `fun`.aragaki.kraft.data

import android.util.Base64
import java.security.MessageDigest
import java.security.SecureRandom

class PKCEParameters(size: Int = 32, algorithm: String = "SHA-256") {

    val verifier: String = Base64.encodeToString(
        ByteArray(size).also { SecureRandom().nextBytes(it) },
        Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING
    )

    val challenge: String = Base64.encodeToString(
        MessageDigest.getInstance(algorithm)
            .apply { update(verifier.toByteArray(Charsets.US_ASCII)) }.digest(),
        Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING
    )
}