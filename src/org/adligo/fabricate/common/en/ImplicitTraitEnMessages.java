package org.adligo.fabricate.common.en;

import org.adligo.fabricate.common.i18n.I_ImplicitTraitMessages;


public class ImplicitTraitEnMessages implements I_ImplicitTraitMessages {
 
  private static final String THE_FOLLOWING_LINE_CONTAINS_THE_DECRYPTED_RESULT = "The following line contains the decrypted result;";
  private static final String THE_FOLLOWING_LINE_CONTAINS_THE_ENCRYPTED_RESULT = "The following line contains the encrypted result;";
  private static final String PLEASE_ENTER_THE_DATA_TO_ENCRYPT = "Please enter the data to encrypt.";
  private static final String PLEASE_ENTER_THE_DATA_TO_DECRYPT = "Please enter the data to decrypt.";
  public static final I_ImplicitTraitMessages INSTANCE = new ImplicitTraitEnMessages();
    
  private ImplicitTraitEnMessages() {}

  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.en.I_InherentTraitMessages#getPleaseEnterTheDataToDecrypt()
   */
  @Override
  public String getPleaseEnterTheDataToDecrypt() {
    return PLEASE_ENTER_THE_DATA_TO_DECRYPT;
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.en.I_InherentTraitMessages#getPleaseEnterTheDataToEncrypt()
   */
  @Override
  public String getPleaseEnterTheDataToEncrypt() {
    return PLEASE_ENTER_THE_DATA_TO_ENCRYPT;
  }

  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.en.I_InherentTraitMessages#getTheFollowingLineContainsTheEncryptedResult()
   */
  @Override
  public String getTheFollowingLineContainsTheEncryptedResult() {
    return THE_FOLLOWING_LINE_CONTAINS_THE_ENCRYPTED_RESULT;
  }
  
  /* (non-Javadoc)
   * @see org.adligo.fabricate.common.en.I_InherentTraitMessages#getTheFollowingLineContainsTheDecryptedResult()
   */
  @Override
  public String getTheFollowingLineContainsTheDecryptedResult() {
    return THE_FOLLOWING_LINE_CONTAINS_THE_DECRYPTED_RESULT;
  }
}
