package org.adligo.fabricate.common.files;

import java.io.File;

public class FabFileUtils {
  
  public static String getAbsoluteDir(String filePath) {
    return getAbsoluteDir(filePath, File.separatorChar);
  }
  
  public static String getAbsoluteDir(String filePath, char separator) {
    
    int idx = -1;
    char [] chars = filePath.toCharArray();
    for (int i = 0; i < chars.length; i++) {
      char c = chars[i];
      if (c == separator) {
        idx = i;
      }
    }
    if (filePath.length() > idx + 1) {
      return filePath.substring(0, idx + 1);
    }
    return filePath;
  }
}
