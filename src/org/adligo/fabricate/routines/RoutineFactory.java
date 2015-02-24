package org.adligo.fabricate.routines;

import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.common.I_ExpectedRoutineInterface;
import org.adligo.fabricate.models.common.I_FabricationRoutine;
import org.adligo.fabricate.models.common.I_Parameter;
import org.adligo.fabricate.models.common.I_RoutineBrief;
import org.adligo.fabricate.models.common.I_RoutineFactory;
import org.adligo.fabricate.models.common.ParameterMutant;
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
 * Implicit routines are overlaid by fabricate.xml routines,
 * for project specific code (I_ProjectAware) the settings
 * are overlaid with the project.xml routines.
 * 
 *    
 * @author scott
 *
 */
public class RoutineFactory implements I_RoutineFactory {
  private Map<String,RoutineBrief> briefs_ = new HashMap<String,RoutineBrief>();
  
  
  public RoutineFactory() {}
  
  public RoutineFactory(RoutineFactory other) {
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
      Class<?> clazz = routine.getClazz();
      if (clazz == null) {
        RoutineBrief overlay = createOverlay(current, routine);
        //instance comparison
        if (current != overlay) {
          briefs_.put(name, overlay);
        }
      } else {
        Class<?> currentClazz = current.getClazz();
        if ( !clazz.getName().equals(currentClazz.getName())) {
          briefs_.put(name, new RoutineBrief(routine));
        } else {
          RoutineBrief overlay = createOverlay(current, routine);
          //instance comparison
          if (current != overlay) {
            briefs_.put(name, overlay);
          }
        }
      }
    }
  }
  
  public I_FabricationRoutine createRoutine(String name,
      Set<I_ExpectedRoutineInterface> interfaces) throws FabricationRoutineCreationException {
    I_RoutineBrief brief = briefs_.get(name);
    if (brief == null) {
      throw new IllegalArgumentException(name);
    }
    Class<? extends I_FabricationRoutine> clazz = brief.getClazz();
    if (clazz == null) {
      throw new IllegalArgumentException(name);
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
          Iterator<Class<?>> iclassIt = gtypes.iterator();
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
    RoutineFactory toRet = new RoutineFactory();
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
  
  private RoutineBrief createOverlay(RoutineBrief current, I_RoutineBrief overlay) {
    RoutineBriefMutant currentMutant = null;
    List<I_Parameter> params = overlay.getParameters();
    for (I_Parameter param: params) {
      if (param != null) {
        String key = param.getKey();
        String currentVal = current.getParameter(key);
        String overlayVal = param.getValue();
        if (currentVal == null) {
          if (currentMutant == null) {
            currentMutant = new RoutineBriefMutant(current);
          }
          currentMutant.addParameter(param);
        } else if (!currentVal.equals(overlayVal)) {
          if (currentMutant == null) {
            currentMutant = new RoutineBriefMutant(current);
          }
          ParameterMutant pm = currentMutant.getParameterMutant(key);
          pm.setValue(overlayVal);
        }
      }
    }
    
    List<I_RoutineBrief> nested = overlay.getNestedRoutines();
    for (I_RoutineBrief nest: nested) {
      if (nest != null) {
        String name = nest.getName();
        RoutineBrief currentNest = (RoutineBrief) current.getNestedRoutine(name);
        if (currentNest == null) {
          if (currentMutant == null) {
            currentMutant = new RoutineBriefMutant(current);
          }
          currentMutant.addNestedRoutine(nest);
        } else {
          Class<?> currentClazz = current.getClazz();
          Class<?> nestClazz = nest.getClazz();
          if (currentClazz == null) {
            if (currentMutant == null) {
              currentMutant = new RoutineBriefMutant(current);
            }
            currentMutant.removeNestedRoutine(name);
            currentMutant.addNestedRoutine(nest);
          } else if (nestClazz != null){
            if (!currentClazz.getName().equals(nestClazz.getName())) {
              if (currentMutant == null) {
                currentMutant = new RoutineBriefMutant(current);
              }
              currentMutant.removeNestedRoutine(name);
              currentMutant.addNestedRoutine(nest);
            } else {
              if (currentMutant == null) {
                currentMutant = new RoutineBriefMutant(current);
              }
              currentMutant.removeNestedRoutine(name);
              RoutineBrief newBrief = createOverlay(currentNest, nest);
              currentMutant.addNestedRoutine(newBrief);
            }
          } else {
            //current class no no nested class, so parameter replacement
            if (currentMutant == null) {
              currentMutant = new RoutineBriefMutant(current);
            }
            currentMutant.removeNestedRoutine(name);
            RoutineBrief newBrief = createOverlay(currentNest, nest);
            currentMutant.addNestedRoutine(newBrief);
          }
        }
      }
    }
    if (currentMutant != null) {
      return new RoutineBrief(currentMutant);
    }
    return current;
  }


  
}