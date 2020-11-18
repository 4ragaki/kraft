package `fun`.aragaki.kraft

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.StringRes

class Settings(private val context: Context, private val preferences: SharedPreferences) {

    val docTreeAuthority = StringValue(R.string.pref_key_doc_tree_authority)
    val docTreeId = StringValue(R.string.pref_key_doc_tree_id)

    val bioAuth = BooleanValue(R.string.pref_key_bio_auth, false)
    val nsfw = BooleanValue(R.string.pref_key_explicit, false)
    val detectClipBoard = BooleanValue(R.string.pref_key_detect_clipboard, true)
    val scaleSource = BooleanValue(R.string.pref_key_scale_source, true)
    val failureAction = StringValue(R.string.pref_key_failure_actions)
    val fmtPostName =
        StringValue(R.string.pref_key_post_name_format, getString(R.string.post_fmt_default))

    val pixivAccessToken = StringValue(R.string.pref_key_cre_pixiv_access_token)
    val pixivRefreshToken = StringValue(R.string.pref_key_cre_pixiv_refresh_token)
    val sauceNaoApiKey = StringValue(R.string.pref_key_cre_saucenao_apikey)
    val yandereUsername = StringValue(R.string.pref_key_cre_yandere_username)
    val yandereHash = StringValue(R.string.pref_key_cre_yandere_hash)
    val danbooruUsername = StringValue(R.string.pref_key_cre_danbooru_username)
    val danbooruHash = StringValue(R.string.pref_key_cre_danbooru_hash)
    val gelbooruUsername = StringValue(R.string.pref_key_cre_gelbooru_username)
    val gelbooruHash = StringValue(R.string.pref_key_cre_gelbooru_hash)
    val sankakuUsername = StringValue(R.string.pref_key_cre_sankaku_username)
    val sankakuHash = StringValue(R.string.pref_key_cre_sankaku_hash)
    val konachanUsername = StringValue(R.string.pref_key_cre_konachan_username)
    val konachanHash = StringValue(R.string.pref_key_cre_konachan_hash)
    val safebooruUsername = StringValue(R.string.pref_key_cre_safebooru_username)
    val safebooruHash = StringValue(R.string.pref_key_cre_safebooru_hash)

    fun edit(block: SharedPreferences.Editor.() -> Unit) {
        preferences.edit().apply {
            apply(block)
            apply()
        }
    }

    private fun getString(@StringRes id: Int) = context.getString(id)

    open inner class NullableSetting<V>(
        @StringRes private val keyId: Int,
        private inline val valueGetter: (key: String) -> V?
    ) {
        val key get() = getString(keyId)
        open val value: V? get() = valueGetter(key)
    }

    open inner class Setting<V>(
        @StringRes private val keyId: Int,
        private inline val valueGetter: (key: String) -> V
    ) : NullableSetting<V>(keyId, valueGetter) {
        override val value: V get() = valueGetter(key)
    }


    inner class BooleanValue(@StringRes keyId: Int, default: Boolean) :
        Setting<Boolean>(keyId, { preferences.getBoolean(it, default) })

    inner class StringValue(@StringRes keyId: Int, default: String? = null) :
        NullableSetting<String>(keyId, { preferences.getString(it, default) })
}