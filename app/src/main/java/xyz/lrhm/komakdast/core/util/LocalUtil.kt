package xyz.lrhm.komakdast.core.util

import android.content.Context
import com.squareup.moshi.Moshi
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import xyz.lrhm.komakdast.core.data.model.Package
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalUtil @Inject constructor( val moshi: Moshi,@ApplicationContext val context: Context )  {

    fun parseLocalJson(){

        try {
            val inputStream= context.assets.open("local.json")
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            val string = String(buffer)

            val converter = moshi.adapter(LocalList::class.java)
            Timber.d("string is $string")
            val objects = converter.fromJson(string)

            Timber.d("object is $objects")
            

        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    data class LocalList constructor(
        val objects: List<Package>
    )


}