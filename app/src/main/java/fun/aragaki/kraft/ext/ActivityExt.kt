package `fun`.aragaki.kraft.ext

import `fun`.aragaki.kraft.Settings
import android.app.Activity
import android.content.Intent
import android.provider.DocumentsContract
import android.util.TypedValue
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts

fun Activity.resolveColor(attr: Int): Int {
    val value = TypedValue()
    theme.resolveAttribute(attr, value, true)
    return value.data
}

fun Activity.toast(str: String, length: Int = Toast.LENGTH_LONG) =
    Toast.makeText(this, str, length).show()

fun ComponentActivity.registerRequestDocumentTree(settings: Settings) =
    registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) {
        it?.let {
            contentResolver.takePersistableUriPermission(
                it, Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            settings.edit { preferences ->
                preferences.putString(settings.docTreeAuthority.key, it.authority)
                preferences.putString(
                    settings.docTreeId.key, DocumentsContract.getTreeDocumentId(it)
                )
            }
        }
    }