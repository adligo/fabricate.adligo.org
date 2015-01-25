package org.adligo.fabricate.models.dependencies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Dependency implements I_Dependency {
  
  private String artifact_;
  private List<I_Ide> children_; 
  private boolean extract_;
  private String fileName_;
  private String group_;
  private String platform_;
  private String type_;
  private String version_;
  
  public Dependency() {}
  
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
  public void setArtifact(String artifact) {
    this.artifact_ = artifact;
  }
  private void setChildren(List<I_Ide> children) {
    List<Ide> toAdd = new ArrayList<Ide>();
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
