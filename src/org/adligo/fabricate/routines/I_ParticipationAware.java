package org.adligo.fabricate.routines;

import org.adligo.fabricate.models.common.I_FabricationRoutine;

/**
 * This is a marker interface for {@link I_FabricationRoutine}s
 * which indicates that the project.xml file must have a corresponding
 * node for any of the following in order to execute;<br/>
 * commands<br/>
 * command/tasks<br/>
 * stages<br/>
 * stage/tasks<br/>
 * 
 * @author scott
 *
 */
public interface I_ParticipationAware {

}
