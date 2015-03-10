package org.adligo.fabricate.models.dependencies;

import org.adligo.fabricate.xml.io_v1.library_v1_0.LibraryReferenceType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LibraryDependencyMutant implements I_LibraryDependency {

  public static List<LibraryDependencyMutant> convert(Collection<LibraryReferenceType> deps) {
    List<LibraryDependencyMutant> toRet = new ArrayList<LibraryDependencyMutant>();
    if (deps != null) {
      for (LibraryReferenceType lrt: deps) {
        toRet.add(convert(lrt));
      }
    }
    return toRet;
  }
  
  public static LibraryDependencyMutant convert(LibraryReferenceType dep) {
    String platform = dep.getPlatform();
    String libName = dep.getValue();
    
    LibraryDependencyMutant ldm = new LibraryDependencyMutant();
    ldm.setLibraryName(libName);
    ldm.setPlatform(platform);
    return ldm;
  }
  
  public static int equals(I_LibraryDependency dep) {
    final int prime = 31;
    int result = 1;
    String libraryName = dep.getLibraryName();
    String platform = dep.getPlatform();
    
    result = prime * result + ((libraryName == null) ? 0 : libraryName.hashCode());
    result = prime * result + ((platform == null) ? 0 : platform.hashCode());
    return result;
  }
  
  public static boolean equals(Object obj, I_LibraryDependency dep) {
    if (dep == obj) 
      return true;
    if (obj instanceof I_LibraryDependency) {
      I_LibraryDependency other = (I_LibraryDependency) obj;
      String libName = dep.getLibraryName();
      String oln = other.getLibraryName();
      if (libName == null) {
        if (oln != null)
          return false;
      } else if (!libName.equals(oln))
        return false;
      
      String platformName = dep.getPlatform();
      String plat = other.getPlatform();
      if (platformName == null) {
        if (plat != null)
          return false;
      } else if (!platformName.equals(plat))
        return false;
      return true;
    }
    return false;
  }
  
  public static String toString(I_LibraryDependency dep) {
    StringBuilder sb = new StringBuilder();
    sb.append(dep.getClass().getSimpleName());
    sb.append(" [name=");
    sb.append(dep.getLibraryName());
    String platform = dep.getPlatform();
    if (platform != null) {
      sb.append(",platform=");
      sb.append(platform);
    }
    sb.append("]");
    return sb.toString();
  }
  
  private String libraryName_;
  private String platform_;
  
  public LibraryDependencyMutant() {}
  
  public LibraryDependencyMutant(I_LibraryDependency other) {
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
  
  public void setLibraryName(String name) {
    this.libraryName_ = name;
  }
  
  public void setPlatform(String platform) {
    this.platform_ = platform;
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
