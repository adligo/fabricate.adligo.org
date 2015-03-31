package org.adligo.fabricate.routines;

import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.i18n.I_SystemMessages;
import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.common.I_ExpectedRoutineInterface;
import org.adligo.fabricate.models.common.I_FabricationRoutine;
import org.adligo.fabricate.models.common.I_Parameter;
import org.adligo.fabricate.models.common.I_RoutineBrief;
import org.adligo.fabricate.models.common.I_RoutineFactory;
import org.adligo.fabricate.models.common.RoutineBrief;
import org.adligo.fabricate.models.common.RoutineBriefMutant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class contains the setting overlay logic as follows;
 * Implicit routines are overlaid (replaced/amended) by fabricate.xml routines.
 * Note there is no way to replace a parameter or nested routine with nothing,
 * parameters and nested routines may only be replaced by new values.
 *    
 * @author scott
 *
 */
public class RoutineFactory implements I_RoutineFactory {
  private final I_FabSystem system_;
  private Map<String,RoutineBrief> briefs_ = new HashMap<String,RoutineBrief>();
  
  public RoutineFactory(I_FabSystem system) {
    system_ = system;
  }
  
  public RoutineFactory(I_FabSystem system, RoutineFactory other) {
    system_ = system;
    briefs_.putAll(other.briefs_);
  }
  
  /**
   * The overlay logic is as follows;
   * If the routine getting added has the same name
   * but a different class, the routine replaces the one
   * in this instance.  Otherwise the tasks and parameters 
   * are overlaid.  When a task overlays another task,
   * it follows the same logic as this method.
   *    When parameters are overlaid if a key
   * is found in the current parameters it's
   * value replaces the current value,  If a key
   * is not found the the key and value are added.
   * 
   * @param routine
   */
  public void add(I_RoutineBrief routine) {
    String name = routine.getName();
    RoutineBrief current = briefs_.get(name);
    if (current == null) {
      briefs_.put(name, new RoutineBrief(routine));
    } else {
      RoutineBrief overlay = createOverlay(current, routine);
      briefs_.put(name, overlay);
    }
  }
  
  public RoutineBrief createOverlay(I_RoutineBrief current, I_RoutineBrief overlay) {
    RoutineBriefMutant currentMutant = new RoutineBriefMutant(current);
    Class<? extends I_FabricationRoutine> clazz = current.getClazz();
    Class<? extends I_FabricationRoutine> newClazz = overlay.getClazz();
    if (clazz == null) {
      currentMutant.setClazz(newClazz);
    } else if (newClazz != null) {
      if ( !clazz.getName().equals(newClazz.getName())) {
        currentMutant.setClazz(newClazz);
      }
    }
    List<I_Parameter> params = overlay.getParameters();
    for (I_Parameter param: params) {
      if (param != null) {
        String key = param.getKey();
        if (current.hasParameter(key)) {
          currentMutant.removeParameters(key);
        } 
      }
    }
    
    for (I_Parameter param: params) {
      if (param != null) {
        currentMutant.addParameter(param);
      }
    }
    List<I_RoutineBrief> nested = overlay.getNestedRoutines();
    for (I_RoutineBrief nest: nested) {
      if (nest != null) {
        String name = nest.getName();
        RoutineBrief currentNest = (RoutineBrief) current.getNestedRoutine(name);
        if (currentNest == null) {
          currentMutant.addNestedRoutine(nest);
        } else {
          currentMutant.removeNestedRoutine(name);
          RoutineBrief nestedOverlay = createOverlay(currentNest, nest);
          currentMutant.addNestedRoutine(nestedOverlay);
        }
      }
    }
    return new RoutineBrief(currentMutant);
  }
  
  public I_FabricationRoutine createRoutine(String name,
      Set<I_ExpectedRoutineInterface> interfaces) throws FabricationRoutineCreationException {
    I_RoutineBrief brief = briefs_.get(name);
    if (brief == null) {
      I_FabricateConstants constants_ = system_.getConstants();
      I_SystemMessages sysMes = constants_.getSystemMessages();
      String message = sysMes.getNoRoutineFoundWithNameX();
      if (name == null) {
        name = "null";
      }
      message = message.replaceAll("<X/>", name);
      throw new IllegalArgumentException(message);
    }
    Class<? extends I_FabricationRoutine> clazz = brief.getClazz();
    if (clazz == null) {
      throw new IllegalArgumentException(name);
    }
    String thread = system_.currentThreadName();
    if ( !"main".equals(thread)) {
      I_FabricateConstants constants = system_.getConstants();
      I_SystemMessages sysMessges =  constants.getSystemMessages();
      throw new IllegalStateException(sysMessges.getThisMethodMustBeCalledFromTheMainThread());
    }
    try {
      I_FabricationRoutine instance = clazz.newInstance();
      Iterator<I_ExpectedRoutineInterface> iface = interfaces.iterator();
      while (iface.hasNext()) {
        I_ExpectedRoutineInterface eri = iface.next();
        Class<?> iclass = eri.getInterfaceClass();
        if ( !iclass.isAssignableFrom(instance.getClass())) {
          FabricationRoutineCreationException ie = new FabricationRoutineCreationException();
          ie.setRoutine(clazz);
          ie.setExpectedInterface(iclass);
          throw ie;
        }
        List<Class<?>> gtypes = eri.getGenericTypes();
        if (gtypes != null && gtypes.size() >= 1) {
          if ( !I_GenericTypeAware.class.isAssignableFrom(instance.getClass())) {
            FabricationRoutineCreationException ie = new FabricationRoutineCreationException();
            ie.setRoutine(clazz);
            ie.setExpectedInterface(I_GenericTypeAware.class);
            throw ie;
          }
          I_GenericTypeAware gta = (I_GenericTypeAware) instance;
          
          List<Class<?>> iclassTypes = gta.getClassType(iclass);
          if (iclassTypes == null) {
            FabricationRoutineCreationException ie = new FabricationRoutineCreationException();
            ie.setRoutine(clazz);
            ie.setExpectedInterface(iclass);
            ie.setExpectedGenericType(gtypes.get(0));
            throw ie;
          }
          Iterator<Class<?>> gtypesIt = gtypes.iterator();
          Iterator<Class<?>> iclassIt = iclassTypes.iterator();
          int counter = 0;
          while (gtypesIt.hasNext()) {
            Class<?> gt = gtypesIt.next();
            Class<?> it = iclassIt.next();
            if (!gt.getName().equals(it.getName())) {
              FabricationRoutineCreationException ie = new FabricationRoutineCreationException();
              ie.setRoutine(clazz);
              ie.setExpectedInterface(iclass);
              ie.setExpectedGenericType(gt);
              ie.setActualGenericType(it);
              ie.setWhichGenericType(counter);
              throw ie;
            }
            counter++;
          }
        }
      }
      instance.setSystem(system_);
      instance.setBrief(brief);
      return instance;
    } catch (InstantiationException | IllegalAccessException e) {
      FabricationRoutineCreationException ie = new FabricationRoutineCreationException();
      ie.setRoutine(clazz);
      ie.initCause(e);
      throw ie;
    }
    
  }
  
  @Override
  public I_RoutineFactory createTaskFactory(String name) {
    I_RoutineBrief brief = briefs_.get(name);
    if (brief == null) {
      return null;
    }
    List<I_RoutineBrief> nested =  brief.getNestedRoutines();
    RoutineFactory toRet = new RoutineFactory(system_);
    for (I_RoutineBrief nest: nested) {
      toRet.add(nest);
    }
    return toRet;
  }
  
  public I_RoutineBrief get(String name) {
    return briefs_.get(name);
  }
  
  public List<I_RoutineBrief> getValues() {
    return new ArrayList<I_RoutineBrief>(briefs_.values());
  }
  
}
