package org.adligo.fabricate.repository;

import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.models.dependencies.I_Dependency;
/**
 * Implementations of this class create the full path name
 * of a directory under the local_repository
 * where a dependency artifact can go.
 * It doesn't create the directory.
 * Also this is expected to be threadsafe.
 * 
 * @author scott
 *
 */
public interface I_RepositoryPathBuilder {
  public String getArtifactPath(I_Dependency dependency);
  public String getArtifactUrl(I_Dependency dependency);
  
  public String getExtractPath(I_Dependency dependency, I_FabricateConstants constants);
  
  public String getMd5Path(I_Dependency dependency);
  public String getMd5Url(I_Dependency dependency);
  
  public String getFolderPath(I_Dependency dependency);
}
