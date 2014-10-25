package org.adligo.fabricate.common;

public class StringUtils {
  public static boolean isEmpty(String p) {
    if (p == null) {
      return true;
    }
    if (p.trim().length() == 0) {
      return true;
    }
    return false;
  }
}
