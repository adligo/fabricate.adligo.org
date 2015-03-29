package org.adligo.fabricate.models.dependencies;

import org.adligo.fabricate.common.i18n.I_SystemMessages;
import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.xml.io_v1.library_v1_0.DependencyType;

import java.util.ArrayList;
import java.util.List;

/**
 * @see I_Dependency 
 * @author scott
 *
 */
public class DependencyMutant implements I_Dependency {
  public static List<I_Dependency> convert(List<DependencyType> types, String project) {
    List<I_Dependency> toRet = new ArrayList<I_Dependency>();
    if (types != null) {
      for (DependencyType type: types) {
        if (type != null) {
          toRet.add(new DependencyMutant(type, project));
        }
      }
    }
    return toRet;
  }
  
  public static boolean equals(I_Dependency me, Object other) {
    if (me == other)
      return true;
    if (other == null)
      return false;
    
    I_Dependency obj = (I_Dependency) other;
    if (me.getArtifact() == null) {
      if (obj.getArtifact() != null)
        return false;
    } else if (!me.getArtifact().equals(obj.getArtifact()))
      return false;
    if (me.getFileName() == null) {
      if (obj.getFileName() != null)
        return false;
    } else if (!me.getFileName().equals(obj.getFileName()))
      return false;
    if (me.getGroup() == null) {
      if (obj.getGroup() != null)
        return false;
    } else if (!me.getGroup().equals(obj.getGroup()))
      return false;
    if (me.getType() == null) {
      if (obj.getType()  != null)
        return false;
    } else if (!me.getType().equals(obj.getType()))
      return false;
    if (me.getVersion() == null) {
      if (obj.getVersion() != null)
        return false;
    } else if (!me.getVersion().equals(obj.getVersion()))
      return false;
    return true;
  }
  
  public static int hashCode(I_Dependency dep) {
    final int prime = 31;
    int result = 1;
    String artifact = dep.getArtifact();
    result = prime * result + ((artifact == null) ? 0 : artifact.hashCode());
    String fileName = dep.getFileName();
    result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
    
    String group = dep.getGroup();
    result = prime * result + ((group == null) ? 0 : group.hashCode());
    String type = dep.getType();
    result = prime * result + ((type == null) ? 0 : type.hashCode());
    String version = dep.getVersion();
    result = prime * result + ((version == null) ? 0 : version.hashCode());
    return result;
  }
  
  public static String toString(I_Dependency dep) {
    int size = 0;
    List<I_Ide> children = dep.getChildren();
    if (children != null) {
      size = children.size();
    }
    String toRet =  dep.getClass().getSimpleName() + " [artifact=" + dep.getArtifact() + ", extract="
        + dep.isExtract() + "," + System.lineSeparator() + 
          "\tfileName=" + dep.getFileName() + ", group=" + dep.getGroup() + "," + System.lineSeparator() +
          "\tplatform=" + dep.getPlatform() + ", type=" + dep.getType() + "," + System.lineSeparator() + 
          "\tversion=" + dep.getVersion() + ", ideChidren=" + size + "]";
    return toRet;
  }  
  
  private String artifact_;
  private List<IdeMutant> children_ = new ArrayList<IdeMutant>(); 
  private boolean extract_;
  private String fileName_;
  private String group_;
  private String platform_;
  private String project_;
  private String type_ = DependencyConstants.JAR;
  private String version_;
  
  public DependencyMutant() {}
  
  public DependencyMutant(I_Dependency other) {
    artifact_ = other.getArtifact();
    setChildren(other.getChildren());
    extract_ = other.isExtract();
    fileName_ = other.getFileName();
    group_ = other.getGroup();
    platform_ = other.getPlatform();
    project_ = other.getProject();
    type_ = other.getType();
    version_ = other.getVersion();
  }
  
  @SuppressWarnings("boxing")
  public DependencyMutant(DependencyType other, String project) {
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
    project_ = project;
    String type = other.getType();
    if (type != null) {
      type_ = type;
    }
    version_ = other.getVersion();
  }
  
  @Override
  public boolean equals(Object obj) {
     return equals(this, obj);
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
    return hashCode(this);
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
  
  public void setProject(String project) {
    this.project_ = project;
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

  @Override
  public String toString() {
    return toString(this);
  }

}
