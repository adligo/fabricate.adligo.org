package org.adligo.fabricate;

import org.adligo.fabricate.build.run.StageManager;
import org.adligo.fabricate.common.files.FabFileIO;
import org.adligo.fabricate.common.files.I_FabFileIO;
import org.adligo.fabricate.common.log.ThreadLocalPrintStream;
import org.adligo.fabricate.common.system.CommandLineArgs;
import org.adligo.fabricate.common.system.FabSystem;
import org.adligo.fabricate.common.system.FabricateXmlDiscovery;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


public class Fabricate {
  
  @SuppressWarnings("unused")
  public static final void main(String [] args) {
    new Fabricate(args);
  }
  
  public Fabricate(String [] args) {
    Map<String,String> argMap = CommandLineArgs.parseArgs(args);
    if (argMap.containsKey("debug")) {
      Set<Entry<String,String>> entries = argMap.entrySet();
      for (Entry<String,String> e: entries) {
        ThreadLocalPrintStream.println(e.getKey() + " = " + e.getValue());
      }
    }
    FabSystem sys = new FabSystem();
    sys.setDebug(argMap.containsKey("debug"));
    FabricateXmlDiscovery discovery = new FabricateXmlDiscovery(sys);
    
    if (!discovery.hasFabricateXml()) {
      ThreadLocalPrintStream.println("Fabricate did not discover a fabricate.xml or project.xml.");
      return;
    }
    String fabricateXmlPath = discovery.getFabricateXmlPath();
    String fabricateDir = fabricateXmlPath.substring(0,  fabricateXmlPath.length() - 13);
    File runMarker = new File(fabricateDir + File.separator + "run.marker");
    if (runMarker.exists()) {
      ThreadLocalPrintStream.println("Fabricate appears to already be running " + System.lineSeparator() +
            "(run.marker is in the same directory as fabricate.xml).");
      return;
    }
    
    
    FileOutputStream fos = null;
    try {
      if (!runMarker.createNewFile()) {
        ThreadLocalPrintStream.println("There was a problem creating run.marker in the directory with fabricate.xml.");
        return;
      }
      ThreadLocalPrintStream.println("Fabricateing...");
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
      ThreadLocalPrintStream.println("There was a problem creating run.marker in the directory with fabricate.xml.");
      return;
    } finally {
      try {
        fos.close();
      } catch (IOException x) {
        //do nothing
      }
    }
    
    StageManager tm = new StageManager(sys);
    tm.setup(discovery, argMap);
    tm.run();
  }
  
}
