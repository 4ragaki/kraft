package `fun`.aragaki.kraft.extensions

import `fun`.aragaki.kraft.data.extensions.Post
import `fun`.aragaki.kraft.data.servicewrappers.BooruWrapper
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import androidx.documentfile.provider.DocumentFile

inline fun <reified A : Activity> Context.startActivity(
    options: Bundle? = null, noinline intent: (Intent.() -> Unit) = {}
) {
    startActivity(Intent(this, A::class.java).apply(intent), options)
}

fun Context.view(uri: String) = startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))

fun Context.getDocumentTree(authority: String, treeId: String) =
    DocumentFile.fromTreeUri(this, DocumentsContract.buildTreeDocumentUri(authority, treeId))

//val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

fun List<BooruWrapper>.find(post: Post) =
    this@find.find { it.booru.booruId == post.booruId } ?: first()