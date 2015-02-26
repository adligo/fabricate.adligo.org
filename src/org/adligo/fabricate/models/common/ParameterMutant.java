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
        sb.append(currentIndent);
        sb.append("]");
      }
    } else {
      sb.append("]");
    }
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

  @Override
  public int hashCode() {
    return hashCode(this);
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

  public String toString() {
    return toString(this);
  }

}
