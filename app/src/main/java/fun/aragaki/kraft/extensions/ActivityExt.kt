package `fun`.aragaki.kraft.extensions

import `fun`.aragaki.kraft.Settings
import android.app.Activity
import android.content.Context
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

fun Context.toast(str: String?, length: Int = Toast.LENGTH_LONG) =
    str?.let { Toast.makeText(this, str, length).show() }