package org.adligo.fabricate.models.dependencies;

import org.adligo.fabricate.models.common.I_KeyValue;
import org.adligo.fabricate.models.common.KeyValueMutant;

import java.util.ArrayList;
import java.util.List;

public class IdeMutant implements I_Ide {
  private String name_;
  private List<KeyValueMutant> children_ = new ArrayList<KeyValueMutant>();
  
  public IdeMutant() {}
  
  public IdeMutant(I_Ide other) {
    name_ = other.getName();
    setChildren(other.getChildren());
  }
  @Override
  public I_KeyValue get(int child) {
    return children_.get(child);
  }
  
  @Override
  public List<I_KeyValue> getChildren() {
    return new ArrayList<I_KeyValue>(children_);
  }
  
  public String getName() {
    return name_;
  }
  
  public void setChildren(List<I_KeyValue> args) {
    children_.clear();
    if (args != null) {
      for (I_KeyValue kv: args) {
        if (kv instanceof KeyValueMutant) {
          children_.add((KeyValueMutant) kv);
        } else {
          children_.add(new KeyValueMutant(kv));
        }
      }
    }
  }
  
  public void setName(String name) {
    this.name_ = name;
  }
  
  

  @Override
  public int size() {
    return children_.size();
  }
  
}
