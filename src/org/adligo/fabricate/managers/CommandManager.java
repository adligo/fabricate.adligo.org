package org.adligo.fabricate.managers;

import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.common.system.I_FailureTransport;
import org.adligo.fabricate.models.common.FabricationMemoryMutant;
import org.adligo.fabricate.routines.I_RoutineBuilder;
import org.adligo.fabricate.routines.I_RoutineExecutor;
import org.adligo.fabricate.routines.I_RoutineFabricateFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CommandManager {
  private final List<String> commands_ = new ArrayList<String>();
  private final I_RoutineFabricateFactory factory_;
  
  private final I_RoutineBuilder routineBuilder_;
  
  public CommandManager(Collection<String> commands, I_FabSystem system, 
      I_RoutineFabricateFactory factory, I_RoutineBuilder routineBuilder) {
    commands_.addAll(commands);
    factory_ = factory;
    routineBuilder_ = routineBuilder;
  }
  
  /**
   * @return FailureType if failure occurs, otherwise null.
   */
  public I_FailureTransport processCommands(FabricationMemoryMutant<Object> memory) {
    
    for (String command: commands_) {
      I_RoutineExecutor executor = factory_.createRoutineExecutor();
      I_FailureTransport result = executor.run(command, routineBuilder_, memory);
      if (result != null) {
        return result;
      }
    }
    return null;
  }

}
