package org.adligo.fabricate.common.i18n;

public interface I_SystemMessages {
  public String getArtifactColon();
  public String getAntHelperRequiresADirectoryArgument();
  public String getArchiveStageXCompletedSuccessfully();
  public String getArchiveStageXIsStillSettingUp();
  public String getArchiveStageXIsStillRunning();
  public String getArchiveStageXIsStillRunningOnProjectZ() ;
  public String getArchiveStageXTaskYIsStillRunning();
  public String getArchiveStageXTaskYIsStillRunningOnProjectZ();
  public String getArchiveStageXProjectYIsWaitingOnTheFollowingProjects();
  
  public String getCommandXCompletedSuccessfully();
  public String getCommandXIsStillSettingUp();
  public String getCommandXIsStillRunning();
  public String getCommandXIsStillRunningOnProjectZ() ;
  public String getCommandXTaskYIsStillRunning();
  public String getCommandXTaskYIsStillRunningOnProjectZ();
  public String getCommandXProjectYIsWaitingOnTheFollowingProjects();
  
  public String getBuildingFabricateRuntimeClassPath();
  public String getBuildStageXCompletedSuccessfully();
  public String getBuildStageXIsStillSettingUp();
  public String getBuildStageXIsStillRunning();
  public String getBuildStageXIsStillRunningOnProjectZ();
  public String getBuildStageXTaskYIsStillRunning();
  public String getBuildStageXTaskYIsStillRunningOnProjectZ();
  public String getBuildStageXProjectYIsWaitingOnTheFollowingProjects();
  
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
  public String getExceptionUnableToFindSSH_AUTH_SOCKWhenParsingOutputOfSshAgent();
  public String getExceptionUnableToFindSSH_AGENT_PIDWhenParsingOutputOfSshAgent();
  
  public String getExtractingTheFollowingArtifact();
  public String getExtractionOfTheFollowingArtifact();
  
  public String getFacetXCompletedSuccessfully();
  public String getFacetXIProjectYIsWaitingOnTheFollowingProjects();
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
  
  public String getFacetXIsStillSettingUp();
  public String getFacetXIsStillRunning();
  public String getFacetXIsStillRunningOnProjectZ();
  public String getFacetXTaskYIsStillRunning() ;
  public String getFacetXTaskYIsStillRunningOnProjectZ();
  
  public String getForSemicolon();
  public String getFileNameColon();
  public String getFinished();
  public String getFinishedGetCloneOnProjectX();
  public String getFinishedGetPullOnProjectX();
  public String getFinishedXOnProjectY();
  
  public String getGitDoesNotAppearToBeInstalledPleaseInstallIt();
  public String getGroupColon();
  
  public String getInsteadOfTheFollowingActualGenericType();
  public String getItWasExpectedToImplementTheFollowingInterface();
  
  public String getLocksMustContainAtLeastOneAllowedCaller();
  public String getLocksMayOnlyBeSetOnOrUnderTheCallersPackage();
  
  public String getManagingTheFollowingLocalRepository();
  public String getManagingFabricateRuntimeClassPathDependencies();
  
  public String getNoProjectFoundWithNameX();
  public String getNoRemoteRepositoriesCouldBeReached();
  public String getNoRoutineFoundWithNameX();
  
  public String getPassedTheMd5Check();
  public String getPassedTheExtractCheck();
  public String getProjectsAreLocatedInTheFollowingDirectory();
  
  
  public String getRunningArchiveStageX();
  public String getRunningArchiveStages();
  public String getRunningBuildStageX();
  public String getRunningBuildStages();
  public String getRunningCommandX();
  public String getRunningCommands();
  public String getRunningFacetX();
  public String getRunningFacets();
  
  public String getSendingOptsToScript();
  
  public String getStartingDownloadFromTheFollowingUrl();
  public String getStartingGetCloneOnProjectX();
  public String getStartingGetPullOnProjectX();
  public String getStartingXOnProjectY();
  
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
  
  public String getTheFollowingCommandLineArgumentIsRequiredForCommandX();
  public String getTheFollowingCommandLineArgumentWasNotProvidedForCommandXUsingDefaultY();
  public String getTheFollowingCommandLineProgramExitedAbnormallyWithExitCodeX();
  public String getTheFollowingDependenciesVersionDoesNotMatchTheFileName();
  public String getTheFollowingRemoteRepositoryAppearsToBeDown();
  
  public String getTheFollowingRoutineImplementsXHoweverItsGetClassTypeMethodReturnedYClassTypesInsteadOfOne(); 
  public String getTheFollowingRoutineImplementsXHoweverItsClassTypeIsNull();
  public String getTheFollowingRoutineImplementsXButTheRoutinesPopulatorValueIsNull();
  public String getTheFollowingRequiredFileIsMissing();
  
  public String getTheFollowingArtifact();
  public String getTheMemoryKeyXHasBeenLockedByTheFollowingClasses();
  
  public String getThereWasAProblemCreatingTheFollowingDirectory();
  public String getThereWasAProblemCreatingTheFollowingRoutine();
  public String getThereWasAProblemCreatingRunMarkerInTheFollowingDirectory();
  public String getThereWasAProblemCheckingOutVersionXOnTheFollowingProject();
  public String getThereWasAProblemDeletingTheFollowingDirectory();
  public String getThereWasAProblemUpdatingTheFollowingProject();
  public String getThereWasAProblemVerifyingOrDownloadingTheFollowingDependency();
  
  public String getThisMethodMustBeCalledFromTheMainThread();
  public String getThisVersionOfFabricateRequiresGitXOrGreater();
  
  public String getToTheFollowingFolder();
  public String getTraitXIsStillSettingUp();
  public String getTraitXIsStillRunning();
  public String getTraitXIsStillRunningOnProjectZ();
  public String getTraitXTaskYIsStillRunning();
  public String getTraitXTaskYIsStillRunningOnProjectZ();
  public String getTypeColon();
  
  public String getUnableToLoadTheFollowingClass();
  public String getUnknown();
  public String getUsingTheFollowingRemoteRepositories();
  
  public String getVersionX();
  public String getVersionColon();
  public String getWithTheFollowingGenericTypeX();
}
