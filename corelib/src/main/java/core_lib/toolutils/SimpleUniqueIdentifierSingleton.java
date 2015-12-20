package core_lib.toolutils;


import core_lib.global_data_cache.ApplicationSingleton;
import core_lib.global_data_cache.GlobalDataCacheForMemorySingleton;
import core_lib.toolutils.android_device_unique_identifier.DeviceUuidFactory;
import core_lib.toolutils.android_device_unique_identifier.InstallationID;

/**
 * 唯一标识符(android_device_unique_identifier包下面的readme.txt中有详细的说明)
 *
 * @author zhihua.tang
 */
public enum SimpleUniqueIdentifierSingleton {
    getInstance;

    private final DeviceUuidFactory deviceUuidFactory = new DeviceUuidFactory(
            ApplicationSingleton.getInstance.getApplication());

    /**
     * 设备唯一ID
     *
     * @return
     */
    // 综合以上所述，为了实现在设备上更通用的获取设备唯一标识，我们可以实现这样的一个类，
    // 为每个设备产生唯一的UUID，以ANDROID_ID为基础，
    // 在获取失败时以TelephonyManager.getDeviceId()为备选方法，如果再失败，使用UUID的生成策略。
    public String getDeviceUniqueIdentifierString() {
        return deviceUuidFactory.getDeviceUuid().toString();
    }

    private final String installtionIdentifierString = InstallationID.id(ApplicationSingleton.getInstance
            .getApplication());

    /**
     * 安装ID
     *
     * @return
     */
    // 通过在程序安装后第一次运行后生成一个ID实现的，但该方式跟设备唯一标识不一样，
    // 它会因为不同的应用程序而产生不同的ID，而不是设备唯一ID。
    // 因此经常用来标识在某个应用中的唯一ID（即Installtion ID）， 或者跟踪应用的安装数量。
    public String getInstalltionIdentifierString() {
        return installtionIdentifierString;
    }
}
