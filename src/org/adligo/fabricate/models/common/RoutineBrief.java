package org.adligo.fabricate.models.common;

import org.adligo.fabricate.common.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class RoutineBrief implements I_RoutineBrief {
  private final String name_;
  private final Class<? extends I_FabricationRoutine> clazz_;
  private final RoutineBriefOrigin origin_;
  private final List<I_Parameter> parameters_;
  private final List<I_RoutineBrief> nestedRoutines_;
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
    if (clazz_ == null) {
      throw new IllegalArgumentException("clazz");
    }
    
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
    optional_ = brief.isOptional();
    List<I_Parameter> parameters = brief.getParameters();
    if (parameters != null) {
      parameters_ = newParameters(parameters);
    } else {
      parameters_ = Collections.emptyList();
    }
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
}
