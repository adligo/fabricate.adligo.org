package org.adligo.fabricate.external;

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
  public String getUrl(I_Dependency dependency);
  public String getPath(I_Dependency dependency);
  public String getFolderPath(I_Dependency dependency);
}
