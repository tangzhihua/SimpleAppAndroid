获取Android设备唯一标识码

概述
有时需要对用户设备进行标识，所以希望能够得到一个稳定可靠并且唯一的识别码。虽然Android系统中提供了这样设备识别码，但是由于Android系统版本、厂商定制系统中的Bug等限制，稳定性和唯一性并不理想。
而通过其他硬件信息标识也因为系统版本、手机硬件等限制存在不同程度的问题。

下面收集了一些“有能力”或“有一定能力”作为设备标识的串码。

DEVICE_ID
这是Android系统为开发者提供的用于标识手机设备的串号，也是各种方法中普适性较高的，可以说几乎所有的设备都可以返回这个串号，并且唯一性良好。

这个DEVICE_ID可以同通过下面的方法获取：

TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE); String DEVICE_ID = tm.getDeviceId(); 
它会根据不同的手机设备返回IMEI，MEID或者ESN码，但在使用的过程中有以下问题：

非手机设备：最开始搭载Android系统都手机设备，而现在也出现了非手机设备：如平板电脑、电子书、电视、音乐播放器等。这些设备没有通话的硬件功能，系统中也就没有TELEPHONY_SERVICE，自然也就无法通过上面的方法获得DEVICE_ID。
权限问题：获取DEVICE_ID需要READ_PHONE_STATE权限，如果只是为了获取DEVICE_ID而没有用到其他的通话功能，申请这个权限一来大才小用，二来部分用户会怀疑软件的安全性。
厂商定制系统中的Bug：少数手机设备上，由于该实现有漏洞，会返回垃圾，如:zeros或者asterisks
MAC ADDRESS
可以使用手机Wifi或蓝牙的MAC地址作为设备标识，但是并不推荐这么做，原因有以下两点：

硬件限制：并不是所有的设备都有Wifi和蓝牙硬件，硬件不存在自然也就得不到这一信息。
获取的限制：如果Wifi没有打开过，是无法获取其Mac地址的；而蓝牙是只有在打开的时候才能获取到其Mac地址。
获取Wifi Mac地址：

获取蓝牙 Mac地址：

Sim Serial Number
装有SIM卡的设备，可以通过下面的方法获取到Sim Serial Number：

TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE); String SimSerialNumber = tm.getSimSerialNumber(); 
注意：对于CDMA设备，返回的是一个空值！

ANDROID_ID
在设备首次启动时，系统会随机生成一个64位的数字，并把这个数字以16进制字符串的形式保存下来，这个16进制的字符串就是ANDROID_ID，当设备被wipe后该值会被重置。可以通过下面的方法获取：

import android.provider.Settings;   String ANDROID_ID = Settings.System.getString(getContentResolver(), Settings.System.ANDROID_ID); 
ANDROID_ID可以作为设备标识，但需要注意：

厂商定制系统的Bug：不同的设备可能会产生相同的ANDROID_ID：9774d56d682e549c。
厂商定制系统的Bug：有些设备返回的值为null。
设备差异：对于CDMA设备，ANDROID_ID和TelephonyManager.getDeviceId() 返回相同的值。
Serial Number
Android系统2.3版本以上可以通过下面的方法得到Serial Number，且非手机设备也可以通过该接口获取。

String SerialNumber = android.os.Build.SERIAL; 
Installtion ID
以上几种方式都或多或少存在一定的局限性或者Bug，如果并不是确实需要对硬件本身进行绑定，使用自己生成的UUID也是一个不错的选择，因为该方法无需访问设备的资源，也跟设备类型无关。

这种方式的原理是在程序安装后第一次运行时生成一个ID，该方式和设备唯一标识不一样，不同的应用程序会产生不同的ID，同一个程序重新安装也会不同。所以这不是设备的唯一ID，
但是可以保证每个用户的ID是不同的。可以说是用来标识每一份应用程序的唯一ID（即Installtion ID），可以用来跟踪应用的安装数量等。

Google Developer Blog提供了这样的一个框架：

略.....







设备唯一ID
上文可以看出，Android系统中并没有可以可靠获取所有厂商设备唯一ID的方法，各个方法都有自己的使用范围和局限性，这也是目前流行的Android系统版本过多，设备也是来自不同厂商，且没有统一标准等原因造成的。

从目前发展来看，Android系统多版本共存还会持续较长的时间，而Android系统也不会被某个设备生产厂商垄断，长远看Android基础系统将会趋于稳定，设备标识也将会作为系统基础部分而标准化，届时这一问题才有望彻底解决。

目前的解决办法，比较可行的是一一适配，在保证大多数设备方便的前提下，如果获取不到，使用其他备选信息作为标识，即自己再封装一个设备ID出来，通过内部算法保证尽量和设备硬件信息相关，以及标识的唯一性。

 

 

android 底层是 Linux，我们还是用Linux的方法来获取：


1 cpu号：

文件在： /proc/cpuinfo

通过Adb shell 查看：

adb shell cat /proc/cpuinfo

2 mac 地址

文件路径 /sys/class/net/wlan0/address

adb shell  cat /sys/class/net/wlan0/address                              
xx:xx:xx:xx:xx:aa

这样可以获取两者的序列号，


方法确定，剩下的就是写代码了

以Mac地址为例：

        String getMac() {
                String macSerial = null;
                String str = "";
                try {
                        Process pp = Runtime.getRuntime().exec(
                                        "cat /sys/class/net/wlan0/address ");
                        InputStreamReader ir = new InputStreamReader(pp.getInputStream());
                        LineNumberReader input = new LineNumberReader(ir);


                        for (; null != str;) {
                                str = input.readLine();
                                if (str != null) {
                                        macSerial = str.trim();// 去空格
                                        break;
                                }
                        }
                } catch (IOException ex) {
                        // 赋予默认值
                        ex.printStackTrace();
                }
                return macSerial;
        }

 

 

 

Android 手机上获取物理唯一标识码
唯一标识码这东西在网络应用中非常有用，例如检测是否重复注册之类的。

import android.provider.Settings.Secure;
private String android_id = Secure.getString(getContext().getContentResolver(), Secure.ANDROID_ID);

 

我们在项目过程中或多或少会使用到设备的唯一识别码，我们希望能够得到一个稳定、可靠的设备唯一识别码。今天我们将介绍几种方式。

       1. DEVICE_ID
 

假设我们确实需要用到真实设备的标识，可能就需要用到DEVICE_ID。在以前，我们的Android设备是手机，这个DEVICE_ID可以同通过TelephonyManager.getDeviceId()获取，
它根据不同的手机设备返回IMEI，MEID或者ESN码，但它在使用的过程中会遇到很多问题：

非手机设备： 如果只带有Wifi的设备或者音乐播放器没有通话的硬件功能的话就没有这个DEVICE_ID
权限： 获取DEVICE_ID需要READ_PHONE_STATE权限，但如果我们只为了获取它，没有用到其他的通话功能，那这个权限有点大才小用
bug：在少数的一些手机设备上，该实现有漏洞，会返回垃圾，如:zeros或者asterisks的产品
 

        2. MAC ADDRESS
 

我们也可以通过手机的Wifi或者蓝牙设备获取MAC ADDRESS作为DEVICE ID，但是并不建议这么做，因为并不是所有的设备都有Wifi，并且，如果Wifi没有打开，那硬件设备无法返回MAC ADDRESS.

 

        3. Serial Number
在Android 2.3可以通过android.os.Build.SERIAL获取，非手机设备可以通过该接口获取。

        4. ANDROID_ID
ANDROID_ID是设备第一次启动时产生和存储的64bit的一个数，当设备被wipe后该数重置

ANDROID_ID似乎是获取Device ID的一个好选择，但它也有缺陷：

 

它在Android <=2.1 or Android >=2.3的版本是可靠、稳定的，但在2.2的版本并不是100%可靠的
在主流厂商生产的设备上，有一个很经常的bug，就是每个设备都会产生相同的ANDROID_ID：9774d56d682e549c
 

        5. Installtion ID : UUID
以上四种方式都有或多或少存在的一定的局限性或者bug，在这里，有另外一种方式解决，就是使用UUID，该方法无需访问设备的资源，也跟设备类型无关。

这种方式是通过在程序安装后第一次运行后生成一个ID实现的，但该方式跟设备唯一标识不一样，它会因为不同的应用程序而产生不同的ID，而不是设备唯一ID。
因此经常用来标识在某个应用中的唯一ID（即Installtion ID），或者跟踪应用的安装数量。很幸运的，Google Developer Blog提供了这样的一个框架：

public class Installation {
    private static String sID = null;
    private static final String INSTALLATION = "INSTALLATION";

    public synchronized static String id(Context context) {
        if (sID == null) {  
            File installation = new File(context.getFilesDir(), INSTALLATION);
            try {
                if (!installation.exists())
                    writeInstallationFile(installation);
                sID = readInstallationFile(installation);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return sID;
    }

    private static String readInstallationFile(File installation) throws IOException {
        RandomAccessFile f = new RandomAccessFile(installation, "r");
        byte[] bytes = new byte[(int) f.length()];
        f.readFully(bytes);
        f.close();
        return new String(bytes);
    }

    private static void writeInstallationFile(File installation) throws IOException {
        FileOutputStream out = new FileOutputStream(installation);
        String id = UUID.randomUUID().toString();
        out.write(id.getBytes());
        out.close();
    }
}

   总结

综合以上所述，为了实现在设备上更通用的获取设备唯一标识，我们可以实现这样的一个类，为每个设备产生唯一的UUID，以ANDROID_ID为基础，在获取失败时以TelephonyManager.getDeviceId()为备选方法，如果再失败，使用UUID的生成策略。

重申下，以下方法是生成Device ID，在大多数情况下Installtion ID能够满足我们的需求，但是如果确实需要用到Device ID，那可以通过以下方式实现：

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

public class DeviceUuidFactory {
    protected static final String PREFS_FILE = "device_id.xml";
    protected static final String PREFS_DEVICE_ID = "device_id";

    protected static UUID uuid;

 

    public DeviceUuidFactory(Context context) {

        if( uuid ==null ) {
            synchronized (DeviceUuidFactory.class) {
                if( uuid == null) {
                    final SharedPreferences prefs = context.getSharedPreferences( PREFS_FILE, 0);
                    final String id = prefs.getString(PREFS_DEVICE_ID, null );

                    if (id != null) {
                        // Use the ids previously computed and stored in the prefs file
                        uuid = UUID.fromString(id);

                    } else {

                        final String androidId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);

                        // Use the Android ID unless it's broken, in which case fallback on deviceId,
                        // unless it's not available, then fallback on a random number which we store
                        // to a prefs file
                        try {
                            if (!"9774d56d682e549c".equals(androidId)) {
                                uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
                            } else {
                                final String deviceId = ((TelephonyManager) context.getSystemService( Context.TELEPHONY_SERVICE )).getDeviceId();
                                uuid = deviceId!=null ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")) : UUID.randomUUID();
                            }
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }

                        // Write the value out to the prefs file
                        prefs.edit().putString(PREFS_DEVICE_ID, uuid.toString() ).commit();

                    }

                }
            }
        }

    }


    /**
     * Returns a unique UUID for the current android device.  As with all UUIDs, this unique ID is "very highly likely"
     * to be unique across all Android devices.  Much more so than ANDROID_ID is.
     *
     * The UUID is generated by using ANDROID_ID as the base key if appropriate, falling back on
     * TelephonyManager.getDeviceID() if ANDROID_ID is known to be incorrect, and finally falling back
     * on a random UUID that's persisted to SharedPreferences if getDeviceID() does not return a
     * usable value.
     *
     * In some rare circumstances, this ID may change.  In particular, if the device is factory reset a new device ID
     * may be generated.  In addition, if a user upgrades their phone from certain buggy implementations of Android 2.2
     * to a newer, non-buggy version of Android, the device ID may change.  Or, if a user uninstalls your app on
     * a device that has neither a proper Android ID nor a Device ID, this ID may change on reinstallation.
     *
     * Note that if the code falls back on using TelephonyManager.getDeviceId(), the resulting ID will NOT
     * change after a factory reset.  Something to be aware of.
     *
     * Works around a bug in Android 2.2 for many devices when using ANDROID_ID directly.
     *
     * @see http://code.google.com/p/android/issues/detail?id=10603
     *
     * @return a UUID that may be used to uniquely identify your device for most purposes.
     */
    public UUID getDeviceUuid() {
        return uuid;
    }
}

如何获取Android手机的唯一标识？
代码: 这里是你在Android里读出 唯一的 IMSI-ID / IMEI-ID 的方法。 
Java: 
String myIMSI = android.os.SystemProperties.get(android.telephony.TelephonyProperties.PROPERTY_IMSI); 
// within my emulator it returns: 310995000000000 

String myIMEI = android.os.SystemProperties.get(android.telephony.TelephonyProperties.PROPERTY_IMEI); 
// within my emulator it returns: 000000000000000 

注：android.os.SystemProperties的标签被打上@hide了，所以sdk中并不会存在。如果需要使用，需要有android的source code支持。