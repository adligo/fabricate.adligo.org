package org.adligo.fabricate.routines.implicit;

import org.adligo.fabricate.common.i18n.I_CommandLineConstants;
import org.adligo.fabricate.common.util.StringUtils;
import org.adligo.fabricate.depot.ArtifactMutant;
import org.adligo.fabricate.depot.I_Depot;
import org.adligo.fabricate.models.common.FabricationMemoryConstants;
import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.common.I_FabricationMemory;
import org.adligo.fabricate.models.common.I_FabricationMemoryMutant;
import org.adligo.fabricate.models.common.I_FabricationRoutine;
import org.adligo.fabricate.models.common.I_RoutineMemory;
import org.adligo.fabricate.models.common.I_RoutineMemoryMutant;
import org.adligo.fabricate.models.common.MemoryLock;
import org.adligo.fabricate.models.common.ParameterMutant;
import org.adligo.fabricate.models.dependencies.I_ProjectDependency;
import org.adligo.fabricate.models.project.I_Project;
import org.adligo.fabricate.models.project.I_ProjectBrief;
import org.adligo.fabricate.models.project.Project;
import org.adligo.fabricate.routines.AbstractRoutine;
import org.adligo.fabricate.routines.I_ConcurrencyAware;
import org.adligo.fabricate.routines.I_InputAware;
import org.adligo.fabricate.routines.I_OutputProducer;
import org.adligo.fabricate.routines.I_PlatformAware;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This task identifies which projects should be executed on,
 * and clears the depot for those projects only.
 * @author scott
 *
 */
public class SetupProjectsRoutine extends FabricateAwareRoutine implements 
  I_ConcurrencyAware, I_OutputProducer<String> {
  private static final String QUEUE = "queue";
  
  private I_Depot depot_;
  private List<String> platforms_;
  private String jarFileName_;
  private I_FabricationRoutine nameJarTrait_;
  private List<Project> projects_;
  private Map<String,Project> projectsMap_;
  protected ConcurrentLinkedQueue<Project> queue_;
  
  @SuppressWarnings("unchecked")
  @Override
  public boolean setupInitial(I_FabricationMemoryMutant<Object> memory,
      I_RoutineMemoryMutant<Object> routineMemory) throws FabricationRoutineCreationException {
    
    depot_ = (I_Depot) memory.get(FabricationMemoryConstants.DEPOT);
    nameJarTrait_ = traitFactory_.createRoutine(NameJarTrait.NAME, NameJarTrait.IMPLEMENTED_INTERFACES);
    projects_ = (List<Project>) memory.get(FabricationMemoryConstants.PROJECTS_LOADED);
    projectsMap_ = (Map<String,Project>) memory.get(FabricationMemoryConstants.PROJECTS_LOADED_MAP);
    
    I_CommandLineConstants clConstants = constants_.getCommandLineConstants();
    String value = system_.getArgValue(clConstants.getPlatforms());
    List<String> platforms = new ArrayList<String>();
    if (StringUtils.isEmpty(value)) {
      platforms.add("jse");
    } else {
      String [] plats = ParameterMutant.getValueDelimited(value, ",");
      for (int i = 0; i < plats.length; i++) {
        platforms.add(plats[i].toLowerCase());
      }
    }
    platforms_ = Collections.unmodifiableList(platforms);
    memory.put(FabricationMemoryConstants.PLATFORMS, platforms_);
    memory.addLock(new MemoryLock(FabricationMemoryConstants.PLATFORMS, 
        Collections.singletonList(SetupProjectsRoutine.class.getName())));

    queue_ = system_.newConcurrentLinkedQueue(Project.class);
    String projectRunDir = fabricate_.getFabricateProjectRunDir();
    if (log_.isLogEnabled(SetupProjectsRoutine.class)) {
      log_.println(SetupProjectsRoutine.class.getName() + " projectRunDir " +
           system_.lineSeparator() + projectRunDir);  
    }
    File prFile = files_.instance(projectRunDir);
    String project = prFile.getName();
    
    String rebuildDependents = cmdConstants_.getRebuildDependents(true);
    
    List<Project> participants = new ArrayList<Project>();
    if (StringUtils.isEmpty(projectRunDir)) {
      participants.addAll(projects_);
    } else if (system_.hasArg(rebuildDependents)) {
      Set<String> initialParticipants = new HashSet<String>();
      for (Project proj: projects_) {
        addIfParticipant(project, initialParticipants, proj);
      }
      int added = participants.size();
      while (added >= 1) {
        List<String> lastParticipants = new ArrayList<String>(initialParticipants);
        Set<String> newParticipants = new HashSet<String>();
        for (String lasProject: lastParticipants) {
          Project proj = projectsMap_.get(lasProject);
          addIfParticipant(project, newParticipants, proj);
        }
        newParticipants.removeAll(participants);
        added = newParticipants.size();
        initialParticipants.addAll(newParticipants);
      }
      for (String part: initialParticipants) {
        participants.add(projectsMap_.get(part));
      }
    } else {
      File file = files_.instance(projectRunDir);
      String projectName = file.getName();
      Project prj = projectsMap_.get(projectName);
      if (log_.isLogEnabled(SetupProjectsRoutine.class)) {
        log_.println(SetupProjectsRoutine.class.getName() + " got the following project for name " + projectName +
             system_.lineSeparator() + prj);  
      }
      participants.add(prj);
    }
    memory.put(FabricationMemoryConstants.PARTICIPATING_PROJECTS, Collections.unmodifiableList(participants));
    memory.addLock(new MemoryLock(FabricationMemoryConstants.PARTICIPATING_PROJECTS, 
        Collections.singletonList(SetupProjectsRoutine.class.getName())));
    
    
    queue_.addAll(participants);
    //if (log_.isLogEnabled(SetupProjectsRoutine.class)) {
      log_.println(SetupProjectsRoutine.class.getSimpleName() + " setup with " + queue_.size() + " participants." );
    //}
    routineMemory.put(QUEUE, queue_);
    return super.setupInitial(memory, routineMemory);
  }


  private void addIfParticipant(String project, Set<String> initialParticipants, Project proj) {
    List<I_ProjectDependency> pDeps = proj.getProjectDependencies();
    for (I_ProjectDependency pdep: pDeps) {
      if (project.equals(pdep.getProjectName())) {
        initialParticipants.add(proj.getName());
        break;
      }
    }
  }


  @SuppressWarnings("unchecked")
  @Override
  public void setup(I_FabricationMemory<Object> memory, I_RoutineMemory<Object> routineMemory)
      throws FabricationRoutineCreationException {
    
    depot_ = (I_Depot) memory.get(FabricationMemoryConstants.DEPOT);
    nameJarTrait_ = traitFactory_.createRoutine(NameJarTrait.NAME, NameJarTrait.IMPLEMENTED_INTERFACES);
    projectsMap_ = (Map<String,Project>) memory.get(FabricationMemoryConstants.PROJECTS_LOADED_MAP);
    
    queue_ = (ConcurrentLinkedQueue<Project>) routineMemory.get(QUEUE);
    
    platforms_ = (List<String>) memory.get(FabricationMemoryConstants.PLATFORMS);
    super.setup(memory, routineMemory);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public void run() {
    super.setRunning();
    Project project = queue_.poll();
    if (log_.isLogEnabled(SetupProjectsRoutine.class)) {
      log_.println(SetupProjectsRoutine.class.getSimpleName() + " running with " + queue_.size() + " participants." );
    }
    while (project != null) {
      Iterator<String> platforms = platforms_.iterator();
      while (platforms.hasNext()) {
        String platform = platforms.next();
        ((I_InputAware<I_Project>) nameJarTrait_).setInput(project);
        nameJarTrait_.run();
        jarFileName_ = ((I_OutputProducer<String>) nameJarTrait_).getOutput();
        if (log_.isLogEnabled(SetupProjectsRoutine.class)) {
          log_.println(SetupProjectsRoutine.class.getSimpleName() + " running with " + jarFileName_ );
        }
        
        ArtifactMutant am = new ArtifactMutant();
        am.setFileName(jarFileName_);
        am.setPlatformName(platform);
        am.setType("jar");
        am.setProjectName(project.getName());
        if (log_.isLogEnabled(SetupProjectsRoutine.class)) {
          log_.println(SetupProjectsRoutine.class.getSimpleName() + " deleting " + project.getName() + " from depot");
        }
        depot_.deleteIfPresent(am);
      }
      project = queue_.poll();
    }
  }

  public String getJarFileName() {
    return jarFileName_;
  }
  
  
  public void setJarFileName(String jarFileName) {
    this.jarFileName_ = jarFileName;
  }

  @Override
  public List<Class<?>> getClassType(Class<?> interfaceClass) {
    if (I_OutputProducer.class.getName().equals(interfaceClass.getName())) {
      List<Class<?>> toRet = new ArrayList<Class<?>>();
      toRet.add(String.class);
      return toRet;
    }
    return null;
  }

  @Override
  public String getOutput() {
    return jarFileName_;
  }
}
