package org.adligo.fabricate.common.i18n;

public interface I_GitMessages {
  
  public String getCancel();
  public String getDefault();
  
  public abstract String getDiscoveredXProjects();

  public abstract String getFinishedGitCloneForTheFollowingProject();

  public abstract String getFinishedGitCommitForTheFollowingProject();

  public abstract String getFinishedGitCheckoutForTheFollowingProject();

  public abstract String getFinishedGitPullForTheFollowingProject();

  public abstract String getFinishedGitPushForTheFollowingProject();

  public abstract String getFinishedGitStage();

  public String getOk();
  
  public abstract String getPleaseEnterYourCommitMessageForProjectX();
  public abstract String getPleaseEnterYourDefaultCommitMessage();
  
  public String getSelectAll();
  public String getSelectNone();
  
  public abstract String getStartedGitPullForTheFollowingProject();

  public abstract String getStartedGitCloneForTheFollowingProject();

  public abstract String getStartedGitCommitForTheFollowingProject();

  public abstract String getStartedGitCheckoutForTheFollowingProject();

  public abstract String getStartedGitPushForTheFollowingProject();

  public abstract String getTheProjectDirectoryIs();

  public abstract String getTheFollowingProjectHasAVersionXInFabricatXmlButIsNotCheckedOutToThatVersionAborting();

}