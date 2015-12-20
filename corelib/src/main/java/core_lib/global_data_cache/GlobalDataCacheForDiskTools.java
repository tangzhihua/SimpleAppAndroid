package core_lib.global_data_cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import core_lib.domainbean_model.login.LoginNetRespondBean;
import core_lib.toolutils.DebugLog;

// 全局数据缓存 - 闪存级别缓存(会固化数据到闪存中)

/**
 * 这里序列化对象的保存目录是 : /data/data/<包名>/files/ , 这个目录会在用户在 "应用程序管理" 中点击 "清理数据" 按钮后被清理
 *
 * @author zhihua.tang
 */
public final class GlobalDataCacheForDiskTools {
    private final static String TAG = GlobalDataCacheForDiskTools.class.getSimpleName();

    private GlobalDataCacheForDiskTools() {
        throw new AssertionError("这个是一个工具类, 不能创建实例对象.");
    }

    /**
     * 1.0版本确认之后 这里定义的常量枚举, 千万不要任意修改, 否则升级apk时, 会导致之前存储的数据不能被识别
     *
     * @author zhihua.tang
     */
    private enum CacheDataNameEnum {

        // 用户是否是首次启动App
        FirstStartApp,
        // 当前app版本号, 用了防止升级app时, 本地缓存的序列化数据恢复出错.
        LocalAppVersion,


        // 用户最后一次登录成功时的 LoginNetRespondBean
        LatestLoginNetRespondBean,

    }

    ;

    private static final SharedPreferences sharedPreferences = ApplicationSingleton.getInstance.getApplication()
            .getSharedPreferences(GlobalConstantTools.APP_NAME, Context.MODE_PRIVATE);

    /**
     * 第一次启动APP的标志位
     *
     * @return true : 是第一次启动, false : 不是第一次启动
     */
    public static boolean isFirstStartApp() {
        //
        return sharedPreferences.getBoolean(CacheDataNameEnum.FirstStartApp.name(), true);
    }

    /**
     * 获取用户最后一次成功登录时的 LoginNetRespondBean
     *
     * @return
     */
    public static LoginNetRespondBean getLatestLoginNetRespondBean() {
        LoginNetRespondBean latestLoginNetRespondBean = deserializeObjectFromDevice(CacheDataNameEnum.LatestLoginNetRespondBean.name());
        return latestLoginNetRespondBean;
    }


    // ///////////////////////////////////////////////////////////////////////////
    // ///////////////////////////////////////////////////////////////////////////
    // ///////////////////////////////////////////////////////////////////////////
    // ///////////////////////////////////////////////////////////////////////////
    // ///////////////////////////////////////////////////////////////////////////
    // ///////////////////////////////////////////////////////////////////////////
    // ///////////////////////////////////////////////////////////////////////////

    public static void setFirstStartAppMark(boolean isFirstStartApp) {
        //
        Editor editor = sharedPreferences.edit();
        //
        editor.putBoolean(CacheDataNameEnum.FirstStartApp.name(), isFirstStartApp);
        //
        editor.commit();
    }

    /**
     * 保存用户登录信息到设备文件系统中
     *
     * @param latestLoginNetRespondBean
     */
    public static void setLatestLoginNetRespondBean(LoginNetRespondBean latestLoginNetRespondBean) {

        // 最后登录成功时服务器反馈的信息
        serializeObjectToDevice(CacheDataNameEnum.LatestLoginNetRespondBean.name(), latestLoginNetRespondBean);
    }


    // ///////////////////////////////////////////////////////////////////////////
    // ///////////////////////////////////////////////////////////////////////////
    // ///////////////////////////////////////////////////////////////////////////
    // ///////////////////////////////////////////////////////////////////////////
    // ///////////////////////////////////////////////////////////////////////////
    // ///////////////////////////////////////////////////////////////////////////
    // ///////////////////////////////////////////////////////////////////////////


    /**
     * 将一个对象, 序列化到SD卡存储中(序列化文件保存在SD卡中)
     *
     * @param serializeFileName 序列化文件名称
     * @param directoryPath     SD卡上面的路径
     * @param object
     */
    private static boolean serializeObjectToSDCard(final String serializeFileName, final String directoryPath, final Object object) {
        try {
            if (TextUtils.isEmpty(serializeFileName)) {
                throw new Exception("序列化对象失败, 原因 = 入参 serializeFileName 为空.");
            }

            if (TextUtils.isEmpty(directoryPath)) {
                throw new Exception("序列化对象失败, 原因 = 入参 directoryPath 为空.");
            }

            // 先清理本地旧的序列化文件
            File file = new File(directoryPath + "/" + serializeFileName);
            if (file.exists()) {
                file.delete();
            }

            if (object == null) {
                // 如果调用者传递的object为空的话, 就意味着调用者想要删除本地的缓存数据
                return true;
            }

            return serializeObjectToFileOutputStream(object, new FileOutputStream(file));
        } catch (Exception e) {
            DebugLog.e(TAG, "序列化对象失败, 原因 = " + e.getLocalizedMessage());
            return false;
        }

    }

    /**
     * 将一个对象, 序列化到设备存储中(序列化文件保存在设备中)
     *
     * @param serializeFileName 序列化文件名称
     * @param object
     */
    private static boolean serializeObjectToDevice(final String serializeFileName, final Object object) {
        try {
            if (TextUtils.isEmpty(serializeFileName)) {
                throw new Exception("序列化对象失败, 原因 = 入参 serializeFileName 为空.");
            }

            // 先清理本地旧的序列化文件
            File file = new File(ApplicationSingleton.getInstance.getApplication().getFilesDir() + "/" + serializeFileName);
            if (file.exists()) {
                file.delete();
            }

            if (object == null) {
                // 如果调用者传递的object为空的话, 就意味着调用者想要删除本地的缓存数据
                return true;
            }

            return serializeObjectToFileOutputStream(object, ApplicationSingleton.getInstance.getApplication().openFileOutput(serializeFileName, Context.MODE_PRIVATE));
        } catch (Exception e) {
            DebugLog.e(TAG, "序列化对象失败, 原因 = " + e.getLocalizedMessage());
            return false;
        }
    }

    /**
     * 将一个对象, 序列化到一个 fileOutputStream 中.
     *
     * @param object
     * @param fileOutputStream
     */
    private static boolean serializeObjectToFileOutputStream(final Object object, final FileOutputStream fileOutputStream) {
        ObjectOutputStream objectOutputStream = null;
        try {

            if (fileOutputStream == null) {
                throw new Exception("入参 fileOutputStream 为空.");
            }
            if (object == null) {
                throw new Exception("入参 object 为空.");
            }

            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(object);

            return true;

        } catch (Exception e) {
            DebugLog.e(TAG, "序列化对象失败, 原因 = " + e.getLocalizedMessage());
            return false;
        } finally {
            //
            try {
                if (objectOutputStream != null) {
                    objectOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            //
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 从设备中反序列化一个对象(序列化文件保存在设备中)
     *
     * @param serializeFileName
     * @param <DeserializeObject>
     * @return
     */
    private static <DeserializeObject> DeserializeObject deserializeObjectFromDevice(final String serializeFileName) {
        try {
            if (TextUtils.isEmpty(serializeFileName)) {
                throw new Exception("入参 serializeFileName 为空.");
            }
            File file = new File(ApplicationSingleton.getInstance.getApplication().getFilesDir() + "/" + serializeFileName);
            if (!file.exists()) {
                throw new Exception("设备中缓存的序列化文件 " + serializeFileName + " 不存在.");
            }

            FileInputStream fin = ApplicationSingleton.getInstance.getApplication().openFileInput(serializeFileName);
            return deserializeObjectFromFileInputStream(fin);
        } catch (Exception e) {
            DebugLog.e(TAG, "反序列化对象失败, 原因 = " + e.getLocalizedMessage());
            return null;
        }
    }

    /**
     * 从SD卡中反序列话一个对象(序列化文件保存在SD卡中)
     *
     * @param serializeFileName   序列化文件名称
     * @param directoryPath       序列化文件所在的目录
     * @param <DeserializeObject>
     * @return
     */
    private static <DeserializeObject> DeserializeObject deserializeObjectFromSDCard(final String serializeFileName, final String directoryPath) {
        DeserializeObject object = null;
        try {
            object = deserializeObjectFromFileInputStream(new FileInputStream(new File(directoryPath + "/"
                    + serializeFileName)));
        } catch (FileNotFoundException e) {
            DebugLog.e(TAG, "反序列化对象失败, 原因 = " + e.getLocalizedMessage());
        }
        return object;
    }


    /**
     * 从一个文件流中, 反序列化一个对象
     *
     * @param fileInputStream
     * @param <DeserializeObject>
     * @return
     */
    private static <DeserializeObject> DeserializeObject deserializeObjectFromFileInputStream(FileInputStream fileInputStream) {
        DeserializeObject object = null;
        ObjectInputStream objectInputStream = null;

        try {
            objectInputStream = new ObjectInputStream(fileInputStream);
            object = (DeserializeObject) objectInputStream.readObject();
        } catch (Exception e) {
            object = null;
            DebugLog.e(TAG, "反序列化对象失败, 原因 = " + e.getLocalizedMessage());
        } finally {
            //
            try {
                if (objectInputStream != null) {
                    objectInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            //
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return object;
    }

}
