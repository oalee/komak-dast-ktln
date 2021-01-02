package xyz.lrhm.komakdast.core.util.legacy

import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.widget.LinearLayout
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import java.io.InputStream
import java.util.regex.Pattern
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Tools @Inject constructor(
    @ApplicationContext val context: Context,
) {

    fun convertPixelsToDp(px: Float): Float {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return px / (metrics.densityDpi / 160f)
    }

    //
    //    public Bitmap updateHSV(Bitmap src, float settingHue, float settingSat, float settingVal) {
    //        int w = src.getWidth();
    //        int h = src.getHeight();
    //        int[] mapSrcColor = new int[w * h];
    //        int[] mapDestColor = new int[w * h];
    //
    //        float[] pixelHSV = new float[3];
    //
    //        src.getPixels(mapSrcColor, 0, w, 0, 0, w, h);
    //
    //        int index = 0;
    //        for (int y = 0; y < h; ++y) {
    //            for (int x = 0; x < w; ++x) {
    //
    //                // Convert from Color to HSV
    //                Color.colorToHSV(mapSrcColor[index], pixelHSV);
    //                int alpha = Color.alpha(mapSrcColor[index]);
    //
    //                // Adjust HSV
    //                pixelHSV[0] = pixelHSV[0] + settingHue;
    //                if (pixelHSV[0] < 0.0f) {
    //                    pixelHSV[0] += 360;
    //                } else if (pixelHSV[0] > 360.0f) {
    //                    pixelHSV[0] -= 360.0f;
    //                }
    //
    //                pixelHSV[1] = pixelHSV[1] + settingSat;
    //                if (pixelHSV[1] < 0.0f) {
    //                    pixelHSV[1] = 0.0f;
    //                } else if (pixelHSV[1] > 1.0f) {
    //                    pixelHSV[1] = 1.0f;
    //                }
    //
    //                pixelHSV[2] = pixelHSV[2] + settingVal;
    //                if (pixelHSV[2] < 0.0f) {
    //                    pixelHSV[2] = 0.0f;
    //                } else if (pixelHSV[2] > 1.0f) {
    //                    pixelHSV[2] = 1.0f;
    //                }
    //
    //                // Convert back from HSV to Color
    //                mapDestColor[index] = Color.HSVToColor(alpha, pixelHSV);
    //
    //                index++;
    //            }
    //        }
    //
    //        return Bitmap.createBitmap(mapDestColor, w, h, Bitmap.Config.ARGB_8888);
    //
    //    }
    fun resizeView(view: View, width: Int, height: Int) {
        val layoutParams = view.layoutParams
        if (width != layoutParams.width || height != layoutParams.height) {
            layoutParams.width = width
            layoutParams.height = height
        }
    }

    fun reverseLinearLayout(linearLayout: LinearLayout) {
        val views = arrayOfNulls<View>(linearLayout.childCount)
        for (i in views.indices) views[i] = linearLayout.getChildAt(i)
        linearLayout.removeAllViews()
        for (i in views.size - 1 downTo 1) linearLayout.addView(views[i])
    }

    fun setViewBackground(view: View, dialogDrawable: Drawable?) {
        if (Build.VERSION.SDK_INT >= 16) view.background = dialogDrawable else {
            view.setBackgroundDrawable(dialogDrawable)
        }
    }

    companion object {
        const val ENCRYPT_KEY = "shared_prefs_last_long"
        const val USER_SAVED_DATA = "shared_prefs_user"
        const val SHARED_PREFS_TOKEN = "shared_prefs_tk"
        const val SHARED_PREFS_SEED = "shared_prefs_seed_key"
        private const val TAG = "Tools"
        private const val test = true
        private const val testUrl = "http://192.168.1.33:3000/"
        private const val baseUrl = "https://komakdast2.com:2020/"
        val url: String
            get() = if (test) testUrl else baseUrl

        fun convertDPtoPixel(dp: Int, context: Context): Int {
            val displayMetrics = context.resources.displayMetrics
            return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
        }

        fun numeralStringToPersianDigits(s: String): String {
            val persianDigits = "۰۱۲۳۴۵۶۷۸۹"
            val result = CharArray(s.length)
            for (i in 0 until s.length) result[i] =
                if (Character.isDigit(s[i])) persianDigits[s[i] - '0'] else s[i]
            return String(result)
        }

        val VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)
        val VALID_PHONE = Pattern.compile("^09[0-9]{9}$")
        val VALID_PHONE_2 = Pattern.compile("^9[0-9]{9}$")
        val VALID_PHONE_3 = Pattern.compile("^989[0-9]{9}$")
        val VALID_PHONE_4 = Pattern.compile("^00989[0-9]{9}$")
        val VALID_NAME_PATTERN_REGEX = Pattern.compile("[a-zA-Z_0-9]+$")
        fun isNameValid(string: String?): Boolean {
            return VALID_NAME_PATTERN_REGEX.matcher(string).find()
        }

        fun isAEmail(emailStr: String?): Boolean {
            val matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr)
            return matcher.find()
        }

        fun isAPhoneNumber(number: String?): Boolean {
            val matcher = VALID_PHONE.matcher(number)
            return (matcher.find() || VALID_PHONE_2.matcher(number).find() || VALID_PHONE_3.matcher(
                number
            ).find()
                    || VALID_PHONE_3.matcher(number).find() || VALID_PHONE_4.matcher(number).find())
        }

        fun mySigCheck(context: Context): String {
            var sigChk = "Bsaq"
            var signature: Array<Signature?>? = arrayOfNulls(1)
            try {
                signature = context.packageManager.getPackageInfo(
                    context.packageName,
                    PackageManager.GET_SIGNATURES
                ).signatures

//            Log.d("Hash", Integer.toString(signature[0].hashCode())); //<< Prints your signature. Remove once you know this and have changed it below.
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

//        if (signature[0].hashCode() == -1769074008 || signature[0].hashCode() == 254943616) {
            sigChk = "TTy"
            //        }
            return sigChk
        }

        fun isAssetExists(pathInAssetsDir: String?, context: Context): Boolean {
            val assetManager = context.resources.assets
            var inputStream: InputStream? = null
            try {
                inputStream = assetManager.open(pathInAssetsDir!!)
                if (null != inputStream) {
                    inputStream.close()
                    return true
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return false
        }

        fun getAsset(path: String?, context: Context): InputStream? {
            val assetManager = context.resources.assets
            var inputStream: InputStream? = null
            try {
                inputStream = assetManager.open(path!!)
                return inputStream
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return null
        }
    }
}