package org.adligo.fabricate.common;
/**
 * I_FabTask is a interface to allow plug-able
 * tasks to be called from fabricate.  It is generally
 * assumed that I_FabTask's are NOT concurrent
 * and only one at a time will run on the fabricate
 * environment.
 * 
 * @author scott
 *
 */
public interface I_FabTask {
  public void execute(I_FabContext ctx);
}
