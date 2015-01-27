package org.adligo.fabricate;

import org.adligo.fabricate.common.FabConstantsDiscovery;
import org.adligo.fabricate.common.FabricateHelper;
import org.adligo.fabricate.common.LocalRepositoryHelper;
import org.adligo.fabricate.common.RunContextMutant;
import org.adligo.fabricate.common.en.FabricateEnConstants;
import org.adligo.fabricate.common.files.I_FabFileIO;
import org.adligo.fabricate.common.files.PatternFileMatcher;
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
import org.adligo.fabricate.external.JavaCalls;
import org.adligo.fabricate.external.ManifestParser;
import org.adligo.fabricate.external.RepositoryDownloader;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.FabricateDependencies;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.FabricateType;
import org.adligo.fabricate.xml.io_v1.library_v1_0.DependencyType;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

public class FabricateSetup {
  private static JavaCalls JAVA_CALLS = JavaCalls.INSTANCE;
  
  private ManifestParser manifestParser_;
  private final I_FabSystem sys_;
  private final I_FabFileIO files_;
  private final I_FabXmlFileIO xmlFiles_;
  private String language_;
  private String country_;
  private I_FabricateConstants constants_;
  private FabricateType fab_;
  
  /**
   * @diagram_sync on 1/18/2014 with Overview.seq
   * @param args
   */
	@SuppressWarnings("unused")
  public static void main(String [] args) {
	  new FabricateSetup(args, new FabSystem(), new ManifestParser());
	}
	
	public FabricateSetup(String [] args, FabSystem sys, ManifestParser manifestParser) {
	  sys_ = sys;
	  manifestParser_ = manifestParser;
	  files_ = sys.getFileIO();
    xmlFiles_ = sys.getXmlFileIO();
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
	 
    if (argMap.containsKey("args")) {
      doArgs(argMap);
    } else if (argMap.containsKey("opts")) {
      doOpts(argMap);
    }
	}

 

  private void workWithFabricateXml(String fabHome, FabricateXmlDiscovery fd, 
      Map<String,String> argMap) {
    String fabricateXmlPath = new File(fd.getFabricateXmlPath()).getAbsolutePath();
    
    try {
      fab_ =  xmlFiles_.parseFabricate_v1_0(fabricateXmlPath);
      FabricateHelper fh = new FabricateHelper(fab_);
     
      downloadFabricateRunClasspathDependencies();
      
      System.out.println(" -Xmx" + fh.getXmx() + " -Xms" + fh.getXms() + " -cp " + 
          buildClasspath(fabricateXmlPath, fabHome));
    } catch (IOException e) {
      //this should also de-rail the fabricate run, as there will be no classpath
      e.printStackTrace();
    }
  }

  private void doArgs(Map<String,String> argMap) {
    long now = sys_.getCurrentTime();
    
    String homeDir = null;
    try {
       homeDir = JAVA_CALLS.getJavaHome();
    } catch (IllegalStateException x) {
      ThreadLocalPrintStream.println(CommandLineArgs.MESSAGE);
      I_SystemMessages sysMessages = constants_.getSystemMessages();
      String message = sysMessages.getExceptionNoJavaHomeSet();
      ThreadLocalPrintStream.println(message);
      return;
    }
    String versionNbrString = null;
    try {
      versionNbrString = JAVA_CALLS.getJavaVersion(homeDir, files_.getNameSeparator());
      double versionDouble = JAVA_CALLS.getJavaMajorVersion(versionNbrString);
      if (versionDouble < 1.7) {
        ThreadLocalPrintStream.println(CommandLineArgs.MESSAGE);
        I_SystemMessages sysMessages = constants_.getSystemMessages();
        String message = sysMessages.getExceptionFabricateRequiresJava1_7OrGreater();
        ThreadLocalPrintStream.println(message);
      }
    } catch (IOException | InterruptedException e) {
      ThreadLocalPrintStream.println(CommandLineArgs.MESSAGE);
      I_SystemMessages sysMessages = constants_.getSystemMessages();
      String message = sysMessages.getExceptionExecutingJavaWithTheFollowingJavaHome();
      ThreadLocalPrintStream.println(
          message + constants_.getLineSeperator() +
          homeDir);
      ThreadLocalPrintStream.printTrace(e);
      return;
    }
    File fabricateJar = locateFabricateJarAndVerifyFabricateHomeJars();
    
    if (fabricateJar != null) {
      if (hasDisplayVersionArg(argMap)) {
        displayFabricateJarManifestVersionAndEnd(fabricateJar);
      } else {
        sendOptsToScript(now, versionNbrString);
      }
    }
   
  }

  private void doOpts(Map<String,String> argMap) {
    String fabHome = System.getenv("FABRICATE_HOME");
    //@diagram_sync on 1/18/2014 with Overview.seq
    FabricateXmlDiscovery fd = new FabricateXmlDiscovery(sys_);
    if (!fd.hasFabricateXml()) {
      System.out.println("Exception no fabricate.xml or project.xml found.");
    } else {
      
      String version = argMap.get("java");
      if (StringUtils.isEmpty(version)) {
        
        ThreadLocalPrintStream.println("Java version parameter expected.");
        return;
      }
      workWithFabricateXml(fabHome, fd, argMap);
    }
  }
  
  public boolean hasDisplayVersionArg(Map<String, String> argMap) {
    I_SystemMessages messages = constants_.getSystemMessages();
    if ( argMap.containsKey(messages.getClaVersion())) {
      return true;
    }
    if (argMap.containsKey(messages.getClaVersionShort())) {
      return true;
    }
    return false;
  }

  public void sendOptsToScript(long now, String javaVersionNbr) {
    ThreadLocalPrintStream.println("start=" + now + " java=" + javaVersionNbr );
  }

  public File locateFabricateJarAndVerifyFabricateHomeJars() {
    String fabricateHome = sys_.getenv("FABRICATE_HOME");
    if (fabricateHome == null) {
      ThreadLocalPrintStream.println(CommandLineArgs.MESSAGE);
      I_SystemMessages sysMessages = constants_.getSystemMessages();
      String message = sysMessages.getExceptionNoFabricateHomeSet();
      ThreadLocalPrintStream.println(message);
      return null;
    } else {
      if (sys_.isDebug()) {
        ThreadLocalPrintStream.println("FABRICATE_HOME is;");
        ThreadLocalPrintStream.println(fabricateHome);
      }
    }
    File fabricateJar = null;
    boolean hasLogging = false;
    boolean hasHttpCore = false;
    boolean hasHttpClient = false;
    try {
      List<String> files = files_.list(fabricateHome + files_.getNameSeparator() + "lib", 
          new PatternFileMatcher(files_, sys_, "*.jar", true));
      
      if (files.size() != 4) {
        logTheFollowingFabricateHomeLibShouldHaveTheseJars(fabricateHome);
        if (sys_.isDebug()) {
          ThreadLocalPrintStream.println("files " + files);
          ThreadLocalPrintStream.println("files_.getNameSeparator()  " + files_.getNameSeparator());
        }
        return null;
      }
      
      for (String absPath: files) {
        File file = files_.instance(absPath);
        String fileName = file.getName();
        if (fileName.indexOf("fabricate_") == 0 && fileName.indexOf(".jar") != -1) {
          fabricateJar = file;
        } else if (fileName.indexOf("commons-logging-1.2.jar") == 0) {
          hasLogging = true;
        } else if (fileName.indexOf("httpclient-4.3.5.jar") == 0) {
          hasHttpClient = true;
        } else if (fileName.indexOf("httpcore-4.3.2.jar") == 0) {
          hasHttpCore = true;
        }
      }
    } catch (IOException x) {
      logTheFollowingFabricateHomeLibShouldHaveTheseJars(fabricateHome);
      ThreadLocalPrintStream.printTrace(x);
      return null;
    }
    if (fabricateJar == null) {
      ThreadLocalPrintStream.println(CommandLineArgs.MESSAGE);
      I_SystemMessages sysMessages = constants_.getSystemMessages();
      String message = sysMessages.getExceptionNoFabricateJarInFabricateHomeLib();
      ThreadLocalPrintStream.println(message);
      ThreadLocalPrintStream.println(fabricateHome);
      return null;
    }
    //NOTE fabricate_*.jar is checked for first
    if (!hasLogging || !hasHttpClient || !hasHttpCore) {
      logTheFollowingFabricateHomeLibShouldHaveTheseJars(fabricateHome);
      if (sys_.isDebug()) {
        ThreadLocalPrintStream.println("hasLogging" + hasLogging);
        ThreadLocalPrintStream.println("hasHttpClient" + hasHttpClient);
        ThreadLocalPrintStream.println("hasHttpCore" + hasHttpCore);
      }
      return null;
    }
    return fabricateJar;
  }

  public void logTheFollowingFabricateHomeLibShouldHaveTheseJars(String fabricateHome) {
    ThreadLocalPrintStream.println(CommandLineArgs.MESSAGE);
    I_SystemMessages sysMessages = constants_.getSystemMessages();
    String message = sysMessages.getTheFollowingFabricateHomeLibShouldHaveOnlyTheseJars();
    ThreadLocalPrintStream.println(message);
    ThreadLocalPrintStream.println(fabricateHome);
    ThreadLocalPrintStream.println("commons-logging-1.2.jar");
    ThreadLocalPrintStream.println("fabricate_*.jar");
    ThreadLocalPrintStream.println("httpclient-4.3.5.jar");
    ThreadLocalPrintStream.println("httpcore-4.3.2.jar");
  }
  
  private void displayFabricateJarManifestVersionAndEnd(File fabricateJar) {
    manifestParser_.readManifest(fabricateJar.getAbsolutePath());
    String fabricateVersion = manifestParser_.get(ManifestParser.SPECIFICATION_VERSION);
    I_SystemMessages systemMessages =  constants_.getSystemMessages();
    String versionX = systemMessages.getVersionX();
    versionX = versionX.replaceAll("<X/>", fabricateVersion);
    
    String compileDate = manifestParser_.get(ManifestParser.IMPLEMENTATION_VERSION);
    Date parsedDate = null;
    try {
      //This time format string must match the build.xml file
      SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a SSS");
      parsedDate = sdf.parse(compileDate);
      Locale local = new Locale(constants_.getLanguage(), constants_.getCountry());
      DateFormat odf = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.FULL, local);
      compileDate = odf.format(parsedDate);
    } catch (ParseException x) {
      //do nothing
    }
    String compiledOnX = systemMessages.getCompiledOnX();
    compiledOnX = compiledOnX.replaceAll("<X/>", compileDate);
    
    ThreadLocalPrintStream.println(CommandLineArgs.MESSAGE);
    ThreadLocalPrintStream.println(versionX);
    ThreadLocalPrintStream.println(compiledOnX);
  }
  
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
   * throws a fatal error when one of the 
   * dependencies requested for the fabricate 
   * class path can not be down loaded
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
        RepositoryDownloader rdl = new RepositoryDownloader(repos, 
            new DefaultRepositoryPathBuilder(localRepository, File.separator), fcm);
        for (DependencyType dep: depTypes) {
          rdl.findOrDownloadAndSha1(dep);
        }
      }
    }
  }

  public I_FabricateConstants getConstants() {
    return constants_;
  }

  public static JavaCalls getJAVA_CALLS() {
    return JAVA_CALLS;
  }

  /**
   * This method is for injecting a mock 
   * for testing only.
   * @param jAVA_CALLS
   */
  public static void setJAVA_CALLS(JavaCalls jc) {
    JAVA_CALLS = jc;
  }
}
