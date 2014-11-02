package org.adligo.fabricate.build.stages;

import org.adligo.fabricate.common.I_FabContext;
import org.adligo.fabricate.common.I_FabStage;
import org.adligo.fabricate.common.NamedProject;
import org.adligo.fabricate.common.ThreadLocalPrintStream;
import org.adligo.fabricate.xml.io.FabricateType;
import org.adligo.fabricate.xml.io.StagesAndProjectsType;
import org.adligo.fabricate.xml.io.project.FabricateProjectType;
import org.adligo.fabricate.xml.io.project.ProjectStageType;
import org.adligo.fabricate.xml.io.project.StagesType;
import org.adligo.fabricate.xml.io.tasks.ParamType;
import org.adligo.fabricate.xml.io.tasks.TaskType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * this class is a delegates to to other classes that 
 * obtain projects from source control (i.e. git).
 * 
 * @author scott
 *
 */
public abstract class BaseConcurrentStage implements I_FabStage {
  protected String stageName_;
  protected String projectsPath_;
  protected I_FabContext ctx_;
  protected FabricateType fabricate_;
  private final ArrayBlockingQueue<Boolean> finished_ = new ArrayBlockingQueue<Boolean>(1);
  private volatile Exception lastException_ = null;
  private Map<String,String> stageParams_ = null;
  private Map<String,Map<String,String>> taskParams_ = null;
  
  @SuppressWarnings("unchecked")
  @Override
  public void setup(I_FabContext ctx) {
    ctx_ = ctx;
    fabricate_ = ctx_.getFabricate();
    projectsPath_ = ctx_.getProjectsPath();
    
    StagesAndProjectsType spt =  fabricate_.getProjectGroup();
    org.adligo.fabricate.xml.io.StagesType st = spt.getStages();
    List<org.adligo.fabricate.xml.io.StageType> stages = st.getStage();
    for (org.adligo.fabricate.xml.io.StageType stage: stages) {
      String stageName = stage.getName();
      if (stageName_.equals(stageName)) {
        List<ParamType> params = stage.getParam();
        if (params != null) {
          if (params.size() > 0) {
            Map<String,String> paramMap = new HashMap<String, String>();
            for (ParamType stp: params) {
              paramMap.put(stp.getKey(), stp.getValue());
            }
            stageParams_ = Collections.unmodifiableMap(paramMap);
          }
        }
        if (stageParams_ == null) {
          stageParams_ = Collections.EMPTY_MAP;
        }
        List<TaskType> tasks =  stage.getTask();
        if (tasks != null) {
          for (TaskType task: tasks) {
            String taskName = task.getName();
            Map<String,String> paramMap = toMap(task.getParam());
            if (taskParams_  == null) {
              taskParams_ = new HashMap<String,Map<String,String>>();
            }
            taskParams_.put(taskName, paramMap);
          }
        }
        break;
      }
    }
    
  }

  public Map<String,String> toMap(List<ParamType> taskParams) {
    Map<String,String> toRet = new HashMap<String, String>();
    if (taskParams != null) {
      for (ParamType pt: taskParams) {
        toRet.put(pt.getKey(), pt.getValue());
      }
    }
    return Collections.unmodifiableMap(toRet);
  }

  @Override
  public boolean isConcurrent() {
    return true;
  }

  @Override
  public void waitUntilFinished() {
    try {
      finished_.take();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  protected void finish() {
    try {
      finished_.put(Boolean.TRUE);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
  
  protected void finish(Exception x) {
    lastException_ = x;
    if (ctx_.isLogEnabled(BaseConcurrentStage.class)) {
      ThreadLocalPrintStream.println(this.getClass().getSimpleName() + " had a exception.");
    }
    try {
      
      finished_.put(Boolean.TRUE);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
  
  @Override
  public boolean hadException() {
    if (lastException_ != null) {
      return true;
    }
    return false;
  }

  @Override
  public Exception getException() {
    return lastException_;
  }

  @Override
  public void setStageName(String stageName) {
    stageName_ = stageName;
  }

  /**
   * This method returns a cached non unmodifiable instance
   * created by setup from the fabricate.xml file, so if it's null 
   * there is probably calling code that didn't call setup.
   * 
   * 
   * @param project
   * @return
   */
  public Map<String,String> getParams() {
    return stageParams_;
  }

  /**
   * This method returns a instance
   * with the project.xml stage params overriding the fabricate.xml stage params.
   * This should be done in the child class setup and cached as a instance
   * variable.  
   * @param project
   * @return
   */
  public Map<String,String> getParams(FabricateProjectType project) {
    Map<String,String> toRet = new HashMap<String,String>();
    toRet.putAll(stageParams_);
    
    StagesType st = project.getStages();
    if (st != null) {
      List<ProjectStageType> stages = st.getStage();
      for (ProjectStageType stage: stages) {
        String projectStage = stage.getName();
        if (stageName_.equals(projectStage)) {
          List<ParamType> params =  stage.getParam();
          for (ParamType param: params) {
            toRet.put(param.getKey(), param.getValue());
          }
        }
      }
    }
    return toRet;
  }
  
  /**
   * This method returns a instance
   * with the project.xml stage params overriding the fabricate.xml stage params.
   * This should be done in the child class setup and cached as a instance
   * variable.  
   * @param project
   * @return
   */
  public Map<String,String> getParams(String task, FabricateProjectType project) {
    Map<String,String> toRet = new HashMap<String,String>();
    if (taskParams_ != null) {
      Map<String,String> fabParams = taskParams_.get(task);
      if (fabParams != null) {
        toRet.putAll(toRet);
      }
    }
    StagesType st = project.getStages();
    if (st != null) {
      List<ProjectStageType> stages = st.getStage();
      for (ProjectStageType stage: stages) {
        String projectStage = stage.getName();
        if (stageName_.equals(projectStage)) {
          Map<String,String> toAdd = toMap(stage.getParam());
          toRet.putAll(toAdd);
          
          List<TaskType> tasks = stage.getTask();
          if (tasks != null) {
            for (TaskType taskType: tasks) {
              String projTaskName = taskType.getName();
              if (task.equals(projTaskName)) {
                toAdd = toMap(taskType.getParam());
                toRet.putAll(toAdd);
                break;
              }
            }
          }
        }
      }
    }
    if (ctx_.isLogEnabled(BaseConcurrentStage.class)) {
      ThreadLocalPrintStream.println("Task " + task + " has params " + toRet);
    }
    return toRet;
  }
  
  /**
   * A threadsafe method to determine if this project
   * is participating in this stage, for I_FabTask
   * implementations that require participation ie
   * CompileAndJar.
   * 
   * @param project
   * @return
   */
  public boolean isParticipant(FabricateProjectType project) {
    StagesType st = project.getStages();
    if (st != null) {
      List<ProjectStageType> stages = st.getStage();
      for (ProjectStageType stage: stages) {
        String projectStage = stage.getName();
        if (stageName_.equals(projectStage)) {
          return true;
        }
      }
    }
    return false;
  }
  
  /**
   * this method will build a queue 
   * for sub classes that require participation.
   * This also updates the projects to done 
   * in the ProjectsMemeory
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public ConcurrentLinkedQueue<NamedProject> getParticipantQueue() {
    ConcurrentLinkedQueue<NamedProject> queue = getNewProjectDependencyOrder();
    List<NamedProject> list = new ArrayList<NamedProject>(queue);
    
    ConcurrentHashMap<String, AtomicBoolean> projectStates =
        (ConcurrentHashMap<String, AtomicBoolean>)
        ctx_.getFromMemory(DefaultMemoryConstants.PROJECT_STATES);
    
    Iterator<NamedProject> it = list.iterator();
    while (it.hasNext()) {
      NamedProject np = it.next();
      FabricateProjectType fpt = np.getProject();
      if (!isParticipant(fpt)) {
        it.remove();
        AtomicBoolean b = projectStates.get(np.getName());
        b.set(true);
      } 
    }
    return new ConcurrentLinkedQueue<NamedProject>(list);
  }
  
  /**
   * This method can be called by stage/tasks after LoadAndCleanProjects
   * to obtain a new project queue, if a task for a project
   * depends on other projects being finished the methods
   * hasProjectFinishedStage and 
   * setProjectFinisedForStage can be used to make sure
   * that execution happens in a nice orderly manor.
   * @return
   */
  @SuppressWarnings("unchecked")
  protected ConcurrentLinkedQueue<NamedProject> getNewProjectDependencyOrder() {
    ConcurrentHashMap<String, AtomicBoolean> projectStates = new ConcurrentHashMap<String, AtomicBoolean>();
    List<NamedProject> projs = (List<NamedProject>)
          ctx_.getFromMemory(DefaultMemoryConstants.PROJECTS_DEPENDENCY_ORDER);
    
    for (NamedProject np: projs) {
      projectStates.put(np.getName(), new AtomicBoolean(false));
    }
    ctx_.putInMemory(DefaultMemoryConstants.PROJECT_STATES, projectStates);
    return new ConcurrentLinkedQueue<NamedProject>(projs);
  }
  
  public boolean hasFinishedStage(String projectName) {
    @SuppressWarnings("unchecked")
    ConcurrentHashMap<String, AtomicBoolean> projectStates =
        (ConcurrentHashMap<String, AtomicBoolean>)
        ctx_.getFromMemory(DefaultMemoryConstants.PROJECT_STATES);
    AtomicBoolean b = projectStates.get(projectName);
    return b.get();
  }
  
  public void setFinisedStage(String projectName) {
    @SuppressWarnings("unchecked")
    ConcurrentHashMap<String, AtomicBoolean> projectStates =
        (ConcurrentHashMap<String, AtomicBoolean>)
        ctx_.getFromMemory(DefaultMemoryConstants.PROJECT_STATES);
    AtomicBoolean b = projectStates.get(projectName);
    b.set(true);
  }
}
