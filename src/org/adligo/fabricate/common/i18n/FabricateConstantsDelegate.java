package org.adligo.fabricate.common.i18n;

import java.util.ArrayList;
import java.util.List;

public class FabricateConstantsDelegate implements I_FabricateConstants {
  private I_FabricateConstants constants_;
  
  public I_ProjectMessages getProjectMessages() {
    return constants_.getProjectMessages();
  }
  
  public FabricateConstantsDelegate(String className) {
    try {
      constants_ = (I_FabricateConstants) Class.forName(className).newInstance();
    } catch (ClassNotFoundException | IllegalArgumentException | InstantiationException | IllegalAccessException x) {
      throw new IllegalArgumentException(x);
    }
  }
  
  public I_FabricateConstants getConstants() {
    return constants_;
  }

  @SuppressWarnings("unused")
  public void setConstants(I_FabricateConstants constants) {
    List<String> classNames = new ArrayList<String>();
    classNames.add("org.adligo.fabricate.Fabricate");
    classNames.add("org.adligo.fabricate_tests.mocks.FabricateConstantsMock");
    new MethodBlocker(FabricateConstantsDelegate.class,"setConstants" , classNames);
    this.constants_ = constants;
  }

  
  
}
