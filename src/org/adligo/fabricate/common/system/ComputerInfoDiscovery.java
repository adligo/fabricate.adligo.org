package org.adligo.fabricate.common.system;

import org.adligo.fabricate.common.util.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ComputerInfoDiscovery {
  private static final Set<String> OS_WITH_SYSCTL = getOsWithSysCtl();
  
  public static Set<String> getOsWithSysCtl() {
    Set<String> toRet = new HashSet<String>();
    toRet.add("mac");
    toRet.add("linux");
    return Collections.unmodifiableSet(toRet);
  }
  
  public static String getOperatingSystem() {
    String os = System.getProperty("os.name", "unknown");
    os = os.toLowerCase();
    if ((os.indexOf("mac") >= 0) || (os.indexOf("darwin") >= 0)) {
        return "mac";
      } else if (os.indexOf("win") >= 0) {
        return "windows";
      } else if (os.indexOf("linux") >= 0) {
        return "linux";
      } else if (os.indexOf("nux") >= 0) {
        return "unux";
      } else {
        return os;
      }
  }
  
  public static String getJavaVersion() {
    return System.getProperty("java.version", "unknown");
  }
  
  public static String getOperatingSystemVersion(I_FabSystem sys, String os) {
    if ("mac".equals(os)) {
      try {
        I_Executor exe = sys.getExecutor();
        I_ExecutionResult er = exe.executeProcess(".", "sw_vers", "-productVersion");
        String ver = er.getOutput();
        if (ver != null) {
          return ver;
        }
      } catch (Exception x) {
        //do nothing
      }
    }
    return "unknown";
  }
  
  public static String getHostname() {
    try {
      String result = InetAddress.getLocalHost().getHostName();
      if (!StringUtils.isEmpty( result))
          return result;
    } catch (UnknownHostException e) {
        // failed;  try alternate means.
    }

    // try environment properties.
  //        
    String host = System.getenv("COMPUTERNAME");
    if (host != null) {
        return host;
    }
    host = System.getenv("HOSTNAME");
    return host;
  }
  
  public static String[] getCpuInfo(I_FabSystem sys, String os) {
    try {
      if (OS_WITH_SYSCTL.contains(os)) {
        I_Executor exe = sys.getExecutor();
        I_ExecutionResult er = exe.executeProcess(".", "sysctl", "-a"); 
        String cpu = er.getOutput();
        int index = cpu.indexOf(".cpu.brand_string") + 18;
        if (index != -1) {
          char[] cpuChars = cpu.toCharArray();
          StringBuilder sbCpu = new StringBuilder();
          StringBuilder sbSpeed = new StringBuilder();
          StringBuilder sb = sbCpu;
          for (int i = index; i < cpuChars.length; i++) {
            char c = cpuChars[i];
            if (c == '@') {
              sb = sbSpeed;
            } else if (c == 'z') {
              sb.append(c);
              break;
            } else {
              sb.append(c);
            }
          }
          String cpuResult = sbCpu.toString().trim();
          String speed = sbSpeed.toString().trim();
          if (cpuResult.length() >= 40) {
            cpuResult = cpuResult.substring(0, 40);
          }
          if (speed.length() >= 40) {
            speed = speed.substring(0, 40);
          }
          return new String[] {cpuResult,speed};
        }
        return new String[] {cpu,"unknown"};
      }
    } catch (Exception x) {
      //do nothing
    }
    return new String[] {"unknown","unknown"};
  }
  
  public static String getCpuSpeed() {
    String cpu = System.getProperty("cpu.brand_string");
    int atIdx = cpu.indexOf("@");
    if (atIdx != -1) {
      if (atIdx != cpu.length() - 1) {
        return cpu.substring(atIdx  + 1, cpu.length());
      }
    }
    return cpu;
  }
  
}
