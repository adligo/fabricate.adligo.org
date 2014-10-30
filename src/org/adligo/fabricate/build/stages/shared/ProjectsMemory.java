package org.adligo.fabricate.build.stages.shared;

import org.adligo.fabricate.common.I_FabContext;
import org.adligo.fabricate.common.NamedProject;
import org.adligo.fabricate.common.ThreadLocalPrintStream;
import org.adligo.fabricate.xml.io.library.DependenciesType;
import org.adligo.fabricate.xml.io.project.FabricateProjectType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class provides a way to pass the loaded FabricateProject
 * xml files in memory from one stage/task to another.  
 * 
 * @author scott
 *
 */
public class ProjectsMemory{
  private static final ConcurrentHashMap<String,NamedProject> MEMORY =
      new ConcurrentHashMap<String,NamedProject>();
  private static final ConcurrentHashMap<String, AtomicBoolean> FINISHED_STAGE_MAP =
      new ConcurrentHashMap<String, AtomicBoolean>();
  private static List<NamedProject> PROJECT_DEPENDECY_ORDER;
  
  public static void add(NamedProject project) {
    MEMORY.putIfAbsent(project.getName(), project);
  }
  
  public static int size() {
    return MEMORY.size();
  }
  
  public static void calcProjectDependencyOrder(I_FabContext ctx_) throws IllegalStateException {
    if (ctx_.isLogEnabled(ProjectsMemory.class)) {
      ThreadLocalPrintStream.println("Calculating project dependency order for " + MEMORY.size() + " projects.");
    }
    if (MEMORY.size() == 1) {
      //only one project assume everything else is already done for
      //it, and if not error out when it finds something that needs to be completed
      //ie a missing .jar file
      PROJECT_DEPENDECY_ORDER = Collections.unmodifiableList(
          new ArrayList<NamedProject>(MEMORY.values()));
      return;
    }
    List<String> order = new ArrayList<String>();
    Iterator<NamedProject> oit =  MEMORY.values().iterator();
    
    while (oit.hasNext()) {
      NamedProject proj = oit.next();
      String name = proj.getName();
      FabricateProjectType fp = proj.getProject();
      DependenciesType dt =  fp.getDependencies();
      int count = 0;
      if (dt == null) {
        order.add(name);
      } else {
        List<String> projects =  dt.getProject();
        if (projects == null) {
          order.add(name);
        } else if (projects.size() == 0){
          order.add(name);
        } else {
          count = projects.size();
        }
      }
      if (ctx_.isLogEnabled(ProjectsMemory.class)) {
        if (order.contains(name)) {
          ThreadLocalPrintStream.println("The following project had NO project dependencies " + name);
        } else {
          ThreadLocalPrintStream.println("The following project had " + count + 
              " project dependencies " + name);
        }
      }
    }
    
    if (order.size() == 0) {
      throw new IllegalStateException("All projects depend on other projects,"
          + "which is a indication of circular project dependency.");
    }
    
    List<NamedProject> toDo = new ArrayList<NamedProject>(MEMORY.values());
    Iterator<NamedProject> it = toDo.iterator();
    while (it.hasNext()) {
      NamedProject np = it.next();
      if (order.contains(np.getName())) {
        it.remove();
      }
    }
    int lastSize = toDo.size();
    while (lastSize > 0){
      it = toDo.iterator();
      while (it.hasNext()) {
        NamedProject proj = it.next();
        String name = proj.getName();
        if (!order.contains(name)) {
          FabricateProjectType fp = proj.getProject();
          DependenciesType dt =  fp.getDependencies();
          List<String> projects =  dt.getProject();
          if (order.containsAll(projects)) {
              order.add(name);
              it.remove();
          }
        }
      }
      int nextSize = toDo.size();
      if (lastSize == nextSize) {
        StringBuilder sb = new StringBuilder();
        it = toDo.iterator();
        while (it.hasNext()) {
          NamedProject np = it.next();
          sb.append(np.getName() + System.lineSeparator());
        }
        throw new IllegalStateException("Unable to find project dependency order"
            + "for the following projects."  + System.lineSeparator() + 
            sb.toString());
      }
      lastSize = nextSize;
    }
    List<NamedProject> projectOrder = new ArrayList<NamedProject>();
    for (String projectName: order) {
      NamedProject np = MEMORY.get(projectName);
      projectOrder.add(np);
    }
    PROJECT_DEPENDECY_ORDER = Collections.unmodifiableList(projectOrder);
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
  public static ConcurrentLinkedQueue<NamedProject> getNewProjectDependencyOrder() {
    FINISHED_STAGE_MAP.clear();
    for (NamedProject np: PROJECT_DEPENDECY_ORDER) {
      FINISHED_STAGE_MAP.put(np.getName(), new AtomicBoolean(false));
    }
    return new ConcurrentLinkedQueue<NamedProject>(PROJECT_DEPENDECY_ORDER);
  }
  
  public static boolean hasProjectFinishedStage(String projectName) {
    AtomicBoolean ab = FINISHED_STAGE_MAP.get(projectName);
    return ab.get();
  }
  
  public static void setProjectFinisedForStage(NamedProject np) {
    AtomicBoolean ab = FINISHED_STAGE_MAP.get(np.getName());
    ab.set(true);
  }
  
}
