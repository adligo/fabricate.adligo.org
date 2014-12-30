package org.adligo.fabricate.common.i18n;

public class ProjectMessagesDelegate implements I_ProjectMessages {
  private I_ProjectMessages delegate_;
  
  public I_ProjectMessages getDelegate() {
    return delegate_;
  }

  public void setDelegate(I_ProjectMessages delegate) {
    this.delegate_ = delegate;
  }

  public String getTheFollowingProject() {
    return delegate_.getTheFollowingProject();
  }

  public String getCanNotDependOnIteself() {
    return delegate_.getCanNotDependOnIteself();
  }

  public String getMustDependOnAFabricateXmlProject() {
    return delegate_.getMustDependOnAFabricateXmlProject();
  }

  public String getDoesNotContainAProjectXml() {
    return delegate_.getDoesNotContainAProjectXml();
  }
  
}
