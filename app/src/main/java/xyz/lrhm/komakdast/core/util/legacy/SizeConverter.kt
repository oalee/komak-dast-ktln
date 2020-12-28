package xyz.lrhm.komakdast.core.util.legacy

import xyz.lrhm.komakdast.core.util.SizeManager

class SizeConverter(var sizeManager: SizeManager) {
    var height = 0
    var width = 0
    private var baseWidth = 0
    private var baseHeight = 0
    private var style = 0
    fun convertWidth(amount: Int): Int {
        return (amount / baseWidth.toFloat() * width).toInt()
    }

    fun convertHeight(amount: Int): Int {
        return (amount / baseHeight.toFloat() * height).toInt()
    }

    fun convertHeightCalcOffset(amount: Int): Int {
        return (amount / baseHeight.toFloat() * (height + topOffset)).toInt()
    }

    val offset: Int
        get() = when (style) {
            1 -> sizeManager.deviceWidth - width
            2 -> sizeManager.deviceHeight - height
            else -> 0
        }
    val leftOffset: Int
        get() = sizeManager.deviceWidth - width
    val topOffset: Int
        get() = sizeManager.deviceHeight - height

    companion object {
        @JvmStatic
        fun SizeConvertorFormHeight(
            mHeight: Double,
            baseWidth: Int, baseHeight: Int,
            sizeManager: SizeManager
        ): SizeConverter {
            val convertor = SizeConverter(sizeManager)
            convertor.style = 1
            convertor.height = mHeight.toInt()
            convertor.baseHeight = baseHeight
            convertor.baseWidth = baseWidth
            convertor.width = (mHeight / baseHeight.toFloat() * baseWidth) as Int
            return convertor
        }

        @JvmStatic
        fun SizeConvertorFromWidth(
            mWidth: Float,
            baseWidth: Int, baseHeight: Int,
            sizeManager: SizeManager
        ): SizeConverter {
            val convertor = SizeConverter(sizeManager)
            convertor.style = 2
            convertor.width = mWidth.toInt()
            convertor.baseHeight = baseHeight
            convertor.baseWidth = baseWidth
            convertor.height = (mWidth / baseWidth.toFloat() * baseHeight).toInt()
            return convertor
        } //    public static SizeConverter SizeConverterFromLessOffset(float mWidth, float mHeight, int baseWidth, int baseHeight) {
        //        SizeConverter fromHeight = new SizeConverter();
        //        SizeConverter fromWidth = new SizeConverter();
        //
        //        if (fromWidth.getOffset() <= 0) {
        //            return fromWidth;
        //        }
        //        if (fromHeight.getOffset() <= 0) {
        //            return fromHeight;
        //        }
        //
        //        if (fromHeight.getOffset() < fromWidth.getOffset()) {
        //            return fromHeight;
        //        }
        //        return fromWidth;
        //
        //    }
    }
}