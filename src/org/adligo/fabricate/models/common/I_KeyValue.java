package org.adligo.fabricate.models.common;

import java.util.List;

public interface I_KeyValue {
  public I_KeyValue get(int child);
  public List<I_KeyValue> getChildren();
  public String getKey();
  public String getValue();
  public int size();
}
