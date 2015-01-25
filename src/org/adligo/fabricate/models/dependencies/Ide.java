package org.adligo.fabricate.models.dependencies;

import org.adligo.fabricate.models.common.I_KeyValue;
import org.adligo.fabricate.models.common.KeyValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Ide implements I_Ide {
  private String name_;
  private List<I_KeyValue> children_;
  
  public Ide() {}
  
  public Ide(I_Ide other) {
    name_ = other.getName();
    setChildren(other.getChildren());
  }
  @Override
  public I_KeyValue get(int child) {
    return children_.get(child);
  }
  
  @Override
  public List<I_KeyValue> getChildren() {
    return children_;
  }
  
  public String getName() {
    return name_;
  }
  
  private void setChildren(List<I_KeyValue> args) {
    List<I_KeyValue> toAdd = new ArrayList<I_KeyValue>();
    if (args != null) {
      for (I_KeyValue kv: args) {
        if (kv instanceof KeyValue) {
          toAdd.add((KeyValue) kv);
        } else {
          toAdd.add(new KeyValue(kv));
        }
      }
    }
    children_ = Collections.unmodifiableList(toAdd);
  }
  
  @Override
  public int size() {
    return children_.size();
  }
  
}
