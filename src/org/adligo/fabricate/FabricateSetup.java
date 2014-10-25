package org.adligo.fabricate;

import org.adligo.fabricate.common.FabLogMutant;
import org.adligo.fabricate.common.FabricateDefaults;
import org.adligo.fabricate.common.FabricateXmlDiscovery;
import org.adligo.fabricate.common.StringUtils;
import org.adligo.fabricate.models.Fabricate;
import org.adligo.fabricate.parsers.FabricateParser;
import org.adligo.fabricate.xml.io.FabricateType;
import org.adligo.fabricate.xml.io.JavaType;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class FabricateSetup {
  private static PrintStream OUT = System.out;
  
	public static void main(String [] args) {
	  if ("args".equals(args[0])) {
	    doArgs();
	  } else if ("opts".equals(args[0])) {
	    doOpts();
	  }
	  
	}

  private static void doOpts() {
    String fabHome = System.getenv("FABRICATE_HOME");
    FabricateXmlDiscovery fd = new FabricateXmlDiscovery(new FabLogMutant());
    if (!fd.hasFabricateXml()) {
      System.out.println("-cp " + fabHome + "/lib/*.jar");
    } else {
      workWithFabricateXml(fabHome, fd);
    }
  }

  public static void workWithFabricateXml(String fabHome, FabricateXmlDiscovery fd) {
    File fabricateXml = fd.getFabricateXml();
    try {
      FabricateParser parser = new FabricateParser(new FabLogMutant());
      FabricateType fab =  parser.parse(fabricateXml);
      JavaType jt = fab.getJava();
      
      String xmx = FabricateDefaults.JAVA_XMX_DEFAULT;
      String xms = FabricateDefaults.JAVA_XMX_DEFAULT;
      if (jt != null) {
        String jxmx = jt.getXmx();
        if (!StringUtils.isEmpty(jxmx)) {
          xmx = jxmx;
        }
        String jxms = jt.getXms();
        if (!StringUtils.isEmpty(jxms)) {
          xms = jxms;
        }
      }
      System.out.println(" -Xmx" + xmx + " -Xms" + xms + " -cp " + fabHome + "/lib/*.jar");
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println(" -Xmx" + Fabricate.JAVA_XMX_DEFAULT + " -Xms" + 
          Fabricate.JAVA_XMS_DEFAULT + " -cp " + fabHome + "/lib/*.jar");
    }
    
   
  }

  private static void doArgs() {
    long now = System.currentTimeMillis();
    OUT.println("start=" + now);
  }
}
