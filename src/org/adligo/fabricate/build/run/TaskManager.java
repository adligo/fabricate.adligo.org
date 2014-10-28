package org.adligo.fabricate.build.run;

import org.adligo.fabricate.common.FabricateHelper;
import org.adligo.fabricate.common.FabricateXmlDiscovery;
import org.adligo.fabricate.common.I_FabContext;
import org.adligo.fabricate.common.I_FabSetupTask;
import org.adligo.fabricate.common.I_FabTask;
import org.adligo.fabricate.common.SystemHelper;
import org.adligo.fabricate.xml.io.FabricateType;
import org.adligo.fabricate.xml.io.JavaType;
import org.adligo.fabricate.xml.io.ProjectGroupsType;
import org.adligo.fabricate.xml.io.StagesAndProjectsType;
import org.adligo.fabricate.xml.io.StagesType;
import org.adligo.fabricate.xml.io.TaskType;
import org.adligo.fabricate.xml.io.project.FabricateProjectType;
import org.adligo.fabricate.xml.io.result.FailureType;
import org.adligo.fabricate.xml.io.result.MachineInfoType;
import org.adligo.fabricate.xml.io.result.ResultType;
import org.adligo.fabricate.xml_io.FabricateIO;
import org.adligo.fabricate.xml_io.ProjectIO;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.namespace.QName;

public class TaskManager {
  private static PrintStream OUT = System.out;
  private List<TaskType> tasks_ = new ArrayList<TaskType>();
  private Map<String, Object> taskMap_ = new HashMap<String, Object>();
  private List<String> sucessfulTasks_ = new ArrayList<String>();
  private String currentTask_;
  private FabricateType fab_;
  private FabricateProjectType project_;
  private Exception failureException_;
  private Map<String,String> args_;
  private String initalDirPath;
  private boolean setup = false;
  private String fabricateXmlPath_;
  private String projectXmlPath_;
  private I_FabContext ctx_;
  private String failureStage_;
  private String failureProject_;
  
  public TaskManager(FabricateXmlDiscovery xmlDiscovery, Map<String,String> args) {
    File file = new File(".");
    initalDirPath = file.getAbsolutePath();
    initalDirPath = initalDirPath.substring(0, initalDirPath.length() - 2);
    
    String fabricateXml = xmlDiscovery.getFabricateXmlPath();
    try {
      fabricateXmlPath_ = fabricateXml;
      fab_ = FabricateIO.parse(new File(fabricateXml));
      if (xmlDiscovery.hasProjectXml()) {
        projectXmlPath_ = xmlDiscovery.getProjectXml();
        project_ = ProjectIO.parse(new File(projectXmlPath_));
      }
    } catch (IOException e) {
      failureException_ = e;
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
    StagesType stages = stagesAndProjects.getStages();
    List<TaskType> tasks = stages.getStage();
    for (int i = 0; i < tasks.size(); i++) {
      TaskType task = tasks.get(i);
      String taskName = task.getName();
      
      String className = task.getClazz();
      Class<?> clazz = Class.forName(className);
      Object instance = clazz.newInstance();
      tasks_.add(task);
      taskMap_.put(taskName, instance);
    }
    
    ConcurrentExecutor concurrentExecutor = new ConcurrentExecutor();
    JavaType java = fab_.getJava();
    if (java != null) {
      Integer threads = java.getThreads();
      if (threads != null && threads >= 1) {
        concurrentExecutor.setThreads(threads);
      }
    }
    
    for (TaskType task: tasks_) {
      String taskName = task.getName();
      Boolean optional = task.isOptional();
      if (optional == null) {
        optional = Boolean.FALSE;
      }
      boolean execute = true;
      if (optional) {
        execute = false;
        if (args_.containsKey(taskName)) {
          execute = true;
        }
      }
      if (execute) {
        if (ctx_ == null) {
          OUT.println("Starting stage " + taskName);
        } else {
          if (ctx_.isLogEnabled(TaskManager.class)) {
            OUT.println("Starting stage " + taskName);
          }
        }
        Object obj = taskMap_.get(taskName);
        if (!setup) {
          I_FabSetupTask setupTask = (I_FabSetupTask) obj;
          setupTask.setFabricate(fab_);
          setupTask.setInitalDirPath(initalDirPath);
          setupTask.setProject(project_);
          setupTask.setFabricateXmlPath(fabricateXmlPath_);
          setupTask.setProjectXmlPath(projectXmlPath_);
          ctx_ = setupTask.setup(args_);
          setup = true;
          sucessfulTasks_.add(taskName);
        } else {
          
          I_FabTask fabTask = (I_FabTask) obj;
          fabTask.setup(ctx_);
          if (fabTask.isConcurrent()) {
            concurrentExecutor.setTask(fabTask);
            if (ctx_.isLogEnabled(TaskManager.class)) {
              OUT.println("Starting concurrent execution with " + concurrentExecutor.getThreads() + " threads");
            }
            concurrentExecutor.execute();
            concurrentExecutor.waitUntilFinished();
            
          } else {
            //run it on this thread
            fabTask.run();
          }
          if (fabTask.hadException()) {
            throw fabTask.getException();
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
    if (ctx_ != null) {
      if (ctx_.isLogEnabled(TaskManager.class)) {
        OUT.println("Writing " + resultFile.getAbsolutePath());
      }
    } else {
      OUT.println("Writing " + resultFile.getAbsolutePath());
    }
    ResultType result = new ResultType();
    File fabricatePath = new File(fabricateXmlPath_.substring(0, fabricateXmlPath_.length() - 14));
    
    
    result.setName(fabricatePath.getName());
    String os = SystemHelper.getOperatingSystem();
    result.setOs(os);
    String osVersion = SystemHelper.getOperatingSystemVersion(os);
    result.setOsVersion(osVersion);
    if (failureException_ == null) {
      result.setSuccessful(true);
      if (ctx_.isLogEnabled(TaskManager.class)) {
        OUT.println("Fabrication Successful!");
      }
    } else {
      result.setSuccessful(false);
      FailureType failure = new FailureType();
      failure.setStage(failureStage_);
      failure.setProject(failureProject_);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      PrintStream ps = new PrintStream(baos);
      failureException_.printStackTrace(ps);
      String stackText = new String(baos.toByteArray());
      failure.setDetail(stackText);
      result.setFailure(failure);
      if (ctx_.isLogEnabled(TaskManager.class)) {
        failureException_.printStackTrace(OUT);
        OUT.println("Fabrication Failed!");
      }
    }
    String hostName = SystemHelper.getHostname();
    
    MachineInfoType machine = new MachineInfoType();
    machine.setHostname(hostName);
    machine.setProcessors("" + Runtime.getRuntime().availableProcessors());
    FabricateHelper fh = new FabricateHelper(fab_);
    machine.setRam(fh.getXms());
    String[] cpu = SystemHelper.getCpuInfo(os);
    machine.setCpuName(cpu[0]);
    machine.setCpuSpeed(cpu[1]);
    String jv = SystemHelper.getJavaVersion();
    machine.setJavaVersion(jv);
    result.setMachine(machine);
    
    
    
    long end = System.currentTimeMillis();
    String startString = args_.get("start");
    long start = new Long(startString);
    long dur = end - start;
    if (ctx_ != null) {
      if (ctx_.isLogEnabled(TaskManager.class)) {
         logDuration(dur);
      }
    } else {
      logDuration(dur);
    }
    try {
      DatatypeFactory df = DatatypeFactory.newInstance();
      Duration duration = df.newDuration(dur);
      result.setDuration(duration);
      
      JAXBContext jaxbContext = JAXBContext.newInstance("org.adligo.fabricate.xml.io.result");
      Marshaller marshaller = jaxbContext.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
      marshaller.marshal(new JAXBElement<ResultType>(
          new QName("http://www.adligo.org/fabricate/xml/io/result",
          "result"), ResultType.class, result), resultFile);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } 
    
  }
  
  public void logDuration(long duration) {
    if (duration < 1000) {
      OUT.println("Duration was " + duration + " milliseconds.");
    } else if (duration < 1000 * 60) {
      double secs = duration;
      secs = secs / 1000;
      int secsInt = (int) secs;
      OUT.println("Duration was " + secsInt + " seconds.");
    } else if (duration < 1000 * 60 * 60) {
      double mins = duration;
      mins = mins / 1000 * 60;
      int minsInt = (int) mins;
      OUT.println("Duration was " + minsInt + " minutes.");
    }
  }
}
