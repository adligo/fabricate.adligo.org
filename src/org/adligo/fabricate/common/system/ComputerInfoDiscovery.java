package org.adligo.fabricate.common.system;

import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.i18n.I_SystemMessages;
import org.adligo.fabricate.common.util.StringUtils;
import org.adligo.fabricate.models.common.FabricationMemoryConstants;

import java.net.UnknownHostException;

/**
 * This class discovers information about
 * the computer where it is running.
 * 
 * @author scott
 *
 */
public class ComputerInfoDiscovery {
  private static final String UNUX = "Unux";
  public static final String LINUX = "Linux";
  public static final String WINDOWS = "Windows";
  public static final String MAC = "Mac";
  
  public static String[] getCpuInfo(I_FabSystem sys, String os) {
    try {
      if (MAC.equals(os)) {
        I_Executor exe = sys.getExecutor();
        I_ExecutionResult er = exe.executeProcess( FabricationMemoryConstants.EMPTY_ENV,
            ".", "sysctl", "-a"); 
        String cpu = er.getOutput();
        int index = cpu.indexOf(".cpu.brand_string");
        if (index != -1) {
          index =  index + 18;
          return parseCpuInfo(cpu, index);
        }
        I_FabricateConstants constants = sys.getConstants();
        I_SystemMessages sysMessages = constants.getSystemMessages();
        return new String[] {cpu, sysMessages.getUnknown()};
      } else if (WINDOWS.equals(os)){
        I_Executor exe = sys.getExecutor();
        I_ExecutionResult er = exe.executeProcess(FabricationMemoryConstants.EMPTY_ENV,
            ".", "wmic", "cpu", "get", "name"); 
        String cpu = er.getOutput();
        cpu = cpu.replaceFirst("Name", "").trim();
        return parseCpuInfo(cpu, 0);
      } else {
        I_Executor exe = sys.getExecutor();
        I_ExecutionResult er = exe.executeProcess(FabricationMemoryConstants.EMPTY_ENV,
            ".", "cat", "/proc/cpuinfo"); 
        String cpu = er.getOutput();
        int index = cpu.indexOf("model name\t: ");
        if (index != -1) {
          index =  index + 13;
          return parseCpuInfo(cpu, index);
        }
        I_FabricateConstants constants = sys.getConstants();
        I_SystemMessages sysMessages = constants.getSystemMessages();
        return new String[] {cpu, sysMessages.getUnknown()};
      }
    } catch (Exception x) {
      //do nothing
    }
    return new String[] {"unknown","unknown"};
  }


  public static String[] parseCpuInfo(String cpu, int index) {
    
    char[] cpuChars = cpu.toCharArray();
    StringBuilder sbCpu = new StringBuilder();
    StringBuilder sbSpeed = new StringBuilder();
    StringBuilder sb = sbCpu;
    boolean lastWasSpace = false;
    for (int i = index; i < cpuChars.length; i++) {
      char c = cpuChars[i];
      if (c == '@') {
        lastWasSpace = false;
        sb = sbSpeed;
      } else if (c == 'z') {
        lastWasSpace = false;
        sb.append(c);
        break;
      } else if (lastWasSpace) {
        if (!Character.isWhitespace(c)) {
          lastWasSpace = false;
          sb.append(c);
        }
      } else {
        if (Character.isWhitespace(c)) {
          lastWasSpace = true;
          sb.append(" ");
        } else {
          lastWasSpace = false;
          sb.append(c);
        }
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
  

  public static String getCpuSpeed(I_FabSystem sys) {
    I_FabricateConstants constants = sys.getConstants();
    I_SystemMessages sysMessages = constants.getSystemMessages();
    String cpu = sys.getProperty("cpu.brand_string",sysMessages.getUnknown());
    int atIdx = cpu.indexOf("@");
    if (atIdx != -1) {
      if (atIdx != cpu.length() - 1) {
        return cpu.substring(atIdx  + 1, cpu.length());
      }
    }
    return cpu;
  }
  
  
  public static String getOperatingSystem(I_FabSystem sys) {
    
    
    I_FabricateConstants constants = sys.getConstants();
    I_SystemMessages sysMessages = constants.getSystemMessages();
    String os = sys.getProperty("os.name", sysMessages.getUnknown());
    os = os.toLowerCase();
    if ((os.indexOf("mac") >= 0) || (os.indexOf("darwin") >= 0)) {
        return MAC;
      } else if (os.indexOf("win") >= 0) {
        return WINDOWS;
      } else if (os.indexOf("linux") >= 0) {
        return LINUX;
      } else if (os.indexOf("nux") >= 0) {
        return UNUX;
      } else {
        return os;
      }
  }
  
  public static String getJavaVersion(I_FabSystem sys) {
    I_FabricateConstants constants = sys.getConstants();
    I_SystemMessages sysMessages = constants.getSystemMessages();
    return sys.getProperty("java.version", sysMessages.getUnknown());
  }
  
  public static String getOperatingSystemVersion(I_FabSystem sys, String os) {
    if (MAC.equals(os)) {
      try {
        I_Executor exe = sys.getExecutor();
        I_ExecutionResult er = exe.executeProcess(FabricationMemoryConstants.EMPTY_ENV,
            ".", "sw_vers", "-productVersion");
        String ver = er.getOutput();
        if (ver != null) {
          StringBuilder sb = getVersionNumbersAndDots(ver);
          ver = sb.toString();
          return ver;
        }
      } catch (Exception x) {
        //do nothing
      }
    } else if (WINDOWS.equals(os)) {
      I_FabricateConstants constants = sys.getConstants();
      I_SystemMessages sysMessages = constants.getSystemMessages();
      String osName = sys.getProperty("os.name", sysMessages.getUnknown());
      StringBuilder sb = getVersionNumbersAndDots(osName);
      return sb.toString();
    } else if (LINUX.equals(os)) {
      I_FabricateConstants constants = sys.getConstants();
      I_SystemMessages sysMessages = constants.getSystemMessages();
      String osName = sys.getProperty("os.version", sysMessages.getUnknown());
      StringBuilder sb = getVersionNumbersAndDots(osName);
      return sb.toString();
    }
    I_FabricateConstants constants = sys.getConstants();
    I_SystemMessages sysMessages = constants.getSystemMessages();
    return sysMessages.getUnknown();
  }


  public static StringBuilder getVersionNumbersAndDots(String ver) {
    StringBuilder sb = new StringBuilder();
    char [] chars = ver.toCharArray();
    for (int i = 0; i < chars.length; i++) {
      char c = chars[i];
      if (Character.isDigit(c) || '.' == c) {
        sb.append(c);
      }
    }
    return sb;
  }
  
  public static String getHostname(I_FabSystem sys) {
    try {
      String result = sys.getInetAddressHostname();
      if (!StringUtils.isEmpty( result))
          return result;
    } catch (UnknownHostException e) {
        // failed;  try alternate means.
    }
       
    String host = sys.getenv("COMPUTERNAME");
    if (host != null) {
        return host;
    }
    host = sys.getenv("HOSTNAME");
    return host;
  }
  
}
