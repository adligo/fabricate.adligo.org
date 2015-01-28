package org.adligo.fabricate.models.dependencies;

import org.adligo.fabricate.models.common.I_Parameter;
import org.adligo.fabricate.models.common.Parameter;
import org.adligo.fabricate.models.common.ParameterMutant;
import org.adligo.fabricate.xml.io_v1.common_v1_0.ParamsType;
import org.adligo.fabricate.xml.io_v1.library_v1_0.IdeType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Ide implements I_Ide {
  public static List<I_Ide> convert(List<IdeType> ps) {
    List<I_Ide> toRet = new ArrayList<I_Ide>();
    if (ps != null) {
      for (IdeType p: ps) {
        if (p != null) {
          toRet.add(new Ide(p));
        }
      }
    }
    return toRet;
  }
  
  private String name_;
  private List<I_Parameter> children_;
  
  public Ide() {}
  
  public Ide(I_Ide other) {
    name_ = other.getName();
    setChildren(other.getChildren());
  }
  
  public Ide(IdeType other) {
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
    return children_;
  }
  
  public String getName() {
    return name_;
  }
  
  private void setChildren(List<I_Parameter> args) {
    List<I_Parameter> toAdd = new ArrayList<I_Parameter>();
    if (args != null) {
      for (I_Parameter kv: args) {
        if (kv instanceof Parameter) {
          toAdd.add((Parameter) kv);
        } else {
          toAdd.add(new Parameter(kv));
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
