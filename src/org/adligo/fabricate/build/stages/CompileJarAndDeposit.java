package org.adligo.fabricate.build.stages;

import org.adligo.fabricate.build.stages.tasks.CompileTask;
import org.adligo.fabricate.build.stages.tasks.DepositTask;
import org.adligo.fabricate.build.stages.tasks.JarTask;
import org.adligo.fabricate.common.I_Depot;
import org.adligo.fabricate.common.I_FabContext;
import org.adligo.fabricate.common.I_FabStage;
import org.adligo.fabricate.common.NamedProject;
import org.adligo.fabricate.common.ProjectBlock;
import org.adligo.fabricate.common.ThreadLocalPrintStream;
import org.adligo.fabricate.external.GitCalls;
import org.adligo.fabricate.external.ManifestParser;
import org.adligo.fabricate.xml.io.library.DependenciesType;
import org.adligo.fabricate.xml.io.project.FabricateProjectType;

import java.io.File;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class CompileJarAndDeposit extends BaseConcurrentStage implements I_FabStage {
  private ConcurrentLinkedQueue<NamedProject> projectsQueue_;
  private final Semaphore semaphore_ = new Semaphore(1);
  private AtomicInteger projectsFinished_ = new AtomicInteger(0);
  /**
   * the key to this is the current threads projects name space
   * the blocking projects name the value is a ArrayBlockingQueue
   * with size 1 that the current thread adds to the concurrent hash
   * map, when a project finishes it spins through the map entries
   * looking for it's name and adds a item to the BlockingQueue. 
   */
  private ConcurrentHashMap<ProjectBlock, ArrayBlockingQueue<Boolean>> projectBlockMap_ =
      new ConcurrentHashMap<ProjectBlock, ArrayBlockingQueue<Boolean>>();
  private int projectsQueueSize_;
  @Override
  public void setup(I_FabContext ctx) {
    super.setup(ctx);
    projectsQueue_ = super.getParticipantQueue();
    projectsQueueSize_ = projectsQueue_.size();
  }
  
  @Override
  public void run() {
    try {
      NamedProject p = projectsQueue_.poll();
      boolean didOne = false;
      while (p != null) {
        didOne = true;
        String projectName = p.getName();
        if (ctx_.isLogEnabled(CompileJarAndDeposit.class)) {
          ThreadLocalPrintStream.println(this.getClass().getSimpleName() + " working on project " +
              projectName);
        }
        FabricateProjectType fpt = p.getProject();
        Map<String,String> params = super.getParams("compile",fpt);
        CompileTask compile = new CompileTask();
        compile.setup(ctx_, p, params);
        
        if (compile.hadSetupException()) {
          super.finish(compile.getLastSetupException());
          return;
        }
        checkIfProjectsDependedOnAreAllFinished(p);
        compile.execute();
        String projectVersion = GitCalls.describe(compile.getProjectPath());
        JarTask jar = new JarTask();
        if ( !params.containsKey(ManifestParser.SPECIFICATION_VERSION)) {
          params.put(ManifestParser.SPECIFICATION_VERSION, projectVersion);
        }
        //don't allow override of the git describe!
        params.put(ManifestParser.IMPLEMENTATION_VERSION, projectVersion);
        
        jar.setup(ctx_, p, params);
        if (jar.hadSetupException()) {
          super.finish(jar.getLastSetupException());
          return;
        }
        jar.execute();
        
        String jarName = jar.getFileName();
        
        DepositTask deposit = new DepositTask();
        deposit.setup(ctx_, p, params);
        if (deposit.hadSetupException()) {
          super.finish(deposit.getLastSetupException());
          return;
        }
        
        String fileName = projectsPath_ + File.separator + projectName + 
            File.separator + "build" + File.separator + jarName;
       deposit.execute(fileName, "jar");
        
       
       Enumeration<ProjectBlock> keys =  projectBlockMap_.keys();
       if (ctx_.isLogEnabled(CompileJarAndDeposit.class)) {
         ThreadLocalPrintStream.println(projectName + " is checking necessary notifications " +
             projectBlockMap_.size());
       }
       while (keys.hasMoreElements()) {
         ProjectBlock key = keys.nextElement();
         
         if (projectName.equals(key.getBlockingProject())) {
           ArrayBlockingQueue<Boolean> block =  projectBlockMap_.get(key);
           if (ctx_.isLogEnabled(CompileJarAndDeposit.class)) {
             ThreadLocalPrintStream.println(projectName + " is checking notifying " +
                 key.getProject());
           }
           block.add(Boolean.TRUE);
           projectBlockMap_.remove(key);
         }
       }
        setFinisedStage(projectName);
        projectsFinished_.incrementAndGet();
        p = projectsQueue_.poll();
      }
      
      if (didOne) {
        if (ctx_.isLogEnabled(CompileJarAndDeposit.class)) {
          ThreadLocalPrintStream.println( " projectsQueueSize_ = " + projectsQueueSize_ +
              " projectsFinished_ = " + projectsFinished_.get());
        }
        if (projectsQueueSize_ == projectsFinished_.get()) {
          finish();
        }
      }
    } catch (Exception x) {
      super.finish(x);
    }
    
  }

  public void finish() {
    try {
      semaphore_.acquire();
      if (ctx_.isLogEnabled(CompileJarAndDeposit.class)) {
        ThreadLocalPrintStream.println(this.getClass().getSimpleName() + " finishing.");
      }
      I_Depot depot =  ctx_.getDepot();
      depot.store();
      super.finish();
      semaphore_.release();
    } catch (InterruptedException x) {
      //do nothing this thread didn't acquire the lock
    }
  }


  private void checkIfProjectsDependedOnAreAllFinished(NamedProject np) {
    FabricateProjectType fpt =  np.getProject();
    DependenciesType deps = fpt.getDependencies();
    
    if (deps != null) {
      List<String> projects = deps.getProject();
      for (String project: projects) {
        if (!hasFinishedStage(project)) {
          ArrayBlockingQueue<Boolean> queue = new ArrayBlockingQueue<Boolean>(1);
          projectBlockMap_.putIfAbsent(new ProjectBlock(np.getName(),project), 
              queue);
          try {
            queue.take();
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        }
      }
    }
  }
}
