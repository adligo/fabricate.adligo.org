package org.adligo.fabricate.routines.implicit;

import org.adligo.fabricate.models.project.I_Project;
import org.adligo.fabricate.models.project.I_ProjectBrief;
import org.adligo.fabricate.routines.ProjectBriefQueueRoutine;

import java.util.Collection;
import java.util.List;

/**
 * This command will publish the local
 * depot to another local directory which 
 * resembles a server repository, creating the md5
 * files and moving the jars and wars.  
 * A publish command is generally only done after a 
 * successful build from projects with version identifiers.
 * 
 * @author scott
 *
 */
public class PublishCommand extends ProjectBriefQueueRoutine  {
  public static final String NAME = "publish";

  

}
