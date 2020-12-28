package xyz.lrhm.komakdast.core.util.legacy

class ImageKey {
    var data: String

    internal constructor(resourceId: Int, width: Int, height: Int) {
        data = "_$resourceId,$width,$height"
    }

    internal constructor(relativePath: String, width: Int, height: Int) {
        data = "@$relativePath,$width,$height"
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val imageKey = o as ImageKey
        return if (if (data != null) data != imageKey.data else imageKey.data != null) false else true
    }

    override fun hashCode(): Int {
        return if (data != null) data.hashCode() else 0
    }

    override fun toString(): String {
        return data
    }
}