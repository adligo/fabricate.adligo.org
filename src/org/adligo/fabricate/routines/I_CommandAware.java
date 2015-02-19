package org.adligo.fabricate.routines;

import org.adligo.fabricate.models.common.I_FabricationRoutine;
import org.adligo.fabricate.models.common.I_RoutineFactory;

/**
 * Commands that implement this interface may call
 * other commands.  This allows for lists of commands
 * (like a ordered script), and also concurrent
 * command execution.
 * @author scott
 *
 */
public interface I_CommandAware extends I_FabricationRoutine {
  public I_RoutineFactory getCommandFactory();
  public void setCommandFactory(I_RoutineFactory factory);
}
