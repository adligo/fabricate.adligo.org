package org.adligo.fabricate.repository;

import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.util.StringUtils;
import org.adligo.fabricate.models.dependencies.I_Dependency;

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
    if (StringUtils.isEmpty(repository)) {
      throw new IllegalArgumentException();
    }
    repo_ = repository;
    if (StringUtils.isEmpty(seperator)) {
      throw new IllegalArgumentException();
    }
    seperator_ = seperator;
  }
  
  /**
   * @param dependency
   * @return The name of the artifact file only,
   * with no directory information.
   */
  public String getArtifactFileName(I_Dependency dependency) {
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
  
  @Override
  public String getArtifactPath(I_Dependency dependency) {
    return getFolderPath(dependency) + seperator_ +
        getArtifactFileName(dependency);
  }

  @Override
  public String getArtifactUrl(I_Dependency dependency) {
    return getFolderUrl(dependency) + seperator_ + getArtifactFileName(dependency);
  }
  
  @Override
  public String getExtractPath(I_Dependency dependency, I_FabricateConstants constants) {
    return getFolderPath(dependency) + seperator_ + constants.getExtractDirName();
  }
  
  public String getFolderPath(I_Dependency dependency) {
    String group = dependency.getGroup();
    return repo_ + group;
  }
  
  public String getFolderUrl(I_Dependency dependency) {
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
  
  /**
   * 
   * @param dependency
   * @return The name of the artifact md5 file only,
   * with no directory information.
   */
  public String getMd5FileName(I_Dependency dependency) {
    return getArtifactFileName(dependency) + ".md5";
  }

  @Override
  public String getMd5Path(I_Dependency dependency) {
    return getFolderPath(dependency) + seperator_ +
        getMd5FileName(dependency);
  }
  
  @Override
  public String getMd5Url(I_Dependency dependency) {
    return getFolderUrl(dependency) + seperator_ + getMd5FileName(dependency);
  }

  public String getRepository() {
    return repo_;
  }

  public String getSeperator() {
    return seperator_;
  }
}
