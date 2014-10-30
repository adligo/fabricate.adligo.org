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
public class DefaultLocalRepositoryPathBuilder implements I_RepositoryPathBuilder {
  private String repo_;
  private String seperator_;
  
  public DefaultLocalRepositoryPathBuilder(String repository, String seperator) {
    repo_ = repository;
    seperator_ = seperator;
  }
  @Override
  public String getPath(DependencyType dependency) {
    return getFolderPath(dependency) + seperator_ +
        getFileName(dependency);
  }

  public String getFileName(DependencyType dependency) {
    String artifact = dependency.getArtifact();
    String version = dependency.getVersion();
    String type = dependency.getType();
    if (type == null) {
      type = "jar";
    }
    
    return artifact + "-" + version + "." + type;
  }
  
  public String getFolderPath(DependencyType dependency) {
    String group = dependency.getGroup();
    return repo_ + group;
  }
  
  public String getUrl(DependencyType dependency) {
    String artifact = dependency.getArtifact();
    String version = dependency.getVersion();
    String type = dependency.getType();
    if (type == null) {
      type = "jar";
    }
    
    return getFolderUrl(dependency) + seperator_ +
        artifact + "-" + version + "." + type;
  }

  public String getFolderUrl(DependencyType dependency) {
    String group = dependency.getGroup();
    String artifact = dependency.getArtifact();
    String version = dependency.getVersion();
    
    StringBuilder sb = new StringBuilder();
    char [] groupChars = group.toCharArray();
    for (int i = 0; i < groupChars.length; i++) {
      char c = groupChars[i];
      if (c == '.') {
        sb.append("/");
      } else {
        sb.append(c);
      }
    }
    group = sb.toString();
    
    return repo_ + group + seperator_ + 
        artifact + seperator_ + version ;
  }
}
