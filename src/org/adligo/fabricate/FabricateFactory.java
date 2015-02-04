package org.adligo.fabricate;

import org.adligo.fabricate.common.system.FabricateXmlDiscovery;
import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.repository.DefaultRepositoryPathBuilder;
import org.adligo.fabricate.repository.I_RepositoryPathBuilder;
import org.adligo.fabricate.repository.I_RepositoryPathBuilderFactory;
import org.adligo.fabricate.repository.RepositoryManager;

public class FabricateFactory implements I_RepositoryPathBuilderFactory {

  public FabricateXmlDiscovery createDiscovery(I_FabSystem sys) {
    return new FabricateXmlDiscovery(sys);
  }
  
  public RepositoryManager createRepositoryManager(I_FabSystem sys, String localRepositoryPath) {
    return new RepositoryManager(sys, localRepositoryPath);
  }

  @Override
  public I_RepositoryPathBuilder create(String remoteRepository) {
    return new DefaultRepositoryPathBuilder(remoteRepository, "/");
  }
}
