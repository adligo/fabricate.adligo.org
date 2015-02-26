package org.adligo.fabricate.routines.implicit;

import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.common.I_FabricationMemoryMutant;
import org.adligo.fabricate.models.common.I_FabricationRoutine;
import org.adligo.fabricate.routines.AbstractRoutine;
import org.adligo.fabricate.routines.I_InputAware;
import org.adligo.fabricate.routines.I_PresentationAware;
import org.adligo.fabricate.routines.I_OutputProducer;

/**
 *       
 * @author scott
 *
 */
public class EncryptCommand extends AbstractRoutine implements 
  I_FabricationRoutine, I_PresentationAware {
  /**
   * This is the implicit name of this command.
   */
  public static final String NAME = "encrypt";
  /**
   * This is a optional command line argument.
   * When it is present the encrypt command
   * encrypts the data instead of asking the
   * user on the command line.
   */
  public static final String INPUT_CLA = "encryptInput";
  
  I_FabricationRoutine encryptTrait_;

  @SuppressWarnings("unchecked")
  @Override
  public boolean setup(I_FabricationMemoryMutant memory) throws FabricationRoutineCreationException {
    encryptTrait_ = traitFactory_.createRoutine(EncryptTrait.NAME, EncryptTrait.IMPLEMENTED_INTERFACES);
    
    String data =  system_.getArgValue(INPUT_CLA);
    if (data == null) {
      data = system_.doDialog(implicit_.getPleaseEnterTheDataToEncrypt(), true);
    }
    ((I_InputAware<String>) encryptTrait_).setInput(data);
    encryptTrait_.run();
    String output = ((I_OutputProducer<String>) encryptTrait_).getOutput();
    log_.println(implicit_.getTheFollowingLineContainsTheEncryptedResult());
    log_.println(output);
    return true;
  }


}
