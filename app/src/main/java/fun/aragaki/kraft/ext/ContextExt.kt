package `fun`.aragaki.kraft.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.DocumentsContract
import androidx.documentfile.provider.DocumentFile

inline fun <reified A : Activity> Context.startActivity(
    options: Bundle? = null, noinline intent: (Intent.() -> Unit) = {}
) {
    startActivity(Intent(this, A::class.java).apply(intent), options)
}

fun Context.getDocumentTree(authority: String, treeId: String) =
    DocumentFile.fromTreeUri(this, DocumentsContract.buildTreeDocumentUri(authority, treeId))

//val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")