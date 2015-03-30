package org.adligo.fabricate.routines.view;

public class ViewFactory {

  public I_GitCommitView newGitCommitView() {
    return new GitCommitView();
  }
}
