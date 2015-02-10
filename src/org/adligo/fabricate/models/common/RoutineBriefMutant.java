package org.adligo.fabricate.models.common;

import org.adligo.fabricate.common.util.StringUtils;
import org.adligo.fabricate.xml.io_v1.common_v1_0.ParamsType;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.CommandType;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.OptionalRoutineType;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.RoutineType;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.StageType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * This is the description of a routine class,
 * in memory which came from the fabricate.xml file.
 * @author scott
 *
 */
public class RoutineBriefMutant implements I_RoutineBrief {
  private String name_;
  private Class<? extends I_FabricationRoutine> clazz_;
  private RoutineBriefOrigin origin_;
  private List<ParameterMutant> parameters_;
  private List<RoutineBriefMutant> nestedRoutines_;
  /**
   * This is the setting from xml, or null
   */
  private Boolean optional_;
  
  public RoutineBriefMutant() {}
  
  @SuppressWarnings("unchecked")
  public RoutineBriefMutant(String name, String className, RoutineBriefOrigin origin) throws IllegalArgumentException {

    if (StringUtils.isEmpty(name)) {
      throw new IllegalArgumentException("name");
    }
    name_ = name;
    try {
      if (StringUtils.isEmpty(className)) {
        throw new IllegalArgumentException("className");
      }
      //This uses the system class loader so that
      // it doesn't collide with the instrumented class
      // loader for tests4j_4jacoco.
      ClassLoader cl = ClassLoader.getSystemClassLoader();
      clazz_ = (Class<? extends I_FabricationRoutine>) Class.forName(className, true, cl);
    } catch (ClassNotFoundException e) {
      throw new IllegalArgumentException(e.getMessage());
    }
    if (origin == null) {
      throw new IllegalArgumentException("origin");
    }
    origin_ = origin;
  }
  /**
   * 
   * @param brief
   */
  @SuppressWarnings("boxing")
  public RoutineBriefMutant(I_RoutineBrief brief) {
    name_ = brief.getName();
    clazz_ = brief.getClazz();
    origin_ = brief.getOrigin();
    List<I_RoutineBrief> nested = brief.getNestedRoutines();
    if (nested != null) {
      setNestedRoutines(nested);
    }
    optional_ = brief.isOptional();
    origin_ = brief.getOrigin();
    List<I_Parameter> parameters = brief.getParameters();
    if (parameters != null) {
      setParameters(parameters);
    }
  }
  public RoutineBriefMutant(CommandType command)  throws IllegalArgumentException {
    this(command.getName(), command.getClazz(), RoutineBriefOrigin.COMMAND);
    ParamsType params = command.getParams();
    if (params != null) {
      List<I_Parameter> ps = ParameterMutant.convert(params);
      if (ps.size() >= 1) {
        setParameters(ps);
      }
    }
    
    List<RoutineType> nested = command.getTask();
    if (nested != null) {
      if (nested.size() >= 1) {
        addNested(nested, RoutineBriefOrigin.COMMAND_TASK);
      }
    }
  }

  public void addNested(List<? extends RoutineType> nested, RoutineBriefOrigin origin) {
    if (nested != null) {
      List<RoutineBriefMutant> nestedMuts = new ArrayList<RoutineBriefMutant>();
      
      for (RoutineType type: nested) {
        RoutineBriefMutant mut = new RoutineBriefMutant(type, origin);
        nestedMuts.add(mut);
        if (type instanceof OptionalRoutineType) {
          mut.setOptional(((OptionalRoutineType) type).isOptional());
        }
      }
      nestedRoutines_ = nestedMuts;
    }
  }
  /**
   * 
   * @param routine
   * @param origin a STAGE_TASK, or COMMAND_TASK
   *   this constructor doesn't do nestedRoutines.
   * @throws a exception when the class couldn't be located,
   *      or there was some other problem paramter.
   */
  public RoutineBriefMutant(RoutineType routine, RoutineBriefOrigin origin)  throws IllegalArgumentException {
    this(routine.getName(), routine.getClazz(), origin);
    ParamsType params = routine.getParams();
    List<I_Parameter> ps = ParameterMutant.convert(params);
    setParameters(ps);
  }
  public RoutineBriefMutant(StageType stage)  throws IllegalArgumentException {
    this(stage.getName(), stage.getClazz(), RoutineBriefOrigin.STAGE);
    ParamsType params = stage.getParams();
    if (params != null) {
      List<I_Parameter> ps = ParameterMutant.convert(params);
      if (ps.size() >= 1) {
        setParameters(ps);
      }
    }
    optional_ = stage.isOptional();
    
    List<? extends RoutineType> nested = stage.getTask();
    if (nested != null) {
      if (nested.size() >= 1) {
        addNested(nested, RoutineBriefOrigin.STAGE_TASK);
      }
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
    if (optional_ == null) {
      return false;
    }
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
    if (parameters_ == null) {
      return Collections.emptyList();
    }
    return new ArrayList<I_Parameter>(parameters_);
  }
  /* (non-Javadoc)
   * @see org.adligo.fabricate.models.routines.I_RoutineBrief#getNestedRoutines()
   */
  @Override
  public List<I_RoutineBrief> getNestedRoutines() {
    if (nestedRoutines_ == null) {
      return Collections.emptyList();
    }
    return new ArrayList<I_RoutineBrief>(nestedRoutines_);
  }
  public void setName(String name) {
    this.name_ = name;
  }
  public void setClazz(Class<? extends I_FabricationRoutine> clazz) {
    this.clazz_ = clazz;
  }
  public void setOptional(Boolean optional) {
    this.optional_ = optional;
  }
  public void setOrigin(RoutineBriefOrigin origin) {
    this.origin_ = origin;
  }
  public void setNestedRoutines(List<I_RoutineBrief> nestedRoutines) {
    if (nestedRoutines_ == null) {
      nestedRoutines_ = new ArrayList<RoutineBriefMutant>();
    } else {
      nestedRoutines_.clear();
    }
    if (nestedRoutines != null) {
      for (I_RoutineBrief brief: nestedRoutines) {
        if (brief instanceof RoutineBriefMutant) {
          nestedRoutines_.add((RoutineBriefMutant) brief);
        } else {
          nestedRoutines_.add(new RoutineBriefMutant(brief));
        }
      }
    }
  }
  public void setParameters(Collection<I_Parameter> parameters) {
    if (parameters_ == null) {
      parameters_ = new ArrayList<ParameterMutant>();
    } else {
      parameters_.clear();
    }
    if (parameters != null) {
      for (I_Parameter param: parameters) {
        if (param instanceof ParameterMutant) {
          parameters_.add((ParameterMutant) param);
        } else {
          parameters_.add(new ParameterMutant(param));
        }
      }
    }
  }
}
