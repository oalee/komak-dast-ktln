package xyz.lrhm.komakdast.core.util.legacy

import android.app.ActivityManager
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.LruCache
import android.widget.ImageView
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageManager @Inject constructor(
    @ApplicationContext val context: Context,
    private val lengthManager: LengthManager

) {

    var memoryClass = 0
    var cache: LruCache<ImageKey, Bitmap> = initCache(context)
    
    private fun initCache(context: Context): LruCache<ImageKey, Bitmap> {

        val am = context
            .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        memoryClass = am.memoryClass
        val max =
            (memoryClass * 1024 * 1024 * if (memoryClass > 100) 0.80 else 0.666).toInt() // more than 50%
        return object : LruCache<ImageKey, Bitmap>(max) {
            override fun sizeOf(key: ImageKey, value: Bitmap): Int {
                return value.byteCount
            }
        }
    }

    @JvmOverloads
    fun loadImageFromResource(
        resourceId: Int,
        outWidth: Int,
        outHeight: Int,
        scalingLogic: ScalingLogic? = ScalingLogic.CROP,
        config: Bitmap.Config? = Bitmap.Config.ARGB_8888
    ): Bitmap {
        var outWidth = outWidth
        var outHeight = outHeight
        if (outWidth == -1) outWidth = lengthManager.getWidthWithFixedHeight(resourceId, outHeight)
        if (outHeight == -1) outHeight = lengthManager.getHeightWithFixedWidth(resourceId, outWidth)
        val key = ImageKey(resourceId, outWidth, outHeight)
        val him = cache!![key]
        if (him != null && !him.isRecycled) return him
        System.gc()
        val scaledBitmap: Bitmap
        val unscaledBitmap: Bitmap
        unscaledBitmap =
            decodeFile(resourceId, outWidth, outHeight, scalingLogic, context.resources)
        scaledBitmap = createScaledBitmap(unscaledBitmap, outWidth, outHeight, scalingLogic, config)
        if (!unscaledBitmap.isRecycled) unscaledBitmap.recycle()
        cache!!.put(key, scaledBitmap)
        return scaledBitmap
    }

    fun loadImageFromFilse(
        path: String?,
        outWidth: Int,
        outHeight: Int,
        scalingLogic: ScalingLogic?
    ): Bitmap {
        val key = ImageKey(path!!, outWidth, outHeight)
        val him = cache!![key]
        if (him != null && !him.isRecycled) return him
        System.gc()
        val scaledBitmap: Bitmap
        val unscaledBitmap: Bitmap
        unscaledBitmap = decodeFile(path, outWidth, outHeight, scalingLogic, context.resources)
        scaledBitmap = createScaledBitmap(
            unscaledBitmap,
            outWidth,
            outHeight,
            scalingLogic,
            Bitmap.Config.ARGB_8888
        )
        if (!unscaledBitmap.isRecycled) unscaledBitmap.recycle()
        cache!!.put(key, scaledBitmap)
        return scaledBitmap
    }

    fun loadImageFromResourceNoCache(
        resourceId: Int,
        outWidth: Int,
        outHeight: Int,
        scalingLogic: ScalingLogic?,
        config: Bitmap.Config?
    ): Bitmap {
        return loadImageFromResource(resourceId, outWidth, outHeight, scalingLogic, config)
    }

    fun loadImageFromResourceNoCache(
        resourceId: Int,
        outWidth: Int,
        outHeight: Int,
        scalingLogic: ScalingLogic?
    ): Bitmap {
        return loadImageFromResource(
            resourceId,
            outWidth,
            outHeight,
            scalingLogic,
            Bitmap.Config.ARGB_8888
        )
    }

    fun loadImageFromResource(
        resourceId: Int,
        outWidth: Int,
        outHeight: Int,
        config: Bitmap.Config?
    ): Bitmap {
        return loadImageFromResource(resourceId, outWidth, outHeight, ScalingLogic.CROP, config)
    }

    fun loadImageFromInputStream(inputStream: InputStream?, outWidth: Int, outHeight: Int): Bitmap {
        var outWidth = outWidth
        var outHeight = outHeight
        System.gc()
        val scaledBitmap: Bitmap
        val unscaledBitmap: Bitmap?
        checkNotNull(inputStream) { "null InputStream!" }
        unscaledBitmap = decodeInputStream(inputStream)
        if (outHeight == -1) outHeight = unscaledBitmap!!.height * outWidth / unscaledBitmap.width
        if (outWidth == -1) outWidth = unscaledBitmap!!.width * outHeight / unscaledBitmap.height
        scaledBitmap = createScaledBitmap(
            unscaledBitmap,
            outWidth,
            outHeight,
            ScalingLogic.CROP,
            Bitmap.Config.ARGB_8888
        )
        if (!unscaledBitmap!!.isRecycled) unscaledBitmap.recycle()
        return scaledBitmap
    }

    enum class ScalingLogic {
        FIT, CROP, ALL_TOP
    }

    fun calculateSrcRect(
        srcWidth: Int,
        srcHeight: Int,
        dstWidth: Int,
        dstHeight: Int,
        scalingLogic: ScalingLogic?
    ): Rect? {
        return when (scalingLogic) {
            ScalingLogic.CROP -> {
                val srcAspect = srcWidth.toFloat() / srcHeight.toFloat()
                val dstAspect = dstWidth.toFloat() / dstHeight.toFloat()
                if (srcAspect > dstAspect) {
                    val srcRectWidth = (srcHeight * dstAspect).toInt()
                    val srcRectLeft = (srcWidth - srcRectWidth) / 2
                    Rect(srcRectLeft, 0, srcRectLeft + srcRectWidth, srcHeight)
                } else {
                    val srcRectHeight = (srcWidth / dstAspect).toInt()
                    val scrRectTop = (srcHeight - srcRectHeight) / 2
                    Rect(0, scrRectTop, srcWidth, scrRectTop + srcRectHeight)
                }
            }
            ScalingLogic.FIT -> Rect(0, 0, srcWidth, srcHeight)
            ScalingLogic.ALL_TOP -> Rect(
                0,
                0,
                srcWidth,
                Math.min(srcHeight, dstHeight * srcWidth / dstWidth)
            )
            else -> null
        }
    }

    fun calculateDstRect(
        srcWidth: Int,
        srcHeight: Int,
        dstWidth: Int,
        dstHeight: Int,
        scalingLogic: ScalingLogic?
    ): Rect? {
        return when (scalingLogic) {
            ScalingLogic.FIT -> {
                val srcAspect = srcWidth.toFloat() / srcHeight.toFloat()
                val dstAspect = dstWidth.toFloat() / dstHeight.toFloat()
                if (srcAspect > dstAspect) Rect(
                    0,
                    0,
                    dstWidth,
                    (dstWidth / srcAspect).toInt()
                ) else Rect(0, 0, (dstHeight * srcAspect).toInt(), dstHeight)
            }
            ScalingLogic.CROP -> Rect(0, 0, dstWidth, dstHeight)
            ScalingLogic.ALL_TOP -> Rect(
                0,
                0,
                dstWidth,
                Math.min(dstHeight, srcHeight * dstWidth / srcWidth)
            )
            else -> null
        }
    }

    fun createScaledBitmap(
        unscaledBitmap: Bitmap?,
        dstWidth: Int,
        dstHeight: Int,
        scalingLogic: ScalingLogic?,
        config: Bitmap.Config?
    ): Bitmap {
        val srcRect = calculateSrcRect(
            unscaledBitmap!!.width,
            unscaledBitmap.height,
            dstWidth,
            dstHeight,
            scalingLogic
        )
        val dstRect = calculateDstRect(
            unscaledBitmap.width,
            unscaledBitmap.height,
            dstWidth,
            dstHeight,
            scalingLogic
        )
        val scaledBitmap = Bitmap.createBitmap(dstRect!!.width(), dstRect.height(), config!!)
        val canvas = Canvas(scaledBitmap)
        canvas.drawBitmap(unscaledBitmap, srcRect, dstRect, Paint(Paint.FILTER_BITMAP_FLAG))
        return scaledBitmap
    }

    fun decodeFile(
        resourceId: Int,
        dstWidth: Int,
        dstHeight: Int,
        scalingLogic: ScalingLogic?,
        resources: Resources?
    ): Bitmap {
        val options = BitmapFactory.Options()
        options.inScaled = false
        return BitmapFactory.decodeResource(resources, resourceId, options)
    }

    fun decodeFile(
        file: String?,
        dstWidth: Int,
        dstHeight: Int,
        scalingLogic: ScalingLogic?,
        resources: Resources?
    ): Bitmap {
        val options = BitmapFactory.Options()
        options.inScaled = false
        return BitmapFactory.decodeFile(file, options)
    }

    fun decodeInputStream(inputStream: InputStream?): Bitmap? {
        val options = BitmapFactory.Options()
        options.inScaled = false
        return BitmapFactory.decodeStream(inputStream, null, options)
    }

    fun toGrayscale(imageView: ImageView) {
        val matrix = ColorMatrix()
        matrix.setSaturation(0f)
        val filter = ColorMatrixColorFilter(matrix)
        imageView.colorFilter = filter
    }

    fun toNormalscale(imageView: ImageView) {
        val matrix = ColorMatrix()
        val filter = ColorMatrixColorFilter(matrix)
        imageView.colorFilter = filter
    }


}