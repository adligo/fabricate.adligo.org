package org.adligo.fabricate.routines;

/**
 * note the I_RoutineProcessorFactory should not be
 * cast to other classes, since it will eventually be 
 * blocked by fabricate.
 * 
 * @author scott
 *
 */
public interface I_RoutineProcessorFactoryAware {
  public I_RoutineProcessorFactory getRoutineFabricateFactory();
  public void setRoutineFabricateFactory(I_RoutineProcessorFactory factory);
}