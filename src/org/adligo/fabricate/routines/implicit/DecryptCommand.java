package org.adligo.fabricate.routines.implicit;

import org.adligo.fabricate.models.common.I_FabricationRoutine;
import org.adligo.fabricate.routines.FabricationRoutineCreationException;
import org.adligo.fabricate.routines.I_InputAware;
import org.adligo.fabricate.routines.I_PresentationAware;
import org.adligo.fabricate.routines.I_ResultFactory;

/**
 *       
 * @author scott
 *
 */
public class DecryptCommand extends AbstractRoutine implements 
  I_FabricationRoutine, I_PresentationAware {
  /**
   * This is the implicit name of this command.
   */
  public static final String NAME = "decrypt";
  /**
   * This is a optional command line argument.
   * When it is present the encrypt command
   * encrypts the data instead of asking the
   * user on the command line.
   */
  public static final String INPUT_CLA = "decryptInput";
  
  I_FabricationRoutine decryptTrait_;

  @SuppressWarnings("unchecked")
  @Override
  public void setup() throws FabricationRoutineCreationException {
    decryptTrait_ = traitFactory_.createRoutine(DecryptTrait.NAME, EncryptTrait.IMPLEMENTED_INTERFACES);
    
    String data =  system_.getArgValue(INPUT_CLA);
    if (data == null) {
      data = system_.doDialog(implicit_.getPleaseEnterTheDataToDecrypt(), false);
    }
    ((I_InputAware<String>) decryptTrait_).setInput(data);
    decryptTrait_.run();
    String output = ((I_ResultFactory<String>) decryptTrait_).getResult();
    log_.println(implicit_.getTheFollowingLineContainsTheDecryptedResult());
    log_.println(output);
  }


}
