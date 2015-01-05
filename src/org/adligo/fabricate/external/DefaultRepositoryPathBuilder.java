package org.adligo.fabricate.external;

import org.adligo.fabricate.common.StringUtils;
import org.adligo.fabricate.xml.io_v1.library_v1_0.DependencyType;

/**
 * This can be used with a local or remote repository
 * it assumes the repository pass to it has the last slash
 * 
 * @author scott
 *
 */
public class DefaultRepositoryPathBuilder implements I_RepositoryPathBuilder {
  public static final String DASH = "-";
  private String repo_;
  private String seperator_;
  
  public DefaultRepositoryPathBuilder(String repository, String seperator) {
    repo_ = repository;
    seperator_ = seperator;
  }
  
  
  @Override
  public String getPath(DependencyType dependency) {
    return getFolderPath(dependency) + seperator_ +
        getFileName(dependency);
  }

  public String getFileName(DependencyType dependency) {
    String fileName  = dependency.getFileName();
    if (!StringUtils.isEmpty(fileName)) {
      return fileName;
    }
    String artifact = dependency.getArtifact();
    String version = dependency.getVersion();
    String type = dependency.getType();
    if (type == null) {
      type = "jar";
    }
    return artifact + DASH + version + "." + type;
  }
  
  public String getFolderPath(DependencyType dependency) {
    String group = dependency.getGroup();
    return repo_ + group;
  }
  
  public String getUrl(DependencyType dependency) {
    String type = dependency.getType();
    if (type == null) {
      type = "jar";
    }
    
    return getFolderUrl(dependency) + seperator_ + getFileName(dependency);
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
    
    return repo_ + group + "/" + 
        artifact + "/" + version ;
  }
}
