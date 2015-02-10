package org.adligo.fabricate;

import org.adligo.fabricate.build.run.StageManager;
import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.i18n.I_SystemMessages;
import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.common.system.CommandLineArgs;
import org.adligo.fabricate.common.system.FabSystem;
import org.adligo.fabricate.common.system.FabSystemSetup;
import org.adligo.fabricate.common.system.FabricateXmlDiscovery;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;


public class FabricateController {
  private final FabSystem sys_;
  private final I_FabricateConstants constants_;
  private final I_SystemMessages sysMessages_;
  private final I_FabLog log_;
  
  @SuppressWarnings("unused")
  public static final void main(String [] args) {
    new FabricateController(new FabSystem(), args, new FabricateFactory());
  }
  
  public FabricateController(FabSystem sys, String [] args, FabricateFactory factory) {
    Map<String,String> argMap = CommandLineArgs.parseArgs(args);
    sys_ = sys;
    constants_ = sys_.getConstants();
    sysMessages_ = constants_.getSystemMessages();
    FabSystemSetup.setup(sys, args);
    log_ = sys.getLog();
    FabricateXmlDiscovery discovery = new FabricateXmlDiscovery(sys);
    
    if (!discovery.hasFabricateXml()) {
      log_.println(sysMessages_.getExceptionNoFabricateXmlOrProjectXmlFound());
      return;
    } 
    String fabricateXmlPath = discovery.getFabricateXmlPath();
    String fabricateDir = fabricateXmlPath.substring(0,  fabricateXmlPath.length() - 13);
    File runMarker = new File(fabricateDir + File.separator + "run.marker");
    if (runMarker.exists()) {
      log_.println("Fabricate appears to already be running " + System.lineSeparator() +
            "(run.marker is in the same directory as fabricate.xml).");
      return;
    } 
    
    
    FileOutputStream fos = null;
    try {
      if (!runMarker.createNewFile()) {
        log_.println("There was a problem creating run.marker in the directory with fabricate.xml.");
        return;
      }

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
      log_.println("There was a problem creating run.marker in the directory with fabricate.xml.");
      return;
    } finally {
      try {
        fos.close();
      } catch (IOException x) {
        //do nothing
      }
    }
    
    log_.println(sys_.lineSeperator() + sysMessages_.getFabricating() + 
        sys_.lineSeperator());
    
    log_.println("failed todo: ");
    log_.println("add read of command arguments cmd,stages,tasks into Fabricate");
    log_.println("add control logic of what to do");
    log_.println("with read of Fabricate.xml commands or stages accordingly");
    log_.println("add command phase presenter");
    log_.println("add default commands encrypt, decrypt");
    log_.println("add obtain projects presenter");
    log_.println("add stage phase presenter");
    log_.println("add compiles presenter");
    log_.println("add classpath to eclipse");
    log_.println("add fabricate4tests4j");
    log_.println("add fabricate4junit");
    
  }
  
}
