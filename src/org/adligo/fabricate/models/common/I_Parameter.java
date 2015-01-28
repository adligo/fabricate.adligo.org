package org.adligo.fabricate.models.common;

import java.util.List;

public interface I_Parameter {
  public I_Parameter get(int child);
  public List<I_Parameter> getChildren();
  public String getKey();
  public String getValue();
  public int size();
}
