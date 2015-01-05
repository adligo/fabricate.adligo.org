package org.adligo.fabricate.external;

import org.adligo.fabricate.xml.io_v1.library_v1_0.DependencyType;
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
  public String getUrl(DependencyType dependency);
  public String getPath(DependencyType dependency);
  public String getFolderPath(DependencyType dependency);
}
