package org.adligo.fabricate.common.en;

import org.adligo.fabricate.common.i18n.I_GitMessages;

public class GitEnMessages implements I_GitMessages {
  private static final String DISCOVERED_X_PROJECTS = "Discovered <X/> projects.";
  private static final String FINISHED_GIT_CHECKOUT_FOR_THE_FOLLOWING_PROJECT = "Finished git checkout for the following project;";
  
  private static final String FINISHED_GIT_CLONE_FOR_THE_FOLLOWING_PROJECT = "Finished git clone for the following project;";
  private static final String FINISHED_GIT_COMMIT_FOR_THE_FOLLOWING_PROJECT = "Finished git checkout for the following project;";
 
  private static final String FINISHED_GIT_PULL_FOR_THE_FOLLOWING_PROJECT = "Finished git pull for the following project;";
  private static final String FINISHED_GIT_PUSH_FOR_THE_FOLLOWING_PROJECT = "Finished git push for the following project;";
  
  private static final String FINISHED_WITH_GIT_STAGE = "Finished git stage.";
  
  
  private static final String PLEASE_ENTER_THE_PASSWORD_FOR_YOUR_SSH_KEY = 
      "Please enter the password for your ssh key (Enter for empty password):";
  private static final String PLEASE_ENTER_YOUR_COMMIT_MESSAGE = 
      "Please enter your commit message (Enter twice to finish);";
  private static final String THE_FOLLOWING_PROJECT_HAS_A_VERSION_X_IN_FABRICATE_XML_BUT_IS_NOT_CHECKED_OUT_TO_THAT_VERSION_ABORT = 
      "The following project has the version '<X/>' in fabricate.xml but is not checked out to that version aborting fabrication;";
  
  private static final String THE_PROJECT_DIRECTORY_IS = "The project directory is as follows;";
  
  private static final String STARTED_GIT_CLONE_FOR_THE_FOLLOWING_PROJECT = "Started git clone for the following project;";
  private static final String STARTED_GIT_COMMIT_FOR_THE_FOLLOWING_PROJECT = "Started git commit for the following project;";
  
  private static final String STARTED_GIT_CHECKOUT_FOR_THE_FOLLOWING_PROJECT = "Started git checkout for the following project;";
  private static final String STARTED_GIT_PULL_FOR_THE_FOLLOWING_PROJECT = "Started git pull for the following project;";
  private static final String STARTED_GIT_PUSH_FOR_THE_FOLLOWING_PROJECT = "Started git push for the following project;";
  
  public static final GitEnMessages INSTANCE = new GitEnMessages();
    
  private GitEnMessages() {}
 
  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.en.I_GitMessages#getDiscoveredXProjects()
   */
  @Override
  public String getDiscoveredXProjects() {
    return DISCOVERED_X_PROJECTS;
  }
  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.en.I_GitMessages#getFinishedGitCloneForTheFollowingProject()
   */
  @Override
  public String getFinishedGitCloneForTheFollowingProject() {
    return FINISHED_GIT_CLONE_FOR_THE_FOLLOWING_PROJECT;
  }
  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.en.I_GitMessages#getFinishedGitCommitForTheFollowingProject()
   */
  @Override
  public String getFinishedGitCommitForTheFollowingProject() {
    return FINISHED_GIT_COMMIT_FOR_THE_FOLLOWING_PROJECT;
  }
  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.en.I_GitMessages#getFinishedGitCheckoutForTheFollowingProject()
   */
  @Override
  public String getFinishedGitCheckoutForTheFollowingProject() {
    return FINISHED_GIT_CHECKOUT_FOR_THE_FOLLOWING_PROJECT;
  }
  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.en.I_GitMessages#getFinishedGitPullForTheFollowingProject()
   */
  @Override
  public String getFinishedGitPullForTheFollowingProject() {
    return FINISHED_GIT_PULL_FOR_THE_FOLLOWING_PROJECT;
  }
  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.en.I_GitMessages#getFinishedGitPushForTheFollowingProject()
   */
  @Override
  public String getFinishedGitPushForTheFollowingProject() {
    return FINISHED_GIT_PUSH_FOR_THE_FOLLOWING_PROJECT;
  }
  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.en.I_GitMessages#getFinishedGitStage()
   */
  @Override
  public String getFinishedGitStage() {
    return FINISHED_WITH_GIT_STAGE;
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.en.I_GitMessages#getPleaseEnterThePasswordForYourSshKey()
   */
  @Override
  public String getPleaseEnterThePasswordForYourSshKey() {
    return PLEASE_ENTER_THE_PASSWORD_FOR_YOUR_SSH_KEY;
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.en.I_GitMessages#getPleaseEnterYourCommitMessage()
   */
  @Override
  public String getPleaseEnterYourCommitMessage() {
    return PLEASE_ENTER_YOUR_COMMIT_MESSAGE;
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.en.I_GitMessages#getStartedGitPullForTheFollowingProject()
   */
  @Override
  public String getStartedGitPullForTheFollowingProject() {
    return STARTED_GIT_PULL_FOR_THE_FOLLOWING_PROJECT;
  }

  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.en.I_GitMessages#getStartedGitCloneForTheFollowingProject()
   */
  @Override
  public String getStartedGitCloneForTheFollowingProject() {
    return STARTED_GIT_CLONE_FOR_THE_FOLLOWING_PROJECT;
  }
  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.en.I_GitMessages#getStartedGitCommitForTheFollowingProject()
   */
  @Override
  public String getStartedGitCommitForTheFollowingProject() {
    return STARTED_GIT_COMMIT_FOR_THE_FOLLOWING_PROJECT;
  }
  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.en.I_GitMessages#getStartedGitCheckoutForTheFollowingProject()
   */
  @Override
  public String getStartedGitCheckoutForTheFollowingProject() {
    return STARTED_GIT_CHECKOUT_FOR_THE_FOLLOWING_PROJECT;
  }
  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.en.I_GitMessages#getStartedGitPushForTheFollowingProject()
   */
  @Override
  public String getStartedGitPushForTheFollowingProject() {
    return STARTED_GIT_PUSH_FOR_THE_FOLLOWING_PROJECT;
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.en.I_GitMessages#getTheProjectDirectoryIs()
   */
  @Override
  public String getTheProjectDirectoryIs() {
    return THE_PROJECT_DIRECTORY_IS;
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.en.I_GitMessages#getTheFollowingProjectHasAVersionXInFabricatXmlButIsNotCheckedOutToThatVersionAborting()
   */
  @Override
  public String getTheFollowingProjectHasAVersionXInFabricatXmlButIsNotCheckedOutToThatVersionAborting() {
    return THE_FOLLOWING_PROJECT_HAS_A_VERSION_X_IN_FABRICATE_XML_BUT_IS_NOT_CHECKED_OUT_TO_THAT_VERSION_ABORT;
  }
 
}
