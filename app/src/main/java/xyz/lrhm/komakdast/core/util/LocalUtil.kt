package xyz.lrhm.komakdast.core.util

import android.content.Context
import com.squareup.moshi.Moshi
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalUtil @Inject constructor( val moshi: Moshi, val context: Context )  {

    fun parseLocalJson(){

        try {
            val inputStream= context.assets.open("")
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            val string = String(buffer)
            

        } catch (e: IOException) {
            e.printStackTrace()
        }


    }

}