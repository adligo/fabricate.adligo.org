package org.adligo.fabricate.common;

import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.i18n.I_ProjectMessages;

import java.util.ArrayList;
import java.util.List;

public class FabricateConstantsWrapper implements I_FabricateConstants {
  private I_FabricateConstants constants_;
  
  public I_ProjectMessages getProjectMessages() {
    return constants_.getProjectMessages();
  }
  
  public FabricateConstantsWrapper(I_FabricateConstants constants) {
    constants_ = constants;
  }
  
  public I_FabricateConstants getConstants() {
    return constants_;
  }

  @SuppressWarnings("unused")
  public void setConstants(I_FabricateConstants constants) {
    List<String> classNames = new ArrayList<String>();
    classNames.add("org.adligo.fabricate.Fabricate");
    classNames.add("org.adligo.fabricate_tests.mocks.FabricateConstantsMock");
    new MethodBlocker(FabricateConstantsWrapper.class,"setConstants" , classNames);
    this.constants_ = constants;
  }

  
  
}
