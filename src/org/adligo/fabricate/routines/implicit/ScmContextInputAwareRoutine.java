package org.adligo.fabricate.routines.implicit;

import org.adligo.fabricate.common.system.I_GitCalls;
import org.adligo.fabricate.models.common.FabricationMemoryConstants;
import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.common.I_ExecutionEnvironment;
import org.adligo.fabricate.models.common.I_FabricationMemory;
import org.adligo.fabricate.models.common.I_FabricationMemoryMutant;
import org.adligo.fabricate.models.common.I_RoutineMemory;
import org.adligo.fabricate.models.common.I_RoutineMemoryMutant;
import org.adligo.fabricate.models.fabricate.I_Fabricate;
import org.adligo.fabricate.models.project.I_ProjectBrief;
import org.adligo.fabricate.routines.AbstractRoutine;
import org.adligo.fabricate.routines.I_FabricateAware;
import org.adligo.fabricate.routines.I_InputAware;
import org.adligo.fabricate.routines.I_ProjectBriefAware;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ScmContextInputAwareRoutine  extends AbstractRoutine 
implements I_ProjectBriefAware, I_InputAware<ScmContext>, I_FabricateAware {
  private static final List<Class<?>> INPUT_AWARE_CLASS_TYPES = getInputAwareClassTypes();
  protected I_ExecutionEnvironment env_;
  
  private static final List<Class<?>>  getInputAwareClassTypes() {
    List<Class<?>> classTypes = new ArrayList<Class<?>>();
    classTypes.add(ScmContext.class);
    return Collections.unmodifiableList(classTypes);
  }
  
  protected I_ProjectBrief brief_;
  protected ScmContext context_;
  protected I_Fabricate fabricate_;
  protected I_GitCalls gitCalls_;
  
  @Override
  public I_ProjectBrief getProjectBrief() {
    return brief_;
  }
  
  @Override
  public void setProjectBrief(I_ProjectBrief brief) {
    brief_ = brief;
  }
  
  @Override
  public List<Class<?>> getClassType(Class<?> interfaceClass) {
    if (I_InputAware.class.getName().equals(interfaceClass.getName())) {
      return INPUT_AWARE_CLASS_TYPES;
    }
    return null;
  }
  
  @Override
  public void setInput(ScmContext input) {
    context_ = input;
  }

  @Override
  public I_Fabricate getFabricate() {
    return fabricate_;
  }

  @Override
  public void setFabricate(I_Fabricate fab) {
    fabricate_ = fab;
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean setup(I_FabricationMemoryMutant<Object> memory, I_RoutineMemoryMutant<Object> routineMemory)
      throws FabricationRoutineCreationException {
    setupGitCalls();
    env_ = (I_ExecutionEnvironment) memory.get(FabricationMemoryConstants.ENV);
    
    return super.setup(memory, routineMemory);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void setup(I_FabricationMemory<Object> memory, I_RoutineMemory<Object> routineMemory)
      throws FabricationRoutineCreationException {
    setupGitCalls();
    env_ = (I_ExecutionEnvironment) memory.get(FabricationMemoryConstants.ENV);
    
    super.setup(memory, routineMemory);
  }

  
  public void setupGitCalls() {
    gitCalls_ = system_.newGitCalls();
    String hostName = context_.getHostname();
    gitCalls_.setHostname(hostName);
    String protocol = context_.getProtocol();
    gitCalls_.setProtocol(protocol);
    String path = context_.getPath();
    gitCalls_.setRemotePath(path);
    String userName = context_.getUsername();
    gitCalls_.setUser(userName);
  }
}
