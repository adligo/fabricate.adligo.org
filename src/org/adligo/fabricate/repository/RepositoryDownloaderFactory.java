package org.adligo.fabricate.repository;

import org.adligo.fabricate.common.log.I_FabLog;

public class RepositoryDownloaderFactory {

  public RepositoryDownloader create(I_FabLog log, String localRepositoryPath) {
    return new RepositoryDownloader(log, localRepositoryPath);
  }
}