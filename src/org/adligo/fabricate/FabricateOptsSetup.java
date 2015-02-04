package org.adligo.fabricate;

import org.adligo.fabricate.common.LocalRepositoryHelper;
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
import org.adligo.fabricate.models.dependencies.Dependency;
import org.adligo.fabricate.models.fabricate.I_JavaSettings;
import org.adligo.fabricate.models.fabricate.JavaSettings;
import org.adligo.fabricate.repository.DefaultRepositoryPathBuilder;
import org.adligo.fabricate.repository.RepositoryManager;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.FabricateDependencies;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.FabricateType;
import org.adligo.fabricate.xml.io_v1.library_v1_0.DependencyType;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FabricateOptsSetup {
  
  private final FabSystem sys_;
  private final I_FabLog log_;
  private final I_FabFileIO files_;
  private final I_FabXmlFileIO xmlFiles_;
  private I_FabricateConstants constants_;
  private FabricateType fab_;
  private final FabricateFactory factory_;
  
  /**
   * @diagram_sync on 1/26/2014 with Overview.seq
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
    String fabHome = System.getenv("FABRICATE_HOME");
    
    FabricateXmlDiscovery fd = factory.createDiscovery(sys);
    
    if (!fd.hasFabricateXml()) {
      
      I_SystemMessages sysMessages = constants_.getSystemMessages();
      log_.println(sysMessages.getExceptionNoFabricateXmlOrProjectXmlFound());
      log_.println(CommandLineArgs.END);
      return;
    } else {
      
      String version = sys.getArgValue("java");
      if (StringUtils.isEmpty(version)) {
        I_SystemMessages sysMessages = constants_.getSystemMessages();
        log_.println(sysMessages.getExceptionJavaVersionParameterExpected());
        log_.println(CommandLineArgs.END);
        return;
      }
      workWithFabricateXml(fabHome, fd);
    }
  }

	/**
	 * @diagram_sync on 1/26/2014 with Overview.seq
	 * @param fabHome
	 * @param fd
	 * @param argMap
	 */
  private void workWithFabricateXml(String fabHome, FabricateXmlDiscovery fd) {
    String fabricateXmlPath = files_.instance(fd.getFabricateXmlPath()).getAbsolutePath();
    
    try {
      fab_ =  xmlFiles_.parseFabricate_v1_0(fabricateXmlPath);
      FabSystemSetup.setup(sys_, fab_);
      I_JavaSettings fh = new JavaSettings(fab_.getJava());
     
      //@diagram_sync on 1/26/2014 with Overview.seq
      downloadFabricateRunClasspathDependencies();
      //@diagram_sync on 1/26/2014 with Overview.seq
      String classpath = buildClasspath(fabricateXmlPath, fabHome);
      //@diagram_sync on 1/26/2014 with Overview.seq
      sendOptsToScript(fh, classpath);
    } catch (IOException e) {
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
  private void sendOptsToScript(I_JavaSettings fh, String classpath) {
    log_.println(" -Xmx" + fh.getXmx() + " -Xms" + fh.getXms() + " -cp " + 
        classpath);
  }


  /**
   * @diagram_sync on 1/26/2014 with Overview.seq
   * @param fabricateXml
   * @param fabricateHome
   * @return
   */
  private String buildClasspath(String fabricateXml, String fabricateHome) {
    StringBuilder sb = new StringBuilder();
    
    File file = new File(fabricateHome + File.separator + "lib");
    if (file.exists()) {
      String [] files = file.list();
      if (files != null) {
        for (int i = 0; i < files.length; i++) {
          if (i != 0) {
            sb.append(File.pathSeparator);
          }
          sb.append(file.getAbsolutePath() + File.separator + files[i]);
        }
      }
    }
    int idx = fabricateXml.indexOf("fabricate.xml");
    String fabricatePath = fabricateXml.substring(0, idx);
    File fabLib = new File(fabricatePath + File.separator + "lib");
    if (fabLib.exists()) {
      String [] files = fabLib.list();
      if (files != null) {
        for (int i = 0; i < files.length; i++) {
          sb.append(File.pathSeparator + file.getAbsolutePath() + File.separator + files[i]);
        }
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
  private void downloadFabricateRunClasspathDependencies() throws IOException {
    FabricateDependencies deps =  fab_.getDependencies();
    if (deps != null) {
      List<DependencyType> depTypes = deps.getDependency();
      if (depTypes != null) {
        LocalRepositoryHelper lrh = new LocalRepositoryHelper();
        String localRepository = lrh.getRepositoryPath(fab_);
        
        //leave logs empty here
        List<String> repos = deps.getRemoteRepository();
        RepositoryManager rdl = factory_.createRepositoryManager(sys_, localRepository);
        rdl.setPathBuilder(new DefaultRepositoryPathBuilder(localRepository, File.separator));
        rdl.setRepositories(repos);
       
        for (DependencyType dep: depTypes) {
          rdl.findOrDownloadAndVerify(new Dependency(dep));
        }
      }
    }
  }

  public I_FabricateConstants getConstants() {
    return constants_;
  }

}
