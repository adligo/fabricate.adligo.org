package org.adligo.fabricate;

import org.adligo.fabricate.build.run.StageManager;
import org.adligo.fabricate.common.ArgsParser;
import org.adligo.fabricate.common.FabricateXmlDiscovery;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


public class Fabricate {
  private static PrintStream OUT = System.out;
  
  
  public static final void main(String [] args) {
    Map<String,String> argMap = ArgsParser.parseArgs(args);
    if (argMap.containsKey("debug")) {
      Set<Entry<String,String>> entries = argMap.entrySet();
      for (Entry<String,String> e: entries) {
        OUT.println(e.getKey() + " = " + e.getValue());
      }
    }
    FabricateXmlDiscovery discovery = new FabricateXmlDiscovery(argMap.containsKey("debug"));
    
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
    
    StageManager tm = new StageManager(discovery, argMap);
    tm.run();
  }
  
}
