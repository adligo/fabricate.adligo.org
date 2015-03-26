package org.adligo.fabricate.models.common;

import org.adligo.fabricate.common.util.StringUtils;
import org.adligo.fabricate.xml.io_v1.common_v1_0.ParamType;
import org.adligo.fabricate.xml.io_v1.common_v1_0.ParamsType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

/**
 * This class mostly represents a typical non thread safe
 * model which is mutable and represents a key, value
 * and nested or child parameters. 
 * @author scott
 *
 */
public class ParameterMutant implements I_Parameter {
  
  /**
   * This method is a thread safe method to add a ParameterMutant
   * from any I_Parameter implementation, without changing
   * implementations of ParameterMutant.
   * @param params
   * @return
   */
  public static void addOrClone(I_Parameter parameter, Collection<I_Parameter> collection) {
    //don't dedup allow multiple attributes_
    if (parameter instanceof ParameterMutant) {
      collection.add(parameter);
    } else if (parameter != null) {
      collection.add(new ParameterMutant(parameter));
    }
  }
  
  /**
   * This method is a thread safe method to convert parameters.
   * @param params
   * @return
   */
  public static List<I_Parameter> convert(ParamsType params) {
    List<ParamType> ps = null;
    if (params != null) {
      ps = params.getParam();
    }
    return convert(ps);
  }

  /**
   * This method is a thread safe method to convert parameters
   * @param ps
   * @return
   */
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
  
  /**
   * This method is a thread safe method to convert instance into a 
   * list I_Parameter which actually contains ParameterMutant only instances.
   * @param ps
   * @return
   */
  public static void setMutants(Collection<I_Parameter> to, Collection<? extends I_Parameter> from) {
    to.clear();
    if (from != null && from.size() >= 1) {
      for (I_Parameter attribute: from) {
        addOrClone(attribute, to);
      }
    }
  }
  
  /**
   * This method is a thread safe method to extract equals for I_Parameter instances
   * @param me
   * @param obj
   * @return
   */
  public static boolean equals(I_Parameter me, Object obj) {
    if (me == obj)
      return true;
    if (obj == null)
      return false;
    if (obj instanceof I_Parameter) {
      I_Parameter other = (I_Parameter) obj;
      
      String key = me.getKey();
      String oKey = other.getKey();
      if (key == null) {
        if (oKey != null) {
          return false;
        }
      } else if (!key.equals(oKey)) {
        return false;
      }
      String value = me.getValue();
      String oValue = other.getValue();
      if (value == null) {
        if (oValue != null) {
          return false;
        }
      } else if (!value.equals(oValue)) {
        return false;
      }
      
      List<I_Parameter> children = me.getChildren();
      List<I_Parameter> otherChildren = other.getChildren();
      if (children == null) {
        if (otherChildren != null) {
          return false;
        }
      } else if (children.size() != otherChildren.size()) {
        return false;
      } else {
        for (int i = 0; i < children.size(); i++) {
          I_Parameter child = children.get(i);
          I_Parameter otherChild = otherChildren.get(i);
          if (!child.equals(otherChild)) {
            return false;
          }
        }
      }
      return true;
    }
    return false;
  }
  
  /**
   * This method is a thread safe method to parse a string value.
   * @param value
   * @param delimitor
   * @return
   */
  public static String [] getValueDelimited(String value, String delimitor) {
    String [] delimited = null;
    
    if (StringUtils.isEmpty(value)) {
      delimited = new String[1];
      delimited[0] = value;
    } else {
      StringTokenizer st = new StringTokenizer(value, delimitor);
      List<String> tokens = new ArrayList<String>();
      while (st.hasMoreTokens()) {
        tokens.add(st.nextToken());
      }
      delimited = tokens.toArray(new String[tokens.size()]);
    }
    return delimited;
  }
  
  /**
   * This method is a thread safe method to extract hashCode for I_Parameter instance.
   * @param value
   * @param delimitor
   * @return
   */
  public static int hashCode(I_Parameter param) {
    final int prime = 31;
    int result = 1;
    String key = param.getKey();
    result = prime * result + ((key == null) ? 0 : key.hashCode());
    String value = param.getValue();
    result = prime * result + ((value == null) ? 0 : value.hashCode());
    List<I_Parameter> children = param.getChildren();
    if (children != null) {
      for (I_Parameter child: children) {
        result = prime * result + hashCode(child);
      }
    }
    return result;
  }
  
  public static String toString(I_Parameter param) {
    StringBuilder sb = new StringBuilder();
    StringBuilder indent = new StringBuilder();
    toString(param, sb, indent);
    return sb.toString();
  }
  
  public static void toString(I_Parameter param, StringBuilder sb, StringBuilder indent) {
    String currentIndent = indent.toString();
    sb.append(currentIndent);
    sb.append(param.getClass().getSimpleName());
    sb.append(" [key=");
    sb.append(param.getKey());
    sb.append(", value=");
    sb.append(param.getValue());
    
    List<I_Parameter> children = param.getChildren();
    if (children == null) {
      sb.append("]");
    } else if (children.size() >= 1) {
      for (I_Parameter child: children) {
        sb.append(System.lineSeparator());
        indent.append("\t");
        toString(child, sb, indent);
        String newIndent = indent.toString();
        indent.delete(0,newIndent.length());
        indent.append(currentIndent);
        sb.append(System.lineSeparator());
      }
      sb.append(currentIndent);
      sb.append("]");
    } else {
      sb.append("]");
    }
  }
  
  private List<I_Parameter> children_ = new ArrayList<I_Parameter>();
  private String key_;
  private String value_;
  
  public ParameterMutant() {}

  public ParameterMutant(String key, String value) {
    key_ = key;
    value_ = value;
  }
  
  public ParameterMutant(String key, String value, List<I_Parameter> children) {
    key_ = key;
    value_ = value;
    if (children != null && children.size() >= 1) { 
      setChildren(children);
    }
  }
  
  
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
  
  public void addChild(I_Parameter p) {
    addOrClone(p, children_);
  }
  
  @Override
  public boolean equals(Object obj) {
    return equals(this, obj);
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

  public String [] getValueDelimited(String delimitor) {
    return getValueDelimited(value_, delimitor);
  }
  
  @Override
  public int hashCode() {
    return hashCode(this);
  }
  
  public void setChildren(Collection<I_Parameter> children) {
     setMutants(children_, children);
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

  public String toString() {
    return toString(this);
  }

}
