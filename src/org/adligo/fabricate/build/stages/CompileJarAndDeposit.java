package org.adligo.fabricate.build.stages;

import org.adligo.fabricate.build.stages.tasks.OldCompileTask;
import org.adligo.fabricate.build.stages.tasks.OldDepositTask;
import org.adligo.fabricate.build.stages.tasks.OldJarTask;
import org.adligo.fabricate.common.I_FabStage;
import org.adligo.fabricate.common.I_RunContext;
import org.adligo.fabricate.common.NamedProject;
import org.adligo.fabricate.common.log.ThreadLocalPrintStream;
import org.adligo.fabricate.common.system.FabSystem;
import org.adligo.fabricate.common.system.GitCalls;
import org.adligo.fabricate.common.system.I_GitCalls;
import org.adligo.fabricate.depot.I_Depot;
import org.adligo.fabricate.java.ManifestParser;
import org.adligo.fabricate.xml.io_v1.project_v1_0.FabricateProjectType;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class CompileJarAndDeposit 
/*
extends OldBaseConcurrentStage implements I_FabStage
*/ {
  /*
  private ConcurrentLinkedQueue<NamedProject> projectsQueue_;
  private final Semaphore semaphore_ = new Semaphore(1);
  private AtomicInteger projectsFinished_ = new AtomicInteger(0);

  private int projectsQueueSize_;
  @Override
  public void setup(I_RunContext ctx) {
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
        
        if (log_.isLogEnabled(CompileJarAndDeposit.class)) {
          log_.println(this.getClass().getSimpleName() + " working on project " +
              projectName);
        }
        FabricateProjectType fpt = p.getProject();
        Map<String,String> params = super.getParams("compile",fpt);
        OldCompileTask compile = new OldCompileTask();
        compile.setup(ctx_, p, params);
        
        if (compile.hadSetupException()) {
          super.finish(compile.getLastSetupException());
          return;
        }
        waitForDependentProjectsToFinish(p);
        compile.execute();
        //TODO this system should be passed not created
        I_GitCalls calls = new GitCalls(new FabSystem());
        String projectVersion = calls.describe( compile.getProjectPath());
        OldJarTask jar = new OldJarTask();
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
        
        OldDepositTask deposit = new OldDepositTask();
        deposit.setup(ctx_, p, params);
        if (deposit.hadSetupException()) {
          super.finish(deposit.getLastSetupException());
          return;
        }
        
        String fileName = projectsPath_ + File.separator + projectName + 
            File.separator + "build" + File.separator + jarName;
        log_.println(this.getClass().getSimpleName() + " adding jar to depot " +
            fileName);
       deposit.execute(fileName, "jar");
        
        
        setFinisedStage(projectName);
        projectsFinished_.incrementAndGet();
        p = projectsQueue_.poll();
      }
      
      if (didOne) {
        if (log_.isLogEnabled(CompileJarAndDeposit.class)) {
          log_.println( " projectsQueueSize_ = " + projectsQueueSize_ +
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
      if (log_.isLogEnabled(CompileJarAndDeposit.class)) {
        log_.println(this.getClass().getSimpleName() + " finishing.");
      }
      I_Depot depot =  ctx_.getDepot();
      depot.store();
      super.finish();
      semaphore_.release();
    } catch (InterruptedException x) {
      //do nothing this thread didn't acquire the lock
    }
  }
*/


  
}
