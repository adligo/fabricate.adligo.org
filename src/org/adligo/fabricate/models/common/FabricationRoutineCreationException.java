package org.adligo.fabricate.models.common;

/**
 * This exception helps export data
 * to the layer which has i18n constants available
 * for logging.
 * 
 * @author scott
 *
 */
public class FabricationRoutineCreationException extends InstantiationException {

  private static final long serialVersionUID = 1L;
  private Class<?> actualGenericType;
  private Class<?> routine;
  private Class<?> expectedInterface;
  private Class<?> expectedGenericType;
  private int whichGenericType = 0;
  
  public Class<?> getActualGenericType() {
    return actualGenericType;
  }
  
  public Class<?> getRoutine() {
    return routine;
  }
  public Class<?> getExpectedInterface() {
    return expectedInterface;
  }
  public Class<?> getExpectedGenericType() {
    return expectedGenericType;
  }
  public int getWhichGenericType() {
    return whichGenericType;
  }
  public void setRoutine(Class<?> routine) {
    this.routine = routine;
  }
  public void setExpectedInterface(Class<?> expectedInterface) {
    this.expectedInterface = expectedInterface;
  }
  public void setExpectedGenericType(Class<?> expectedGenericType) {
    this.expectedGenericType = expectedGenericType;
  }
  public void setWhichGenericType(int whichGenericType) {
    this.whichGenericType = whichGenericType;
  }

  public void setActualGenericType(Class<?> actualGenericType) {
    this.actualGenericType = actualGenericType;
  }

  @Override
  public synchronized Throwable initCause(Throwable cause) {
    if (cause instanceof FabricationRoutineCreationException) {
      FabricationRoutineCreationException other = (FabricationRoutineCreationException) cause;
      setActualGenericType(other.getActualGenericType());
      setExpectedGenericType(other.getExpectedGenericType());
      setExpectedInterface(other.getExpectedInterface());
      setExpectedGenericType(other.getExpectedGenericType());
      setRoutine(other.getRoutine());
      setWhichGenericType(other.getWhichGenericType());
    }
    return super.initCause(cause);
  }
}
