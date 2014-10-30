package org.adligo.fabricate.common;

import org.adligo.fabricate.xml.io.project.FabricateProjectType;

import java.util.Map;

/**
 * Tasks are intended to be reusable in multiple stage
 * classes so custom stages can be built by java developers
 * based on their need to modify fabricate.
 * 
 * Tasks are not intended to be thread safe, 
 * a new one is should be created for each project/stage.
 * 
 * Note tasks should also have some sort of execute method,
 * which could throw any type of exception.
 * 
 * @author scott
 *
 */
public interface I_FabTask {
  public void setup(I_FabContext ctx, NamedProject project, Map<String,String> params);
}
