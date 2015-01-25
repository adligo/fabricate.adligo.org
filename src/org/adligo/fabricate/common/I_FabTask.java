package org.adligo.fabricate.common;

import org.adligo.fabricate.models.common.I_KeyValue;
import org.adligo.fabricate.models.project.I_Project;

import java.io.IOException;
import java.util.Map;

/**
 * Tasks are intended to be reusable (classes) in multiple stage
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
  /**
   * @deprecated
   * Many tasks in a stage will be concurrently executing this method
   * on their local instance implementations of this class.
   * 
   * @param ctx
   * @param project
   * @param params
   */
  public void setup(I_RunContext ctx, NamedProject project, Map<String,String> params);
  
  /**
   * 
   * @param ctx
   * @param stageCtx may be null if the task is running from a command.
   *   They are the merged params from the fabricate.xml and project.xml files.
   */
  public void setup(I_RunContext ctx, I_StageContext stageCtx, I_Project project);
  
  /**
   * 
   * @param taskParams the merged params from the fabricate.xml/stage/task/params
   * and project.xml/stage/task/params or or 
   * fabricate.xml/command/params and project.xml/command/params.
   * @throws IOException
   */
  public void execute(I_KeyValue taskParams) throws IOException;
}
