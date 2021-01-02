package xyz.lrhm.komakdast.core.util.legacy

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Pair
import dagger.hilt.android.qualifiers.ApplicationContext
import xyz.lrhm.komakdast.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LengthManager @Inject constructor(@ApplicationContext val context: Context) {
    //    private static boolean initialized = false;

    val screenHeight = context.resources.displayMetrics.heightPixels


    val screenWidth = context.resources.displayMetrics.widthPixels


    fun getResourceDimensions(resourceId: Int): Pair<Int, Int> {
        val resources = context.resources
        val bounds = BitmapFactory.Options()
        bounds.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, resourceId, bounds)
        return Pair(bounds.outWidth, bounds.outHeight)
    }

    fun getHeightWithFixedWidth(resourceId: Int, width: Int): Int {
        val dimensions = getResourceDimensions(resourceId)
        return dimensions.second * width / dimensions.first
    }

    fun getWidthWithFixedHeight(resourceId: Int, height: Int): Int {
        val dimensions = getResourceDimensions(resourceId)
        return dimensions.first * height / dimensions.second
    }

    val headerHeight: Int
        get() = (screenHeight * 0.12f).toInt()

    //    public int getTabsHeight() {
//
//        return getHeightWithFixedWidth(R.drawable.tabbar_background, getScreenWidth());
//    }
//
//    public int getTabBarShadeHeight() {
//        return getHeightWithFixedWidth(R.drawable.shadow_top, getScreenWidth());
//    }
//    public int getTabBarHeight() {
//        return getTabsHeight() + getTabBarShadeHeight();
//    }
    val pageLevelCount: Int
        get() = getPageRowCount() * getPageColumnCount()

    fun getPageColumnCount(): Int {
        return 4
    }

    fun getPageRowCount(): Int {
        return 4
    }

    fun getAlphabetButtonSize(): Int {
        return screenWidth / 8
    }

    fun getAlphabetFontSize(): Float {
        return (screenWidth / 14).toFloat()
    }

    fun getSolutionButtonSize(): Int {
        return screenWidth / 12
    }

    fun getSolutionFontSize(): Float {
        return (screenWidth / 24).toFloat()
    }

    fun getFragmentHeight(): Int {
        return screenHeight - headerHeight
    }

    fun getLevelsBackTopHeight(): Int {
        return getHeightWithFixedWidth(R.drawable.top_seperator, screenWidth)
    }

    fun getLevelsBackBottomHeight(): Int {
        return getHeightWithFixedWidth(R.drawable.bottom_seperator, screenWidth)
    }

    fun getLevelsViewpagerHeight(): Int {
        return getPageRowCount() * getLevelFrameHeight() + 2 * getLevelsGridViewTopAndBottomPadding()
    }

    fun getLevelsGridViewLeftRightPadding(): Int {
        return screenWidth / 20
    }

    fun getLevelFrameWidth(): Int {
        return (screenWidth - 2 * getLevelsGridViewLeftRightPadding()) / 4
    }

    fun getLevelFrameHeight(): Int {
        return (screenWidth * 0.235).toInt()
        //getHeightWithFixedWidth(R.drawable.level_unlocked, getLevelFrameWidth());
    }

    fun getLevelImageFrameWidth(): Int {
        return screenWidth * 91 / 100
    }

    fun getLevelImageFrameHeight(): Int {
        return (getLevelImageFrameWidth() * (3.05 / 4f)).toInt()
    }

    fun getLevelThumbnailPadding(): Int {
        return getLevelFrameWidth() * 10 / 100
    }

    fun getLevelsGridViewTopAndBottomPadding(): Int {
        return 0
    }

    fun getLevelImageWidth(): Int {
        return getLevelImageFrameWidth() * 927 / 997
    }

    fun getLevelImageHeight(): Int {
        return getLevelImageFrameHeight() * 642 / 704
    }

    fun getIndicatorBigSize(): Int {
        return screenWidth / 15
    }

    fun getIndicatorSmallSize(): Int {
        return screenWidth / 30
    }

    fun getStoreDialogMargin(): Int {
        return screenWidth / 20
    }

    fun getStoreDialogPadding(): Int {
        return screenWidth / 15
    }

    fun getStoreItemsInnerWidth(): Int {
        return screenWidth - 2 * (getStoreDialogMargin() + getStoreDialogPadding())
    }

    fun getStoreItemFontSize(): Float {
        return (screenWidth * 60 / 1000).toFloat()
    }

    fun getStoreItemWidth(): Int {
        return screenWidth * 70 / 100
    }

    fun getCheatButtonWidth(): Int {
        return getLevelImageWidth()
    }

    fun getCheatButtonHeight(): Int {
        return getHeightWithFixedWidth(R.drawable.cheat_right, getCheatButtonWidth())
    }

    fun getCheatButtonFontSize(): Float {
        return (screenWidth / 15).toFloat()
    }

    fun getShopTitleWidth(): Int {
        return screenWidth / 2
    }

    fun getToastWidth(): Int {
        return screenWidth * 90 / 100
    }

    fun getToastFontSize(): Float {
        return screenWidth / 15f
    }

    fun getCheatButtonSize(): Int {
        return screenWidth / 7
    }

    fun getVideoPlayButtonSize(): Int {
        return getLevelImageWidth() / 2
    }

    fun getShopTitleBottomMargin(): Int {
        return getShopTitleWidth() / 8
    }

    fun getTickViewSize(): Int {
        return screenWidth / 3
    }

    fun getLevelFinishedButtonsSize(): Int {
        return screenWidth / 6
    }

    fun getLevelSolutionTextSize(): Float {
        return screenWidth / 15f
    }

    fun getLevelAuthorTextSize(): Float {
        return screenWidth / 18f
    }

    fun getLevelFinishedDialogPadding(): Int {
        return screenWidth / 10
    }

    fun getOneFingerDragWidth(): Int {
        return getLevelImageFrameWidth() / 4
    }

    fun getResourceLockWidth(): Int {
        return getLevelImageFrameWidth() / 3
    }

    fun getToastPadding(): Int {
        return getToastWidth() / 17
    }

    fun getPackagePurchaseTitleSize(): Float {
        return screenWidth / 13f
    }

    fun getPackagePurchaseDescriptionSize(): Float {
        return screenWidth / 19f
    }

    fun getPackagePurchaseItemTextSize(): Float {
        return screenWidth / 22f
    }

    fun getPackagePurchaseItemWidth(): Int {
        return screenWidth * 60 / 100
    }

    fun getPackagePurchaseItemHeight(): Int {
        return getPackagePurchaseItemWidth() * 504 / 1809
    }

    fun getLoadingEhemWidth(): Int {
        return screenWidth / 3
    }

    fun getLoadingToiletWidth(): Int {
        return screenWidth / 11
    }

    fun getLoadingEhemPadding(): Int {
        return screenWidth / 11
    }

    //    public int getPackagesListColumnCount() {
//        return Math.min(Math.max((int) (tools.convertPixelsToDp(getScreenWidth()) / 150), 1), 3);
//    }
    fun getLevelFinishedDialogTopPadding(): Int {
        return screenWidth / 30
    }

    fun getPrizeBoxSize(): Int {
        return screenWidth / 5
    }

    fun getTipTextSize(): Float {
        return screenWidth / 12f
    }

    fun getTipWidth(): Int {
        return screenWidth * 8 / 10
    }

    fun getPlayStopButtonFontSize(): Float {
        return (screenWidth / 3).toFloat()
    }

    fun getAdViewPagerHeight(): Int {
        return screenWidth * 579 / 1248
    }

    //    public int getPackageIconSize() {
//        return getScreenWidth() / getPackagesListColumnCount();
//    }
    fun getPlaceHolderTextSize(): Float {
        return screenWidth / 14f
    }
    
}