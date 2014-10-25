package org.adligo.fabricate.common;

import java.util.HashMap;
import java.util.Map;

public class FabLogMutant implements I_FabLogModel {
  private Map<Class<?>,Boolean> settings = new HashMap<Class<?>,Boolean>();
  
  @SuppressWarnings("boxing")
  @Override
  public boolean isLogEnabled(Class<?> clazz) {
    Boolean toRet = settings.get(clazz);
    if (toRet != null) {
      return toRet;
    }
    return false;
  }
  
  @SuppressWarnings("boxing")
  public void setLogSetting(Class<?> clazz, boolean b) {
    settings.put(clazz, b);
  }

  @Override
  public Map<Class<?>, Boolean> getSettings() {
    return settings;
  }

}
