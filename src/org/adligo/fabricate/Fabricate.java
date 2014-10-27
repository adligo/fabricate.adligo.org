package org.adligo.fabricate;

import org.adligo.fabricate.build.run.TaskManager;
import org.adligo.fabricate.common.FabricateXmlDiscovery;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


public class Fabricate {
  private static PrintStream OUT = System.out;
  
  
  public static final void main(String [] args) {
    FabricateXmlDiscovery discovery = new FabricateXmlDiscovery();
    
    if (!discovery.hasFabricateXml()) {
      OUT.println("Fabricate did not discover a fabricate.xml or project.xml.");
      return;
    }
    String fabricateXmlPath = discovery.getFabricateXmlPath();
    String fabricateDir = fabricateXmlPath.substring(0,  fabricateXmlPath.length() - 13);
    File runMarker = new File(fabricateDir + File.separator + "run.marker");
    if (runMarker.exists()) {
      OUT.println("Fabricate appears to already be running " + System.lineSeparator() +
            "(run.marker is in the same directory as fabricate.xml).");
      return;
    }
    
    Map<String,String> argMap = parseArgs(args);
    Set<Entry<String,String>> entries = argMap.entrySet();
    for (Entry<String,String> e: entries) {
      OUT.println(e.getKey() + " = " + e.getValue());
    }
    FileOutputStream fos = null;
    try {
      if (!runMarker.createNewFile()) {
        OUT.println("There was a problem creating run.marker in the directory with fabricate.xml.");
        return;
      }
      OUT.println("Fabricateing...");
      runMarker.deleteOnExit();
      
      
      fos = new FileOutputStream(runMarker);
      String start = argMap.get("start");
      fos.write(start.getBytes("UTF-8"));
      
      File result = new File(fabricateDir + File.separator + "result.xml");
      if (result.exists()) {
        Files.delete(Paths.get(result.getPath()));
      }
    } catch (IOException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
      OUT.println("There was a problem creating run.marker in the directory with fabricate.xml.");
      return;
    } finally {
      try {
        fos.close();
      } catch (IOException x) {
        //do nothing
      }
    }
    
    TaskManager tm = new TaskManager(discovery, argMap);
    tm.run();
  }
  
  private static Map<String,String> parseArgs(String [] args) {
    Map<String,String> toRet = new HashMap<String,String>();
    for (int i = 0; i < args.length; i++) {
      String arg = args[i];
      int eq = arg.indexOf("=");
      if (eq != -1) {
        String key = arg.substring(0, eq);
        String value = "";
        if (arg.length() > eq + 1) {
          value = arg.substring(eq + 1, arg.length());
        } 
        toRet.put(key, value);
      } else {
        toRet.put(arg,null);
      }
    }
    return toRet;
  }
}
