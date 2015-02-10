package org.adligo.fabricate.build.run;

import org.adligo.fabricate.common.I_FabSetupStage;
import org.adligo.fabricate.common.I_FabStage;
import org.adligo.fabricate.common.I_RunContext;
import org.adligo.fabricate.common.files.I_FabFileIO;
import org.adligo.fabricate.common.files.xml_io.FabXmlFileIO;
import org.adligo.fabricate.common.files.xml_io.I_FabXmlFileIO;
import org.adligo.fabricate.common.files.xml_io.ResultIO;
import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.common.log.ThreadLocalPrintStream;
import org.adligo.fabricate.common.system.FabricateXmlDiscovery;
import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.common.system.ComputerInfoDiscovery;
import org.adligo.fabricate.models.fabricate.I_JavaSettings;
import org.adligo.fabricate.models.fabricate.JavaSettings;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.FabricateType;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.JavaType;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.ProjectGroupsType;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.StageType;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.StagesAndProjectsType;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.StagesType;
import org.adligo.fabricate.xml.io_v1.project_v1_0.FabricateProjectType;
import org.adligo.fabricate.xml.io_v1.result_v1_0.FailureType;
import org.adligo.fabricate.xml.io_v1.result_v1_0.MachineInfoType;
import org.adligo.fabricate.xml.io_v1.result_v1_0.ResultType;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;

public class StageManager {
  private final I_FabSystem sys_;
  private final I_FabFileIO files_;
  private final I_FabXmlFileIO xmlFiles_;
  private List<StageType> stages_ = new ArrayList<StageType>();
  private Map<String, Object> stageMap_ = new HashMap<String, Object>();
  private List<String> sucessfulTasks_ = new ArrayList<String>();
  private FabricateType fab_;
  private FabricateProjectType project_;
  private Exception failureException_;
  private Map<String,String> args_;
  private String initalDirPath;
  private boolean setup = false;
  private String fabricateXmlPath_;
  private String projectXmlPath_;
  private I_RunContext ctx_;
  private I_FabLog log_;
  private String failureStage_;
  private String failureProject_;
  
  public StageManager(I_FabSystem sys) {
    sys_ = sys;
    log_ = sys.getLog();
    files_ = sys.getFileIO();
    xmlFiles_ = sys.getXmlFileIO();
  }
  
  public void setup(FabricateXmlDiscovery xmlDiscovery, Map<String,String> args) {
    File file = new File(".");
    initalDirPath = file.getAbsolutePath();
    initalDirPath = initalDirPath.substring(0, initalDirPath.length() - 2);
    
    String fabricateXml = xmlDiscovery.getFabricateXmlPath();
    
    try {
      fabricateXmlPath_ = fabricateXml;
      log_.println("reading " + fabricateXml);
      fab_ = xmlFiles_.parseFabricate_v1_0(fabricateXml);
    } catch (IOException e) {
      failureException_ = e;
    }
    if (fab_ == null) {
      try {
        if (xmlDiscovery.hasProjectXml()) {
          projectXmlPath_ = xmlDiscovery.getProjectXml();
          log_.println("reading " + projectXmlPath_);
          project_ = xmlFiles_.parseProject_v1_0(projectXmlPath_);
        }
      } catch (IOException e) {
        failureException_ = e;
      }
    }
    args_ = args;
  }
  
  public void run() {
    if (failureException_ == null) {
      try {
        ProjectGroupsType projectGroups = fab_.getGroups();
        if (projectGroups != null) {
          runProjectGroups();
        } else {
          runProjects();
        }
      } catch (Exception x) {
        failureException_= x;
      }
    }
    writeResult();
  }
  
  private void runProjectGroups() {
    new Exception("TODO runProjectGroups").printStackTrace();
  }
  
  
  @SuppressWarnings("boxing")
  private void runProjects() throws Exception {
    StagesAndProjectsType stagesAndProjects = fab_.getProjectGroup();
    StagesType stagesType = stagesAndProjects.getStages();
    List<StageType> stages = stagesType.getStage();
    for (int i = 0; i < stages.size(); i++) {
      StageType stage = stages.get(i);
      String taskName = stage.getName();
      
      String className = stage.getClazz();
      Class<?> clazz = Class.forName(className);
      Object instance = clazz.newInstance();
      stages_.add(stage);
      stageMap_.put(taskName, instance);
    }
    
    ConcurrentExecutor concurrentExecutor = new ConcurrentExecutor();
    JavaType java = fab_.getJava();
    if (java != null) {
      Integer threads = java.getThreads();
      if (threads != null && threads >= 1) {
        concurrentExecutor.setThreads(threads);
      }
    }
    
    for (StageType stage: stages_) {
      String stageName = stage.getName();
      Boolean optional = stage.isOptional();
      if (optional == null) {
        optional = Boolean.FALSE;
      }
      boolean execute = true;
      if (optional) {
        execute = false;
        if (args_.containsKey(stageName)) {
          execute = true;
        }
      }
      if (execute) {
        if (ctx_ == null) {
          log_.println("Starting stage " + stageName);
        } else {
          if (log_.isLogEnabled(StageManager.class)) {
            log_.println("Starting stage " + stageName);
          }
        }
        Object obj = stageMap_.get(stageName);
        if (!setup) {
          I_FabSetupStage setupStage = (I_FabSetupStage) obj;
          setupStage.setFabricate(fab_);
          setupStage.setInitalDirPath(initalDirPath);
          setupStage.setProject(project_);
          setupStage.setFabricateXmlPath(fabricateXmlPath_);
          setupStage.setProjectXmlPath(projectXmlPath_);
          ctx_ = setupStage.setup(args_);
          log_ = ctx_.getLog();
          setup = true;
          sucessfulTasks_.add(stageName);
        } else {
          
          I_FabStage fabTask = (I_FabStage) obj;
          fabTask.setStageName(stageName);
          fabTask.setup(ctx_);
          
          if (fabTask.isConcurrent()) {
            concurrentExecutor.setTask(fabTask);
            if (log_.isLogEnabled(StageManager.class)) {
              log_.println("Starting concurrent execution with " + 
                  concurrentExecutor.getThreads() + " threads");
            }
            concurrentExecutor.execute();
            concurrentExecutor.waitUntilFinished();
            
          } else {
            //run it on this thread
            fabTask.run();
          }
          if (fabTask.hadException()) {
            if (log_.isLogEnabled(StageManager.class)) {
              log_.println("Finished stage " + stageName + " with an exception.");
            }
            throw fabTask.getException();
          } else {
            if (log_.isLogEnabled(StageManager.class)) {
              log_.println("Finished stage " + stageName + " succesfuly");
            }
          }
        }
      }
    }
  }
  
  @SuppressWarnings("boxing")
  private void writeResult() {
    File resultFile = null;
    if (ctx_ != null) {
      try {
        String outputPath = ctx_.getOutputPath();
        resultFile = new File(outputPath + File.separator + "result.xml");
        Files.createFile(Paths.get(resultFile.getPath()), new FileAttribute[]{});
      } catch (IOException x) {
        //do nothing
      }
    }
    if (resultFile == null) {
      try {
        String fabFolder = fabricateXmlPath_.substring(0, fabricateXmlPath_.length() - 14);
        resultFile = new File(fabFolder + File.separator + "result.xml");
        Files.createFile(Paths.get(resultFile.getPath()), new FileAttribute[]{});
      } catch (IOException x) {
        x.printStackTrace();
      }
    }
    if (log_ != null) {
      if (log_.isLogEnabled(StageManager.class)) {
        log_.println("Writing " + resultFile.getAbsolutePath());
      }
    } else {
      log_.println("Writing " + resultFile.getAbsolutePath());
    }
    ResultType result = new ResultType();
    File fabricatePath = new File(fabricateXmlPath_.substring(0, fabricateXmlPath_.length() - 14));
    
    
    result.setName(fabricatePath.getName());
    String os = ComputerInfoDiscovery.getOperatingSystem();
    result.setOs(os);
    String osVersion = ComputerInfoDiscovery.getOperatingSystemVersion(sys_, os);
    result.setOsVersion(osVersion);
    if (failureException_ == null) {
      result.setSuccessful(true);
    } else {
      result.setSuccessful(false);
      FailureType failure = new FailureType();
      failure.setStage(failureStage_);
      failure.setProject(failureProject_);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      PrintStream ps = new PrintStream(baos);
      failureException_.printStackTrace(ps);
      Throwable t = failureException_.getCause();
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
    I_JavaSettings fh = new JavaSettings(fab_.getJava());
    machine.setRam(fh.getXms());
    String[] cpu = ComputerInfoDiscovery.getCpuInfo(sys_, os);
    machine.setCpuName(cpu[0]);
    machine.setCpuSpeed(cpu[1]);
    String jv = ComputerInfoDiscovery.getJavaVersion();
    machine.setJavaVersion(jv);
    result.setMachine(machine);
    
    
    
    long end = System.currentTimeMillis();
    String startString = args_.get("start");
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
      
    try { 
      ResultIO.write(result, resultFile.getAbsolutePath());
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } 
    if (result.isSuccessful()) {
      log_.println("Fabrication Successful!");
    } else {
      if (failureException_ != null) {
        log_.printTrace(failureException_);
      }
      log_.println("Fabrication Failed!");
    }
    System.exit(0);
  }
  
  public void logDuration(long duration) {
    if (duration < 1000) {
      log_.println("Duration was " + duration + " milliseconds.");
    } else if (duration > 1000 * 60) {
      double mins = duration;
      double divisor = 1000 * 60;
      mins = mins / divisor;
      BigDecimal bd = new BigDecimal(mins);
      bd = bd.setScale(2, RoundingMode.HALF_UP);
      
      log_.println("Duration was " + bd.toPlainString() + " minutes.");
    } else {
      double secs = duration;
      double divisor = 1000;
      secs = secs / divisor;
      BigDecimal bd = new BigDecimal(secs);
      bd = bd.setScale(2, RoundingMode.HALF_UP);
      String message = "Duration was " + bd.toPlainString() + " seconds.";
      log_.println(message);
    }
  }
}
