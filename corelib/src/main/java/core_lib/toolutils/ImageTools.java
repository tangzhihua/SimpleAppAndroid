package core_lib.toolutils;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import core_lib.global_data_cache.ApplicationSingleton;

/**
 * Created by tangzhihua on 15/9/9.
 */
public final class ImageTools {
    private ImageTools() {
        throw new AssertionError("这个是一个工具类, 不能创建实例对象.");
    }

    private final static String TAG = ImageTools.class.getSimpleName();

    public interface Constants {

        int IMAGE_CONNECT_TIMEOUT = 20000;
        int IMAGE_READ_TIMEOUT = 20000;
        int IMAGE_MAX_PIXELS_NONE = -1;

        String SCHEME_FILE = "file";
        String SCHEME_HTTP = "http";
        String SCHEME_HTTPS = "https";
        String SCHEME_ASSETS = "assets";
        String SCHEME_RESOURCE = "resource";
        String SCHEME_RESOURCE_FULL = "resource://";
        String SCHEME_THUMBNAILS = "thumbnails";
        String SCHEME_THUMBNAILS_FULL = "thumbnails://";
    }

    /**
     * 读取图片属性：旋转的角度
     * add by Daisw
     *
     * @param srcPath 图片绝对路径
     * @return degree 旋转的角度
     */
    public static int getImageDegree(String srcPath) {

        int degree = 0;

        try {

            ExifInterface exifInterface = new ExifInterface(srcPath);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (Throwable t) {

            DebugLog.e(TAG, t.getLocalizedMessage());
        }

        return degree;
    }

    /*
     * 缩放图片，保持图片的宽高比，最长边 = maxSideLength
     * add by Daisw
     *
     * @param bitmap        源
     * @param maxSideLength 长轴的长度，单位px
     * @param filePath      图片的路径，用来检验图片是否被旋转
     * @return
     */
    public static Bitmap getScaleImageByDegree(Bitmap bitmap, int maxSideLength, String filePath) {

        if (bitmap == null)
            return null;

        try {

            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

            DebugLog.e(TAG, "getImage original ## width: " + width + " # height: " + height);

            Matrix matrix = new Matrix();
            if (width > height) {

                float rate = (float) maxSideLength / width;
                matrix.postScale(rate, rate);
            } else {

                float rate = (float) maxSideLength / height;
                matrix.postScale(rate, rate);
            }

            if (filePath != null)
                matrix.postRotate(getImageDegree(filePath));

            Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

            DebugLog.e(TAG, "getImage changed ## width: " + newBitmap.getWidth() + " # height: " + newBitmap.getHeight());

            return newBitmap;

        } catch (Throwable t) {

            DebugLog.e(TAG, t.getLocalizedMessage());
        }

        return null;
    }

    /**
     * 根据最长边压缩图片（压缩出来的图片最长边一定是>=maxSideLength）
     * add by Daisw
     *
     * @param srcPath       图片的路径
     * @param maxSideLength 长轴的长度，单位px
     * @return
     */
    public static Bitmap getSampleSizeImage(String srcPath, int maxSideLength) {

        return TextUtils.isEmpty(srcPath) ? null : getSampleSizeImage(Uri.parse(srcPath), maxSideLength);
    }

    public static Bitmap getSampleSizeImage(File srcFile, int maxSideLength) {

        return srcFile == null ? null : getSampleSizeImage(Uri.fromFile(srcFile), maxSideLength);
    }

    public static Bitmap getSampleSizeImage(Uri uri, int maxSideLength) {

        if (uri == null)
            return null;

        try {

            BitmapFactory.Options newOpts = new BitmapFactory.Options();
            newOpts.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(openInputStream(uri), null, newOpts);
            int w = newOpts.outWidth;
            int h = newOpts.outHeight;

            DebugLog.e(TAG, "getImage original ## w: " + w + " # h: " + h + " # maxSize: " + maxSideLength);

            if (w <= maxSideLength && h <= maxSideLength) {
                newOpts.inJustDecodeBounds = false;
                return BitmapFactory.decodeStream(openInputStream(uri), null, newOpts);
            }

            int minSideLength = -1;
            if (w > h && w > maxSideLength) {// 横图
                w = maxSideLength;
                h = h * w / maxSideLength;
                minSideLength = h;
            } else if (w < h && h > maxSideLength) {// 竖图
                h = maxSideLength;
                w = w * h / maxSideLength;
                minSideLength = w;
            } else if (w == h && w > maxSideLength) {// 方图
                w = maxSideLength;
                h = maxSideLength;
                minSideLength = w;
            }
            int rate = computeSampleSize(newOpts, minSideLength, w * h);

            DebugLog.e(TAG, "getImage compress ## rate: " + rate);

            newOpts.inSampleSize = rate;
            newOpts.inJustDecodeBounds = false;

            Bitmap bitmap = BitmapFactory.decodeStream(openInputStream(uri), null, newOpts);

            DebugLog.e(TAG, "getImage changed ## width: " + bitmap.getWidth() + " # height: " + bitmap.getHeight());

            return bitmap;

        } catch (Throwable t) {


        }

        return null;
    }

    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {

        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {

        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == Constants.IMAGE_MAX_PIXELS_NONE) ? 1 : (int) Math.ceil(Math.sqrt(w * h
                / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength),
                Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }
        if ((maxNumOfPixels == Constants.IMAGE_MAX_PIXELS_NONE) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    public static InputStream openInputStream(Uri uri) {

        if (uri == null)
            return null;

        String scheme = uri.getScheme();
        InputStream stream = null;

        if (scheme == null || ContentResolver.SCHEME_FILE.equals(scheme)) {

            // from file
            stream = openFileInputStream(uri.getPath());

        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {

            // from content
            stream = openContentInputStream(uri);

        } else if (Constants.SCHEME_HTTP.equals(scheme) || Constants.SCHEME_HTTPS.equals(scheme)) {

            // from remote uri
            stream = openRemoteInputStream(uri);

        } else if (Constants.SCHEME_RESOURCE.equals(scheme)) {

            stream = openResourceStream(uri);

        } else if (Constants.SCHEME_ASSETS.equals(scheme)) {

            stream = openAssetsStream(uri);
        }

        return stream;
    }

    private static InputStream openFileInputStream(String path) {

        try {

            return new FileInputStream(path);

        } catch (Exception e) {


        }

        return null;
    }

    private static InputStream openContentInputStream(Uri uri) {

        try {


            return ApplicationSingleton.getInstance.getApplication().getContentResolver().openInputStream(uri);

        } catch (Exception e) {


        }

        return null;
    }

    public static class RemoteInputStream extends InputStream {

        private InputStream input;
        private int contentLength;

        public RemoteInputStream(InputStream input, int contentLength) {

            this.input = input;
            this.contentLength = contentLength;
        }

        @Override
        public int read() throws IOException {

            return input.read();
        }

        public int getContentLength() {

            return contentLength;
        }
    }

    public static InputStream openRemoteInputStream(Uri uri) {

        try {

            URLConnection conn = new URL(uri.toString()).openConnection();
            conn.setConnectTimeout(Constants.IMAGE_CONNECT_TIMEOUT);
            conn.setReadTimeout(Constants.IMAGE_READ_TIMEOUT);
            return new RemoteInputStream((InputStream) conn.getContent(), conn.getContentLength());

        } catch (Exception e) {


        }

        return null;
    }

    public static InputStream openResourceStream(Uri uri) {

        try {

            return ApplicationSingleton.getInstance.getApplication().getResources().openRawResource(Integer.parseInt(uri.getHost()));
        } catch (Exception e) {


        }

        return null;
    }

    public static InputStream openAssetsStream(Uri uri) {

        try {

            return ApplicationSingleton.getInstance.getApplication().getAssets().open(uri.getPath().substring(1));
        } catch (Exception e) {


        }

        return null;
    }

    /**
     * 压缩的图片，以jpeg方式压缩<br>
     * 需要注意的是如果压缩的背景有透明色就不能使用jpeg方法
     * add by Daisw
     *
     * @param image
     * @param quality Hint to the compressor, 0-100. 0 meaning compress for
     *                small size, 100 meaning compress for max quality. Some
     *                formats, like PNG which is lossless, will ignore the
     *                quality setting
     * @return
     */
    public static byte[] bitmapToByteArray(Bitmap image, int quality) {

        if (image == null)
            return null;

        try {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            return baos.toByteArray();
        } catch (Throwable t) {

        }

        return null;
    }
}
