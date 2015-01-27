package org.adligo.fabricate.common.i18n;

public interface I_SystemMessages {
  public String getCompiledOnX();
  public String getExceptionExecutingJavaWithTheFollowingJavaHome();
  public String getExceptionFabricateRequiresJava1_7OrGreater();
  public String getExceptionJavaVersionParameterExpected();
  
  public String getExceptionNoFabricateXmlOrProjectXmlFound();
  public String getExceptionNoFabricateHomeSet();
  public String getExceptionNoFabricateJarInFabricateHomeLib();
  public String getExceptionNoJavaHomeSet();
  
  public String getFabricateByAdligo();
  /**
   * For other languages this may be translated better as
   * Fabricate (failed). Where Fabricate (the name of the 
   * product) is not translated, but the word failed and sentence
   * structure is translated.
   * @return
   */
  public String getFabricationFailed();
  /**
   * This message should contain the jars that come
   * with fabricate only, to make sure that plug-ins are
   * using the fabricate.xml dependencies correctly 
   * and not the Fabricate Home/lib directory.
   * @return
   */
  public String getTheFollowingFabricateHomeLibShouldHaveOnlyTheseJars();
  
  public String getVersionX();
  /**
   * CLA means Command Line Argument
   * @return
   */
  public String getClaVersionShort();
  /**
   * CLA means Command Line Argument
   * @return
   */
  public String getClaVersion();
}
