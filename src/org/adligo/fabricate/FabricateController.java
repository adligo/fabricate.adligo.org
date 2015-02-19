package org.adligo.fabricate;

import org.adligo.fabricate.common.files.xml_io.I_FabXmlFileIO;
import org.adligo.fabricate.common.i18n.I_CommandLineConstants;
import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.i18n.I_SystemMessages;
import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.common.system.CommandLineArgs;
import org.adligo.fabricate.common.system.FabSystem;
import org.adligo.fabricate.common.system.FabSystemSetup;
import org.adligo.fabricate.common.system.FabricateXmlDiscovery;
import org.adligo.fabricate.managers.CommandManager;
import org.adligo.fabricate.models.fabricate.Fabricate;
import org.adligo.fabricate.models.fabricate.FabricateMutant;
import org.adligo.fabricate.routines.FabricationRoutineCreationException;
import org.adligo.fabricate.routines.I_ProjectProcessor;
import org.adligo.fabricate.routines.RoutineFabricateFactory;
import org.adligo.fabricate.routines.RoutineFactory;
import org.adligo.fabricate.xml.io_v1.common_v1_0.RoutineParentType;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.FabricateType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;


public class FabricateController {
  private final FabSystem sys_;
  private final I_CommandLineConstants cmdMessages_;
  private final I_FabricateConstants constants_;
  private final I_SystemMessages sysMessages_;
  private final I_FabLog log_;
  //private final I_FabFileIO files_;
  private final I_FabXmlFileIO xmlFiles_;
  private RoutineFabricateFactory factory_;
  
  @SuppressWarnings("unused")
  public static final void main(String [] args) throws Exception {
    new FabricateController(new FabSystem(), args, new FabricateFactory());
  }
  
  public FabricateController(FabSystem sys, String [] args, FabricateFactory factory) 
      throws ClassNotFoundException, FabricationRoutineCreationException, IOException {
    Map<String,String> argMap = CommandLineArgs.parseArgs(args);
    sys_ = sys;
    //files_ = sys.getFileIO();
    xmlFiles_ = sys.getXmlFileIO();
    constants_ = sys_.getConstants();
    sysMessages_ = constants_.getSystemMessages();
    cmdMessages_ = constants_.getCommandLineConstants();
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
    
    FabricateType fabX =  xmlFiles_.parseFabricate_v1_0(fabricateXmlPath);
    Fabricate fab = factory.create(sys_, fabX, discovery);
    FabricateMutant fm = new FabricateMutant(fab);
    List<RoutineParentType> traits =  fabX.getTrait();
    List<String> argCommands;
    boolean commands = true;
    try {
      if (traits != null) {
        fm.addTraits(traits);
      }
      argCommands = sys_.getArgValues(cmdMessages_.getCommand());
      
      if (argCommands != null) {
        fm.addCommands(fabX);
      } else {
        fm.addStages(fabX);
        commands = false;
      }
       
      fab = new Fabricate(fm);
      factory_ = new RoutineFabricateFactory(fab, commands);
    } catch (ClassNotFoundException x) {
      String message = sysMessages_.getUnableToLoadTheFollowingClass() + 
        sys_.lineSeperator() + x.getMessage();
      log_.println(message);
      log_.printTrace(x);
      return;
    }
    
    if (requiresProjects()) {
      
    }
    
    if (commands) {
      CommandManager presenter = new CommandManager(argCommands, sys_, factory_);
      presenter.processCommands();
    } else {
      
    }
    log_.println("not yet working todo: ");
    log_.println("add projects manager code");
    log_.println("add fabrication manager code");
    log_.println("add classpath to eclipse");
    log_.println("add fabricate4tests4j");
    log_.println("add fabricate4junit");
    
  }
  
  private boolean requiresProjects() throws FabricationRoutineCreationException {
    if (factory_.anyAssignableTo(I_ProjectProcessor.class)) {
      return true;
    }
    return false;
  }
}
