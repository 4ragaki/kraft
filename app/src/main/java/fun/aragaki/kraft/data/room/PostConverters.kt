package `fun`.aragaki.kraft.data.room

import androidx.room.TypeConverter

object PostConverters {
    private const val LIST_DELIMITER = " "

    @TypeConverter
    @JvmStatic
    fun stringToList(string: String?): List<String>? {
        return string?.split(LIST_DELIMITER)
    }

    @TypeConverter
    @JvmStatic
    fun listToString(list: List<String>?): String? {
        return list?.joinToString(LIST_DELIMITER)
    }
}