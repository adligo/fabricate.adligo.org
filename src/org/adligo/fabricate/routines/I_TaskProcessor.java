package org.adligo.fabricate.routines;

/**
 * Routines that process tasks should impelemtent this
 * interface to provide Fabricate with information about
 * what is happening, or happened.
 * 
 * NOTE this interface should be implemented by all 
 * child classes, when the parent class implements the interface.
 * 
 * @author scott
 *
 */
public interface I_TaskProcessor {
  /**
   * The current task name from fabricate.xml
   * or the last task name, when processing has 
   * completed (or failed).
   * @return
   */
  public String getCurrentTask();
}
