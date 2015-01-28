package org.adligo.fabricate.models.dependencies;

import org.adligo.fabricate.models.common.I_Parameter;
import org.adligo.fabricate.models.common.ParameterMutant;
import org.adligo.fabricate.xml.io_v1.common_v1_0.ParamsType;
import org.adligo.fabricate.xml.io_v1.library_v1_0.IdeType;

import java.util.ArrayList;
import java.util.List;

public class IdeMutant implements I_Ide {
  public static List<I_Ide> convert(List<IdeType> ps) {
    List<I_Ide> toRet = new ArrayList<I_Ide>();
    if (ps != null) {
      for (IdeType p: ps) {
        if (p != null) {
          toRet.add(new IdeMutant(p));
        }
      }
    }
    return toRet;
  }
  
  private String name_;
  private List<ParameterMutant> children_ = new ArrayList<ParameterMutant>();
  
  public IdeMutant() {}
  
  public IdeMutant(I_Ide other) {
    name_ = other.getName();
    setChildren(other.getChildren());
  }
  
  public IdeMutant(IdeType other) {
    name_ = other.getName();
    ParamsType args = other.getArgs();
    setChildren(ParameterMutant.convert(args));
  }
  
  @Override
  public I_Parameter get(int child) {
    return children_.get(child);
  }
  
  @Override
  public List<I_Parameter> getChildren() {
    return new ArrayList<I_Parameter>(children_);
  }
  
  public String getName() {
    return name_;
  }
  
  public void setChildren(List<I_Parameter> args) {
    children_.clear();
    if (args != null) {
      for (I_Parameter kv: args) {
        if (kv instanceof ParameterMutant) {
          children_.add((ParameterMutant) kv);
        } else {
          children_.add(new ParameterMutant(kv));
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
