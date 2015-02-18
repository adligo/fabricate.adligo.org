package org.adligo.fabricate.routines.implicit;

import org.adligo.fabricate.models.common.I_FabricationRoutine;
import org.adligo.fabricate.models.common.I_RoutineBrief;
import org.adligo.fabricate.models.common.RoutineBrief;
import org.adligo.fabricate.models.common.RoutineBriefMutant;
import org.adligo.fabricate.routines.I_DefaultRoutines;

import java.util.HashMap;
import java.util.Map;

public class DefaultRoutines implements I_DefaultRoutines {

  @Override
  public Map<String, I_RoutineBrief> getImplicitTraits() {
    Map<String, I_RoutineBrief> toRet = new HashMap<String, I_RoutineBrief>();
    addBrief(toRet, EncryptTrait.NAME, EncryptTrait.class);
    addBrief(toRet, DecryptTrait.NAME, DecryptTrait.class);
    return toRet;
  }

  public void addBrief(Map<String, I_RoutineBrief> toRet, String name, Class<? extends I_FabricationRoutine> clazz) {
    RoutineBriefMutant rbm = new RoutineBriefMutant();
    rbm.setClazz(clazz);
    rbm.setName(name);
    toRet.put(name, new RoutineBrief(rbm));
  }

  @Override
  public Map<String, I_RoutineBrief> getImplicitCommands() {
    Map<String, I_RoutineBrief> toRet = new HashMap<String, I_RoutineBrief>();
    addBrief(toRet, EncryptCommand.NAME, EncryptCommand.class);
    addBrief(toRet, DecryptCommand.NAME, DecryptCommand.class);
    return toRet;
  }

  @Override
  public Map<String, I_RoutineBrief> getImplicitStages() {
    Map<String, I_RoutineBrief> toRet = new HashMap<String, I_RoutineBrief>();
    return toRet;
  }

}
