package org.adligo.fabricate.build.stages;

import org.adligo.fabricate.build.stages.shared.ProjectsMemory;
import org.adligo.fabricate.build.stages.tasks.CompileTask;
import org.adligo.fabricate.common.I_FabContext;
import org.adligo.fabricate.common.I_FabStage;
import org.adligo.fabricate.common.NamedProject;
import org.adligo.fabricate.common.StringUtils;
import org.adligo.fabricate.common.ThreadLocalPrintStream;
import org.adligo.fabricate.external.JavaCParam;
import org.adligo.fabricate.external.JavaCompilerCalls;
import org.adligo.fabricate.xml.io.library.DependenciesType;
import org.adligo.fabricate.xml.io.project.FabricateProjectType;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class CompileAndJar extends BaseConcurrentStage implements I_FabStage {
  private ConcurrentLinkedQueue<NamedProject> projectsQueue_;
  private AtomicBoolean finished_ = new AtomicBoolean(false);
  private final Semaphore semaphore_ = new Semaphore(1);
  private AtomicInteger projectsFinished_ = new AtomicInteger(0);
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
      while (p != null) {
        
        FabricateProjectType fpt = p.getProject();
        Map<String,String> params = super.getParams("compile",fpt);
        CompileTask compile = new CompileTask();
        compile.setup(ctx_, p, params);
        
        if (compile.hadSetupException()) {
          lastException_ = compile.getLastSetupException();
          return;
        }
        while (!checkIfProjectsDependedOnAreAllFinished(p)) {
          try {
            Thread.sleep(250);
          } catch (InterruptedException x) {
            //do nothing
          }
        }
        compile.execute();
        ProjectsMemory.setProjectFinisedForStage(p);
        projectsFinished_.incrementAndGet();
        p = projectsQueue_.poll();
      }
      if (projectsQueueSize_ == projectsFinished_.get()) {
        if (!finished_.get()) {
          finish();
        }
      }
    } catch (Exception x) {
      lastException_ = x;
    }
    
  }

  public void finish() {
    if (!finished_.get()) {
      try {
        semaphore_.acquire();
        if (ctx_.isLogEnabled(CompileAndJar.class)) {
          ThreadLocalPrintStream.println("Finished compile.");
        }
        finished_.set(true);
      } catch (InterruptedException x) {
        //do nothing this thread didn't acquire the lock
      }
    }
  }


  private boolean checkIfProjectsDependedOnAreAllFinished(NamedProject np) {
    FabricateProjectType fpt =  np.getProject();
    DependenciesType deps = fpt.getDependencies();
    if (deps != null) {
      List<String> projects = deps.getProject();
      for (String project: projects) {
        if (!ProjectsMemory.hasProjectFinishedStage(project)) {
          return false;
        }
      }
    }
    return true;
  }
}
