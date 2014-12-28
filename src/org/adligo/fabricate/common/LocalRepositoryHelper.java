package org.adligo.fabricate.common;

import org.adligo.fabricate.xml.io.v1_0.FabricateDependencies;
import org.adligo.fabricate.xml.io.v1_0.FabricateType;

import java.io.File;

public class LocalRepositoryHelper {

  /**
   * this identifies the local_repository path
   * and creates it if it is NOT there.
   * @param fab
   * @return
   */
  @SuppressWarnings("boxing")
  public String getRepositoryPath(FabricateType fab) {
    FabricateDependencies fabDeps = fab.getDependencies();
    String localRepository = fabDeps.getLocalRepository();
    if (localRepository == null) {
      String userHome = System.getProperty("user.home");
      localRepository = userHome + File.separator + "local_repository" ;
    } else {
      int length = localRepository.length();
      if (File.separator.equals(localRepository.charAt(length))) {
        localRepository = localRepository.substring(0, length - 2);
      }
    }
    File file = new File(localRepository);
    if (!file.exists()) {
      if (!file.mkdirs()) {
        throw new RuntimeException("There was a problem creating " + file);
      }
    }
    return localRepository + File.separator;
  }
  
}
