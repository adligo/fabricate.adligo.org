package org.adligo.fabricate.common;

public interface I_ScmTask {
  public void setup(String projectGroupFolder, I_Scm scm);
  public void interact(String projectName, String projectVersion);
}
