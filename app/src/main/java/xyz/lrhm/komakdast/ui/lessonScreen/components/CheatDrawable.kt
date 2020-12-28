package xyz.lrhm.komakdast.ui.lessonScreen.components

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import xyz.lrhm.komakdast.R
import xyz.lrhm.komakdast.core.util.legacy.ImageManager
import xyz.lrhm.komakdast.core.util.legacy.ImageManager.Companion.getInstance
import xyz.lrhm.komakdast.core.util.legacy.LengthManager

class CheatDrawable(
    context: Context,
    index: Int,
    private val title: String,
    private val price: String
) : Drawable() {
    private var background: Bitmap
    private val rotated: Boolean
    private val textPaint: Paint
    private val context: Context
    private val titlePosition = IntArray(2)
    private val pricePosition = IntArray(2)
    private val paint: Paint
    private val imageManager: ImageManager?
    private val lengthManager: LengthManager
    private fun centerTextAt(text: String, x: Int, y: Int, paint: Paint, textPosition: IntArray) {
        val rectText = Rect()
        paint.getTextBounds(text, 0, text.length, rectText)
        textPosition[0] = (if (rotated) background.width - x else x) - rectText.width() / 2
        textPosition[1] = y + rectText.height() / 2
    }

    override fun draw(canvas: Canvas) {
        canvas.drawBitmap(background, 0f, 0f, paint)
        canvas.drawText(title, titlePosition[0].toFloat(), titlePosition[1].toFloat(), textPaint)
        canvas.drawText(price, pricePosition[0].toFloat(), pricePosition[1].toFloat(), textPaint)
    }

    override fun setAlpha(i: Int) {}
    override fun setColorFilter(colorFilter: ColorFilter?) {}
    override fun getOpacity(): Int {
        return PixelFormat.UNKNOWN
    }

    fun flip(src: Bitmap): Bitmap {
        val matrix = Matrix()
        matrix.preScale(-1.0f, 1.0f)
        return Bitmap.createBitmap(src, 0, 0, src.width, src.height, matrix, true)
    }

    init {
        rotated = index == 1
        this.context = context
        imageManager = getInstance(context)
        lengthManager = LengthManager(context)
        background = imageManager!!.loadImageFromResource(
            R.drawable.cheat_right,
            lengthManager.getCheatButtonWidth(),
            -1
        )
        paint = Paint()
        if (rotated) {
            background = flip(background)
        }
        textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        textPaint.textSize = lengthManager.getCheatButtonFontSize()
        textPaint.typeface = ResourcesCompat.getFont(context, R.font.sans_bold_num)
        textPaint.color = Color.WHITE
        textPaint.setShadowLayer(1f, 1f, 1f, Color.BLACK)
        centerTextAt(
            title,
            (0.375 * background.width).toInt(),
            (background.height * 0.420).toInt(),
            textPaint,
            titlePosition
        )
        centerTextAt(
            price,
            (0.863 * background.width).toInt(),
            (background.height * 0.480).toInt(),
            textPaint,
            pricePosition
        )
    }
}