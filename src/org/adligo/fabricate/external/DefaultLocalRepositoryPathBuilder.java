package org.adligo.fabricate.external;

import org.adligo.fabricate.xml.io.library.DependencyType;

import java.io.File;

/**
 * This can be used with a local or remote repository
 * it assumes the repository pass to it has the last slash
 * 
 * @author scott
 *
 */
public class DefaultLocalRepositoryPathBuilder implements I_LocalRepositoryPathBuilder {
  private String repo_;
  private String seperator_;
  
  public DefaultLocalRepositoryPathBuilder(String repository, String seperator) {
    repo_ = repository;
    seperator_ = seperator;
  }
  @Override
  public String getPath(DependencyType dependency) {
    String artifact = dependency.getArtifact();
    String version = dependency.getVersion();
    String type = dependency.getType();
    if (type == null) {
      type = "jar";
    }
    
    return getFolderPath(dependency) + seperator_ +
        artifact + "-" + version + "." + type;
  }

  public String getFolderPath(DependencyType dependency) {
    String group = dependency.getGroup();
    String artifact = dependency.getArtifact();
    String version = dependency.getVersion();
    
    
    return repo_ + group + seperator_ + 
        artifact + seperator_ + version ;
  }
}
