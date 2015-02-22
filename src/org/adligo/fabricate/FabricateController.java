package org.adligo.fabricate;

import org.adligo.fabricate.build.run.StageManager;
import org.adligo.fabricate.common.files.I_FabFileIO;
import org.adligo.fabricate.common.files.xml_io.I_FabXmlFileIO;
import org.adligo.fabricate.common.i18n.I_CommandLineConstants;
import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.i18n.I_SystemMessages;
import org.adligo.fabricate.common.log.FabFileLog;
import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.common.system.CommandLineArgs;
import org.adligo.fabricate.common.system.ComputerInfoDiscovery;
import org.adligo.fabricate.common.system.FabSystem;
import org.adligo.fabricate.common.system.FabSystemSetup;
import org.adligo.fabricate.common.system.FabricateXmlDiscovery;
import org.adligo.fabricate.managers.CommandManager;
import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.fabricate.Fabricate;
import org.adligo.fabricate.models.fabricate.FabricateMutant;
import org.adligo.fabricate.models.fabricate.I_JavaSettings;
import org.adligo.fabricate.models.fabricate.JavaSettings;
import org.adligo.fabricate.routines.I_ProjectProcessor;
import org.adligo.fabricate.routines.RoutineFabricateFactory;
import org.adligo.fabricate.xml.io_v1.common_v1_0.RoutineParentType;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.FabricateType;
import org.adligo.fabricate.xml.io_v1.result_v1_0.FailureType;
import org.adligo.fabricate.xml.io_v1.result_v1_0.MachineInfoType;
import org.adligo.fabricate.xml.io_v1.result_v1_0.ResultType;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
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
  private Throwable failureThrowable_;
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
    discovery_ = new FabricateXmlDiscovery(sys);
    
    if (!discovery_.hasFabricateXml()) {
      log_.println(sysMessages_.getExceptionNoFabricateXmlOrProjectXmlFound());
      return;
    } 
    String fabricateXmlPath = discovery_.getFabricateXmlPath();
    String fabricateDir = fabricateXmlPath.substring(0,  fabricateXmlPath.length() - 13);
    String runMarker = fabricateDir + File.separator + "run.marker";
    if (files_.exists(runMarker)) {
      log_.println(sysMessages_.getFabricateAppearsToBeAlreadyRunning() + sys_.lineSeperator() +
          sysMessages_.getFabricateAppearsToBeAlreadyRunningPartTwo());
      return;
    } 
    
    
    FileOutputStream fos = null;
    try {
      files_.create(runMarker); 
      files_.deleteOnExit(runMarker);
      
      
      fos = new FileOutputStream(runMarker);
      String start = argMap.get("start");
      fos.write(start.getBytes("UTF-8"));
      
      
      
    } catch (IOException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
      log_.println(sysMessages_.getThereWasAProblemCreatingRunMarkerInTheFollowingDirectory() + 
          sys_.lineSeperator() + fabricateDir);
      return;
    } finally {
      try {
        fos.close();
      } catch (IOException x) {
        //do nothing
      }
    }
    
    String outputDir = "output";
    if (files_.exists(outputDir)) {
      files_.deleteRecursive(outputDir);
    }
    files_.mkdirs(outputDir);
    
    FabFileLog fileLog = null;
    if (sys.hasArg(cmdMessages_.getWriteLog(true))) {
      fileLog = new FabFileLog(outputDir + files_.getNameSeparator() + 
          "fab.log", files_);
      sys.setLogFileOutputStream(fileLog);
    }
    
    log_.println(sys_.lineSeperator() + sysMessages_.getFabricating() + 
        sys_.lineSeperator());
    
    FabricateType fabX =  xmlFiles_.parseFabricate_v1_0(fabricateXmlPath);
    fab_ = factory.create(sys_, fabX, discovery_);
    FabricateMutant fm = new FabricateMutant(fab_);
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
       
      fab_ = new Fabricate(fm);
      factory_ = new RoutineFabricateFactory(fab_, commands);
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
    
    
    writeResult();
    if (fileLog != null) {
      fileLog.close();
    }
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
    resultFile = files_.create("output" + files_.getNameSeparator() + "result.xml");
    
    ResultType result = new ResultType();
    String fabricateXmlPath_ = discovery_.getFabricateXmlDir();
    File fabricatePath = files_.instance(fabricateXmlPath_);
    
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
    String os = ComputerInfoDiscovery.getOperatingSystem();
    result.setOs(os);
    String osVersion = ComputerInfoDiscovery.getOperatingSystemVersion(sys_, os);
    result.setOsVersion(osVersion);
    if (failureThrowable_ == null) {
      result.setSuccessful(true);
    } else {
      result.setSuccessful(false);
      FailureType failure = new FailureType();
      //failure.setStage(failureStage_);
      //failure.setProject(failureProject_);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      PrintStream ps = new PrintStream(baos);
      failureThrowable_.printStackTrace(ps);
      Throwable t = failureThrowable_.getCause();
      while (t != null) {
        t.printStackTrace(ps);
        t = t.getCause();
      }
      String stackText = new String(baos.toByteArray());
      failure.setDetail(stackText);
      result.setFailure(failure);
      
    }
    String hostName = ComputerInfoDiscovery.getHostname();
    
    MachineInfoType machine = new MachineInfoType();
    machine.setHostname(hostName);
    machine.setProcessors("" + Runtime.getRuntime().availableProcessors());
    
    machine.setRam(fab_.getXms());
    String[] cpu = ComputerInfoDiscovery.getCpuInfo(sys_, os);
    machine.setCpuName(cpu[0]);
    machine.setCpuSpeed(cpu[1]);
    String jv = ComputerInfoDiscovery.getJavaVersion();
    machine.setJavaVersion(jv);
    result.setMachine(machine);
    
    
    
    long end = System.currentTimeMillis();
    String startString = sys_.getArgValue("start");
    long start = new Long(startString);
    long dur = end - start;
    if (log_ != null) {
      if (log_.isLogEnabled(StageManager.class)) {
         logDuration(dur);
      }
    } else {
      logDuration(dur);
    }
    
    try {
      DatatypeFactory df = DatatypeFactory.newInstance();
      Duration duration = df.newDuration(dur);
      result.setDuration(duration);
    } catch (DatatypeConfigurationException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
      return;
    }
      
    xmlFiles_.writeResult_v1_0(resultFile.getAbsolutePath(), result);

    if (result.isSuccessful()) {
      log_.println(sysMessages_.getFabricationSuccessful());
    } else {
      if (failureThrowable_ != null) {
        log_.printTrace(failureThrowable_);
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
