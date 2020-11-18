package `fun`.aragaki.kraft.data.room

import `fun`.aragaki.kraft.Kraft
import `fun`.aragaki.kraft.data.entities.PixivIllustrationResponse
import `fun`.aragaki.kraft.data.entities.SankakuPost
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.kodein.di.instance

object PostConverters {
    private val gson by Kraft.app.instance<Gson>()
    private const val LIST_DELIMITER = " "

    @TypeConverter
    @JvmStatic
    fun string2List(string: String?) = string?.split(LIST_DELIMITER)

    @TypeConverter
    @JvmStatic
    fun list2String(list: List<String>?) = list?.joinToString(LIST_DELIMITER)

    @TypeConverter
    @JvmStatic
    fun sankakuTags2Json(tags: List<SankakuPost.Tag>): String = gson.toJson(tags)

    @TypeConverter
    @JvmStatic
    fun json2SankakuTags(string: String?): List<SankakuPost.Tag> =
        gson.fromJson<List<SankakuPost.Tag>>(
            string,
            object : TypeToken<List<SankakuPost.Tag>>() {}.type
        )

    @TypeConverter
    @JvmStatic
    fun pixivMetaPages2Json(pages: List<PixivIllustrationResponse.PixivIllustration.MetaPage>): String =
        gson.toJson(pages)

    @TypeConverter
    @JvmStatic
    fun json2PixivMetaPages(string: String?): List<PixivIllustrationResponse.PixivIllustration.MetaPage> =
        gson.fromJson(
            string,
            object : TypeToken<List<PixivIllustrationResponse.PixivIllustration.MetaPage>>() {}.type
        )

    @TypeConverter
    @JvmStatic
    fun pixivTags2Json(tags: List<PixivIllustrationResponse.PixivIllustration.Tag>): String =
        gson.toJson(tags)

    @TypeConverter
    @JvmStatic
    fun json2pixivTags(string: String?): List<PixivIllustrationResponse.PixivIllustration.Tag> =
        gson.fromJson(
            string,
            object : TypeToken<List<PixivIllustrationResponse.PixivIllustration.Tag>>() {}.type
        )
}