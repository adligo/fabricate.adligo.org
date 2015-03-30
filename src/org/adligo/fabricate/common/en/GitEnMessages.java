package org.adligo.fabricate.common.en;

import org.adligo.fabricate.common.i18n.I_GitMessages;

public class GitEnMessages implements I_GitMessages {

  private static final String CANCEL = "Cancel";
  private static final String DEFAULT = "Default";
  
  private static final String DISCOVERED_X_PROJECTS = "Discovered <X/> projects.";
  private static final String FINISHED_GIT_CHECKOUT_FOR_THE_FOLLOWING_PROJECT = 
      "Finished git checkout for the following project;";
  
  private static final String FINISHED_GIT_CLONE_FOR_THE_FOLLOWING_PROJECT = 
      "Finished git clone for the following project;";
  private static final String FINISHED_GIT_COMMIT_FOR_THE_FOLLOWING_PROJECT = 
      "Finished git commit for the following project;";
 
  private static final String FINISHED_GIT_PULL_FOR_THE_FOLLOWING_PROJECT = 
      "Finished git pull for the following project;";
  private static final String FINISHED_GIT_PUSH_FOR_THE_FOLLOWING_PROJECT = 
      "Finished git push for the following project;";
  
  private static final String FINISHED_WITH_GIT_STAGE = "Finished git stage.";
  
  private static final String SELECT_ALL = "Select All";
  private static final String SELECT_NONE = "Select None";
  private static final String OK = "Ok";
  
  private static final String PLEASE_ENTER_YOUR_DEFAULT_COMMIT_MESSAGE = 
      "Please enter your default commit message.";
  private static final String PLEASE_ENTER_YOUR_COMMIT_MESSAGE_FOR_PROJECT_X = 
      "Please enter your commit message for project <X/>.";
  
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
   * @see org.adligo.fabricate.common.en.I_GitMessages#getCancel()
   */
  @Override
  public String getCancel() {
    return CANCEL;
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.en.I_GitMessages#getDefault()
   */
  @Override
  public String getDefault() {
    return DEFAULT;
  }
  
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
   * @see org.adligo.fabricate.common.en.I_GitMessages#getOk()
   */
  @Override
  public String getOk() {
    return OK;
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.en.I_GitMessages#getPleaseEnterYourCommitMessageForProjectX()
   */
  @Override  
  public String getPleaseEnterYourCommitMessageForProjectX() {
    return PLEASE_ENTER_YOUR_COMMIT_MESSAGE_FOR_PROJECT_X;
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.en.I_GitMessages#getPleaseEnterYourCommitMessage()
   */
  @Override
  public String getPleaseEnterYourDefaultCommitMessage() {
    return PLEASE_ENTER_YOUR_DEFAULT_COMMIT_MESSAGE;
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.en.I_GitMessages#getAll()
   */
  @Override
  public String getSelectAll() {
    return SELECT_ALL;
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.en.I_GitMessages#getNone()
   */
  @Override
  public String getSelectNone() {
    return SELECT_NONE;
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
