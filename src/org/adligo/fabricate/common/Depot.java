package org.adligo.fabricate.common;

/**
 * This class manages the depot directory 
 * which consists of a depot.xml file
 * 
 * @author scott
 *
 */
public class Depot implements I_Depot {

  public synchronized boolean exists() {
    return false;
  }
  
  public synchronized void create() {
    
  }
  
  @Override
  public synchronized void clean() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public synchronized void add(I_DepotInput input) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public synchronized void remove(I_DepotInput input) {
    // TODO Auto-generated method stub
    
  }

}
