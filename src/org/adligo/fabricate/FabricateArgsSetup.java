package org.adligo.fabricate;

import org.adligo.fabricate.common.FabConstantsDiscovery;
import org.adligo.fabricate.common.en.FabricateEnConstants;
import org.adligo.fabricate.common.files.I_FabFileIO;
import org.adligo.fabricate.common.files.PatternFileMatcher;
import org.adligo.fabricate.common.i18n.I_CommandLineConstants;
import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.i18n.I_SystemMessages;
import org.adligo.fabricate.common.log.ThreadLocalPrintStream;
import org.adligo.fabricate.common.system.CommandLineArgs;
import org.adligo.fabricate.common.system.FabSystem;
import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.external.JavaCalls;
import org.adligo.fabricate.external.ManifestParser;

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

/**
 * This class parses the command line arguments manipulates them 
 * and then passes them to the FabricateOptsSetup, so it must either
 * print a error message or the command line arguments 
 * for FabricateOptsSetup.
 * 
 * @author scott
 *
 */
public class FabricateArgsSetup {
  private static JavaCalls JAVA_CALLS = JavaCalls.INSTANCE;
  
  private ManifestParser manifestParser_;
  private final I_FabSystem sys_;
  private final I_FabFileIO files_;
  private String language_;
  private String country_;
  private I_FabricateConstants constants_;
  
  /**
   * @diagram_sync on 1/26/2014 with Overview.seq
   * @param args
   */
	@SuppressWarnings("unused")
  public static void main(String [] args) {
	  new FabricateArgsSetup(args, new FabSystem(), new ManifestParser());
	}
	
	public FabricateArgsSetup(String [] args, FabSystem sys, ManifestParser manifestParser) {
	  sys_ = sys;
	  manifestParser_ = manifestParser;
	  files_ = sys.getFileIO();
    Map<String,String> argMap = CommandLineArgs.parseArgs(args);
    
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
	  I_CommandLineConstants clConstants = constants_.getCommandLineConstants();
	  argMap = CommandLineArgs.normalizeArgs(argMap, clConstants);
	  String logAlias = clConstants.getLog(true);
	  sys.setDebug(argMap.containsKey(logAlias));
    
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
    
    // @diagram_sync on 1/26/2014 with Overview.seq
    if (fabricateJar != null) {
     // @diagram_sync on 1/26/2014 with Overview.seq
      if (hasDisplayVersionArg(argMap)) {
        //@diagram_sync on 1/26/2014 with Overview.seq
        displayFabricateJarManifestVersionAndEnd(fabricateJar);
      } else {
        sendArgsToScript(argMap, now, versionNbrString);
      }
    }
   
  }

 
  /**
   * @diagram_sync on 1/26/2014 with Overview.seq
   * @param argMap
   * @return
   */
  public boolean hasDisplayVersionArg(Map<String, String> argMap) {
    I_CommandLineConstants messages = constants_.getCommandLineConstants();
    String version = messages.getVersion(true);
    if ( argMap.containsKey(version)) {
      return true;
    }
    return false;
  }

  public void sendArgsToScript(Map<String,String> args, long now, String javaVersionNbr) {
    StringBuilder sb = new StringBuilder();
    CommandLineArgs.appendArgs(sb, args);
    ThreadLocalPrintStream.println("start=" + now + " java=" + javaVersionNbr +
        sb.toString());
  }

  /**
   * @diagram_sync on 1/26/2014 with Overview.seq
   * @param args
   */
  public File locateFabricateJarAndVerifyFabricateHomeJars() {
    String fabricateHome = sys_.getenv("FABRICATE_HOME");
    if (fabricateHome == null) {
      ThreadLocalPrintStream.println(CommandLineArgs.MESSAGE);
      I_SystemMessages sysMessages = constants_.getSystemMessages();
      String message = sysMessages.getExceptionNoFabricateHomeSet();
      ThreadLocalPrintStream.println(message);
      return null;
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
  
  /**
   * @diagram_sync on 1/26/2014 with Overview.seq
   * @param fabricateJar
   */
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
