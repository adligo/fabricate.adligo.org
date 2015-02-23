package org.adligo.fabricate.common.util;

public class StringUtils {
  /**
   * This singleton refernce is just to make sure
   * we hit the constructor for code coverage
   */
  public static final StringUtils INSTANCE = new StringUtils();
  
  public static boolean isEmpty(String p) {
    if (p == null) {
      return true;
    }
    if (p.trim().length() == 0) {
      return true;
    }
    return false;
  }
  
  private StringUtils() {}
}
