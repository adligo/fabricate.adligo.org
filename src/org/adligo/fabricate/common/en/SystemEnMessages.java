package org.adligo.fabricate.common.en;

import org.adligo.fabricate.common.i18n.I_SystemMessages;

public class SystemEnMessages implements I_SystemMessages {
  
  private static final String DID_NOT_PASS_THE_EXTRACT_CHECK = "did not pass the extract check.";
  private static final String DID_NOT_PASS_THE_MD5_CHECK = "did not pass the md5 check.";
  
  private static final String FAILED = "failed!";
  private static final String FINISHED = "finished.";
  
  private static final String STARTING_DOWNLOAD_FROM_THE_FOLLOWING_URL = 
      "Starting download from the following url;";
  public static final SystemEnMessages INSTANCE = new SystemEnMessages();
  private static final String COMPILED_ON_X = "Compiled on <X/>.";
  private static final String FABRICATION_FAILED = "Fabrication failed!";
  private static final String EXCEPTION_NO_FABRICATE_JAR_IN_$FABRICATE_HOME_LIB_FOR_THE_FOLLOWING_$FABRICATE_HOME =
      "Exception: No fabricate_*.jar in $FABRICATE_HOME/lib for the following $FABRICATE_HOME;";
  private static final String EXCEPTION_NO_$FABRICATE_HOME_ENVIRONMENT_VARIABLE_SET = 
      "Exception: No $FABRICATE_HOME environment variable set.";
  private static final String EXCEPTION_NO_$JAVA_HOME_ENVIRONMENT_VARIABLE_SET = 
      "Exception: No $JAVA_HOME environment variable set.";

  
  private static final String EXCEPTION_EXECUTING_JAVA_WITH_THE_FOLLOWING_JAVA_HOME = 
      "Exception: There was a problem executing java with the following $JAVA_HOME;";
  private static final String EXCEPTION_FABRICATE_REQUIRES_JAVA_1_7_OR_GREATER = 
      "Exception: Fabricate requires Java 1.7 or greater.";
  private static final String EXCEPTION_JAVA_VERSION_PARAMETER_EXPECTED = 
      "Exception: Java version parameter expected.";
  private static final String EXCEPTION_NO_FABRICATE_XML_OR_PROJECT_XML_FOUND = 
      "Exception: No fabricate.xml or project.xml found.";
  private static final String EXTRACTION_OF_THE_FOLLOWING_ARTIFACT = 
      "Extraction of the following artifact;";
  private static final String EXTRACTING_THE_FOLLOWING_ARTIFACT = 
      "Extracting the following artifact;";

  
  private static final String FABRICATE_BY_ADLIGO = "Fabricate by Adligo.";
  
  private static final String PASSED_THE_EXTRACT_CHECK = "passed the extract check.";
  private static final String PASSED_THE_MD5_CHECK = "passed the md5 check.";
  
  private static final String THE_DOWNLOAD_FROM_THE_FOLLOWING_URL = 
      "The download from the following url;";
  private static final String THE_FOLLOWING_ARTIFACT = "The following artifact;";
  private static final String THE_FOLLOWING_FABRICATE_HOME_SHOULD_HAVE_ONLY_THESE_JARS = 
      "The following Fabricate Home should have only these jars;";
  private static final String TO_THE_FOLLOWING_FOLDER = "to the following folder;";
  

  private static final String VERSION_X = "Version <X/>.";
  
  private SystemEnMessages() {
  }

  @Override
  public String getCompiledOnX() {
    return COMPILED_ON_X;
  }
  
  public String getDidNotPassTheMd5Check() {
    return DID_NOT_PASS_THE_MD5_CHECK;
  }
  public String getDidNotPassTheExtractCheck() {
    return DID_NOT_PASS_THE_EXTRACT_CHECK;
  }
  
  @Override
  public String getExceptionExecutingJavaWithTheFollowingJavaHome() {
    return EXCEPTION_EXECUTING_JAVA_WITH_THE_FOLLOWING_JAVA_HOME;
  }
  
  @Override
  public String getExceptionFabricateRequiresJava1_7OrGreater() {
    return EXCEPTION_FABRICATE_REQUIRES_JAVA_1_7_OR_GREATER;
  }
  
  @Override
  public String getExceptionJavaVersionParameterExpected() {
    return EXCEPTION_JAVA_VERSION_PARAMETER_EXPECTED;
  }
  
  @Override
  public String getExceptionNoFabricateXmlOrProjectXmlFound() {
    return EXCEPTION_NO_FABRICATE_XML_OR_PROJECT_XML_FOUND;
  }
  
  @Override
  public String getExceptionNoJavaHomeSet() {
    return EXCEPTION_NO_$JAVA_HOME_ENVIRONMENT_VARIABLE_SET;
  }
  
  @Override
  public String getExceptionNoFabricateHomeSet() {
    return EXCEPTION_NO_$FABRICATE_HOME_ENVIRONMENT_VARIABLE_SET;
  }
  
  @Override
  public String getExceptionNoFabricateJarInFabricateHomeLib() {
    return EXCEPTION_NO_FABRICATE_JAR_IN_$FABRICATE_HOME_LIB_FOR_THE_FOLLOWING_$FABRICATE_HOME;
  }
  
  @Override
  public String getExtractingTheFollowingArtifact() {
    return EXTRACTING_THE_FOLLOWING_ARTIFACT;
  }
  
  @Override
  public String getExtractionOfTheFollowingArtifact() {
    return EXTRACTION_OF_THE_FOLLOWING_ARTIFACT;
  }
  
  @Override
  public String getFabricateByAdligo() {
    return FABRICATE_BY_ADLIGO;
  }
  
  @Override
  public String getFabricationFailed() {
    return FABRICATION_FAILED;
  }
  
  @Override
  public String getFailed() {
    return FAILED;
  }

  @Override
  public String getFinished() {
    return FINISHED;
  }

  public String getPassedTheMd5Check() {
    return PASSED_THE_MD5_CHECK;
  }
  public String getPassedTheExtractCheck() {
    return PASSED_THE_EXTRACT_CHECK;
  }
  
  
  @Override
  public String getStartingDownloadFromTheFollowingUrl() {
    return STARTING_DOWNLOAD_FROM_THE_FOLLOWING_URL;
  }
  
  @Override
  public String getToTheFollowingFolder() {
    return TO_THE_FOLLOWING_FOLDER;
  }
  
  @Override
  public String getTheDownloadFromTheFollowingUrl() {
    return THE_DOWNLOAD_FROM_THE_FOLLOWING_URL;
  }
  
  public String getTheFollowingArtifact() {
    return THE_FOLLOWING_ARTIFACT;
  }
  @Override
  public String getTheFollowingFabricateHomeLibShouldHaveOnlyTheseJars() {
    return THE_FOLLOWING_FABRICATE_HOME_SHOULD_HAVE_ONLY_THESE_JARS;
  }
  
  @Override
  public String getVersionX() {
    return VERSION_X;
  }
}
