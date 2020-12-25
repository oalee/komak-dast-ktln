package xyz.lrhm.komakdast.core.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey




@Entity
data class Lesson(
    @PrimaryKey val id: Int,

    val resolved: Boolean = false,

    val type: Type? = null,

    val video: String? = null,

    val pics: String? = null,

    val answer: String? = null
){
    enum class Type(val type: String){
        FourPics("4pics"),
        OnePic("1pic"),
        Keyboard("keyboard")
    }


}

