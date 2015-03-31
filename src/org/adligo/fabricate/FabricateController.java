package org.adligo.fabricate;

import org.adligo.fabricate.common.files.I_FabFileIO;
import org.adligo.fabricate.common.files.PatternFileMatcher;
import org.adligo.fabricate.common.files.xml_io.I_FabXmlFileIO;
import org.adligo.fabricate.common.i18n.I_CommandLineConstants;
import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.i18n.I_SystemMessages;
import org.adligo.fabricate.common.log.I_FabFileLog;
import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.common.system.CommandLineArgs;
import org.adligo.fabricate.common.system.FabSystem;
import org.adligo.fabricate.common.system.FabSystemSetup;
import org.adligo.fabricate.common.system.FabricateEnvironment;
import org.adligo.fabricate.common.system.FabricateXmlDiscovery;
import org.adligo.fabricate.common.system.I_ExecutionResult;
import org.adligo.fabricate.common.system.I_Executor;
import org.adligo.fabricate.common.system.I_FailureTransport;
import org.adligo.fabricate.common.util.StringUtils;
import org.adligo.fabricate.depot.Depot;
import org.adligo.fabricate.depot.DepotContext;
import org.adligo.fabricate.java.JavaFactory;
import org.adligo.fabricate.java.ManifestParser;
import org.adligo.fabricate.managers.CommandManager;
import org.adligo.fabricate.managers.FabricationManager;
import org.adligo.fabricate.managers.ProjectsManager;
import org.adligo.fabricate.models.common.ExecutionEnvironmentMutant;
import org.adligo.fabricate.models.common.FabricationMemoryConstants;
import org.adligo.fabricate.models.common.FabricationMemoryMutant;
import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.common.I_RoutineBrief;
import org.adligo.fabricate.models.common.MemoryLock;
import org.adligo.fabricate.models.common.RoutineBriefOrigin;
import org.adligo.fabricate.models.fabricate.Fabricate;
import org.adligo.fabricate.models.fabricate.FabricateMutant;
import org.adligo.fabricate.models.project.I_Project;
import org.adligo.fabricate.repository.RepositoryManager;
import org.adligo.fabricate.routines.I_ProjectBriefsAware;
import org.adligo.fabricate.routines.I_ProjectsAware;
import org.adligo.fabricate.routines.I_RoutineBuilder;
import org.adligo.fabricate.routines.I_RoutineFabricateProcessorFactory;
import org.adligo.fabricate.routines.I_RoutinePopulatorMutant;
import org.adligo.fabricate.routines.RoutineBuilder;
import org.adligo.fabricate.routines.implicit.ImplicitRoutineFactory;
import org.adligo.fabricate.routines.implicit.ScmContext;
import org.adligo.fabricate.xml.io_v1.common_v1_0.RoutineParentType;
import org.adligo.fabricate.xml.io_v1.depot_v1_0.DepotType;
import org.adligo.fabricate.xml.io_v1.dev_v1_0.FabricateDevType;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.FabricateType;
import org.adligo.fabricate.xml.io_v1.result_v1_0.FailureType;
import org.adligo.fabricate.xml.io_v1.result_v1_0.MachineInfoType;
import org.adligo.fabricate.xml.io_v1.result_v1_0.ResultType;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;


public class FabricateController {
  private static FabricateEnvironment ENV = FabricateEnvironment.INSTANCE;
  
  public static FabricateEnvironment getENV() {
    return ENV;
  }

  public static void setENV(FabricateEnvironment eNV) {
    ENV = eNV;
  }
  
  private final FabSystem sys_;
  
  private final I_CommandLineConstants cmdMessages_;
  private final I_FabricateConstants constants_;
  private final I_SystemMessages sysMessages_;
  private final I_FabLog log_;
  private final I_FabFileIO files_;
  private final I_FabXmlFileIO xmlFiles_;
  private final FabricateFactory fabFactory_;
  private final FabricateXmlDiscovery discovery_;
  private I_RoutineFabricateProcessorFactory factory_;
  private FabricateType fabXml_;
  private boolean commands_;
  private RepositoryManager repositoryManager_;
  private I_FabFileLog fileLog_ = null;
  private FabricationMemoryMutant<Object> memory_;
  
  /**
   * The first throwable thrown by either
   * processing of a command or a build or share stage.
   */
  private I_FailureTransport failure_;
  private Fabricate fab_;
  
  @SuppressWarnings("unused")
  public static final void main(String [] args) throws Exception {
    new FabricateController(new FabSystem(), args, new FabricateFactory());
  }
  
  public FabricateController(FabSystem sys, String [] args, FabricateFactory factory) 
      throws ClassNotFoundException, IOException {
    Map<String,String> argMap = CommandLineArgs.parseArgs(args);
    sys_ = sys;
    fabFactory_ = factory;
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
    String runMarker = fabricateDir + "run.marker";
    if (files_.exists(runMarker)) {
      log_.println(sysMessages_.getFabricateAppearsToBeAlreadyRunning() + sys_.lineSeparator() +
          sysMessages_.getFabricateAppearsToBeAlreadyRunningPartTwo() + sys_.lineSeparator() +
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
          sys_.lineSeparator() + fabricateDir);
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
    
    String outputDir = fabricateDir +  "output";
    if (files_.exists(outputDir)) {
      files_.deleteRecursive(outputDir);
    }
    if (!files_.mkdirs(outputDir)) {
      File dir = files_.instance(outputDir);
      String absPath = dir.getAbsolutePath();
      log_.println(sysMessages_.getThereWasAProblemCreatingTheFollowingDirectory() + sys_.lineSeparator() +
          absPath);
      return;
    }
    
    
    String writeLogArg = cmdMessages_.getWriteLog(true);
    if (sys.hasArg(writeLogArg)) {
      fileLog_ = sys_.newFabFileLog(outputDir + files_.getNameSeparator() + 
          "fab.log");
      sys.setLogFile(fileLog_);
    }
    
    log_.println(sys_.lineSeparator() + sysMessages_.getFabricating() + 
        sys_.lineSeparator());
    
    List<String> argCommands = sys_.getArgValues(cmdMessages_.getCommand());
    if (argCommands.size() >= 1) {
      commands_ = true;
    }
    if (!addXmlRoutines(factory, argCommands)) {
      return;
    }
    memory_ = addMemoryValues(sys, factory);
    
    String depotDir = fab_.getFabricateXmlRunDir() + "depot";
    String depotFile = depotDir + files_.getNameSeparator() + "depot.xml";
    DepotType depotType = null;
    if (log_.isLogEnabled(FabricateController.class)) {
      log_.println("checking depot file " + depotFile);
    }
    if (files_.exists(depotFile)) {
      depotType = xmlFiles_.parseDepot_v1_0(depotFile);
    } else {
      depotType = new DepotType();
    }
    memory_.put(FabricationMemoryConstants.DEPOT, new Depot(depotDir, new DepotContext(sys_), depotType));
    memory_.addLock(new MemoryLock(FabricationMemoryConstants.DEPOT, 
        Collections.singleton(FabricateController.class.getName())));
    
    repositoryManager_ = factory.createRepositoryManager(sys_, fab_);
    I_RoutinePopulatorMutant populator = factory_.createRoutinePopulator();
    setupRoutinePopulator(populator);
    if (requiresProjects(argCommands)) {
      if (!manageProjectsDirAndMode(factory)) {
        return;
      }
      if (log_.isLogEnabled(FabricateController.class)) {
        log_.println(sys_.lineSeparator() + sysMessages_.getRunningFacets());
      }
      I_RoutineBuilder routineBuilder = factory_.createRoutineBuilder(RoutineBriefOrigin.FACET, populator);
      ProjectsManager pm = factory.createProjectsManager(sys_, factory_, routineBuilder);
      failure_ = pm.setupAndRun(memory_);
      if (checkFailure()) {
        return;
      }
    }
    //add in loaded projects
    setupRoutinePopulatorAfterFacets(populator);
    
    if (commands_) {
      if (log_.isLogEnabled(FabricateController.class)) {
        log_.println(sys_.lineSeparator() + sysMessages_.getRunningCommands());
      }
      I_RoutineBuilder routineBuilder = factory_.createRoutineBuilder(RoutineBriefOrigin.COMMAND, populator);
      CommandManager manager = factory.createCommandManager(argCommands, sys_, factory_, routineBuilder);

      failure_ = manager.processCommands(memory_);
      if (checkFailure()) {
        return;
      }
    } else {
      if (log_.isLogEnabled(FabricateController.class)) {
        log_.println(sys_.lineSeparator() + sysMessages_.getRunningBuildStages());
      }
      I_RoutineBuilder routineBuilder = factory_.createRoutineBuilder(RoutineBriefOrigin.STAGE, populator);
      I_RoutineBuilder routineArchiveBuilder = factory_.createRoutineBuilder(RoutineBriefOrigin.ARCHIVE_STAGE, populator);
      FabricationManager fabManager = factory.createFabricationManager(sys_, factory_, routineBuilder,
          routineArchiveBuilder);
      failure_ = fabManager.setupAndRunBuildStages(memory_);
      if (checkFailure()) {
        return;
      }
      if (sys_.hasArg(cmdMessages_.getArchive(true))) {
        if (log_.isLogEnabled(FabricateController.class)) {
          log_.println(sys_.lineSeparator() + sysMessages_.getRunningArchiveStages());
        }
        log_.println("TODO fabManager.setupAndRunArchiveStages");
      }
    }
    
    writeResult();
    
  }

  private boolean checkFailure() throws IOException {
    if (failure_ != null) {
      writeResult();
      return true;
    }
    return false;
  }

  private FabricationMemoryMutant<Object> addMemoryValues(FabSystem sys, FabricateFactory factory) {
    FabricationMemoryMutant<Object> memory = factory.createMemory(sys);
    memory.put(FabricationMemoryConstants.ENV, 
        new ExecutionEnvironmentMutant(sysMessages_));
    memory.addLock(new MemoryLock(FabricationMemoryConstants.ENV, 
        Collections.singleton(FabricateController.class.getName())));
    
    String javaHome = ENV.getJavaHome(sys_);
    memory.put(FabricationMemoryConstants.JAVA_HOME, javaHome);
    memory.addLock(new MemoryLock(FabricationMemoryConstants.JAVA_HOME, 
        Collections.singleton(FabricateController.class.getName())));
    
    JavaFactory jFactory = factory.createJavaFactory();
    memory.put(FabricationMemoryConstants.JAVA_FACTORY, jFactory);
    memory.addLock(new MemoryLock(FabricationMemoryConstants.JAVA_FACTORY, 
        Collections.singleton(FabricateController.class.getName())));
    return memory;
  }

  private boolean addXmlRoutines(FabricateFactory factory, List<String> argCommands)
      throws IOException, ClassNotFoundException {
    
    String fabricateXmlPath = discovery_.getFabricateXmlPath();
    fabXml_ =  xmlFiles_.parseFabricate_v1_0(fabricateXmlPath);
    fab_ = factory.create(sys_, fabXml_, discovery_);
    
    
    FabricateMutant fm = factory.createMutant(fab_);
    
    String fabricateHome = fm.getFabricateHome();
    PatternFileMatcher pfm = new PatternFileMatcher(files_, sys_, "fabricate*", true);
    List<String> files = files_.list(fabricateHome + files_.getNameSeparator() + "lib", pfm);
    if (files.size() != 1) {
      throw new IllegalStateException("no fabricate*.jar in " + fabricateHome + " lib?");
    }
    JavaFactory jFactory = factory.createJavaFactory();
    ManifestParser mp = jFactory.newManifestParser();
    String fabricateJar = files.get(0);
    mp.readManifest(fabricateJar);
    String version = mp.get("Specification-Version");
    fm.setFabricateVersion(version);
    
    List<RoutineParentType> traits =  fabXml_.getTrait();
    
    try {
      if (traits != null) {
        fm.addTraits(traits);
      }
      fm.addCommands(fabXml_.getCommand());
      fm.addStagesAndProjects(fabXml_);
      
      fab_ = factory.create(fm);
      factory_ = factory.createRoutineFabricateFactory(sys_, fab_, commands_);
      return true;
    } catch (ClassNotFoundException x) {
      String message = sysMessages_.getUnableToLoadTheFollowingClass() + 
        sys_.lineSeparator() + x.getMessage();
      log_.println(message);
      log_.printTrace(x);
      return false;
    }
  }

  private boolean manageProjectsDirAndMode(FabricateFactory factory) throws IOException, ClassNotFoundException {
    FabricateMutant fm = factory.createMutant(fab_);
    
    String devXmlDir = discovery_.getDevXmlDir();
    String projectRunDir = discovery_.getProjectXmlDir();
    if ( !StringUtils.isEmpty(devXmlDir)) {
      //it was run from a project directory and a dev.xml file was discovered in
      // the parent directory
      fm.setDevelopmentMode(true);
      fm.setProjectsDir(devXmlDir);
    } else if (!StringUtils.isEmpty(projectRunDir)){
      //it was run from the project dir like project_group/projects/projectX
      String projectsDir = files_.getParentDir(projectRunDir);
      fm.setProjectsDir(projectsDir);
    } else {
      String runDir = discovery_.getFabricateXmlDir();
      //it was run from the project group dir
      if (sys_.hasArg(cmdMessages_.getDevelopment(true))) {
        //development mode
        File runDirFile = files_.instance(runDir);
        String projectsDir = files_.getParentDir(runDir);
        fm.setProjectsDir(projectsDir);
        fm.setDevelopmentMode(true);
        String dev = projectsDir + "dev.xml";
        if (!files_.exists(dev)) {
          FabricateDevType devType = new FabricateDevType();
          String projectGroup = runDirFile.getName();
          devType.setProjectGroup(projectGroup);
          xmlFiles_.writeDev_v1_0(dev, devType);
        }
      } else {
        String projectsDir = runDir + "projects";
        if (files_.exists(projectsDir)) {
          if (sys_.hasArg(cmdMessages_.getPurge(true))) {
            
            String fabricateDir = discovery_.getFabricateXmlDir();
            //pull off the last slash
            fabricateDir = fabricateDir.substring(0, fabricateDir.length() - 1);
            //.idx and .pack files had a read only attribute set on Windows, this is the fix
            // which allows the next Fabricate to purge (-p) the projects directory.
            I_Executor exe = sys_.getExecutor();
            I_ExecutionResult result = exe.executeProcess(FabricationMemoryConstants.EMPTY_ENV, fabricateDir, 
                "chmod", "-R", "+w", "projects");
            if (result.getExitCode() != 0) {
              String message = sysMessages_.getTheFollowingCommandLineProgramExitedAbnormallyWithExitCodeX();
              message = message.replace("<X/>", "" + result.getExitCode());
              throw new IllegalStateException(message + sys_.lineSeparator() +
                  fabricateDir + ": chmod -R +x projects");
            }
            
            try {
              files_.deleteRecursive(projectsDir);
            } catch (IOException x) {
              log_.println(sysMessages_.getThereWasAProblemDeletingTheFollowingDirectory() +
                  sys_.lineSeparator() + projectsDir);
              if (log_.isLogEnabled(FabricateController.class)) {
                log_.printTrace(x);
              }
              return false;
            }
            makeProjectsDir(projectsDir);
          }
        } else {
          makeProjectsDir(projectsDir);
        }
        fm.setProjectsDir(projectsDir + files_.getNameSeparator());
      }
    }
    
    fab_ = factory.create(fm);
    factory_ = factory.createRoutineFabricateFactory(sys_, fab_, commands_);
    String dir = fab_.getProjectsDir();
    String message = sysMessages_.getProjectsAreLocatedInTheFollowingDirectory();
    log_.println(message + sys_.lineSeparator() + dir); 
    return true;
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
  
  public void makeProjectsDir(String projectsDir) throws IOException {
    if (!files_.mkdirs(projectsDir)) {
      String message = sysMessages_.getThereWasAProblemCreatingTheFollowingDirectory();
      throw new IOException(message + sys_.lineSeparator() +
          projectsDir);
    }
  }
  
  private boolean requiresProjects(List<String> argCommands) {
    
    try {
      if (commands_) {
        if (factory_.anyCommandsAssignableTo(I_ProjectBriefsAware.class, 
            argCommands)) {
          return true;
        }
        if (factory_.anyCommandsAssignableTo(I_ProjectsAware.class, 
            argCommands)) {
          return true;
        }
      } else {
        String archive = cmdMessages_.getArchive(true);
        if (sys_.hasArg(archive)) {
          List<String> aStages = sys_.getArgValues(cmdMessages_.getArchiveStages());
          List<String> aSkips = sys_.getArgValues(cmdMessages_.getSkipArchives());
          if (factory_.anyArchiveStagesAssignableTo(I_ProjectBriefsAware.class, 
              aStages, aSkips)) {
            return true;
          }
          if (factory_.anyArchiveStagesAssignableTo(I_ProjectsAware.class, 
              aStages, aSkips)) {
            return true;
          }
        }
        
        List<String> stages = sys_.getArgValues(cmdMessages_.getStages());
        List<String> skips = sys_.getArgValues(cmdMessages_.getSkip());
        if (factory_.anyStagesAssignableTo(I_ProjectBriefsAware.class, 
            stages, skips)) {
          return true;
        }
        if (factory_.anyStagesAssignableTo(I_ProjectsAware.class, 
            stages, skips)) {
          return true;
        }
      }
    } catch (FabricationRoutineCreationException x) {
      FabricationRoutineCreationException.log(log_, sysMessages_, x);
    }
    return false;
  }
  
  
  @SuppressWarnings("boxing")
  private void writeResult() throws IOException {
    File resultFile = null;
    String fabricateXmlPath = discovery_.getFabricateXmlDir();
    resultFile = files_.create(fabricateXmlPath + 
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
      result.setFailure(failure_.getFailure());
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
        if (!failure_.isLogged()) {
          FailureType f = failure_.getFailure();
          log_.println(f.getDetail());
        }
      }
      log_.println(sysMessages_.getFabricationFailed());
    }
    log_.derail();
    if (fileLog_ != null) {
      fileLog_.close();
    }
    sys_.exit(0);
  }
  
  private void setupRoutinePopulator(I_RoutinePopulatorMutant populatorMutant) {
    populatorMutant.setRepositoryFactory(fabFactory_);
    populatorMutant.setRepositoryManager(repositoryManager_);
    
    populatorMutant.setRoutineProcessorFactory(factory_);
    
    I_RoutineBrief scm = fab_.getScm();
    if (scm != null) {
      ScmContext scmCtx = new ScmContext(scm);
      populatorMutant.putInput(ScmContext.class, scmCtx);
    }
  }
  
  @SuppressWarnings("unchecked")
  private void setupRoutinePopulatorAfterFacets(I_RoutinePopulatorMutant populatorMutant) {
    List<I_Project> projects = (List<I_Project>) memory_.get(FabricationMemoryConstants.PARTICIPATING_PROJECTS);
    populatorMutant.setProjects(projects);
  }
  
}
