package xyz.lrhm.komakdast.core.util.legacy;

import xyz.lrhm.komakdast.core.util.SizeManager;

public class SizeConverter {

    public int mHeight;
    public int mWidth;
    private int baseWidth;
    private int baseHeight;
    private int style;
    SizeManager sizeManager;

    public SizeConverter(SizeManager sizeManager) {
        this.sizeManager = sizeManager;
    }


    public int getHeight() {
        return mHeight;
    }

    public int getWidth() {
        return mWidth;
    }

    public int convertWidth(int amount) {
        return (int) ((amount / (float) baseWidth) * (mWidth));

    }

    public int convertHeight(int amount) {
        return (int) (((amount / (float) baseHeight) * mHeight));
    }

    public int convertHeightCalcOffset(int amount) {
        return (int) (((amount / (float) baseHeight) * (mHeight + getTopOffset())));

    }

    public int getOffset() {
        switch (style) {
            case 1:
                return sizeManager.getDeviceWidth() - mWidth;
            case 2:
                return sizeManager.getDeviceHeight() - mHeight;

            default:
                return 0;
        }
    }

    public int getLeftOffset() {
        return sizeManager.getDeviceWidth() - mWidth;
    }

    public int getTopOffset() {
        return sizeManager.getDeviceHeight() - mHeight;
    }

    public static SizeConverter SizeConvertorFormHeight(double mHeight,
                                                        int baseWidth, int baseHeight,
                                                        SizeManager sizeManager) {
        SizeConverter convertor = new SizeConverter(sizeManager);
        convertor.style = 1;
        convertor.mHeight = (int) mHeight;
        convertor.baseHeight = baseHeight;
        convertor.baseWidth = baseWidth;
        convertor.mWidth = (int) ((mHeight / (float) baseHeight) * baseWidth);
        return convertor;
    }

    public static SizeConverter SizeConvertorFromWidth(float mWidth,
                                                       int baseWidth, int baseHeight,
                                                       SizeManager sizeManager) {
        SizeConverter convertor = new SizeConverter(sizeManager);
        convertor.style = 2;

        convertor.mWidth = (int) mWidth;
        convertor.baseHeight = baseHeight;
        convertor.baseWidth = baseWidth;
        convertor.mHeight = (int) ((mWidth / (float) baseWidth) * baseHeight);
        return convertor;
    }

//    public static SizeConverter SizeConverterFromLessOffset(float mWidth, float mHeight, int baseWidth, int baseHeight) {
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
