package core_lib.toolutils;

import android.text.TextUtils;
import android.util.Log;

/**
 * 程序中不要使用系统原生的Log类来输出打印消息, 统一使用当前调试Log类
 * 
 * @author zhihua.tang
 * 
 */
public final class DebugLog {
  private final static String TAG = DebugLog.class.getSimpleName();

  private DebugLog() {
    throw new AssertionError("这个是一个工具类, 不能创建实例对象.");
  }

  public static boolean logIsOpen = true;
  /**
   * 在静态块中预先读取是否打开调试Log的标志位 note : 静态块会在首次调用当前类的静态方法的时候被调用, 所以不用担心
   */
  static {

    Log.i(TAG, "logIsOpen=" + logIsOpen);
  }

  public static int i(String tag, String msg) {
    int result = -1;
    do {
      if (TextUtils.isEmpty(tag) || TextUtils.isEmpty(msg)) {
        break;
      }

      if (logIsOpen) {
        result = Log.i(tag, msg);
      }
    } while (false);

    return result;
  }

  public static int d(String tag, String msg) {
    int result = -1;
    do {
      if (TextUtils.isEmpty(tag) || TextUtils.isEmpty(msg)) {
        break;
      }

      if (logIsOpen) {
        result = Log.d(tag, msg);
      }
    } while (false);

    return result;
  }

  public static int e(String tag, String msg) {
    int result = -1;
    do {
      if (TextUtils.isEmpty(tag) || TextUtils.isEmpty(msg)) {
        break;
      }

      if (logIsOpen) {
        result = Log.e(tag, msg);
      }
    } while (false);

    return result;
  }

  public static int e(String tag, String msg, Throwable tr) {
    int result = -1;
    do {
      if (TextUtils.isEmpty(tag) || TextUtils.isEmpty(msg)) {
        break;
      }

      if (logIsOpen) {
        result = Log.e(tag, msg, tr);
      }
    } while (false);

    return result;
  }
}
