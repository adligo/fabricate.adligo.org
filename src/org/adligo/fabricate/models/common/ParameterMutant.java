package org.adligo.fabricate.models.common;

import org.adligo.fabricate.xml.io_v1.common_v1_0.ParamType;
import org.adligo.fabricate.xml.io_v1.common_v1_0.ParamsType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ParameterMutant implements I_Parameter {
  public static List<I_Parameter> convert(ParamsType params) {
    List<ParamType> ps = null;
    if (params != null) {
      ps = params.getParam();
    }
    return convert(ps);
  }

  public static List<I_Parameter> convert(List<ParamType> ps) {
    List<I_Parameter> toRet = new ArrayList<I_Parameter>();
    if (ps != null) {
      for (ParamType p: ps) {
        if (p != null) {
          toRet.add(new ParameterMutant(p));
        }
      }
    }
    return toRet;
  }
  private List<ParameterMutant> children_ = new ArrayList<ParameterMutant>();
  private String key_;
  private String value_;
  
  public ParameterMutant() {}
  
  public ParameterMutant(I_Parameter other) {
    key_ = other.getKey();
    value_ = other.getValue();
    setChildren(other.getChildren());
  }

  public ParameterMutant(ParamType other) {
    key_ = other.getKey();
    value_ = other.getValue();
    setChildren(convert(other.getParam()));
  }
  
  public I_Parameter get(int child) {
    return children_.get(child);
  }
  public List<I_Parameter> getChildren() {
    return new ArrayList<I_Parameter>(children_);
  }
  public String getKey() {
    return key_;
  }
  
  public String getValue() {
    return value_;
  }

  public void setChildren(Collection<I_Parameter> children) {
    children_.clear();
    if (children != null) {
      for (I_Parameter child: children) {
        if (child instanceof ParameterMutant) {
          children_.add((ParameterMutant) child);
        } else {
          children_.add(new ParameterMutant(child));
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
