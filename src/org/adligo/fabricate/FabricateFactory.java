package org.adligo.fabricate;

import org.adligo.fabricate.common.system.FabricateEnvironment;
import org.adligo.fabricate.common.system.FabricateXmlDiscovery;
import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.models.dependencies.I_Dependency;
import org.adligo.fabricate.models.fabricate.Fabricate;
import org.adligo.fabricate.models.fabricate.FabricateMutant;
import org.adligo.fabricate.models.fabricate.I_Fabricate;
import org.adligo.fabricate.models.fabricate.I_FabricateXmlDiscovery;
import org.adligo.fabricate.repository.DefaultRepositoryPathBuilder;
import org.adligo.fabricate.repository.DependenciesManager;
import org.adligo.fabricate.repository.DependencyManager;
import org.adligo.fabricate.repository.I_DependenciesManager;
import org.adligo.fabricate.repository.I_DependencyManager;
import org.adligo.fabricate.repository.I_LibraryResolver;
import org.adligo.fabricate.repository.I_RepositoryFactory;
import org.adligo.fabricate.repository.I_RepositoryPathBuilder;
import org.adligo.fabricate.repository.LibraryResolver;
import org.adligo.fabricate.repository.RepositoryManager;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.FabricateDependencies;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.FabricateType;
import org.adligo.fabricate.xml.io_v1.library_v1_0.LibraryReferenceType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FabricateFactory implements I_RepositoryFactory {

  public Fabricate create(I_FabSystem sys, FabricateType fab, I_FabricateXmlDiscovery xmlDisc) {
    FabricateMutant fm = new FabricateMutant(fab, xmlDisc);
    String fabHome = FabricateEnvironment.INSTANCE.getFabricateHome(sys);
    fm.setFabricateHome(fabHome);
    String fabRepository = FabricateEnvironment.INSTANCE.getFabricateRepository(sys);
    fm.setFabricateRepository(fabRepository);
    String javaHome = FabricateEnvironment.INSTANCE.getJavaHome(sys);
    fm.setJavaHome(javaHome);
    //return new Fabricate(fm);
    Fabricate tf = new Fabricate(fm);
    FabricateDependencies deps = fab.getDependencies();
    List<LibraryReferenceType> lrts = deps.getLibrary();
    if (lrts.size() >= 1) {
      LibraryResolver lr = new LibraryResolver(sys,tf);
      List<I_Dependency> imutDeps = lr.getDependencies(lrts);
      List<I_Dependency> fmDeps = fm.getDependencies();
      List<I_Dependency> allDeps = new ArrayList<I_Dependency>();
      allDeps.addAll(imutDeps);
      allDeps.addAll(fmDeps);
      fm.setDependencies(allDeps);
      return new Fabricate(fm);
    }
    return tf;
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
  public I_RepositoryPathBuilder createRepositoryPathBuilder(String remoteRepository) {
    return new DefaultRepositoryPathBuilder(remoteRepository, "/");
  }
  
  @Override
  public I_RepositoryPathBuilder createRepositoryPathBuilder(String remoteRepository, String separator) {
    return new DefaultRepositoryPathBuilder(remoteRepository, separator);
  }

  @Override
  public I_LibraryResolver createLibraryResolver(I_FabSystem sys, I_Fabricate fabricate) {
    return new LibraryResolver(sys, fabricate);
  }
}
