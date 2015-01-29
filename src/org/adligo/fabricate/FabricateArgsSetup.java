package org.adligo.fabricate;

import org.adligo.fabricate.common.files.I_FabFileIO;
import org.adligo.fabricate.common.files.PatternFileMatcher;
import org.adligo.fabricate.common.i18n.I_CommandLineConstants;
import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.i18n.I_SystemMessages;
import org.adligo.fabricate.common.log.DeferredLog;
import org.adligo.fabricate.common.log.DelayedLog;
import org.adligo.fabricate.common.system.CommandLineArgs;
import org.adligo.fabricate.common.system.FabSystem;
import org.adligo.fabricate.common.system.FabSystemSetup;
import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.java.JavaCalls;
import org.adligo.fabricate.java.ManifestParser;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
  private static JavaCalls JAVA_CALLS;
  
  private ManifestParser manifestParser_;
  private final I_FabSystem sys_;
  private DelayedLog log_;
  private final I_FabFileIO files_;
  private final I_FabricateConstants constants_;
  
  /**
   * @diagram_sync on 1/26/2014 with Overview.seq
   * @param args
   */
	@SuppressWarnings("unused")
  public static void main(String [] args) {
	  FabSystem sys = new FabSystem();
	  JAVA_CALLS = new JavaCalls(sys);
	  new FabricateArgsSetup(args, sys, new ManifestParser());
	}
	
	public FabricateArgsSetup(String [] args, FabSystem sys, ManifestParser manifestParser) {
	  sys_ = sys;
	  manifestParser_ = manifestParser;
	  files_ = sys.getFileIO();
    FabSystemSetup.setupWithDelayedLog(sys, args);
    log_ = (DelayedLog) ((DeferredLog) sys.getLog()).getDelegate();
    constants_ = sys.getConstants();
    long now = sys_.getCurrentTime();
    
    String homeDir = null;
    try {
       homeDir = JAVA_CALLS.getJavaHome();
    } catch (IllegalStateException x) {
      log_.printlnNow(CommandLineArgs.END);
      I_SystemMessages sysMessages = constants_.getSystemMessages();
      String message = sysMessages.getExceptionNoJavaHomeSet();
      log_.printlnNow(message);
      log_.render();
      return;
    }
    String versionNbrString = null;
    try {
      versionNbrString = JAVA_CALLS.getJavaVersion(homeDir, files_.getNameSeparator());
      double versionDouble = JAVA_CALLS.getJavaMajorVersion(versionNbrString);
      if (versionDouble < 1.7) {
        log_.println(CommandLineArgs.END);
        I_SystemMessages sysMessages = constants_.getSystemMessages();
        String message = sysMessages.getExceptionFabricateRequiresJava1_7OrGreater();
        log_.println(message);
        log_.render();
        return;
      }
    } catch (IOException | InterruptedException e) {
      log_.println(CommandLineArgs.END);
      I_SystemMessages sysMessages = constants_.getSystemMessages();
      String message = sysMessages.getExceptionExecutingJavaWithTheFollowingJavaHome();
      log_.println(
          message + constants_.getLineSeperator() +
          homeDir);
      log_.printTrace(e);
      log_.render();
      return;
    }
    File fabricateJar = locateFabricateJarAndVerifyFabricateHomeJars();
    
    // @diagram_sync on 1/26/2014 with Overview.seq
    if (fabricateJar != null) {
     // @diagram_sync on 1/26/2014 with Overview.seq
      if (hasDisplayVersionArg()) {
        //@diagram_sync on 1/26/2014 with Overview.seq
        displayFabricateJarManifestVersionAndEnd(fabricateJar);
      } else {
        sendArgsToScript(now, versionNbrString);
      }
    }
   
  }

 
  /**
   * @diagram_sync on 1/26/2014 with Overview.seq
   * @param argMap
   * @return
   */
  public boolean hasDisplayVersionArg() {
    I_CommandLineConstants messages = constants_.getCommandLineConstants();
    String version = messages.getVersion(true);
    if ( sys_.hasArg(version)) {
      return true;
    }
    return false;
  }

  public void sendArgsToScript(long now, String javaVersionNbr) {

    log_.printlnNow("start=" + now + " java=" + javaVersionNbr +
        sys_.toScriptArgs());
    log_.render();
  }

  /**
   * @diagram_sync on 1/26/2014 with Overview.seq
   * @param args
   */
  public File locateFabricateJarAndVerifyFabricateHomeJars() {
    String fabricateHome = sys_.getenv("FABRICATE_HOME");
    if (fabricateHome == null) {
      log_.printlnNow(CommandLineArgs.END);
      I_SystemMessages sysMessages = constants_.getSystemMessages();
      String message = sysMessages.getExceptionNoFabricateHomeSet();
      log_.printlnNow(message);
      log_.render();
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
      log_.printTrace(x);
      log_.render();
      return null;
    }
    if (fabricateJar == null) {
      log_.println(CommandLineArgs.END);
      I_SystemMessages sysMessages = constants_.getSystemMessages();
      String message = sysMessages.getExceptionNoFabricateJarInFabricateHomeLib();
      log_.println(message);
      log_.println(fabricateHome);
      log_.render();
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
    log_.printlnNow(CommandLineArgs.END);
    I_SystemMessages sysMessages = constants_.getSystemMessages();
    String message = sysMessages.getTheFollowingFabricateHomeLibShouldHaveOnlyTheseJars();
    log_.printlnNow(message);
    log_.printlnNow(fabricateHome);
    log_.printlnNow("commons-logging-1.2.jar");
    log_.printlnNow("fabricate_*.jar");
    log_.printlnNow("httpclient-4.3.5.jar");
    log_.printlnNow("httpcore-4.3.2.jar");
    log_.render();
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
    
    log_.printlnNow(CommandLineArgs.END);
    log_.printlnNow(versionX);
    log_.printlnNow(compiledOnX);
    log_.render();
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
