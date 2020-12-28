package xyz.lrhm.komakdast.core.util.legacy;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools {
    private Context context;
    private LengthManager lengthManager;
    private ImageManager imageManager;
    public final static String ENCRYPT_KEY = "shared_prefs_last_long";
    public final static String USER_SAVED_DATA = "shared_prefs_user";
    public final static String SHARED_PREFS_TOKEN = "shared_prefs_tk";
    public final static String SHARED_PREFS_SEED = "shared_prefs_seed_key";
    private final static String TAG = "Tools";


    private static boolean test = true;


    private static final String testUrl = "http://192.168.1.33:3000/";
    private static final String baseUrl = "https://komakdast2.com:2020/";

    public static String getUrl() {
        if (test)
            return testUrl;
        return baseUrl;
    }


    public Tools(Context context) {
        this.context = context;
    }

    public String decodeBase64(String string) {
        byte[] data = Base64.decode(string, Base64.DEFAULT);
        String solution = "";

        try {
            solution = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return solution;

    }

    public float convertPixelsToDp(float px) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return px / (metrics.densityDpi / 160f);
    }

    public static int convertDPtoPixel(int dp, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
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

    public void resizeView(View view, int width, int height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (width != layoutParams.width || height != layoutParams.height) {
            layoutParams.width = width;
            layoutParams.height = height;
        }
    }

    public void reverseLinearLayout(LinearLayout linearLayout) {
        View views[] = new View[linearLayout.getChildCount()];
        for (int i = 0; i < views.length; i++)
            views[i] = linearLayout.getChildAt(i);
        linearLayout.removeAllViews();
        for (int i = views.length - 1; i > 0; i--)
            linearLayout.addView(views[i]);
    }

    public static String numeralStringToPersianDigits(String s) {
        String persianDigits = "۰۱۲۳۴۵۶۷۸۹";
        char[] result = new char[s.length()];
        for (int i = 0; i < s.length(); i++)
            result[i] = Character.isDigit(s.charAt(i)) ? persianDigits.charAt(s.charAt(i) - '0') : s.charAt(i);
        return new String(result);
    }

    public void setViewBackground(final View view, Drawable dialogDrawable) {
        if (Build.VERSION.SDK_INT >= 16)
            view.setBackground(dialogDrawable);
        else {
            view.setBackgroundDrawable(dialogDrawable);
        }
    }


    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static final Pattern VALID_PHONE =
            Pattern.compile("^09[0-9]{9}$");
    public static final Pattern VALID_PHONE_2 =
            Pattern.compile("^9[0-9]{9}$");
    public static final Pattern VALID_PHONE_3 =
            Pattern.compile("^989[0-9]{9}$");
    public static final Pattern VALID_PHONE_4 =
            Pattern.compile("^00989[0-9]{9}$");

    public static final Pattern VALID_NAME_PATTERN_REGEX = Pattern.compile("[a-zA-Z_0-9]+$");

    public static boolean isNameValid(String string) {
        return VALID_NAME_PATTERN_REGEX.matcher(string).find();
    }

    public static boolean isAEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    public static boolean isAPhoneNumber(String number) {
        Matcher matcher = VALID_PHONE.matcher(number);
        return matcher.find() || VALID_PHONE_2.matcher(number).find() || VALID_PHONE_3.matcher(number).find()
                || VALID_PHONE_3.matcher(number).find() || VALID_PHONE_4.matcher(number).find();
    }


    public static String mySigCheck(Context context) {
        String sigChk = "Bsaq";

        Signature[] signature = new Signature[1];

        try {
            signature = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES).signatures;

//            Log.d("Hash", Integer.toString(signature[0].hashCode())); //<< Prints your signature. Remove once you know this and have changed it below.

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

//        if (signature[0].hashCode() == -1769074008 || signature[0].hashCode() == 254943616) {

        sigChk = "TTy";
//        }

        return sigChk;
    }

    public static boolean isAssetExists(String pathInAssetsDir, Context context) {
        AssetManager assetManager = context.getResources().getAssets();
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open(pathInAssetsDir);
            if (null != inputStream) {
                inputStream.close();
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static InputStream getAsset(String path, Context context) {
        AssetManager assetManager = context.getResources().getAssets();
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open(path);
            return inputStream;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
