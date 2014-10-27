package org.adligo.fabricate;

import org.adligo.fabricate.common.FabricateDefaults;
import org.adligo.fabricate.common.FabricateHelper;
import org.adligo.fabricate.common.FabricateXmlDiscovery;
import org.adligo.fabricate.common.StringUtils;
import org.adligo.fabricate.xml.io.FabricateType;
import org.adligo.fabricate.xml.io.JavaType;
import org.adligo.fabricate.xml_io.FabricateIO;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class FabricateSetup {
  private static PrintStream OUT = System.out;
  
	public static void main(String [] args) {
	  List<String> argList = new ArrayList<String>();
	  for (int i = 0; i < args.length; i++) {
      argList.add(args[i]);
    }
	  if ("args".equals(args[0])) {
	    doArgs(argList.contains("debug"));
	  } else if ("opts".equals(args[0])) {
	    doOpts(argList.contains("debug"));
	  }
	  
	}

  private static void doOpts(boolean debug) {
    String fabHome = System.getenv("FABRICATE_HOME");
    FabricateXmlDiscovery fd = new FabricateXmlDiscovery();
    if (!fd.hasFabricateXml()) {
      System.out.println("-cp " + fabHome + "/lib/*.jar");
    } else {
      workWithFabricateXml(fabHome, fd, debug);
    }
  }

  public static void workWithFabricateXml(String fabHome, FabricateXmlDiscovery fd, boolean debug) {
    File fabricateXml = new File(fd.getFabricateXmlPath());
    
    try {
      FabricateType fab =  FabricateIO.parse(fabricateXml);
      FabricateHelper fh = new FabricateHelper(fab);
     
      System.out.println(" -Xmx" + fh.getXmx() + " -Xms" + fh.getXms() + " -cp " + 
          buildClasspath(fabricateXml.getAbsolutePath(), fabHome));
    } catch (IOException e) {
      if (debug) {
        e.printStackTrace();
      }
      System.out.println(" -Xmx" + FabricateDefaults.JAVA_XMX_DEFAULT + " -Xms" + 
          FabricateDefaults.JAVA_XMS_DEFAULT + " -cp " + fabHome + "/lib/*.jar");
    }
    
   
  }

  private static void doArgs(boolean debug) {
    long now = System.currentTimeMillis();
    OUT.println("start=" + now);
  }
  
  public static String buildClasspath(String fabricateXml, String fabricateHome) {
    StringBuilder sb = new StringBuilder();
    
    File file = new File(fabricateHome + File.separator + "lib");
    if (file.exists()) {
      String [] files = file.list();
      if (files != null) {
        for (int i = 0; i < files.length; i++) {
          if (i != 0) {
            sb.append(File.pathSeparator);
          }
          sb.append(file.getAbsolutePath() + File.separator + files[i]);
        }
      }
    }
    int idx = fabricateXml.indexOf("fabricate.xml");
    String fabricatePath = fabricateXml.substring(0, idx);
    File fabLib = new File(fabricatePath + File.separator + "lib");
    if (fabLib.exists()) {
      String [] files = fabLib.list();
      if (files != null) {
        for (int i = 0; i < files.length; i++) {
          sb.append(File.pathSeparator + file.getAbsolutePath() + File.separator + files[i]);
        }
      }
    }
    String result = sb.toString();
    return result;
  }
}
