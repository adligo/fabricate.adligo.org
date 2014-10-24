package org.adligo.fabricate.common;

public interface I_Depot {
  /**
   * cleans/removes all files from the depot
   */
  public void clean();
  /**
   * adds something to the depot for other projects to reference
   * @param input
   */
  public void add(I_DepotInput input);
}
