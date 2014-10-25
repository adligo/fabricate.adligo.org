package org.adligo.fabricate.common;

import java.util.Map;

public interface I_FabLogModel extends I_FabLog {
  public Map<Class<?>, Boolean> getSettings();
}
