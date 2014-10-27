package org.adligo.fabricate.common;

public interface I_Depot {
  /**
   * determine if the depot directory exists
   * and if there is a depot.xml file
   * @return
   */
  public boolean exists();
  /**
   * create the the depot directory 
   * with a new depot.xml
   */
  public void create();
  
  /**
   * cleans/removes all files from the depot
   */
  public void clean();
  /**
   * adds something to the depot for other projects to reference
   * @param input
   */
  public void add(I_DepotInput input);
  /**
   * removes something to the depot for other projects to reference
   * @param input
   */
  public void remove(I_DepotInput input);
}
