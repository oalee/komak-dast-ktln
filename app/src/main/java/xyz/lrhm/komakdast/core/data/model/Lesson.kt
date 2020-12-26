package xyz.lrhm.komakdast.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

//"file:///android_asset" + "/Packages/package_"

@Entity
data class Lesson(

    @PrimaryKey(autoGenerate = true)
    val key: Int?,

    val id: Int,

    val resolved: Boolean = true,

    val type: Type? = null,

    val video: String? = null,

    val pics: String? = null,

    val answer: String? = null,

    var packageId: Int? = null
) {
    enum class Type(val type: String) {
        @Json(name = "4pics")
        FourPics("4pics"),

        @Json(name = "1pic")
        OnePic("1pic"),

        @Json(name = "keyboard")
        Keyboard("keyboard")
    }


}

