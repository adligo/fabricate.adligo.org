package org.adligo.fabricate.routines;

/**
 * This interface allows routines to dialog the user for various information
 * that shouldn't be kept in a file.  This was originally added for 
 * the ssh keystore password for git, so that Fabricate can dialog
 * the user for the password.
 *    
 * Dialogs and user interaction should occur in the setup method.
 * 
 * @author scott
 *
 */
public interface I_PresentationAware {
}
