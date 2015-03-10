package org.adligo.fabricate.models.dependencies;

import org.adligo.fabricate.xml.io_v1.library_v1_0.LibraryReferenceType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LibraryDependency implements I_LibraryDependency {

  public static List<LibraryDependency> convert(Collection<LibraryReferenceType> deps) {
    List<LibraryDependency> toRet = new ArrayList<LibraryDependency>();
    if (deps != null) {
      for (LibraryReferenceType lrt: deps) {
        toRet.add(convert(lrt));
      }
    }
    return toRet;
  }
  
  public static LibraryDependency convert(LibraryReferenceType dep) {
    String platform = dep.getPlatform();
    String libName = dep.getValue();
    
    LibraryDependencyMutant ldm = new LibraryDependencyMutant();
    ldm.setLibraryName(libName);
    ldm.setPlatform(platform);
    return new LibraryDependency(ldm);
  }
  
  private String libraryName_;
  private String platform_;
  
  public LibraryDependency() {}
  
  public LibraryDependency(I_LibraryDependency other ) {
    libraryName_ = other.getLibraryName();
    platform_ = other.getPlatform();
  } 
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.dependencies.I_LibraryDependency#getLibraryName()
   */
  @Override
  public String getLibraryName() {
    return libraryName_;
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.dependencies.I_LibraryDependency#getPlatform()
   */
  @Override
  public String getPlatform() {
    return platform_;
  }

  @Override
  public int hashCode() {
    return LibraryDependencyMutant.equals(this); 
  }

  @Override
  public boolean equals(Object obj) {
    return LibraryDependencyMutant.equals(obj, this);
  }
  
  public String toString() {
    return LibraryDependencyMutant.toString(this);
  }
}
