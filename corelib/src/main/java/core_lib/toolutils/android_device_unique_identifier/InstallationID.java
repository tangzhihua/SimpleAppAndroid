package core_lib.toolutils.android_device_unique_identifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

import android.content.Context;

// 1. DEVICE_ID 
// 2. MAC ADDRESS 
// 3. Serial Number 
// 4. ANDROID_ID

/**
 * Installtion ID : UUID 以上四种方式都有或多或少存在的一定的局限性或者bug，在这里，有另外一种方式解决，
 * 就是使用UUID，该方法无需访问设备的资源，也跟设备类型无关。
 * 
 * 这种方式是通过在程序安装后第一次运行后生成一个ID实现的，但该方式跟设备唯一标识不一样， 它会因为不同的应用程序而产生不同的ID，而不是设备唯一ID。
 * 因此经常用来标识在某个应用中的唯一ID（即Installtion ID）， 或者跟踪应用的安装数量。很幸运的，Google Developer
 * Blog提供了这样的一个框架：
 * 
 * @author zhihua.tang
 * 
 */
public class InstallationID {
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
