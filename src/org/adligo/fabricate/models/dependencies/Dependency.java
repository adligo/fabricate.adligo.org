package org.adligo.fabricate.models.dependencies;

import org.adligo.fabricate.xml.io_v1.library_v1_0.DependencyType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Dependency implements I_Dependency {
  public static List<I_Dependency> convert(List<DependencyType> types, String project) throws DependencyVersionMismatchException {
    List<I_Dependency> toRet = new ArrayList<I_Dependency>();
    if (types != null) {
      for (DependencyType type: types) {
        if (type != null) {
          toRet.add(new Dependency(type, project));
        }
      }
    }
    return toRet;
  }
  
  private final String artifact_;
  private final List<I_Ide> children_; 
  private final boolean extract_;
  private final String fileName_;
  private final String group_;
  private final String platform_;
  private final String project_;
  private final String type_;
  private final String version_;
  
  public Dependency(I_Dependency other) throws DependencyVersionMismatchException {
    artifact_ = other.getArtifact();
    children_ = getChildren(other.getChildren());
    extract_ = other.isExtract();
    fileName_ = other.getFileName();
    group_ = other.getGroup();
    platform_ = other.getPlatform();
    project_ = other.getProject();
    type_ = other.getType();
    version_ = other.getVersion();
    check();
  }
  
  @SuppressWarnings("boxing")
  public Dependency(DependencyType other, String project) throws DependencyVersionMismatchException {
    artifact_ = other.getArtifact();
    children_ = getChildren(IdeMutant.convert(other.getIde()));
    Boolean oe = other.isExtract();
    boolean setExtract = false;
    if (oe != null) {
      if (oe) {
        setExtract = true;
      }
    }
    extract_ = setExtract;
    fileName_ = other.getFileName();
    group_ = other.getGroup();
    platform_ = other.getPlatform();
    project_ = project;
    String type = other.getType();
    if (type == null) {
      type_ = DependencyConstants.JAR;
    } else {
      type_ = type;
    }
    version_ = other.getVersion();
    check();
  }
  
  
  @Override
  public boolean equals(Object o) {
    return DependencyMutant.equals(this, o);
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.dependencies.I_Dependency#getArtifact()
   */
  @Override
  public String getArtifact() {
    return artifact_;
  }
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.dependencies.I_Dependency#getChildren()
   */
  @Override
  public List<I_Ide> getChildren() {
    return children_;
  }
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.dependencies.I_Dependency#isExtract()
   */
  @Override
  public boolean isExtract() {
    return extract_;
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.dependencies.I_Dependency#getFileName()
   */
  @Override
  public String getFileName() {
    return fileName_;
  }
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.dependencies.I_Dependency#getGroup()
   */
  @Override
  public String getGroup() {
    return group_;
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.dependencies.I_Dependency#getPlatform()
   */
  @Override
  public String getPlatform() {
    return platform_;
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.dependencies.I_Dependency#getProject()
   */
  @Override
  public String getProject() {
    return project_;
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.dependencies.I_Dependency#getType()
   */
  @Override
  public String getType() {
    return type_;
  }
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.dependencies.I_Dependency#getVersion()
   */
  @Override
  public String getVersion() {
    return version_;
  }
  
  @Override
  public int hashCode() {
    return DependencyMutant.hashCode(this);
  }


  @Override
  public I_Ide get(int child) {
    return children_.get(child);
  }
  @Override
  public int size() {
    return children_.size();
  }

  @Override
  public String toString() {
    return DependencyMutant.toString(this);
  }

  private void check() throws DependencyVersionMismatchException {
    if (fileName_ != null) {
      if (version_ != null) {
        if (fileName_.indexOf(version_) == -1) {
          throw new DependencyVersionMismatchException(this);
        }
      }
    }
  }
  
  private List<I_Ide> getChildren(List<I_Ide> children) {
    List<I_Ide> toAdd = new ArrayList<I_Ide>();
    if (children != null) {
      for(I_Ide child: children) {
        if (child instanceof Ide) {
          toAdd.add((Ide) child);
        } else {
          toAdd.add(new Ide(child));
        }
      }
    }
    return Collections.unmodifiableList(toAdd);
  }
}
