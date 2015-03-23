package org.adligo.fabricate.routines.implicit;

import org.adligo.fabricate.common.util.StringUtils;
import org.adligo.fabricate.models.common.ExpectedRoutineInterface;
import org.adligo.fabricate.models.common.I_ExpectedRoutineInterface;
import org.adligo.fabricate.models.common.I_FabricationRoutine;
import org.adligo.fabricate.models.project.I_Project;
import org.adligo.fabricate.routines.AbstractRoutine;
import org.adligo.fabricate.routines.I_InputAware;
import org.adligo.fabricate.routines.I_OutputProducer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *       
 * @author scott
 *
 */
public class NameJar extends AbstractRoutine implements I_FabricationRoutine, I_OutputProducer<String>, 
I_InputAware<I_Project> {
  /**
   * This is the implicit name of this trait.
   */
  public static final String NAME = "name jar";
  public static final Set<I_ExpectedRoutineInterface> IMPLEMENTED_INTERFACES = getInterfaces();
  
  private static Set<I_ExpectedRoutineInterface> getInterfaces() {
    Set<I_ExpectedRoutineInterface> toRet = new HashSet<I_ExpectedRoutineInterface>();
    toRet.add(new ExpectedRoutineInterface(I_InputAware.class, I_Project.class));
    toRet.add(new ExpectedRoutineInterface(I_OutputProducer.class, String.class));
    return Collections.unmodifiableSet(toRet);
  }
  private I_Project project_;
  private String output_;
  
  public void run() {
    String shortName = project_.getShortName();
    if (shortName == null) {
      shortName = project_.getName();
    }
    String version = project_.getVersion();
    /**
     * Maven users can compile the adligo.org code with dashes
     * instead of underscores by changing this implicit trait in the fabricate.xml.
     * This will cause any underscores to be replace by the delimiter as well,
     * so if a version string from a SCM (git, cvs) contains a underscore,
     * it will be replaced by the delimiter.
     */
    String delimiter = brief_.getParameterValue("delimiter");
    if (StringUtils.isEmpty(delimiter)) {
      delimiter = "_";
    }
    if (StringUtils.isEmpty(version)) {
      version = "snapshot";
    }
    String name = shortName + delimiter + version + ".jar";
    if ( !"_".equals(delimiter)) {
      StringBuilder sb = new StringBuilder();
      char [] chars = name.toCharArray();
      for (int i = 0; i < chars.length; i++) {
        char c = chars[i];
        if ('_' == c) {
          sb.append(delimiter);
        } else {
          sb.append(c);
        }
      }
      name = sb.toString();
    }
    output_ = name;
  }
  
  @Override
  public String getOutput() {
    return output_;
  }

  @Override
  public void setInput(I_Project input) {
    project_ = input;
  }

  @Override
  public List<Class<?>> getClassType(Class<?> interfaceClass) {
    if (interfaceClass.getName().equals(I_InputAware.class.getName())) {
      List<Class<?>> toRet = new ArrayList<Class<?>>();
      toRet.add(I_Project.class);
      return toRet;
    }
    if (interfaceClass.getName().equals(I_OutputProducer.class.getName())) {
      List<Class<?>> toRet = new ArrayList<Class<?>>();
      toRet.add(String.class);
      return toRet;
    }
    return null;
  }

}
