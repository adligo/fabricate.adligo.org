package org.adligo.fabricate.models.common;

import org.adligo.fabricate.common.i18n.I_SystemMessages;
import org.adligo.fabricate.common.log.I_FabLog;

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
  public static void log(I_FabLog log, I_SystemMessages messages, 
      FabricationRoutineCreationException x) {
    String message = messages.getThereWasAProblemCreatingTheFollowingRoutine();
    log.println(message);
    Class<?> c = x.getRoutine();
    if (c == null) {
      log.println("null");
    } else {
      log.println(c.getName());
    }
    Class<?> ei = x.getExpectedInterface();
    if (ei != null) {
      message = messages.getItWasExpectedToImplementTheFollowingInterface();
      log.println(message);
      log.println(ei.getName());
    }
    Class<?> egt = x.getExpectedGenericType();
    if (egt != null) {
      message = messages.getWithTheFollowingGenericTypeX();
      message = message.replaceAll("<X/>", "" + x.getWhichGenericType());
      log.println(message);
      log.println(egt.getName());
    }
    Class<?> agt = x.getActualGenericType();
    if (agt != null) {
      message = messages.getInsteadOfTheFollowingActualGenericType();
      log.println(message);
      log.println(agt.getName());
    }
    log.printTrace(x);
  }
  
  
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
