package org.adligo.fabricate.routines;

import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.common.I_FabricationMemory;
import org.adligo.fabricate.models.common.I_FabricationMemoryMutant;
import org.adligo.fabricate.models.common.I_FabricationRoutine;
import org.adligo.fabricate.models.common.I_RoutineMemory;
import org.adligo.fabricate.models.common.I_RoutineMemoryMutant;

/**
 * This interface is used to create and setup
 * I_FabricationRoutines when the RoutineExecutor
 * is deciding how to run them (on this thread, or
 * in a thread pool).  This allows the client of
 * RoutineExecutor to do any special setup
 * that may be necessary.
 *   If the RoutineExecutor is running the
 * routines on multiple threads, it builds a initial
 * routine using shared memory which it then
 * makes immutable and passes to the subsequent
 * routines.
 * @author scott
 *
 */
public interface I_RoutineBuilder {


  /**
   * The first routine for a routine on the current thread or
   * in group of concurrent routines.
   * @param memory this is global memory for the entire fabrication.
   * @param routineMemory this is memory scoped to a set of routine instances of the same class.
   * @return
   * @throws FabricationRoutineCreationException
   */
  public I_FabricationRoutine buildInitial(I_FabricationMemoryMutant<Object> memory, 
      I_RoutineMemoryMutant<Object> routineMemory) throws FabricationRoutineCreationException;
  /**
   * The second and subsequent routines for a run of a
   * group of concurrent routine instances are created using 
   * this method.
   * @param memory this is global memory for the entire fabrication.
   * @param routineMemory this is memory scoped to a set of routine instances of the same class.
   * @param
   * @return
   * @throws FabricationRoutineCreationException
   */
  public I_FabricationRoutine build(I_FabricationMemory<Object> memory, 
      I_RoutineMemory<Object> routineMemory) throws FabricationRoutineCreationException;
}
