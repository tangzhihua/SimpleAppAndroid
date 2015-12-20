package core_lib.global_data_cache;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import core_lib.toolutils.DebugLog;
import core_lib.toolutils.SimpleStorageUtilTools;
import core_lib.toolutils.ToolsFunctionForThisProgect;

import static com.google.common.collect.Lists.newArrayList;

/**
 * 本地缓存数据路径常量类
 *
 * @author zhihua.tang
 */
public final class LocalCacheDataPathConstantTools {
    private static final String TAG = "LocalCacheDataPathConstantTools";

    private LocalCacheDataPathConstantTools() {
        throw new AssertionError("这个是一个工具类, 不能创建实例对象.");
    }

    // app 本地缓存根目录(在SD卡上)
    public static File localCacheDirectoryRootPathInSDCard() {
        File pathFile = new File(Environment.getExternalStorageDirectory().getPath() + "/" + GlobalConstantTools.APP_NAME);
        return pathFile;
    }

    // area数据库在SD卡上面的缓存目录
    public static File localCacheAreaDatabaseRootPathInSDCard() {
        File pathFile = new File(localCacheDirectoryRootPathInSDCard() + "/" + "AreaDatabase");
        return pathFile;
    }

    // 项目中 "缩略图" 缓存目录 (在设备存储中, 可以被清除)
    public static File thumbnailCachePathInDevice() {
        File pathFile = new File(ApplicationSingleton.getInstance.getApplication().getCacheDir().getPath() + "/" + "ThumbnailCachePath");
        return pathFile;
    }

    // 用户数据的本地缓存根目录(这个目录下面, 会根据用户id, 来创建N个具体用户账号目录)
    public static File localCacheUserDataRootPathInSDCard() {
        File pathFile = new File(localCacheDirectoryRootPathInSDCard() + "/" + "UserData");
        return pathFile;
    }



    /* -------------------------------------------------------------------------------------------------- */

    // 返回能被用户清空的文件目录数组(可以从这里获取用户可以直接清空的文件夹路径数组)
    public static List<File> directoriesCanBeClearByTheUser() {
        List<File> list = new ArrayList<File>();
        File file = thumbnailCachePathInDevice();
        list.add(file);
        return list;
    }

    public static long getLocalCacheDataSizeInDevice() {
        long size = 0;
        for (File file : directoriesCanBeClearByTheUser()) {
            size += ToolsFunctionForThisProgect.getDirectorySize(file);
        }
        return size;
    }

    // 创建本地数据缓存目录(一次性全部创建, 不会重复创建)
    public static void createLocalCacheDirectories() {
        if (!SimpleStorageUtilTools.isExternalStoreWritable()) {
            DebugLog.e(TAG, "创建本地缓存目录失败, 因为外部存储不可写操作.");
            return;
        }
        List<File> directories = newArrayList();
        /* 根目录必须在首位 */
        directories.add(localCacheDirectoryRootPathInSDCard());
        // 以下是根目录下面的子目录
        directories.add(thumbnailCachePathInDevice());

        // 创建 "AreaDatabase" 在SD卡上面的缓存目录
        directories.add(localCacheAreaDatabaseRootPathInSDCard());

        // 创建 "用户数据缓存根目录"
        directories.add(localCacheUserDataRootPathInSDCard());

        for (File directoryPath : directories) {
            if (!directoryPath.exists()) {
                if (!directoryPath.mkdirs()) {
                    DebugLog.e(TAG, "创建重要的本地缓存目录失败, 目录路径是-->" + directoryPath.getPath());
                }
            }
        }
    }


}
