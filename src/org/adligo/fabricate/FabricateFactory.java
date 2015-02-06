package org.adligo.fabricate;

import org.adligo.fabricate.common.system.FabricateEnvironment;
import org.adligo.fabricate.common.system.FabricateXmlDiscovery;
import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.models.dependencies.I_Dependency;
import org.adligo.fabricate.models.fabricate.Fabricate;
import org.adligo.fabricate.models.fabricate.FabricateMutant;
import org.adligo.fabricate.models.fabricate.I_Fabricate;
import org.adligo.fabricate.repository.DefaultRepositoryPathBuilder;
import org.adligo.fabricate.repository.DependenciesManager;
import org.adligo.fabricate.repository.DependencyManager;
import org.adligo.fabricate.repository.I_DependenciesManager;
import org.adligo.fabricate.repository.I_DependencyManager;
import org.adligo.fabricate.repository.I_RepositoryFactory;
import org.adligo.fabricate.repository.I_RepositoryPathBuilder;
import org.adligo.fabricate.repository.RepositoryManager;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.FabricateType;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FabricateFactory implements I_RepositoryFactory {

  public Fabricate create(I_FabSystem sys, FabricateType fab) {
    FabricateMutant fm = new FabricateMutant(fab);
    String fabHome = FabricateEnvironment.INSTANCE.getFabricateHome(sys);
    fm.setFabricateHome(fabHome);
    String fabRepository = FabricateEnvironment.INSTANCE.getFabricateRepository(sys);
    fm.setFabricateRepository(fabRepository);
    String javaHome = FabricateEnvironment.INSTANCE.getJavaHome(sys);
    fm.setJavaHome(javaHome);
    return new Fabricate(fm);
  }
  
  public FabricateXmlDiscovery createDiscovery(I_FabSystem sys) {
    return new FabricateXmlDiscovery(sys);
  }
  
  public I_DependenciesManager createDependenciesManager(I_FabSystem sys, 
      ConcurrentLinkedQueue<I_Dependency> deps) {
    return new DependenciesManager(sys, deps, this);
  }

  public I_DependencyManager createDependencyManager(
      I_FabSystem sys, List<String> repos, I_RepositoryPathBuilder builder) {
    return new DependencyManager(sys, repos, builder);
  }
  
  public RepositoryManager createRepositoryManager(I_FabSystem sys, I_Fabricate fab) {
    return new RepositoryManager(sys, fab, this);
  }

  @Override
  public I_RepositoryPathBuilder create(String remoteRepository) {
    return new DefaultRepositoryPathBuilder(remoteRepository, "/");
  }
  
  @Override
  public I_RepositoryPathBuilder create(String remoteRepository, String separator) {
    return new DefaultRepositoryPathBuilder(remoteRepository, separator);
  }
}
