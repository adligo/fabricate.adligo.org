package org.adligo.fabricate.models.common;

import org.adligo.fabricate.common.util.StringUtils;
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
  
  /**
   * This method creates a unmodifiable list of Parameter only instances,
   * or a empty list if input is null or empty.
   * @param ps
   * @return
   */
  public static List<I_Parameter> toImmutables(List<I_Parameter> ps) {
    if (ps == null || ps.size() == 0) {
      return Collections.emptyList();
    }
    List<I_Parameter> toRet = new ArrayList<I_Parameter>();
    if (ps != null) {
      for (I_Parameter p: ps) {
        if (p instanceof Parameter) {
          toRet.add(p);
        } else {
          toRet.add(new Parameter(p));
        }
      }
    }
    return Collections.unmodifiableList(toRet);
  }
  
  private static final List<I_Parameter> EMPTY_LIST = Collections.emptyList();
  
  private final List<I_Parameter> children_;
  private final String key_;
  private final String value_;
  private final int hashCode_;

  public Parameter(String key, String value) {
    this(key,value, EMPTY_LIST);
  }
  
  public Parameter(String key, String value, List<I_Parameter> children) {
    key_ = key;
    if (StringUtils.isEmpty(key_)) {
      throw new IllegalArgumentException("key");
    }
    //allow null values
    value_ = value;
    children_ = newChildren(children);
    hashCode_ = ParameterMutant.hashCode(this);
  }
  
  public Parameter(I_Parameter other) {
    key_ = other.getKey();
    if (StringUtils.isEmpty(key_)) {
      throw new IllegalArgumentException("key");
    }
    //allow null values
    value_ = other.getValue();
    children_ = newChildren(other.getChildren());
    hashCode_ = ParameterMutant.hashCode(this);
  }
  
  public Parameter(ParamType other) {
    key_ = other.getKey();
    if (StringUtils.isEmpty(key_)) {
      throw new IllegalArgumentException("key");
    }
    //allow null values
    value_ = other.getValue();
    children_ = newChildren(convert(other.getParam()));
    hashCode_ = ParameterMutant.hashCode(this);
  }
  
  public boolean equals(Object obj) {
    return ParameterMutant.equals(this, obj);
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

  public String [] getValueDelimited(String delimitor) {
    return ParameterMutant.getValueDelimited(value_, delimitor);
  }
  
  private List<I_Parameter> newChildren(Collection<I_Parameter> children) {
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
    return Collections.unmodifiableList(toAdd);
  }
  
  public int hashCode() {
    return hashCode_;
  }
  
  public int size() {
    return children_.size();
  }
  
  public String toString() {
    return ParameterMutant.toString(this);
  }
}
