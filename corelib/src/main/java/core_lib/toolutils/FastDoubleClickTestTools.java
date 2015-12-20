package core_lib.toolutils;

/**
 * 快速双击屏幕检测, 用于防御某些按钮被快速按下后, 而产生bug
 *
 * @author zhihua.tang
 */
public final class FastDoubleClickTestTools {

    private FastDoubleClickTestTools() {
        throw new AssertionError("这个是一个工具类, 不能创建实例对象.");
    }

    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 800) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
