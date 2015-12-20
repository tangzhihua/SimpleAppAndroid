package core_lib.toolutils;

import java.io.File;
import java.util.Set;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;

public final class SimpleDeleteFileTools {

  private SimpleDeleteFileTools() {
    throw new AssertionError("这个是一个工具类, 不能创建实例对象.");
  }

  //删除文件夹
  //param folderPath 文件夹完整绝对路径

  public static void delFolder(String folderPath) {
    try {
      delAllFile(folderPath); //删除完里面所有内容
      String filePath = folderPath;
      filePath = filePath.toString();
      java.io.File myFilePath = new java.io.File(filePath);
      myFilePath.delete(); //删除空文件夹
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  //删除指定文件夹下所有文件
  //param path 文件夹完整绝对路径
  public static boolean delAllFile(String folderPath) {
    return delAllFileExceptForSpecialFile(folderPath, null);
  }

  public static boolean delAllFileExceptForSpecialFile(String folderPath, String specialFileName) {
    return delAllFileExceptForSpecialFiles(folderPath, null);
  }

  public static boolean delAllFileExceptForSpecialFiles(String folderPath, Set<String> specialFileNames) {
    if (Strings.isNullOrEmpty(folderPath)) {
      return false;
    }
    if (specialFileNames == null) {
      specialFileNames = Sets.newHashSet();
    }
    boolean flag = false;
    File file = new File(folderPath);
    if (!file.exists()) {
      return flag;
    }
    if (!file.isDirectory()) {
      return flag;
    }
    String[] tempList = file.list();
    File temp = null;
    for (int i = 0; i < tempList.length; i++) {
      if (folderPath.endsWith(File.separator)) {
        temp = new File(folderPath + tempList[i]);
      } else {
        temp = new File(folderPath + File.separator + tempList[i]);
      }
      if (temp.isFile()) {
        if (!specialFileNames.contains(temp.getName())) {
          // 不能删除指定要保留的文件
          temp.delete();
        }
      }
      if (temp.isDirectory()) {
        delAllFile(folderPath + "/" + tempList[i]);//先删除文件夹里面的文件
        delFolder(folderPath + "/" + tempList[i]);//再删除空文件夹
        flag = true;
      }
    }
    return flag;
  }
}
