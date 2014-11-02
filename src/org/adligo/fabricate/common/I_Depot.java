package org.adligo.fabricate.common;

public interface I_Depot {

  /**
   * adds something to the depot for other projects to reference
   * @param the absolutePath to the external file that is getting added to the depot
   * @param entryData the data about the entry 
   */
  public boolean add(String externalFile, I_DepotEntry entryData);
  
  /**
   * @param projectName
   * @return the full abstract path name for the jar
   * added to the depot by the project with the projectName parameter
   */
  public String get(String projectName);
  /**
  * @param projectName
  * @param artifactType the type of artifact (i.e. jar, war, sar exc)
  * @return the full abstract path name for the jar
  * added to the depot by the project with the projectName parameter
  */
  public String get(String projectName, String artifactType);

  /**
   * returns the absolute path of the 
   * depo directory
   * @return
   */
  public String getDir();
  
  /**
   * this writes out the depot.xml file
   * for other runs, and should not be called my multiple threads
   * only once at the end of each stage
   */
  public void store();
}
