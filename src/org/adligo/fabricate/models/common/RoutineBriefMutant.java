package org.adligo.fabricate.models.common;

import org.adligo.fabricate.xml.io_v1.common_v1_0.ParamsType;
import org.adligo.fabricate.xml.io_v1.common_v1_0.RoutineParentType;
import org.adligo.fabricate.xml.io_v1.common_v1_0.RoutineType;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.StageType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This is the description of a routine class,
 * in memory which came from the fabricate.xml file.
 * @author scott
 *
 */
public class RoutineBriefMutant implements I_RoutineBrief {
  private static final String NULL = "null";

  public static Map<String, I_RoutineBrief> convert(List<RoutineParentType> types, 
      RoutineBriefOrigin origin) throws ClassNotFoundException {
    Map<String, I_RoutineBrief> toRet = new HashMap<String, I_RoutineBrief>();
    if (types != null) {
      for (RoutineParentType type: types) {
        if (type != null) {
          toRet.put( type.getName(), new RoutineBriefMutant(type, origin));
        }
      }
    }
    return toRet;
  }

  public static Map<String, I_RoutineBrief> convertStages(List<StageType> types, RoutineBriefOrigin origin) throws ClassNotFoundException {
    Map<String, I_RoutineBrief> toRet = new HashMap<String, I_RoutineBrief>();
    if (types != null) {
      for (StageType type: types) {
        if (type != null) {
          toRet.put(type.getName(), new RoutineBriefMutant(type, origin));
        }
      }
    }
    return toRet;
  }
  /**
   * Note parameters and nested routines must be in
   * identical order to have equal return true.
   * @param me
   * @param obj
   * @return
   */
  public static boolean equals(I_RoutineBrief me, Object obj) {
    if (me == obj) {
      return true;
    }
    if (obj instanceof I_RoutineBrief) {
      I_RoutineBrief other = (I_RoutineBrief) obj;
      String name = me.getName();
      String oName = other.getName();
      if (name == null) {
        if (oName != null) {
          return false;
        }
      } else if (!name.equals(oName)) {
        return false;
      }
      Class<?> clazz = me.getClazz();
      Class<?> oClazz = other.getClazz();
      if (clazz == null) {
        if (oClazz != null) {
          return false;
        }
      } else if (oClazz == null) {
        return false;
      } else if ( !clazz.getName().equals(oClazz.getName())) {
        return false;
      }
      boolean optional = me.isOptional();
      boolean oOptional = other.isOptional();
      if (optional != oOptional) {
        return false;
      }
      RoutineBriefOrigin origin = me.getOrigin();
      RoutineBriefOrigin oOrigin = other.getOrigin();
      if (origin == null) {
        if (oOrigin != null) {
          return false;
        }
      } else if (!origin.equals(oOrigin)) {
        return false;
      }
      List<I_Parameter> params = me.getParameters();
      List<I_Parameter> oParams = other.getParameters();
      if (params == null) {
        if (oParams != null) {
          return false;
        }
      } else if (oParams == null) {
        return false;
      } else if (params.size() != oParams.size()){
        return false;
      } else {
        for (int i = 0; i < params.size(); i++) {
          I_Parameter param = params.get(i);
          I_Parameter oParam = oParams.get(i);
          if (param == null) {
            if (oParam != null) {
              return false;
            }
          } else if ( !param.equals(oParam)) {
            return false;
          }
        }
      }
      
      List<I_RoutineBrief> nests = me.getNestedRoutines();
      List<I_RoutineBrief> oNests = other.getNestedRoutines();
      if (nests == null) {
        if (oNests != null) {
          return false;
        }
      } else if (oNests == null) {
        return false;
      } else if (nests.size() != oNests.size()){
        return false;
      } else {
        for (int i = 0; i < nests.size(); i++) {
          I_RoutineBrief nest = nests.get(i);
          I_RoutineBrief oNest = oNests.get(i);
          if (nest == null) {
            if (oNest != null) {
              return false;
            }
          } else if ( !nest.equals(oNest)) {
            return false;
          }
        }
      }
      return true;
    }
    return false;
  }
  public static int hashCode(I_RoutineBrief in) {
    final int prime = 31;
    int result = 1;
    Class<?> clazz = in.getClazz();
    result = prime * result + ((clazz == null) ? 0 : clazz.hashCode());
    String name = in.getName();
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    boolean optional = in.isOptional();
    result = prime * result + ((optional) ? 1 : 0);
    RoutineBriefOrigin origin = in.getOrigin();
    result = prime * result + ((origin == null) ? 0 : origin.hashCode());
    List<I_Parameter> params = in.getParameters();
    if (params != null) {
      for (I_Parameter param: params) {
        result = prime * result + ((param == null) ? 0 : param.hashCode());
      }
    }
    List<I_RoutineBrief> nests = in.getNestedRoutines();
    if (nests != null) {
      for (I_RoutineBrief nest: nests) {
        result = prime * result + ((nest == null) ? 0 : nest.hashCode());
      }
    }
    return result;
  }
  
  public static boolean hasNested(String key, List<I_RoutineBrief> briefs) {
    if (briefs == null || key == null) {
      return false;
    }
    for (I_RoutineBrief rbm: briefs) {
      if (rbm != null) {
        if (key.equals(rbm.getName())) {
          return true;
        }
      }
    }
    return false;
  }
  
  public static String toString(I_RoutineBrief me) {
    StringBuilder sb = new StringBuilder();
    StringBuilder indent = new StringBuilder();
    toString(me, sb, indent);
    return sb.toString();
  }
  
  public static void toString(I_RoutineBrief me, StringBuilder sb, StringBuilder indent) {
    String currentIndent = indent.toString();
    sb.append(currentIndent);
    sb.append(me.getClass().getSimpleName());
    sb.append(" [name=");
    sb.append(me.getName());
    sb.append(", class=");
    Class<?> clazz = me.getClazz();
    if (clazz == null) {
      sb.append(NULL);
    } else {
      sb.append(clazz.getName());
    }
    
    
    boolean listSubs = false;
    List<I_Parameter> children = me.getParameters();
    if (children.size() >= 1) {
      listSubs = true;
      sb.append(",");
      for (I_Parameter child: children) {
        sb.append(System.lineSeparator());
        indent.append("\t");
        ParameterMutant.toString(child, sb, indent);
        String newIndent = indent.toString();
        indent.delete(0,newIndent.length());
        indent.append(currentIndent);
      }
    } 
    
    List<I_RoutineBrief> nests = me.getNestedRoutines();
    if (nests.size() >= 1) {
      if (!listSubs) {
        sb.append(",");
        listSubs = true;
      }
      for (I_RoutineBrief child: nests) {
        sb.append(System.lineSeparator());
        indent.append("\t");
        toString(child, sb, indent);
        String newIndent = indent.toString();
        indent.delete(0,newIndent.length());
        indent.append(currentIndent);
      }
    } 
    if (!listSubs) {
      sb.append("]");
    } else {
      sb.append(System.lineSeparator());
      sb.append(currentIndent);
      sb.append("]");
    }
  }
  
  private String name_;
  private Class<? extends I_FabricationRoutine> clazz_;
  private RoutineBriefOrigin origin_;
  /**
   * actually only ParameterMutant instances.
   */
  private List<I_Parameter> parameters_;
  /**
   * actually only RoutineBriefMutant instances.
   */
  private List<I_RoutineBrief> nestedRoutines_;
  /**
   * This is the setting from xml, or null
   */
  private Boolean optional_;
  
  public RoutineBriefMutant() {}

  /**
   * 
   * @param name
   * @param className
   * @param origin
   * @throws IllegalArgumentException
   *   Note these are internal exceptions and should never show to the user.
   * @throws IOException with the message the name 
   * of the class that couldn't be loaded.
   */
  @SuppressWarnings("unchecked")
  public RoutineBriefMutant(String name, String className, 
      RoutineBriefOrigin origin) throws IllegalArgumentException, ClassNotFoundException {
    if (name == null) {
      throw new IllegalArgumentException("name");
    }
    name_ = name;
    if (origin == null) {
      throw new IllegalArgumentException("origin");
    }
    origin_ = origin;
    if (className != null) {
      //This uses the system class loader so that
      // it doesn't collide with the instrumented class
      // loader for tests4j_4jacoco.
      ClassLoader cl = ClassLoader.getSystemClassLoader();
      clazz_ = (Class<? extends I_FabricationRoutine>) Class.forName(className, true, cl);
    }
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
  public RoutineBriefMutant(RoutineParentType routine, RoutineBriefOrigin origin)
      throws IllegalArgumentException, ClassNotFoundException {
    this(routine.getName(), routine.getClazz(), origin);
    ParamsType params = routine.getParams();
    if (params != null) {
      List<I_Parameter> ps = ParameterMutant.convert(params);
      if (ps.size() >= 1) {
        setParameters(ps);
      }
    }
    
    List<RoutineType> nested = routine.getTask();
    if (nested != null) {
      if (nested.size() >= 1) {
        addNested(origin, nested);
      }
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
  public RoutineBriefMutant(RoutineType routine, RoutineBriefOrigin origin) 
      throws IllegalArgumentException, ClassNotFoundException {
    this(routine.getName(), routine.getClazz(), origin);
    ParamsType params = routine.getParams();
    List<I_Parameter> ps = ParameterMutant.convert(params);
    setParameters(ps);
  }
  
  public RoutineBriefMutant(StageType stage, RoutineBriefOrigin origin)  
      throws IllegalArgumentException, ClassNotFoundException {
    this(stage.getName(), stage.getClazz(), origin);
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
        switch (origin) {
          case ARCHIVE_STAGE:
            addNested(nested, RoutineBriefOrigin.ARCHIVE_STAGE_TASK);
            break;
          case FABRICATE_ARCHIVE_STAGE:
            addNested(nested, RoutineBriefOrigin.FABRICATE_ARCHIVE_STAGE_TASK);
            break;
          case FABRICATE_STAGE:
            addNested(nested, RoutineBriefOrigin.FABRICATE_STAGE_TASK);
            break;
          case IMPLICIT_ARCHIVE_STAGE:
            addNested(nested, RoutineBriefOrigin.IMPLICIT_ARCHIVE_STAGE_TASK);
            break;
          case IMPLICIT_STAGE:
            addNested(nested, RoutineBriefOrigin.IMPLICIT_STAGE_TASK);
            break;
          case PROJECT_STAGE:
            addNested(nested, RoutineBriefOrigin.PROJECT_STAGE_TASK);
            break;
          case STAGE:
          default:
            addNested(nested, RoutineBriefOrigin.STAGE_TASK);
        }
      }
    }
  }
 
  public void addNestedRoutine(I_RoutineBrief nested) {
    String name = nested.getName();
    if (hasNested(name, nestedRoutines_)) {
      DuplicateRoutineException dre = new DuplicateRoutineException();
      dre.setName(name);
      dre.setOrigin(nested.getOrigin());
      dre.setParentName(name_);
      dre.setParentOrigin(origin_);
      throw dre;
    }
    if (nestedRoutines_ == null) {
      nestedRoutines_ = new ArrayList<I_RoutineBrief>();
    }
    
    if (nested instanceof RoutineBriefMutant) {
      nestedRoutines_.add((RoutineBriefMutant) nested);
    } else {
      //should throw a npe for null input
      nestedRoutines_.add(new RoutineBriefMutant(nested));
    }
  }
  
  public void addParameter(I_Parameter param) {
    if (parameters_ == null) {
      parameters_ = new ArrayList<I_Parameter>();
    }
    if (param instanceof ParameterMutant) {
      parameters_.add((ParameterMutant) param);
    } else {
      //should throw a npe for null input
      parameters_.add(new ParameterMutant(param));
    }
  }

  @Override
  public boolean equals(Object obj) {
    return equals(this, obj);
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
  
  
  @Override
  public boolean hasParameter(String key) {
    if (parameters_ == null) {
      return false;
    }
    for (I_Parameter rbm: parameters_) {
      if (rbm != null) {
        if (key.equals(rbm.getKey())) {
          return true;
        }
      }
    }
    return false;
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

  public boolean removeNestedRoutine(String name) {
    if (nestedRoutines_ == null) {
      return false;
    }
    Iterator<I_RoutineBrief> nestedIt = nestedRoutines_.iterator();
    while(nestedIt.hasNext()) {
      I_RoutineBrief next = nestedIt.next();
      if (name.equals(next.getName())) {
        nestedIt.remove();
        return true;
      }
    }
    return false;
  }
  
  public void removeParameters(String key) {
    if (parameters_ == null) {
      return;
    }
    Iterator<I_Parameter> it = parameters_.iterator();
    while (it.hasNext()) {
      I_Parameter pm = it.next();
      if (key.equals(pm.getKey())) {
        it.remove();
      }
    }
  }
  
  public void setClazz(Class<? extends I_FabricationRoutine> clazz) {
    this.clazz_ = clazz;
  }
  
  public void setName(String name) {
    this.name_ = name;
  }
  
  public void setOptional(Boolean optional) {
    this.optional_ = optional;
  }
  public void setOrigin(RoutineBriefOrigin origin) {
    this.origin_ = origin;
  }
  public void setNestedRoutines(List<I_RoutineBrief> nestedRoutines) {
    if (nestedRoutines_ != null) {
      nestedRoutines_.clear();
    }
    if (nestedRoutines != null) {
      for (I_RoutineBrief brief: nestedRoutines) {
        addNestedRoutine(brief);
      }
    }
  }
  public void setParameters(Collection<I_Parameter> parameters) {
    if (parameters_ != null) {parameters_.clear();
    }
    if (parameters != null) {
      for (I_Parameter param: parameters) {
        addParameter(param);
      }
    }
  }
  

  private void addNested(RoutineBriefOrigin origin, List<RoutineType> nested)
      throws ClassNotFoundException {
    switch (origin) {
      case ARCHIVE_STAGE:
        addNested(nested, RoutineBriefOrigin.ARCHIVE_STAGE_TASK);
        break;
      case COMMAND:
        addNested(nested, RoutineBriefOrigin.COMMAND_TASK);
        break;
      case FABRICATE_ARCHIVE_STAGE:
        addNested(nested, RoutineBriefOrigin.FABRICATE_ARCHIVE_STAGE_TASK);
        break;
      case FABRICATE_COMMAND:
        addNested(nested, RoutineBriefOrigin.FABRICATE_COMMAND_TASK);
        break;
      case FABRICATE_FACET:
        addNested(nested, RoutineBriefOrigin.FABRICATE_FACET_TASK);
        break;
      case FABRICATE_STAGE:
        addNested(nested, RoutineBriefOrigin.FABRICATE_STAGE_TASK);
        break;
      case FABRICATE_TRAIT:
        addNested(nested, RoutineBriefOrigin.FABRICATE_TRAIT_TASK);
        break;
      case FACET:
        addNested(nested, RoutineBriefOrigin.FACET_TASK);
        break;
      case IMPLICIT_ARCHIVE_STAGE:
        addNested(nested, RoutineBriefOrigin.IMPLICIT_ARCHIVE_STAGE_TASK);
        break;
      case IMPLICIT_COMMAND:
        addNested(nested, RoutineBriefOrigin.IMPLICIT_COMMAND_TASK);
        break;
      case IMPLICIT_FACET:
        addNested(nested, RoutineBriefOrigin.IMPLICIT_FACET_TASK);
        break;
      case IMPLICIT_STAGE:
        addNested(nested, RoutineBriefOrigin.IMPLICIT_STAGE_TASK);
        break;
      case IMPLICIT_TRAIT:
        addNested(nested, RoutineBriefOrigin.IMPLICIT_TRAIT_TASK);
        break;
      case PROJECT_COMMAND:
        addNested(nested, RoutineBriefOrigin.PROJECT_COMMAND_TASK);
        break;
      case PROJECT_STAGE:
        addNested(nested, RoutineBriefOrigin.PROJECT_STAGE_TASK);
        break;
      case PROJECT_TRAIT:
        addNested(nested, RoutineBriefOrigin.PROJECT_TRAIT_TASK);
        break;
      case STAGE:
        addNested(nested, RoutineBriefOrigin.STAGE_TASK);
        break;
      case TRAIT:
        addNested(nested, RoutineBriefOrigin.TRAIT_TASK);
        break;
      default:
        addNested(nested, RoutineBriefOrigin.TRAIT_TASK);
    }
  }
  
  private void addNested(List<? extends RoutineType> nested, RoutineBriefOrigin origin) 
    throws IllegalArgumentException, ClassNotFoundException {
    if (nested != null) {
      List<I_RoutineBrief> nestedMuts = new ArrayList<I_RoutineBrief>();
      
      for (RoutineType type: nested) {
        RoutineBriefMutant mut = new RoutineBriefMutant(type, origin);
        String name = mut.getName();
        if (hasNested(name, nestedMuts)) {
          DuplicateRoutineException dre = new DuplicateRoutineException();
          dre.setName(name);
          dre.setOrigin(mut.getOrigin());
          dre.setParentName(name_);
          dre.setParentOrigin(origin_);
          throw dre;
        }
        nestedMuts.add(mut);
      }
      nestedRoutines_ = nestedMuts;
    }
  }

  @Override
  public I_RoutineBrief getNestedRoutine(String name) {
    if (nestedRoutines_ == null) {
      return null;
    }
    for (I_RoutineBrief rbm: nestedRoutines_) {
      if (rbm != null) {
        if (name.equals(rbm.getName())) {
          return rbm;
        }
      }
    }
    return null;
  }

  @Override
  public List<String> getParameters(String key) {
    if (parameters_ == null) {
      return Collections.emptyList();
    }
    List<String> toRet = new ArrayList<String>();
    for (I_Parameter rbm: parameters_) {
      if (rbm != null) {
        if (key.equals(rbm.getKey())) {
          toRet.add(rbm.getValue());
        }
      }
    }
    return toRet;
  }

  @Override
  public String getParameter(String key) {
    if (parameters_ == null) {
      return null;
    }
    for (I_Parameter rbm: parameters_) {
      if (rbm != null) {
        if (key.equals(rbm.getKey())) {
          return rbm.getValue();
        }
      }
    }
    return null;
  }
  
  public ParameterMutant getParameterMutant(String key) {
    if (parameters_ == null) {
      return null;
    }
    for (I_Parameter rbm: parameters_) {
      if (rbm != null) {
        if (key.equals(rbm.getKey())) {
          return (ParameterMutant) rbm;
        }
      }
    }
    return null;
  }

  @Override
  public int hashCode() {
    return hashCode(this);
  }

  @Override
  public String toString() {
    return toString(this);
  }

}
