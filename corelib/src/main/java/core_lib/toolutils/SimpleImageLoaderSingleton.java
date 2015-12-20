package core_lib.toolutils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.Volley;

import core_lib.global_data_cache.ApplicationSingleton;
import core_lib.global_data_cache.GlobalDataCacheForMemorySingleton;

/**
 * 实验Volley的icon 图片加载机制
 *
 * @author zhihua.tang
 */
public enum SimpleImageLoaderSingleton {
    getInstance;

    private final RequestQueue responseQueue;

    // ImageLoader也可以用于加载网络上的图片，并且它的内部也是使用ImageRequest来实现的，
    // 不过ImageLoader明显要比ImageRequest更加高效，因为它不仅可以帮我们对图片进行缓存，还可以过滤掉重复的链接，避免重复发送请求。
    private final ImageLoader volleyImageLoader;

    public class BitmapCache implements ImageCache {

        private final LruCache<String, Bitmap> imageLruCache;

        public BitmapCache() {
            int maxSize = 10 * 1024 * 1024;
            imageLruCache = new LruCache<String, Bitmap>(maxSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    return bitmap.getRowBytes() * bitmap.getHeight();
                }
            };
        }

        @Override
        public Bitmap getBitmap(String url) {
            return imageLruCache.get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            imageLruCache.put(url, bitmap);
        }

    }

    // 用于进行Volley队列初始化的实例块
    {
        responseQueue = Volley.newRequestQueue(ApplicationSingleton.getInstance.getApplication());

        volleyImageLoader = new ImageLoader(responseQueue, new BitmapCache());

    }

    /**
     * ---------------------------> 下面是对外的接口 <---------------------------
     */
    /**
     * @param imageUrlString
     * @param imageView
     * @param defaultImageResId
     * @param maxWidth          参数分别用于指定允许图片最大的宽度和高度，如果指定的网络图片的宽度或高度大于这里的最大值，则会对图片进行压缩，
     *                          指定成0的话就表示不管图片有多大，都不会进行压缩
     * @param maxHeight
     * @param decodeConfig      参数用于指定图片的颜色属性，Bitmap.Config下的几个常量都可以在这里使用，其中ARGB_8888可以展示最好的颜色属性，
     *                          每个图片像素占据4个字节的大小，而RGB_565则表示每个图片像素占据2个字节大小
     */
    public void displayImage(final String imageUrlString, final ImageView imageView,
                             final int defaultImageResId, final int errorImageResId, final int maxWidth,
                             final int maxHeight, final Config decodeConfig) {

        ImageListener listener = ImageLoader.getImageListener(imageView, defaultImageResId,
                errorImageResId);
        // ImageLoader的get方法, 内部默认设置 Config.RGB_565
        volleyImageLoader.get(imageUrlString, listener, maxWidth, maxHeight);
    }

    public void displayImage(final String imageUrlString, final ImageView imageView,
                             final int defaultImageResID, final int errorImageResId) {
        displayImage(imageUrlString, imageView, defaultImageResID, errorImageResId, 0, 0,
                Config.RGB_565);
    }
}
