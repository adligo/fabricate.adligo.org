package org.adligo.fabricate.models.dependencies;

import org.adligo.fabricate.xml.io_v1.library_v1_0.DependencyType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Dependency implements I_Dependency {
  public static List<I_Dependency> convert(List<DependencyType> types) {
    List<I_Dependency> toRet = new ArrayList<I_Dependency>();
    if (types != null) {
      for (DependencyType type: types) {
        if (type != null) {
          toRet.add(new Dependency(type));
        }
      }
    }
    return toRet;
  }
  
  private String artifact_;
  private List<I_Ide> children_; 
  private boolean extract_;
  private String fileName_;
  private String group_;
  private String platform_;
  private String type_;
  private String version_;
  
  public Dependency(I_Dependency other) {
    artifact_ = other.getArtifact();
    setChildren(other.getChildren());
    extract_ = other.isExtract();
    fileName_ = other.getFileName();
    group_ = other.getGroup();
    platform_ = other.getPlatform();
    type_ = other.getType();
    version_ = other.getVersion();
  }
  
  @SuppressWarnings("boxing")
  public Dependency(DependencyType other) {
    artifact_ = other.getArtifact();
    setChildren(IdeMutant.convert(other.getIde()));
    Boolean oe = other.isExtract();
    extract_ = false;
    if (oe != null) {
      if (oe) {
        extract_ = true;
      }
    }
    fileName_ = other.getFileName();
    group_ = other.getGroup();
    platform_ = other.getPlatform();
    type_ = other.getType();
    version_ = other.getVersion();
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
  
  private void setChildren(List<I_Ide> children) {
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
    children_ = Collections.unmodifiableList(toAdd);
  }

  @Override
  public I_Ide get(int child) {
    return children_.get(child);
  }
  @Override
  public int size() {
    return children_.size();
  } 
  
}
