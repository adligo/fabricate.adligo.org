package org.adligo.fabricate;

import org.adligo.fabricate.common.FabConstantsDiscovery;
import org.adligo.fabricate.common.FabricateHelper;
import org.adligo.fabricate.common.LocalRepositoryHelper;
import org.adligo.fabricate.common.RunContextMutant;
import org.adligo.fabricate.common.en.FabricateEnConstants;
import org.adligo.fabricate.common.files.FabFileIO;
import org.adligo.fabricate.common.files.I_FabFileIO;
import org.adligo.fabricate.common.files.xml_io.FabXmlFileIO;
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
  @SuppressWarnings("unused")
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
	  new FabricateSetup(args, new FabSystem());
	}
	
	public FabricateSetup(String [] args, FabSystem sys) {
	  sys_ = sys;
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

  private void doOpts(Map<String,String> argMap) {
    String fabHome = System.getenv("FABRICATE_HOME");
    //@diagram_sync on 1/18/2014 with Overview.seq
    FabricateXmlDiscovery fd = new FabricateXmlDiscovery(sys_);
    if (!fd.hasFabricateXml()) {
      System.out.println("Exception no fabricate.xml or project.xml found.");
    } else {
      String javaHome = System.getenv("JAVA_HOME");
      if (StringUtils.isEmpty(javaHome)) {
        ThreadLocalPrintStream.println(
            "No $JAVA_HOME environment variable set.");
        return;
      } else {
        String version = argMap.get("java");
        if (StringUtils.isEmpty(javaHome)) {
          ThreadLocalPrintStream.println("Java version parameter expected.");
          return;
        }
        try {
          String javaHomeVersion = JavaCalls.getJavaVersion(
              new File(javaHome + File.separator + "bin"));
          if (!javaHomeVersion.equals(version)) {
            ThreadLocalPrintStream.println("Expected $JAVA_HOME to have version "
                + version + " but was " + javaHomeVersion);
            return;
          }
        } catch (IOException | InterruptedException e) {
          e.printStackTrace();
          return;
        }
      }
      workWithFabricateXml(fabHome, fd, argMap);
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
    
    try {
      String versionNbrString = JavaCalls.getJavaVersion();
      double versionDouble = JavaCalls.getJavaMajorVersion(versionNbrString);
      if (versionDouble < 1.8) {
        throw new IllegalStateException("Fabricate requires Java 1.7 or greater.");
      }

      File fabricateJar = locateFabricateJar();
      
     if (fabricateJar != null) {
        if (hasDisplayVersionArg(argMap)) {
          displayFabricateJarManifestVersionAndEnd(fabricateJar);
        } else {
          sendOptsToScript(now, versionNbrString);
        }
      }
    } catch (IOException | InterruptedException | IllegalStateException e) {
      e.printStackTrace();
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

  public File locateFabricateJar() {
    String fabricateHome = System.getenv("FABRICATE_HOME");
    if (fabricateHome == null) {
      ThreadLocalPrintStream.println("Exception no fabricate home.");
      return null;
    }
    File lib = new File(fabricateHome + File.separator + "lib");
    File [] files = lib.listFiles();
    File fabricateJar = null;
    for (int i = 0; i < files.length; i++) {
      File file = files[i];
      String fileName = file.getName();
      if (fileName.indexOf("fabricate_") == 0) {
        fabricateJar = file;
        break;
      }
    }
    if (fabricateJar == null) {
      ThreadLocalPrintStream.println("No fabricate_*.jar in $FABRICATE_HOME/lib for the following $FABRICATE_HOME;" + 
            fabricateHome + "/lib!");
      return null;
    }
    return fabricateJar;
  }
  
  private void displayFabricateJarManifestVersionAndEnd(File fabricateJar) {
    ManifestParser mp = new ManifestParser();
    mp.readManifest(fabricateJar.getAbsolutePath());
    String fabricateVersion = mp.get(ManifestParser.SPECIFICATION_VERSION);
    I_SystemMessages systemMessages =  constants_.getSystemMessages();
    String versionX = systemMessages.getVersionX();
    versionX = versionX.replaceAll("<X/>", fabricateVersion);
    
    String compileDate = mp.get(ManifestParser.IMPLEMENTATION_VERSION);
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
}
