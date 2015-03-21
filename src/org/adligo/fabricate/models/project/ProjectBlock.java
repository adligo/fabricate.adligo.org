package org.adligo.fabricate.models.project;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class helps multiple threads communicate by keeping track of projects that are waiting
 * on other projects to finish before they start.  It is intended to be used in other concurrent collections 
 * 
 * @author scott
 *
 */
public class ProjectBlock {
  
  /**
   * The project that is waiting to start.
   */
  private final String project_;
  
  /**
   * The project which has started and must finish before the waiting project can start.
   */
  private final String blockingProject_;
  private final AtomicBoolean unblocked_;
  private final ArrayBlockingQueue<Boolean> block_;
  
  public ProjectBlock(String project, String blockingProject) {
    this(project,  blockingProject, new ArrayBlockingQueue<Boolean>(1));
  }
  
  public ProjectBlock(String project, String blockingProject, ArrayBlockingQueue<Boolean> block) {
    project_ = project;
    blockingProject_ = blockingProject;
    block_ = block;
    unblocked_ = new AtomicBoolean(false);
  }

  public String getProject() {
    return project_;
  }

  public String getBlockingProject() {
    return blockingProject_;
  }
  
  /**
   * @param milliseconds
   * @return
   *  if the blockingProject is running this method returns false.
   *  if the blockingProject is finished running this method returns true.
   * @throws InterruptedException
   */
  public synchronized boolean waitUntilUnblocked(int milliseconds) throws InterruptedException {
    if (unblocked_.get()) {
      return true;
    }
    Boolean unblocked = block_.poll(milliseconds, TimeUnit.MILLISECONDS);
    if (unblocked != null) {
      unblocked_.set(true);
      return true;
    }
    return false;
  }
  
  public void setProjectFinished() throws InterruptedException {
    block_.put(Boolean.TRUE);
  }

  @Override
  public String toString() {
    return "ProjectBlock [project=" + project_ + ", blockingProject=" + blockingProject_
        + ", blocked=" + !unblocked_.get() + "]";
  }

}
