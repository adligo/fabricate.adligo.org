package org.adligo.fabricate.common;

import org.adligo.fabricate.xml.io.FabricateDependencies;
import org.adligo.fabricate.xml.io.FabricateType;

import java.io.File;

public class LocalRepositoryHelper {

  /**
   * this identifies the local_repository path
   * and creates it if it is NOT there.
   * @param fab
   * @return
   */
  public String getRepositoryPath(FabricateType fab) {
    FabricateDependencies fabDeps = fab.getDependencies();
    String localRepository = fabDeps.getLocalRepository();
    if (localRepository == null) {
      String userHome = System.getProperty("user.home");
      localRepository = userHome + File.separator + "local_repository";
    }
    File file = new File(localRepository);
    if (!file.exists()) {
      if (!file.mkdirs()) {
        throw new RuntimeException("There was a problem creating " + file);
      }
    }
    return localRepository;
  }
  
}
