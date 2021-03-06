package org.adligo.fabricate.depot;

import java.io.IOException;

public interface I_Depot {

  /**
   * adds something to the depot for other projects to reference
   * @param the absolutePath to the external file that is getting added to the depot
   * @param entryData the data about the entry 
   */
  public boolean add(String externalFile, I_Artifact entryData);
  
  /**
   * remove a projects artifact from the depot
   * @param entryData
   * @return
   */
  public boolean deleteIfPresent(I_Artifact entryData);
  
  /**
  * @param projectName
  * @param artifactType the type of artifact (i.e. jar, war, sar exc)
  * @param platformName
  * @return the full absolute path name for the jar
  * added to the depot by the project with the projectName parameter
  */
  public String get(String projectName, String artifactType, String platformName) ;

  /**
   * returns the absolute path of the 
   * depo directory
   * @return
   */
  public String getDir();
  
  /**
   * If the depot is aware of the project artifacts this returns true.
   * @param projectName
   * @return
   */
  public boolean has(String projectName);
  
  /**
   * this writes out the depot.xml file
   * for other runs, and should not be called my multiple threads
   * only once at the end of each stage (during writeMemory)/
   */
  public void store();
}
