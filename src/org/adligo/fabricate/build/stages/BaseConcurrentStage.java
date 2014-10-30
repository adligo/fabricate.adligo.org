package org.adligo.fabricate.build.stages;

import org.adligo.fabricate.build.stages.shared.ProjectsMemory;
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
  protected final AtomicBoolean finished_ = new AtomicBoolean(false);
  protected volatile Exception lastException_ = null;
  private Map<String,String> stageParams_ = null;
  private Map<String,Map<String,String>> taskParams_ = null;
  
  @SuppressWarnings("unchecked")
  @Override
  public void setup(I_FabContext ctx) {
    ctx_ = ctx;
    fabricate_ = ctx_.getFabricate();
    projectsPath_ = ctx_.getProjectsPath();
    finished_.set(false);
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
  public boolean isFinished() {
    if (lastException_ != null) {
       if (ctx_.isLogEnabled(BaseConcurrentStage.class)) {
         ThreadLocalPrintStream.println(this.getClass().getSimpleName() + " had a exception.");
       }
      return true;
    }
    return finished_.get();
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
          List<TaskType> tasks = stage.getTask();
          if (tasks != null) {
            for (TaskType taskType: tasks) {
              String projTaskName = taskType.getName();
              if (task.equals(projTaskName)) {
                Map<String,String> toAdd = toMap(taskType.getParam());
                toRet.putAll(toAdd);
                break;
              }
            }
          }
        }
      }
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
  public ConcurrentLinkedQueue<NamedProject> getParticipantQueue() {
    ConcurrentLinkedQueue<NamedProject> queue = ProjectsMemory.getNewProjectDependencyOrder();
    List<NamedProject> list = new ArrayList<NamedProject>(queue);
    
    Iterator<NamedProject> it = list.iterator();
    while (it.hasNext()) {
      NamedProject np = it.next();
      FabricateProjectType fpt = np.getProject();
      if (!isParticipant(fpt)) {
        it.remove();
        ProjectsMemory.setProjectFinisedForStage(np);
      } 
    }
    return new ConcurrentLinkedQueue<NamedProject>(list);
  }
}
