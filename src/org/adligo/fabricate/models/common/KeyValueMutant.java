package org.adligo.fabricate.models.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KeyValueMutant implements I_KeyValue {
  private List<KeyValueMutant> children_ = new ArrayList<KeyValueMutant>();
  private String key_;
  private String value_;
  
  public KeyValueMutant() {}
  
  public KeyValueMutant(I_KeyValue other) {
    key_ = other.getKey();
    value_ = other.getValue();
    setChildren(other.getChildren());
  }
  
  public I_KeyValue get(int child) {
    return children_.get(child);
  }
  public List<I_KeyValue> getChildren() {
    return new ArrayList<I_KeyValue>(children_);
  }
  public String getKey() {
    return key_;
  }
  
  public String getValue() {
    return value_;
  }

  public void setChildren(Collection<I_KeyValue> children) {
    children_.clear();
    if (children != null) {
      for (I_KeyValue child: children) {
        if (child instanceof KeyValueMutant) {
          children_.add((KeyValueMutant) child);
        } else {
          children_.add(new KeyValueMutant(child));
        }
      }
    }
  }
  public void setKey(String key) {
    this.key_ = key;
  }
  public void setValue(String value) {
    this.value_ = value;
  }
  
  public int size() {
    return children_.size();
  }
}
