package org.adligo.fabricate.routines.implicit;

import org.adligo.fabricate.models.common.FabricationMemoryConstants;
import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.common.I_FabricationMemory;
import org.adligo.fabricate.models.common.I_FabricationMemoryMutant;
import org.adligo.fabricate.models.common.I_FabricationRoutine;
import org.adligo.fabricate.models.common.I_RoutineBrief;
import org.adligo.fabricate.models.common.I_RoutineMemory;
import org.adligo.fabricate.models.common.I_RoutineMemoryMutant;
import org.adligo.fabricate.routines.I_InputAware;
import org.adligo.fabricate.routines.I_OutputProducer;
import org.adligo.fabricate.routines.ProjectBriefQueueRoutine;

import java.util.List;

public class ScmTrait extends ProjectBriefQueueRoutine {
  /**
   * key to routine memory 
   */
  private static final String SCM_CONTEXT = "scmContext";
  /**
   * key to a optional command line argument which 
   * may contain a encrypted password for continous intergration builds.
   */
  public static final String KEYSTORE_PASSWORD = "gitKeystorePassword";
  private ScmContext context_;
  
  @Override
  public boolean setup(I_FabricationMemoryMutant memory, I_RoutineMemoryMutant routineMemory)
      throws FabricationRoutineCreationException {
    
    I_RoutineBrief scm = fabricate_.getScm();
    String password = (String) memory.get(FabricationMemoryConstants.GIT_KEYSTORE_PASSWORD);
    if (password == null) {
      List<String> keystorePassEncrypted = scm.getParameters(ScmContext.KEYSTORE_PASSWORD);
      if (keystorePassEncrypted == null || keystorePassEncrypted.size() == 0) {
        //check command line args
        String value = system_.getArgValue(KEYSTORE_PASSWORD);
        if (value != null) {
          password = decryptPassword(memory, value);
        } 
      } else {
        String keystorePassEncryptedValue = keystorePassEncrypted.get(0);
        password = decryptPassword(memory, keystorePassEncryptedValue);
      }
    }
    if (password == null) {
      //allow the user to enter it on the command line
      String message = sysMessages_.getPleaseEnterYourGitKeystorePassword();
      password = system_.doDialog(message, true);
      memory.put(FabricationMemoryConstants.GIT_KEYSTORE_PASSWORD, password);
    }
    context_ = new ScmContext(scm, password);
    routineMemory.put(SCM_CONTEXT, context_);
    return super.setup(memory, routineMemory);
  }

  @SuppressWarnings("unchecked")
  public String decryptPassword(I_FabricationMemoryMutant memory, String keystorePassEncryptedValue)
      throws FabricationRoutineCreationException {
    String password;
    I_FabricationRoutine routine = traitFactory_.createRoutine(EncryptTrait.NAME, EncryptTrait.IMPLEMENTED_INTERFACES);
    ((I_InputAware<String>) routine).setInput(keystorePassEncryptedValue);
    routine.run();
    password = ((I_OutputProducer<String>) routine).getOutput();
    memory.put(FabricationMemoryConstants.GIT_KEYSTORE_PASSWORD, password);
    return password;
  }

  @Override
  public void setup(I_FabricationMemory memory, I_RoutineMemory routineMemory)
      throws FabricationRoutineCreationException {
    context_ = (ScmContext) routineMemory.get(SCM_CONTEXT);
    super.setup(memory, routineMemory);
  }

  @Override
  public void setupTask(I_FabricationRoutine taskRoutine) {
    super.setupTask(taskRoutine);
  }
  
  

  
}
