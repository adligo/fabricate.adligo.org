package org.adligo.fabricate;

import org.adligo.fabricate.common.FabConstantsDiscovery;
import org.adligo.fabricate.common.FabricateHelper;
import org.adligo.fabricate.common.LocalRepositoryHelper;
import org.adligo.fabricate.common.RunContextMutant;
import org.adligo.fabricate.common.en.FabricateEnConstants;
import org.adligo.fabricate.common.files.I_FabFileIO;
import org.adligo.fabricate.common.files.xml_io.I_FabXmlFileIO;
import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.i18n.I_SystemMessages;
import org.adligo.fabricate.common.log.ThreadLocalPrintStream;
import org.adligo.fabricate.common.system.CommandLineArgs;
import org.adligo.fabricate.common.system.FabSystem;
import org.adligo.fabricate.common.system.FabricateXmlDiscovery;
import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.common.util.StringUtils;
import org.adligo.fabricate.external.DefaultRepositoryPathBuilder;
import org.adligo.fabricate.external.ManifestParser;
import org.adligo.fabricate.external.RepositoryDownloader;
import org.adligo.fabricate.external.RepositoryDownloaderFactory;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.FabricateDependencies;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.FabricateType;
import org.adligo.fabricate.xml.io_v1.library_v1_0.DependencyType;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class FabricateOptsSetup {
  
  private final I_FabSystem sys_;
  private final I_FabFileIO files_;
  private final I_FabXmlFileIO xmlFiles_;
  private String language_;
  private String country_;
  private I_FabricateConstants constants_;
  private FabricateType fab_;
  private final RepositoryDownloaderFactory factory_;
  
  /**
   * @diagram_sync on 1/26/2014 with Overview.seq
   * @param args
   */
	@SuppressWarnings("unused")
  public static void main(String [] args) {
	  new FabricateOptsSetup(args, new FabSystem(), new RepositoryDownloaderFactory());
	}
	
	public FabricateOptsSetup(String [] args, FabSystem sys, RepositoryDownloaderFactory factory) {
	  sys_ = sys;
	  files_ = sys.getFileIO();
    xmlFiles_ = sys.getXmlFileIO();
    factory_ = factory;
    Map<String,String> argMap = CommandLineArgs.parseArgs(args);
    sys.setDebug(argMap.containsKey("debug"));
    
    
    
	  language_ = sys.getDefaultLanguage();
	  country_ = sys.getDefaultCountry();
	  
	  String argLoc = argMap.get(CommandLineArgs.LOCALE);
	  if (argLoc != null) {
	    StringTokenizer st = new StringTokenizer(argLoc, "_");
	    String tempLang = st.nextToken();
	    String tempCountry = st.nextToken();
	    language_ = tempLang;
	    country_ = tempCountry;
	  }
	  try {
	    constants_ = new FabConstantsDiscovery(language_, country_);
	  } catch (IOException x) {
	    constants_ = FabricateEnConstants.INSTANCE;
	  }
	 
    String fabHome = System.getenv("FABRICATE_HOME");
    
    FabricateXmlDiscovery fd = new FabricateXmlDiscovery(sys_);
    if (!fd.hasFabricateXml()) {
      ThreadLocalPrintStream.println(CommandLineArgs.MESSAGE);
      I_SystemMessages sysMessages = constants_.getSystemMessages();
      ThreadLocalPrintStream.println(sysMessages.getExceptionNoFabricateXmlOrProjectXmlFound());
    } else {
      
      String version = argMap.get("java");
      if (StringUtils.isEmpty(version)) {
        ThreadLocalPrintStream.println(CommandLineArgs.MESSAGE);
        I_SystemMessages sysMessages = constants_.getSystemMessages();
        ThreadLocalPrintStream.println(sysMessages.getExceptionJavaVersionParameterExpected());
        return;
      }
      workWithFabricateXml(fabHome, fd, argMap);
    }
  }

	/**
	 * @diagram_sync on 1/26/2014 with Overview.seq
	 * @param fabHome
	 * @param fd
	 * @param argMap
	 */
  private void workWithFabricateXml(String fabHome, FabricateXmlDiscovery fd, 
      Map<String,String> argMap) {
    String fabricateXmlPath = files_.instance(fd.getFabricateXmlPath()).getAbsolutePath();
    
    try {
      fab_ =  xmlFiles_.parseFabricate_v1_0(fabricateXmlPath);
      FabricateHelper fh = new FabricateHelper(fab_);
     
      //@diagram_sync on 1/26/2014 with Overview.seq
      downloadFabricateRunClasspathDependencies();
      //@diagram_sync on 1/26/2014 with Overview.seq
      String classpath = buildClasspath(fabricateXmlPath, fabHome);
      //@diagram_sync on 1/26/2014 with Overview.seq
      sendOptsToScript(fh, classpath);
    } catch (IOException e) {
      ThreadLocalPrintStream.println(CommandLineArgs.MESSAGE);
      ThreadLocalPrintStream.printTrace(e);
    }
  }

  /**
   * sendOptsToScript(FabricateHelper fh, String classpath)
   * @param fh
   * @param classpath
   */
  private void sendOptsToScript(FabricateHelper fh, String classpath) {
    ThreadLocalPrintStream.println(" -Xmx" + fh.getXmx() + " -Xms" + fh.getXms() + " -cp " + 
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
        RunContextMutant fcm = new RunContextMutant();
        fcm.setLocalRepositoryPath(localRepository);
        //leave logs empty here
        List<String> repos = deps.getRemoteRepository();
        RepositoryDownloader rdl = factory_.create(fcm.getLog(), fcm.getLocalRepositoryPath());
        rdl.setPathBuilder(new DefaultRepositoryPathBuilder(localRepository, File.separator));
        rdl.setRepositories(repos);
       
        for (DependencyType dep: depTypes) {
          rdl.findOrDownloadAndMd5(dep);
        }
      }
    }
  }

  public I_FabricateConstants getConstants() {
    return constants_;
  }

}
