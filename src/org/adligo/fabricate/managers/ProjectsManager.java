package org.adligo.fabricate.managers;

import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.models.fabricate.I_Fabricate;
import org.adligo.fabricate.routines.implicit.RoutineFabricateFactory;

public class ProjectsManager {
  private final I_FabSystem system_;
  private final I_Fabricate fabricate_;
  private final RoutineFabricateFactory factory_;
  
  public ProjectsManager(I_FabSystem system,  RoutineFabricateFactory factory) {
    system_ = system;
    factory_ = factory;
    fabricate_ = factory.getFabricate();
  }
}
