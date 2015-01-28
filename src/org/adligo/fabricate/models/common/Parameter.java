package org.adligo.fabricate.models.common;

import org.adligo.fabricate.xml.io_v1.common_v1_0.ParamType;
import org.adligo.fabricate.xml.io_v1.common_v1_0.ParamsType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Parameter implements I_Parameter {
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
          toRet.add(new Parameter(p));
        }
      }
    }
    return toRet;
  }
  private List<I_Parameter> children_;
  private String key_;
  private String value_;

  public Parameter(I_Parameter other) {
    key_ = other.getKey();
    value_ = other.getValue();
    setChildren(other.getChildren());
  }
  
  public Parameter(ParamType other) {
    key_ = other.getKey();
    value_ = other.getValue();
    setChildren(convert(other.getParam()));
  }
  
  public I_Parameter get(int child) {
    return children_.get(child);
  }
  public List<I_Parameter> getChildren() {
    return children_;
  }
  public String getKey() {
    return key_;
  }
  
  public String getValue() {
    return value_;
  }

  private void setChildren(Collection<I_Parameter> children) {
    List<I_Parameter> toAdd = new ArrayList<I_Parameter>();
    if (children != null) {
      for (I_Parameter child: children) {
        if (child instanceof Parameter) {
          toAdd.add((Parameter) child);
        } else {
          toAdd.add(new Parameter(child));
        }
      }
    }
    children_ = Collections.unmodifiableList(toAdd);
  }
  
  public int size() {
    return children_.size();
  }
}
