package org.adligo.fabricate;

import java.io.File;
import java.io.PrintStream;

public class FabricateSetup {
  private static PrintStream OUT = System.out;
  
	public static void main(String [] args) {
	  if ("args".equals(args[0])) {
	    long now = System.currentTimeMillis();
	    OUT.println("start=" + now);
	  } else if ("opts".equals(args[0])) {
	    String fabHome = System.getenv("FABRICATE_HOME");
	    
	    File fabricateXml = new File("fabricate.xml");
	    if (!fabricateXml.exists()) {
	      File projectXml = new File("project.xml");
	      if (!projectXml.exists()) {
	        System.out.println("-cp " + fabHome + "/lib/*.jar");
	        return;
	      }
	    }
	    OUT.println("-cp " + fabHome + "/lib/*.jar");
	  }
	  
	}
}
