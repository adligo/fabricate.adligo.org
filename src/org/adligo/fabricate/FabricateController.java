package org.adligo.fabricate;

import org.adligo.fabricate.common.files.I_FabFileIO;
import org.adligo.fabricate.common.files.xml_io.I_FabXmlFileIO;
import org.adligo.fabricate.common.i18n.I_CommandLineConstants;
import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.i18n.I_SystemMessages;
import org.adligo.fabricate.common.log.I_FabFileLog;
import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.common.system.CommandLineArgs;
import org.adligo.fabricate.common.system.FabSystem;
import org.adligo.fabricate.common.system.FabSystemSetup;
import org.adligo.fabricate.common.system.FabricateXmlDiscovery;
import org.adligo.fabricate.managers.CommandManager;
import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.fabricate.Fabricate;
import org.adligo.fabricate.models.fabricate.FabricateMutant;
import org.adligo.fabricate.routines.I_ProjectProcessor;
import org.adligo.fabricate.routines.RoutineFabricateFactory;
import org.adligo.fabricate.xml.io_v1.common_v1_0.RoutineParentType;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.FabricateType;
import org.adligo.fabricate.xml.io_v1.result_v1_0.FailureType;
import org.adligo.fabricate.xml.io_v1.result_v1_0.MachineInfoType;
import org.adligo.fabricate.xml.io_v1.result_v1_0.ResultType;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;


public class FabricateController {
  private final FabSystem sys_;
  private final I_CommandLineConstants cmdMessages_;
  private final I_FabricateConstants constants_;
  private final I_SystemMessages sysMessages_;
  private final I_FabLog log_;
  private final I_FabFileIO files_;
  private final I_FabXmlFileIO xmlFiles_;
  private final FabricateXmlDiscovery discovery_;
  private RoutineFabricateFactory factory_;
  private boolean commands_;
  /**
   * The first throwable thrown by either
   * processing of a command or a build or share stage.
   */
  private FailureType failure_;
  private Fabricate fab_;
  
  @SuppressWarnings("unused")
  public static final void main(String [] args) throws Exception {
    new FabricateController(new FabSystem(), args, new FabricateFactory());
  }
  
  public FabricateController(FabSystem sys, String [] args, FabricateFactory factory) 
      throws ClassNotFoundException, FabricationRoutineCreationException, IOException {
    Map<String,String> argMap = CommandLineArgs.parseArgs(args);
    sys_ = sys;
    files_ = sys.getFileIO();
    xmlFiles_ = sys.getXmlFileIO();
    constants_ = sys_.getConstants();
    sysMessages_ = constants_.getSystemMessages();
    cmdMessages_ = constants_.getCommandLineConstants();
    FabSystemSetup.setup(sys, args);
    
    log_ = sys.getLog();
    discovery_ = factory.createDiscovery(sys);
    
    if (!discovery_.hasFabricateXml()) {
      log_.println(sysMessages_.getExceptionNoFabricateXmlOrProjectXmlFound());
      return;
    } 
    String fabricateDir = discovery_.getFabricateXmlDir();
    String runMarker = fabricateDir + files_.getNameSeparator() + "run.marker";
    if (files_.exists(runMarker)) {
      log_.println(sysMessages_.getFabricateAppearsToBeAlreadyRunning() + sys_.lineSeperator() +
          sysMessages_.getFabricateAppearsToBeAlreadyRunningPartTwo() + sys_.lineSeperator() +
          fabricateDir);
      return;
    } 
    
    OutputStream fos = null;
    try {
      files_.create(runMarker); 
      files_.deleteOnExit(runMarker);
      
      fos = files_.newFileOutputStream(runMarker);
      String start = argMap.get("start");
      fos.write(start.getBytes("UTF-8"));
      
    } catch (IOException e1) {
      log_.printTrace(e1);
      log_.println(sysMessages_.getThereWasAProblemCreatingRunMarkerInTheFollowingDirectory() + 
          sys_.lineSeperator() + fabricateDir);
      return;
    } finally {
      if (fos != null) {
        try {
          fos.close();
        } catch (IOException x) {
          //do nothing
        }
      }
    }
    
    String outputDir = fabricateDir + files_.getNameSeparator() + "output";
    if (files_.exists(outputDir)) {
      files_.deleteRecursive(outputDir);
    }
    if (!files_.mkdirs(outputDir)) {
      File dir = files_.instance(outputDir);
      String absPath = dir.getAbsolutePath();
      log_.println(sysMessages_.getThereWasAProblemCreatingTheFollowingDirectory() + sys_.lineSeperator() +
          absPath);
      return;
    }
    
    I_FabFileLog fileLog = null;
    String writeLogArg = cmdMessages_.getWriteLog(true);
    if (sys.hasArg(writeLogArg)) {
      fileLog = sys_.newFabFileLog(outputDir + files_.getNameSeparator() + 
          "fab.log");
      sys.setLogFile(fileLog);
    }
    
    log_.println(sys_.lineSeperator() + sysMessages_.getFabricating() + 
        sys_.lineSeperator());
    
    List<String> argCommands = sys_.getArgValues(cmdMessages_.getCommand());
    if (!addRoutinesToFabricate(factory, argCommands)) {
      return;
    }
    
    if (requiresProjects()) {
      //TODO add projects manager code
    }
    
    if (commands_) {
      CommandManager manager = factory.createCommandManager(argCommands, sys_, factory_);
      failure_ = manager.processCommands();
    } else {
      //TODO add fabrication manager code
    }
    
    writeResult();
    if (fileLog != null) {
      fileLog.close();
    }
  }

  public boolean addRoutinesToFabricate(FabricateFactory factory, List<String> argCommands)
      throws IOException, ClassNotFoundException {
    String fabricateXmlPath = discovery_.getFabricateXmlPath();
    FabricateType fabX =  xmlFiles_.parseFabricate_v1_0(fabricateXmlPath);
    fab_ = factory.create(sys_, fabX, discovery_);
    FabricateMutant fm = factory.createMutant(fab_);
    List<RoutineParentType> traits =  fabX.getTrait();
    
    commands_ = true;
    try {
      if (traits != null) {
        fm.addTraits(traits);
      }
      
      if (argCommands.size() >= 1) {
        fm.addCommands(fabX);
      } else {
        fm.addStages(fabX);
        commands_ = false;
      }
       
      fab_ = factory.create(fm);
      factory_ = factory.createRoutineFabricateFactory(fab_, commands_);
    } catch (ClassNotFoundException x) {
      String message = sysMessages_.getUnableToLoadTheFollowingClass() + 
        sys_.lineSeperator() + x.getMessage();
      log_.println(message);
      log_.printTrace(x);
      return false;
    }
    return true;
  }
  
  private boolean requiresProjects() throws FabricationRoutineCreationException {
    if (factory_.anyAssignableTo(I_ProjectProcessor.class)) {
      return true;
    }
    return false;
  }
  
  @SuppressWarnings("boxing")
  private void writeResult() throws IOException {
    File resultFile = null;
    String fabricateXmlPath = discovery_.getFabricateXmlDir();
    resultFile = files_.create(fabricateXmlPath + files_.getNameSeparator() + 
          "output" + files_.getNameSeparator() + "result.xml");
    
    ResultType result = new ResultType();
    
    File fabricatePath = files_.instance(fabricateXmlPath);
    
    String passable = sys_.getArgValue(CommandLineArgs.PASSABLE_ARGS_);
    String [] array = CommandLineArgs.fromPassableString(passable);
    StringBuilder sb = new StringBuilder();
    sb.append("fab");
    for (int i = 0; i < array.length; i++) {
      sb.append(" ");
      sb.append(array[i]);
    }
    result.setCommandLine(sb.toString());
    
    result.setName(fabricatePath.getName());
    String os = sys_.getOperatingSystem();
    result.setOs(os);
    String osVersion = sys_.getOperatingSystemVersion(os);
    result.setOsVersion(osVersion);
    if (failure_ == null) {
      result.setSuccessful(true);
    } else {
      result.setSuccessful(false);
      result.setFailure(failure_);
    }
    String hostName = sys_.getHostname();
    
    MachineInfoType machine = new MachineInfoType();
    machine.setHostname(hostName);
    machine.setProcessors(sys_.getAvailableProcessors());
    
    machine.setRam(fab_.getXms());
    String[] cpu = sys_.getCpuInfo(os);
    machine.setCpuName(cpu[0]);
    machine.setCpuSpeed(cpu[1]);
    String jv = sys_.getJavaVersion();
    machine.setJavaVersion(jv);
    result.setMachine(machine);
    
    long end = sys_.getCurrentTime();
    String startString = sys_.getArgValue("start");
    long start = new Long(startString);
    long dur = end - start;
    if (log_ != null) {
      if (log_.isLogEnabled(FabricateController.class)) {
         logDuration(dur);
      }
    } else {
      logDuration(dur);
    }
    
    try {
      DatatypeFactory df = sys_.newDatatypeFactory();
      Duration duration = df.newDuration(dur);
      result.setDuration(duration);
    } catch (DatatypeConfigurationException e1) {
      log_.printTrace(e1);
      return;
    }
      
    xmlFiles_.writeResult_v1_0(resultFile.getAbsolutePath(), result);

    if (result.isSuccessful()) {
      log_.println(sysMessages_.getFabricationSuccessful());
    } else {
      if (failure_ != null) {
        log_.println(failure_.getDetail());
      }
      log_.println(sysMessages_.getFabricationFailed());
    }
  }
  
  public void logDuration(long duration) {
    if (duration < 1000) {
      String message = sysMessages_.getDurationWasXMilliseconds();
      message = message.replaceFirst("<X/>", "" + duration);
      log_.println(message);
    } else if (duration > 1000 * 60) {
      double mins = duration;
      double divisor = 1000 * 60;
      mins = mins / divisor;
      BigDecimal bd = new BigDecimal(mins);
      bd = bd.setScale(2, RoundingMode.HALF_UP);
      
      String message = sysMessages_.getDurationWasXMinutes();
      message = message.replaceFirst("<X/>", "" + bd.toPlainString());
      log_.println(message);
    } else {
      double secs = duration;
      double divisor = 1000;
      secs = secs / divisor;
      BigDecimal bd = new BigDecimal(secs);
      bd = bd.setScale(2, RoundingMode.HALF_UP);
      String message = sysMessages_.getDurationWasXSeconds();
      message = message.replaceFirst("<X/>", "" + bd.toPlainString());
      log_.println(message);
    }
  }
}
