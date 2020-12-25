package xyz.lrhm.komakdast.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Package constructor(
    @PrimaryKey val id: Int,
    val name: String? = null,
    val revision: Int= 0,
    val isLocal: Boolean = false
) {
}