package org.adligo.fabricate.build.run;

import org.adligo.fabricate.common.FabricateHelper;
import org.adligo.fabricate.common.FabricateXmlDiscovery;
import org.adligo.fabricate.common.I_FabContext;
import org.adligo.fabricate.common.SystemHelper;
import org.adligo.fabricate.parsers.FabricateParser;
import org.adligo.fabricate.parsers.ProjectParser;
import org.adligo.fabricate.xml.io.FabricateType;
import org.adligo.fabricate.xml.io.ProjectGroupsType;
import org.adligo.fabricate.xml.io.ProjectType;
import org.adligo.fabricate.xml.io.StagesAndProjectsType;
import org.adligo.fabricate.xml.io.StagesType;
import org.adligo.fabricate.xml.io.TaskType;
import org.adligo.fabricate.xml.io.result.FailureType;
import org.adligo.fabricate.xml.io.result.MachineInfoType;
import org.adligo.fabricate.xml.io.result.ResultType;

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
  private PrintStream out = System.out;
  private List<String> tasks_ = new ArrayList<String>();
  private Map<String, Object> taskMap_ = new HashMap<String, Object>();
  private List<String> sucessfulTasks_ = new ArrayList<String>();
  private String currentTask_;
  private FabricateType fab_;
  private ProjectType project_;
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
      fab_ = FabricateParser.parse(new File(fabricateXml));
      if (xmlDiscovery.hasProjectXml()) {
        projectXmlPath_ = xmlDiscovery.getProjectXml();
        project_ = ProjectParser.parse(new File(projectXmlPath_));
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
      tasks_.add(taskName);
      taskMap_.put(taskName, instance);
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
    out.println("Writing " + resultFile.getAbsolutePath());
    ResultType result = new ResultType();
    File fabricatePath = new File(fabricateXmlPath_.substring(0, fabricateXmlPath_.length() - 14));
    
    
    result.setName(fabricatePath.getName());
    String os = SystemHelper.getOperatingSystem();
    result.setOs(os);
    String osVersion = SystemHelper.getOperatingSystemVersion(os);
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
      String stackText = new String(baos.toByteArray());
      failure.setDetail(stackText);
      result.setFailure(failure);
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
    
    try {
      DatatypeFactory df = DatatypeFactory.newInstance();
      Duration duration = df.newDuration(dur);
      result.setDuration(duration);
      
      JAXBContext jaxbContext = JAXBContext.newInstance("org.adligo.fabricate.xml.io.result");
      Marshaller marshaller = jaxbContext.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
      marshaller.marshal(new JAXBElement<ResultType>(new QName("http://www.adligo.org/fabricate/xml/io/result",
          "result"), ResultType.class, result), resultFile);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } 
    
  }
}
