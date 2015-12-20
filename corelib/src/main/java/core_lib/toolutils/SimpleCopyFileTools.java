package core_lib.toolutils;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 文件拷贝工具类
 * <p/>
 * 说明 : 性能最好的是 nio mapped 和 buffered stream, 其他方式只是为了性能测试比对
 *
 * @author zhihua.tang
 */
public final class SimpleCopyFileTools {

    private SimpleCopyFileTools() {
        throw new AssertionError("这个是一个工具类, 不能创建实例对象.");
    }

    /**
     * 使用nio的方式拷贝文件
     *
     * @param resource
     * @param destination
     */
    public static boolean copyFileUseNioMapped(final String resource, final String destination) {
        boolean result = false;
        RandomAccessFile rafi = null;
        RandomAccessFile rafo = null;
        FileChannel readChannel = null;// 读文件通道
        FileChannel writeChannel = null;// 写文件通道
        try {
            rafi = new RandomAccessFile(resource, "r");
            rafo = new RandomAccessFile(destination, "rw");
            readChannel = rafi.getChannel();// 读文件通道
            writeChannel = rafo.getChannel();// 写文件通道
            long fileSize = readChannel.size();
            MappedByteBuffer mbbi = readChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileSize);
            MappedByteBuffer mbbo = writeChannel.map(FileChannel.MapMode.READ_WRITE, 0, fileSize);

            byte tmpBuf[] = new byte[1024 * 8];
            while (mbbi.hasRemaining()) {
                ByteBuffer tmpByteBuffer = mbbi.get(tmpBuf);
                mbbo.put(tmpByteBuffer);
            }

            result = true;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            result = false;
        } finally {
            //
            if (readChannel != null) {
                try {
                    readChannel.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    readChannel = null;
                }
            }

            //
            if (writeChannel != null) {
                try {
                    writeChannel.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    writeChannel = null;
                }
            }
            //
            if (rafi != null) {
                try {
                    rafi.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    rafi = null;
                }
            }
            //
            if (rafo != null) {
                try {
                    rafo.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    rafo = null;
                }
            }
        }

        return result;
    }

    /**
     * 使用nio的方式拷贝文件
     *
     * @param resource
     * @param destination
     */
    public static void copyFileUseNio(final String resource, final String destination) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel readChannel = null;// 读文件通道
        FileChannel writeChannel = null;// 写文件通道
        try {
            fis = new FileInputStream(resource);
            fos = new FileOutputStream(destination);
            readChannel = fis.getChannel();// 读文件通道
            writeChannel = fos.getChannel();// 写文件通道
            ByteBuffer buffer = ByteBuffer.allocate(1024);// 读入数据缓存
            while (true) {
                buffer.clear();
                int len = readChannel.read(buffer);
                if (len == -1) {
                    break;
                }
                buffer.flip();
                writeChannel.write(buffer);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            //
            if (readChannel != null) {
                try {
                    readChannel.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    readChannel = null;
                }
            }

            //
            if (writeChannel != null) {
                try {
                    writeChannel.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    writeChannel = null;
                }
            }
            //
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    fis = null;
                }
            }
            //
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    fos = null;
                }
            }
        }

    }

    /**
     * 使用流的方式拷贝文件
     *
     * @param resource
     * @param destination
     */
    public static void copyFileUseStream(final String resource, final String destination) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(resource);
            fos = new FileOutputStream(destination);
            byte[] buffer = new byte[1024];
            while (true) {
                int length = fis.read(buffer);
                if (length == -1) {
                    break;
                }
                fos.write(buffer, 0, length);
            }

            fos.flush();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            //
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    fis = null;
                }
            }
            //
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    fos = null;
                }
            }
        }

    }

    /**
     * 使用流的方式拷贝文件
     *
     * @param resource
     * @param destination
     */
    public static boolean copyFileUseBufferedStream(final String resource, final String destination) {
        boolean result = false;
        InputStream is = null;
        OutputStream os = null;
        try {

            // TODO : 一定要对 输入流和输出流都使用Buffered装饰, 性能才是最优的
            // TODO : 使用默认的buffer大小, 不一定是最优的, 可以指定缓存区大小
            is = new BufferedInputStream(new FileInputStream(resource), 1024 * 8);
            os = new BufferedOutputStream(new FileOutputStream(destination), 1024 * 8);
            byte[] buffer = new byte[1024];
            while (true) {
                int length = is.read(buffer);
                if (length == -1) {
                    break;
                }
                os.write(buffer, 0, length);
            }

            os.flush();

            result = true;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            result = false;
        } finally {
            //
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    is = null;
                }
            }
            //
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    os = null;
                }
            }
        }

        return result;
    }

    /**
     * 从assets目录下面拷贝文件到目标路径下(只限于单独文件)
     *
     * @param context
     * @param assetsFileName assets中的文件名
     * @param outFilePath    输出路径
     */

    public static void copyFileFromAssetsUseStream(Context context, String assetsFileName, String outFilePath) {
        InputStream is = null;
        OutputStream os = null;

        try {

            is = context.getAssets().open(assetsFileName);
            os = new FileOutputStream(outFilePath);
            byte[] buffer = new byte[1024];

            while (true) {
                int length = is.read(buffer);
                if (length == -1) {
                    break;
                }
                os.write(buffer, 0, length);
            }

            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    is = null;
                }
            }

            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    os = null;
                }
            }
        }
    }

    public static void copyFileFromAssetsUseNio(Context context, String assetsFileName, String outFilePath) {
        InputStream is = null;
        FileOutputStream fos = null;
        FileChannel writeChannel = null;// 写文件通道

        try {
            // TODO: 在这里, 给is增加Buffered装饰, 不会提高性能
            is = new BufferedInputStream(context.getAssets().open(assetsFileName));
            fos = new FileOutputStream(outFilePath);
            writeChannel = fos.getChannel();// 写文件通道
            byte[] buffer = new byte[1024];
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);// 读入数据缓存
            while (true) {
                byteBuffer.clear();
                int length = is.read(buffer);
                if (length == -1) {
                    break;
                }
                byteBuffer.put(buffer);
                byteBuffer.flip();
                writeChannel.write(byteBuffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    is = null;
                }
            }

            if (writeChannel != null) {
                try {
                    writeChannel.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    writeChannel = null;
                }
            }

            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    fos = null;
                }
            }
        }
    }

    public static boolean copyFileFromAssetsUseBufferedStream(Context context, String assetsFileName, String outFilePath) {
        InputStream is = null;
        OutputStream os = null;

        try {

            is = new BufferedInputStream(context.getAssets().open(assetsFileName));
            os = new BufferedOutputStream(new FileOutputStream(outFilePath));
            byte[] buffer = new byte[1024];

            while (true) {
                int length = is.read(buffer);
                if (length == -1) {
                    break;
                }
                os.write(buffer, 0, length);
            }

            os.flush();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    is = null;
                }
            }

            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    os = null;
                }
            }
        }
    }

    /**
     * 这个方法目前有问题
     *
     * @param context
     * @param assetsFileName
     * @param outFilePath
     * @deprecated 这个方法目前有问题, 当数据过小时, 将出现问题
     */
    public static void copyFileFromAssetsUseNioMapped(Context context, String assetsFileName, String outFilePath) {
        DataInputStream is = null;
        RandomAccessFile fos = null;
        FileChannel writeChannel = null;// 写文件通道

        try {
            is = new DataInputStream(context.getAssets().open(assetsFileName));
            fos = new RandomAccessFile(outFilePath, "rw");
            writeChannel = fos.getChannel();// 写文件通道
            int fileSize = is.available();
            MappedByteBuffer mbbo = writeChannel.map(FileChannel.MapMode.READ_WRITE, 0, fileSize);

            byte tmpBuf[] = new byte[1024 * 8];
            while (true) {
                int len = is.read(tmpBuf);
                if (len == -1) {
                    break;
                }
                mbbo.put(tmpBuf, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    is = null;
                }
            }

            if (writeChannel != null) {
                try {
                    writeChannel.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    writeChannel = null;
                }
            }

            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    fos = null;
                }
            }
        }
    }

    public static boolean copyFileUseBufferedStream(final byte[] resource, final String destination) {
        boolean result = false;
        InputStream is = null;
        OutputStream os = null;
        try {

            // TODO : 一定要对 输入流和输出流都使用Buffered装饰, 性能才是最优的
            // TODO : 使用默认的buffer大小, 不一定是最优的, 可以指定缓存区大小
            is = new BufferedInputStream(new ByteArrayInputStream(resource), 1024 * 8);
            os = new BufferedOutputStream(new FileOutputStream(destination), 1024 * 8);
            byte[] buffer = new byte[1024];
            while (true) {
                int length = is.read(buffer);
                if (length == -1) {
                    break;
                }
                os.write(buffer, 0, length);
            }

            os.flush();

            result = true;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            result = false;
        } finally {
            //
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    is = null;
                }
            }
            //
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    os = null;
                }
            }
        }

        return result;
    }
}
