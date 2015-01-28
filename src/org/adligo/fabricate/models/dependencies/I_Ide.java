package org.adligo.fabricate.models.dependencies;

import org.adligo.fabricate.models.common.I_Parameter;

import java.util.List;

/**
 * This interface represents the IDE (Integrated Development Environment)
 * settings for a dependency, which could change from Eclipse to NetBeans (etc).
 * 
 * @author scott
 *
 */
public interface I_Ide {

  
  public List<I_Parameter> getChildren();
  /**
   * The name of of the IDE (i.e. Eclipse)
   * @return
   */
  public String getName();
  public int size();
  public I_Parameter get(int child);
}
