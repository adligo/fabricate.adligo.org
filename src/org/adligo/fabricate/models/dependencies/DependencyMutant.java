package org.adligo.fabricate.models.dependencies;

import java.util.ArrayList;
import java.util.List;

public class DependencyMutant implements I_Dependency {
  
  private String artifact_;
  private List<IdeMutant> children_ = new ArrayList<IdeMutant>(); 
  private boolean extract_;
  private String fileName_;
  private String group_;
  private String platform_;
  private String type_;
  private String version_;
  
  public DependencyMutant() {}
  
  public DependencyMutant(I_Dependency other) {
    artifact_ = other.getArtifact();
    setChildren(other.getChildren());
    extract_ = other.isExtract();
    fileName_ = other.getFileName();
    group_ = other.getGroup();
    platform_ = other.getPlatform();
    type_ = other.getType();
    version_ = other.getVersion();
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
    return new ArrayList<I_Ide>(children_);
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
  public void setArtifact(String artifact) {
    this.artifact_ = artifact;
  }
  public void setChildren(List<I_Ide> children) {
    children_.clear();
    if (children != null) {
      for(I_Ide child: children) {
        if (child instanceof IdeMutant) {
          children_.add((IdeMutant) child);
        } else {
          children_.add(new IdeMutant(child));
        }
      }
    }
  }
  public void setExtract(boolean extract) {
    this.extract_ = extract;
  }
  public void setFileName(String fileName) {
    this.fileName_ = fileName;
  }
  public void setGroup(String group) {
    this.group_ = group;
  }
  public void setPlatform(String platform) {
    this.platform_ = platform;
  }
  public void setType(String type) {
    this.type_ = type;
  }
  public void setVersion(String version) {
    this.version_ = version;
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
