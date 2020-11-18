package `fun`.aragaki.kraft.data.entities

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class SauceNaoResponse(
    val header: Header?,
    val results: List<Result>?
) {

    @Keep
    @Serializable
    data class Header(
        val account_type: String?,
        val index: Index?,
        val long_limit: String?,
        val long_remaining: Int?,
        val minimum_similarity: Double?,
        val query_image: String?,
        val query_image_display: String?,
        val results_requested: Int?,
        val results_returned: Int?,
        val search_depth: String?,
        val short_limit: String?,
        val short_remaining: Int?,
        val status: Int?,
        val user_id: String?
    ) {
        @Keep
        @Serializable
        data class Index(
            val `0`: X0?,
            val `10`: X10?,
            val `11`: X11?,
            val `12`: X12?,
            val `16`: X16?,
            val `18`: X18?,
            val `19`: X19?,
            val `2`: X2?,
            val `20`: X20?,
            val `21`: X21?,
            val `211`: X211?,
            val `22`: X22?,
            val `23`: X23?,
            val `24`: X24?,
            val `25`: X25?,
            val `26`: X26?,
            val `27`: X27?,
            val `28`: X28?,
            val `29`: X29?,
            val `30`: X30?,
            val `31`: X31?,
            val `32`: X32?,
            val `33`: X33?,
            val `34`: X34?,
            val `35`: X35?,
            val `36`: X36?,
            val `37`: X37?,
            val `5`: X5?,
            val `51`: X51?,
            val `52`: X52?,
            val `53`: X53?,
            val `6`: X6?,
            val `8`: X8?,
            val `9`: X9?
        ) {

            @Keep
            @Serializable
            data class X0(
                val id: Int?,
                val parent_id: Int?,
                val results: Int?,
                val status: Int?
            )

            @Keep
            @Serializable
            data class X10(
                val id: Int?,
                val parent_id: Int?,
                val results: Int?,
                val status: Int?
            )

            @Keep
            @Serializable
            data class X11(
                val id: Int?,
                val parent_id: Int?,
                val results: Int?,
                val status: Int?
            )

            @Keep
            @Serializable
            data class X12(
                val id: Int?,
                val parent_id: Int?,
                val status: Int?
            )

            @Keep
            @Serializable
            data class X16(
                val id: Int?,
                val parent_id: Int?,
                val results: Int?,
                val status: Int?
            )

            @Keep
            @Serializable
            data class X18(
                val id: Int?,
                val parent_id: Int?,
                val results: Int?,
                val status: Int?
            )

            @Keep
            @Serializable
            data class X19(
                val id: Int?,
                val parent_id: Int?,
                val results: Int?,
                val status: Int?
            )

            @Keep
            @Serializable
            data class X2(
                val id: Int?,
                val parent_id: Int?,
                val results: Int?,
                val status: Int?
            )

            @Keep
            @Serializable
            data class X20(
                val id: Int?,
                val parent_id: Int?,
                val results: Int?,
                val status: Int?
            )

            @Keep
            @Serializable
            data class X21(
                val id: Int?,
                val parent_id: Int?,
                val results: Int?,
                val status: Int?
            )

            @Keep
            @Serializable
            data class X211(
                val id: Int?,
                val parent_id: Int?,
                val results: Int?,
                val status: Int?
            )

            @Keep
            @Serializable
            data class X22(
                val id: Int?,
                val parent_id: Int?,
                val results: Int?,
                val status: Int?
            )

            @Keep
            @Serializable
            data class X23(
                val id: Int?,
                val parent_id: Int?,
                val results: Int?,
                val status: Int?
            )

            @Keep
            @Serializable
            data class X24(
                val id: Int?,
                val parent_id: Int?,
                val results: Int?,
                val status: Int?
            )

            @Keep
            @Serializable
            data class X25(
                val id: Int?,
                val parent_id: Int?,
                val status: Int?
            )

            @Keep
            @Serializable
            data class X26(
                val id: Int?,
                val parent_id: Int?,
                val status: Int?
            )

            @Keep
            @Serializable
            data class X27(
                val id: Int?,
                val parent_id: Int?,
                val status: Int?
            )

            @Keep
            @Serializable
            data class X28(
                val id: Int?,
                val parent_id: Int?,
                val status: Int?
            )

            @Keep
            @Serializable
            data class X29(
                val id: Int?,
                val parent_id: Int?,
                val status: Int?
            )

            @Keep
            @Serializable
            data class X30(
                val id: Int?,
                val parent_id: Int?,
                val status: Int?
            )

            @Keep
            @Serializable
            data class X31(
                val id: Int?,
                val parent_id: Int?,
                val results: Int?,
                val status: Int?
            )

            @Keep
            @Serializable
            data class X32(
                val id: Int?,
                val parent_id: Int?,
                val results: Int?,
                val status: Int?
            )

            @Keep
            @Serializable
            data class X33(
                val id: Int?,
                val parent_id: Int?,
                val results: Int?,
                val status: Int?
            )

            @Keep
            @Serializable
            data class X34(
                val id: Int?,
                val parent_id: Int?,
                val results: Int?,
                val status: Int?
            )

            @Keep
            @Serializable
            data class X35(
                val id: Int?,
                val parent_id: Int?,
                val results: Int?,
                val status: Int?
            )

            @Keep
            @Serializable
            data class X36(
                val id: Int?,
                val parent_id: Int?,
                val results: Int?,
                val status: Int?
            )

            @Keep
            @Serializable
            data class X37(
                val id: Int?,
                val parent_id: Int?,
                val results: Int?,
                val status: Int?
            )

            @Keep
            @Serializable
            data class X5(
                val id: Int?,
                val parent_id: Int?,
                val results: Int?,
                val status: Int?
            )

            @Keep
            @Serializable
            data class X51(
                val id: Int?,
                val parent_id: Int?,
                val results: Int?,
                val status: Int?
            )

            @Keep
            @Serializable
            data class X52(
                val id: Int?,
                val parent_id: Int?,
                val results: Int?,
                val status: Int?
            )

            @Keep
            @Serializable
            data class X53(
                val id: Int?,
                val parent_id: Int?,
                val results: Int?,
                val status: Int?
            )

            @Keep
            @Serializable
            data class X6(
                val id: Int?,
                val parent_id: Int?,
                val results: Int?,
                val status: Int?
            )

            @Keep
            @Serializable
            data class X8(
                val id: Int?,
                val parent_id: Int?,
                val results: Int?,
                val status: Int?
            )

            @Keep
            @Serializable
            data class X9(
                val id: Int?,
                val parent_id: Int?,
                val results: Int?,
                val status: Int?
            )
        }
    }

    @Keep
    @Serializable
    data class Result(
        val `data`: Data?,
        val header: Header?
    ) {
        @Keep
        @Serializable
        data class Data(
            val anidb_aid: String?,
            val author_name: String?,
            val author_url: String?,
            val bcy_id: String?,
            val bcy_type: String?,
            val characters: String?,
            val company: String?,
            val created_at: String?,
            val creator: String?,
            val da_id: String?,
            val danbooru_id: String?,
            val est_time: String?,
            val ext_urls: List<String?>?,
            val getchu_id: String?,
            val material: String?,
            val member_id: String?,
            val member_link_id: String?,
            val member_name: String?,
            val part: String?,
            val pawoo_id: String?,
            val pawoo_user_acct: String?,
            val pawoo_user_display_name: String?,
            val pawoo_user_username: String?,
            val pixiv_id: String?,
            val sankaku_id: String?,
            val seiga_id: String?,
            val source: String?,
            val title: String?,
            val year: String?
        )

        @Keep
        @Serializable
        data class Header(
            val index_id: Int?,
            val index_name: String?,
            val similarity: String?,
            val thumbnail: String?
        )
    }
}