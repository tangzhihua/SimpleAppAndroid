package core_lib.toolutils;

import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * Created by tangzhihua on 15/10/23.
 */
public final class SimpleStorageUtilTools {
    private SimpleStorageUtilTools() {
        throw new AssertionError("这个是一个工具类, 不能创建实例对象.");
    }

    /**
     * 外部存储是否可读
     *
     * @return 如果可用返回true，否则返回false
     */
    public static boolean isExternalStoreReadable() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED) ||
                state.equals(Environment.MEDIA_MOUNTED_READ_ONLY) ||
                state.equals(Environment.MEDIA_SHARED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 外部存储是否可写
     *
     * @return 如果可以写则返回true，否则返回false
     */
    public static boolean isExternalStoreWritable() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED) ||
                state.equals(Environment.MEDIA_SHARED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取SD卡路径
     *
     * @return
     */
    public static File getSDCardDir() {
        if (!isExternalStoreReadable()) {
            return null;
        }
        return Environment.getExternalStorageDirectory();
    }

    /**
     * 获取设备本身存储空间总大小
     *
     * @return
     */
    static public long getDeviceStorageTotalSize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = 0;
        if (Build.VERSION.SDK_INT == 18) {
            blockSize = stat.getBlockSizeLong();
        } else {
            blockSize = stat.getBlockSize();
        }
        long totalBlocks = 0;
        if (Build.VERSION.SDK_INT == 18) {
            totalBlocks = stat.getBlockCountLong();
        } else {
            totalBlocks = stat.getBlockCount();
        }
        return totalBlocks * blockSize;
    }

    /**
     * 获取设备本身存储空闲空间大小
     *
     * @return
     */
    public static long getDeviceStorageFreeSize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = 0;
        if (Build.VERSION.SDK_INT == 18) {
            blockSize = stat.getBlockSizeLong();
        } else {
            blockSize = stat.getBlockSize();
        }
        long availableBlocks = 0;
        if (Build.VERSION.SDK_INT == 18) {
            availableBlocks = stat.getAvailableBlocksLong();
        } else {
            availableBlocks = stat.getAvailableBlocks();
        }
        return availableBlocks * blockSize;
    }

    /**
     * 获取SD卡的存储空间总大小
     *
     * @return
     */
    public static long getSDCardTotalSize() {
        if (isExternalStoreWritable()) {
            StatFs stat = new StatFs(getSDCardDir().getPath());
            long blockSize = 0;
            if (Build.VERSION.SDK_INT == 18) {
                blockSize = stat.getBlockSizeLong();
            } else {
                blockSize = stat.getBlockSize();
            }
            long totalBlocks = 0;
            if (Build.VERSION.SDK_INT == 18) {
                totalBlocks = stat.getBlockCountLong();
            } else {
                totalBlocks = stat.getBlockCount();
            }
            return totalBlocks * blockSize;
        }
        return 0;
    }

    /**
     * 获取SD卡的空闲存储空间大小
     *
     * @return
     */
    public static long getSDCardFreeSize() {
        if (isExternalStoreWritable()) {
            StatFs stat = new StatFs(getSDCardDir().getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = 0;
            if (Build.VERSION.SDK_INT == 18) {
                availableBlocks = stat.getAvailableBlocksLong();
            } else {
                availableBlocks = stat.getAvailableBlocks();
            }
            return availableBlocks * blockSize;
        }
        return 0;
    }


}


/*

android设备的外存储，不仅仅指SD卡，当然最常见的就是SD卡，但现在很多手机自带的空间就比较大比如8G或16G或32G，这些空间，android设备在识别的时候也把它们当作是外部存储,这些外部存储有下面这些状态:

MEDIA_BAD_REMOVAL
如果外部存储没有被移出就把直接拔掉后的状态(就像我们在pc上面使用u盘一样，我们没有安全移出u盘，就把u盘拔掉)
------- 表明SDCard 被卸载前己被移除

MEDIA_CHECKING
当外存储刚被连接到手机上，手机要对外存储进行检测，还有就是手机开机后也要对外存储设置进行检测，在这检测过程中就是这一状态。
------- 表明对象正在磁盘检查

MEDIA_MOUNTED
当外存储是可以读，也可以写的时候，也就是外存储正常的时候大多数的状态。如果有的外存储有写保护并打开了，那么这个外存储设置连接后，就只可以被读了，就不是这个状态了。
------- 表明对象是否存在并具有读/写权限

MEDIA_MOUNTED_READ_ONLY
如果外存储连接到手机上，并且这个外存储已经打开发写保护开关，那么就是这个状态。
------- 表明对象权限为只读

MEDIA_NOFS
当外存储坏了使存储空间为0（常见的我们把u盘插入pc上时显示0字节空间）或者外存储所用的文件系统格式系统不认识那么就是这个状态
------- 表明对象为空白或正在使用不受支持的文件系统。

REMOVED
如果外存储不存在则是这个状态
------- 如果不存在 SDCard

MEDIA_SHARED
这个在手机上面比较少见，但在pc上很常见，比如在pc上面，我们把手机连接到电脑的时候，有的手机就会显示一个选择，是充电还是存储，这个存储就是分享受手机当中sd卡的存储，这个时候pc既有读的权限也有写的权限，还有手机本身的存储空间，还有照像机等连接电脑后等。
------- 如果 SDCard 未安装 ，并通过 USB 大容量存储共享

MEDIA_UNMOUNTABLE
这个是手机上面的外存储存在，但系统不能挂载它，常见的就是手机能够识这个外存储，并且也能够识别外存储的文件系统，但外存储的文件系统出现问题了。
------- 返回 SDCard 不可被安装 如果 SDCard 是存在但不可以被安装

MEDIA_UNMOUNT
当手机的外存储存在，但没有挂载就是这个状态（就像我们把u盘从pc上安全移除后，如果我们不把u盘从pc上拔下来，就是这个状态）
------- 返回 SDCard 已卸掉如果 SDCard   是存在但是没有被安装





+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
Environment 常用方法：
方法：getDataDirectory()
解释：返回 File ，获取 Android 数据目录。
方法：getDownloadCacheDirectory()
解释：返回 File ，获取 Android 下载/缓存内容目录。
方法：getExternalStorageDirectory()
解释：返回 File ，获取外部存储目录即 SDCard
方法：getExternalStoragePublicDirectory(String type)
解释：返回 File ，取一个高端的公用的外部存储器目录来摆放某些类型的文件
方法：getExternalStorageState()
解释：返回 File ，获取外部存储设备的当前状态
方法：getRootDirectory()
解释：返回 File ，获取 Android 的根目录


+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
讲述 StatFs 类
StatFs 一个模拟linux的df命令的一个类,获得SD卡和手机内存的使用情况
StatFs 常用方法:
getAvailableBlocks()
解释：返回 Int ，获取当前可用的存储空间
getBlockCount()
解释：返回 Int ，获取该区域可用的文件系统数
getBlockSize()
解释：返回 Int ，大小，以字节为单位，一个文件系统
getFreeBlocks()
解释：返回 Int ，该块区域剩余的空间
restat(String path)
解释：执行一个由该对象所引用的文件系统

*/