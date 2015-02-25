package org.adligo.fabricate.common.i18n;

public interface I_SystemMessages {
  
  public String getArchiveStageXIsStillSettingUp();
  public String getArchiveStageXIsStillRunning();
  public String getArchiveStageXIsStillRunningOnProjectZ() ;
  public String getArchiveStageXTaskYIsStillRunning();
  public String getArchiveStageXTaskYIsStillRunningOnProjectZ();
  
  public String getCommandXIsStillSettingUp();
  public String getCommandXIsStillRunning();
  public String getCommandXIsStillRunningOnProjectZ() ;
  public String getCommandXTaskYIsStillRunning();
  public String getCommandXTaskYIsStillRunningOnProjectZ();
  
  public String getBuildingFabricateRuntimeClassPath();
  public String getBuildStageXIsStillSettingUp();
  public String getBuildStageXIsStillRunning();
  public String getBuildStageXIsStillRunningOnProjectZ();
  public String getBuildStageXTaskYIsStillRunning();
  public String getBuildStageXTaskYIsStillRunningOnProjectZ();
  public String getCheckingFabricateRuntimeDependencies();
  public String getCompiledOnX();
  
  
  public String getDidNotPassTheMd5Check();
  public String getDidNotPassTheExtractCheck();
  
  public String getDurationWasXMilliseconds();
  public String getDurationWasXMinutes();
  public String getDurationWasXSeconds();
  
  public String getExceptionExecutingJavaWithTheFollowingJavaHome();
  public String getExceptionFabricateRequiresJava1_7OrGreater();
  public String getExceptionJavaVersionParameterExpected();
  
  public String getExceptionNoFabricateXmlOrProjectXmlFound();
  public String getExceptionNoFabricateHomeSet();
  public String getExceptionNoFabricateJarInFabricateHomeLib();
  public String getExceptionNoStartTimeArg();
  public String getExceptionNoJavaHomeSet();
  
  public String getExtractingTheFollowingArtifact();
  public String getExtractionOfTheFollowingArtifact();
  
  public String getFabricateByAdligo();
  public String getFabricating();
  public String getFabricateAppearsToBeAlreadyRunning();
  public String getFabricateAppearsToBeAlreadyRunningPartTwo();
  
  /**
   * For other languages this may be translated better as
   * Fabricate (failed). Where Fabricate (the name of the 
   * product) is not translated, but the word failed and sentence
   * structure is translated.
   * @return
   */
  public String getFabricationFailed();
  public String getFabricationSuccessful();
  public String getFailed();
  public String getFinished();
  
  public String getManagingTheFollowingLocalRepository();
  public String getManagingFabricateRuntimeClassPathDependencies();
  public String getNoRemoteRepositoriesCouldBeReached();
  public String getPassedTheMd5Check();
  public String getPassedTheExtractCheck();
  
  public String getSendingOptsToScript();
  
  public String getStartingDownloadFromTheFollowingUrl();
  
  public String getToTheFollowingFolder();
  
  public String getTheDownloadFromTheFollowingUrl();
  /**
   * This message should contain the jars that come
   * with fabricate only, to make sure that plug-ins are
   * using the fabricate.xml dependencies correctly 
   * and not the Fabricate Home/lib directory.
   * @return
   */
  public String getTheFollowingFabricateHomeLibShouldHaveOnlyTheseJars();
  
  public String getTheFollowingFabricateLibraryCanNotBeFound();
  
  public String getTheFollowingListOfFabricateLibrariesContainsACircularReference();
  public String getTheFollowingLocalRepositoryIsLockedByAnotherProcess();
  
  public String getTheFollowingRemoteRepositoryAppearsToBeDown();
  
  public String getTheFollowingArtifact();
  public String getThereWasAProblemCreatingTheFollowingDirectory();
  public String getThereWasAProblemCreatingRunMarkerInTheFollowingDirectory();
  
  public String getUnableToLoadTheFollowingClass();
  public String getUnknown();
  public String getUsingTheFollowingRemoteRepositories();
  
  public String getVersionX();
}
