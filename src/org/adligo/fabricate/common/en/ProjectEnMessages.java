package org.adligo.fabricate.common.en;

import org.adligo.fabricate.common.i18n.I_ProjectMessages;

public class ProjectEnMessages implements I_ProjectMessages {

  private static final String DOES_NOT_CONTAIN_A_PROJECT_XML_FILE = 
        "does not contain a project.xml file.";
  private static final String MUST_DEPEND_ON_A_PROJECT_CONTAINED_IN_THE_FABRICATE_XML_FILE = 
        "must depend on a project contained in the fabricate.xml file.";
  private static final String CAN_NOT_DEPEND_ON_ITSELF = 
        "can NOT depend on itself.";
  private static final String THE_FOLLOWING_PROJECT = 
        "The following project;";

  @Override
  public String getTheFollowingProject() {
    return THE_FOLLOWING_PROJECT;
  }

  @Override
  public String getCanNotDependOnIteself() {
    return CAN_NOT_DEPEND_ON_ITSELF;
  }

  @Override
  public String getMustDependOnAFabricateXmlProject() {
    return MUST_DEPEND_ON_A_PROJECT_CONTAINED_IN_THE_FABRICATE_XML_FILE;
  }

  @Override
  public String getDoesNotContainAProjectXml() {
    return DOES_NOT_CONTAIN_A_PROJECT_XML_FILE;
  }

}
