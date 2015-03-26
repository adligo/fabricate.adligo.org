package org.adligo.fabricate.models.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @see I_AttributesOverlay
 * @author scott
 *
 */
public class AttributesOverlay implements I_AttributesOverlay {

  /**
   * A thread safe method to find the first parameter by name.
   * @param key
   * @param attributes
   * @return
   */
  public static I_Parameter getAttribute(String key, List<I_Parameter> attributes) {
    for (I_Parameter attrib: attributes) {
      if (attrib != null) {
        if (key.equals(attrib.getKey())) {
          return attrib;
        }
      }
    }
    return null;
  }
  
  /**
   * A thread safe method to find a list of parameters by name.
   * @param key
   * @param attributes
   * @return
   */
  public static List<I_Parameter> getAttributes(String key, List<I_Parameter> attributes) {
    ArrayList<I_Parameter> toRet = new ArrayList<I_Parameter>();
    for (I_Parameter attrib: attributes) {
      if (attrib != null) {
        if (key.equals(attrib.getKey())) {
          toRet.add(attrib);
        }
      }
    }
    return toRet;
  }
  
  /**
   * A thread safe method to find a list of parameters by name.
   * @param key
   * @param attributes
   * @return
   */
  public static List<I_Parameter> getAttributes(String key, String value, List<I_Parameter> attributes) {
    ArrayList<I_Parameter> toRet = new ArrayList<I_Parameter>();
    for (I_Parameter attrib: attributes) {
      if (attrib != null) {
        if (key.equals(attrib.getKey())) {
          if (value == null) {
            if (null == attrib.getValue()) {
              toRet.add(attrib);
            }
          } else {
            if (value.equals(attrib.getValue())) {
              toRet.add(attrib);
            }
          }
        }
        
      }
    }
    return toRet;
  }
  /**
   * A thread safe method to find the first parameter by name.
   * @param key
   * @param attributes
   * @return the attribute value, which may be null.
   */
  public static String getAttributeValue(String key, List<I_Parameter> attributes) {
    for (I_Parameter attrib: attributes) {
      if (attrib != null) {
        if (key.equals(attrib.getKey())) {
          return attrib.getValue();
        }
      }
    }
    return null;
  }
  
  /**
   * A thread safe method to find a list of parameters by name.
   * @param key
   * @param attributes
   * @return The attribute values, note that null values are not included.
   */
  public static List<String> getAttributeValues(String key, List<I_Parameter> attributes) {
    ArrayList<String> toRet = new ArrayList<String>();
    for (I_Parameter attrib: attributes) {
      if (attrib != null) {
        if (key.equals(attrib.getKey())) {
          String value = attrib.getValue();
          if (value != null) {
            toRet.add(value);
          }
        }
      }
    }
    return toRet;
  }
  
  private final List<I_Parameter> project_;
  private final List<I_Parameter> fabricate_;
  private final List<I_Parameter> merged_;
  
  public AttributesOverlay(I_AttributesContainer fabricate, I_AttributesContainer project) {
    fabricate_ = Parameter.toImmutables(fabricate.getAttributes());
    project_ = Parameter.toImmutables(project.getAttributes());
    merged_ = overlay();
  }
  
  @Override
  public List<I_Parameter> getAttributes() {
    return merged_;
  }

  @Override
  public I_Parameter getAttribute(String key) {
    return getAttribute(key, merged_);
  }

  @Override
  public String getAttributeValue(String key) {
    return getAttributeValue(key, merged_);
  }

  @Override
  public List<I_Parameter> getAttributes(String key) {
    return getAttributes(key, merged_);
  }

  @Override
  public List<I_Parameter> getAttributes(String key, String value) {
    return getAttributes(key, value, getAttributes());
  }
  
  @Override
  public List<String> getAttributeValues(String key) {
    return getAttributeValues(key, merged_);
  }

  @Override
  public List<I_Parameter> getAllAttributes() {
    List<I_Parameter> toRet = new ArrayList<I_Parameter>();
    toRet.addAll(project_);
    toRet.addAll(fabricate_);
    return toRet;
  }

  @Override
  public I_Parameter getAnyAttribute(String key) {
    I_Parameter toRet = getAttribute(key, project_);
    if (toRet != null) {
      return toRet;
    }
    return getAttribute(key, fabricate_);
  }

  @Override
  public String getAnyAttributeValue(String key) {
    I_Parameter ret = getAttribute(key, project_);
    if (ret != null) {
      return ret.getValue();
    }
    ret = getAttribute(key, fabricate_);
    if (ret != null) {
      return ret.getValue();
    }
    return null;
  }

  @Override
  public List<I_Parameter> getAllAttributes(String key) {
    List<I_Parameter> toRet = new ArrayList<I_Parameter>();
    toRet.addAll(getAttributes(key, project_));
    toRet.addAll(getAttributes(key, fabricate_));
    return toRet;
  }

  @Override
  public List<I_Parameter> getAllAttributes(String key, String value) {
    return getAttributes(key, value, getAllAttributes());
  }
  
  @Override
  public List<String> getAllAttributeValues(String key) {
    List<String> toRet = new ArrayList<String>();
    toRet.addAll(getAttributeValues(key, project_));
    toRet.addAll(getAttributeValues(key, fabricate_));
    return toRet;
  }
  
  
  private List<I_Parameter> overlay() {
    List<I_Parameter> ret = new ArrayList<I_Parameter>();
    ParameterMutant.setMutants(ret, fabricate_);
    
    List<I_Parameter> projParams = new ArrayList<I_Parameter>(project_);
    for (I_Parameter mut: ret) {
      String key = mut.getKey();
      Iterator<I_Parameter> pit = projParams.iterator();
      while (pit.hasNext()) {
        I_Parameter im = pit.next();
        if (key.equals(im.getKey())) {
          overlay((ParameterMutant) mut, im);
          pit.remove();
          break;
        }
      }
    }
    for (I_Parameter proj: projParams) {
      ret.add(proj);
    }
    return Parameter.toImmutables(ret);
  }
  
  private void overlay(ParameterMutant mut, I_Parameter over) {
    mut.setValue(over.getValue());
    
    List<I_Parameter> current = new ArrayList<I_Parameter>(mut.getChildren());
    List<I_Parameter> overlay = new ArrayList<I_Parameter>(over.getChildren());
    
    for (I_Parameter c: current) {
      ParameterMutant cm = (ParameterMutant) c;
      String key = cm.getKey();
      Iterator<I_Parameter> pit = overlay.iterator();
      while (pit.hasNext()) {
        I_Parameter next = pit.next();
        if (key.equals(next.getKey())) {
          overlay(cm, next);
          pit.remove();
        }
      }
    }
    
    List<I_Parameter> remainMuts = new ArrayList<I_Parameter>();
    for (I_Parameter remain: overlay) {
      ParameterMutant.addOrClone(remain, remainMuts);
    }
    for (I_Parameter p: remainMuts) {
      mut.addChild(p);
    }
  }
}
