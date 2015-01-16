package org.adligo.fabricate;

import org.adligo.fabricate.common.ArgsParser;
import org.adligo.fabricate.common.FabricateHelper;
import org.adligo.fabricate.common.FabricateXmlDiscovery;
import org.adligo.fabricate.common.LocalRepositoryHelper;
import org.adligo.fabricate.common.RunContextMutant;
import org.adligo.fabricate.common.StringUtils;
import org.adligo.fabricate.common.log.ThreadLocalPrintStream;
import org.adligo.fabricate.external.DefaultRepositoryPathBuilder;
import org.adligo.fabricate.external.JavaCalls;
import org.adligo.fabricate.external.ManifestParser;
import org.adligo.fabricate.external.RepositoryDownloader;
import org.adligo.fabricate.files.FabFileIO;
import org.adligo.fabricate.files.I_FabFileIO;
import org.adligo.fabricate.files.xml_io.FabXmlFileIO;
import org.adligo.fabricate.files.xml_io.I_FabXmlFileIO;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.FabricateDependencies;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.FabricateType;
import org.adligo.fabricate.xml.io_v1.library_v1_0.DependencyType;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class FabricateSetup {
  @SuppressWarnings("unused")
  private final I_FabFileIO files_;
  private final I_FabXmlFileIO xmlFiles_;
  
	@SuppressWarnings("unused")
  public static void main(String [] args) {
	  new FabricateSetup(args, FabFileIO.INSTANCE, FabXmlFileIO.INSTANCE);
	}
	
	public FabricateSetup(String [] args, I_FabFileIO files, I_FabXmlFileIO xmlFiles) {
	  files_ = files;
	  xmlFiles_ = xmlFiles;
	  Map<String,String> argMap = ArgsParser.parseArgs(args);
    if (argMap.containsKey("args")) {
      doArgs(argMap);
    } else if (argMap.containsKey("opts")) {
      doOpts(argMap);
    }
	}

  private void doOpts(Map<String,String> argMap) {
    String fabHome = System.getenv("FABRICATE_HOME");
    FabricateXmlDiscovery fd = new FabricateXmlDiscovery(argMap.containsKey("debug"));
    if (!fd.hasFabricateXml()) {
      System.out.println("Exception no fabricate.xml or project.xml found.");
    } else {
      String javaHome = System.getenv("JAVA_HOME");
      if (StringUtils.isEmpty(javaHome)) {
        ThreadLocalPrintStream.println(
            "Exception no $JAVA_HOME environment variable set");
        return;
      } else {
        String version = argMap.get("java");
        if (StringUtils.isEmpty(javaHome)) {
          ThreadLocalPrintStream.println("Exception java version parameter expected");
          return;
        }
        try {
          String javaHomeVersion = JavaCalls.getJavaVersion(
              new File(javaHome + File.separator + "bin"));
          if (!javaHomeVersion.equals(version)) {
            ThreadLocalPrintStream.println("Exception java home to have version "
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
      FabricateType fab =  xmlFiles_.parseFabricate_v1_0(fabricateXmlPath);
      FabricateHelper fh = new FabricateHelper(fab);
     
      downloadFabricateRunClasspathDependencies(fab);
      
      System.out.println(" -Xmx" + fh.getXmx() + " -Xms" + fh.getXms() + " -cp " + 
          buildClasspath(fabricateXmlPath, fabHome));
    } catch (IOException e) {
      //this should also de-rail the fabricate run, as there will be no classpath
      e.printStackTrace();
    }
  }

  private void doArgs(Map<String,String> argMap) {
    long now = System.currentTimeMillis();
    
    try {
      String versionNbrString = JavaCalls.getJavaVersion();
      double versionDouble = JavaCalls.getJavaMajorVersion(versionNbrString);
      if (versionDouble < 1.8) {
        throw new IllegalStateException("Fabricate requires java 1.7 or greater");
      }
      String fabricateHome = System.getenv("FABRICATE_HOME");
      if (fabricateHome == null) {
        ThreadLocalPrintStream.println("Exception no fabricate home.");
        return;
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
        ThreadLocalPrintStream.println("Exception no fabricate_*.jar in FABRICATE_HOME/lib " + 
              fabricateHome + "/lib!");
        return;
      }
      ManifestParser mp = new ManifestParser();
      mp.readManifest(fabricateJar.getAbsolutePath());
      String fabricateVersion = mp.get(ManifestParser.IMPLEMENTATION_VERSION);
      
      ThreadLocalPrintStream.println("start=" + now + " java=" + versionNbrString +
          " fabricate_version=" + fabricateVersion);
    } catch (IOException | InterruptedException | IllegalStateException e) {
      e.printStackTrace();
    }
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
  private void downloadFabricateRunClasspathDependencies(FabricateType fab) throws IOException {
    FabricateDependencies deps =  fab.getDependencies();
    if (deps != null) {
      List<DependencyType> depTypes = deps.getDependency();
      if (depTypes != null) {
        LocalRepositoryHelper lrh = new LocalRepositoryHelper();
        String localRepository = lrh.getRepositoryPath(fab);
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
}
