package org.adligo.fabricate.repository;

import org.adligo.fabricate.common.files.I_FabFileIO;
import org.adligo.fabricate.common.files.xml_io.I_FabXmlFileIO;
import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.i18n.I_SystemMessages;
import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.models.dependencies.Dependency;
import org.adligo.fabricate.models.dependencies.DependencyMutant;
import org.adligo.fabricate.models.dependencies.I_Dependency;
import org.adligo.fabricate.models.fabricate.I_Fabricate;
import org.adligo.fabricate.xml.io_v1.library_v1_0.DependenciesType;
import org.adligo.fabricate.xml.io_v1.library_v1_0.DependencyType;
import org.adligo.fabricate.xml.io_v1.library_v1_0.LibraryReferenceType;
import org.adligo.fabricate.xml.io_v1.library_v1_0.LibraryType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class helps identify dependencies for a fabricate library.  It is NOT thread safe
 * and throws IllegalStateExceptions when it can't find a library or a circular library
 * reference is detected. This class makes no attempt to filter by platform,
 * or extract as this can be done later on.
 * 
 * @author scott
 */
public class LibraryResolver implements I_LibraryResolver {
  private final I_FabSystem sys_;
  private final I_FabricateConstants constants_;
  private final I_FabFileIO files_;
  private final I_FabXmlFileIO xmlFiles_;
  private final String libDir_;
  /**
   * A list is used to keep the order 
   * for the circular reference error
   */
  private List<String> libs_ = new ArrayList<String>();
  private List<I_Dependency> deps_ = new ArrayList<I_Dependency>();
  
  public LibraryResolver(I_FabSystem sys, I_Fabricate fab) {
    sys_ = sys;
    constants_ = sys.getConstants();
    files_ = sys.getFileIO();
    xmlFiles_ = sys.getXmlFileIO();
    libDir_ = fab.getFabricateXmlRunDir() + "lib";
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.repository.I_LibraryResolver#getDependencies(java.lang.String, java.lang.String)
   */
  @Override
  public List<I_Dependency> getDependencies(List<LibraryReferenceType> libs, String projectName) throws IllegalStateException {
    List<I_Dependency> toRet = new ArrayList<I_Dependency>();
    for (LibraryReferenceType lib: libs) {
      String libName = lib.getValue();
      String platform = lib.getPlatform();
      if (platform != null) {
        platform = platform.toLowerCase();
      }
      List<I_Dependency> deps = getDependencies(libName, projectName, platform);
      toRet.addAll(deps);
    }
    return toRet;
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.repository.I_LibraryResolver#getDependencies(java.lang.String, java.lang.String, java.lang.String)
   */
  @Override
  public List<I_Dependency> getDependencies(String libName, String projectName, String platform) throws IllegalStateException {
    libs_ = new ArrayList<String>();
    deps_ = new ArrayList<I_Dependency>();
    if (platform != null) {
      platform = platform.toLowerCase();
    }
    return getDependenciesInternal(libName, projectName, platform);
  }
  
  private List<I_Dependency> getDependenciesInternal(String libName, String projectName, String platform) throws IllegalStateException {
    if (libs_.contains(libName)) {
      I_SystemMessages messages = constants_.getSystemMessages();
      throw new IllegalStateException(messages.getTheFollowingListOfFabricateLibrariesContainsACircularReference() +
          sys_.lineSeparator() + libs_);
    }
    libs_.add(libName);
    String xmlFilePath = libDir_ + files_.getNameSeparator() + libName + ".xml";
    if (!files_.exists(xmlFilePath)) {
      I_SystemMessages messages = constants_.getSystemMessages();
      throw new IllegalStateException(messages.getTheFollowingFabricateLibraryCanNotBeFound() +
          sys_.lineSeparator() + xmlFilePath);
    }
    try {
      LibraryType lib = xmlFiles_.parseLibrary_v1_0(xmlFilePath);
      DependenciesType deps = lib.getDependencies();
      List<DependencyType> libDeps = deps.getDependency();
      
      List<I_Dependency> depsList = Dependency.convert(libDeps, projectName);
      if (platform != null) {
        for (I_Dependency dep: depsList) {
          DependencyMutant dm = new DependencyMutant(dep);
          dm.setPlatform(platform);
          deps_.add(new Dependency(dm));
        }
      } else {
        deps_.addAll(depsList);
      }
      List<LibraryReferenceType> libs = deps.getLibrary();
      for (LibraryReferenceType lrt: libs) {
        String lrtName = lrt.getValue();
        getDependenciesInternal(lrtName, projectName, platform);
      }
    } catch (IOException x) {
      throw new IllegalStateException(x.getMessage(), x);
    }
    return deps_;
  }
}
