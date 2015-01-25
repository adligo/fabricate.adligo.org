package org.adligo.fabricate.models.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class KeyValue implements I_KeyValue {
  private List<I_KeyValue> children_;
  private String key_;
  private String value_;

  public KeyValue(I_KeyValue other) {
    key_ = other.getKey();
    value_ = other.getValue();
    setChildren(other.getChildren());
  }
  
  public I_KeyValue get(int child) {
    return children_.get(child);
  }
  public List<I_KeyValue> getChildren() {
    return children_;
  }
  public String getKey() {
    return key_;
  }
  
  public String getValue() {
    return value_;
  }

  private void setChildren(Collection<I_KeyValue> children) {
    List<I_KeyValue> toAdd = new ArrayList<I_KeyValue>();
    if (children != null) {
      for (I_KeyValue child: children) {
        if (child instanceof KeyValue) {
          toAdd.add((KeyValue) child);
        } else {
          toAdd.add(new KeyValue(child));
        }
      }
    }
    children_ = Collections.unmodifiableList(toAdd);
  }
  
  public int size() {
    return children_.size();
  }
}
