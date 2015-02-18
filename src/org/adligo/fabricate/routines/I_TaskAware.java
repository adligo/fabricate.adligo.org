package org.adligo.fabricate.routines;

import java.util.List;

/**
 * This interface provides the task names,
 * so that a command, stage or trait can
 * use a I_TaskFactory to create runtime
 * instances of runnable code.
 * 
 * @author scott
 *
 */
public interface I_TaskAware {
  public List<String> getTasks();
  public void setTasks(List<String> tasks);
}
