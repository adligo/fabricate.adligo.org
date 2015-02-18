package org.adligo.fabricate.models.common;

import org.adligo.fabricate.common.util.StringUtils;
import org.adligo.fabricate.xml.io_v1.common_v1_0.RoutineParentType;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.StageType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoutineBrief implements I_RoutineBrief {
  
  public static Map<String, I_RoutineBrief> convert(List<RoutineParentType> types, 
      RoutineBriefOrigin origin) throws ClassNotFoundException {
    Map<String, I_RoutineBrief> toRet = new HashMap<String, I_RoutineBrief>();
    
    if (types != null) {
      for (RoutineParentType type: types) {
        if (type != null) {
          toRet.put(type.getName(), new RoutineBrief(new RoutineBriefMutant(type, origin)));
        }
      }
    }
    return Collections.unmodifiableMap(toRet);
  }
  
  public static Map<String, I_RoutineBrief> convert(List<StageType> types) throws ClassNotFoundException {
    Map<String, I_RoutineBrief> toRet = new HashMap<String, I_RoutineBrief>();
    
    if (types != null) {
      for (StageType type: types) {
        if (type != null) {
          toRet.put(type.getName(), new RoutineBrief(new RoutineBriefMutant(type)));
        }
      }
    }
    return Collections.unmodifiableMap(toRet);
  }
  
  private final String name_;
  /**
   * Clazz may be null when there is no specified class
   * in the xml node that pertains to this instance.
   */
  private final Class<? extends I_FabricationRoutine> clazz_;
  private final RoutineBriefOrigin origin_;
  private final List<I_Parameter> parameters_;
  private final Map<String, I_Parameter> parametersLookup_;
  private final List<I_RoutineBrief> nestedRoutines_;
  private final Map<String, I_RoutineBrief> nestedRoutinesLookup_;
  /**
   * This is the setting from xml, or null
   */
  private boolean optional_;
  
  /**
   * 
   * @param brief
   */
  @SuppressWarnings("unused")
  public RoutineBrief(I_RoutineBrief brief) throws IllegalArgumentException {
    name_ = brief.getName();
    if (StringUtils.isEmpty(name_)) {
      throw new IllegalArgumentException("name");
    }
    
    clazz_ = brief.getClazz();
    
    origin_ = brief.getOrigin();
    if (origin_ == null) {
      throw new IllegalArgumentException("origin");
    }
    
    List<I_RoutineBrief> nested = brief.getNestedRoutines();
    if (nested != null) {
      nestedRoutines_ = newNestedRoutines(nested);
    } else {
      nestedRoutines_ = Collections.emptyList();
    }
    nestedRoutinesLookup_ = getNestedMap();
    
    optional_ = brief.isOptional();
    List<I_Parameter> parameters = brief.getParameters();
    if (parameters != null) {
      parameters_ = newParameters(parameters);
    } else {
      parameters_ = Collections.emptyList();
    }
    parametersLookup_ = getParametersMap();
    
    
  }
  

  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.routines.I_RoutineBrief#getClazz()
   */
  @Override
  public Class<? extends I_FabricationRoutine> getClazz() {
    return clazz_;
  }
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.routines.I_RoutineBrief#getName()
   */
  @Override
  public String getName() {
    return name_;
  }
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.routines.I_RoutineBrief#isOptional()
   */
  @Override
  public boolean isOptional() {
    return optional_;
  }
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.routines.I_RoutineBrief#getOrigin()
   */
  @Override
  public RoutineBriefOrigin getOrigin() {
    return origin_;
  }
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.routines.I_RoutineBrief#getParameters()
   */
  @Override
  public List<I_Parameter> getParameters() {
   return parameters_;
  }
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.routines.I_RoutineBrief#getNestedRoutines()
   */
  @Override
  public List<I_RoutineBrief> getNestedRoutines() {
    return nestedRoutines_;
  }
  
  private Map<String, I_RoutineBrief> getNestedMap() {
    if (nestedRoutines_.size() == 0) {
      return Collections.emptyMap();
    }
    Map<String,I_RoutineBrief> toRet = new HashMap<String, I_RoutineBrief>();
    for (I_RoutineBrief rou: nestedRoutines_) {
      toRet.put(rou.getName(), rou);
    }
    return Collections.unmodifiableMap(toRet);
  }
  
  private Map<String, I_Parameter> getParametersMap() {
    if (parameters_.size() == 0) {
      return Collections.emptyMap();
    }
    Map<String,I_Parameter> toRet = new HashMap<String, I_Parameter>();
    for (I_Parameter rou: parameters_) {
      toRet.put(rou.getKey(), rou);
    }
    return Collections.unmodifiableMap(toRet);
  }
  
  private List<I_RoutineBrief> newNestedRoutines(List<I_RoutineBrief> nestedRoutines) {
    List<I_RoutineBrief> toRet = new ArrayList<I_RoutineBrief>();
    for (I_RoutineBrief brief: nestedRoutines) {
      if (brief instanceof RoutineBrief) {
        toRet.add((RoutineBrief) brief);
      } else {
        toRet.add(new RoutineBrief(brief));
      }
    }
    return Collections.unmodifiableList(toRet);
  }
  private List<I_Parameter> newParameters(Collection<I_Parameter> parameters) {
    List<I_Parameter> toRet = new ArrayList<I_Parameter>();
    
    for (I_Parameter param: parameters) {
      if (param instanceof Parameter) {
        toRet.add((Parameter) param);
      } else {
        toRet.add(new Parameter(param));
      }
    }
    return Collections.unmodifiableList(toRet);
  }

  @Override
  public I_RoutineBrief getNestedRoutine(String name) {
    return nestedRoutinesLookup_.get(name);
  }

  @Override
  public String getParameter(String key) {
    I_Parameter toRet = parametersLookup_.get(key);
    if (toRet == null) {
      return null;
    }
    return toRet.getValue();
  }
}
