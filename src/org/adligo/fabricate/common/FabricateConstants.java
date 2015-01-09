package org.adligo.fabricate.common;

import org.adligo.fabricate.common.en.FabricateEnConstants;
import org.adligo.fabricate.common.i18n.I_FabricateConstants;

/**
 * To use this class, cache the reference in the class that needs i18n messsages i.e.;
 * public static final I_FabricateConstants CONSTANTS = FabricateConstants.INSTANCE;
 * 
 * Do not cache any references to values returned from this the INSTANCE 
 * get the values in method execution so that the Fabricate system
 * has a chance to swap out the English with another language.
 * 
 * @author scott
 *
 */
public class FabricateConstants {
  public static final I_FabricateConstants INSTANCE = new FabricateConstantsWrapper(FabricateEnConstants.INSTANCE);
}
