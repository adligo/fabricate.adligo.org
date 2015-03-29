package org.adligo.fabricate;

import org.adligo.fabricate.common.files.I_FabFileIO;
import org.adligo.fabricate.common.files.xml_io.I_FabXmlFileIO;
import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.i18n.I_SystemMessages;
import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.common.system.CommandLineArgs;
import org.adligo.fabricate.common.system.FabSystem;
import org.adligo.fabricate.common.system.FabSystemSetup;
import org.adligo.fabricate.common.system.FabricateXmlDiscovery;
import org.adligo.fabricate.common.util.StringUtils;
import org.adligo.fabricate.models.dependencies.I_Dependency;
import org.adligo.fabricate.models.fabricate.Fabricate;
import org.adligo.fabricate.models.fabricate.I_FabricateXmlDiscovery;
import org.adligo.fabricate.models.fabricate.I_JavaSettings;
import org.adligo.fabricate.repository.DependencyNotAvailableException;
import org.adligo.fabricate.repository.I_DependenciesNormalizer;
import org.adligo.fabricate.repository.I_RepositoryManager;
import org.adligo.fabricate.repository.I_RepositoryPathBuilder;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.FabricateType;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FabricateOptsSetup {
  
  private final FabSystem sys_;
  private final I_FabLog log_;
  private final I_FabFileIO files_;
  private final I_FabXmlFileIO xmlFiles_;
  private I_FabricateConstants constants_;
  private Fabricate fab_;
  private final FabricateFactory factory_;
  
  /**
   * @diagram_sync on 1/26/2014 with Overview.seq
   * Note this method lets Runtime Exceptions propagate
   * out of the method, the JVM prints exception traces to the console.
   * @param args
   */
	@SuppressWarnings("unused")
  public static void main(String [] args) {
	  new FabricateOptsSetup(args, new FabSystem(), new FabricateFactory());
	}
	
	public FabricateOptsSetup(String [] args, FabSystem sys, FabricateFactory factory) {
	  sys_ = sys;
	  files_ = sys.getFileIO();
    xmlFiles_ = sys.getXmlFileIO();
    factory_ = factory;
    FabSystemSetup.setup(sys, args);
    log_ = sys.getLog();
    constants_ = sys.getConstants();

    
    FabricateXmlDiscovery fd = factory.createDiscovery(sys);
    
    if (!fd.hasFabricateXml()) {
      
      I_SystemMessages sysMessages = constants_.getSystemMessages();
      log_.println(sysMessages.getExceptionNoFabricateXmlOrProjectXmlFound());
      log_.println(CommandLineArgs.END);
      return;
    } else {
      FabSystemSetup.checkManditoryInjectedArgs(sys);
      workWithFabricateXml(fd);
    }
  }

	/**
	 * @diagram_sync on 1/26/2014 with Overview.seq
	 * @param fabHome
	 * @param fd
	 * @param argMap
	 */
  private void workWithFabricateXml(I_FabricateXmlDiscovery fd) {
    String xmlPath = fd.getFabricateXmlPath();
    File fabXml = files_.instance(xmlPath);
    String fabricateXmlPath = fabXml.getAbsolutePath();
    
    try {
      FabricateType fabX =  xmlFiles_.parseFabricate_v1_0(fabricateXmlPath);
      FabSystemSetup.setup(sys_, fabX);
      fab_ = factory_.create(sys_, fabX, fd);
      
      //@diagram_sync on 1/26/2014 with Overview.seq
      if (!manageFabricateRuntimeClasspathDependencies()) {
        return;
      }
      
      //@diagram_sync on 1/26/2014 with Overview.seq
      String classpath = buildFabricateRuntimeClasspath();
     
      //@diagram_sync on 1/26/2014 with Overview.seq
      sendOptsToScript(classpath);
    } catch (IOException | ClassNotFoundException e) {
      log_.printTrace(e);
      log_.println(CommandLineArgs.END);
      return;
    }
  }

  /**
   * sendOptsToScript(FabricateHelper fh, String classpath)
   * @param fh
   * @param classpath
   */
  private void sendOptsToScript(String classpath) {
    if (log_.isLogEnabled(FabricateOptsSetup.class)) {
      I_SystemMessages messages = constants_.getSystemMessages();
      log_.println(messages.getSendingOptsToScript());
    }
    log_.println(CommandLineArgs.LASTLINE + "-Xmx" + fab_.getXmx() + " -Xms" + fab_.getXms() + " -cp " + 
        classpath);
  }


  /**
   * @diagram_sync on 1/26/2014 with Overview.seq
   * @param fabricateXml
   * @param fabricateHome
   * @return
   */
  private String buildFabricateRuntimeClasspath() {
    if (log_.isLogEnabled(FabricateOptsSetup.class)) {
      I_SystemMessages messages = constants_.getSystemMessages();
      log_.println(messages.getBuildingFabricateRuntimeClassPath());
    }
    StringBuilder sb = new StringBuilder();
    
    String fabricateHome = fab_.getFabricateHome();
    sb.append(fabricateHome + files_.getNameSeparator() + 
        "lib" + files_.getNameSeparator() + "commons-logging-1.2.jar" + 
        sys_.getPathSeparator());
    sb.append(fabricateHome + files_.getNameSeparator() + 
        "lib" + files_.getNameSeparator() + "httpclient-4.3.5.jar" + 
        sys_.getPathSeparator());
    sb.append(fabricateHome + files_.getNameSeparator() + 
        "lib" + files_.getNameSeparator() + "httpcore-4.3.2.jar");
    //the script always adds fabricate_??.jar at the end
    
    List<I_Dependency> deps = fab_.getDependencies();
    if (deps.size() >= 1) {
      String repository = fab_.getFabricateRepository();
      I_RepositoryPathBuilder builder = factory_.createRepositoryPathBuilder(repository, files_.getNameSeparator());
      I_DependenciesNormalizer dn = factory_.createDependenciesNormalizer();
      dn.add(deps);
      ConcurrentLinkedQueue<I_Dependency> uDeps = dn.get();
      I_Dependency dep = uDeps.poll();
      while (dep != null) {
        sb.append(sys_.getPathSeparator());
        String path = builder.getArtifactPath(dep);
        sb.append(path);
        dep = uDeps.poll();
      }
    }
    String result = sb.toString();
    return result;
  }
  
  /**
   * 
   * This throws a fatal error when one of the 
   * dependencies requested for the fabricate 
   * class path can not be down loaded.
   * @diagram_sync on 1/26/2014 with Overview.seq
   * @param fab
   * @throws IOException
   */
  private boolean manageFabricateRuntimeClasspathDependencies() throws IOException {
    if (log_.isLogEnabled(FabricateOptsSetup.class)) {
      I_SystemMessages messages = constants_.getSystemMessages();
      log_.println(messages.getManagingFabricateRuntimeClassPathDependencies() +
          sys_.lineSeparator() + fab_.getDependencies().size());
    }
    List<I_Dependency> deps = fab_.getDependencies();
    if (deps.size() >= 1) {
      I_RepositoryManager rm = factory_.createRepositoryManager(sys_, fab_);
      ConcurrentLinkedQueue<I_Dependency> depQueue = sys_.newConcurrentLinkedQueue(I_Dependency.class);
      depQueue.addAll(deps);
      rm.addDependencies(depQueue);
      if (!rm.manageDependencies()) {
        IOException x = rm.getLocalException();
        if (x != null) {
          log_.printTrace(x);
          log_.println(CommandLineArgs.END);
          return false;
        }
        DependencyNotAvailableException caught = rm.getRemoteException();
        if (caught != null) {
          rm.logError(caught);
        }
        log_.println(CommandLineArgs.END);
        return false;
      }
    }
    return true;
  }

  public I_FabricateConstants getConstants() {
    return constants_;
  }

}
